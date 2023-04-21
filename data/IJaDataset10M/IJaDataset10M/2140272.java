package util;

/**
 * һ��ʾ��<br/>
 * ��ӡ쳲��������У�������ƽ����
 * @author dewafer
 * @version 2010/3/7
 */
public class Fibonacci {

    /**
	 * �������
	 */
    private static final int ROUND = 50;

    /**
	 * ǰһ������
	 */
    private static long n_pervious = 1;

    /**
	 * ��ǰ��
	 */
    private static long n_this = 1;

    /**
	 * ��һ������
	 */
    private static long n_next = 0;

    /**
	 * �ƽ����
	 */
    private static double golden_section = 0.0d;

    /**
	 * ��ӡ��ͷ
	 */
    private static void print() {
        StringBuilder str = new StringBuilder();
        str.append(n_pervious);
        str.append(" ");
        str.append(n_this);
        str.append(" ");
        str.append(n_next);
        str.append(" ");
        str.append(golden_section);
        str.append("\n");
        System.out.print(str.toString());
    }

    /**
	 * main����
	 * @param args
	 */
    public static void main(String[] args) {
        for (int i = 0; i < ROUND; i++) {
            n_next = n_pervious + n_this;
            golden_section = (double) n_next / n_this;
            print();
            n_pervious = n_this;
            n_this = n_next;
        }
    }
}
