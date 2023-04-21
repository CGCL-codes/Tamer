package de.hbrs.inf.atarrabi.action.wizard.scientist;

import java.io.Serializable;
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
import de.hbrs.inf.atarrabi.action.ActionHelper;
import de.hbrs.inf.atarrabi.action.AtarrabiWizardPageNavAction;
import de.hbrs.inf.atarrabi.action.components.AddEditPersonAction;
import de.hbrs.inf.atarrabi.enums.NavCommand;
import de.hbrs.inf.atarrabi.model.Person;
import de.hbrs.inf.atarrabi.model.PublicationEntity;

/**
 * Action class for the author-view.
 * 
 * @author Benedikt Huber
 * @author Marcel Sponer
 */
@Name("authorAction")
@Scope(ScopeType.CONVERSATION)
public class AuthorAction implements Serializable, AtarrabiWizardPageNavAction {

    private static final long serialVersionUID = 1L;

    @In
    private FacesMessages facesMessages;

    @In
    private PublicationEntity publicationEntity;

    @In
    private ActionHelper actionHelper;

    @In
    private CommonWizardAction commonWizardAction;

    @Logger
    private Log log;

    private Person selectedPerson;

    private final String findAction = "findAction";

    /**
	 * To be used in the rendered attribute.
	 * 
	 * @return true if a person was selected before
	 */
    public boolean isPersonSelected() {
        return selectedPerson != null;
    }

    /**
	 * Method for post processing for saving or updating a person in
	 * addEditPersonAction.
	 */
    public void postProcessSaveOrUpdatePerson() {
        this.setSelectedPerson(this.retrievePersonFromAddEditPersonAction());
        this.selectedPerson.setInstitute(getInstituteAction().getSelectedInstitute());
    }

    /**
	 * Retrieves the treated person from addEditPersonAction.
	 * 
	 * @return treated Person
	 */
    private Person retrievePersonFromAddEditPersonAction() {
        AddEditPersonAction action = (AddEditPersonAction) Component.getInstance("addEditPersonAction", ScopeType.CONVERSATION);
        return action.getTreatedPerson();
    }

    /**
	 * Delivers the selected person.
	 * 
	 * @return The selected person
	 */
    public Person getSelectedPerson() {
        return selectedPerson;
    }

    /**
	 * Sets the selected person.
	 * 
	 * @param selectedPerson
	 *            the selected person
	 */
    public void setSelectedPerson(Person selectedPerson) {
        if (selectedPerson != null) {
            this.selectedPerson = selectedPerson;
        }
    }

    private InstituteAction getInstituteAction() {
        return (InstituteAction) Component.getInstance("instituteAction", ScopeType.CONVERSATION);
    }

    /**
	 * Validation method for the author view.
	 * 
	 * @return is the page valid.
	 */
    public boolean validatePage() {
        if (publicationEntity.getAuthors().isEmpty()) {
            facesMessages.addFromResourceBundle(Severity.ERROR, "validator.page.required.author");
            return false;
        }
        return true;
    }

    /**
	 * Execute validation action.
	 * 
	 * @return The navigation command.
	 */
    @Override
    public NavCommand doNavAction() {
        if (publicationEntity.getAuthors().isEmpty()) {
            facesMessages.addFromResourceBundle(Severity.ERROR, "validator.page.required.author");
            return NavCommand.STAY_AT_CURRENT_PAGE;
        }
        boolean institueNull = false;
        for (Person anAuthor : publicationEntity.getAuthors()) {
            if (anAuthor.getInstitute() == null) {
                facesMessages.addFromResourceBundle(Severity.ERROR, "validator.page.emptyInstitute.author", anAuthor.getFirstName() + " " + anAuthor.getLastName());
                institueNull = true;
            }
        }
        if (institueNull) {
            return NavCommand.STAY_AT_CURRENT_PAGE;
        }
        return NavCommand.DO_DEFAULT_TRANSITION;
    }

    /**
	 * Add a person to the authors list.
	 * 
	 * @param author
	 *            The person to add to the authors list
	 */
    public void addAuthor(Person author) {
        publicationEntity.getAuthors().add(author);
        actionHelper.getEntityManager().flush();
        facesMessages.addFromResourceBundle(Severity.INFO, "wizard.scientist.authors.statusmsg.addAuthor", author.getFirstName(), author.getLastName());
        ((FindAction) Component.getInstance(findAction, ScopeType.CONVERSATION)).find();
    }

    /**
	 * Remove person from the authors list.
	 * 
	 * @param author
	 *            - test
	 */
    public void removeAuthor(Person author) {
        log.debug("removing author: #0", author);
        publicationEntity.getAuthors().remove(author);
        log.debug("Removing author");
        log.debug("Author was updated: #0", author.isUpdated());
        actionHelper.getEntityManager().flush();
        facesMessages.addFromResourceBundle(Severity.INFO, "wizard.scientist.authors.statusmsg.remAuthor", author.getFirstName(), author.getLastName());
        ((FindAction) Component.getInstance(findAction, ScopeType.CONVERSATION)).find();
    }

    /**
	 * Converter method for persons (authors). Is needed by the ordering list
	 * (author-view)
	 * 
	 * @return The author converter
	 */
    public Converter getAuthorConverter() {
        return new Converter() {

            @Override
            public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
                Person p = (Person) arg2;
                String s = p.getFirstName();
                if (p.getSecondName() != null && p.getSecondName().length() > 0) {
                    s += p.getSecondName();
                }
                s += p.getLastName();
                return s;
            }

            @Override
            public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
                ActionHelper ah = (ActionHelper) Component.getInstance(ActionHelper.class);
                for (Person p : ah.getPublicationEntity().getAuthors()) {
                    String s = p.getFirstName();
                    if (p.getSecondName() != null && p.getSecondName().length() > 0) {
                        s += p.getSecondName();
                    }
                    s += p.getLastName();
                    if (s.equals(arg2)) {
                        return p;
                    }
                }
                return null;
            }
        };
    }

    /**
	 * Checks for author and institute if it has been changed recently and
	 * checks the left references for both. If there is no other reference for
	 * author or institute we need to show a confirmation panel to inform the
	 * user that the information will not be transferred to the cera-database if
	 * the dataset gets removed from the publication entity.
	 * 
	 * @param author
	 *            author to check about
	 * @return if the panel should be shown
	 */
    public boolean showRemovalPanel(Person author) {
        if (author != null && author.isUpdated() && (commonWizardAction.countReferencesLeftForAuthor(author) <= 1)) {
            return true;
        }
        return author.getInstitute() != null && author.getInstitute().isUpdated() && commonWizardAction.countReferencesLeftForInstitute(author.getInstitute()) <= 1;
    }

    /**
	 * Checks if the author has been changed recently and if there are other
	 * references so the changes will still be transferred to the cera-database.
	 * If not we show a panel to inform the user about that and ask if he/she is
	 * sure about the removal.
	 * 
	 * @return if the panel should be shown
	 */
    public boolean showDoiReplacementPanel() {
        if (publicationEntity.getDoiContact() != null) {
            return publicationEntity.getDoiContact().isUpdated() && commonWizardAction.countReferencesLeftForAuthor(publicationEntity.getDoiContact()) <= 1;
        } else {
            return false;
        }
    }
}
