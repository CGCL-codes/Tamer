package team9.routenplaner.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.Vector;

@RemoteServiceRelativePath("routengraph")
public interface RoutengraphService extends RemoteService {

    public Knoten getKnoten(String raumNr);

    public Vector<Karte> getKarten();

    public Route berechneRoute(String start, String ziel);
}
