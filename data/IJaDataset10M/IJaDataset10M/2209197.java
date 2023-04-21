package models.requests;

import whisper.Whisper;
import connection.WhisperTransportConnection;

/**
 * This class is responsible for sending a "Login" request to the server.
 * 
 * @author Thomas Pedley
 */
public class RequestLogin extends Request {

    /** The first name of the avatar. */
    private String firstName;

    /** The last name of the avatar. */
    private String lastName;

    /** The password of the avatar. */
    private String password;

    /** The desired start location. */
    private String startLocation;

    /** The Sim name to start in. */
    private String simName;

    /** The desired local X coordinate to start at within the Sim. */
    private String x;

    /** The desired local Y coordinate to start at within the Sim. */
    private String y;

    /** The desired local Z coordinate to start at within the Sim. */
    private String z;

    /**
	 * Constructor. Log in to home or last location.
	 * 
	 * @param connection The connection over which the request will be sent.
	 * @param firstName The first name of the avatar.
	 * @param lastName The last name of the avatar.
	 * @param password The password of the avatar.
	 * @param home True to log in to home, false to log in to last location.
	 */
    public RequestLogin(WhisperTransportConnection connection, String firstName, String lastName, String password, boolean home) {
        super(connection);
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.startLocation = home ? "home" : "last";
    }

    /**
	 * Constructor. Log in to specific location.
	 * 
	 * @param connection The connection over which the request will be sent.
	 * @param firstName The first name of the avatar.
	 * @param lastName The last name of the avatar.
	 * @param password The password of the avatar.
	 * @param simName The Sim name to start in.
	 * @param x The desired local X coordinate to start at within the Sim.
	 * @param y The desired local Y coordinate to start at within the Sim.
	 * @param z The desired local Z coordinate to start at within the Sim.
	 */
    public RequestLogin(WhisperTransportConnection connection, String firstName, String lastName, String password, String simName, int x, int y, int z) {
        super(connection);
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.simName = simName;
        this.x = Integer.toString(x);
        this.y = Integer.toString(y);
        this.z = Integer.toString(z);
    }

    /**
	 * Execute the request.
	 */
    @Override
    public void execute() {
        addArgument("FirstName", firstName);
        addArgument("LastName", lastName);
        addArgument("Password", password);
        if (startLocation != null) {
            addArgument("StartLocation", startLocation);
        }
        addArgument("SimName", simName);
        addArgument("X", x);
        addArgument("Y", y);
        addArgument("Z", z);
        addArgument("ClientName", Whisper.NAME + Whisper.VERSION);
        String request = constructRequest();
        connection.send(request);
    }

    /**
	 * Get the name of the request.
	 * 
	 * @return The name of the request.
	 */
    @Override
    protected String getName() {
        return "Login";
    }
}
