package net.sourceforge.kas.cTree;

import java.util.ArrayList;
import java.util.HashMap;
import net.sourceforge.kas.cTree.adapter.EElementHelper;
import org.w3c.dom.Element;

public class CPot extends CElement {

    public CPot(final Element element, HashMap<Element, CElement> map) {
        super(map);
        this.element = element;
    }

    public static CElement createSimplifiedSquared(CElement base) {
        if (base.getCType().equals(CType.SQRT)) {
            return ((CSqrt) base).getRadikand().cloneCElement(false);
        } else {
            if (CTypeHelper.mustFenceToPot().contains(base.getCType())) {
                base = CFences.createFenced(base.cloneCElement(false));
                return CPot.createPot(base, 2);
            } else {
                return CPot.createPot(base.cloneCElement(false), 2);
            }
        }
    }

    @Override
    public CType getCType() {
        return CType.POT;
    }

    public boolean isHoch0() {
        if (this.getExponent().getCType() != CType.NUM) {
            return false;
        }
        return ((CNum) this.getExponent()).is0();
    }

    public boolean isHoch1() {
        if (this.getExponent().getCType() != CType.NUM) {
            return false;
        }
        return ((CNum) this.getExponent()).is1();
    }

    @Override
    public boolean equals(final CElement other) {
        return this.equalsWithoutPraefix(other) && this.getPraefixAsString().equals(other.getPraefixAsString());
    }

    public boolean equalsWithoutPraefix(final CElement other) {
        return other.getCType().equals(CType.POT) && this.getBasis().equals(((CPot) other).getBasis()) && this.getExponent().equals(((CPot) other).getExponent());
    }

    public static CPot createPot(final CElement basis, final CElement expo) {
        final CPot pot = (CPot) CElementHelper.createAll(basis.getElement(), "msup", "msup", CRolle.UNKNOWN, null, basis.map);
        pot.appendChild(basis);
        pot.appendPraefixAndChild(expo);
        basis.setCRolle(CRolle.BASIS);
        expo.setCRolle(CRolle.EXPONENT);
        return pot;
    }

    public static CPot createPot(final CElement basis, final int expo) {
        final CElement cExp = CNum.createNum(basis, "" + expo);
        return CPot.createPot(basis, cExp);
    }

    public static CPot createPot(final Element producer, final String ident, final int expo, HashMap<Element, CElement> map) {
        final CElement cBas = CIdent.createIdent(producer, ident, map);
        return CPot.createPot(cBas, expo);
    }

    @Override
    public boolean hatGleichenBetrag(final CElement cE2) {
        if (cE2 instanceof CPot) {
            return this.getBasis().hatGleichenBetrag(((CPot) cE2).getBasis()) && this.getExponent().hatGleichenBetrag(((CPot) cE2).getExponent());
        } else if (cE2 instanceof CMinTerm) {
            return this.hatGleichenBetrag(((CMinTerm) cE2).getValue());
        }
        return false;
    }

    public static ArrayList<CElement> map(final CElement first, final ArrayList<CElement> list) {
        final ArrayList<CElement> result = new ArrayList<CElement>();
        for (final CElement second : list) {
            final CElement basis = first.cloneCElement(false);
            final CElement expo = second.cloneCElement(false);
            final CElement pot = CPot.createPot(basis, expo);
            result.add(pot);
        }
        return result;
    }

    public static CPot createSquare(final CElement cElement) {
        CPot newSquare = null;
        if (cElement instanceof CPot) {
            final CElement newBase = ((CPot) cElement).getBasis().cloneCElement(true);
            final CElement first = CElementHelper.createAll(cElement.getElement(), "mn", "mn", CRolle.FAKTOR1, null, cElement.map);
            first.setText("2");
            final CElement second = ((CPot) cElement).getExponent().cloneCElement(true);
            final Element op = EElementHelper.createOp(cElement.getElement(), "*");
            second.setExtPraefix(op);
            final ArrayList<CElement> faktoren = new ArrayList<CElement>();
            faktoren.add(first);
            faktoren.add(second);
            final CElement newExp = CTimesRow.createRow(faktoren);
            newSquare = (CPot) CElementHelper.createAll(cElement.getElement(), "msup", "msup", cElement.getCRolle(), cElement.getExtPraefix(), cElement.map);
            newSquare.appendChild(newBase);
            newSquare.appendChild(newExp);
            newBase.setCRolle(CRolle.BASIS);
            newExp.setCRolle(CRolle.EXPONENT);
        } else {
            newSquare = (CPot) CElementHelper.createAll(cElement.getElement(), "msup", "msup", cElement.getCRolle(), cElement.getExtPraefix(), cElement.map);
            final CElement newBase = cElement.cloneCElement(false);
            newSquare.appendChild(newBase);
            final CElement newExp = CElementHelper.createAll(cElement.getElement(), "mn", "mn", CRolle.EXPONENT, null, cElement.map);
            newExp.setText("2");
            newSquare.appendChild(newExp);
            newBase.setCRolle(CRolle.BASIS);
            newExp.setCRolle(CRolle.EXPONENT);
        }
        return newSquare;
    }

    @Override
    public void normalize() {
    }

    ;

    @Override
    public boolean istGleichartigesMonom(final CElement el) {
        boolean result = false;
        if (el.getCType() == CType.IDENT) {
            result = (this.getElement().getFirstChild().getTextContent().equals(el.getElement().getTextContent()));
        } else if (el.getCType() == CType.POT) {
            result = (this.getElement().getFirstChild().getTextContent().equals(el.getElement().getFirstChild().getTextContent()));
        } else if (el.getCType() == CType.MINROW) {
            return this.istGleichartigesMonom(((CMinTerm) el).getValue());
        } else if (el.getCType() == CType.TIMESROW) {
            return el.istGleichartigesMonom(this);
        }
        return result;
    }

    public CElement getBasis() {
        return this.getFirstChild();
    }

    public void setBasis(final String s) {
        this.element.getFirstChild().setTextContent(s);
    }

    public CElement getExponent() {
        return this.getFirstChild().getNextSibling();
    }

    public int getExponentValue() {
        final String exp = this.getExponent().getElement().getTextContent();
        try {
            return Integer.parseInt(exp);
        } catch (final NumberFormatException e) {
            System.out.println("Kein guter Exponent");
        }
        return -1;
    }

    public void setExponent(final String e) {
        this.element.getFirstChild().getNextSibling().setTextContent(e);
    }

    public String getVar() {
        return this.getBasis().getElement().getTextContent();
    }

    public String getSignatur() {
        final String nr = this.getBasis().getElement().getTextContent();
        final String exp = this.getExponent().getElement().getTextContent();
        return nr + exp;
    }

    @Override
    public boolean hasNumberValue() {
        return this.getBasis().hasNumberValue() && this.getExponent().hasNumberValue();
    }

    @Override
    public double getNumberValue() {
        return Math.pow(this.getBasis().getNumberValue(), this.getExponentValue());
    }

    @Override
    public int internalCompare(final CElement o) {
        if (o instanceof CPot) {
            final CPot f2 = (CPot) o;
            if (this.getBasis().compareTo(f2.getBasis()) != 0) {
                return this.getBasis().compareTo(f2.getBasis());
            } else {
                return this.getExponentValue() - f2.getExponentValue();
            }
        } else {
            final CIdent f2 = (CIdent) o;
            return -f2.internalCompare(this);
        }
    }

    public boolean isMonom() {
        return (this.getBasis() instanceof CIdent);
    }
}
