package ch.bbv.mda.persistence.mdr;

import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.ModelElement;

/**
 * Wrapper Class for a MDR Generalization instance This class provides methods
 * to simplify the access to the mdr model
 * @author UeliKurmann
 * @version $Revision: 1.2 $
 */
public class MdrUmlGeneralization extends MdrUmlElement {

    /**
   * the mdr generalization instance
   */
    private Generalization generalization;

    /**
   * Constructor
   * @param generalization
   */
    public MdrUmlGeneralization(Generalization generalization) {
        this.generalization = generalization;
    }

    public String getQualifiedName() {
        return getName();
    }

    /**
   * Returns the parent of the generalization
   * @return GerneraliyableElement
   */
    public GeneralizableElement getParent() {
        return generalization.getParent();
    }

    /**
   * Sets the parent of the generalization
   * @param element
   */
    public void setParent(GeneralizableElement element) {
        generalization.setParent(element);
    }

    /**
   * Returns the child of the generalization
   * @return GeneralizableElement
   */
    public GeneralizableElement getChild() {
        return generalization.getChild();
    }

    /**
   * Sets the child of the generalization
   * @param element
   */
    public void setChild(GeneralizableElement element) {
        generalization.setChild(element);
    }

    public ModelElement getModelElement() {
        return generalization;
    }
}
