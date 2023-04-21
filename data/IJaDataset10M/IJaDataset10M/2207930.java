package szene.display;

import javax.microedition.location.Coordinates;
import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationProvider;
import szene.main.AbstractSzene1Client;
import szene.main.Szene1Client;
import weblife.server.RequestInterface;
import de.enough.polish.ui.Command;
import de.enough.polish.ui.Displayable;
import de.enough.polish.ui.Form;

/**
 * Liefert die GPS Koordinaten des Benutzers
 * @author Knoll
 *
 */
public class GPS extends AbstractDisplayThread {

    private Szene1Client client;

    private RequestInterface rq;

    private Form form;

    private double latitude;

    private double longitude;

    /**
	 * 
	 * @param client
	 */
    public GPS(Szene1Client client, RequestInterface rq) {
        super(client);
        this.client = client;
        this.rq = rq;
        form = new Form("Bitte Warten");
        form.setTicker(AbstractSzene1Client.szeneTicker);
        form.addCommand(AbstractSzene1Client.backCmd);
        form.setCommandListener(this);
        client.setCurrent(form);
        this.start();
    }

    public void run() {
        latitude = 0;
        longitude = 0;
        try {
            Criteria cr = new Criteria();
            cr.setHorizontalAccuracy(500);
            LocationProvider lp = LocationProvider.getInstance(cr);
            Location l = lp.getLocation(60);
            Coordinates c = l.getQualifiedCoordinates();
            if (c != null) {
                latitude = c.getLatitude();
                longitude = c.getLongitude();
                this.form.append(String.valueOf(latitude));
                this.form.append(String.valueOf(longitude));
            } else this.form.append("GPS Fail");
            rq.GetUser().setLattitude(latitude);
            rq.GetUser().setLongitude(longitude);
        } catch (InterruptedException e) {
            AlertError(e.getMessage());
            e.printStackTrace();
        } catch (LocationException e) {
            AlertError(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            AlertError(e.toString());
            e.printStackTrace();
        }
    }

    public void commandAction(Command cmd, Displayable dsp) {
        if (cmd == AbstractSzene1Client.cancelCmd) {
            this.client.ShowMenu();
        }
        if (cmd == AbstractSzene1Client.backCmd) {
            this.client.ShowMenu();
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void RefreshLocation() {
        this.start();
    }
}
