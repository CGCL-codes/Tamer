package com.liferay.portal.tools;

import com.liferay.portal.kernel.util.StringMaker;
import com.liferay.portal.util.PropsUtil;
import com.liferay.util.FileUtil;

/**
 * <a href="JavaScriptBuilder.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class JavaScriptBuilder {

    public static void main(String[] args) {
        if (args.length == 2) {
            new JavaScriptBuilder(args[0], args[1]);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public JavaScriptBuilder(String jsDir, String mergedFile) {
        try {
            StringMaker sm = new StringMaker();
            String[] files = PropsUtil.getArray(PropsUtil.JAVASCRIPT_FILES);
            for (int i = 0; i < files.length; i++) {
                String content = FileUtil.read(jsDir + files[i]);
                sm.append(content);
                sm.append("\n");
            }
            FileUtil.write(mergedFile, sm.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
