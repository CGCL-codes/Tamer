package server;

import objects.Galaxy;
import util.schedule.Schedule;
import util.schedule.ScheduleIO;
import util.schedule.ScheduleItem;
import java.io.LineNumberReader;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Iterator;

public final class SetGameScheduleCommand extends GMBroadcastMessageCommand {

    private static final String[] names = new DateFormatSymbols().getWeekdays();

    private static final DecimalFormat format = new DecimalFormat("00");

    private static String scheduleToString(Schedule s) {
        StringBuilder result = new StringBuilder();
        for (Iterator<ScheduleItem> i = s.getScheduleItems(); i.hasNext(); ) {
            ScheduleItem x = i.next();
            result.append(names[x.getDay()]).append(' ').append(format.format(x.getHour())).append(':').append(format.format(x.getMinute())).append('\n');
        }
        return result.toString();
    }

    @Override
    public final void doIt(LineNumberReader in, String[] cmd, javax.mail.internet.MimeMessage msg) {
        beforeDoIt(msg);
        int result = BAD_RESULT;
        if ((cmd.length < 2) || (cmd.length > 3)) {
            System.out.println("Wrong number of parameters");
            afterDoIt(cmd, result);
            return;
        }
        String gameName = cmd[1];
        Schedule s = null;
        try {
            s = ScheduleIO.load(".", gameName);
        } catch (Exception e) {
            System.err.println("Unknown game: " + gameName);
        }
        if (s == null) {
            System.out.println("Unknown game: " + gameName);
            if (cmd.length > 2) skipMessageBody(in);
            afterDoIt(cmd, result);
            return;
        }
        switch(cmd.length) {
            case 2:
                System.out.println("Game " + gameName + " has following schedule:\n");
                System.out.println(scheduleToString(s));
                result = OK_RESULT;
                break;
            case 3:
                setGameMindAddress(answer);
                String pass = cmd[2];
                if (!OGSserver.getProperties().getProperty("Server.Password").equals(pass)) {
                    System.out.println("Bad server password - " + pass);
                    skipMessageBody(in);
                    break;
                }
                s.clearScheduleItems();
                try {
                    String line;
                    while ((line = readLine(in)) != null) {
                        String[] tmp1 = util.Utils.split(line);
                        if (tmp1.length == 0) continue;
                        if (tmp1.length == 3) {
                            try {
                                int day = Integer.parseInt(tmp1[0]);
                                int hour = Integer.parseInt(tmp1[1]);
                                int minute = Integer.parseInt(tmp1[2]);
                                if ((day >= 1) && (day <= 7) && (hour >= 0) && (hour <= 23) && (minute >= 0) && (minute <= 59)) {
                                    s.addScheduleItem(new ScheduleItem(day, hour, minute));
                                    System.out.println(tmp1.toString() + " - OK");
                                } else System.out.println(tmp1.toString() + " - bad schedule item format");
                            } catch (NumberFormatException e) {
                                System.out.println(tmp1.toString() + " - bad schedule item format");
                            }
                        } else System.out.println(tmp1.toString() + " - bad schedule item format");
                    }
                    ScheduleIO.save(".", gameName, s);
                } catch (Exception e) {
                    System.out.println("Reading  schedule: " + e);
                    break;
                }
                toGalaxy = Galaxy.load(gameName, "", false);
                messageBody = MessageFormat.format(OGSserver.getMessage("mail.message.schedule"), toGalaxy.name, scheduleToString(s), toGalaxy.props.getProperty("mail.server.signature"));
                MailFactory.sendWall(toGalaxy, messageSubject(), messageBody);
                result = OK_RESULT;
                break;
        }
        afterDoIt(cmd, result);
    }
}
