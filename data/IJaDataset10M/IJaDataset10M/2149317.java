package br.eti.mps.chiroptera.lib.parsers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import br.eti.mps.chiroptera.constants.Constants;
import br.eti.mps.chiroptera.lib.ChiropteraException;
import br.eti.mps.chiroptera.lib.Job;
import br.eti.mps.chiroptera.lib.StatusDirector;

public class ParseStatusDirector {

    public static StatusDirector parse(Map<String, Object> configuration, String input) throws ChiropteraException {
        String language = (String) configuration.get(Constants.Chiroptera.ConfigurationKeys.SERVER_LANGUAGE);
        String country = (String) configuration.get(Constants.Chiroptera.ConfigurationKeys.SERVER_COUNTRY);
        Locale locale = new Locale(language, country);
        DateFormat df = new SimpleDateFormat(Constants.Bacula.DATE_FORMAT, locale);
        StatusDirector sd = new StatusDirector();
        StringBuffer sbTemp = new StringBuffer();
        String temp;
        Parser p = new Parser(input);
        sbTemp.append(p.getToken(Constants.CR)).append(Constants.CR);
        sbTemp.append(p.getToken(Constants.CR)).append(Constants.CR);
        sbTemp.append(p.getToken(Constants.CR));
        sd.setBanner(sbTemp.toString());
        p.getToken(Constants.CR);
        temp = p.getToken(Constants.CR).trim();
        if (temp.equalsIgnoreCase(Constants.Connection.Tokens.SCHEDULED_JOBS)) {
            p.getToken(Constants.CR);
            p.getToken(Constants.CR);
            temp = p.getToken(Constants.CR).trim();
            if (!temp.equalsIgnoreCase(Constants.Connection.Tokens.NO_SCHEDULED_JOBS)) {
                while (!temp.equalsIgnoreCase(Constants.Connection.Tokens.END_JOBS)) {
                    sd.getScheduledJobs().add(parseScheduledJob(temp, df));
                    temp = p.getToken(Constants.CR).trim();
                }
            }
        }
        p.getToken(Constants.CR);
        temp = p.getToken(Constants.CR).trim();
        if (temp.equalsIgnoreCase(Constants.Connection.Tokens.RUNNING_JOBS)) {
            p.getToken(Constants.CR);
            p.getToken(Constants.CR);
            temp = p.getToken(Constants.CR).trim();
            if (!temp.equalsIgnoreCase(Constants.Connection.Tokens.NO_RUNNING_JOBS)) {
                while (!temp.equalsIgnoreCase(Constants.Connection.Tokens.END_JOBS)) {
                    sd.getScheduledJobs().add(parseRunningJob(temp));
                    temp = p.getToken(Constants.CR).trim();
                }
            }
        }
        p.getToken(Constants.CR);
        temp = p.getToken(Constants.CR).trim();
        if (temp.equalsIgnoreCase(Constants.Connection.Tokens.TERMINATED_JOBS)) {
            p.getToken(Constants.CR);
            p.getToken(Constants.CR);
            temp = p.getToken(Constants.CR).trim();
            if (!temp.equalsIgnoreCase(Constants.Connection.Tokens.NO_TERMINATED_JOBS)) {
                while (!temp.equalsIgnoreCase(Constants.Connection.Tokens.END_JOBS) && temp.trim().length() > 0) {
                    sd.getScheduledJobs().add(parseTerminatedJob(temp));
                    temp = p.getToken(Constants.CR).trim();
                }
            }
        }
        return sd;
    }

    private static Job parseScheduledJob(String line, DateFormat df) throws ChiropteraException {
        Job job = new Job();
        job.setLevel(line.substring(0, 14).trim());
        job.setType(line.substring(15, 24).trim());
        job.setPriority(Integer.parseInt(line.substring(25, 27)));
        try {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(df.parse(line.substring(48, 67).trim()));
            job.setScheduled(calendar);
        } catch (ParseException pe) {
            throw new ChiropteraException(Constants.Chiroptera.Errors.DATE_PARSE, pe.getMessage(), pe);
        }
        job.setName(line.substring(48, 67).trim());
        job.setVolume(line.substring(67).trim());
        return job;
    }

    private static Job parseRunningJob(String line) {
        Job job = new Job();
        return job;
    }

    private static Job parseTerminatedJob(String line) {
        Job job = new Job();
        return job;
    }
}
