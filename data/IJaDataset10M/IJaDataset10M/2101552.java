package de.schwarzrot.service.json;

import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import org.json.JSONException;
import org.json.JSONWriter;
import de.schwarzrot.net.http.HttpStatusCode;
import de.schwarzrot.service.http.ProtocolHandler;
import de.schwarzrot.service.support.AbstractJSONRequestHandler;

public class FileSystemHandler extends AbstractJSONRequestHandler {

    protected static final String DEFAULT_PATH_ARG = "path";

    protected static final String TARGET_PATH_ARG = "target";

    @Override
    public void process(Socket clientSocket, Map<String, String> header, Map<String, String> request) throws Exception {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
        if (!request.containsKey("c")) {
            getProtocolHandler().status(out, HttpStatusCode.BadRequest, usage());
        } else {
            boolean scriptTag = request.containsKey("callback");
            String command = request.get("c");
            if (scriptTag) out.write(request.get("callback") + "(");
            if (command.equals("q")) doQuery(out, request); else if (command.equals("b")) doTellBaseDir(out, request); else if (command.equals("c")) doCreateDir(out, request); else if (command.equals("m")) doMoveOrRename(out, request); else if (command.equals("r")) doRemove(out, request); else if (command.equals("p")) doSetReadOnly(out, request); else if (command.equals("s")) doStat(out, request); else getProtocolHandler().err404(out, command);
            if (scriptTag) out.write(");");
        }
        out.close();
    }

    @Override
    public void setup(String handlerID, ProtocolHandler ph) {
        super.setup(handlerID, ph);
        baseDir = new File(getServer().getConfig().getRecordingRoot());
        JFileChooser fc = new JFileChooser();
        fsv = fc.getFileSystemView();
    }

    @Override
    public String usage() {
        StringBuilder sb = new StringBuilder("<em>services around file system</em> - this handler ");
        if ("fsr".compareTo(getHandlerIdentifier()) == 0) {
            sb.append("cares about recording filesystem, which means, the filesystem root should be the same as " + "\"Recording-Root\" of recording manager service and job processing service.");
        } else if ("fsc".compareTo(getHandlerIdentifier()) == 0) {
            sb.append("cares about configuration filesystem, which means, the filesystem root should be the same " + "as the \"DocumentRoot\" of this service.");
        } else {
            sb.append("is loaded unintentionally!");
        }
        sb.append(" Filesystem root can be configured via application startup parameter called \"");
        sb.append(getHandlerIdentifier());
        sb.append("Root\".<dl>" + "<dt><strong>c=b</strong></dt>" + "<dd>tell root-directory of managed file-system</dd>" + "<dt><strong>c=q</strong></dt>" + "<dd>query entries of a directory - <strong>path=filename</strong> is the parameter, used to " + "specify the directory of interest.</dd>" + "<dt><strong>c=c</strong></dt>" + "<dd>create directory - <strong>path=filename</strong> is the parameter, used to specify the" + " directory of interest.</dd>" + "<dt><strong>c=m</strong></dt>" + "<dd>move/rename file or directory - <strong>path=filename</strong> " + "is the source path to rename, <strong>target=filename</strong> is the new path the file " + "should become.</dd>" + "<dt><strong>c=p</strong></dt>" + "<dd>set file readOnly - <strong>path=filename</strong> " + "is the parameter, used to specify the file that should be protected.</dd>" + "<dt><strong>c=r</strong></dt>" + "<dd>remove file or directory - <strong>path=filename</strong> " + "is the parameter, used to specify the file or directory to remove.</dd>" + "<dt><strong>c=s</strong></dt>" + "<dd>stat a file or directory - <strong>path=filename</strong> " + "is the parameter, used to specify the file to stat.</dd>" + "</dl>");
        return sb.toString();
    }

    protected JSONWriter closeResult(JSONWriter jsw) throws Exception {
        jsw.endArray().endObject();
        return jsw;
    }

    protected JSONWriter createResult(PrintWriter writer, int entryCount, String errorMessage) throws Exception {
        JSONWriter jsw = new JSONWriter(writer);
        jsw.object().key("total").value(entryCount);
        if (entryCount < 0 && errorMessage != null) {
            jsw.key("success").value(false);
            jsw.key("errors").object().key("clientCode").value(errorMessage).endObject();
            getLogger().fatal("!!! Operation FAILED !!! - " + errorMessage);
        }
        jsw.key("results").array();
        return jsw;
    }

    protected void doCreateDir(PrintWriter writer, Map<String, String> args) throws Exception {
        getLogger().info("doCreateDir()");
        if (!gotValidPath(writer, args)) return;
        File newDirectory = new File(args.get(DEFAULT_PATH_ARG));
        newDirectory.mkdirs();
        JSONWriter jsw = createResult(writer, newDirectory.exists() && newDirectory.isDirectory() ? 1 : -1, "failed to create directory");
        statFile(jsw, newDirectory);
        closeResult(jsw);
    }

