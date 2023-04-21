package javad.jconst;

import java.io.*;
import javad.util.*;

public class constPool extends dataRead implements constPoolTags {

    private int constPoolCnt;

    private constBase constPool[] = null;

    public constPool(DataInputStream dStream) {
        constPoolCnt = readU2(dStream);
        constPool = new constBase[constPoolCnt];
        readConstPool(dStream);
        resolveConstPool();
    }

    public constBase constPoolElem(int ix) {
        constBase elem = null;
        if (ix > 0 && ix < constPool.length) {
            elem = constPool[ix];
        }
        return elem;
    }

    private constBase allocConstEntry(int tag) {
        constBase obj = null;
        switch(tag) {
            case CONSTANT_Utf8:
                obj = new constUtf8();
                break;
            case CONSTANT_Integer:
                obj = new constInt();
                break;
            case CONSTANT_Float:
                obj = new constFloat();
                break;
            case CONSTANT_Long:
                obj = new constLong();
                break;
            case CONSTANT_Double:
                obj = new constDouble();
                break;
            case CONSTANT_Class:
            case CONSTANT_String:
                obj = new constClass_or_String();
                break;
            case CONSTANT_Fieldref:
            case CONSTANT_Methodref:
            case CONSTANT_InterfaceMethodref:
                obj = new constRef();
                break;
            case CONSTANT_NameAndType:
                obj = new constName_and_Type_info();
                break;
            default:
                System.out.println("allocConstEntry: bad tag value = " + tag);
                break;
        }
        if (obj != null) {
            obj.tag = tag;
        }
        return obj;
    }

    private void resolveConstPool() {
        for (int i = 1; i < constPoolCnt; i++) {
            if (constPool[i] != null) constPool[i].set_ref(constPool);
        }
    }

    /**
     Read the JVM class file constant pool and put
     it in the internal constant pool.
<p>
     There is a special case in constant pool construction.
     CONSTANT_Long_info and CONSTANT_Double_info structures
     take up tow elements in the constant pool table 
     (constPool).  See JVM Spec. 4.4.5.  As noted in a footnote
     "In retrospect, making 8-byte constants take two constant
     pool entries was a poor choice."  The extra constant pool
     entry is unused (and is set to null here).
  */
    private void readConstPool(DataInputStream dStream) {
        int tag;
        constBase constObj;
        for (int i = 1; i < constPoolCnt; i++) {
            tag = readU1(dStream);
            if (tag > 0) {
                constObj = allocConstEntry(tag);
                constObj.read(dStream);
                constPool[i] = constObj;
                if (constObj instanceof constLong || constObj instanceof constDouble) {
                    i++;
                    constPool[i] = null;
                }
            } else {
                System.out.println("readConstPool: tag == 0 at constPool index " + i);
            }
        }
    }

    public void pr() {
        System.out.println("constant pool count = " + constPoolCnt + " (size = " + (constPoolCnt - 1) + ")");
        for (int i = 1; i < constPoolCnt; i++) {
            System.out.print(i + " ");
            if (constPool[i] != null) {
                constPool[i].pr();
                System.out.println();
            }
        }
    }
}
