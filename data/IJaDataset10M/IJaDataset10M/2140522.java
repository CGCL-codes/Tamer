package org.tigr.cloe.model.facade.assemblerFacade;

import java.util.Properties;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;
import org.tigr.cloe.model.facade.datastoreFacade.authentication.UserCredentials;
import org.tigr.cloe.model.facade.assemblerFacade.assemblerDataConverter.*;
import org.tigr.seq.cloe.Cloe;
import org.tigr.seq.seqdata.display.AssemblyParameterDialog;
import java.io.*;

/**
 * An implementation to allow calling the
 * Tigr Assembler via cloe to close gaps or add coverage
 * locally (without calling aserver).
 *
 * This version does not support
 * the aserver console but mabye that will
 * be added in a later version.
 *
 * XML messages were created using the aserver specifications
 * as a guide
 * http://intranet.tigr.org/ifx/software/projects/assembly_server/doc/Assembly_Server_Specification.doc
 *
 * @author dkatzel
 *
 * Staus update:
 *
 * This class operates off scripts found in http://svn.jcvi.org/SE/aserver/trunk/src/
 * These scripts rely on other perl/python scripts found in http://svn.jcvi.org/SE/TIGR.App/trunk/src/
 * as well as python 3rd party libraries such as PyXML (http://sourceforge.net/projects/pyxml)
 *
 * Given that
 * 1.  The PyXML 3rd party libraries are poorly rated on SourceForge and no longer maintained
 * 2.  JCVI versions of these libraries are currently not working in CentOS (IT's current default linux OS)
 * 3.  JCVI clients are a hetergenous mix of OSs, so there are no guarantees as to what OS a particular application
 * client my be running and what perl/python environment may exist on it
 *
 * This class and the assemblerDataConverter subclasses it relies upon have been deprecated
 * because they are currently non-functional and because newer, better, more stable and reliable
 * local assembler implementation classes (i.e. the 100% java based LocalAssemblerFacadeImpl)
 * exist for these purposes
 *
 * Adam Resnick
 * 03/30/2010
 *
 */
@Deprecated
public class LocalAssemblerFacadeScriptBasedImpl implements IAssemblerFacade {

    /**
     * This is the thread that actually
     * calls Tigr_Assembler (run_TA).
     * this thread mimics aserver's aexec script.
     *
     * 1)The assembly request XML is parsed
     * and used to generate .seq, .contig .qual files.
     *
     * 2)run_TA is called to close the gap
     * 3)run_TA's asm file is converted to xml for cloe to parse.
     *
     * @author dkatzel
     *
     *
     */
    protected class AssemblerThread implements Runnable {

        protected Process currentProcess;

        protected byte[] inputXML;

        protected volatile Thread asmThread = null;

        protected boolean dead;

        private Logger logger = Cloe.getLogger();

        protected String run_TA_executable;

        protected File assemblyDataDirRoot;

        protected File assemblerInputSequenceFile;

        protected File assemblerInputQualityFile;

        protected File assemblerInputContigFile;

        protected File asmWorkingDirectory;

        protected File asmErrorFile;

        protected File asmOutputFile;

        public AssemblerThread(byte[] assemblyRequestXML) {
            inputXML = assemblyRequestXML;
            run_TA_executable = props.getProperty("run_TA_executable");
            String pathToAssemblyDataDirRoot = props.getProperty("input_gen_asmdir");
            pathToAssemblyDataDirRoot = pathToAssemblyDataDirRoot.replaceAll("REQUEST_ID", subDir);
            assemblyDataDirRoot = new File(pathToAssemblyDataDirRoot);
            assemblerInputSequenceFile = new File(assemblyDataDirRoot, "input.seq");
            assemblerInputQualityFile = new File(assemblyDataDirRoot, "input.qual");
            assemblerInputContigFile = new File(assemblyDataDirRoot, "input.contig");
            asmWorkingDirectory = new File(assemblyDataDirRoot, "asm");
            asmErrorFile = new File(asmWorkingDirectory, "input.error");
            asmOutputFile = new File(asmWorkingDirectory, "input.tasm");
        }

        public void run() {
            try {
                if (!isDead()) {
                    AssemblerInputFileConverter converter = new ScriptBasedXMLRequestInputFileConverter(assemblyDataDirRoot);
                    converter.convertXMLRequestToAssemblyInputFiles(inputXML);
                    inputXML = null;
                }
                if (!isDead()) {
                    callRun_TA();
                    currentProcess = null;
                }
                if (!isDead()) {
                    AssemblerOutputFileConverter converter = new ScriptBasedAssemblyResponseXMLConverter(assemblyDataDirRoot);
                    converter.convertAssemblyResultToXML(asmOutputFile);
                    status = 'F';
                }
                kill();
            } catch (Exception e) {
                e.printStackTrace();
                status = 'E';
                errorMessage = e.getMessage();
                kill();
            }
        }

