package net.srcz.jsjvm.optim;

import java.io.File;
import java.io.FileInputStream;
import org.objectweb.asm.ClassReader;

public class Main {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            String className = "net.srcz.jsjvm.demo.Essai2";
            File f = new File("bin\\" + className.replaceAll("\\.", "\\") + ".class");
            ClassReader cr = new ClassReader(new FileInputStream(f));
            MyClassVisitor js = new MyClassVisitor();
            cr.accept(js, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
