package com.loribel.commons.swing.editor;

import java.util.Hashtable;
import java.util.Map;
import org.syntax.jedit.tokenmarker.BatchFileTokenMarker;
import org.syntax.jedit.tokenmarker.CCTokenMarker;
import org.syntax.jedit.tokenmarker.CTokenMarker;
import org.syntax.jedit.tokenmarker.EiffelTokenMarker;
import org.syntax.jedit.tokenmarker.HTMLTokenMarker;
import org.syntax.jedit.tokenmarker.IDLTokenMarker;
import org.syntax.jedit.tokenmarker.JavaScriptTokenMarker;
import org.syntax.jedit.tokenmarker.JavaTokenMarker;
import org.syntax.jedit.tokenmarker.MakefileTokenMarker;
import org.syntax.jedit.tokenmarker.PHPTokenMarker;
import org.syntax.jedit.tokenmarker.PLSQLTokenMarker;
import org.syntax.jedit.tokenmarker.PatchTokenMarker;
import org.syntax.jedit.tokenmarker.PerlTokenMarker;
import org.syntax.jedit.tokenmarker.PropsTokenMarker;
import org.syntax.jedit.tokenmarker.PythonTokenMarker;
import org.syntax.jedit.tokenmarker.ShellScriptTokenMarker;
import org.syntax.jedit.tokenmarker.TSQLTokenMarker;
import org.syntax.jedit.tokenmarker.TeXTokenMarker;
import org.syntax.jedit.tokenmarker.TokenMarker;
import org.syntax.jedit.tokenmarker.XMLTokenMarker;

/**
 * Editor for Java.
 */
public final class GB_TokenMarkerUtils {

    private static TokenMarker[] tokenMarkers;

    private static String[] tokenNames;

    private static String[] favorisNames;

    private static Map tokenMarkerMap;

    public static class TOKEN_MARKER {

        public static final TokenMarker BATCH = new BatchFileTokenMarker();

        public static final TokenMarker C = new CTokenMarker();

        public static final TokenMarker CC = new CCTokenMarker();

        public static final TokenMarker EIFFEL = new EiffelTokenMarker();

        public static final TokenMarker HTML = new HTMLTokenMarker();

        public static final TokenMarker IDL = new IDLTokenMarker();

        public static final TokenMarker JAVA = new JavaTokenMarker();

        public static final TokenMarker JAVA_SCRIPT = new JavaScriptTokenMarker();

        public static final TokenMarker MAKEFILE = new MakefileTokenMarker();

        public static final TokenMarker PATCH = new PatchTokenMarker();

        public static final TokenMarker PERL = new PerlTokenMarker();

        public static final TokenMarker PHP = new PHPTokenMarker();

        public static final TokenMarker PLSQL = new PLSQLTokenMarker();

        public static final TokenMarker PROPS = new PropsTokenMarker();

        public static final TokenMarker PYTHON = new PythonTokenMarker();

        public static final TokenMarker SHELL = new ShellScriptTokenMarker();

        public static final TokenMarker TEX = new TeXTokenMarker();

        public static final TokenMarker TSQL = new TSQLTokenMarker();

        public static final TokenMarker XML = new XMLTokenMarker();
    }

    public static class TOKEN_NAME {

        public static final String BATCH = "Batch";

        public static final String C = "C";

        public static final String CC = "C++";

        public static final String EIFFEL = "Eiffel";

        public static final String HTML = "HTML";

        public static final String IDL = "IDL";

        public static final String JAVA = "Java";

        public static final String JAVA_SCRIPT = "Java Script";

        public static final String MAKEFILE = "Makefile";

        public static final String PATCH = "Patch";

        public static final String PERL = "Perl";

        public static final String PHP = "PHP";

        public static final String PLSQL = "PL-SQL";

        public static final String PROPS = "Props";

        public static final String PYTHON = "Python";

        public static final String SHELL = "Shell Script";

        public static final String TEX = "TeX";

        public static final String TSQL = "TSQL";

        public static final String XML = "XML";
    }

    public static TokenMarker[] getTokenMarkers() {
        if (tokenMarkers == null) {
            int i = 0;
            tokenMarkers = new TokenMarker[19];
            tokenMarkers[i++] = TOKEN_MARKER.JAVA;
            tokenMarkers[i++] = TOKEN_MARKER.XML;
            tokenMarkers[i++] = TOKEN_MARKER.HTML;
            tokenMarkers[i++] = TOKEN_MARKER.JAVA_SCRIPT;
            tokenMarkers[i++] = TOKEN_MARKER.PROPS;
            tokenMarkers[i++] = TOKEN_MARKER.BATCH;
            tokenMarkers[i++] = TOKEN_MARKER.C;
            tokenMarkers[i++] = TOKEN_MARKER.CC;
            tokenMarkers[i++] = TOKEN_MARKER.PLSQL;
            tokenMarkers[i++] = TOKEN_MARKER.PERL;
            tokenMarkers[i++] = TOKEN_MARKER.PHP;
            tokenMarkers[i++] = TOKEN_MARKER.MAKEFILE;
            tokenMarkers[i++] = TOKEN_MARKER.EIFFEL;
            tokenMarkers[i++] = TOKEN_MARKER.IDL;
            tokenMarkers[i++] = TOKEN_MARKER.PATCH;
            tokenMarkers[i++] = TOKEN_MARKER.PYTHON;
            tokenMarkers[i++] = TOKEN_MARKER.SHELL;
            tokenMarkers[i++] = TOKEN_MARKER.TEX;
            tokenMarkers[i++] = TOKEN_MARKER.TSQL;
        }
        return tokenMarkers;
    }

