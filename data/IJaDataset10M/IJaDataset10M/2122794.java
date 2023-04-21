package org.databene.benerator.distribution;

import static org.junit.Assert.*;
import java.util.List;
import org.databene.benerator.test.BeneratorIntegrationTest;
import org.databene.benerator.test.ConsumerMock;
import org.databene.model.data.Entity;
import org.junit.Test;

/**
 * Tests the definition of custom weight functions.<br/><br/>
 * Created: 09.07.2010 07:23:54
 * @since 0.6.3
 * @author Volker Bergmann
 */
public class CustomWeightFunctionIntegrationTest extends BeneratorIntegrationTest {

    String xml = "<generate type='entity' count='1000' consumer='cons'>" + "	<attribute name='c' values=\"'a', 'b', 'c'\" " + "distribution='new " + StandardWeightingFunction.class.getName() + "(50,30,20)' />" + "</generate>";

    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        ConsumerMock consumer = new ConsumerMock(true);
        context.set("cons", consumer);
        parseAndExecute(xml);
        List<Entity> products = (List<Entity>) consumer.getProducts();
        assertEquals(1000, products.size());
        int a = 0, b = 0, c = 0;
        for (Entity e : products) {
            String val = (String) e.get("c");
            switch(val.charAt(0)) {
                case 'a':
                    a++;
                    break;
                case 'b':
                    b++;
                    break;
                case 'c':
                    c++;
                    break;
                default:
                    fail("expected 'a', 'b' or 'c', found: " + val.charAt(0));
            }
        }
        assertTrue(a > b);
        assertTrue(b > c);
    }
}
