package org.olga.rebus.tests;

import java.io.File;
import java.io.FilenameFilter;
import org.olga.test.ITest;

/**
 * ����� ��� ������������ ���������.
 * 
 * @author Olga
 *
 */
public class Run {

    /**
	 * �������� �����. ��������� ��� ����� �� �������� 
	 * "bin/org/olga/rebus/tests"
	 * 
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        File dirFile = new File("bin/org/olga/rebus/tests");
        File[] files = dirFile.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String s) {
                if (s.endsWith(".class")) {
                    return true;
                }
                return false;
            }
        });
        Class[] classes = new Class[files.length];
        int i = 0;
        for (File f : files) {
            String str = f.getName();
            str = str.substring(0, str.length() - 6);
            classes[i++] = Class.forName("org.olga.rebus.tests." + str);
        }
        boolean showPassed = false;
        int passedCounter = 0;
        int failedCounter = 0;
        for (Class c : classes) {
            Class[] interfaces = c.getInterfaces();
            for (Class inter : interfaces) {
                if (inter.getSimpleName().equals("ITest")) {
                    ITest test = (ITest) c.getConstructor().newInstance();
                    if (test.run()) {
                        if (showPassed) {
                            System.out.println(test.name() + ": passed");
                        }
                        passedCounter++;
                    } else {
                        System.out.println(test.name() + ": failed");
                        failedCounter++;
                    }
                }
                break;
            }
        }
        System.out.println("Total: " + (passedCounter + failedCounter));
        System.out.println("Passed: " + passedCounter);
        System.out.println("Failed: " + failedCounter);
    }
}
