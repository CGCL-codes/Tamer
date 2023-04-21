package org.progeeks.meta.xml;

import java.io.*;
import java.util.*;
import org.progeeks.meta.*;
import org.progeeks.meta.beans.*;
import org.progeeks.util.log.*;
import org.progeeks.util.xml.*;

/**
 *  Command-line test application for reading XML files and dumping
 *  their toString() objects.
 *
 *  @version   $Revision: 1.2 $
 *  @author    Paul Speed
 */
public class MetaObjectHandlerTester {

    public static void main(String[] args) throws Exception {
        Log.initialize();
        if (args.length < 1) {
            System.out.println("Usage: MetaObjectHandlerTester file [+prefix [+prefix]] [class [class]]");
            return;
        }
        String file = args[0];
        MetaObjectHandler handler = new MetaObjectHandler(BeanUtils.getMetaKit());
        ObjectXmlReader reader = new ObjectXmlReader();
        reader.addObjectHandler(handler);
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("+")) {
                handler.addClassPrefix(args[i].substring(1));
            } else {
                Class c = Class.forName(args[i]);
                System.out.println("Found class:" + c);
                MetaClass metaClass = BeanUtils.createBeanMetaClass(handler.getMetaClassRegistry(), c);
                System.out.println("Imported class:" + metaClass);
                for (Iterator j = metaClass.getPropertyInfos().iterator(); j.hasNext(); ) {
                    System.out.println("   " + j.next());
                }
            }
        }
        FileReader in = new FileReader(file);
        try {
            Object obj = reader.readObject(in);
            if (obj instanceof Collection) {
                System.out.println("Read:");
                for (Iterator i = ((Collection) obj).iterator(); i.hasNext(); ) {
                    System.out.println(i.next());
                }
            } else {
                System.out.println("Read MetaObject:" + obj);
                System.out.println("    Real Object:" + BeanUtils.getMetaKit().getInternalObject((MetaObject) obj));
            }
        } finally {
            in.close();
        }
    }
}
