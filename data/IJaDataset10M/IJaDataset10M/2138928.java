package edu.washington.mysms.server.sample.starbus;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import edu.washington.mysms.security.SqlAccount;

public class TaggingTimeAccuracyConsole implements Runnable {

    private SqlAccount starbusSqlAccount;

    public TaggingTimeAccuracyConsole(SqlAccount starbusSqlAccount) {
        this.starbusSqlAccount = starbusSqlAccount;
    }

    private static long getTimeDifference(Route route, RouteArchetype archetype, GPSPoint taggedDestination, GPSPoint groundTruthDestination) {
        VirtualGPSPoint tagged = archetype.findClosestVirtualPoint(taggedDestination);
        VirtualGPSPoint groundTruth = archetype.findClosestVirtualPoint(groundTruthDestination);
        return Math.abs(tagged.getTime().getTime() - groundTruth.getTime().getTime());
    }

    @Override
    public void run() {
        Scanner input = new Scanner(System.in);
        Route route = null;
        RouteArchetype archetype = null;
        GPSPoint taggedLocation = null;
        GPSPoint groundTruthLocation = null;
        while (true) {
            System.out.println("Make a selection:");
            System.out.println("1 - Set Route" + ((route == null) ? "." : ": " + route.getRouteNumber()));
            if (route != null) {
                System.out.println("2 - Set Archetype" + ((archetype == null) ? "." : ": " + archetype.getArchetypeName()));
            }
            if (route != null && archetype != null) {
                System.out.println("3 - Set Tagged Location" + ((taggedLocation == null) ? "." : ": " + taggedLocation.toString()));
                System.out.println("4 - Set True Location" + ((groundTruthLocation == null) ? "." : ": " + groundTruthLocation.toString()));
                System.out.println("0 - Exit the console");
            }
            if (route != null && archetype != null && taggedLocation != null && groundTruthLocation != null) {
                System.out.println();
                System.out.println("Time between locations: " + getTimeDifference(route, archetype, taggedLocation, groundTruthLocation) + " ms");
                System.out.println();
            }
            System.out.print("> ");
            String selectionString = input.nextLine();
            if (selectionString.compareToIgnoreCase("exit") == 0 || selectionString.compareToIgnoreCase("quit") == 0) {
                return;
            }
            int selection = -1;
            try {
                selection = Integer.parseInt(selectionString);
            } catch (Exception e) {
            }
            switch(selection) {
                case 1:
                    System.out.print("Enter Route Number > ");
                    String routeNumber = input.nextLine();
                    try {
                        route = new Route(starbusSqlAccount, routeNumber);
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        route = null;
                        System.out.println();
                        continue;
                    }
                    if (archetype != null) {
                        try {
                            archetype = new RouteArchetype(starbusSqlAccount, route, archetype.getArchetypeName());
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                            archetype = null;
                        }
                    }
                    System.out.println();
                    continue;
                case 2:
                    if (route == null) {
                        System.out.println("You made an invalid selection;");
                        System.out.println();
                        continue;
                    }
                    System.out.print("Enter Archetype Name > ");
                    String archetypeName = input.nextLine();
                    try {
                        archetype = new RouteArchetype(starbusSqlAccount, route, archetypeName);
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        archetype = null;
                    }
                    System.out.println();
                    continue;
                case 3:
                    if (route == null || archetype == null) {
                        System.out.println("You made an invalid selection;");
                        System.out.println();
                        continue;
                    }
                    System.out.print("Enter GPS Coordinates for Tagged Location (deglat, deglon) > ");
                    String taggedString = input.nextLine();
                    try {
                        taggedLocation = gpsPointFromString(taggedString);
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        taggedLocation = null;
                    }
                    System.out.println();
                    continue;
                case 4:
                    if (route == null || archetype == null) {
                        System.out.println("You made an invalid selection;");
                        System.out.println();
                        continue;
                    }
                    System.out.print("Enter GPS Coordinates for True Location (deglat, deglon) > ");
                    String trueString = input.nextLine();
                    try {
                        groundTruthLocation = gpsPointFromString(trueString);
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        groundTruthLocation = null;
                    }
                    System.out.println();
                    continue;
                case 0:
                    return;
                default:
                    System.out.println("You made an invalid selection;");
                    System.out.println();
                    break;
            }
        }
    }

    private GPSPoint gpsPointFromString(String s) throws Exception {
        String[] latAndLon = s.split(",");
        double lat = Double.parseDouble(latAndLon[0]);
        double lon = Double.parseDouble(latAndLon[1]);
        return new GPSPoint(lat, lon);
    }

    public static void main(String[] args) {
        System.out.println("Reading properties....");
        Properties devProp = new Properties();
        try {
            InputStream pStream = ClassLoader.getSystemResourceAsStream(Service.propertiesFileName);
            devProp.load(pStream);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Connecting to Starbus Database....");
        String starbus_hostname = devProp.getProperty("starbus.hostname");
        System.out.println("hostname: " + starbus_hostname);
        String starbus_database = devProp.getProperty("starbus.database");
        System.out.println("database: " + starbus_database);
        String starbus_username = devProp.getProperty("starbus.username");
        System.out.println("username: " + starbus_username);
        String starbus_password = devProp.getProperty("starbus.password");
        System.out.println("password: " + starbus_password);
        System.out.println();
        System.out.println("Initializing Route parameters....");
        Route.initializeParameters(devProp);
        System.out.println("Initializing Route Archetype parameters....");
        RouteArchetype.initializeParameters(devProp);
        SqlAccount starbusSqlAccount = new SqlAccount(starbus_hostname, starbus_database, starbus_username, starbus_password);
        new TaggingTimeAccuracyConsole(starbusSqlAccount).run();
    }
}
