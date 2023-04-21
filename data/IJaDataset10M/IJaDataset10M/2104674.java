package br.ucam.kuabaSubsystem.kuabaModel;

import java.util.ArrayList;
import java.util.List;
import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.Method;
import com.hp.hpl.jena.util.FileUtils;

public class MethodTest extends AbstractKuabaTestCase {

    public void test_get_has_name() {
        Method oo = this.factory.getMethod("OO");
        assertEquals("Object Oriented", oo.getHasName());
    }

    public void test_set_has_name() {
        Method oo = this.factory.getMethod("OO");
        oo.setHasName("o.o.");
        assertEquals("o.o.", oo.getHasName());
        List<String> errors = new ArrayList<String>();
        this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
        assertTrue(errors.isEmpty());
        oo = this.factory.getMethod("OO");
        assertEquals("o.o.", oo.getHasName());
    }
}
