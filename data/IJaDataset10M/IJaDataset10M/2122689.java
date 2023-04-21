package uk.ac.ebi.intact.confidence.model;

/**
 * Attribute Identifier implementation.
 *
 * @author Irina Armean (iarmean@ebi.ac.uk)
 * @version $Id$
 * @since 1.6.0
 *        <pre>
 *               30-Nov-2007
 *               </pre>
 */
public class IdentifierAttributeImpl<T extends Identifier> implements Attribute<Identifier> {

    private T firstElement;

    private T secondElement;

    public IdentifierAttributeImpl(T firstElement, T secondElement) {
        this.firstElement = firstElement;
        this.secondElement = secondElement;
    }

    public Identifier getFirstElement() {
        return firstElement;
    }

    public Identifier getSecondElement() {
        return secondElement;
    }

    public String convertToString() {
        return firstElement + ";" + secondElement;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IdentifierAttributeImpl) {
            IdentifierAttributeImpl objT = (IdentifierAttributeImpl) obj;
            boolean first = firstElement.equals(objT.getFirstElement()) && secondElement.equals(objT.getSecondElement());
            boolean rev = firstElement.equals(objT.getSecondElement()) && secondElement.equals(objT.getFirstElement());
            return first || rev;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return firstElement.hashCode() * secondElement.hashCode();
    }
}
