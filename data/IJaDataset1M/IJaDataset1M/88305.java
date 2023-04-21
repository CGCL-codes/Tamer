package jfireeagle.examples.swing;

import java.util.Calendar;
import java.util.*;
import jfireeagle.FireEagleClient;

public class RecentForm extends AbstractForm {

    private DateTimeField since;

    public RecentForm(FireEagleClient c) {
        super(c, 1);
        Calendar cal = Calendar.getInstance();
        since = new DateTimeField(cal);
        addField("Since", since);
        this.clear.setVisible(false);
    }

    @Override
    protected Object onSubmit() {
        Date d = since.getDate();
        System.out.println("onSubmit: " + d);
        this.client.getRecent(d);
        return null;
    }
}
