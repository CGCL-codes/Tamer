package hu.sztaki.lpds.pgportal.services.asm.exceptions.general;

/**
 *
 * @author Akos Balasko MTA SZTAKI
 */
public class ASM_NoMachingPortIDException extends RuntimeException {

    private String workflow;

    private String user;

    public ASM_NoMachingPortIDException(Throwable cause, String user, String workflow) {
        super(cause);
        this.workflow = workflow;
        this.user = user;
    }

    public ASM_NoMachingPortIDException(String message, String user, String workflow) {
        super(message);
        this.workflow = workflow;
        this.user = user;
    }

    public ASM_NoMachingPortIDException(String message, String user) {
        super(message);
        this.user = user;
    }

    public ASM_NoMachingPortIDException(Throwable cause, String user) {
        super(cause);
        this.user = user;
    }

    public ASM_NoMachingPortIDException() {
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
