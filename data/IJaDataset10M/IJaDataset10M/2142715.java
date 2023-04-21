package uk.ac.ebi.intact.application.editor.struts.view.interaction;

import uk.ac.ebi.intact.application.editor.business.EditUserI;
import uk.ac.ebi.intact.application.editor.exception.SearchException;
import uk.ac.ebi.intact.application.editor.struts.view.AbstractEditBean;
import uk.ac.ebi.intact.application.editor.struts.framework.util.EditorMenuFactory;
import uk.ac.ebi.intact.model.*;
import uk.ac.ebi.intact.business.IntactHelper;
import java.io.Serializable;
import java.util.Iterator;

/**
 * Bean to store data for an Interactor (Protein).
 *
 * @author Sugath Mudali (smudali@ebi.ac.uk)
 * @version $Id: ProteinBean.java 2843 2004-04-23 13:53:27Z smudali $
 */
public class ProteinBean extends AbstractEditBean implements Serializable {

    /**
     * The saving new state; this is different to saving state as this state
     * indicates saving a new item. This state is only used by proteins.jsp
     * to save new componets as as result of a search.
     */
    static final String SAVE_NEW = "saveNew";

    /**
     * Identifier for an error bean.
     */
    public static final String ERROR = "error";

    /**
     * The interaction this protein belongs to.
     */
    private Interaction myInteraction;

    /**
     * The interactor of the bean.
     */
    private Interactor myInteractor;

    /**
     * The object this instance is created with.
     */
    private Component myComponent;

    /**
     * Swiss-Prot AC.
     */
    private String mySPAc;

    /**
     * The role of this Protein.
     */
    private String myRole;

    /**
     * The stoichiometry.
     */
    private float myStoichiometry = 1.0f;

    /**
     * The organism.
     */
    private String myOrganism;

    /**
     * Expressed in as a biosource short label.
     */
    private String myExpressedIn;

    /**
     * Instantiate an object of this class from a Protein instance.
     * @param protein the <code>Protein</code> object.
     */
    public ProteinBean(Protein protein) {
        myInteractor = (Interactor) IntactHelper.getRealIntactObject(protein);
        mySPAc = getSPAc();
        setOrganism();
        setEditState(SAVE_NEW);
    }

    /**
     * Instantiate an object of this class from a Component instance.
     * @param component the <code>Component</code> object.
     */
    public ProteinBean(Component component) {
        myComponent = component;
        myInteraction = (Interaction) IntactHelper.getRealIntactObject(component.getInteraction());
        myInteractor = (Interactor) IntactHelper.getRealIntactObject(component.getInteractor());
        mySPAc = getSPAc();
        myRole = component.getCvComponentRole().getShortLabel();
        myStoichiometry = component.getStoichiometry();
        setOrganism();
        setExpressedIn();
    }

    public Component getComponent(EditUserI user) throws SearchException {
        CvComponentRole newrole = getRole(user);
        if ((newrole == null) || (myInteraction == null)) {
            return null;
        }
        if (myComponent == null) {
            myComponent = new Component(user.getInstitution(), myInteraction, myInteractor, newrole);
        } else {
            myComponent.setCvComponentRole(newrole);
        }
        myComponent.setStoichiometry(getStoichiometry());
        BioSource expressedIn = null;
        if (myExpressedIn != null) {
            expressedIn = (BioSource) user.getObjectByLabel(BioSource.class, myExpressedIn);
        }
        myComponent.setExpressedIn(expressedIn);
        return myComponent;
    }

    public String getAc() {
        return myInteractor.getAc();
    }

    public String getShortLabel() {
        return myInteractor.getShortLabel();
    }

    public String getShortLabelLink() {
        return getLink("Protein", getShortLabel());
    }

    public String getSpAc() {
        return mySPAc;
    }

    public String getFullName() {
        return myInteractor.getFullName();
    }

    public String getRole() {
        return myRole;
    }

    public void setRole(String role) {
        myRole = EditorMenuFactory.normalizeMenuItem(role);
    }

    public float getStoichiometry() {
        return myStoichiometry;
    }

    public void setStoichiometry(float stoichiometry) {
        myStoichiometry = stoichiometry;
    }

    public String getExpressedIn() {
        return myExpressedIn;
    }

    public void setExpressedIn(String expressedIn) {
        myExpressedIn = EditorMenuFactory.normalizeMenuItem(expressedIn);
    }

    public String getOrganism() {
        return myOrganism;
    }

    /**
     * Sets the interaction for this bean. This is necessary for a newly created
     * Interaction as it doesn't exist until it is ready to persist.
     * @param interaction the interaction to set.
     */
    public void setInteraction(Interaction interaction) {
        myInteraction = interaction;
    }

    /**
     * Compares <code>obj</code> with this object according to
     * Java's equals() contract. Only returns <tt>true</tt> if the short labels
     * and roles for both objects match
     * @param obj the object to compare.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if ((obj != null) && (getClass() == obj.getClass())) {
            ProteinBean other = (ProteinBean) obj;
            if (getShortLabel().equals(other.getShortLabel())) {
                if (myRole == null) {
                    return other.myRole == null;
                }
                return myRole.equals(other.myRole);
            }
        }
        return false;
    }

    private void setOrganism() {
        BioSource biosource = myInteractor.getBioSource();
        if (biosource != null) {
            myOrganism = biosource.getShortLabel();
        }
    }

    private void setExpressedIn() {
        BioSource bs = myComponent.getExpressedIn();
        if (bs != null) {
            myExpressedIn = bs.getShortLabel();
        }
    }

    private String getSPAc() {
        for (Iterator iter = myInteractor.getXrefs().iterator(); iter.hasNext(); ) {
            Xref xref = (Xref) iter.next();
            if (xref.getCvDatabase().getShortLabel().equals("uniprot")) {
                return xref.getPrimaryId();
            }
        }
        return "";
    }

    private CvComponentRole getRole(EditUserI user) throws SearchException {
        if (myRole != null) {
            return (CvComponentRole) user.getObjectByLabel(CvComponentRole.class, myRole);
        }
        return null;
    }
}
