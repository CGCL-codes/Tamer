package yuba.env;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import yuba.compiler.relation.DependencyList;
import yuba.util.TimeAna;

/**
 * description
 * 
 * @author hoge1000
 *  
 */
public class SourceStateChecker {

    Set needResolve = new HashSet();

    boolean hasSyntaxError = false;

    /**
     * @param env
     * @throws IOException
     */
    public SourceStateChecker(PackageEnvironment env) throws IOException {
        List s = env.allSourceFiles();
        Set A1 = new HashSet();
        Set A2 = new HashSet();
        TimeAna a4 = new TimeAna("COMP_a");
        for (Iterator it = s.iterator(); it.hasNext(); ) {
            File f = (File) it.next();
            Cache c = Cache.get(f);
            if (!c.isUp2Date()) {
                String name = env.getNameOf(f);
                SingleCompiler sn = new SingleCompiler();
                TimeAna a3 = new TimeAna("COMP");
                CompilerEnvironment cenv = sn.compile2(name);
                cenv.setResolved(env);
                a3.result();
                if (!sn.hasSyntaxError) {
                    if (sn.signatureChanged) {
                        A2.add(name);
                    } else {
                        A1.add(name);
                    }
                    if (!env.typeExists(name)) {
                        env.addType(name, cenv.getTarget());
                    }
                } else hasSyntaxError = true;
                env.getProblems().addAll(cenv.getProblems());
            }
        }
        a4.result();
        TimeAna t3 = new TimeAna("B_add");
        Set B = new HashSet();
        for (Iterator it = A2.iterator(); it.hasNext(); ) {
            String name = (String) it.next();
            Iterator bit = DependencyList.instance.getFrom(name);
            while (bit.hasNext()) {
                String bs = (String) bit.next();
                if (!A1.contains(bs) && !A2.contains(bs)) {
                    B.add(bs);
                }
            }
        }
        t3.result();
        System.out.println("List of A1");
        for (Iterator it = A1.iterator(); it.hasNext(); ) {
            String e = (String) it.next();
            System.out.println(e);
        }
        System.out.println("List of A2");
        for (Iterator it = A2.iterator(); it.hasNext(); ) {
            String e = (String) it.next();
            System.out.println(e);
        }
        System.out.println("List of B");
        for (Iterator it = B.iterator(); it.hasNext(); ) {
            String e = (String) it.next();
            System.out.println(e);
        }
        t3 = new TimeAna("ReCOMP");
        for (Iterator it = B.iterator(); it.hasNext(); ) {
            String name = (String) it.next();
            SingleCompiler sn = new SingleCompiler();
            CompilerEnvironment cenv = sn.compile2(name);
            if (cenv != null) {
                if (sn.signatureChanged) {
                    A2.add(name);
                } else {
                    A1.add(name);
                }
                cenv.setResolved(env);
                if (!env.typeExists(name)) {
                    env.addType(name, cenv.getTarget());
                }
            } else {
                System.out.println("??? Fatal error : somewhy " + name + " is out of date.");
            }
        }
        t3.result();
        B.addAll(A1);
        B.addAll(A2);
        needResolve = B;
    }
}
