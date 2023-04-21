package com.sitescape.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import org.apache.tools.ant.DirectoryScanner;

/**
 * <a href="Java2Html.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class Java2Html {

    public static void main(String[] args) {
        new Java2Html(args);
    }

    public Java2Html(String[] args) {
        if ((args == null) || (args.length != 3)) {
            return;
        }
        String batchFile = args[0];
        String srcDir = args[1];
        String outDir = args[2];
        batchFile = StringUtil.replace(batchFile, "/", "\\");
        try {
            Runtime rt = Runtime.getRuntime();
            String javaHome = System.getProperty("java.home");
            if (javaHome.endsWith("\\jre")) {
                javaHome = javaHome.substring(0, javaHome.length() - 4);
            }
            Process p = rt.exec(batchFile + " -js " + srcDir + " -d " + outDir + " -jd " + javaHome + "/docs/api" + " http://java.sun.com/products/jdk/1.4/docs/api" + " -m 4 -t 4" + " -nf");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while (br.readLine() != null) {
            }
            br.close();
            DirectoryScanner ds = new DirectoryScanner();
            ds.setIncludes(new String[] { "**\\*.java.html" });
            ds.setBasedir(outDir);
            ds.scan();
            String[] files = ds.getIncludedFiles();
            for (int i = 0; i < files.length; i++) {
                File file = new File(outDir + "/" + files[i]);
                String content = FileUtil.read(file);
                content = StringUtil.replace(content, "stylesheet.css", "java2html.css");
                FileUtil.write(file, content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
