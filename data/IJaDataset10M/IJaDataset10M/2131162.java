package server.commands;

import server.Answer;
import server.MailFactory;
import server.OGSserver;
import server.Session;
import util.schedule.Holiday;
import java.text.DateFormatSymbols;
import java.util.List;
import java.util.ListIterator;

/**
 * Author: serhiy
 * Created on Aug 22, 2007, 5:19:12 PM
 */
public class GetHolidays extends AbstractServerCommand {

    @Override
    public boolean exec(Session session, Answer answer, ListIterator<String> cmd, String[] text) {
        formatHolidays(answer, Holiday.getHolidays());
        return true;
    }

    static void formatHolidays(Answer answer, List<Holiday> holidays) {
        answer.setSubject(MailFactory.getSubject("holidays", OGSserver.getMessageParameters()));
        for (Holiday holiday : holidays) {
            if (holiday.fromMonth == holiday.toMonth && holiday.fromDay == holiday.toDay) answer.println(formatDay(holiday.fromMonth, holiday.fromDay) + " : " + holiday.getName()); else answer.println(formatDay(holiday.fromMonth, holiday.fromDay) + " - " + formatDay(holiday.toMonth, holiday.toDay) + " : " + holiday.getName());
        }
    }

    private static String formatDay(int month, int day) {
        return new DateFormatSymbols().getMonths()[month] + ", " + day;
    }
}
