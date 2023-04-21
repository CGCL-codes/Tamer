package common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class FileUtils {

    private FileUtils() {
    }

    public static final String DEFAULT_CHAR = "_";

    public static final Character[] pattern = { '_', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з', 'и', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'y', 'э', 'ю', 'я', 'ы', '@', '^', ':', '.', ',', ';', '!', '(', ')', '-', '[', ']' };

    public static final String[] replacement = { "_", "q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v", "b", "n", "m", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "a", "b", "v", "g", "d", "e", "j", "z", "i", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "c", "ch", "sh", "sh", "y", "ye", "yu", "ya", "i", "@", "^", ":", ".", ",", ";", "!", "(", ")", "-", "[", "]" };

    public static final Hashtable<Character, String> dictionary = new Hashtable<Character, String>();

    static {
        for (int i = 0; i < pattern.length; i++) dictionary.put(pattern[i], replacement[i]);
    }

    /**
     * Проверяет правописание и производит коррекцию. Переводит все символы
     * в нижний регистр, убирает пробелы слева и справа, заменяет кирилицу, и символы которые
     * не входят в шаблон на "_".
	 * @param value
	 * @return
	 */
    public static String toTranslit(String value) {
        String temp = value.trim();
        temp = temp.toLowerCase();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < temp.length(); i++) {
            Character c = temp.charAt(i);
            String s = dictionary.get(c);
            if (s == null) {
                sb.append(DEFAULT_CHAR);
            } else {
                sb.append(s);
            }
        }
        return sb.toString();
    }

    /**
     * Проверяет правописание в названии файла и производит коррекцию. Переводит все символы
     * в нижний регистр, убирает пробелы слева и справа, заменяет кирилицу, и символы которые
     * не входят в шаблон на "_".
     * @param fileName имя файла, or path
     * @return новое имя файла, excluding any path separators if present
     */
    public static String checkFileNameSpelling(String fileName) {
        String file_name = fileName;
        int val = file_name.lastIndexOf(File.separator);
        if (val >= 0) {
            file_name = file_name.substring(val + 1);
        }
        return toTranslit(file_name);
    }

    /**
	 * copies content of src file to dst
	 * @param src source file to copy
	 * @param dst destination file
	 * @throws java.io.IOException
	 */
    public static void copyFile(File src, File dst) throws IOException {
        if (dst.exists()) dst.delete();
        dst.createNewFile();
        InputStream in = new FileInputStream(src);
        saveToFile(in, dst);
        in.close();
    }

    /**
	 * saves content of src into dst
	 * @param in stream that contains input data
	 * @param dst destination file
	 * @throws java.io.IOException
	 */
    public static void saveToFile(InputStream in, File dst) throws IOException {
        if (dst.exists()) dst.delete();
        dst.createNewFile();
        OutputStream out = new FileOutputStream(dst);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
    }

    /**
	 * saves content of src into dst
	 * uses NIO
	 * WARNING: dst is not closed
	 * @param src file with input data
	 * @param dst output stream where to put data
	 * @throws java.io.IOException
	 */
    public static void loadFromFileNew(File src, OutputStream dst) throws IOException {
        java.io.FileInputStream fis = new java.io.FileInputStream(src);
        java.nio.channels.WritableByteChannel wbc = java.nio.channels.Channels.newChannel(dst);
        fis.getChannel().transferTo(0, fis.available(), wbc);
        fis.close();
    }

    /**
	 * saves content of src into dst
	 * uses IO
	 * WARNING: dst is not closed
	 * @param src file with input data
	 * @param dst output stream where to put data
	 * @throws java.io.IOException
	 */
    public static void loadFromFileOld(File src, OutputStream dst) throws IOException {
        FileInputStream in = new FileInputStream(src);
        byte[] buf = new byte[2048];
        int len;
        while ((len = in.read(buf)) > 0) {
            dst.write(buf, 0, len);
        }
        in.close();
    }

    /**
     * deletes all directory's content
     * if it is a directory deletes all subfiles, subdirectories ...
	 * if it is file delete it
     * @param f 
	 * @param del_self true if delete also given file if it is a directory
     * @return
     */
    public static boolean deleteFiles(File f, boolean del_self) {
        boolean rez = true;
        if (f.exists()) {
            if (f.isDirectory()) {
                File[] files = f.listFiles();
                for (File file : files) {
                    rez = deleteFiles(file, true) & rez;
                }
                if (del_self) {
                    rez = f.delete() & rez;
                }
            } else {
                rez = f.delete() & rez;
            }
        }
        return rez;
    }
}
