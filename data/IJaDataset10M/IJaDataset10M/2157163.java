package self.micromagic.eterna.view;

import java.io.Writer;
import java.io.IOException;
import java.util.Map;
import java.util.Iterator;
import java.sql.SQLException;
import self.micromagic.eterna.digester.ConfigurationException;
import self.micromagic.eterna.share.EternaFactory;
import self.micromagic.eterna.sql.ResultIterator;
import self.micromagic.eterna.sql.ResultRow;

/**
 * ��ݼ������, ���ڽ���ݼ�����һ���ĸ�ʽ�������.
 * ע: ��ݼ��в����������õ����.
 */
public interface DataPrinter {

    /**
    * ��ʼ������ݼ������.
    */
    public void initialize(EternaFactory factory) throws ConfigurationException;

    /**
    * ��ȡ����ݼ�����������.
    */
    public String getName() throws ConfigurationException;

    /**
    * �����ݼ�.
    *
    * @param out          �����
    * @param data         ��ݼ�
    * @param hasPreData   �������ݼ�ǰ�Ƿ����������
    */
    void printData(Writer out, Map data, boolean hasPreData) throws IOException, ConfigurationException;

    /**
    * �����������ֵ.
    *
    * @param out         �����
    * @param b           ����ֵ
    */
    void print(Writer out, boolean b) throws IOException, ConfigurationException;

    /**
    * ����ַ�����ֵ.
    *
    * @param out         �����
    * @param c           �ַ�ֵ
    */
    void print(Writer out, char c) throws IOException, ConfigurationException;

    /**
    * �������ֵ.
    *
    * @param out         �����
    * @param i           ����ֵ
    */
    void print(Writer out, int i) throws IOException, ConfigurationException;

    /**
    * ���������ֵ.
    *
    * @param out         �����
    * @param l           ������ֵ
    */
    void print(Writer out, long l) throws IOException, ConfigurationException;

    /**
    * ���������ֵ.
    *
    * @param out         �����
    * @param f           ������ֵ
    */
    void print(Writer out, float f) throws IOException, ConfigurationException;

    /**
    * ���˫���ȸ�����ֵ.
    *
    * @param out         �����
    * @param d           ˫���ȸ�����ֵ
    */
    void print(Writer out, double d) throws IOException, ConfigurationException;

    /**
    * ����ַ�����ֵ.
    *
    * @param out         �����
    * @param s           �ַ�����ֵ
    */
    void print(Writer out, String s) throws IOException, ConfigurationException;

    /**
    * ���һ��Object����.
    *
    * @param out          �����
    * @param value        Ҫ�����Object����
    */
    void print(Writer out, Object value) throws IOException, ConfigurationException;

    /**
    * ���һ��Map����.
    *
    * @param out          �����
    * @param map          Ҫ�����Map����
    */
    void printMap(Writer out, Map map) throws IOException, ConfigurationException;

    /**
    * ����������������.
    *
    * @param out          �����
    * @param ritr         �������
    */
    void printResultIterator(Writer out, ResultIterator ritr) throws IOException, ConfigurationException, SQLException;

    /**
    * ����н�������.
    *
    * @param out          �����
    * @param row          �н��
    */
    void printResultRow(Writer out, ResultRow row) throws IOException, ConfigurationException, SQLException;

    /**
    * ��������������.
    *
    * @param out          �����
    * @param itr          �����
    */
    void printIterator(Writer out, Iterator itr) throws IOException, ConfigurationException;

    /**
    * ��ȡһ��beanʵ��������.
    *
    * @param beanClass     bean��Class
    * @return      beanʵ��������
    */
    BeanPrinter getBeanPrinter(Class beanClass) throws ConfigurationException;

    /**
    * beanʵ��������.
    */
    interface BeanPrinter {

        /**
       * ���bean�е�����.
       *
       * @param p         ��ݼ������
       * @param out       �����
       * @param bean      bean��ʵ��
       */
        void print(DataPrinter p, Writer out, Object bean) throws IOException, ConfigurationException;
    }
}
