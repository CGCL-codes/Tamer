package tests.test;

public class CharAsFieldTest {

    private static char charAsField;

    private char charAsField2;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        char charAsVariable = 'A';
        System.out.println(charAsVariable);
        charAsField = 'B';
        System.out.println(charAsField + ":" + (int) charAsField);
        CharAsFieldTest tHis = new CharAsFieldTest();
        tHis.charAsField2 = 'C';
        System.out.println(tHis.charAsField2 + ":" + (int) tHis.charAsField2);
    }
}
