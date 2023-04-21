package test;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public class Fail {

    public interface Kernel32 extends StdCallLibrary {

        Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);

        Pointer CreateToolhelp32Snapshot(int dwFlags, int th32ProcessID);
    }

    public static void main(String[] args) {
        System.out.println(Kernel32.INSTANCE.CreateToolhelp32Snapshot(0x00000002, 4504));
    }
}
