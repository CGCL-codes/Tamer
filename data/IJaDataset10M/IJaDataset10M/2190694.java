package forms;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import de.sic_consult.forms.field.PropertyUI;

public class Auto {

    @NotNull()
    Marke marke;

    @Min(value = 20, message = "Zu Wenig")
    @Max(value = 300, message = "Zu Protzig")
    int ps;

    String kennzeichen;

    public void setKennzeichen(String kennzeichen) {
        this.kennzeichen = kennzeichen;
    }

    public String getKennzeichen() {
        return kennzeichen;
    }

    @PropertyUI(description = "Die Pferdest�rken")
    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    @PropertyUI(description = "Die Marke")
    public Marke getMarke() {
        return marke;
    }

    public void setMarke(Marke marke) {
        this.marke = marke;
    }

    @Override
    public String toString() {
        return getMarke() + " mit " + getPs();
    }
}
