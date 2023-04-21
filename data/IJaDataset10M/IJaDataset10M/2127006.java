package com.ibatis.sqlmap.implgen.template.generated;

import com.ibatis.sqlmap.implgen.*;
import java.util.List;
import com.ibatis.sqlmap.implgen.bean.ParsedCacheModel;
import com.ibatis.sqlmap.implgen.bean.ParsedMethod;
import com.ibatis.sqlmap.implgen.bean.ParsedResultMap;
import com.ibatis.sqlmap.implgen.bean.ParsedClass;
import com.ibatis.sqlmap.implgen.bean.ParsedResult;
import com.ibatis.sqlmap.implgen.bean.ParsedParam;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.CharArrayWriter;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

public class GeneratedSqlMapInterfaceTemplate {

    public ParsedClass parsedClass;

    public List allItems;

    private static final String[] _jspDeps = new String[] { "file:/C:/java/ibatis-implgen/src/com/ibatis/sqlmap/implgen/template/SqlMapInterfaceTemplate.jsp" };

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
            out.write("\n");
            out.write("\n");
            out.write("\n");
            out.write("\n");
            out.write("\n");
            out.write("\n");
            out.write("\n");
            out.write("\n\npackage ");
            out.write(parsedClass.getPackageStr());
            out.write(";\n\nimport java.sql.SQLException;\nimport com.ibatis.sqlmap.implgen.annotations.Procedure; \n\npublic interface ");
            out.write(parsedClass.getName());
            out.write(" {\n\n");
            for (Object obj : allItems) {
                if (obj instanceof ParsedCacheModel) {
                    out.write("\n        ");
                    ParsedCacheModel model = (ParsedCacheModel) obj;
                    out.write("\n        @CacheModel(id = \"");
                    out.write(model.getId());
                    out.write("\", type=\"");
                    out.write(model.getType());
                    out.write("\", flushIntervalHours = \"");
                    out.write(model.getFlushIntervalHours());
                    out.write("\")\n    ");
                }
                if (obj instanceof ParsedResultMap) {
                    ParsedResultMap resultMap = (ParsedResultMap) obj;
                    out.write("@ResultMap(id = \"");
                    out.write(resultMap.getId());
                    out.write("\", results = {");
                    for (ParsedResult result : resultMap.getResults()) {
                        out.write("@Result(property = \"");
                        out.write(result.getProperty());
                        out.write("\", column = \"");
                        out.write(result.getColumn());
                        out.write("\", javaType = \"");
                        out.write(result.getJavaType());
                        out.write("\", jdbcType = \"");
                        out.write(result.getJdbcType());
                        out.write("\", nullValue = \"");
                        out.write(result.getNullValue());
                        out.write("\"),\n            ");
                    }
                    out.write("\n            })\n       ");
                }
                if (obj instanceof ParsedMethod) {
                    ParsedMethod method = (ParsedMethod) obj;
                    out.write("\n        ");
                    if (method.getType() == ParsedMethod.Type.DELETE) {
                        out.write("\n            @Delete\n            public void ");
                        out.write(method.getName());
                        out.write("() throws SQLException /*sql{\n\n                ");
                        out.write(method.getSql());
                        out.write("\n\n            }*/;\n        ");
                    }
                    if (method.getType() == ParsedMethod.Type.PROCEDURE) {
                        out.write("\n            @Procedure\n            public void ");
                        out.write(method.getName());
                        out.write("(");
                        boolean first = true;
                        for (ParsedParam param : method.getParams()) {
                            out.write(first ? "" : ",");
                            out.write(" ");
                            out.write(param.getJavaTypeShort());
                            out.write(" ");
                            out.write(param.getName());
                            first = false;
                        }
                        out.write(" ) throws SQLException /*sql{\n\n                ");
                        out.write(method.getSql());
                        out.write("\n\n            }*/;\n        ");
                    }
                }
            }
            out.write("\n\n\n}");
            out.flush();
        } catch (IOException e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new IOException(e.toString());
        }
    }
}
