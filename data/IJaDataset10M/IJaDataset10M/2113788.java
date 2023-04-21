package de.hbrs.inf.atarrabi.action.wizard.scientist;

import java.io.Serializable;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.jboss.seam.pageflow.Pageflow;
import de.hbrs.inf.atarrabi.action.ActionHelper;
import de.hbrs.inf.atarrabi.action.AtarrabiWizardPageNavAction;
import de.hbrs.inf.atarrabi.action.components.AddEditPersonAction;
import de.hbrs.inf.atarrabi.enums.NavCommand;
import de.hbrs.inf.atarrabi.enums.StatusType;
import de.hbrs.inf.atarrabi.model.CitationStyle;
import de.hbrs.inf.atarrabi.model.Institute;
import de.hbrs.inf.atarrabi.model.Person;
import de.hbrs.inf.atarrabi.util.StringConstants;

/**
 * Action class for the contributors view and the modal panel for adding or editing institutes.
 * 
 * @author Benedikt Huber
 * @author Martin Dames
 * @author Marcel Sponer
 * 
 */
@Name("instituteAction")
@Scope(ScopeType.CONVERSATION)
public class InstituteAction implements Serializable, AtarrabiWizardPageNavAction {

    private static final long serialVersionUID = 1L;

    private static final String FIND_ACTION = "findAction";

    @Logger
    private Log log;

    @In
    private FacesMessages facesMessages;

    @In
    private ActionHelper actionHelper;

    @In
    private CommonWizardAction commonWizardAction;

    private List<Institute> institutePool;

    private Institute selectedInstitute;

    private boolean selectContributer;

    /**
	 * Saves a new institute, if it doesn't exist in institutePool (add).
	 */
    public void saveInstitute() {
        log.debug("Trying to save a new or update an existing institute...");
        selectedInstitute.setPublicationEntity(actionHelper.getPublicationEntity());
        if (!actionHelper.getPublicationEntity().getInstitutePool().contains(selectedInstitute)) {
            actionHelper.getPublicationEntity().getInstitutePool().add(selectedInstitute);
        }
        if (!actionHelper.getPublicationEntity().getContributors().contains(selectedInstitute) && this.selectContributer) {
            actionHelper.getPublicationEntity().getContributors().add(selectedInstitute);
        }
        this.updateDb();
        this.assignPerson();
    }

    /**
	 * Assigns the treated person to the selected institute.
	 */
    public void assignPerson() {
        AddEditPersonAction aepa = (AddEditPersonAction) Component.getInstance("addEditPersonAction");
        String pageViewId = Pageflow.instance().getPageViewId();
        if (aepa.getTreatedPerson() != null) {
            if ("/wizard/scientist/authors.xhtml".equals(pageViewId) || "/wizard/scientist/doicontact.xhtml".equals(pageViewId)) {
                log.debug("assignPerson:#0", selectedInstitute);
                Person treatedPerson = aepa.getTreatedPerson();
                treatedPerson.setInstitute(selectedInstitute);
                actionHelper.getEntityManager().flush();
                log.debug("Update person #0 with institute #1 successfully.", treatedPerson, selectedInstitute);
            }
        } else {
            log.error("assignPerson:Treated person is null.");
        }
    }

    /**
	 * Stores the new/updated institute into the database. 
	 */
    public void updateDb() {
        log.debug("updateDb:#0", selectedInstitute);
        if (selectedInstitute.getStatusType() == StatusType.NEW) {
            log.debug("Adding new institute to db");
            actionHelper.getEntityManager().persist(selectedInstitute);
        } else {
            log.debug("Refreshing institute in db");
        }
        selectedInstitute.setUpdated(true);
        actionHelper.getEntityManager().joinTransaction();
        actionHelper.getEntityManager().flush();
    }

    /**
	 * Creates a new institute instance and sets the name to the provided temporary name.
	 * 
	 * @param select
	 *            true: the Institute will be added as contributer to the publicationEntity. false: the Institute will
	 *            NOT be added as contributer to the publicationEntity.
	 */
    public void createNewInstitute(boolean select) {
        this.selectedInstitute = new Institute();
        this.selectedInstitute.setStatusType(StatusType.NEW);
        this.selectContributer = select;
    }

