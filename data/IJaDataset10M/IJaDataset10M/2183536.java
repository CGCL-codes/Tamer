package org.wdcode.common.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.wdcode.common.constants.ArrayConstants;
import org.wdcode.common.constants.EncodingConstants;
import org.wdcode.common.log.WdLogs;
import org.wdcode.common.params.WdCommonParams;
import org.wdcode.common.tools.Arrays;
import org.wdcode.common.util.CloseUtil;

/**
 * 关于IO操作
 * @author WD
 * @since JDK6
 * @version 1.0 2009-04-21
 */
public final class StreamUtil {

    /**
	 * 读取InputStream内容成为字符串 默认使用UTF-8
	 * @param in 输入流
	 * @return 读取的字符串
	 */
    public static final String readString(InputStream in) {
        return readString(in, EncodingConstants.UTF_8);
    }

    /**
	 * 读取InputStream内容成为字符串
	 * @param in 输入流
	 * @param charsetName 编码格式
	 * @return 读取的字符串
	 */
    public static final String readString(InputStream in, String charsetName) {
        StringBuilder buffer = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(in, charsetName), WdCommonParams.getIoBufferSize());
            String line;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            WdLogs.error(e);
        } finally {
            CloseUtil.close(br, in);
        }
        return buffer.toString();
    }

    /**
	 * 读取出输入流的所有字节
	 * @param in 输入流
	 * @return 字节数组
	 */
    public static final byte[] read(InputStream in) {
        byte[] result = ArrayConstants.BYTES_EMPTY;
        try {
            byte[] buffer = new byte[WdCommonParams.getIoBufferSize()];
            int length = 0;
            while ((length = in.read(buffer)) > 0) {
                result = Arrays.add(result, buffer, length);
            }
        } catch (IOException e) {
            WdLogs.error(e);
        } finally {
            CloseUtil.close(in);
        }
        return result;
    }

    /**
	 * 把text写入到os中 默认使用UTF-8编码
	 * @param os 输出流
	 * @param text 输入的字符串
	 */
    public static final boolean writeString(OutputStream os, String text) {
        return writeString(os, text, EncodingConstants.UTF_8);
    }

    /**
	 * 把text写入到os中
	 * @param out 输出流
	 * @param text 输入的字符串
	 * @param charsetName 编码格式
	 * @return true false
	 */
    public static final boolean writeString(OutputStream out, String text, String charsetName) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(out, charsetName), WdCommonParams.getIoBufferSize());
            bw.write(text);
            bw.flush();
            return true;
        } catch (IOException e) {
            WdLogs.error(e);
        } finally {
            CloseUtil.close(bw, out);
        }
        return false;
    }

    /**
	 * 把字节数组写入到流中
	 * @param out 输出流
	 * @param b 字节数组
	 * @return 是否成功
	 */
    public static final boolean write(OutputStream out, byte[] b) {
        return write(out, new ByteArrayInputStream(b));
    }

    /**
	 * 把text写入到out中
	 * @param out 输出流
	 * @param in 输入流
	 * @return true false
	 */
    public static final boolean write(OutputStream out, InputStream in) {
        try {
            byte[] buffer = new byte[WdCommonParams.getIoBufferSize()];
            int num = 0;
            while ((num = in.read(buffer)) > 0) {
                out.write(buffer, 0, num);
            }
            out.flush();
            return true;
        } catch (IOException e) {
            WdLogs.error(e);
        } finally {
            CloseUtil.close(out, in);
        }
        return false;
    }

    private StreamUtil() {
    }
}
