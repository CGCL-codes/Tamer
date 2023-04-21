package com.ibatis.sqlmap.implgen.template.generated;

import com.ibatis.sqlmap.implgen.bean.ParsedParam;
import com.ibatis.sqlmap.implgen.bean.ParsedProc;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.CharArrayWriter;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

public class GeneratedProcDocTemplate {

    public ParsedProc[] procs;

    public String name;

    private static final String[] _jspDeps = new String[] { "file:/C:/java/ibatis-implgen/src/com/ibatis/sqlmap/implgen/template/ProcDocTemplate.jsp" };

    public final String[] _jspGetDeps() {
        return _jspDeps;
    }

    public final long _jspGetCompilerVersion() {
        return 1L;
    }

    public void writeToFile(File f) throws IOException {
        write(new FileWriter(f));
    }

    public void writeToStream(OutputStream myStream) throws IOException {
        write(new PrintWriter(myStream));
    }

    public String writeToString() throws IOException {
        CharArrayWriter caw = new CharArrayWriter();
        write(caw);
        return caw.toString();
    }

    class JspWriter extends Writer {

        Writer originalWriter;

        public JspWriter(Writer originalWriter) {
            this.originalWriter = originalWriter;
        }

        public void write(String str) throws IOException {
            if (str == null) str = "";
            super.write(str);
        }

        public void write(char cbuf[], int off, int len) throws IOException {
            if (cbuf == null) cbuf = "".toCharArray();
            originalWriter.write(cbuf, off, len);
        }

        public void flush() throws IOException {
            originalWriter.flush();
        }

        public void close() throws IOException {
            originalWriter.close();
        }
    }

    public void write(Writer originalWriter) throws IOException {
        final Writer out = new JspWriter(originalWriter);
        try {
            out.write("\n");
            out.write("\n");
            out.write("\n<html>\n<head>\n    <title>Stored Procedures in ");
            out.write(name);
            out.write("</title>\n    <style type=\"text/css\">\n        .params td {\n            width: 200px;\n            border: 1px solid #eeeeee;\n        }\n    </style>\n</head>\n<body>\n\n<h1>Stored Procedures in ");
            out.write(name);
            out.write("</h1>\n\n");
            if (procs != null) {
                for (ParsedProc proc : procs) {
                    out.write("\n<a name=\"");
                    out.write(proc.getName());
                    out.write("\"></a>\n\n<h2>");
                    out.write(proc.getName());
                    out.write("\n</h2>\n");
                    if (proc.isVersion()) {
                        out.write("\n<small>Version: ");
                        out.write(proc.getVersion());
                        out.write("\n</small>\n");
                    }
                    out.write("\n");
                    if (proc.isAuthor()) {
                        out.write("\n<small>Author: \" + proc.getAuthor() + \"</small>\n");
                    }
                    out.write("\n<p>");
                    out.write(proc.getDescription());
                    out.write("\n</p>\n\n");
                    if (proc.isAnyParams()) {
                        out.write("\n\n<h3>Parameters</h3>\n<table class=\"params\">\n\n    ");
                        for (int j = 0; j < proc.getParams().length; j++) {
                            ParsedParam param = proc.getParams()[j];
                            out.write("\n    <tr>\n        <td><b>");
                            out.write(param.getName());
                            out.write("</b></td>\n        <td>");
                            out.write(param.getSqlType());
                            out.write("</td>\n        <td>");
                            out.write(param.getDescription());
                            out.write("</td>\n    </tr>\n    ");
                        }
                        out.write("\n</table>\n");
                    }
                    out.write("\n\n");
                    if (proc.isReturnDesc()) {
                        out.write("\n    <h3>Return</h3>\n    <p>");
                        out.write(proc.getReturnDesc());
                        out.write("</p>\n");
                    }
                    out.write("\n<hr/>\n\n");
                }
            }
            out.write("\n\n</body>\n</html>\n");
            out.flush();
        } catch (IOException e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new IOException(e.toString());
        }
    }
}
