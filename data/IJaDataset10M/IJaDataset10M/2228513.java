package org.jikesrvm;

import org.jikesrvm.classloader.VM_Atom;
import org.jikesrvm.classloader.VM_Class;
import org.jikesrvm.classloader.VM_Member;

/**
 * This interface is implemented by org.jikesrvm.VM_PrintContainer.  The
 * interfaces is used by our java.lang.Throwable to print stack traces.
 */
@SuppressWarnings("unused")
public abstract class VM_PrintLN {

    public boolean isSysWrite() {
        return false;
    }

    public boolean isSystemErr() {
        return false;
    }

    public abstract void flush();

    public abstract void println();

    public void println(String s) {
        print(s);
        println();
    }

    public abstract void print(String s);

    static final int max_int_pow10 = 1000000000;

    public void print(int n) {
        boolean suppress_leading_zero = true;
        if (n == 0x80000000) {
            print("-2147483648");
            return;
        } else if (n == 0) {
            print('0');
            return;
        } else if (n < 0) {
            print('-');
            n = -n;
        }
        for (int p = max_int_pow10; p >= 1; p /= 10) {
            int digit = n / p;
            n -= digit * p;
            if (digit == 0 && suppress_leading_zero) {
                continue;
            }
            suppress_leading_zero = false;
            char c = (char) ('0' + digit);
            print(c);
        }
    }

    public void printHex(int n) {
        print("0x");
        for (int i = 32 - 4; i >= 0; i -= 4) {
            int digit = (n >>> i) & 0x0000000F;
            char c;
            if (digit <= 9) {
                c = (char) ('0' + digit);
            } else {
                c = (char) ('A' + (digit - 10));
            }
            print(c);
        }
    }

    public abstract void print(char c);

    /** Print the name of the class represented by the class descriptor.
   *
   * @param descriptor The class descriptor whose name we'll print. */
    public void printClassName(VM_Atom descriptor) {
        byte[] val = descriptor.toByteArray();
        if (VM.VerifyAssertions) {
            VM._assert(val[0] == 'L' && val[val.length - 1] == ';');
        }
        for (int i = 1; i < val.length - 1; ++i) {
            char c = (char) val[i];
            if (c == '/') {
                print('.');
            } else {
                print(c);
            }
        }
    }

    public void print(VM_Class class_) {
        VM_Atom descriptor = class_.getDescriptor();
        printClassName(descriptor);
    }

    public void print(VM_Member m) {
        print(m.getDeclaringClass());
        print('.');
        print(m.getName());
        print(' ');
        print(m.getDescriptor());
    }

    public void print(VM_Atom a) {
        byte[] val;
        if (a != null) {
            val = a.toByteArray();
            for (byte aVal : val) {
                print((char) aVal);
            }
        } else {
            print("(null)");
        }
    }
}
