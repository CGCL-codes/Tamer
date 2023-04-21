package edu.vt.eng.swat.workflow.app.global;

public class App {

    public static String getTitle() {
        StringBuffer title = new StringBuffer(Constants.NAME + " " + Constants.VERSION);
        if (AppUser.getUserId() != null) {
            title.append(" (").append(AppUser.getUserFirstLastNames()).append(")");
        }
        return title.toString();
    }
}
