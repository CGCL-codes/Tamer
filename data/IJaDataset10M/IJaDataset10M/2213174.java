package eu.pisolutions.ocelot.object;

/**
 * PDF object that contains elements.
 *
 * @author Laurent Pireyn
 * @see ContainerPdfObjectElement
 */
public interface ContainerPdfObject extends PdfObject {

    boolean isEmpty();

    int size();

    void clear();
}
