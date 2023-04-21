package net.sf.reqbook.services.css;

import net.sf.reqbook.common.InternalErrorException;
import net.sf.reqbook.services.*;
import org.apache.commons.io.FileUtils;
import org.w3c.css.sac.CSSException;
import java.io.*;
import java.net.URI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * $Id: CSSProcessorImpl.java,v 1.6 2006/02/21 10:57:18 poma Exp $
 *
 * @author Pavel Sher
 */
public class CSSProcessorImpl implements CSSProcessor {

    private static final String TARGET_CSS_DIRNAME = "_css";

    private Messages messages;

    private ErrorReporter errorReporter;

    private ResourceMapper resourceMapper;

    private Set processedFiles;

    public CSSProcessorImpl() {
        this.processedFiles = new HashSet();
    }

    public void setMessages(Messages messages) {
        this.messages = messages.forPrefix("CSSProcessor.");
    }

    public void setErrorReporter(ErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
    }

    public void setResourceMapper(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }

    public String copyMainCSS(String filePath, File outputDir) throws InvalidCSSException, InternalErrorException {
        processedFiles.clear();
        File mainFile = new File(filePath).getAbsoluteFile();
        File fileDir = mainFile.getParentFile().getAbsoluteFile();
        verifySpecifiedCSSFile(mainFile);
        assert resourceMapper != null;
        resourceMapper.mapRootPath(mainFile.toURI(), new File(outputDir, TARGET_CSS_DIRNAME + File.separatorChar + mainFile.getName()).toURI());
        try {
            ResourceMapper.ResourceInfo res = resourceMapper.mapResource(fileDir.toURI(), mainFile.toURI());
            copyCSS(mainFile, new File(ServicesUtil.toURI(res.newPath)));
            return TARGET_CSS_DIRNAME + "/" + mainFile.getName();
        } catch (InvalidResourceURIException e) {
            throw new InvalidCSSException(e.getMessage());
        }
    }

    private void verifySpecifiedCSSFile(File mainFile) throws InvalidCSSException {
        if (!mainFile.exists()) throw new InvalidCSSException(messages.format("cssNotFound", mainFile.getAbsolutePath()));
        if (mainFile.isDirectory()) throw new InvalidCSSException(messages.format("cssPointsToDirectory", mainFile.getAbsolutePath()));
    }

    private void prepareDirectory(File dir) throws InvalidCSSException {
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
            if (!result) throw new InvalidCSSException(messages.format("failedToCreateDirectory", dir.getAbsolutePath()));
        }
    }

    private void copyCSS(File sourceFile, File destFile) throws InternalErrorException, InvalidCSSException {
        prepareDirectory(destFile.getParentFile());
        URI sourceFileURI = getAbsoluteURI(sourceFile);
        if (sourceFile.isFile()) {
            PrintStream ps = createPrintStream(destFile);
            if (ps == null) return;
            CSSSerializer cssSerializer = new CSSSerializer(sourceFileURI, ps, resourceMapper);
            copyFile(cssSerializer, sourceFile);
            ps.close();
            copyImportedFiles(cssSerializer.getImportedFiles());
            copyReferencedFiles(sourceFile, cssSerializer.getReferencedFiles());
        } else {
            errorReporter.warning(ErrorReporter.CSS_PROCESSOR_ORIGIN, messages.format("cssFileDoesNotExist", sourceFileURI.toString()));
        }
    }

    private PrintStream createPrintStream(File targetFile) {
        try {
            return new PrintStream(new FileOutputStream(targetFile));
        } catch (FileNotFoundException e) {
            errorReporter.warning(ErrorReporter.CSS_PROCESSOR_ORIGIN, messages.format("cssFileWriteError", targetFile.getAbsolutePath(), e.getMessage()));
            return null;
        }
    }

    private URI getAbsoluteURI(File file) {
        return file.getAbsoluteFile().toURI().normalize();
    }

    private void copyFile(CSSSerializer cssSerializer, File sourceFile) throws InternalErrorException {
        URI sourceFileURI = getAbsoluteURI(sourceFile);
        try {
            CSSParser.parseCSS(sourceFile, cssSerializer, new CSSErrorHandler(errorReporter));
        } catch (CSSException e) {
        } catch (IOException e) {
            errorReporter.warning(ErrorReporter.CSS_PROCESSOR_ORIGIN, messages.format("cssFileIOError", sourceFileURI.toString(), e.getMessage()));
        }
        processedFiles.add(sourceFileURI.toString());
    }

    private void copyImportedFiles(List importedFiles) throws InternalErrorException, InvalidCSSException {
        Iterator it = importedFiles.iterator();
        while (it.hasNext()) {
            ResourceMapper.ResourceInfo resource = (ResourceMapper.ResourceInfo) it.next();
            if (!processedFiles.contains(resource.originalPath)) {
                File origFile = new File(ServicesUtil.toURI(resource.originalPath));
                copyCSS(origFile, new File(ServicesUtil.toURI(resource.newPath)));
            }
        }
    }

    private void copyReferencedFiles(File base, List referencedFiles) throws InternalErrorException {
        Iterator it = referencedFiles.iterator();
        while (it.hasNext()) {
            ResourceMapper.ResourceInfo resource = (ResourceMapper.ResourceInfo) it.next();
            if (!processedFiles.contains(resource.originalPath)) {
                File sourceFile = new File(ServicesUtil.toURI(resource.originalPath));
                if (!sourceFile.isFile()) {
                    errorReporter.warning(ErrorReporter.CSS_PROCESSOR_ORIGIN, messages.format("referencedFileDoesNotExist", sourceFile.getAbsolutePath(), base.getAbsolutePath()));
                } else {
                    File destFile = new File(ServicesUtil.toURI(resource.newPath));
                    try {
                        FileUtils.copyFile(sourceFile, destFile);
                    } catch (IOException e) {
                        errorReporter.warning(ErrorReporter.CSS_PROCESSOR_ORIGIN, messages.format("failedToCopyReferencedFile", new String[] { sourceFile.getAbsolutePath(), base.getAbsolutePath(), destFile.getAbsolutePath(), e.getMessage() }));
                    }
                }
                processedFiles.add(resource.originalPath);
            }
        }
    }
}
