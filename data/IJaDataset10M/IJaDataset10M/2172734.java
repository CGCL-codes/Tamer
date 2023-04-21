package fr.univ_tln.inf9.exaplanning.api.cours.event;

import java.util.Date;
import java.util.EventObject;

public class end_dateChangedEvent extends EventObject {

    private Date newEnd_date;

    public end_dateChangedEvent(Object source, Date newEnd_date) {
        super(source);
        this.newEnd_date = newEnd_date;
    }

    public Date getnewEnd_date() {
        return newEnd_date;
    }
}
