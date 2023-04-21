package appointments.view.beans;

import java.util.HashMap;
import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

public class WelcomeBean {

    private HashMap<String, Locale> locales = null;

    public WelcomeBean() {
        locales = new HashMap<String, Locale>(2);
        locales.put("english", new Locale("en", "UK"));
        locales.put("spanish", new Locale("es", "AR"));
    }

    public void onChooseLocale(ActionEvent event) {
        String current = event.getComponent().getId();
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale((Locale) locales.get(current));
    }
}