    public static String[] getTokenNames() {
        if (tokenMarkers == null) {
            int i = 0;
            tokenNames = new String[19];
            tokenNames[i++] = TOKEN_NAME.JAVA;
            tokenNames[i++] = TOKEN_NAME.XML;
            tokenNames[i++] = TOKEN_NAME.HTML;
            tokenNames[i++] = TOKEN_NAME.JAVA_SCRIPT;
            tokenNames[i++] = TOKEN_NAME.PROPS;
            tokenNames[i++] = TOKEN_NAME.BATCH;
            tokenNames[i++] = TOKEN_NAME.C;
            tokenNames[i++] = TOKEN_NAME.CC;
            tokenNames[i++] = TOKEN_NAME.PLSQL;
            tokenNames[i++] = TOKEN_NAME.PERL;
            tokenNames[i++] = TOKEN_NAME.PHP;
            tokenNames[i++] = TOKEN_NAME.MAKEFILE;
            tokenNames[i++] = TOKEN_NAME.EIFFEL;
            tokenNames[i++] = TOKEN_NAME.IDL;
            tokenNames[i++] = TOKEN_NAME.PATCH;
            tokenNames[i++] = TOKEN_NAME.PYTHON;
            tokenNames[i++] = TOKEN_NAME.SHELL;
            tokenNames[i++] = TOKEN_NAME.TEX;
            tokenNames[i++] = TOKEN_NAME.TSQL;
        }
        return tokenNames;
    }

    public static String[] getFavorisNames() {
        if (favorisNames == null) {
            int i = 0;
            favorisNames = new String[12];
            favorisNames[i++] = TOKEN_NAME.JAVA;
            favorisNames[i++] = TOKEN_NAME.XML;
            favorisNames[i++] = TOKEN_NAME.HTML;
            favorisNames[i++] = TOKEN_NAME.JAVA_SCRIPT;
            favorisNames[i++] = TOKEN_NAME.PROPS;
            favorisNames[i++] = TOKEN_NAME.BATCH;
            favorisNames[i++] = TOKEN_NAME.C;
            favorisNames[i++] = TOKEN_NAME.CC;
            favorisNames[i++] = TOKEN_NAME.PLSQL;
            favorisNames[i++] = TOKEN_NAME.PERL;
            favorisNames[i++] = TOKEN_NAME.PHP;
            favorisNames[i++] = TOKEN_NAME.MAKEFILE;
        }
        return favorisNames;
    }

    public static Map getTokenMarkerMap() {
        if (tokenMarkerMap == null) {
            tokenMarkerMap = new Hashtable();
            tokenMarkerMap.put(TOKEN_NAME.JAVA, TOKEN_MARKER.JAVA);
            tokenMarkerMap.put(TOKEN_NAME.XML, TOKEN_MARKER.XML);
            tokenMarkerMap.put(TOKEN_NAME.HTML, TOKEN_MARKER.HTML);
            tokenMarkerMap.put(TOKEN_NAME.JAVA_SCRIPT, TOKEN_MARKER.JAVA_SCRIPT);
            tokenMarkerMap.put(TOKEN_NAME.PROPS, TOKEN_MARKER.PROPS);
            tokenMarkerMap.put(TOKEN_NAME.BATCH, TOKEN_MARKER.BATCH);
            tokenMarkerMap.put(TOKEN_NAME.C, TOKEN_MARKER.C);
            tokenMarkerMap.put(TOKEN_NAME.CC, TOKEN_MARKER.CC);
            tokenMarkerMap.put(TOKEN_NAME.PLSQL, TOKEN_MARKER.PLSQL);
            tokenMarkerMap.put(TOKEN_NAME.PERL, TOKEN_MARKER.PERL);
            tokenMarkerMap.put(TOKEN_NAME.PHP, TOKEN_MARKER.PHP);
            tokenMarkerMap.put(TOKEN_NAME.MAKEFILE, TOKEN_MARKER.MAKEFILE);
            tokenMarkerMap.put(TOKEN_NAME.EIFFEL, TOKEN_MARKER.EIFFEL);
            tokenMarkerMap.put(TOKEN_NAME.IDL, TOKEN_MARKER.IDL);
            tokenMarkerMap.put(TOKEN_NAME.PATCH, TOKEN_MARKER.PATCH);
            tokenMarkerMap.put(TOKEN_NAME.PYTHON, TOKEN_MARKER.PYTHON);
            tokenMarkerMap.put(TOKEN_NAME.SHELL, TOKEN_MARKER.SHELL);
            tokenMarkerMap.put(TOKEN_NAME.TEX, TOKEN_MARKER.TEX);
            tokenMarkerMap.put(TOKEN_NAME.TSQL, TOKEN_MARKER.TSQL);
        }
        return tokenMarkerMap;
    }
}
