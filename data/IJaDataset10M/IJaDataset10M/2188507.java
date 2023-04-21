package donnees;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class TypeRoute {

    static HashMap<String, TypeRoute> typesRoutes = new HashMap<String, TypeRoute>();

    String nom = "";

    protected void addNewTypeRoute(TypeRoute t) {
        typesRoutes.put(t.getNom().toLowerCase(), t);
    }

    public String getNom() {
        return nom;
    }

    public static ArrayList<String> getListeTypesRoutes() {
        return new ArrayList<String>(typesRoutes.keySet());
    }

    public static TypeRoute getTypeRouteByName(String n) {
        return typesRoutes.get(n.toLowerCase());
    }

    public abstract int getNbPixelsEpaisseur();
}
