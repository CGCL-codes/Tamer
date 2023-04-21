package dmeduc.wicket.weblink.applicant;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.modelibra.util.Transformer;
import org.modelibra.wicket.container.DmPanel;
import org.modelibra.wicket.security.AccessPoint;
import dmeduc.DmEduc;
import dmeduc.weblink.WebLink;
import dmeduc.weblink.applicant.Applicant;
import dmeduc.weblink.applicant.Applicants;
import dmeduc.weblink.member.Member;
import dmeduc.weblink.member.Members;
import dmeduc.wicket.app.DmEducApp;
import dmeduc.wicket.app.home.HomePage;

/**
 * Applicant confirm panel.
 * 
 * @author Dzenan Ridjanovic
 * @version 2006-11-27
 */
@SuppressWarnings("serial")
public class ConfirmationPanel extends DmPanel {

    private Label unconfirmLabel;

    /**
	 * Constructs an applicant confirm panel.
	 * 
	 * @param wicketId
	 *            Wicket id
	 */
    public ConfirmationPanel(final String wicketId) {
        super(wicketId);
        ConfirmationForm confirmationForm = new ConfirmationForm("confirmationForm");
        add(confirmationForm);
        unconfirmLabel = new Label("unconfirm", "");
        unconfirmLabel.setVisible(false);
        add(unconfirmLabel);
    }

    public void setInvalidMessage(String message) {
        unconfirmLabel.setModelObject(message);
    }

    /**
	 * Applicant confirm form.
	 */
    private class ConfirmationForm extends Form {

        private String oidString = "";

        private TextField oidField;

        public ConfirmationForm(final String wicketId) {
            super(wicketId);
            oidField = new TextField("oidField", new Model(oidString));
            add(oidField);
        }

        /**
		 * Submits a user action.
		 */
        protected void onSubmit() {
            Applicant applicant;
            oidString = oidField.getModelObjectAsString();
            Long oid = Transformer.longInteger(oidString);
            if (oid == null) {
                unconfirmLabel.setVisible(true);
            } else {
                DmEducApp dmEducApp = (DmEducApp) getApplication();
                DmEduc dmEduc = dmEducApp.getDmEduc();
                WebLink webLink = dmEduc.getWebLink();
                Applicants applicants = webLink.getApplicants();
                applicant = (Applicant) applicants.getMember(oid);
                if (applicant != null) {
                    unconfirmLabel.setVisible(false);
                    Members members = webLink.getMembers();
                    Member newMember = new Member(members.getModel());
                    newMember.copyFromApplicant(applicant);
                    members.add(newMember);
                    applicants.remove(applicant);
                    signOutGest();
                    setResponsePage(HomePage.class);
                } else {
                    unconfirmLabel.setVisible(true);
                }
            }
        }
    }

    /**
	 * Signs out a guest user.
	 */
    private void signOutGest() {
        if (getAppSession().isUserSignedIn()) {
            Member user = (Member) getAppSession().getSignedInUser();
            if (user.getRole().equals(AccessPoint.CASUAL)) {
                if (user.getCode().equals(AccessPoint.GUEST) && user.getPassword().equals(AccessPoint.GUEST)) {
                    getAppSession().signOutUser();
                }
            }
        }
    }
}
