package edu.hawaii.halealohacli.command;

import javax.xml.datatype.XMLGregorianCalendar;
import org.wattdepot.client.WattDepotClientException;
import org.wattdepot.resource.sensordata.jaxb.SensorData;
import org.wattdepot.util.tstamp.Tstamp;
import edu.hawaii.halealohacli.Server;

/**
 * Gets the current power consumption for a particular tower or lounge.
 * 
 * @author Jeffrey M. Beck
 * @author Zach Tomaszewski
 */
public class CurrentPower implements Command {

    protected static final String NAME = "current-power";

    protected static final String ARGS = "[tower | lounge]";

    protected static final String DESCRIPTION = "Returns the current power consumption in kW for the associated tower or lounge.";

    /**
   * Given a tower or lounge, returns its current power usage.
   * 
   * @param args  The name of the lounge or tower to query.
   * @return A message describing current power consumption.
   * @throws CommandFailedException if given insufficient or incorrect
   *   arguments or if could not query the server.
   */
    @Override
    public String execute(String... args) throws CommandFailedException {
        if (args.length < 1) {
            throw new InsufficientArgumentsException();
        }
        String location = args[0];
        if ((!Server.isTower(location)) && (!Server.isLounge(location))) {
            throw new CommandFailedException(location + " is not the name of a tower or lounge.");
        }
        try {
            SensorData lastestData = Server.getClient().getLatestSensorData(location);
            XMLGregorianCalendar now = lastestData.getTimestamp();
            double power = this.getPowerConsumed(location, now);
            String dateTime = Server.formatDate(now);
            String output = location + "'s power as of " + dateTime;
            if (Tstamp.diff(now, Tstamp.makeTimestamp()) > 60 * 1000) {
                output += " (not very current)";
            }
            output += " was ";
            output += String.format("%.1f", power);
            output += " kW.";
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
   * Gets the power of the given location (tower or lounge) 
   * at the given time.
   * @param location   The resource to poll for power
   * @param timestamp  The moment for which to get power consumption 
   * @return  The power consumption
   * @throws WattDepotClientException  If could not poll the server for the 
   *    necessary power data.
   */
    public double getPowerConsumed(String location, XMLGregorianCalendar timestamp) throws WattDepotClientException {
        return Server.getClient().getPowerConsumed(location, timestamp);
    }
}
