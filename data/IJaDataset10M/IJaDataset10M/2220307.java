package webserg.scjp.arrays;

public class AnonymusArrays {

    public static void main(String[] argv) {
        AA[][] a = { { new AA(), new AA() }, { new AA() } };
        System.out.println(a.length);
        System.out.println(a[0].length);
        int[][] i = new int[2][];
        int[] j = new int[4];
        j[1] = 1;
        j[3] = 2;
        int[] k = new int[2];
        k = j;
        byte b = (byte) 2;
        Integer bb = new Integer(2);
        System.out.println(Integer.toBinaryString(b));
        System.out.println("byte=" + b);
        k[0] = 00000010;
        System.out.println("k[0] = " + k[0]);
        System.out.println("k[3] = " + k[3]);
    }
}

class AA {
}
