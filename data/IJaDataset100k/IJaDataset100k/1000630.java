package japatest;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import java.io.FileInputStream;

public class JavaTest {

    public static void main(String[] args) throws Exception {
        String file = "/home/saizhang/workspace/Palus-SVN/experiments/japatest/JavaTest.java";
        FileInputStream in = new FileInputStream(file);
        CompilationUnit cu;
        try {
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }
        dummyCall(10);
        dummyCall(10l);
    }

    public static double dummyCall(int i) {
        return 10d;
    }

    public static long dummyCall(long i) {
        return 10l;
    }
}
