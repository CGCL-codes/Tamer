package javaEdu.util;

public class Octal {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        int x = 0222;
        switch(x) {
            case 222:
                System.out.println("ʮ����: " + x);
                break;
            case (2 * 8 * 8 + 2 * 8 + 2):
                System.out.println("�˽���: " + x);
                break;
            default:
                System.out.println("unknown: " + x);
                break;
        }
    }
}
