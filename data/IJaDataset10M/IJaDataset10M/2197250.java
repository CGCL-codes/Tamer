package wilos.presentation.web.viewer;

import javax.faces.event.ActionEvent;
import wilos.business.services.misc.concreterole.ConcreteRoleDescriptorService;
import wilos.business.services.misc.wilosuser.ParticipantService;
import wilos.model.misc.concreterole.ConcreteRoleDescriptor;
import wilos.model.misc.wilosuser.Participant;
import wilos.presentation.web.tree.WilosObjectNode;
import wilos.presentation.web.utils.WebCommonService;
import wilos.presentation.web.utils.WebSessionService;
import wilos.resources.LocaleBean;

public class ConcreteRoleViewerBean extends ViewerBean {

    private ConcreteRoleDescriptor concreteRoleDescriptor;

    private ConcreteRoleDescriptorService concreteRoleDescriptorService;

    private String concreteRoleDescriptorId = "";

    private ParticipantService participantService;

    private String displayedPanelManager;

    private boolean visibleDeletePopup = false;

    /**
	 * Method which set the value of the concreteRoleDescriptorId to a default value if this one is null
	 * The default value is the concreteRoleDescriptorId of the concreteRoleDescriptorService
	 */
    public void buildConcreteRoleModel() {
        this.concreteRoleDescriptor = new ConcreteRoleDescriptor();
        if (!(concreteRoleDescriptorId.equals("")) || concreteRoleDescriptorId != null) {
            this.concreteRoleDescriptor = this.concreteRoleDescriptorService.getConcreteRoleDescriptor(this.concreteRoleDescriptorId);
        }
    }

    /**
	 * Method which affect and save in the data base the current user to a role
	 * Also refresh the project tree
	 */
    public void affectParticipantToARole() {
        String wilosUserId = (String) WebSessionService.getAttribute(WebSessionService.WILOS_USER_ID);
        Participant user = this.participantService.getParticipant(wilosUserId);
        concreteRoleDescriptor = this.concreteRoleDescriptorService.addPartiConcreteRoleDescriptor(concreteRoleDescriptor, user);
        if (concreteRoleDescriptor != null) {
            if (concreteRoleDescriptor.getParticipant() == user && concreteRoleDescriptor.getParticipant() != null) {
                WebCommonService.addInfoMessage(LocaleBean.getText("concreteRoleViewer.success"));
            } else {
                WebCommonService.addErrorMessage(LocaleBean.getText("concreteRoleViewer.failed"));
            }
        } else {
            WebCommonService.changeContentPage(WilosObjectNode.PROJECTNODE);
            WebCommonService.addErrorMessage(LocaleBean.getText("concreteRoleViewer.delete"));
        }
        super.refreshProjectTree();
    }

    /**
	 * @return the visibleRemove
	 */
    public boolean getVisibleRemove() {
        return (!this.getChangeButtonIsDisabled() && (this.concreteRoleDescriptor.getParticipant() == null));
    }

    /**
	 * Delete a concrete role when the specified event is received
	 * @param event
	 */
    public void removeActionListener(ActionEvent event) {
        this.visibleDeletePopup = true;
    }

    /**
	 * Ask a confirmation for the specified event.
	 * Also refresh the project tree
	 * @param _event
	 */
    public void confirmDelete(ActionEvent _event) {
        ConcreteRoleDescriptor crd;
        if (!this.getChangeButtonIsDisabled() && (this.concreteRoleDescriptor.getParticipant() == null)) {
            crd = this.concreteRoleDescriptorService.deleteConcreteRoleDescriptor(this.concreteRoleDescriptor);
            if (crd == null) {
                WebCommonService.changeContentPage(WilosObjectNode.PROJECTNODE);
                WebCommonService.addInfoMessage(LocaleBean.getText("concreteRoleViewer.removed"));
            } else {
                if (crd.getParticipant() != null) {
                    this.concreteRoleDescriptor.setParticipant(crd.getParticipant());
                    WebCommonService.addErrorMessage(LocaleBean.getText("concreteRoleViewer.failed.removed"));
                } else {
                    WebCommonService.addErrorMessage(LocaleBean.getText("concreteRoleViewer.failed.removed.link"));
                }
            }
            super.rebuildProjectTree();
            super.refreshProjectTree();
            this.visibleDeletePopup = false;
        }
    }

    /**
	 * Method which cancel the deletion of the specified event 
	 * @param _event
	 */
    public void cancelDelete(ActionEvent _event) {
        this.visibleDeletePopup = false;
    }

