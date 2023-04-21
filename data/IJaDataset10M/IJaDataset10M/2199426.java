package cn.tearcry.xiaonei;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 *
 * @author �?��
 */
public class Filter extends FileFilter {

    /**
     * �ļ���չ��
     */
    private String extension;

    private String description;

    /**
     * ���캯���һ��MyFilter��ʵ��
     * @param extension ���յ��ļ���չ��
     */
    public Filter(String extension, String des) {
        this.extension = extension;
        this.description = des;
    }

    /**
     * ʵ��FileFilter�ӿ����accept()����
     * �ж��Ƿ��������ļ�
     * @param file Ҫ�жϵ��ļ�
     * @return true  - ����
     * false - ������
     */
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        String name = file.getName();
        int index = name.lastIndexOf(".");
        if (index == -1) return false; else if (index == name.length() - 1) return false; else return this.extension.equals(name.substring(index + 1));
    }

    public String getDescription() {
        return description;
    }
}
