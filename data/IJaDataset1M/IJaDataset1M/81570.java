package edu.hawaii.halealohacli.command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.wattdepot.client.ResourceNotFoundException;
import org.wattdepot.client.WattDepotClient;
import org.wattdepot.client.WattDepotClientException;

/**
 * Returns the energy used since the date (yyyy-mm-dd) to now.
 * 
 * @author bking
 * 
 */
public class EnergySinceModule implements CommandInterface {

    private String command = "energy-since";

    private String properUsage = command + " [tower | lounge] [date]" + "\nReturns the energy used since the date (yyyy-mm-dd) to now.";

    @Override
    public boolean parseInput(WattDepotClient client, String input) {
        String[] array = input.split(" ");
        if (array.length == 3) {
            String sourceLocation = array[1];
            String timestamp = array[2];
            XMLGregorianCalendar startTime = parseTime(timestamp);
            if (startTime == null) {
                System.out.format("Date %s is in an invalid format. %n", timestamp);
                return false;
            }
            return printEnergySince(client, sourceLocation, startTime);
        } else {
            System.out.format("Invalid args for %s.%n%s%n", command, properUsage);
            return false;
        }
    }

    /**
   * Private method that prints the energy used since the date.
   * 
   * @param client WattDepotClient object
   * @param sourceLocation String for the source location
   * @param startTime XMLGregorianCalendar object for the date
   * @return boolean True if it was successful in printing or false if it was not
   */
    protected boolean printEnergySince(WattDepotClient client, String sourceLocation, XMLGregorianCalendar startTime) {
        try {
            XMLGregorianCalendar endTime = client.getLatestSensorData(sourceLocation).getTimestamp();
            startTime.setTime(0, 0, 0);
            double energyConsumed = client.getEnergyConsumed(sourceLocation, startTime, endTime, 0);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            String start = format.format(startTime.toGregorianCalendar().getTime());
            String end = format.format(endTime.toGregorianCalendar().getTime());
            System.out.format("Total energy consumption by %s from %s to %s is: %.1f kWh%n", sourceLocation, start, end, energyConsumed / 1000);
            return true;
        } catch (ResourceNotFoundException e) {
            System.out.println(sourceLocation + " is not a valid source.");
            return false;
        } catch (WattDepotClientException e) {
            System.out.println("An error has occured printing out the energy used since.");
            return false;
        }
    }

    /**
   * Private method that parses the time stamp and returns an <code>XMLGregorianCalendar</code>
   * object.
   * 
   * @param timestamp Date string
   * @return <code>XMLGregorianCalendar</code> time stamp or null if the input time stamp was
   * invalid
   */
    protected XMLGregorianCalendar parseTime(String timestamp) {
        String[] array = timestamp.split("-");
        int year = 0;
        int month = 0;
        int day = 0;
        if (array.length < 3) {
            return null;
        }
        try {
            year = Integer.valueOf(array[0]);
            month = Integer.valueOf(array[1]);
            day = Integer.valueOf(array[2]);
            GregorianCalendar calendar = new GregorianCalendar();
            XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
            date.setHour(0);
            date.setMinute(0);
            date.setSecond(0);
            date.setYear(year);
            date.setMonth(month);
            date.setDay(day);
            return date;
        } catch (NumberFormatException e) {
            System.out.println("Time stamp is in an invalid format.");
            return null;
        } catch (DatatypeConfigurationException e) {
            System.out.println("Error in parsing the timestamp.");
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println("The time stamp is invalid.");
            return null;
        }
    }
}
