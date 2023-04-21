package com.sun.tools.javah;

import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;

/**
 * Messages, verbose and error handling support.
 *
 * For errors, the failure modes are:
 *      error -- User did something wrong
 *      bug   -- Bug has occurred in javah
 *      fatal -- We can't even find resources, so bail fast, don't localize
 *
 * <p><b>This is NOT part of any supported API.
 * If you write code that depends on this, you do so at your own
 * risk.  This code and its internal interfaces are subject to change
 * or deletion without notice.</b></p>
 */
public class Util {

    /** Exit is used to replace the use of System.exit in the original javah.
     */
    public static class Exit extends Error {

        private static final long serialVersionUID = 430820978114067221L;

        Exit(int exitValue) {
            this(exitValue, null);
        }

        Exit(int exitValue, Throwable cause) {
            super(cause);
            this.exitValue = exitValue;
            this.cause = cause;
        }

        Exit(Exit e) {
            this(e.exitValue, e.cause);
        }

        public final int exitValue;

        public final Throwable cause;
    }

    public boolean verbose = false;

    public PrintWriter log;

    public DiagnosticListener<? super JavaFileObject> dl;

    Util(PrintWriter log, DiagnosticListener<? super JavaFileObject> dl) {
        this.log = log;
        this.dl = dl;
    }

    public void log(String s) {
        log.println(s);
    }

    private ResourceBundle m;

    private void initMessages() throws Exit {
        try {
            m = ResourceBundle.getBundle("com.sun.tools.javah.resources.l10n");
        } catch (MissingResourceException mre) {
            fatal("Error loading resources.  Please file a bug report.", mre);
        }
    }

    private String getText(String key, Object... args) throws Exit {
        if (m == null) initMessages();
        try {
            return MessageFormat.format(m.getString(key), args);
        } catch (MissingResourceException e) {
            fatal("Key " + key + " not found in resources.", e);
        }
        return null;
    }

    public void usage() throws Exit {
        log.println(getText("usage"));
    }

    public void version() throws Exit {
        log.println(getText("javah.version", System.getProperty("java.version"), null));
    }

    public void bug(String key) throws Exit {
        bug(key, null);
    }

    public void bug(String key, Exception e) throws Exit {
        dl.report(createDiagnostic(Diagnostic.Kind.ERROR, key));
        dl.report(createDiagnostic(Diagnostic.Kind.NOTE, "bug.report"));
        throw new Exit(11, e);
    }

    public void error(String key, Object... args) throws Exit {
        dl.report(createDiagnostic(Diagnostic.Kind.ERROR, key, args));
        throw new Exit(15);
    }

    private void fatal(String msg) throws Exit {
        fatal(msg, null);
    }

    private void fatal(String msg, Exception e) throws Exit {
        dl.report(createDiagnostic(Diagnostic.Kind.ERROR, "", msg));
        throw new Exit(10, e);
    }

    private Diagnostic<JavaFileObject> createDiagnostic(final Diagnostic.Kind kind, final String code, final Object... args) {
        return new Diagnostic<JavaFileObject>() {

            public String getCode() {
                return code;
            }

            public long getColumnNumber() {
                return Diagnostic.NOPOS;
            }

            public long getEndPosition() {
                return Diagnostic.NOPOS;
            }

            public Kind getKind() {
                return kind;
            }

            public long getLineNumber() {
                return Diagnostic.NOPOS;
            }

            public String getMessage(Locale locale) {
                if (code.length() == 0) return (String) args[0];
                return getText(code, args);
            }

            public long getPosition() {
                return Diagnostic.NOPOS;
            }

            public JavaFileObject getSource() {
                return null;
            }

            public long getStartPosition() {
                return Diagnostic.NOPOS;
            }
        };
    }
}
