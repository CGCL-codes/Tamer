package extend.lang.util;

import java.io.File;
import java.io.IOException;
import extend.lang.Encoding;
import extend.lang.Files;
import extend.lang.Streams;

/**
 * 文本工具类。涉及文本文件的一些工具功能
 * @author 13leaf
 *
 */
public class Texts {

    /**
	 * 转换文本文件的字符编码.
	 * @param textFile
	 * @param readEncoding
	 * @param writeEncoding
	 */
    public static void convertEncoding(File textFile, String readEncoding, String writeEncoding) {
        String text = Streams.readAndClose(Streams.fileInr(textFile, readEncoding));
        Streams.writeAndClose(Streams.fileOutw(textFile, writeEncoding), text);
    }

    /**
	 * 将一个项目的所有java文件从gbk编码转换为utf8编码。重复使用会有副作用
	 * @param projectPath
	 * @param backUp 是否在转换前先保存一下项目
	 */
    public static void convertProjectEncode(String projectPath, boolean backUp) {
        File projectRoot = new File(projectPath);
        if (backUp) {
            try {
                Files.copyDir(projectRoot, new File(projectRoot.getAbsolutePath() + "_backup"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File[] javaFiles = Files.scanFiles(projectRoot, new SuffixFilter("java"));
        for (File file : javaFiles) {
            convertEncoding(file, Encoding.GBK, Encoding.UTF8);
        }
    }
}
