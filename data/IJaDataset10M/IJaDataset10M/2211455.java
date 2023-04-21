package cn._2dland.uploader.qq;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import cn._2dland.uploader.utils.EncodeUtils;

public class QQUtils {

    /** 从输入流中读取字符串 */
    public static String readStream(InputStream is, String charset) {
        String str = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (int b; (b = is.read()) >= 0; ) {
                baos.write(b);
            }
            str = new String(baos.toByteArray(), charset);
            baos.close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            str = null;
        }
        return str;
    }

    /** 去掉字符串两端的引号 */
    public static String removeQuotes(String str) {
        if (str == null) return null;
        return str.trim().replaceAll("^\'", "").replaceAll("\'$", "");
    }

    /** 获取文本长度 */
    public static int getTextLength(String text) {
        if (text == null) return 0;
        int len = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 255) len += 1; else len += 2;
        }
        return len;
    }

    /**
	 * 计算登录key值
	 * @param verifyCode 验证码
	 * @param password 密码
	 * @return
	 */
    public static String generateKey(String verifyCode, String password) {
        String passMD5 = EncodeUtils.hexMD5(EncodeUtils.md5(EncodeUtils.md5(password.getBytes())));
        String key = EncodeUtils.hexMD5((passMD5 + verifyCode).toUpperCase().getBytes()).toUpperCase();
        return key;
    }
}
