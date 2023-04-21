package org.tockit.cernatoXML.model.tests;

import java.util.Iterator;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.tockit.cernatoXML.model.CernatoObject;
import org.tockit.cernatoXML.model.Criterion;
import org.tockit.cernatoXML.model.View;
import org.tockit.cernatoXML.model.ViewContext;

public class ViewContextTest extends TestCase {

    static final Class<ViewContextTest> THIS = ViewContextTest.class;

    public ViewContextTest(String s) {
        super(s);
    }

    public static Test suite() {
        return new TestSuite(THIS);
    }

    public void testScaling() {
        checkView(TestData.View1);
        checkView(TestData.View2);
        checkView(TestData.View3);
    }

    private void checkView(View view) {
        ViewContext scaledContext = new ViewContext(TestData.Model.getContext(), view);
        Set<CernatoObject> objects = scaledContext.getObjects();
        Set<Criterion> attributes = scaledContext.getAttributes();
        for (Iterator<CernatoObject> it1 = objects.iterator(); it1.hasNext(); ) {
            CernatoObject object = it1.next();
            for (Iterator<Criterion> it2 = attributes.iterator(); it2.hasNext(); ) {
                Criterion criterion = it2.next();
                assertEquals(object.getName() + " x " + criterion.getProperty().getName() + ":" + criterion.getValueGroup().getName(), TestData.isInScaledRelation(object, criterion), scaledContext.getRelation().contains(object, criterion));
            }
        }
    }
}
