package org.freedesktop.dbus.test;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import org.freedesktop.DBus.Description;
import org.freedesktop.DBus.Method;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.UInt16;

/**
 * A sample remote interface which exports one method.
 */
public interface TestRemoteInterface extends DBusInterface {

    @Description("Test of nested maps")
    public <T> int frobnicate(List<Long> n, Map<String, Map<UInt16, Short>> m, T v);

    /**
   * A simple method with no parameters which returns a String
   */
    @Description("Simple test method")
    public String getName();

    public String getNameAndThrow();

    @Description("Testing object paths as Path objects")
    public void newpathtest(Path p);

    @Description("Interface-overloaded method")
    public int overload();

    public List<Path> pathlistrv(List<Path> a);

    public Map<Path, Path> pathmaprv(Map<Path, Path> a);

    public Path pathrv(Path a);

    @Description("Regression test for #13291")
    public void reg13291(byte[] as, byte[] bs);

    @Description("Testing Type Signatures")
    public void sig(Type[] s);

    @Description("Testing the float type")
    public float testfloat(float[] f);

    @Description("Testing structs of structs")
    public int[][] teststructstruct(TestStruct3 in);

    @Description("Throws a TestException when called")
    public void throwme() throws TestException;

    @Description("Waits then doesn't return")
    @Method.NoReply()
    public void waitawhile();
}
