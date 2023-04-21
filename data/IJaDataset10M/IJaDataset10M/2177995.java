package fi.passiba.biblestudy;

import fi.passiba.services.persistance.Person;
import java.util.Locale;
import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;

public class BibleStudySession extends WebSession {

    public static BibleStudySession get() {
        return (BibleStudySession) Session.get();
    }

    private Person person;

    public BibleStudySession(Request request) {
        super(request);
        setLocale(new Locale("fi"));
    }

    public Person getPerson() {
        return person;
    }

    public boolean isAuthenticated() {
        return (person != null);
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
