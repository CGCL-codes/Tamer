package se.rupy.content;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import se.rupy.http.Event;
import se.rupy.http.Query;
import se.rupy.http.Service;
import se.rupy.sprout.Node;
import se.rupy.sprout.Sprout;
import se.rupy.sprout.User;

public abstract class Label extends Sprout {

    public static class Permit extends Service {

        public int index() {
            return 2;
        }

        public String path() {
            return "/label";
        }

        public void filter(Event event) throws Event, Exception {
            Object key = event.session().get("key");
            if (key != null) {
                User user = User.get(key);
                if (user != null && user.permit("ADMIN")) {
                    return;
                }
            }
            event.output().println("<pre>unauthorized</pre>");
            throw event;
        }
    }

    public static class Edit extends Service {

        public int index() {
            return 3;
        }

        public String path() {
            return "/label";
        }

        public void filter(Event event) throws Event, Exception {
            if (event.query().method() == Query.POST) {
                event.query().parse();
                Sprout.redirect(event);
            }
        }
    }
}
