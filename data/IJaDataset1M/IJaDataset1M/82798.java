package samples.reflect;

import alt.jiapi.reflect.*;
import alt.jiapi.file.Method;

/**
 * Class Sample1.
 * 
 * @author Mika Riekkinen
 */
public class Sample1 {

    public static void main(String[] args) throws Exception {
        JiapiClass jc = JiapiClass.createClass("sample");
        short mods = Method.ACC_PUBLIC + Method.ACC_STATIC;
        Signature signature = new Signature("void", new String[] { "java.lang.String[]" });
        JiapiMethod jm = jc.addMethod(mods, "main", signature);
        InstructionList il = jm.getInstructionList();
        InstructionFactory factory = jm.getInstructionFactory();
        il.add(factory.returnMethod(jm));
        jc.dump(new java.io.FileOutputStream("sample.clazz"));
    }
}
