package org.xmlcml.cmlimpl;

import java.io.IOException;
import java.io.Writer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlcml.cml.CMLException;
import org.xmlcml.cml.CMLMolecule;
import uk.co.demon.ursus.dom.PMRDelegate;

public abstract class AbstractAngleImpl extends AbstractAtomRefImpl {

    protected double angle;

    protected String stringAngle;

    public AbstractAngleImpl() {
        super();
    }

    public AbstractAngleImpl(Element element) {
        super(element);
    }

    /** new Object in document context */
    public AbstractAngleImpl(String tagName, Document document) {
        super(tagName, document);
    }

    /** new Object in molecule context */
    public AbstractAngleImpl(String tagName, CMLMolecule molecule) {
        super(tagName, molecule);
    }

    protected AbstractAngleImpl(String tagName, CMLMolecule molecule, double angle, String title, String id) {
        this(tagName, molecule);
        this.setAngle(angle);
        this.setTitle(title);
        this.setId(id);
    }

    ;

    /** new CMLIntegerVal in document context; sets int value and builtin (if not null) */
    public AbstractAngleImpl(String tagName, CMLMolecule molecule, double angle) {
        this(tagName, molecule);
        this.setAngle(angle);
    }

    public void setAngle(double angle) {
        this.angle = angle;
        this.setStringAngle("" + angle);
    }

    public double getAngle() {
        return this.angle;
    }

    protected void setStringAngle(String stringAngle) {
        PMRDelegate.setPCDATAContent(this, stringAngle);
    }

    public boolean processDOM() throws CMLException {
        if (!domNeedsProcessing) return true;
        if (!super.processDOM()) return false;
        stringAngle = PMRDelegate.getPCDATAContent(this);
        try {
            angle = new Double(stringAngle).doubleValue();
        } catch (NumberFormatException nfe) {
            throw new CMLException("Bad angle: " + stringAngle);
        }
        return true;
    }

    public boolean updateDOM() throws CMLException {
        if (!domNeedsUpdating) return true;
        if (!super.updateDOM()) return false;
        stringAngle = "" + angle;
        PMRDelegate.setPCDATAContent(this, stringAngle);
        domNeedsUpdating = false;
        return true;
    }

    public String toString() {
        return super.toString() + " " + this.getClassTagName() + ": " + angle;
    }

    public void debug(Writer w) throws IOException {
        w.write(this.toString() + "\n");
    }
}
