package net.sourceforge.jxweb;

import java.text.MessageFormat;

/**
 * This test step is an interface to the <code>java.text.MessageFormat</code>
 * class. It gives the scripter the ability to print out some parameterized
 * messages to standard out. This is mostly useful for debugging when you want
 * to look at the value of test properties that have been set.
 * @author  Alex Vollmer
 */
public class MessageStep extends HttpStep {

    /** The main text of your message */
    public String text;

    /** The list of test property names */
    public java.util.ArrayList values = new java.util.ArrayList();

    /**
     * Overrides parent method
     */
    public void eval(net.sourceforge.jxunit.JXTestCase jXTestCase) throws java.lang.Throwable {
        super.eval(jXTestCase);
        values = (java.util.ArrayList) values.get(0);
        Object[] paramValues = new Object[values.size()];
        for (int i = 0; i < values.size(); i++) {
            paramValues[i] = properties.get((String) values.get(i));
        }
        System.out.println(MessageFormat.format(text, paramValues));
    }
}
