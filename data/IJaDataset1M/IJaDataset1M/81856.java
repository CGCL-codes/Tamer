package be.xios.mobile.project.mobilegui;

import be.xios.mobile.project.webservice.Afspraak;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.List;
import webservice.IOException;

/**
 *
 * @author Bert Roex
 */
public class EventsList extends List implements CommandListener {

    public static final String NAME = "EventsDetailsForm";

    private Command exit;

    private Command back;

    private Command details;

    private Navigation nav;

    private Calendar cal;

    private EventDetailsForm eventDetailsForm;

    private Afspraak[] events;

    public EventsList(String title) {
        super(title, Choice.IMPLICIT);
        this.deleteAll();
        nav = Navigation.getInstance();
        cal = Calendar.getInstance();
        eventDetailsForm = new EventDetailsForm("Event Details");
        nav.registerDisplayable(eventDetailsForm, EventDetailsForm.NAME);
        initializeCommands();
    }

    public void getEvents() {
        try {
            events = nav.getStub().getEventByDateId(nav.getLoginData().getCalendarID(), cal);
            for (int i = 0; i < events.length; i++) {
                this.append(events[i].getTitle(), null);
            }
            if (this.size() == 1) {
                nav.display(EventDetailsForm.NAME);
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initializeCommands() {
        exit = new Command("Exit", Command.EXIT, 0);
        back = new Command("Back", Command.BACK, 0);
        details = new Command("Event Details", Command.ITEM, 0);
        addCommand(back);
        addCommand(exit);
        addCommand(details);
        setCommandListener(this);
    }

    public void commandAction(Command command, Displayable disp) {
        if (command.equals(exit)) {
            nav.getMidlet().destroyApp(true);
            nav.getMidlet().notifyDestroyed();
        } else if (command.equals(back)) {
            nav.display(CalendarForm.NAME);
        } else if (command.equals(details)) {
            nav.display(EventDetailsForm.NAME);
        }
    }

    public Calendar getDate() {
        return cal;
    }

    public void setDate(Date date) {
        cal.setTime(date);
    }
}
