package org.aoplib4j.uml;

import java.io.IOException;

/**
 * Class implementing the Depth-first search (DFS)
 * (http://en.wikipedia.org/wiki/Depth-first_search) on the sequence method.
 * (since a {@link SequenceMethod} is a tree structure).
 * 
 * This class represents the AbstractClass from the Template Method Pattern
 * (http://en.wikipedia.org/wiki/Template_method_pattern).
 * 
 * Every subclass must implement the way of writing the diagrams.
 * 
 * @author Adrian Citu
 *
 */
public abstract class DFSDiagramWriter extends SequenceDiagramWriter {

    /**
     * 
     * @param meth the method to write
     * @throws IOException if any problem writing.
     */
    @Override
    public final void write(final SequenceMethod meth) throws IOException {
        this.writeHeader(meth);
        this.writeMethod(meth);
        this.writeFooter(meth);
    }

    /**
     * Last method called the end for the diagram writing.
     * @param meth the root method
     * @throws IOException if any exception when writing.
     */
    public abstract void writeFooter(SequenceMethod meth) throws IOException;

    /**
     * Method called only one time at the beginning of the diagram writing.
     *  
     * @param meth the root method.
     * @throws IOException if any exception when writing.
     */
    public abstract void writeHeader(SequenceMethod meth) throws IOException;

    /**
     *  Write the content for a method. The workflow is :
     *  <ul>
     *  <li>
     *      call {@link #writeMethodBeforeChildren(SequenceMethod)}
     *  </li>
     *  <li>
     *      for every child method method call 
     *      {@link #writeMethod(SequenceMethod)}
     *  </li>
     *  
     *  <li>
     *      call {@link #writeMethodAfterChildren(SequenceMethod)}
     *  </li>
     *  </ul>
     *  
     * @param meth the method to write.
     * @throws IOException if any exception when writing.
     */
    private void writeMethod(final SequenceMethod meth) throws IOException {
        this.writeMethodBeforeChildren(meth);
        for (SequenceMethod children : meth.getChildren()) {
            this.writeMethod(children);
        }
        this.writeMethodAfterChildren(meth);
    }

    /**
     * Method called for writing a method called BEFORE the call of the 
     * {@link #write(SequenceMethod)} on the children.
     * 
     * @param meth the method to write
     * @throws IOException if any problem writing.
     */
    public abstract void writeMethodBeforeChildren(final SequenceMethod meth) throws IOException;

    /**
     * Method called for writing a method called AFTER the call of the 
     * {@link #write(SequenceMethod)} on the children.
     * 
     * @param meth the method to write
     * @throws IOException if any problem writing.
     */
    public abstract void writeMethodAfterChildren(final SequenceMethod meth) throws IOException;
}
