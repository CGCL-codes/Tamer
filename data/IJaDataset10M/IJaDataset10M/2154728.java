package jp.hpl.main.test;

import src.backend.wad.WadTool;

public class TestPrintFlags {

    public static void main(String[] args) {
        doSome();
    }

    private static void doSome() {
        short flags = 0;
        flags = 0x4c00;
        short target = 0x4000;
        System.out.println("flags:" + Integer.toBinaryString(flags) + "=" + Integer.toHexString(flags));
        short reverse = (short) ~target;
        System.out.println("~0x4000 = " + Integer.toBinaryString(reverse) + "=" + Integer.toHexString(reverse));
        short test = (short) (flags & (~target));
        System.out.println("flags & ~0x4000:" + Integer.toBinaryString(test) + "=" + Integer.toHexString(test));
        flags = WadTool.addOrRemoveFlag(flags, (short) target, false);
        System.out.println("flags:" + Integer.toBinaryString(flags) + "=" + Integer.toHexString(flags));
    }
}
