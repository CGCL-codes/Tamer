package org.openremote.controller.protocol.datetime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import org.openremote.controller.model.sensor.Sensor;
import org.openremote.controller.protocol.EventListener;
import org.openremote.controller.utils.Logger;
import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

/**
 * DateTime commands can be used to display date or time on the console
 * or use certain DateTime events to trigger rules. To calculate sunrise and sunset the location has to be given as longitude, latidude
 * and timezone. Timezone is the string identifier which is available in Java eg. Europe/Berlin
 * 
 * The following commands are available in the moment:
 * - date (returns a date/time as string depending on the given formatter)
 * - sunrise (returns the sunrise time as string depending on the given formatter)
 * - sunset (returns the sunset time as string depending on the given formatter)
 * - minutesUntilSunrise (returns an integer with the minutes until sunrise)
 * - minutesUntilSunset (returns an integer with the minutes until sunset)
 * - isDay (returns a boolean string)
 * - isNight (returns a boolean string)
 *
 * @author Marcus
 */
public class DateTimeCommand implements EventListener, Runnable {

    private static final Logger logger = Logger.getLogger(DateTimeCommandBuilder.DATETIME_PROTOCOL_LOG_CATEGORY);

    private TimeZone timezone;

    private String command;

    private SimpleDateFormat dateFormatter;

    private SunriseSunsetCalculator calculator;

    private Thread pollingThread;

    private Sensor sensor;

    public DateTimeCommand(String latitude, String longitude, String timezone, String command, String format) {
        if (timezone == null) {
            this.timezone = TimeZone.getDefault();
        } else {
            this.timezone = TimeZone.getTimeZone(timezone);
        }
        if (format != null) {
            dateFormatter = new SimpleDateFormat(format);
        } else {
            dateFormatter = new SimpleDateFormat();
        }
        this.command = command;
        if (!command.equalsIgnoreCase("date")) {
            Location location = new Location(latitude, longitude);
            this.calculator = new SunriseSunsetCalculator(location, timezone);
            logger.debug("DaylightCalculatorCommand created with values latitude=" + latitude + "; longitude=" + longitude + "; timezone=" + timezone);
        }
    }

    public String calculateData() {
        if (command.equalsIgnoreCase("date")) {
            return dateFormatter.format(Calendar.getInstance(this.timezone).getTime());
        } else if (command.equalsIgnoreCase("sunrise")) {
            Calendar now = Calendar.getInstance(timezone);
            Calendar officialSunriseDate = calculator.getOfficialSunriseCalendarForDate(now);
            return dateFormatter.format(officialSunriseDate.getTime());
        } else if (command.equalsIgnoreCase("sunset")) {
            Calendar now = Calendar.getInstance(timezone);
            Calendar officialSunsetDate = calculator.getOfficialSunsetCalendarForDate(now);
            return dateFormatter.format(officialSunsetDate.getTime());
        } else if (command.equalsIgnoreCase("minutesUntilSunrise")) {
            Calendar now = Calendar.getInstance(timezone);
            Calendar officialSunriseDate = calculator.getOfficialSunriseCalendarForDate(now);
            if (now.after(officialSunriseDate)) {
                Calendar tomorrow = (Calendar) now.clone();
                tomorrow.add(Calendar.DAY_OF_YEAR, 1);
                officialSunriseDate = calculator.getOfficialSunriseCalendarForDate(tomorrow);
            }
            int daysUntilSunrise = officialSunriseDate.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR);
            int hoursUntilSunrise = officialSunriseDate.get(Calendar.HOUR_OF_DAY) - now.get(Calendar.HOUR_OF_DAY);
            int minutesUntilSunrise = officialSunriseDate.get(Calendar.MINUTE) - now.get(Calendar.MINUTE);
            return Integer.toString((daysUntilSunrise * 24 * 60) + (hoursUntilSunrise * 60) + minutesUntilSunrise);
        } else if (command.equalsIgnoreCase("minutesUntilSunset")) {
            Calendar now = Calendar.getInstance(timezone);
            Calendar officialSunsetDate = calculator.getOfficialSunsetCalendarForDate(now);
            if (now.after(officialSunsetDate)) {
                Calendar tomorrow = (Calendar) now.clone();
                tomorrow.add(Calendar.DAY_OF_YEAR, 1);
                officialSunsetDate = calculator.getOfficialSunsetCalendarForDate(tomorrow);
            }
            int daysUntilSunset = officialSunsetDate.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR);
            int hoursUntilSunset = officialSunsetDate.get(Calendar.HOUR_OF_DAY) - now.get(Calendar.HOUR_OF_DAY);
            int minutesUntilSunset = officialSunsetDate.get(Calendar.MINUTE) - now.get(Calendar.MINUTE);
            return Integer.toString((daysUntilSunset * 24 * 60) + (hoursUntilSunset * 60) + minutesUntilSunset);
        } else if (command.equalsIgnoreCase("isDay")) {
            Calendar now = Calendar.getInstance(timezone);
            Calendar officialSunriseDate = calculator.getOfficialSunriseCalendarForDate(now);
            Calendar officialSunsetDate = calculator.getOfficialSunsetCalendarForDate(now);
            if (now.after(officialSunriseDate) && now.before(officialSunsetDate)) {
                return "true";
            } else {
                return "false";
            }
        } else if (command.equalsIgnoreCase("isNight")) {
            Calendar now = Calendar.getInstance(timezone);
            Calendar officialSunriseDate = calculator.getOfficialSunriseCalendarForDate(now);
            Calendar officialSunsetDate = calculator.getOfficialSunsetCalendarForDate(now);
            if (now.after(officialSunriseDate) && now.before(officialSunsetDate)) {
                return "false";
            } else {
                return "true";
            }
        }
        logger.error("DateTimeCommand does not know about this command: " + command);
        return "Unknown command";
    }

    @Override
    public void setSensor(Sensor sensor) {
        logger.debug("*** setSensor called as part of EventListener init *** sensor is: " + sensor);
        this.sensor = sensor;
        pollingThread = new Thread(this);
        pollingThread.setName("Polling thread for sensor: " + sensor.getName());
        pollingThread.start();
    }

    @Override
    public void stop(Sensor sensor) {
        pollingThread.stop();
    }

    @Override
    public void run() {
        logger.debug("Sensor thread started for sensor: " + sensor);
        boolean doPoll = true;
        while (doPoll) {
            String readValue = this.calculateData();
            if (!"N/A".equals(readValue)) {
                sensor.update(readValue);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                doPoll = false;
                pollingThread.interrupt();
            }
        }
        logger.debug("*** Out of run method: " + sensor);
    }

    public TimeZone getTimezone() {
        return timezone;
    }

    public String getCommand() {
        return command;
    }
}
