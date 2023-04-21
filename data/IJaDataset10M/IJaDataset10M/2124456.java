package net.itsite.utils;

import java.io.File;
import net.itsite.document.documentconfig.DocumentConfigMgr;

public class UnFileUtils {

    /**
	 * 解压RAR文件
	 * @param targetPath
	 * @param absolutePath
	 */
    public static boolean unRar(String targetPath, String absolutePath) {
        try {
            final File targetFile = new File(targetPath);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            String unrarCmd = DocumentConfigMgr.getDocuMgr().getValue("rar") + " x -o+ -p[123456] -y \"" + absolutePath + "\" \"" + targetPath + "\"";
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(unrarCmd);
            p.waitFor();
            if (p.exitValue() == 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
	 * 解压ZIP
	 * @param targetPath
	 * @param absolutePath
	 */
    public static boolean unZip(final String targetPath, final String absolutePath) {
        final File targetFile = new File(targetPath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        try {
            ZipUtils.unZip(absolutePath, targetPath);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