        /**
         * Start the assembly thread to close a gap.
         *
         */
        public void start() {
            if (asmThread == null) {
                asmThread = new Thread(this, "Assemble");
                dead = false;
                status = 'R';
                currentProcess = null;
                asmThread.start();
            }
        }

        /**
         * Kill the current this thread
         * and any child process.
         *
         */
        public void kill() {
            dead = true;
            if (currentProcess != null) {
                currentProcess.destroy();
            }
        }

        protected boolean isDead() {
            return dead;
        }

        /**
         * Now that the XML file input.xml
         * has been converted to
         * .contig, .seq and .qual
         * files (like from pull_contig)
         * we can run TigrAssembler
         * to close the gap.
         *
         */
        protected void callRun_TA() throws Exception {
            short maxEnd = AssemblyParameterDialog.getInstance().getMaximumOverhang();
            if (maxEnd == AssemblyParameterDialog.ASM_PARAM_NOT_SET) {
                maxEnd = 15;
            }
            float percIdentity = AssemblyParameterDialog.getInstance().getMinimumPercentIdentity();
            if (percIdentity == AssemblyParameterDialog.ASM_PARAM_NOT_SET) {
                percIdentity = 97.5f;
            }
            short minLength = AssemblyParameterDialog.getInstance().getMinimumOverlap();
            if (minLength == AssemblyParameterDialog.ASM_PARAM_NOT_SET) {
                minLength = 40;
            }
            String command = run_TA_executable + " -local " + " -C " + assemblerInputContigFile.getAbsolutePath() + " -e " + maxEnd + " -g 8 -l " + minLength + " -p " + percIdentity + " -q " + assemblerInputQualityFile.getAbsolutePath() + " -dir " + asmWorkingDirectory.getAbsolutePath() + " " + assemblerInputSequenceFile.getAbsolutePath();
            logger.debug("Calling run_TA");
            logger.debug(command);
            currentProcess = Runtime.getRuntime().exec(command, null, new File("/"));
            InputStream stderr = currentProcess.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) logger.debug(line);
            int exitVal = currentProcess.waitFor();
            logger.debug("Process exitValue: " + exitVal);
            if (exitVal != 0) {
                String errorMessage = readTAErrorFile(asmErrorFile);
                throw new Exception("run_TA finished with errors\n" + errorMessage.toString());
            }
            br.close();
            isr.close();
        }

