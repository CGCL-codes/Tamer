package edu.hawaii.halealohacli.command;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.xml.datatype.XMLGregorianCalendar;
import org.wattdepot.client.WattDepotClientException;
import org.wattdepot.util.tstamp.Tstamp;
import edu.hawaii.halealohacli.Server;

/**
 * Retrieves the energy consumed by a particular tower or lounge on a given day.
 * 
 * @author Jeffrey M. Beck
 * @author Zach Tomaszewski
 */
public class DailyEnergy implements Command {

    protected static final String NAME = "daily-energy";

    protected static final String ARGS = "[tower | lounge] [date]";

    protected static final String DESCRIPTION = "Returns the energy in kWh used by the tower or lounge for the specified date (yyyy-mm-dd).";

    /**
   * Given a tower or lounge, and date returns the energy used on that date.
   * 
   * @param args The name of the lounge or tower and the date to query .
   * @return A message describing energy used.
   * @throws CommandFailedException if given insufficient or incorrect arguments or if could not
   * query the server.
   */
    @Override
    public String execute(String... args) throws CommandFailedException {
        if (args.length < 2) {
            throw new InsufficientArgumentsException();
        }
        String location = args[0];
        String dateAsString = args[1];
        if ((!Server.isTower(location)) && (!Server.isLounge(location))) {
            throw new CommandFailedException(location + " is not the name of a tower or lounge.");
        }
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateAsString);
        } catch (ParseException e) {
            throw new CommandFailedException(dateAsString + " is not a valid date, or date format yyyy-MM-dd.", e);
        }
        try {
            XMLGregorianCalendar startOfDay = Tstamp.makeTimestamp(date.getTime());
            startOfDay.setTime(0, 0, 0);
            XMLGregorianCalendar endOfDay = Tstamp.incrementDays(Tstamp.makeTimestamp(date.getTime()), 1);
            endOfDay.setTime(0, 0, 0);
            double energy = this.getEnergyConsumed(location, startOfDay, endOfDay, 0);
            String output = location + "'s energy consumption for " + dateAsString + " was: ";
            output += String.format("%.1f kWh.\n", energy / 1000);
            return output;
        } catch (WattDepotClientException e) {
            throw new CommandFailedException("Could not gather needed data.", e);
        }
    }

    @Override
    public String getHelp() {
        return NAME + " " + ARGS + "\n\t " + DESCRIPTION + "\n";
    }

    @Override
    public String getName() {
        return NAME;
    }

    /**
   * Gets the energy of the given location (tower or lounge) for the requested day.
   * 
   * @param location The resource to poll for power
   * @param startOfDay The moment that represents the start of the requested day
   * @param endOfDay The moment that represents the end of the requested day
   * @param interval the sampling interval
   * @return The energy consumption on the requested day
   * @throws WattDepotClientException If could not poll the server for the necessary energy data.
   */
    public double getEnergyConsumed(String location, XMLGregorianCalendar startOfDay, XMLGregorianCalendar endOfDay, int interval) throws WattDepotClientException {
        return Server.getClient().getEnergyConsumed(location, startOfDay, endOfDay, interval);
    }
}
