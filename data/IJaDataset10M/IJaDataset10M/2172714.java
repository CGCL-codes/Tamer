package org.qcmylyn.ui.tests;

import org.junit.Test;

public class TestEscape {

    @Test
    public void testIt() {
        final String name = "1- ����";
        final CharSequence cr = name;
        for (int i = 0; i < name.length(); i++) {
        }
        System.out.println(cr);
    }
}