        protected String readTAErrorFile(File errorFile) {
            StringBuffer errorMessage = new StringBuffer();
            try {
                FileReader freader = new FileReader(errorFile);
                while (freader.ready()) {
                    errorMessage.append(freader.read());
                }
            } catch (Exception e) {
                errorMessage.append("could not parse error file");
            }
            return errorMessage.toString();
        }
    }

    protected static AssemblerThread thread;

    protected static String Configfilename;

    protected static char status;

    protected static Properties props;

    protected static long id;

    protected String errorMessage;

    protected static String subDir;

    static {
        props = new Properties();
        Configfilename = "org/tigr/cloe/model/facade/assemblerFacade/collectionAssembler.properties";
        try {
            InputStream in = LocalAssemblerFacadeScriptBasedImpl.class.getClassLoader().getResourceAsStream(Configfilename);
            if (in == null) {
                throw new Exception("file not found!");
            }
            props.load(in);
        } catch (Exception e) {
        }
    }

    public LocalAssemblerFacadeScriptBasedImpl() {
        super();
        errorMessage = null;
    }

    /**
     * start the assembly and return a mock XML message
     * with job id.  This job id is currently meaningless
     * and is only to support cloe's parser.  Since this class
     * only runs one job at a time.
     */
    public InputStream requestAssembly(byte[] assemblyRequestXML) throws AssemblerException {
        initializeAssembly();
        assemble(assemblyRequestXML);
        return createRequestAssemblyXMLResponse();
    }

    public InputStream getAssemblyStatus(byte[] statusRequestXML) throws AssemblerException {
        return createStatusAssemblyXMLResponse();
    }

    public InputStream getResultAssembly(byte[] resultRequestXML) throws AssemblerException {
        if (status != 'F') {
            throw new AssemblerException("Job did not finish, can not get result");
        }
        try {
            return createResultAssemblyXML(openOutputXMLFile());
        } catch (Exception e) {
            Cloe.getLogger().debug("error!!! " + e.getMessage());
            throw new AssemblerException("e.getMessage()");
        }
    }

    public InputStream cancelAssembly(byte[] resultRequestXML) throws AssemblerException {
        if (thread != null) {
            thread.kill();
        }
        status = 'C';
        return createCancelAssemblyXMLResponse();
    }

    /**
     * Actually generate the XML repsonse for an
     * Assembly request.
     * @return an XML string as an inputStream containing
     * the acknowledgement of the request and the job id.
     */
    protected InputStream createRequestAssemblyXMLResponse() {
        StringBuffer responseToRequest = new StringBuffer();
        responseToRequest.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        responseToRequest.append("<!DOCTYPE Response SYSTEM \"http:\\\\intranet.tigr.org\\software_docs\\xml\\aserver.dtd\">");
        responseToRequest.append("<Response Type=\"assembly\" Id=\"");
        responseToRequest.append(id);
        responseToRequest.append("\" Code=\"0\">");
        responseToRequest.append("<Message>Local Assembly request received</Message>");
        responseToRequest.append("</Response>");
        return new ByteArrayInputStream(responseToRequest.toString().getBytes());
    }

    /**
     * Create the acknowledgement of receiving a cancel
     * request.
     * @return an XML inputStream acknowledging cancel request.
     */
    protected InputStream createCancelAssemblyXMLResponse() {
        StringBuffer responseToRequest = new StringBuffer();
        responseToRequest.append("<Response Type=\"cancel\" Id=\"");
        responseToRequest.append(id);
        responseToRequest.append("\" Code=\"0\">");
        responseToRequest.append("<Message>Cancel request successful for ID</Message>");
        responseToRequest.append("</Response>");
        return new ByteArrayInputStream(responseToRequest.toString().getBytes());
    }

    /**
     * Create Assembly status update.
     * @return an XML inputStream containing the
     * status of the current job.
     */
    protected InputStream createStatusAssemblyXMLResponse() {
        StringBuffer responseToRequest = new StringBuffer();
        responseToRequest.append("<Response Type=\"status\" Id=\"");
        responseToRequest.append(id);
        responseToRequest.append("\" Code=\"0\">");
        responseToRequest.append("<Message>Status request successful for ID</Message>");
        responseToRequest.append("<Status Code=\"");
        responseToRequest.append(status);
        responseToRequest.append("\">\n");
        responseToRequest.append("<Progress>50</Progress>");
        if (status == 'E') {
            responseToRequest.append("<Error code=\"1\">");
            responseToRequest.append(errorMessage);
            responseToRequest.append("</Error>");
        }
        responseToRequest.append("</Status>");
        responseToRequest.append("</Response>");
        return new ByteArrayInputStream(responseToRequest.toString().getBytes());
    }

    /**
    * Set up all the variables
    * needed during the assembly job.
    *
    */
    protected void initializeAssembly() {
        status = 'I';
        id = 1234;
        String pathToAsmDir = props.getProperty("input_gen_asmdir");
        String username = UserCredentials.getUserCredentials().getUserName();
        subDir = username + "_" + System.currentTimeMillis();
        pathToAsmDir = pathToAsmDir.replaceFirst("REQUEST_ID", subDir);
        try {
            File dir = new File(pathToAsmDir);
            if (!dir.mkdirs()) {
                errorMessage = "Can not create " + pathToAsmDir;
                throw new Exception(errorMessage);
            }
        } catch (Exception e) {
            status = 'E';
        }
    }

    /**
     * start the assembly thread.
     *
     * @param assemblyRequestXML the XML request file from cloe.
     */
    protected void assemble(byte[] assemblyRequestXML) {
        thread = new AssemblerThread(assemblyRequestXML);
        thread.start();
    }

    /**
     * Create the XML response to cloe contianing
     * all the data cloe needs to analyze/display/save
     * the result assembly (closed gap).
     * @param outputXML this is the file that outputgen created which
     * is the conversion of the output.asm file to XML.
     * @return an inputStream object containing the outputXML file
     * wrapped in the aserver response xml object for cloe to parse.
     * @throws Exception
     */
    protected InputStream createResultAssemblyXML(InputStream outputXML) throws Exception {
        StringBuffer resultXML = new StringBuffer();
        resultXML.append("<Response Type=\"results\" Id=\"");
        resultXML.append(id);
        resultXML.append("\" Code=\"0\">\n");
        resultXML.append("<Message>Results request successful for ID</Message>\n");
        resultXML.append("<Results>\n");
        int size = outputXML.available();
        byte outputBuffer[] = new byte[size];
        outputXML.read(outputBuffer);
        resultXML.append(new String(outputBuffer));
        resultXML.append("\n");
        resultXML.append("<Status Code=\"F\">\n");
        resultXML.append("<Progress>100</Progress>\n");
        resultXML.append("</Status>\n");
        resultXML.append("</Results>\n");
        resultXML.append("</Response>\n");
        return new ByteArrayInputStream(resultXML.toString().getBytes());
    }

    protected FileInputStream openOutputXMLFile() throws Exception {
        String pathToAsmDir = props.getProperty("input_gen_asmdir");
        pathToAsmDir = pathToAsmDir.replaceAll("REQUEST_ID", subDir);
        String filename = pathToAsmDir + "/output.xml";
        Cloe.getLogger().debug("outputxml = " + filename);
        return new FileInputStream(filename);
    }
}
