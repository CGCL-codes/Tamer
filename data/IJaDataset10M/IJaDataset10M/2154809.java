package org.jlibrtp;

import org.jlibrtp.StaticProcs;

/**
 * Validates the StaticProcs.
 *
 * @author Arne Kepp
 *
 */
public class ValidateStaticProcs {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        long one = 100;
        long two = 1;
        long three = 9999000;
        byte aByte = (byte) 7;
        System.out.println("aByte.hex: " + StaticProcs.hexOfByte(aByte));
        byte[] twob = StaticProcs.uIntLongToByteWord(two);
        for (int i = 0; i < 4; i++) {
            StaticProcs.printBits(twob[i]);
        }
        two = StaticProcs.bytesToUIntLong(twob, 0);
        System.out.println("  one " + one + "  two " + two + "  three " + three);
        twob = StaticProcs.uIntLongToByteWord(two);
        for (int i = 0; i < 4; i++) {
            StaticProcs.printBits(twob[i]);
        }
        byte[] bytes = new byte[2];
        int check = 0;
        for (int i = 0; i < 65536; i++) {
            bytes = StaticProcs.uIntIntToByteWord(i);
            check = StaticProcs.bytesToUIntInt(bytes, 0);
            if (check != i) {
                System.out.println(" oops:" + check + " != " + i);
                StaticProcs.printBits(bytes[0]);
                StaticProcs.printBits(bytes[1]);
            }
        }
        int a = 65534;
        bytes = StaticProcs.uIntIntToByteWord(a);
        StaticProcs.printBits(bytes[0]);
        StaticProcs.printBits(bytes[1]);
        check = StaticProcs.bytesToUIntInt(bytes, 0);
        System.out.println(check);
        byte[] arbytes = new byte[22];
        arbytes[13] = -127;
        arbytes[14] = 127;
        arbytes[15] = -1;
        arbytes[16] = 127;
        arbytes[17] = -127;
        System.out.println("arbitrary length:");
        StaticProcs.printBits(arbytes[14]);
        StaticProcs.printBits(arbytes[15]);
        StaticProcs.printBits(arbytes[16]);
        byte[] tmp = new byte[4];
        tmp[0] = -127;
        tmp[1] = 127;
        tmp[2] = -49;
        tmp[3] = -1;
        String str2 = "";
        for (int i = 0; i < tmp.length; i++) {
            str2 += StaticProcs.hexOfByte(tmp[i]);
        }
        System.out.println(str2);
        byte temp2[] = str2.getBytes();
        byte temp4[] = new byte[temp2.length / 2];
        byte[] temp3 = new byte[2];
        for (int i = 0; i < temp4.length; i++) {
            temp3[0] = temp2[i * 2];
            temp3[1] = temp2[i * 2 + 1];
            temp4[i] = StaticProcs.byteOfHex(temp3);
        }
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i] == temp4[i]) {
                System.out.println("ok");
            } else {
                System.out.println("nope");
            }
        }
    }
}
