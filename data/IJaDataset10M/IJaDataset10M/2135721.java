package com.wendal.dex.simple.easy.string;

public class StringClass {

    public void get() {
        String DD = "A\"";
        String D1 = "A\"";
        String D2 = "A\b\n\"";
        String D3 = "A\"\n\"\"\"\"\"\\sdfgsfd\n\"\"\"";
        String D4 = "A\"\\\n\\\\\\\\\\\\n\\\\\\sdfgsd\"\"\\";
        String D5 = "A\"\"\"\n\"\"\n\"\"\n\"\"\n\"\"\n\"\"\n\"\"\n\"\"\n";
        System.out.println(DD + D1 + D2 + D3 + D4 + D5);
    }
}
