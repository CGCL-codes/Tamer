package ezinjector.test.junit;

import java.awt.List;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.JPanel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ezinjector.util.PackageUtil;

public class PackageUtilTest {

    private PackageUtil util;

    @Before
    public void setUp() {
        this.util = new PackageUtil();
    }

    @Test
    public void getPackagesStartingWith() {
        Collection<Package> pkgs = Arrays.asList(this.util.getPackagesStartingWith("java.util"));
        Assert.assertTrue(pkgs.contains(Collection.class.getPackage()));
        Assert.assertFalse(pkgs.contains(JPanel.class.getPackage()));
        pkgs = Arrays.asList(this.util.getPackagesStartingWith(""));
        Assert.assertTrue(pkgs.contains(JPanel.class.getPackage()));
    }

    @Test
    public void getClassNameTest() {
        Assert.assertNull(this.util.getClassName(List.class.getName()));
        Assert.assertEquals(List.class.getName(), this.util.getClassName(List.class.getName() + ".class"));
        Assert.assertEquals(List.class.getName(), this.util.getClassName(List.class.getName().replace(".", "/") + ".class"));
        Assert.assertEquals(List.class.getName(), this.util.getClassName("/" + List.class.getName().replace(".", "/") + ".class"));
    }
}
