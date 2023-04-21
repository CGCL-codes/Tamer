package org.pcorp.space.metier.ordre.test;

import org.pcorp.space.helper.MsgHelper;
import junit.framework.TestCase;

public class TestString extends TestCase {

    public TestString(String arg0) {
        super(arg0);
    }

    public void fonction(String aaa) {
        aaa += "ma chaine de la vie entieremlkhs\nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    }

    public void testChaine() {
        String a = new String();
        a += "bonjour";
        fonction(a);
        System.out.println(a);
        String message = MsgHelper.getMessage("destruction");
        message = MsgHelper.setValue(message, "$1", "name");
        message = MsgHelper.setValue(message, "$2", "erff");
        System.out.println(message);
    }
}
