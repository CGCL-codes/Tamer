package interfaces.admin;

import java.util.*;
import maingps.Main_Jen;
import donnees.*;
import engine.ElementTronconD;

public class BouchonDonneesInterfaceAdmin implements Donnees {

    HashMap<String, Ville> villes;

    HashMap<String, Route> routes;

    HashMap<String, Troncon> troncons;

    public BouchonDonneesInterfaceAdmin() {
        villes = new HashMap<String, Ville>();
        for (int i = 0; i < 5; i++) {
            villes.put("ville" + i, new Ville("ville" + i));
        }
        routes = new HashMap<String, Route>();
        for (int i = 0; i < 5; i++) {
            routes.put("route" + i, new Route("route" + i));
        }
        troncons = new HashMap<String, Troncon>();
        for (int i = 0; i < 5; i++) {
            Troncon t = new Troncon(villes.get("ville" + i), villes.get("ville" + i), routes.get("route" + i).getNom());
            troncons.put("route" + i + "#ville" + i + "#ville" + i, t);
            routes.get("route" + i).addTroncon(t);
        }
    }

    public Vector<String> getListeVilles() {
        Vector<String> a = new Vector<String>();
        for (String v : villes.keySet()) {
            a.add(v);
        }
        return a;
    }

    public Vector<String> getListeRoutes() {
        Vector<String> a = new Vector<String>();
        for (String r : routes.keySet()) {
            a.add(r);
        }
        return a;
    }

    public Ville getVille(String nom) {
        return villes.get(nom);
    }

    public void updateVille(Ville v) {
        villes.put(v.getNom(), v);
    }

    public void removeVille(Ville v) {
        villes.remove(v.getNom());
    }

    public Route getRoute(String nom) {
        return routes.get(nom);
    }

    public void updateRoute(Route r) {
        routes.put(r.getNom(), r);
    }

    public void removeRoute(Route r) {
        for (Troncon t : r.getTroncons()) {
            troncons.remove(getStringTroncon(r.getNom(), t.getVille1().getNom(), t.getVille2().getNom()));
        }
        routes.remove(r.getNom());
    }

    public Troncon getTroncon(String ville1, String ville2, String route) {
        Troncon t1 = troncons.get(getStringTroncon(route, ville1, ville2));
        Troncon t2 = troncons.get(getStringTroncon(route, ville2, ville1));
        if (t1 != null) {
            return t1;
        } else if (t2 != null) {
            return t2;
        } else {
            return null;
        }
    }

    public void updateTroncon(Troncon t) {
        Troncon t1 = troncons.get(getStringTroncon(t.getNomRoute(), t.getVille1().getNom(), t.getVille2().getNom()));
        Troncon t2 = troncons.get(getStringTroncon(t.getNomRoute(), t.getVille2().getNom(), t.getVille1().getNom()));
        if (t1 != null) {
            troncons.put(getStringTroncon(t.getNomRoute(), t.getVille1().getNom(), t.getVille2().getNom()), t);
        } else if (t2 != null) {
            troncons.put(getStringTroncon(t.getNomRoute(), t.getVille2().getNom(), t.getVille1().getNom()), t);
        } else {
            troncons.put(getStringTroncon(t.getNomRoute(), t.getVille1().getNom(), t.getVille2().getNom()), t);
        }
    }

    public void removeTroncon(Troncon t) {
        Troncon t1 = troncons.get(getStringTroncon(t.getNomRoute(), t.getVille1().getNom(), t.getVille2().getNom()));
        Troncon t2 = troncons.get(getStringTroncon(t.getNomRoute(), t.getVille2().getNom(), t.getVille1().getNom()));
        if (t1 != null) {
            troncons.remove(getStringTroncon(t.getNomRoute(), t.getVille1().getNom(), t.getVille2().getNom()));
            routes.get(t.getNomRoute()).removeTroncon(t);
        } else if (t2 != null) {
            troncons.remove(getStringTroncon(t.getNomRoute(), t.getVille2().getNom(), t.getVille1().getNom()));
            routes.get(t.getNomRoute()).removeTroncon(t);
        } else {
            Main_Jen.log.debug("Troncon n'existe pas");
        }
    }

    private String getStringTroncon(String route, String ville1, String ville2) {
        return route + "#" + ville1 + "#" + ville2;
    }

    public void acPayant(boolean b) {
        throw new RuntimeException("Pas utilis� dans la partie admin");
    }

    public void acRadar(boolean b) {
        throw new RuntimeException("Pas utilis� dans la partie admin");
    }

    public void acTourismeTroncon(boolean b) {
        throw new RuntimeException("Pas utilis� dans la partie admin");
    }

    public void acTourismeVille(boolean b) {
        throw new RuntimeException("Pas utilis� dans la partie admin");
    }

    public void acTypeRoute(String s) {
        throw new RuntimeException("Pas utilis� dans la partie admin");
    }

    public void acTypeVille(String s) {
        throw new RuntimeException("Pas utilis� dans la partie admin");
    }

    public void acVilleAEviter(String s) {
        throw new RuntimeException("Pas utilis� dans la partie admin");
    }

    public void acVitesseMax(int v) {
        throw new RuntimeException("Pas utilis� dans la partie admin");
    }

    public void acVitesseMin(int v) {
        throw new RuntimeException("Pas utilis� dans la partie admin");
    }

    public ArrayList<Troncon> getArrayTroncons(ArrayList<Integer> idTroncon) throws ExceptionAccesDonnees {
        throw new RuntimeException("Pas utilis� dans la partie admin");
    }

    public ArrayList<Ville> getArrayVilles(ArrayList<String> nom) throws ExceptionAccesDonnees {
        throw new RuntimeException("Pas utilis� dans la partie admin");
    }

    public Route getRouteWoTroncon(String nom) throws ExceptionAccesDonnees {
        throw new RuntimeException("Pas utilis� dans la partie admin");
    }

    public Troncon getTroncon(int idTroncon) throws ExceptionAccesDonnees {
        throw new RuntimeException("Pas utilis� dans la partie admin");
    }

    public HashMap<Integer, ElementTronconD> recupererDonneesTroncons() throws ExceptionAccesDonnees {
        throw new RuntimeException("Pas utilis� dans la partie admin");
    }

    public ArrayList<String> recupererDonneesVilles() throws ExceptionAccesDonnees {
        throw new RuntimeException("Pas utilis� dans la partie admin");
    }

    public void udpateXML(String s) throws ExceptionMiseAjourXML {
        throw new RuntimeException("Pas utilis� dans la partie admin");
    }

    public void reinitialiserContraintes() {
        throw new RuntimeException("Pas utilis� dans la partie admin");
    }

    public void createRoute(Route route) throws ExceptionCreationDonnees, ExceptionAccesDonnees, ExceptionMiseAjourDonnees {
        updateRoute(route);
    }

    public void createTroncon(Troncon troncon) throws ExceptionCreationDonnees, ExceptionAccesDonnees, ExceptionMiseAjourDonnees {
        updateTroncon(troncon);
    }

    public void createVille(Ville ville) throws ExceptionCreationDonnees, ExceptionAccesDonnees, ExceptionMiseAjourDonnees {
        updateVille(ville);
    }

    public void loadXML(String cheminFichier) throws ExceptionInitDonnees {
        throw new RuntimeException("Pas utilis� dans la partie admin");
    }
}
