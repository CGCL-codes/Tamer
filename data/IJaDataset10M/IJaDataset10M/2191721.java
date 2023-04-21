package com.googlecode.voctopus.request.handler;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.googlecode.voctopus.RequestResponseMediator.ReasonPhase;
import com.googlecode.voctopus.config.EnvironmentVariablesBuilder;
import com.googlecode.voctopus.config.VOctopusConfigurationManager;
import com.googlecode.voctopus.request.AbstractHttpRequest;
import com.googlecode.voctopus.request.AbstractHttpRequest.RequestMethodType;
import com.googlecode.voctopus.request.HttpRequest;

public class ScriptRequestHandlerStrategy extends AbstractRequestHandler {

    private static final Logger logger = Logger.getLogger(ScriptRequestHandlerStrategy.class);

    /**
     * The request parameters from the request to be used on the script.
     */
    private Map<String, String> requestParameters;

    /**
     * The lines generated from the script. If the file is binary, get the stream.//TODO STILL TO IMPLEMENT SCRIPT
     * BYNARY
     */
    private String[] scriptsLines;

    private HttpRequest request;

    public ScriptRequestHandlerStrategy(Map<String, String> requestParameters, URI uri, File requestedFile, String handlerFound, ReasonPhase status, HttpRequest request) {
        super(uri, requestedFile, RequestType.ASCII, handlerFound);
        this.status = status;
        this.requestParameters = requestParameters;
        this.request = request;
        try {
            logger.debug("Handling Script '" + uri + "' -> '" + requestedFile + "'");
            executeScriptToGetResponseLines();
        } catch (IOException e) {
            logger.debug("Unexpected error while executing the script '" + requestedFile + "'");
        }
    }

    /**
     * @param uri the requested URI.
     * @return The script path for a given URI that's supposed to be an alias.
     */
    public static File getScriptPathForAlias(URI uri) {
        if (uri.getPath().equals("/")) {
            return null;
        }
        if (uri.getPath().equals("/cgi-bin/")) {
            return new File(uri.getPath());
        }
        String aliasTest = "/" + uri.getPath().split("/")[1] + "/";
        if (!uri.getPath().startsWith("/cgi-bin/")) {
            if (VOctopusConfigurationManager.getScriptAlias().get(aliasTest) == null) {
                return null;
            }
        }
        String scriptPath = VOctopusConfigurationManager.getScriptAlias().get(uri.getPath());
        if (scriptPath != null) {
            return new File(scriptPath);
        }
        if (uri.getPath().split("/").length == 1) {
            return null;
        } else {
            String alias = "/" + uri.getPath().split("/")[1] + "/";
            scriptPath = uri.getPath().replace(alias, "/cgi-bin/");
            scriptPath = VOctopusConfigurationManager.getInstance().getServerRootPath() + scriptPath;
            File file = new File(scriptPath);
            if (file.exists()) {
                return file;
            } else {
                return null;
            }
        }
    }

