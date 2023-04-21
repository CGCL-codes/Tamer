package com.google.javascript.jscomp;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;
import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;

/**
 * Runs the closure compiler on the appropriate JS files.
 * @author User
 */
public class ClosureCompile {

    private static final String dir = System.getProperty("scripts") + "/";

    /**
	 * @param args nothing
	 */
    public static void main(String[] args) throws IOException {
        final String version = System.currentTimeMillis() + "";
        AbstractCommandLineRunner<Compiler, CompilerOptions> r = new AbstractCommandLineRunner<Compiler, CompilerOptions>() {

            @Override
            protected Compiler createCompiler() {
                getCommandLineConfig().setJs(Arrays.asList(dir + "xui.nonmin.js", dir + "json_wrapper.js", dir + "common.js", dir + "views.js", dir + "send.js", dir + "match_editor.js", dir + "matches.js", dir + "index.js", dir + "new.js"));
                getCommandLineConfig().setExterns(Arrays.asList(dir + "externs.js", "w3c_dom1.js", "w3c_dom2.js", "w3c_dom3.js", "webstorage.js", "es3.js", "es5.js", "window.js", "w3c_selectors.js", "w3c_event.js", "html5.js", "w3c_event3.js"));
                return new Compiler();
            }

            @Override
            protected CompilerOptions createOptions() {
                CompilerOptions o = new CompilerOptions();
                CompilationLevel.ADVANCED_OPTIMIZATIONS.setOptionsForCompilationLevel(o);
                getCommandLineConfig().setJsOutputFile(dir + "compiled-" + version + ".cache.js");
                return o;
            }
        };
        for (File c : new File(dir).listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return Pattern.matches("compiled-.*\\.cache\\.js", pathname.getName());
            }
        })) {
            c.delete();
        }
        File file = new File(dir + "compiled_js_version.txt");
        FileOutputStream versionFile = new FileOutputStream(file);
        versionFile.write(version.getBytes());
        versionFile.close();
        r.run();
        Result result = r.getCompiler().getResult();
        for (JSError e : result.errors) {
            print(e);
        }
        for (JSError e : result.warnings) {
            print(e);
        }
        if (result.success) {
            System.out.println("success");
        } else {
            System.out.println("failure");
        }
    }

    private static void print(JSError e) {
        System.out.println(e);
    }
}
