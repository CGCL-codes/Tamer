package org.jtestcase;

/**
* JTestCaseException is thrown in any cases JTestCase has to report an error to the outside.
* 
* @author <a href="mailto:faustothegrey@sourceforge.net">Fausto Lelli</a>
* @author <a href="mailto:yuqingwang_99@yahoo.com">Yuqing Wang</a>
* @author <a href="mailto:ckoelle@sourceforge.net">Christian Koelle</a>
* 
* $Id: JTestCaseException.java,v 1.4 2005/11/05 11:21:05 faustothegrey Exp $
*/
public class JTestCaseException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
   * @see java.lang.Exception#Exception()
   */
    public JTestCaseException() {
        super();
    }

    /**
   * @see java.lang.Exception#Exception(java.lang.String)
   */
    public JTestCaseException(String s) {
        super(s);
    }

    /**
   * @see java.lang.Exception#Exception(java.lang.Throwable)
   */
    public JTestCaseException(Throwable e) {
        super(e);
    }

    /**
   * @see java.lang.Exception#Exception(java.lang.String, java.lang.Throwable)
   */
    public JTestCaseException(String s, Throwable e) {
        super(s, e);
    }
}
