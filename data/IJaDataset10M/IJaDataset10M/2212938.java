package net.sf.beezle.mork.compiler.tests.sideeffect;

import net.sf.beezle.mork.mapping.Mapper;
import java.io.StringReader;

/**
 * Test env arguments.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Mapper mapper;
        Object[] result;
        mapper = new Mapper("net.sf.beezle.mork.compiler.tests.sideeffect.Mapper");
        mapper.setLogging(null, System.out);
        result = mapper.run("<const>", new StringReader("abbb"));
        System.out.println("result: " + result[0]);
    }

    public static StringBuffer copy(StringBuffer a) {
        return a;
    }

    public static StringBuffer create() {
        return new StringBuffer("a");
    }

    public static int add(StringBuffer buffer) {
        buffer.append("b");
        return 0;
    }
}