    /**
     * Performs the execution of the script
     * 
     * @throws IOException
     */
    private void executeScriptToGetResponseLines() throws IOException {
        logger.debug("Ready to execute the script " + this.getRequestedResource().getPath());
        FileChannel channel = new FileInputStream(this.getRequestedFile()).getChannel();
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, this.getRequestedFile().length());
        Charset charset = Charset.forName("ISO-8859-1");
        CharsetDecoder decoder = charset.newDecoder();
        List<String> lines = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();
        if (this.wasErrorOnRequestBeforeScriptExecution()) {
            logger.debug("Handler error: executing the script with " + this.getClass().getSimpleName());
            channel = new FileInputStream(this.getRequestedFile()).getChannel();
            buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, this.getRequestedFile().length());
            CharBuffer charBuffer = decoder.decode(buffer);
            for (int i = 0, n = charBuffer.length(); i < n; i++) {
                char charValue = charBuffer.get();
                if (charValue != '\n') {
                    builder.append(charValue);
                } else {
                    lines.add(builder.toString().replace("$SERVER_ADMIN", VOctopusConfigurationManager.WebServerProperties.HTTPD_CONF.getPropertyValue("ServerAdmin")).replace("$REQUESTED_RESOURCE", this.getRequestedResource().getPath()));
                    builder.delete(0, builder.capacity());
                }
            }
            this.scriptsLines = lines.toArray(new String[lines.size()]);
        } else if (getScriptPathForAlias(this.request.getUri()) != null) {
            String[] args = null;
            if (this.requestParameters != null) {
                args = new String[this.requestParameters.size()];
                int i = -1;
                for (String arg : this.requestParameters.keySet()) {
                    args[++i] = arg + "=" + this.requestParameters.get(arg);
                }
            }
            try {
                this.scriptsLines = this.getCgiExecutionResponse(args);
                if (this.scriptsLines[0].toLowerCase().contains("content-type:")) {
                    this.contentType = this.scriptsLines[0].split(" ")[1].trim();
                } else {
                    this.contentType = "text/plain";
                }
                this.requestType = RequestType.ASCII;
                this.status = ReasonPhase.STATUS_200;
            } catch (CgiExecutionException e) {
                this.status = ReasonPhase.STATUS_500;
                this.contentType = "text/html";
                this.requestType = RequestType.ASCII;
                File error500 = VOctopusConfigurationManager.get500ErrorFile();
                System.out.println("Request generated a 500: handler chose " + error500.getPath());
                channel = new FileInputStream(error500).getChannel();
                buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, error500.length());
                CharBuffer charBuffer = decoder.decode(buffer);
                for (int i = 0, n = charBuffer.length(); i < n; i++) {
                    char charValue = charBuffer.get();
                    if (charValue != '\n') {
                        builder.append(charValue);
                    } else {
                        String complete = builder.toString();
                        String changed = complete.replace("$REQUESTED_RESOURCE", this.getRequestedResource().getPath()).replace("$REASON", e.getMessage());
                        lines.add(changed);
                        builder.delete(0, builder.capacity());
                    }
                }
                this.scriptsLines = lines.toArray(new String[lines.size()]);
            }
        }
    }

    /**
     * Builds a ProcessBuilder with the based on the arguments
     * 
     * @param arguments is the O.S. arguments (program plus arguments)
     * @return a new ProcessBuilder with the correct information with updated environment variables
     */
    private ProcessBuilder buildProcess(String... arguments) {
        ProcessBuilder pb = new ProcessBuilder(arguments);
        EnvironmentVariablesBuilder envBuilder = EnvironmentVariablesBuilder.createNew(this.request);
        pb.environment().putAll(envBuilder.getAllEn());
        return pb;
    }

    /**
     * @param cgiArguments the arguments sent on the URI, that is, the query string
     * @return the list of the lines from the process response.
     * @throws IOException if any problem reading from the process occurs.
     */
    private String[] getCgiExecutionResponse(String[] cgiArguments) throws CgiExecutionException, IOException {
        List<String> processArgs = new ArrayList<String>();
        String path = this.getRequestedResource().getPath();
        String fileExtention = path.substring(path.lastIndexOf("."));
        processArgs.add(VOctopusConfigurationManager.getExecutor(fileExtention));
        processArgs.add(this.getRequestedFile().getAbsolutePath());
        if (cgiArguments != null) {
            for (String arg : cgiArguments) {
                processArgs.add(arg);
            }
        }
        Process process = this.buildProcess((String[]) processArgs.toArray(new String[processArgs.size()])).start();
        String additionalData = ((AbstractHttpRequest) this.request).getAdditionalHeaderData();
        if (this.request.getMethodType().equals(RequestMethodType.POST) && additionalData != null) {
            OutputStreamWriter outProcess = new OutputStreamWriter(new BufferedOutputStream(process.getOutputStream()));
            outProcess.write(additionalData);
            outProcess.close();
        }
        List<String> lines = new ArrayList<String>();
        InputStreamReader reader;
        int scriptResult = -1;
        try {
            scriptResult = process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.setStatus(ReasonPhase.STATUS_200);
        if (scriptResult == 1) {
            reader = new InputStreamReader(process.getErrorStream());
            if (Boolean.valueOf(VOctopusConfigurationManager.WebServerProperties.HTTPD_CONF.getPropertyValue("FAIL_SCRIPT_ON_ERROR"))) {
                this.setStatus(ReasonPhase.STATUS_500);
                BufferedReader buffer = new BufferedReader(reader);
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = buffer.readLine()) != null) {
                    builder.append(line + "\n\r");
                }
                throw new CgiExecutionException(builder.toString());
            }
        } else {
            reader = new InputStreamReader(process.getInputStream());
        }
        if (this.getStatus().equals(ReasonPhase.STATUS_200)) {
            BufferedReader buffer = new BufferedReader(reader);
            String line;
            while ((line = buffer.readLine()) != null) {
                lines.add(line);
            }
            return lines.toArray(new String[lines.size()]);
        } else {
            return null;
        }
    }

    public String[] getResourceLines() throws IOException {
        return this.scriptsLines;
    }

    @Override
    public boolean isRequestedResourceBinary() {
        return !this.contentType.startsWith("text/");
    }

    /**
     * @return Verifies if the request status has a value of {@link ReasonPhase#STATUS_404} or
     *         {@link ReasonPhase#STATUS_403} || {@link ReasonPhase#STATUS_500} || {@link ReasonPhase#STATUS_401}
     */
    private boolean wasErrorOnRequestBeforeScriptExecution() {
        return this.status.equals(ReasonPhase.STATUS_404) || this.status.equals(ReasonPhase.STATUS_403) || this.status.equals(ReasonPhase.STATUS_401);
    }

    public String[] getParticularResponseHeaders() {
        return null;
    }
}
