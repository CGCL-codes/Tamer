package net.sf.codecolleague.core;

import net.sf.codecolleague.core.FullyQualifiedName;
import net.sf.codecolleague.core.Path;
import org.junit.Assert;
import org.junit.Test;

public class PathTest {

    @Test
    public void valueObject() {
        Path path1a = new Path("/ape/bear");
        Path path1b = new Path("/ape/bear");
        Path path2 = new Path("/cheetah/dingo");
        Assert.assertTrue("1a == 1b", path1a.equals(path1b));
        Assert.assertFalse("1a == 2", path1a.equals(path2));
        Assert.assertTrue("# 1a == 1b", path1a.hashCode() == path1b.hashCode());
        Assert.assertFalse("# 1a == 2", path1a.hashCode() == path2.hashCode());
        Assert.assertEquals("1a", "/ape/bear", path1a.toString());
        Assert.assertEquals("1b", "/ape/bear", path1b.toString());
        Assert.assertEquals("2", "/cheetah/dingo", path2.toString());
    }

    @Test
    public void fileName() {
        Assert.assertEquals("Empty", new Path(""), new Path("").fileName());
        Assert.assertEquals("Only relative file name", new Path("ape"), new Path("ape").fileName());
        Assert.assertEquals("Only absolute file name", new Path("bear"), new Path("/bear").fileName());
        Assert.assertEquals("Full path", new Path("dingo"), new Path("/cheetah/dingo").fileName());
    }

    @Test
    public void startsWith() {
        Assert.assertFalse("Empty", new Path("").startsWith(new Path("ape")));
        Assert.assertTrue("Exact match", new Path("bear").startsWith(new Path("bear")));
        Assert.assertTrue("Start", new Path("/cheetah/dingo").startsWith(new Path("/cheetah/")));
        Assert.assertFalse("No match", new Path("/elephant/fox").startsWith(new Path("/giraffe/")));
    }

    @Test
    public void after() {
        Assert.assertEquals("No match", new Path(""), new Path("ape").after(new Path("bear")));
        Assert.assertEquals("Exact match", new Path(""), new Path("cheetah").after(new Path("cheetah")));
        Assert.assertEquals("Suffix", new Path("elephant"), new Path("/dingo/elephant").after(new Path("/dingo/")));
        Assert.assertEquals("Suffix without separator", new Path("elephant"), new Path("/dingo/elephant").after(new Path("/dingo")));
    }

    @Test
    public void toFullyQualifiedName() {
        Assert.assertEquals("Fully qualified name", new FullyQualifiedName("ape.bear.Cheetah"), new Path("ape/bear/Cheetah.java").toFullyQualifiedName());
    }

    @Test
    public void hierarchichalConstructor() {
        Assert.assertEquals("With separator", new Path("/ape/bear/cheetah/dingo"), new Path(new Path("/ape/bear"), new Path("/cheetah/dingo")));
        Assert.assertEquals("Without separator", new Path("/ape/bear/cheetah/dingo"), new Path(new Path("/ape/bear"), new Path("cheetah/dingo")));
    }
}
