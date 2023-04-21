package net.sf.dobo.sample;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class MyLogicContextObject {

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @MyLogicContext.helloThere
    public String helloThere() {
        System.out.println("How diy");
        return "Howdy";
    }
}
