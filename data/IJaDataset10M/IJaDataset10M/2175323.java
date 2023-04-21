package SimulatorPack;

import java.util.StringTokenizer;
import Project.Assembled;

public class Memory {

    public static int memory(String location) {
        location = location.trim();
        if (location.equals("$zero") || location.equals("$0") || location.equals("0")) return 0;
        if (location.equals("$at") || location.equals("$1") || location.equals("1")) return 1;
        if (location.equals("$v0") || location.equals("$2") || location.equals("2")) return 2;
        if (location.equals("$v1") || location.equals("$3") || location.equals("3")) return 3;
        if (location.equals("$a0") || location.equals("$4") || location.equals("4")) return 4;
        if (location.equals("$a1") || location.equals("$5") || location.equals("5")) return 5;
        if (location.equals("$a2") || location.equals("$6") || location.equals("6")) return 6;
        if (location.equals("$a3") || location.equals("$7") || location.equals("7")) return 7;
        if (location.equals("$t0") || location.equals("$8") || location.equals("8")) return 8;
        if (location.equals("$t1") || location.equals("$9") || location.equals("9")) return 9;
        if (location.equals("$t2") || location.equals("$10") || location.equals("10")) return 10;
        if (location.equals("$t3") || location.equals("$11") || location.equals("11")) return 11;
        if (location.equals("$t4") || location.equals("$12") || location.equals("12")) return 12;
        if (location.equals("$t5") || location.equals("$13") || location.equals("13")) return 13;
        if (location.equals("$t6") || location.equals("$14") || location.equals("14")) return 14;
        if (location.equals("$t7") || location.equals("$15") || location.equals("15")) return 15;
        if (location.equals("$s0") || location.equals("$16") || location.equals("16")) return 16;
        if (location.equals("$s1") || location.equals("$17") || location.equals("17")) return 17;
        if (location.equals("$s2") || location.equals("$18") || location.equals("18")) return 18;
        if (location.equals("$s3") || location.equals("$19") || location.equals("19")) return 19;
        if (location.equals("$s4") || location.equals("$20") || location.equals("20")) return 20;
        if (location.equals("$s5") || location.equals("$21") || location.equals("21")) return 21;
        if (location.equals("$s6") || location.equals("$22") || location.equals("22")) return 22;
        if (location.equals("$s7") || location.equals("$23") || location.equals("23")) return 23;
        if (location.equals("$t8") || location.equals("$24") || location.equals("24")) return 24;
        if (location.equals("$t9") || location.equals("$25") || location.equals("25")) return 25;
        if (location.equals("$k0") || location.equals("$26") || location.equals("26")) return 26;
        if (location.equals("$k1") || location.equals("$27") || location.equals("27")) return 27;
        if (location.equals("$gp") || location.equals("$28") || location.equals("28")) return 28;
        if (location.equals("$sp") || location.equals("$29") || location.equals("29")) return 29;
        if (location.equals("$fp") || location.equals("$30") || location.equals("30")) return 30;
        if (location.equals("$ra") || location.equals("$31") || location.equals("31")) return 31;
        return -1;
    }

    public static int[] calculateMemoryLocation(String memoryAndOffset, Assembled assembled) {
        int position = assembled.getLabelAddress(memoryAndOffset);
        int[] output = new int[2];
        if (position != -1) {
            output = new int[1];
            output[0] = position;
        } else {
            StringTokenizer tokenizer = new StringTokenizer(memoryAndOffset, " ,\t()");
            output[0] = Integer.parseInt(tokenizer.nextToken());
            output[1] = memory(tokenizer.nextToken());
        }
        return output;
    }

    public static String calculateMemoryLocation(Assembled assembled, String memoryAndOffset) {
        int position = assembled.getLabelAddress(memoryAndOffset);
        String output = "";
        if (position != -1) output = "" + position; else {
            StringTokenizer tokenizer = new StringTokenizer(memoryAndOffset, " ,\t()");
            output = tokenizer.nextToken() + " " + memory(tokenizer.nextToken());
        }
        return output;
    }
}
