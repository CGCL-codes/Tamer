package name.nirav.opath.reflect;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import name.nirav.opath.OPathInterpreter;
import name.nirav.opath.Variable;
import name.nirav.opath.parse.OPathInterpreterTest;
import name.nirav.opath.reflect.serializable.ReflectValue;
import name.nirav.opath.reflect.serializable.ReflectVariable;

/**
 * @author Nirav Thaker
 * 
 */
public class MainTest {

    @Test
    public void testFacade() throws Exception {
        final A a = new A();
        a.b = new B();
        a.y = "Xyz";
        C c = new C();
        c.c = 'Z';
        a.b.mems.add(c);
        assertEquals(c.c, OPathReflectiveInterpreter.findAll(a, "//c").iterator().next());
    }

    @Test
    public void testReflect() throws Exception {
        final A a = new A();
        a.b = new B();
        a.y = "Xyz";
        C c = new C();
        c.c = 'Z';
        a.b.mems.add(c);
        ReflectVariable context = new ReflectVariable("a") {

            @Override
            public List<Variable> getChildren() {
                Field[] fields = a.getClass().getDeclaredFields();
                List<Variable> list = new ArrayList<Variable>();
                for (Field field : fields) {
                    list.add(ReflectVariable.create(field, a));
                }
                return list;
            }
        };
        OPathInterpreter intp;
        context.setValue(new ReflectValue(a, context));
        OPathInterpreterTest.prettyPrint(context);
        intp = new OPathInterpreter();
        intp.evaluate("//c", context);
        Collection<Variable> result = intp.getResult();
        assertEquals(c.c, result.iterator().next().getValue().getValue());
        context.setValue(new ReflectValue(a, context));
        intp = new OPathInterpreter();
        intp.evaluate("//y[count>1]", context);
        result = intp.getResult();
        assertEquals(a.y, result.iterator().next().getValue().getValue());
    }
}
