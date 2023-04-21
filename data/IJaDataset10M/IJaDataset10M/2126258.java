package hu.sztaki.lpds.pgportal.services.asm.exceptions.download;

/**
 *
 * @author Akos Balasko MTA SZTAKI
 */
public class Download_GettingFileToPortalServiceException extends RuntimeException {

    private String workflow;

    private String user;

    public Download_GettingFileToPortalServiceException(Throwable cause, String user, String workflow) {
        super(cause);
        this.workflow = workflow;
        this.user = user;
    }

    public Download_GettingFileToPortalServiceException(String message, String user, String workflow) {
        super(message);
        this.workflow = workflow;
        this.user = user;
    }

    public Download_GettingFileToPortalServiceException(String message, String user) {
        super(message);
        this.user = user;
    }

    public Download_GettingFileToPortalServiceException(Throwable cause, String user) {
        super(cause);
        this.user = user;
    }

    public Download_GettingFileToPortalServiceException() {
    }

    public String getWorkflow() {
        return workflow;
    }

    public String getUser() {
        return user;
    }

    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
