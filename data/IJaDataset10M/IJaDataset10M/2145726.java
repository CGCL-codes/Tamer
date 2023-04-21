package net.sourceforge.kas.cTree;

import java.util.HashMap;
import org.w3c.dom.Element;

public class CEqu extends CElement {

    public CEqu(final Element element, HashMap<Element, CElement> map) {
        super(map);
        this.element = element;
    }

    @Override
    public CType getCType() {
        return CType.EQU;
    }

    public CElement getLinkeSeite() {
        return this.getFirstChild();
    }

    public CElement getRechteSeite() {
        return this.getFirstChild().getNextSibling();
    }

    @Override
    public void normalize() {
    }

    ;

    @Override
    public boolean equals(final CElement other) {
        return this.equalsWithoutPraefix(other) && this.getPraefixAsString().equals(other.getPraefixAsString());
    }

    public boolean equalsWithoutPraefix(final CElement other) {
        return other.getCType().equals(CType.EQU) && this.getLinkeSeite().equals(((CEqu) other).getLinkeSeite()) && this.getRechteSeite().equals(((CEqu) other).getRechteSeite());
    }

    @Override
    public int internalCompare(final CElement o) {
        final CEqu eq2 = (CEqu) o;
        if (this.getLinkeSeite().compareTo(eq2.getLinkeSeite()) != 0) {
            return this.getLinkeSeite().compareTo(eq2.getLinkeSeite());
        } else {
            return this.getRechteSeite().compareTo(eq2.getRechteSeite());
        }
    }
}
