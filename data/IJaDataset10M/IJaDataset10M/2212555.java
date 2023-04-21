package ch.superj.jjtree.test;

import java.io.ByteArrayInputStream;
import ch.superj.jjtree.compiler.SJCompiler;
import ch.superj.jjtree.util.SJNode;
import ch.superj.jjtree.util.VarRegistry;

/**
 * @author superj
 *
 * tag: 
 */
public class Test {

    protected void setUp() {
        ByteArrayInputStream bs = new ByteArrayInputStream("".getBytes());
        SJCompiler comp = new SJCompiler(bs);
        comp.setVarRegistry(new VarRegistry());
        comp.setEnvBeans(new EnvBeansTest());
        comp.setInitFactory(new InitFactoryTest());
    }

    public static void main(String args[]) {
        try {
            long start = System.currentTimeMillis();
            String test;
            test = "var test = new StringBean('blabla').getString();" + "var test2 = new StringBean('test');";
            ByteArrayInputStream bs = new ByteArrayInputStream(test.getBytes());
            SJCompiler comp = new SJCompiler(bs);
            comp.setVarRegistry(new VarRegistry());
            comp.setEnvBeans(new EnvBeansTest());
            comp.setInitFactory(new InitFactoryTest());
            SJNode n = (SJNode) comp.Start();
            System.out.println("return " + ((SJNode) n.jjtGetChild(0)).getReturn());
            System.out.println("varregistry test: " + comp.getVarRegistry().find("test"));
            System.out.println("varregistry test2: " + comp.getVarRegistry().find("test2"));
            System.out.println("duration " + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            System.out.println("Oops.");
            e.printStackTrace();
        }
    }
}
