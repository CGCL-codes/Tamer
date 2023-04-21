package org.dragonfly.jsminx;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import org.dragonfly.util.FileUtil;
import org.inconspicuous.jsmin.JSMin;

/**
 * JavaScript-Minifier tool
 * 
 * @author Van.Fred
 * @email f15_nsm@hotmail.com
 * @version 1.1
 * @date 2008-6-19
 * @since 1.0
 */
public class JSMinX {

    public static final String JSMIN_SUFFIX = "-min";

    public static final String JSMIN_REMARK = "/** JSMinX 1.1 - Dragonfly.org */";

    /**
	 * Judge if jsmin file.
	 * 
	 * @param filePath
	 * @return Yes or No
	 */
    private static boolean isJsminFile(String filePath) {
        return filePath.toLowerCase().endsWith(JSMIN_SUFFIX + ".js");
    }

    /**
	 * jsmin one js file.
	 * 
	 * @param jsFile
	 * @param encoding
	 * @throws Exception
	 */
    public static void jsminFile(String jsFile, String encoding) throws Exception {
        jsminFile(new File(jsFile), encoding);
    }

    /**
	 * jsmin one js file.
	 * 
	 * @param jsFile
	 * @param encoding
	 * @throws Exception
	 */
    public static void jsminFile(File jsFile, String encoding) throws Exception {
        if (!jsFile.isFile() || isJsminFile(jsFile.getPath())) return;
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(FileUtil.getFileNameWithNewSuffix(jsFile.getPath(), JSMIN_SUFFIX)));
        JSMin jsmin = new JSMin(new FileInputStream(jsFile), bos);
        jsmin.remark(JSMIN_REMARK);
        jsmin.jsmin();
        bos.write('\n');
        OutputStreamWriter out = new OutputStreamWriter(bos, encoding);
        BufferedWriter writer = new BufferedWriter(out);
        writer.flush();
        writer.close();
        out.close();
        bos.close();
    }

    /**
	 * jsmin all js of the directory.
	 * 
	 * @param jsDir
	 * @param encoding
	 * @throws Exception
	 */
    public static void jsminDir(String jsDir, String encoding) throws Exception {
        List files = FileUtil.listOnlyJScriptFiles(jsDir);
        for (int i = 0; i < files.size(); i++) {
            jsminFile((File) files.get(i), encoding);
        }
    }

    /**
	 * jsmin all js under the directory. 
	 * 
	 * @param jsDir
	 * @param encoding
	 * @throws Exception
	 */
    public static void jsminTree(String jsDir, String encoding) throws Exception {
        List files = FileUtil.listJScriptTreeFiles(jsDir);
        for (int i = 0; i < files.size(); i++) {
            jsminFile((File) files.get(i), encoding);
        }
    }
}
