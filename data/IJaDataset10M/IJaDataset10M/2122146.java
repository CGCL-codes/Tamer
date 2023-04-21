package org.mozilla.javascript.tools.shell;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class InputStreamReaderGlobal extends Global {

    private static final String ERROR_FREE = "jslint: No problems found in";

    private static Map<String, InputStream> registeredStreams = new HashMap<String, InputStream>(1);

    public static void registerStream(String name, InputStream stream) {
        registeredStreams.put(name, stream);
    }

    public void init(Context cx) {
        super.init(cx);
        String[] names = { "readFile", "print" };
        defineFunctionProperties(names, InputStreamReaderGlobal.class, ScriptableObject.DONTENUM);
    }

    /**
	 * @see org.mozilla.javascript.tools.shell.Global no changes, call modified
	 *      private
	 */
    public static Object readFile(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws IOException {
        if (args.length == 0) {
            throw reportRuntimeError("msg.shell.readFile.bad.args");
        }
        String path = ScriptRuntime.toString(args[0]);
        String charCoding = null;
        if (args.length >= 2) {
            charCoding = ScriptRuntime.toString(args[1]);
        }
        return readUrl(path, charCoding, true);
    }

    /**
	 * @see org.mozilla.javascript.tools.shell.Global Catch
	 *      FileNotFoundException and search in registerStream for file
	 */
    private static String readUrl(String filePath, String charCoding, boolean urlIsFile) throws IOException {
        int chunkLength;
        InputStream is = null;
        try {
            if (!urlIsFile) {
                URL urlObj = new URL(filePath);
                URLConnection uc = urlObj.openConnection();
                is = uc.getInputStream();
                chunkLength = uc.getContentLength();
                if (chunkLength <= 0) chunkLength = 1024;
                if (charCoding == null) {
                    String type = uc.getContentType();
                    if (type != null) {
                        charCoding = getCharCodingFromType(type);
                    }
                }
            } else {
                if (registeredStreams.containsKey(filePath)) {
                    is = registeredStreams.get(filePath);
                    chunkLength = 4096;
                } else {
                    File f = new File(filePath);
                    long length = f.length();
                    chunkLength = (int) length;
                    if (chunkLength != length) throw new IOException("Too big file size: " + length);
                    if (chunkLength == 0) {
                        return "";
                    }
                    is = new FileInputStream(f);
                }
            }
            Reader r;
            if (charCoding == null) {
                r = new InputStreamReader(is);
            } else {
                r = new InputStreamReader(is, charCoding);
            }
            return readReader(r, chunkLength);
        } finally {
            if (is != null) is.close();
        }
    }

    /**
	 * @see org.mozilla.javascript.tools.shell.Global no changes, but private
	 */
    private static String getCharCodingFromType(String type) {
        int i = type.indexOf(';');
        if (i >= 0) {
            int end = type.length();
            ++i;
            while (i != end && type.charAt(i) <= ' ') {
                ++i;
            }
            String charset = "charset";
            if (charset.regionMatches(true, 0, type, i, charset.length())) {
                i += charset.length();
                while (i != end && type.charAt(i) <= ' ') {
                    ++i;
                }
                if (i != end && type.charAt(i) == '=') {
                    ++i;
                    while (i != end && type.charAt(i) <= ' ') {
                        ++i;
                    }
                    if (i != end) {
                        while (type.charAt(end - 1) <= ' ') {
                            --end;
                        }
                        return type.substring(i, end);
                    }
                }
            }
        }
        return null;
    }

    /**
	 * @see org.mozilla.javascript.tools.shell.Global no changes, but private
	 */
    private static String readReader(Reader reader, int initialBufferSize) throws IOException {
        char[] buffer = new char[initialBufferSize];
        int offset = 0;
        for (; ; ) {
            int n = reader.read(buffer, offset, buffer.length - offset);
            if (n < 0) {
                break;
            }
            offset += n;
            if (offset == buffer.length) {
                char[] tmp = new char[buffer.length * 2];
                System.arraycopy(buffer, 0, tmp, 0, offset);
                buffer = tmp;
            }
        }
        return new String(buffer, 0, offset);
    }

    /**
	 * @see org.mozilla.javascript.tools.shell.Global 
	 *
	 * Catch parser errors 
	 */
    public static Object print(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        if (!(args.length == 1 && args[0].toString().contains(ERROR_FREE))) {
            PrintStream out = getInstance(funObj).getOut();
            for (int i = 0; i < args.length; i++) {
                if (i > 0) out.print(" ");
                String s = Context.toString(args[i]);
                out.print(s);
            }
            out.println();
        }
        return Context.getUndefinedValue();
    }

    /**
	 * @see org.mozilla.javascript.tools.shell.Global no changes, but private
	 */
    private static Global getInstance(Function function) {
        Scriptable scope = function.getParentScope();
        if (!(scope instanceof Global)) throw reportRuntimeError("msg.bad.shell.function.scope", String.valueOf(scope));
        return (Global) scope;
    }
}
