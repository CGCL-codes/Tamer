package net.sf.jguard.core.principals;

import org.junit.Test;

public class RolePrincipalTest {

    @Test
    public void testClone() throws CloneNotSupportedException {
        RolePrincipal rolePrincipal = new RolePrincipal("myrole");
        RolePrincipal clonedRolePrincipal = (RolePrincipal) rolePrincipal.clone();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorsWithOneNullParameter() {
        RolePrincipal rolePrincipal = new RolePrincipal(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorsWithOneEmptyParameter() {
        RolePrincipal rolePrincipal = new RolePrincipal("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorsWithtwoNullParameter() {
        RolePrincipal rolePrincipal = new RolePrincipal(null, (String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorsWithtwoEmptyParameter() {
        RolePrincipal rolePrincipal = new RolePrincipal("", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorsWithOneFilledAndOneEmptyParameter() {
        RolePrincipal rolePrincipal = new RolePrincipal("zeze", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorsWithOneEmptyAndOneFilledParameter() {
        RolePrincipal rolePrincipal = new RolePrincipal("", "zeze");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorsWithOneNullAndOneFilledParameter() {
        RolePrincipal rolePrincipal = new RolePrincipal(null, "zeze");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorsWithOneFilledAndOneNullParameter() {
        RolePrincipal rolePrincipal = new RolePrincipal("zeze", (String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructors1() {
        RolePrincipal rolePrincipal = new RolePrincipal(null, (String) null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructors2() {
        RolePrincipal rolePrincipal = new RolePrincipal("", (String) null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructors3() {
        RolePrincipal rolePrincipal = new RolePrincipal("", "", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructors4() {
        RolePrincipal rolePrincipal = new RolePrincipal("", "", new Organization());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructors5() {
        RolePrincipal rolePrincipal = new RolePrincipal("sdfsd", "", new Organization());
    }
}
