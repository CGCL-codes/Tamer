package algoritms.microsoftTask;

/**
 * 
 * @author Sergiy Doroshenko
 *
 */
public class Appointment {

    String message;

    public Appointment(String message) {
        super();
        this.message = message;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj != null && obj instanceof Appointment) {
            return this.message.equals(((Appointment) obj).message);
        }
        return false;
    }

    @Override
    public String toString() {
        return message.toString();
    }

    public static Appointment merge(Appointment app, Appointment app2) {
        return new Appointment(app.getMessage() + " merged with " + app.getMessage());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
