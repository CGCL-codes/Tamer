package cs;

import com.puppycrawl.tools.checkstyle.BaseCheckTestCase;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;

/**
 * When a method return an array, you can return a zero length array instead of
 * null.
 * 
 * <p>So instead of:
 * <pre>
 * public MyClass[] getMethod1() {
 * 	if (someCondition()) {
 * 		return null;
 * 	}
 * 	...
 * }
 * </pre>
 * 
 * You can use:
 *  <pre>
 * public MyClass[] getMethod1() {
 * 	if (someCondition()) {
 * 		return new MyClass[0];
 * 	}
 * 	...
 * }
 * </pre>
 * 
 * @author Arnaud Roques
 *  
 */
public class ReturnZeroLengthArrayTest extends BaseCheckTestCase {

    public void testDefault() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(ReturnZeroLengthArray.class);
        final String[] expected = { "6:9: Return a zero length array instead of null." };
        verify(checkConfig, getPath("testinputs/InputReturnZeroLengthArray.java"), expected);
    }
}
