package ru.jnano.app.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class TextFileUtils {

    /**
    * ���������� ������ ��� ����������� �����������
    */
    private static final String COMMENT_LINE = "//";

    /**
    * ���������� �������� �� ������ �������-������������
    * ��������� ������-������������ �������� ������ 
    * ������������ � COMMENT_LINE
    * @param line - ����������� ������
    * @return true - ���� ������ �������� �������-������������;
    *  false - � ��������������� ������
    */
    private static boolean isCommentLine(String line) {
        if (line.length() < COMMENT_LINE.length()) return false;
        return line.substring(0, COMMENT_LINE.length()).equals(COMMENT_LINE);
    }

    /**
    * ������� ����������� �� ������
    * ����������� ������ ���������� � COMMENT_LINE
    * @param line - ������ ��� ��������������
    * @return ����������������� ������
    */
    private static String DelCommentFromLine(String line) {
        int index = line.indexOf(COMMENT_LINE);
        if (index != -1) {
            return line.substring(0, index);
        }
        return line;
    }

    /**
    * ����� ����������� ��������� ���� � ������ �����.
    * ����� ������� �����������, ������ ������ � ������-�����������
    * @param file - ������ ����
    * @return ������ �����
    * @throws IOException
    */
    public static String[] TxtToArrayStrings(File file) throws IOException {
        ArrayList<String> array = new ArrayList<String>();
        BufferedReader in = new BufferedReader(new FileReader(file));
        String line;
        while ((line = in.readLine()) != null) {
            if ((!isCommentLine(line)) && (!line.trim().equals(""))) {
                array.add(DelCommentFromLine(line));
            }
        }
        return array.toArray(new String[0]);
    }

    /**
	 * �������� ������� ������������ ����� �� ��������� �����
	 * @param file - �������� ����
	 * @param equilsNumColumn - �������� �������� �������� �� ���� �������
	 * @return ������� ������������ �����
	 * @throws IOException
	 */
    public static double[][] TableFromTxtFile(File file, boolean equilsNumColumn) throws IOException {
        return ArrayStringsToTableDouble(TxtToArrayStrings(file), equilsNumColumn);
    }

    public static Object[] TableWidthStringFromTxtFile(File file) throws IOException {
        String[] lines = TxtToArrayStrings(file);
        Object[] arrayObjs = new Object[lines.length];
        for (int i = 0; i < lines.length; i++) {
            try {
                arrayObjs[i] = ArrayStringToArrayDouble(lines[i].trim().split("(\\s+)|(\\t+)"));
            } catch (Exception e) {
                arrayObjs[i] = lines[i];
            }
        }
        return arrayObjs;
    }

    /**
    * ����� ����������� ������ � ������ ���� Double.
    * ������ ������ ��������� ���������� ����� ����� 
    * ���������� ���������� NumberFormatException
    * ������������ ������� ����� ����� ���� ����� � �������
    * @param line - ������� ������
    * @return ������������ �����
    */
    private static double StringToDouble(String line) {
        String[] cols = line.trim().split("[.,]");
        if (cols.length == 1) return Double.parseDouble(line);
        return Double.parseDouble(cols[0] + "." + cols[1]);
    }

    /**
    * ����� ����������� ������ ����� � ������ Double
    * ������ ������ ��������� ���������� ����� 
    * ����� ���������� ���������� NumberFormatException
    * @param lines - ������ �����
    * @return ������ double
    */
    private static double[] ArrayStringToArrayDouble(String[] lines) {
        double[] numbers = new double[lines.length];
        for (int i = 0; i < numbers.length; i++) numbers[i] = StringToDouble(lines[i]);
        return numbers;
    }

    /**
    * ����� ����������� ������ ����� � ������� �������� ���� Double
    * ���������� �������� � ������ ������ ���������� 
    * ����� ��������� ���������� IllegalArgumentException
    * @param lines - ������ �����
    * @param equilsNumColumn - ���� �������� ����������� ����� ��������
    * ��� ������ ������  
    * @return ������� ���� double
    */
    public static double[][] ArrayStringsToTableDouble(String[] lines, boolean equilsNumColumn) {
        double[][] table = new double[lines.length][];
        for (int i = 0; i < lines.length; i++) table[i] = ArrayStringToArrayDouble(lines[i].trim().split("(\\s+)|(\\t+)"));
        if (equilsNumColumn) {
            for (int i = 1; i < table.length; i++) {
                if (table[i - 1].length != table[i].length) throw new IllegalArgumentException("ImportData.ArrayStringsToTableDouble(String[] lines): " + "���������� �������� �� ��������� (" + i + " ������)");
            }
        }
        return table;
    }

    /**
    * ����� ������������ ��� ������ ������� ����� � ����
    * @param srcStrings - �������� ������ �����
    * @param pathDstFile - ���� � ����� ����������
    * @throws IOException 
    */
    public static void ArrayStringsToTxtFile(String[] srcStrings, File pathDstFile) throws IOException {
        BufferedWriter bwFile = new BufferedWriter(new FileWriter(pathDstFile));
        for (String line : srcStrings) {
            bwFile.write(line);
            bwFile.newLine();
        }
        bwFile.flush();
        bwFile.close();
    }

    /**
    * ����� ����������� ������ double � ������ � �������� ���������������
    * ��� ������� ����� ������ ���� ������ ��������������,
    * ����� ��������� ����������  IllegalArgumentException
    * @param srcDoubles - �������� ������ double
    * @param sep - ����������� ����� �������
    * @param fmt - ������ ����� ���������� ��� ������� �������
    * @return ������
    */
    public static String ArrayDoublesToString(double[] srcDoubles, String sep, String[] fmts, double[] scales) {
        if (srcDoubles.length != fmts.length) throw new IllegalArgumentException("ExportData.ArrayDoublesToString: " + "�� ��� ������� ����� ������ ��������������");
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < srcDoubles.length; j++) {
            sb.append(DoubleToString(srcDoubles[j], fmts[j], scales[j]));
            if (j != (srcDoubles.length - 1)) sb.append(sep);
        }
        return sb.toString();
    }

    /**
    * ����� ����������� ������ double � ������ � �������� ���������������
    * ��� ������� ����� ������ ���� ������ ��������������,
    * ����� ��������� ����������  IllegalArgumentException
    * @param srcDoubles - �������� ������ double
    * @param sep - ����������� ����� �������
    * @param fmt - ������ ����� ���������� ��� ������� �������
    * @return ������
    */
    public static String ArrayDoublesToString(double[] srcDoubles, String sep, String fmt) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < srcDoubles.length; j++) {
            sb.append(DoubleToString(srcDoubles[j], fmt, 1));
            if (j != (srcDoubles.length - 1)) sb.append(sep);
        }
        return sb.toString();
    }

    /**
    * ����� ����������� ����� ���� double � ������ � �������� ���������������
    * @param srcDouble - �����
    * @param fmt - ����� ���������� �������������� ��� �����
    * @return ������
    */
    public static String DoubleToString(double srcDouble, String fmt, double scale) {
        return String.format(Locale.ENGLISH, fmt, srcDouble * scale);
    }

    public static double[][] swapColsInTables(double[][] tableSrc, double[][] tableExt, int[] indexsSrc, int[] indexsExt, int indexColSrc, int indexColExt) {
        double[][] table = new double[tableSrc.length][];
        for (int i = 0; i < tableSrc.length; i++) {
            table[i] = new double[tableSrc[i].length];
            if (tableSrc[i][indexColSrc] != tableExt[i][indexColExt]) throw new IllegalArgumentException();
            for (int j = 0; j < tableSrc[i].length; j++) {
                boolean swap = false;
                for (int k = 0; k < indexsSrc.length; k++) {
                    if (j == indexsSrc[k]) {
                        table[i][j] = tableExt[i][indexsExt[k]];
                        swap = true;
                    }
                }
                if (!swap) table[i][j] = tableSrc[i][j];
            }
        }
        return table;
    }
}
