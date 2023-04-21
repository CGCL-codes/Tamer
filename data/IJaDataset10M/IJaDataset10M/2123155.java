package org.slf4j.migrator.line;

import org.slf4j.migrator.line.JCLRuleSet;
import org.slf4j.migrator.line.LineConverter;
import junit.framework.TestCase;

public class JCLRuleSetTest extends TestCase {

    LineConverter jclConverter = new LineConverter(new JCLRuleSet());

    public void testImportReplacement() {
        assertEquals("import org.slf4j.LoggerFactory;", jclConverter.getOneLineReplacement("import org.apache.commons.logging.LogFactory;"));
        assertEquals("import org.slf4j.Logger;", jclConverter.getOneLineReplacement("import org.apache.commons.logging.Log;"));
    }

    public void testLogFactoryGetLogReplacement() {
        assertEquals("  Logger   l = LoggerFactory.getLogger(MyClass.class);", jclConverter.getOneLineReplacement("  Log   l = LogFactory.getLog(MyClass.class);"));
        assertEquals("public Logger mylog=LoggerFactory.getLogger(MyClass.class);", jclConverter.getOneLineReplacement("public Log mylog=LogFactory.getLog(MyClass.class);"));
        assertEquals("public static Logger mylog1 = LoggerFactory.getLogger(MyClass.class);", jclConverter.getOneLineReplacement("public static Log mylog1 = LogFactory.getLog(MyClass.class);"));
        assertEquals("public static Logger mylog1 = LoggerFactory.getLogger(MyClass.class); //logger instanciation and declaration", jclConverter.getOneLineReplacement("public static Log mylog1 = LogFactory.getLog(MyClass.class); //logger instanciation and declaration"));
        assertEquals(" myLog = LoggerFactory.getLogger(MyClass.class);//logger instanciation", jclConverter.getOneLineReplacement(" myLog = LogFactory.getLog(MyClass.class);//logger instanciation"));
        assertEquals("//public static Logger mylog1 = LoggerFactory.getLogger(MyClass.class);", jclConverter.getOneLineReplacement("//public static Log mylog1 = LogFactory.getLog(MyClass.class);"));
        assertEquals("// myLog = LoggerFactory.getLogger(MyClass.class);//logger instanciation", jclConverter.getOneLineReplacement("// myLog = LogFactory.getLog(MyClass.class);//logger instanciation"));
    }

    public void testLogFactoryGetFactoryReplacement() {
        assertEquals("Logger l = LoggerFactory.getLogger(MyClass.class);", jclConverter.getOneLineReplacement("Log l = LogFactory.getFactory().getInstance(MyClass.class);"));
        assertEquals("public Logger mylog=LoggerFactory.getLogger(MyClass.class);", jclConverter.getOneLineReplacement("public Log mylog=LogFactory.getFactory().getInstance(MyClass.class);"));
        assertEquals("public static Logger mylog1 = LoggerFactory.getLogger(MyClass.class);", jclConverter.getOneLineReplacement("public static Log mylog1 = LogFactory.getFactory().getInstance(MyClass.class);"));
        assertEquals("public static Logger mylog1 = LoggerFactory.getLogger(MyClass.class); //logger instanciation and declaration", jclConverter.getOneLineReplacement("public static Log mylog1 = LogFactory.getFactory().getInstance(MyClass.class); //logger instanciation and declaration"));
        assertEquals(" myLog = LoggerFactory.getLogger(MyClass.class);//logger instanciation", jclConverter.getOneLineReplacement(" myLog = LogFactory.getFactory().getInstance(MyClass.class);//logger instanciation"));
        assertEquals("//public static Logger mylog1 = LoggerFactory.getLogger(MyClass.class);", jclConverter.getOneLineReplacement("//public static Log mylog1 = LogFactory.getFactory().getInstance(MyClass.class);"));
        assertEquals("// myLog = LoggerFactory.getLogger(MyClass.class);//logger instanciation", jclConverter.getOneLineReplacement("// myLog = LogFactory.getFactory().getInstance(MyClass.class);//logger instanciation"));
    }

    public void testLogDeclarationReplacement() {
        assertEquals("Logger mylog;", jclConverter.getOneLineReplacement("Log mylog;"));
        assertEquals("private Logger mylog;", jclConverter.getOneLineReplacement("private Log mylog;"));
        assertEquals("public static final Logger myLog;", jclConverter.getOneLineReplacement("public static final Log myLog;"));
        assertEquals("public Logger myLog;//logger declaration", jclConverter.getOneLineReplacement("public Log myLog;//logger declaration"));
        assertEquals("//private Logger myLog;", jclConverter.getOneLineReplacement("//private Log myLog;"));
    }

    public void testMultiLineReplacement() {
        assertEquals("protected Logger log =", jclConverter.getOneLineReplacement("protected Log log ="));
        assertEquals(" LoggerFactory.getLogger(MyComponent.class);", jclConverter.getOneLineReplacement(" LogFactory.getLog(MyComponent.class);"));
        assertEquals("protected Logger log ", jclConverter.getOneLineReplacement("protected Log log "));
        assertEquals(" = LoggerFactory.getLogger(MyComponent.class);", jclConverter.getOneLineReplacement(" = LogFactory.getFactory().getInstance(MyComponent.class);"));
    }
}
