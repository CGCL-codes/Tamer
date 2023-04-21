package org.formaria.oracle.forms.plsql.compiler;

import java.io.ByteArrayInputStream;
import org.formaria.oracle.forms.plsql.visitors.PlSqlNodeVisitor;
import org.formaria.oracle.forms.plsql.parser.exception.ParseException;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.formaria.oracle.forms.plsql.parser.*;

public class PLSqlCompiler {

    private static Logger logger = Logger.getLogger(PLSqlCompiler.class.getName());

    public static void main(String[] args) {
        try {
            URL url = PLSqlCompiler.class.getResource(args[0]);
            File f = new File(url.toURI());
            PLSqlCompiler parser = new PLSqlCompiler();
            String res = parser.compile(f);
            logger.log(Level.INFO, res);
        } catch (URISyntaxException uriSyntaxException) {
            logger.log(Level.SEVERE, "Invalid file location");
        } catch (FileNotFoundException fileNotFoundException) {
            logger.log(Level.SEVERE, "File not found");
        } catch (ParseException parseException) {
            logger.log(Level.SEVERE, "Could not parse the file");
        }
    }

    public PLSqlCompiler() {
    }

    public String compile(String src) throws ParseException {
        logger.log(Level.INFO, "Compiling: " + src);
        if (src != null) {
            ByteArrayInputStream bais = new ByteArrayInputStream(src.getBytes());
            return compile(bais);
        }
        return src;
    }

    public String compile(File f) throws ParseException, FileNotFoundException {
        InputStream is = new DataInputStream(new FileInputStream(f));
        return compile(is);
    }

    public String compile(InputStream is) throws ParseException {
        SimpleNode astRoot = null;
        FormsPlSql parser = new FormsPlSql(is);
        astRoot = parser.CompilationUnit();
        PlSqlNodeVisitor compilationUnitVisitor = new MethodCodeGenerator();
        StringBuffer javaSource = new StringBuffer("");
        astRoot.accept(compilationUnitVisitor, javaSource);
        return javaSource.toString();
    }
}
