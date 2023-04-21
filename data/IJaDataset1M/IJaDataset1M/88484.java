package tests.jd;

import com.webdeninteractive.bie.persistent.*;

/**
 *
 * @author  root
 */
public class SQLTest {

    /** Creates a new instance of SQLTest */
    public SQLTest() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String stuff = "asdfa\"asdf";
        String sql = "asdfasd\\f'a" + stuff + "sasdf''asdf";
        System.out.println("sql = " + SQLTools.escape(sql));
    }
}
