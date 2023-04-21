package org.nightlabs.print;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashDocAttributeSet;
import org.apache.log4j.Logger;
import org.nightlabs.print.DelegatingDocumentPrinterCfMod.ExternalEngineDelegateConfig;
import org.nightlabs.print.DelegatingDocumentPrinterCfMod.SystemCallDelegateConfig;
import org.nightlabs.util.IOUtil;
import org.nightlabs.util.ObservedProcess;

/**
 * PrinterInterface to print documents. This relies on the Java DocPrint API (javax.print)
 * and can be configured to use system calls or external
 * engines for specific file types (by their extension).
 *
 * See {@link DelegatingDocumentPrinterCfMod} and {@link SystemCallDelegateConfig} or
 * {@link ExternalEngineDelegateConfig} on how to configure external calls for printing.
 *
 * By default the {@link DelegatingDocumentPrinter} tries to print documents using
 * a {@link DocPrintJob} with a document flavor of {@link DocFlavor.INPUT_STREAM#AUTOSENSE}.
 *
 * @author Alexander Bieber <!-- alex [AT] nightlabs [DOT] de -->
 */
public class DelegatingDocumentPrinter implements DocumentPrinter {

    public static class Factory implements PrinterInterfaceFactory {

        public PrinterInterface createPrinterInterface() {
            return new DelegatingDocumentPrinter();
        }
    }

    private static Logger logger = Logger.getLogger(DelegatingDocumentPrinter.class);

    private static String VAR_NAME_PRINT_SERVICE = "PRINTSERVICE";

    private static String VAR_NAME_FILE = "FILE";

    private boolean waitForProcess = false;

    private boolean failOnInterruption = false;

    private boolean failOnError = true;

    private String printServiceName;

    private PrinterConfiguration configuration;

    /**
	 *
	 */
    public DelegatingDocumentPrinter() {
    }

    public void configure(PrinterConfiguration printerConfiguration) {
        logger.debug("Configuring DelegatingDocumentPrinter with " + printerConfiguration);
        if (printerConfiguration != null) {
            this.printServiceName = printerConfiguration.getPrintServiceName();
            this.configuration = (PrinterConfiguration) printerConfiguration.clone();
        }
        if (printServiceName == null) {
            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
            if (printService == null) throw new IllegalStateException("No PrintService configured and no system default PrintService could be found.");
            printServiceName = printService.getName();
        }
    }

    protected void printDocumentDelegated(File document, DocumentPrinterDelegateConfig printConfig) throws PrinterException {
        if (printConfig instanceof SystemCallDelegateConfig) printDocumentSystemCall(document, (SystemCallDelegateConfig) printConfig); else if (printConfig instanceof ExternalEngineDelegateConfig) printDocumentExternalEngine(document, (ExternalEngineDelegateConfig) printConfig); else throw new IllegalArgumentException("The DocumentPrinterDelegateConfig of type " + printConfig.getClass().getName() + " is not supported.");
    }

    protected void printDocumentExternalEngine(File document, ExternalEngineDelegateConfig printConfig) throws PrinterException {
        logger.debug("Printing file " + document + " by external engine " + printConfig.getClassName());
        if (printConfig == null) return;
        Class<?> clazz;
        try {
            clazz = Class.forName(printConfig.getClassName());
        } catch (ClassNotFoundException e) {
            PrinterException ex = new PrinterException("Could not find class for the configured external print engine: " + printConfig.getClassName());
            ex.initCause(e);
            throw ex;
        }
        if (!DocumentPrinter.class.isAssignableFrom(clazz)) throw new PrinterException("Can not use external print engine, it does not implement DocumentPrinter. ClassName is " + printConfig.getClassName());
        DocumentPrinter externalEngine;
        try {
            externalEngine = (DocumentPrinter) clazz.newInstance();
        } catch (Exception e) {
            PrinterException ex = new PrinterException("Could not instantiate external print engine: " + printConfig.getClassName());
            ex.initCause(e);
            throw ex;
        }
        logger.debug("Instantiated external engine, now configre it.");
        externalEngine.configure(configuration);
        logger.debug("Configure successfull, now printing via external engine.");
        externalEngine.printDocument(document);
    }

