package net.sf.hdkp.data.io;

import static org.junit.Assert.assertEquals;
import java.io.*;
import java.util.Date;
import org.junit.Test;
import net.sf.hdkp.data.*;

public class LuaWriterTest {

    private Data createTestData() {
        final Data data = new Data(42, 7, new Date(1255253103635L));
        data.addToon(new Toon("aOne", "A", 1));
        data.addToon(new Toon("bTwo", "B", 2));
        data.addToon(new Toon("cThree", "C", 3));
        return data;
    }

    private String expectedOutput() {
        final String[] lines = new String[] { "HydraDKP_Data = {", "	[\"date\"] = \"2009-10-11 11:25\",", "	[\"dkp\"] = 42,", "	[\"raids\"] = 7,", "	[\"toons\"] = {", "		[\"aOne\"] = {", "			[\"type\"] = \"A\",", "			[\"dkp\"] = 1,", "		},", "		[\"bTwo\"] = {", "			[\"type\"] = \"B\",", "			[\"dkp\"] = 2,", "		},", "		[\"cThree\"] = {", "			[\"type\"] = \"C\",", "			[\"dkp\"] = 3,", "		},", "	},", "}" };
        final String LN = System.getProperty("line.separator");
        final StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line).append(LN);
        }
        return sb.toString();
    }

    @Test
    public void testWriteData() throws IOException {
        final Data data = createTestData();
        final StringWriter swr = new StringWriter();
        final LuaWriter wr = new LuaWriter(new PrintWriter(swr));
        wr.writeData(data);
        wr.flush();
        assertEquals(expectedOutput(), swr.toString());
    }
}
