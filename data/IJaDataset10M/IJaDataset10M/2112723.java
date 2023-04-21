package basic;

import java.util.Arrays;

/**
 * @author Axel Terfloth
 */
public class RefByNewArraySource {

    public void foo() {
        Arrays.asList(new RefByNewArrayTarget[0]);
    }
}
