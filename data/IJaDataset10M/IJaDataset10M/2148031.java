package org.primordion.xholon.app;

/**
 * Xholon launcher. A clone of this class can be used to specialize Xhn.
 * This will be especially useful if you are creating your own project
 * outside of the Xholon project.
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.8 (Created on May 7, 2009)
 */
public class XhnSpecial {

    public static void main(String[] args) {
        Xhn xhn = new Xhn();
        xhn.launch(args, "Special Model");
    }
}
