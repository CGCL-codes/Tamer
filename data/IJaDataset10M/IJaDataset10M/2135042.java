package aoetec.javalang._221variables;

public class A02_Array {

    public static void main(String[] args) {
        String[] strArray = new String[3];
        strArray[0] = "123";
        strArray[1] = "456";
        String[] sArray = { "array 2", "dfafas" };
        prt(sArray);
    }

    static void prt(String[] strArray) {
        for (String s : strArray) {
            System.out.println(s);
        }
    }
}
