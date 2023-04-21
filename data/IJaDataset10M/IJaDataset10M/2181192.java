package activejava.vm.step;

import activejava.vm.AJRuntime;
import activejava.vm.AJRuntimeException;
import activejava.vm.Atom;
import activejava.vm.AtomType;
import activejava.vm.ReflectionUtils;

/** 
 * AcitveJava
 *
 * �½���������
 * ��ջ��ȡ��Ҫ�½��ĳ���
 * ������Ϊ������
 *
 * @author �����
 * @email 17bity@gmail.com
 * @since 2006-10-26
 * @version 0.1a
 */
public class NewArrayCommand extends Command {

    public int run(AJRuntime runtime) throws AJRuntimeException {
        Atom lengthAtom = runtime.popRun();
        String className = (String) parameter.getRef();
        int length = ((Integer) runtime.getEntity(lengthAtom)).intValue();
        try {
            Object array = ReflectionUtils.newArray(className, length);
            runtime.pushRun(new Atom(array, AtomType.ENTITY));
        } catch (ClassNotFoundException e) {
            throw new AJRuntimeException("ClassNotFound: " + className, e);
        }
        return 0;
    }

    public int getType() {
        return StepType.NEW_ARRAY_COMMAND;
    }

    public String toString() {
        return "NewArray  " + parameter.getRef();
    }
}
