package net.kano.joustsim.oscar.oscar.service.icbm.dim;

import junit.framework.TestCase;

class DirectimTest extends TestCase {

    protected String makeString(int len) {
        char[] array = new char[len];
        for (int j = 0; j < array.length; j++) {
            array[j] = (char) ('a' + (j % 26));
        }
        return new String(array);
    }
}
