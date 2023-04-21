package com.pk.test.util;

import com.pk.platform.util.StringConverter;

public class TestStringConverter {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Double d = 1.564;
        System.out.println(StringConverter.toNBitString(d, 0));
    }
}
