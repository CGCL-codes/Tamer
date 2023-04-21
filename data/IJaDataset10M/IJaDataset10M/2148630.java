package swarm;

import java.io.IOException;
import gnu.bytecode.ClassType;
import gnu.bytecode.Access;
import gnu.bytecode.Type;
import gnu.bytecode.Method;

public class ProxyClassLoader extends ClassLoader {

    public Class findClass(String name) {
        try {
            ClassType clas = new ClassType(name);
            ClassType superClass = ClassType.make("swarm.ObjCProxy");
            clas.setSuper(superClass);
            Method constructor = clas.addMethod("<init>", Type.typeArray0, Type.void_type, Access.PUBLIC);
            Method superConstructor = superClass.addMethod("<init>", Type.typeArray0, Type.void_type, Access.PUBLIC);
            constructor.init_param_slots();
            gnu.bytecode.CodeAttr code = constructor.getCode();
            code.emitPushThis();
            code.emitInvokeSpecial(superConstructor);
            code.emitReturn();
            byte[] b = clas.writeToArray();
            return defineClass(name, b, 0, b.length);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
        return null;
    }
}
