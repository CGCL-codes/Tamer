package org.isportal.portlet.maillist;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Locale;
import org.isportal.db.tables.Kategorija;
import org.isportal.db.tables.Message;
import org.isportal.portlet.start.MailListSummary;
import org.isportal.portlet.start.interfaces.ILastMailListMessages;

public class LastMessages implements ILastMailListMessages {

    public List<MailListSummary> getLastMails() {
        ArrayList msgs_in = new ArrayList();
        List<MailListSummary> msgs_out = new ArrayList<MailListSummary>();
        msgs_in = MessageDAO.getLastMessages();
        for (int i = 0; i < msgs_in.size(); i++) {
            Message tmp = (Message) msgs_in.get(i);
            MailListSummary msg_out = convert(tmp);
            msgs_out.add(i, msg_out);
        }
        return msgs_out;
    }

    private MailListSummary convert(Message msg) {
        MailListSummary mesg = new MailListSummary();
        mesg.setIdMail(msg.getIdMsg());
        Kategorija kat = msg.getKat();
        mesg.setCategory(kat.getNaziv());
        mesg.setTitle(msg.getZadeva());
        String datum_str = msg.getDatum();
        SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy", Locale.US);
        try {
            Date datum = df.parse(datum_str);
            System.out.println("Date parsing complete.");
            mesg.setDate(datum);
        } catch (ParseException e) {
            System.out.println("DEBUG: Couldn't parse date string...");
            System.out.println("DEBUG: Unparseable date: " + datum_str);
            e.printStackTrace();
        }
        return mesg;
    }
}