    /**
	 * Print the document by system call.
	 * Basically calls {@link Runtime#exec(String[])} with
	 * arguments out of the given printConfig.
	 * See {@link DelegatingDocumentPrinterCfMod.SystemCallDelegateConfig}
	 * on how this can be configured.
	 *
	 * @param document The document to print.
	 * @param printConfig The system call print configuration.
	 * @throws PrinterException When something fails.
	 */
    protected void printDocumentSystemCall(File document, SystemCallDelegateConfig printConfig) throws PrinterException {
        logger.debug("Printing by system call");
        String[] params = printConfig.getParameterPattern().split("\\s");
        for (int i = 0; i < params.length; i++) {
            if (printConfig != null) params[i] = params[i].replace("${" + VAR_NAME_PRINT_SERVICE + "}", printServiceName);
            params[i] = params[i].replace("${" + VAR_NAME_FILE + "}", document.getAbsolutePath());
        }
        String[] allCmds = new String[params.length + 1];
        allCmds[0] = printConfig.getCommandPattern();
        for (int i = 1; i < allCmds.length; i++) {
            allCmds[i] = params[i - 1];
        }
        if (logger.isDebugEnabled()) {
            logger.debug("All system call params are:");
            for (String element : allCmds) {
                logger.debug("  " + element);
            }
        }
        ObservedProcess observedProcess;
        try {
            observedProcess = new ObservedProcess(Runtime.getRuntime().exec(allCmds));
        } catch (IOException e) {
            PrinterException ex = new PrinterException("Could not invoke external print command.");
            ex.initCause(e);
            throw ex;
        }
        logger.debug("Created system call process, waiting for it to terminate");
        StringWriter err = null;
        if (failOnError) err = new StringWriter();
        StringWriter out = new StringWriter();
        int exitVal = 0;
        try {
            exitVal = observedProcess.waitForProcess(out, err);
        } catch (InterruptedException e) {
            if (failOnInterruption) throw new RuntimeException(e); else logger.warn("Interrupted waiting for print process", e);
        }
        if (exitVal != printConfig.getExpectedReturnValue()) {
            if (failOnError) {
                throw new RuntimeException("Print command failed (exit " + exitVal + ") with \n" + "Error Message: \n" + (err != null ? err.getBuffer().toString() : "null") + "\nOutput: \n" + (out != null ? out.getBuffer().toString() : "null"));
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Wait ended, output buffers are:");
            logger.debug("  Err: " + err.getBuffer().toString());
            logger.debug("  Out: " + out.getBuffer().toString());
        }
    }

    /**
	 * Print by Java INPUT_STREAM.AUTOSENSE API.
	 *
	 * @param file The file to print.
	 * @throws PrinterException When something fails.
	 */
    protected void printDocumentJavaAPI(File file) throws PrinterException {
        logger.debug("Printing by Java AUTOSENSE API.");
        PrintService printService = null;
        if (configuration != null) {
            if (configuration.getPrintServiceName() != null) printService = PrintUtil.lookupPrintService(configuration.getPrintServiceName());
        }
        if (printService == null) printService = PrintServiceLookup.lookupDefaultPrintService();
        if (printService == null) throw new PrinterException("Printing with Java API, but no print-service could be found or assigned.");
        DocPrintJob printJob = printService.createPrintJob();
        logger.debug("Created DocPrintJob");
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            PrinterException ex = new PrinterException("Could not create FileInputStream");
            ex.initCause(e);
            throw ex;
        }
        Doc doc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, new HashDocAttributeSet());
        logger.debug("Created SimpleDoc");
        try {
            printJob.print(doc, null);
        } catch (PrintException e) {
            PrinterException ex = new PrinterException("Java API DocPrintJob failed");
            ex.initCause(e);
            throw ex;
        }
        logger.debug("Printed");
    }

    /**
	 * @return the printServiceName
	 */
    public String getPrintServiceName() {
        return printServiceName;
    }

    /**
	 * @param printServiceName the printServiceName to set
	 */
    public void setPrintServiceName(String printServiceName) {
        this.printServiceName = printServiceName;
    }

    /**
	 * @return the failOnError
	 */
    public boolean isFailOnError() {
        return failOnError;
    }

    /**
	 * @param failOnError the failOnError to set
	 */
    public void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

    /**
	 * @return the failOnInterruption
	 */
    public boolean isFailOnInterruption() {
        return failOnInterruption;
    }

    /**
	 * @param failOnInterruption the failOnInterruption to set
	 */
    public void setFailOnInterruption(boolean failOnInterruption) {
        this.failOnInterruption = failOnInterruption;
    }

    /**
	 * @return the waitForProcess
	 */
    public boolean isWaitForProcess() {
        return waitForProcess;
    }

    /**
	 * @param waitForProcess the waitForProcess to set
	 */
    public void setWaitForProcess(boolean waitForProcess) {
        this.waitForProcess = waitForProcess;
    }

    public PrinterConfiguration getConfiguration() {
        return configuration;
    }

    public void printDocument(File file) throws PrinterException {
        String fileExt = IOUtil.getFileExtension(file.getName());
        DelegatingDocumentPrinterCfMod cfMod = DelegatingDocumentPrinterCfMod.sharedInstance();
        DocumentPrinterDelegateConfig printConfig = null;
        if (cfMod != null) printConfig = cfMod.getPrintConfig(fileExt);
        if (printConfig != null) {
            logger.debug("Found printConfig printing delegated");
            printDocumentDelegated(file, printConfig);
        } else {
            logger.debug("No printConfig found try to print with Java Document-Print API.");
            printDocumentJavaAPI(file);
        }
    }

    public static void main(String[] args) {
        DelegatingDocumentPrinter printer = new DelegatingDocumentPrinter();
        try {
            printer.printDocument(new File("F:\\temp\\JFire-Order.pdf"));
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }
}
