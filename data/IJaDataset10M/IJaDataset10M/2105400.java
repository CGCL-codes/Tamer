package time_manager;

/**
 * @author Kwintio
 *
 */
public class Bell extends Message {

    private String beep;

    public Bell(String time, String nameEvent, String textMassage, String beep) {
        super(time, nameEvent, textMassage);
        this.beep = beep;
    }

    public String getBeep() {
        return beep;
    }
}
