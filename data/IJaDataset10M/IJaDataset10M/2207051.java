package org.ladybug.gui.phases.client.users;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.ladybug.core.AbstractNamedItem;
import org.ladybug.core.users.User;
import org.ladybug.gui.toolbox.AbstractCenteredTranslucentGlassPaneComponent;
import org.ladybug.utils.ErrorCode;
import org.ladybug.utils.Operation;
import org.ladybug.utils.validation.ValidateArgument;
import org.ladybug.utils.validation.ValidationTemplate;

/**
 * @author Aurelian Pop
 */
class UsersInputComponent extends AbstractCenteredTranslucentGlassPaneComponent {

    private static final long serialVersionUID = 1L;

    private final JButton okButton = new JButton();

    private final JButton cancelButton = new JButton("Cancel");

    private final UsersModel userManagementModel;

    private UserDetailsPanel userDetailsPanel;

    private Operation operation;

    private AbstractNamedItem namedItem;

    public UsersInputComponent(final JFrame window, final UsersModel userManagementModel) {
        super(window);
        this.userManagementModel = userManagementModel;
        final MyActionListener actionListener = new MyActionListener();
        okButton.addActionListener(actionListener);
        cancelButton.addActionListener(actionListener);
    }

    public void setContext(final Operation operation, final User user) {
        userDetailsPanel = new UserDetailsPanel(operation, okButton, cancelButton, TEXT_LABEL_FOREGROUND);
        userDetailsPanel.setCancelButtonEnabled(true);
        switch(operation) {
            case CREATE:
                setTitle("Create new user");
                userDetailsPanel.setBrowseButtonEnabled(true);
                break;
            case DELETE:
                ValidateArgument.isNotNull(user, ValidationTemplate.NOT_NULL, "user");
                setTitle("Delete user");
                userDetailsPanel.setOkButtonEnabled(true);
                break;
            default:
                break;
        }
        this.operation = operation;
        namedItem = user;
        final JPanel centerPanel = getCenterPanel();
        centerPanel.add(userDetailsPanel);
    }

    /**
     * Listener for the button-pressing user actions.
     * 
     * @author Aurelian Pop
     */
    private class MyActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            final JButton button = (JButton) e.getSource();
            if (button == okButton) {
                switch(operation) {
                    case CREATE:
                        final String firstName = userDetailsPanel.getFirstNameDetail();
                        final String lastName = userDetailsPanel.getLastNameDetail();
                        final String login = userDetailsPanel.getLoginDetail();
                        final String password = userDetailsPanel.getPasswordDetail();
                        final String confirm = userDetailsPanel.getConfirmDetail();
                        if (password.equals(confirm)) {
                            final User user = new User(login, firstName, lastName, password);
                            user.setAvatar(userDetailsPanel.getAvatarDetail());
                            if (userManagementModel.getSuperModel().getLocalClient().create(user).getErrorCode() == ErrorCode.SUCCESS) {
                                deActivate();
                            }
                        }
                        break;
                    case DELETE:
                        if (namedItem != null && userManagementModel.getSuperModel().getLocalClient().delete(namedItem).getErrorCode() == ErrorCode.SUCCESS) {
                            deActivate();
                        }
                        break;
                    default:
                        break;
                }
            }
            if (button == cancelButton) {
                deActivate();
            }
        }
    }
}
