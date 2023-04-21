package com.dukesoftware.utils.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import javax.swing.ListModel;
import com.dukesoftware.utils.math.Matrix4f;

/**
 * 
 * 
 *
 */
public class SystemPrintOut {

    private SystemPrintOut() {
    }

    private static final PrintOut out = new PrintOut();

    public static final void print(double[] array) {
        out.print(array);
    }

    public static void print(Object[] target) {
        out.print(target);
    }

    public static void print(Map<?, ?> target) {
        out.print(target);
    }

    public static void print(Collection<?> target) {
        out.print(target);
    }

    public static void print(Enumeration<?> en) {
        out.print(en);
    }

    public static void print(Iterator<?> it) {
        out.print(it);
    }

    public static void print(Matrix4f matrix) {
        out.print(matrix);
    }

    public static final void print(ListModel model) {
        out.print(model);
    }

    public static final void print(BufferedReader d) throws IOException {
        out.print(d);
    }

    public static final void printFromated(Object[] array) {
        out.printFromated(array);
    }
}
