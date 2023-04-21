package org.ponder.groovy.builders.pdf;

import net.sourceforge.javautil.groovy.builder.standard.StandardStack;
import org.ponder.groovy.builders.pdf.itext.ItextElement;
import org.ponder.groovy.builders.pdf.itext.ItextParentObject;
import org.ponder.groovy.builders.pdf.itext.OutputText;

/**
 * This implements a print(Object) and println(Object) which directs those methods to be executed on the
 * current node (element). Also we call start() and finish() on each element as they are respectively push()'ed
 * and pop()'ed off the stack.
 * 
 * @author elponderador
 *
 */
public class PDFStack extends StandardStack<ItextElement> {

    /**
	 * This will pass the object to print onto the current element.
	 * 
	 * @param o Content to print
	 */
    public void print(Object o) {
        if (this.getCurrent() instanceof ItextParentObject) {
            this.push(new OutputText(((ItextParentObject) this.getCurrent()), String.valueOf(o), false));
            this.pop();
        } else {
            throw new IllegalArgumentException("Cannot print() on this object: " + this.getCurrent());
        }
    }

    /**
	 * This will pass the object to print onto the current element
	 * 
	 * @param o Content to print
	 */
    public void println(Object o) {
        if (this.getCurrent() instanceof ItextParentObject) {
            this.push(new OutputText(((ItextParentObject) this.getCurrent()), String.valueOf(o), true));
            this.pop();
        } else {
            throw new IllegalArgumentException("Cannot print() on this object: " + this.getCurrent());
        }
    }
}