    /**
	 * Creates a new institute instance and sets the name to the provided temporary name.
	 */
    public void createNewInstitute() {
        this.selectedInstitute = new Institute();
        this.selectedInstitute.setStatusType(StatusType.NEW);
    }

    /**
	 * Resets the selected institute values.
	 */
    public void clearInstituteValues() {
        this.selectedInstitute = new Institute();
        this.selectedInstitute.setStatusType(StatusType.NEW);
    }

    /**
	 * Delivers a list of all Institutes.
	 * 
	 * @return The list of all institutes.
	 */
    public List<Institute> getInstitutePool() {
        institutePool = actionHelper.getPublicationEntity().getInstitutePool();
        return institutePool;
    }

    public void setInstitutePool(List<Institute> institutePool) {
        this.institutePool = institutePool;
    }

    public Institute getSelectedInstitute() {
        return selectedInstitute;
    }

    public void setSelectedInstitute(Institute selectedInstitute) {
        this.selectedInstitute = selectedInstitute;
    }

    /**
	 * Edits an institute.
	 * 
	 * @param institute
	 *            The institute to edit
	 */
    public void editInstitute(Institute institute) {
        this.setSelectedInstitute(institute);
        ((FindAction) Component.getInstance(FIND_ACTION, ScopeType.CONVERSATION)).find();
    }

    public Converter getContributorsConverter() {
        return new Converter() {

            public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
                Institute i = (Institute) arg2;
                return i.getAcronym() + i.getName();
            }

            public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
                ActionHelper ah = (ActionHelper) Component.getInstance(ActionHelper.class);
                for (Institute i : ah.getPublicationEntity().getContributors()) {
                    String s = i.getAcronym() + i.getName();
                    if (s.equals(arg2)) {
                        return i;
                    }
                }
                return null;
            }
        };
    }

    /**
	 * Adds a new institute.
	 * 
	 * @param institute
	 *            the institute to add.
	 */
    public void addInstitute(Institute institute) {
        actionHelper.getPublicationEntity().getContributors().add(institute);
        facesMessages.addFromResourceBundle(Severity.INFO, "wizard.scientist.contributors.statusmsg.addInstitute", institute.getAcronym(), StringConstants.COLON, institute.getName());
        ((FindAction) Component.getInstance(FIND_ACTION, ScopeType.CONVERSATION)).find();
    }

    /**
	 * Removes an institute from the contributors list.
	 * 
	 * @param institute
	 *            The institute to remove
	 */
    public void removeInstitute(Institute institute) {
        actionHelper.getPublicationEntity().getContributors().remove(institute);
        facesMessages.addFromResourceBundle(Severity.INFO, "wizard.scientist.contributors.statusmsg.remInstitute", institute.getAcronym(), StringConstants.COLON, institute.getName());
        ((FindAction) Component.getInstance(FIND_ACTION, ScopeType.CONVERSATION)).find();
    }

    /**
	 * Institute view input validation.
	 * 
	 * @return null if the inputs are valid. Null does a normal navigation. Empty String stays at the current page.
	 */
    public NavCommand doNavAction() {
        if (CitationStyle.BY_INSTITUTE.equals(actionHelper.getPublicationEntity().getGeneralData().getCitationStyle()) && actionHelper.getPublicationEntity().getContributors().isEmpty()) {
            facesMessages.addFromResourceBundle(Severity.ERROR, "validator.page.required.contributors");
            return NavCommand.STAY_AT_CURRENT_PAGE;
        }
        return NavCommand.DO_DEFAULT_TRANSITION;
    }

    /**
	 * Checks if the institute has been changed recently and if the institute is not referenced anywhere else, we show a panel to confirm the removal.
	 * 
	 * @param institute institute to check about
	 * @return if the panel should be shown
	 */
    public boolean showRemovalPanelForInstitute(Institute institute) {
        return institute.isUpdated() && (commonWizardAction.countReferencesLeftForInstitute(institute) <= 1);
    }
}