    protected void doMoveOrRename(PrintWriter writer, Map<String, String> args) throws Exception {
        getLogger().info("doMoveOrRename()");
        if (!gotValidPath(writer, args)) return;
        if (!gotValidPath(writer, args, TARGET_PATH_ARG)) return;
        File curFile = new File(args.get(DEFAULT_PATH_ARG));
        File destFile = new File(args.get(TARGET_PATH_ARG));
        curFile.renameTo(destFile);
        JSONWriter jsw = createResult(writer, destFile.exists() ? 1 : -1, "failed to rename/move file");
        statFile(jsw, destFile);
        closeResult(jsw);
    }

    protected void doQuery(PrintWriter writer, Map<String, String> args) throws Exception {
        getLogger().info("doQuery()");
        if (!gotValidPath(writer, args)) return;
        File resource = new File(args.get(DEFAULT_PATH_ARG));
        JSONWriter jsw = createResult(writer, resource.exists() && resource.isDirectory() ? resource.listFiles().length : -1, "invalid path");
        if (resource.exists() && resource.isDirectory()) {
            File[] entries = resource.listFiles();
            getLogger().info("process " + entries.length + " entries of: " + resource.getAbsolutePath());
            for (File entry : entries) statFile(jsw, entry);
        }
        closeResult(jsw);
    }

    protected void doRemove(PrintWriter writer, Map<String, String> args) throws Exception {
        getLogger().info("doRemove()");
        if (!gotValidPath(writer, args)) return;
        File entry = new File(args.get(DEFAULT_PATH_ARG));
        boolean success = entry.delete();
        JSONWriter jsw = createResult(writer, success ? 1 : -1, "failed to remove path");
        closeResult(jsw);
    }

    protected void doSetReadOnly(PrintWriter writer, Map<String, String> args) throws Exception {
        getLogger().info("doSetReadOnly()");
        if (!gotValidPath(writer, args)) return;
        File entry = new File(args.get(DEFAULT_PATH_ARG));
        JSONWriter jsw = createResult(writer, entry.exists() ? 1 : -1, "path does not exist");
        entry.setReadOnly();
        statFile(jsw, entry);
        closeResult(jsw);
    }

    protected void doStat(PrintWriter writer, Map<String, String> args) throws Exception {
        getLogger().info("doStat()");
        if (!gotValidPath(writer, args)) return;
        File entry = new File(args.get(DEFAULT_PATH_ARG));
        JSONWriter jsw = createResult(writer, entry.exists() ? 1 : -1, "path does not exist");
        statFile(jsw, entry);
        closeResult(jsw);
    }

    protected void doTellBaseDir(PrintWriter writer, Map<String, String> args) throws Exception {
        getLogger().info("doQuery()");
        JSONWriter jsw = new JSONWriter(writer);
        jsw.object().key("total").value(1).key("results").array();
        jsw.object().key("base").value(baseDir.getAbsolutePath()).endObject();
        jsw.endArray().endObject();
    }

    protected boolean gotValidPath(PrintWriter writer, Map<String, String> args) throws Exception {
        return gotValidPath(writer, args, DEFAULT_PATH_ARG);
    }

    protected boolean gotValidPath(PrintWriter writer, Map<String, String> args, String propertyName) throws Exception {
        if (args == null) return replySuccess(writer, HttpStatusCode.BadRequest, "missing " + propertyName + " argument!");
        if (args.containsKey(propertyName)) {
            if (!args.get(propertyName).startsWith(baseDir.getAbsolutePath())) return replySuccess(writer, HttpStatusCode.BadRequest, "invalid " + propertyName + " argument!");
            return true;
        } else return replySuccess(writer, HttpStatusCode.BadRequest, "missing " + propertyName + " argument!");
    }

    protected void statFile(JSONWriter jsw, File entry) throws JSONException {
        jsw.object();
        jsw.key("name").value(entry.getName());
        jsw.key("t").value(entry.isDirectory() ? "D" : "F");
        jsw.key("s").value(entry.isDirectory() ? 0 : entry.length());
        jsw.key("tm").value(entry.lastModified() / 1000);
        jsw.key("h").value(fsv.isHiddenFile(entry));
        jsw.key("w").value(entry.canWrite());
        jsw.key("r").value(entry.canRead());
        jsw.key("x").value(fsv.isTraversable(entry));
        jsw.endObject();
    }

    private File baseDir = new File("/var/video0");

    private FileSystemView fsv;
}
