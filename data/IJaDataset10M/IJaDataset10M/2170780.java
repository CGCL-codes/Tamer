package cn.aprilsoft.TinyAppServer.jssp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.SocketException;
import java.util.Scanner;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import org.apache.log4j.Logger;
import cn.aprilsoft.TinyAppServer.configuare.Configuare;
import cn.aprilsoft.TinyAppServer.exception.ClientAbortException;
import cn.aprilsoft.TinyAppServer.exception.ServerException;
import cn.aprilsoft.TinyAppServer.mailer.AppMailer;
import cn.aprilsoft.TinyAppServer.reqNrep.Request;
import cn.aprilsoft.TinyAppServer.reqNrep.Response;
import cn.aprilsoft.TinyAppServer.session.Session;
import cn.aprilsoft.io.FileUtil;
import cn.aprilsoft.io.IOUtil;
import cn.aprilsoft.lang.EnvTool;
import cn.aprilsoft.lang.StringUtil;

public class JsspExecutor {

    private static final String JSSP_SCRIPT_LANGUAGE = "JavaScript";

    private static final Logger JSSP_LOG = Logger.getLogger("JsspLog");

    private static final ScriptEngineManager JSSP_ENG_MAN = new ScriptEngineManager();

    public static void clearJsspWork() {
        File work = new File(Configuare.WORK_PATH);
        if (work.exists() && work.isDirectory()) {
            FileUtil.removeFilesEndsWith(work, Configuare.EXPLAINED_EXT);
            FileUtil.removeFilesEndsWith(work, Configuare.COMPILED_EXT);
        } else {
            work.mkdirs();
        }
    }

    public static File explainJssp(File file) throws IOException {
        File explainedFile = new File(getExplainedFileName(file));
        if (explainedFile.exists()) {
            if (explainedFile.lastModified() > file.lastModified()) {
                return explainedFile;
            }
        }
        Scanner scanner = new Scanner(file, "UTF-8");
        try {
            PrintWriter pw = new PrintWriter(explainedFile, "UTF-8");
            pw.write("(function() {\r\n");
            StringBuilder sb = new StringBuilder();
            boolean inScript = false;
            boolean isStart = false;
            boolean isEnd = false;
            try {
                while (scanner.hasNext()) {
                    String line = scanner.nextLine() + "\\r\\n";
                    for (int i = 0; i < line.length(); i++) {
                        char c = line.charAt(i);
                        if (inScript) {
                            if ((c == '%') && (i < (line.length() - 1)) && (line.charAt(i + 1) == '>')) {
                                isStart = false;
                                isEnd = true;
                                inScript = false;
                                i += 1;
                            } else {
                                isStart = false;
                                isEnd = false;
                            }
                        } else {
                            if ((c == '<') && (i < (line.length() - 1)) && (line.charAt(i + 1) == '%')) {
                                isStart = true;
                                isEnd = false;
                                inScript = true;
                                i += 1;
                            } else {
                                isStart = false;
                                isEnd = false;
                            }
                        }
                        boolean process = false;
                        if (isStart || isEnd) {
                            process = true;
                        } else {
                            sb.append(c);
                            if (i == (line.length() - 1)) {
                                process = true;
                            }
                        }
                        if (process) {
                            String sbs = sb.toString();
                            sb = new StringBuilder();
                            if (isStart) {
                                if (sbs.length() > 0) {
                                    explainPlain(pw, sbs);
                                }
                            } else if (isEnd) {
                                if (sbs.length() > 0) {
                                    explainScript(pw, sbs);
                                }
                            } else if (i == (line.length() - 1)) {
                                if (!inScript) {
                                    explainPlain(pw, sbs);
                                } else {
                                    explainScript(pw, sbs);
                                }
                            } else if (line.length() == 0) {
                                if (!inScript) {
                                    explainPlain(pw, sbs);
                                } else {
                                    explainScript(pw, sbs);
                                }
                            }
                        }
                    }
                }
                pw.write("})();\r\n");
                pw.flush();
            } finally {
                pw.close();
            }
        } finally {
            scanner.close();
        }
        return explainedFile;
    }

    public static void executeJssp(File file, Request request, Response response) {
        File explained;
        try {
            explained = explainJssp(file);
        } catch (IOException e) {
            throw new ServerException(e);
        }
        executeExplained(explained, request, response);
    }

    public static void executeExplained(File file, Request request, Response response) {
        ScriptEngine se = JSSP_ENG_MAN.getEngineByName(JSSP_SCRIPT_LANGUAGE);
        ByteArrayOutputStream cache = new ByteArrayOutputStream();
        BufferWriter out = new BufferWriter(cache, request.getCharset());
        Session session = request.getSession();
        Bindings b = new SimpleBindings();
        b.put("envTool", EnvTool.INSTANCE);
        b.put("log", JSSP_LOG);
        b.put("mailer", AppMailer.getMailer());
        b.put("request", request);
        b.put("session", session);
        b.put("response", response);
        b.put("out", out);
        try {
            se.eval(FileUtil.readFileToString(file, "UTF-8"), b);
            out.flush();
            if (response.getLocation() != null) {
                response.flush();
                return;
            }
            InputStream is = new ByteArrayInputStream(cache.toByteArray());
            if (is.available() > 0) {
                response.setContentSize(is.available());
                IOUtil.copy(is, response.getOutputStream());
            }
        } catch (SocketException e) {
            throw new ClientAbortException();
        } catch (IOException e) {
            throw new ServerException(e);
        } catch (ScriptException e) {
            throw new ServerException(e);
        }
    }

    private static final String getExplainedFileName(File file) {
        String explained = file.getPath() + Configuare.EXPLAINED_EXT;
        explained = explained.replaceAll("[:\\\\/]", "_");
        explained = Configuare.WORK_PATH + File.separator + explained;
        return explained;
    }

    private static final void explainPlain(Writer pw, String plain) throws IOException {
        plain = plain.replace("\"", "\\\"");
        plain = "out.write(\"" + plain + "\");";
        pw.write(plain);
        pw.write("\r\n");
    }

    private static final void explainScript(Writer pw, String script) throws IOException {
        if (script.endsWith("\\r\\n")) {
            script = script.substring(0, (script.length() - "\\r\\n".length()));
        }
        if (script.length() == 0) {
            return;
        }
        if (script.trim().startsWith("=")) {
            int equal = script.indexOf('=');
            String expression = script.substring(equal + 1);
            pw.write("out.writeEscape(");
            pw.write(expression);
            pw.write(");");
        } else if (script.trim().startsWith("!=")) {
            int equal = script.indexOf('!');
            String expression = script.substring(equal + 2);
            pw.write("out.write(");
            pw.write(expression);
            pw.write(");");
        } else {
            pw.write(script);
        }
        pw.write("\r\n");
    }
}

class BufferWriter {

    private PrintWriter pw;

    public BufferWriter(OutputStream out, String encoding) {
        try {
            this.pw = new PrintWriter(new OutputStreamWriter(out, encoding));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeEscape(Object o) {
        pw.write(StringUtil.escapeHtml(objectToString(o)));
    }

    public void write(Object o) {
        pw.write(objectToString(o));
    }

    public void flush() {
        pw.flush();
    }

    public void close() {
        pw.close();
    }

    private String objectToString(Object o) {
        if (o instanceof Double) {
            double doubleValue = ((Double) o).doubleValue();
            if (Double.compare(Math.ceil(doubleValue), Math.floor(doubleValue)) == 0) {
                o = Double.valueOf(doubleValue).longValue();
            }
        }
        return ((o == null) ? "" : o.toString());
    }
}
