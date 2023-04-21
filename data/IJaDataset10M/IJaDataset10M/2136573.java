package org.progeeks.osg;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import org.progeeks.util.*;

/**
 *  Knows enough about the OsgObject hierarchy to do prettier
 *  debug output.
 *
 *  @version   $Revision: 1.2 $
 *  @author    Paul Speed
 */
public class DebugWriter extends IndentPrintWriter {

    public DebugWriter(Writer out) {
        super(out);
    }

    protected void printSpecial(Object val) {
        if (val instanceof OsgObject) {
            print((OsgObject) val);
        } else if (val instanceof List) {
            print((List) val);
        } else if (val == null) {
            println("null");
        } else if (val.getClass().isArray()) {
            printArray(val);
        } else if (val instanceof Map) {
            print((Map) val);
        } else {
            println(val);
        }
    }

    public void print(List list) {
        println(list.size());
        if (list.size() == 0) return;
        println("[");
        pushIndent();
        for (Iterator i = list.iterator(); i.hasNext(); ) {
            printSpecial(i.next());
        }
        popIndent();
        println("]");
    }

    public void printArray(Object array) {
        printArray(array, true);
    }

    protected void printArray(Object array, boolean decorate) {
        if (Array.getLength(array) == 0) {
            println(Array.getLength(array) + " " + array.getClass().getSimpleName());
            return;
        }
        Class elementType = array.getClass().getComponentType();
        if (decorate) {
            println(Array.getLength(array) + " " + array.getClass().getSimpleName());
            println("[");
        } else {
            print("[");
        }
        pushIndent();
        if (elementType.isPrimitive()) {
            if (Byte.TYPE.equals(elementType)) {
                for (int i = 0; i < Array.getLength(array); i++) {
                    if (i > 0 && (i % 32) == 0) println();
                    Byte b = (Byte) Array.get(array, i);
                    String s = Integer.toHexString(b.byteValue() & 0xff);
                    if (s.length() == 1) s = "0" + s;
                    print(s);
                }
                if (decorate) println();
            } else {
                for (int i = 0; i < Array.getLength(array); i++) {
                    if (i > 0 && (i % 10) == 0) println(); else if (i > 0) print(" ");
                    print(Array.get(array, i));
                }
                if (decorate) println();
            }
        } else if (elementType.isArray()) {
            for (int i = 0; i < Array.getLength(array); i++) {
                printArray(Array.get(array, i), false);
            }
        } else {
            for (int i = 0; i < Array.getLength(array); i++) printSpecial(Array.get(array, i));
        }
        popIndent();
        println("]");
    }

    public void print(OsgObject obj) {
        print(obj.getType() + " v_" + obj.getVersion());
        println(" {");
        pushIndent();
        for (Map.Entry e : obj.entrySet()) {
            print(e.getKey() + " ");
            printSpecial(e.getValue());
        }
        popIndent();
        println("}");
    }

    public void print(Map map) {
        println("{");
        pushIndent();
        for (Iterator i = map.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry) i.next();
            print(e.getKey() + " ");
            printSpecial(e.getValue());
        }
        popIndent();
        println("}");
    }
}