    /**
	 * action called to change the concrete name of a concrete role
	 */
    public void saveName() {
        if (this.concreteRoleDescriptor.getConcreteName().trim().length() == 0) {
            ConcreteRoleDescriptor crd = this.concreteRoleDescriptorService.getConcreteRoleDescriptor(this.concreteRoleDescriptor.getId());
            this.concreteRoleDescriptor.setConcreteName(crd.getConcreteName());
            WebCommonService.addErrorMessage(LocaleBean.getText("viewer.err.checkNameBySaving"));
        } else {
            WebCommonService.addInfoMessage(LocaleBean.getText("viewer.visibility.successMessage"));
        }
        this.concreteRoleDescriptorService.saveConcreteRoleDescriptor(this.concreteRoleDescriptor);
        super.refreshProjectTree();
        super.setNameIsEditable(false);
    }

    /**
	 * Method which return the concreteRoleDescriptor
	 * @return
	 */
    public ConcreteRoleDescriptor getConcreteRoleDescriptor() {
        return concreteRoleDescriptor;
    }

    /**
	 * Method which set the concreteRoleDescriptor 
	 * @param concreteRoleDescriptor
	 */
    public void setConcreteRoleDescriptor(ConcreteRoleDescriptor concreteRoleDescriptor) {
        this.concreteRoleDescriptor = concreteRoleDescriptor;
    }

    /**
	 * Method which return the concreteRoleDescriptorId
	 * @return
	 */
    public String getConcreteRoleDescriptorId() {
        return concreteRoleDescriptorId;
    }

    /**
	 * Method which set the concreteRoleDescriptorId
	 * @param concreteRoleDescriptorId
	 */
    public void setConcreteRoleDescriptorId(String concreteRoleDescriptorId) {
        this.concreteRoleDescriptorId = concreteRoleDescriptorId;
    }

    /**
	 * Method which return the concreteRoleDescriptorService
	 * @return concreteRoleDescriptorService
	 */
    public ConcreteRoleDescriptorService getConcreteRoleDescriptorService() {
        return concreteRoleDescriptorService;
    }

    /**
	 * Method which set the concreteRoleDescriptorService
	 * @param concreteRoleDescriptorService
	 */
    public void setConcreteRoleDescriptorService(ConcreteRoleDescriptorService concreteRoleDescriptorService) {
        this.concreteRoleDescriptorService = concreteRoleDescriptorService;
    }

    /**
	 * Method which display the user affected to the concreteRole
	 * 	display "projectNotselected" if no project is selected
	 * 	display "projectNotInstancied" if there is no project
	 * 	display "roleNotaffected" is no user is affected to the concreteRole
	 */
    public String getDisplayedPanelManager() {
        String projectId = (String) WebSessionService.getAttribute(WebSessionService.PROJECT_ID);
        if ((projectId == null) || (projectId.equals("default"))) {
            this.setDisplayedPanelManager("projectNotSelected");
        } else if (this.concreteRoleDescriptorService.getAllConcreteRoleDescriptorsForProject(projectId).size() == 0) {
            this.setDisplayedPanelManager("projectNotInstanciated");
        } else {
            String wilosUserId = (String) WebSessionService.getAttribute(WebSessionService.WILOS_USER_ID);
            if (this.concreteRoleDescriptor == null || this.concreteRoleDescriptor.getParticipant() == null) {
                this.setDisplayedPanelManager("roleNotAffected");
            } else {
                this.setDisplayedPanelManager("roleAffectedTo");
            }
        }
        return this.displayedPanelManager;
    }

    public String getDisplayedPanelManagerForSeveralRolesAssignment() {
        String projectId = (String) WebSessionService.getAttribute(WebSessionService.PROJECT_ID);
        if ((projectId == null) || (projectId.equals("default"))) {
            return "projectNotSelected";
        } else if (this.concreteRoleDescriptorService.getAllConcreteRoleDescriptorsForProject(projectId).size() == 0) {
            return "projectNotInstanciated";
        } else {
            String wilosUserId = (String) WebSessionService.getAttribute(WebSessionService.WILOS_USER_ID);
            if (this.participantService.getParticipant(wilosUserId) == null) {
                return "roleNotAffected";
            } else {
                return "roleAffectedTo";
            }
        }
    }

    /**
	 * Method which display the PanelManager
	 * That is to say the panel which allow you to see the concreteRoles and the users affected to them
	 * @param displayedPanelManager
	 */
    public void setDisplayedPanelManager(String displayedPanelManager) {
        this.displayedPanelManager = displayedPanelManager;
    }

    /**
	 * Method which return the participantService
	 * @return participantService
	 */
    public ParticipantService getParticipantService() {
        return participantService;
    }

    /**
	 * Method which set the participantService
	 * @param participantService
	 */
    public void setParticipantService(ParticipantService participantService) {
        this.participantService = participantService;
    }

    /**
	 * @return the visibleDeletePopup
	 */
    public boolean getVisibleDeletePopup() {
        return this.visibleDeletePopup;
    }

    /**
	 * @param visibleDeletePopup
	 *            the visibleDeletePopup to set
	 */
    public void setVisibleDeletePopup(boolean _visibleDeletePopup) {
        this.visibleDeletePopup = _visibleDeletePopup;
    }
}
