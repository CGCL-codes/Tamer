package utility.testCase;

import java.util.Hashtable;
import utility.ConvertToLower;
import utility.CharAt;
import junit.framework.TestCase;

/**@author
 * This program test a conversion of Lower case charascter to upper case character
**/
public class TestConvertToLower extends TestCase {

    public TestConvertToLower(String name) {
        super(name);
    }

    /**
 * This method Convert lower case character to upper case character 
 **/
    public void testFirstChar() {
        ConvertToLower ctl = new ConvertToLower();
        Hashtable<Object, Object> temp = new Hashtable<Object, Object>();
        Hashtable<Object, Object> expected = new Hashtable<Object, Object>();
        temp.put("SACHIN", "1");
        temp.put("ABCD", "2");
        temp.put("Sahin", "3");
        temp.put("1111", "test");
        temp.put("s1a2c3", "test2");
        temp.put("check", "check");
        temp.put("wikI", "www");
        temp.put("OcEaN", "");
        temp.put("", "pro");
        expected.put("sachin", "1");
        expected.put("abcd", "2");
        expected.put("sahin", "3");
        expected.put("1111", "test");
        expected.put("s1a2c3", "test2");
        expected.put("check", "check");
        expected.put("wiki", "www");
        expected.put("ocean", "");
        expected.put("", "pro");
        Hashtable<Object, Object> actual = ctl.convertHashKey(temp);
        assertEquals(expected, actual);
    }
}
