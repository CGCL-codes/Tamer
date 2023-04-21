package ks.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import ks.core.Shiduan;

public class Util {

    /**
	 * ��ֵ��ʽ
	 * @param key ��Ҫ����
	 * @param sKey start
	 * @param eKey end
	 * @param sValue start
	 * @param eValue end
	 * @return
	 */
    public static double calCZ(double key, double sKey, double eKey, double sValue, double eValue) {
        return sValue + (key - sKey) * (eValue - sValue) / (eKey - sKey);
    }

    /**
	 * ���ò�ֵ�㷨��������ļ��е�һ�У��õ���һ�е�ֵ��Ҫ����ֵ����С����˳������
	 * @param x
	 * @param minX	��һ���б�������С�ģ�ֵ
	 * @param fileName ����ļ����ļ���
	 * @param xIndex �б����ڵڼ��У���0��ʼ��
	 * @param yIndex �����ڵڼ��У���0��ʼ��
	 * @return ��Ӧ��ֵ������-1��ʾ
	 */
    public static double getYByX(double x, double minX, double[][] array, int xIndex, int yIndex) {
        if (x < minX) {
            return -2;
        }
        for (int i = 0; i < array.length; i++) {
            if (x == array[i][xIndex]) {
                return array[i][yIndex];
            } else if (x < array[i][xIndex]) {
                return Util.calCZ(x, array[i - 1][xIndex], array[i][xIndex], array[i][yIndex], array[i][yIndex]);
            }
        }
        return -1;
    }

    /**
	 *  ������ı���д�볣������
	 * @param fileName  �ı��ļ���
	 * @param row ��ݵ�����
	 * @return ��������
	 */
    public static double[][] openText(String fileName, int row, int column) {
        double[][] result = new double[row][column];
        try {
            String defaultRoot = System.getProperty("user.dir");
            FileReader fr = new FileReader(defaultRoot + "\\res\\" + fileName);
            BufferedReader reader = new BufferedReader(fr);
            String line = "";
            int rowId = 0;
            while ((line = reader.readLine()) != null) {
                String[] arr = line.split("\t");
                for (int i = 0; i < column; i++) {
                    result[rowId][i] = Double.parseDouble(arr[i]);
                }
                rowId++;
            }
            reader.close();
            fr.close();
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return result;
    }

    public static double[][] openText(String fileName, int row) {
        return Util.openText(fileName, row, 2);
    }

    public static void saveAsText(String fileName, Shiduan[] shiduans) {
        try {
            String defaultRoot = System.getProperty("user.dir");
            FileWriter fw = new FileWriter(defaultRoot + "\\res\\" + fileName);
            BufferedWriter writer = new BufferedWriter(fw);
            for (Shiduan shiduan : shiduans) {
                writer.write(shiduan.getS() + "\t" + shiduan.getS_end() + "\t" + shiduan.getXlkr() + "\t" + shiduan.getDuring() + "\t" + shiduan.getX());
                writer.newLine();
            }
            writer.close();
            fw.close();
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public static void saveAsText(String fileName, double[] a, double[] b) {
        try {
            String defaultRoot = System.getProperty("user.dir");
            FileWriter fw = new FileWriter(defaultRoot + "\\res\\" + fileName);
            BufferedWriter writer = new BufferedWriter(fw);
            for (int j = 0; j < a.length; j++) {
                writer.write(a[j] + "\t" + b[j]);
                writer.newLine();
            }
            writer.close();
            fw.close();
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
}
