package pt.ips.estsetubal.mig.academicCloud.client.helper;

import java.util.List;
import pt.ips.estsetubal.mig.academicCloud.client.components.dialogs.ApplicationDialogBox;
import pt.ips.estsetubal.mig.academicCloud.client.components.dialogs.DialogBoxType;
import pt.ips.estsetubal.mig.academicCloud.client.helper.log.ClientLogger;
import pt.ips.estsetubal.mig.academicCloud.client.helper.log.ILogger;
import pt.ips.estsetubal.mig.academicCloud.shared.constants.ApplicationConstants;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.AuditDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.SessionDTO;
import com.google.gwt.user.client.Window;

/**
 * This class gives access to the several helper classes to manage the client
 * application. It also provides a method to display messages to the user.
 * 
 * @author António Casqueiro
 */
public class ClientApplicationHelper {

    /**
	 * Application name.
	 */
    private String name;

    /**
	 * Application version.
	 */
    private String version;

    /**
	 * Reference to the object responsible for managing the unhandled
	 * exceptions.
	 */
    private ClientExceptionHandler exceptionHandler = null;

    /**
	 * Reference to the object responsible for managing the application log.
	 */
    private ILogger log = null;

    /**
	 * Reference to the object responsible for managing the application
	 * configuration parameters.
	 */
    private ClientConfiguration configuration = null;

    /**
	 * Reference to the object responsible for managing information from the
	 * current session.
	 */
    private ClientContextInformation contextInformation = null;

    /**
	 * Reference to the object that hold the application dialog box.
	 */
    private ApplicationDialogBox dialogBox;

    /**
	 * Reference to the only instance of this class.
	 */
    private static ClientApplicationHelper INSTANCE = new ClientApplicationHelper();

    /**
	 * Returns the singleton instance.
	 * 
	 * @return the instance of this class
	 */
    public static ClientApplicationHelper getInstance() {
        return INSTANCE;
    }

    /**
	 * Constructor. It's private to prevent the creation of instances by other
	 * classes.
	 */
    private ClientApplicationHelper() {
        name = ApplicationConstants.APPLICATION_NAME;
        version = ApplicationConstants.VERSION;
        dialogBox = new ApplicationDialogBox();
        log = new ClientLogger();
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public ClientExceptionHandler getExceptionHandler() {
        if (exceptionHandler == null) {
            exceptionHandler = new ClientExceptionHandler();
        }
        return exceptionHandler;
    }

    public ILogger getLog() {
        return log;
    }

    public ClientConfiguration getConfiguration() {
        if (configuration == null) {
            configuration = new ClientConfiguration();
        }
        return configuration;
    }

    public ClientContextInformation getContextInformation() {
        if (contextInformation == null) {
            AuditDTO audit = new AuditDTO();
            SessionDTO session = new SessionDTO();
            session.setAudit(audit);
            contextInformation = new ClientContextInformation(session);
        }
        return contextInformation;
    }

    /**
	 * Show an informative message.
	 * 
	 * @param message
	 *            message
	 */
    public void showInformation(String message) {
        dialogBox.show(message, DialogBoxType.INFORMATION);
    }

    /**
	 * Show an error message.
	 * 
	 * @param message
	 *            message
	 */
    public void showError(String message) {
        dialogBox.show(message, DialogBoxType.ERROR);
    }

    /**
	 * Show an error message.
	 * 
	 * @param message
	 *            message
	 */
    public void showError(List<String> messages) {
        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append(message);
            sb.append("<br/>");
        }
        sb.delete(sb.length() - 5, sb.length());
        dialogBox.show(sb.toString(), DialogBoxType.ERROR);
    }

    /**
	 * Show an warning message.
	 * 
	 * @param message
	 *            message
	 */
    public void showWarning(String message) {
        dialogBox.show(message, DialogBoxType.WARNING);
    }

    /**
	 * Show a confirmation message.
	 * 
	 * @param message
	 *            message
	 * @return true if the user clicks Yes/OK and false otherwise.
	 */
    public boolean askConfirmation(String message) {
        return Window.confirm(message);
    }
}
