package com.coury.dasle.parser;

import junit.framework.Assert;
import org.junit.Test;
import com.coury.dasle.Dasle;

public class ParserTest {

    public static String lastValue;

    @Test
    public void testSimpleDSL() throws Exception {
        Dasle dasle = Dasle.newInstance();
        dasle.registerParser(CrieUsuario.class);
        dasle.registerParser(ColoqueUsuario.class);
        dasle.registerParser(GarantaQue.class);
        dasle.execute("crie usuario A");
        Assert.assertEquals("A", lastValue);
        dasle.execute("coloque usuario A e B no pool");
        dasle.execute("garanta que usuario A tenha sala");
    }
}
