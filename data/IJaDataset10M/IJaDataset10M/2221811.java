package net.itsite.document.utils;

import com.sun.istack.internal.Nullable;
import info.monitorenter.cpdetector.io.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Properties;

public class FileUtils {

    public static Properties getProperties(String config) throws Exception {
        Properties properties = new Properties();
        ClassLoader loader = FileUtils.class.getClassLoader();
        InputStream is = loader.getResourceAsStream(config);
        if (is == null) {
            throw new Exception("properties is not found");
        }
        properties.load(is);
        return properties;
    }

    public static final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");

    public static String getFilePrefix(String fileFullName) {
        int splitIndex = fileFullName.lastIndexOf(".");
        return fileFullName.substring(0, splitIndex);
    }

    public static String getFilePrefix(File file) {
        String fileFullName = file.getName();
        return getFilePrefix(fileFullName);
    }

    public static String getFileSuffix(String fileFullName) {
        int splitIndex = fileFullName.lastIndexOf(".");
        return fileFullName.substring(splitIndex + 1);
    }

    public static String getFileSuffix(File file) {
        String fileFullName = file.getName();
        return getFileSuffix(fileFullName);
    }

    public static String appendFileSeparator(String path) {
        return path + (path.lastIndexOf(File.separator) == path.length() - 1 ? "" : File.separator);
    }

    /**
	 * 文件转化为字节数组
	 */
    public static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1) out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }

    /**
	 * 把字节数组保存为一个文件
	 */
    public static File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
	 * 从字节数组获取对象
	 */
    public static Object getObjectFromBytes(byte[] objBytes) throws Exception {
        if (objBytes == null || objBytes.length == 0) {
            return null;
        }
        ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);
        ObjectInputStream oi = new ObjectInputStream(bi);
        return oi.readObject();
    }

    /**
	 * 从对象获取一个字节数组
	 */
    public static byte[] getBytesFromObject(Serializable obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(obj);
        return bo.toByteArray();
    }

    /**
	 * 获取文件编码格式(字符集)
	 * 
	 * @param file
	 *            输入文件
	 * @return 字符集名称，如果不支持的字符集则返回null
	 */
    public static Charset getFileEncoding(File file) {
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        detector.add(new ParsingDetector(false));
        detector.add(JChardetFacade.getInstance());
        detector.add(ASCIIDetector.getInstance());
        detector.add(UnicodeDetector.getInstance());
        Charset charset = null;
        try {
            charset = detector.detectCodepage(file.toURI().toURL());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return charset;
    }

    /**
	 * 文件编码格式转换
	 * 
	 * @param inFile
	 *            输入文件
	 * @param inCharset
	 *            输入文件字符集
	 * @param outFile
	 *            输出文件
	 * @param outCharset
	 *            输出文件字符集
	 * @return 转码后的字符流
	 * @throws IOException
	 */
    public static byte[] convertFileEncoding(File inFile, Charset inCharset, @Nullable File outFile, Charset outCharset) throws IOException {
        RandomAccessFile inRandom = new RandomAccessFile(inFile, "r");
        FileChannel inChannel = inRandom.getChannel();
        MappedByteBuffer byteMapper = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int) inFile.length());
        CharsetDecoder inDecoder = inCharset.newDecoder();
        CharsetEncoder outEncoder = outCharset.newEncoder();
        CharBuffer cb = inDecoder.decode(byteMapper);
        ByteBuffer outBuffer = null;
        try {
            outBuffer = outEncoder.encode(cb);
            RandomAccessFile outRandom = null;
            FileChannel outChannel = null;
            if (outFile != null) {
                try {
                    outRandom = new RandomAccessFile(outFile, "rw");
                    outChannel = outRandom.getChannel();
                    outChannel.write(outBuffer);
                } finally {
                    if (outChannel != null) {
                        outChannel.close();
                    }
                    if (outRandom != null) {
                        outRandom.close();
                    }
                }
            }
        } finally {
            inChannel.close();
            inRandom.close();
        }
        return outBuffer.array();
    }

    /**
	 * 文件编码格式转换
	 * 
	 * @param inFile
	 *            输入文件
	 * @param inCharset
	 *            输入文件字符集
	 * @param outCharset
	 *            输出字符集
	 * @return 转码后的字符流
	 * @throws IOException
	 *             Exception
	 */
    public static byte[] convertFileEncoding(File inFile, Charset inCharset, Charset outCharset) throws IOException {
        return convertFileEncoding(inFile, inCharset, (File) null, outCharset);
    }

    /**
	 * 将文件字符集转化为指定字符集
	 * 
	 * @param inFile
	 *            输入文件
	 * @param outFile
	 *            输出文件
	 * @param outCharset
	 *            输出文件字符集
	 * @return 转码后的字符流
	 * @throws IOException
	 */
    public static byte[] convertFileEncoding(File inFile, @Nullable File outFile, Charset outCharset) throws IOException {
        return convertFileEncoding(inFile, getFileEncoding(inFile), outFile, outCharset);
    }

    /**
	 * 将文件字符集转化为指定字符集
	 * 
	 * @param inFile
	 *            输入文件
	 * @return 转码后的字符流
	 * @throws IOException
	 */
    public static byte[] convertFileEncoding(File inFile, Charset outCharset) throws IOException {
        return convertFileEncoding(inFile, (File) null, outCharset);
    }

    /**
	 * 将文件字符集转换为系统字符集
	 * 
	 * @param inFile
	 *            输入文件
	 * @return 转码后的字符流
	 * @throws IOException
	 */
    public static byte[] convertFileEncodingToSys(File inFile) throws IOException {
        return convertFileEncoding(inFile, (File) null, Charset.defaultCharset());
    }

    /**
	 * 将文件字符集转换为系统字符集
	 * 
	 * @param inFile
	 *            输入文件
	 * @param outFile
	 *            输出文件
	 * @return 转码后的字符流
	 * @throws IOException
	 */
    public static byte[] convertFileEncodingToSys(File inFile, @Nullable File outFile) throws IOException {
        return convertFileEncoding(inFile, outFile, Charset.defaultCharset());
    }

    public static boolean isPrefix(File file, String prefix) {
        String suffix = FileUtils.getFileSuffix(file);
        if (suffix.toLowerCase().equals(prefix)) {
            return true;
        }
        return false;
    }
}
