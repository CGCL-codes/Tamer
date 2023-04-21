package org.openedc.web.prime.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.openedc.domain.SecurityModel;
import org.openedc.domain.User;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author peter
 */
@ManagedBean
@ViewScoped
public class UserWizardBean {

    private User user = new User();

    private boolean skip;

    private static final Logger logger = Logger.getLogger(UserWizardBean.class.getName());

    private String selectedRole;

    private List<String> selectedAdditionalResources = new ArrayList<String>();

    private String selectedVisibilityOption;

    SecurityModel securityModel;

    public User getUser() {
        return user;
    }

    @PostConstruct
    public void init() {
        securityModel = new SecurityModel();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void save(ActionEvent actionEvent) {
        FacesMessage msg = new FacesMessage("Successful", "Welcome :" + user.getFirstname());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String onFlowProcess(FlowEvent event) {
        logger.log(Level.INFO, "Current wizard step:{0}", event.getOldStep());
        logger.log(Level.INFO, "Next step:{0}", event.getNewStep());
        if (skip) {
            skip = false;
            return "confirm";
        } else {
            return event.getNewStep();
        }
    }

    public List<String> getSelectedAdditionalResources() {
        return selectedAdditionalResources;
    }

    public void setSelectedAdditionalResources(List<String> selectedAdditionalResources) {
        this.selectedAdditionalResources = selectedAdditionalResources;
    }

    public String getSelectedVisibilityOption() {
        return selectedVisibilityOption;
    }

    public void setSelectedVisibilityOption(String selectedVisibilityOption) {
        this.selectedVisibilityOption = selectedVisibilityOption;
    }

    public String getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }

    public Map<String, String> getVisibilityOptions() {
        return securityModel.getVisibilityOptions();
    }

    public List<String> getRoles() {
        return securityModel.getRoles();
    }

    public Map<String, String> getAdditionalResources() {
        return securityModel.getAdditionalResources();
    }
}
