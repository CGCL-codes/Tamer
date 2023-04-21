package util.basedatatype.collection;

import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import util.basedatatype.string.StringBase;

public class testBaseDateType {

    @Test
    public void Z_test$c$Date() {
        Set set = new HashSet();
        String[] ss = new String[] { "3" };
        set.add(ss);
        String s = "asdasdasd";
        set.add(s);
        Assert.assertTrue(set.contains("asdasdasd"));
        Assert.assertTrue(set.contains("Aasdasdasd".substring(1)));
        Assert.assertTrue(set.contains("as" + "dasdasd"));
        Assert.assertTrue(set.contains(ss));
        Assert.assertTrue(!set.contains(new String[] { "3" }));
        Assert.assertTrue(!(new Integer[] {}).equals(new Integer[] {}));
        String[] sss = new String[] { "a", "ccc", "as", "x", "er" };
        StringBase.sort(sss);
        for (String sDo : sss) {
            System.out.println(sDo);
        }
    }
}
