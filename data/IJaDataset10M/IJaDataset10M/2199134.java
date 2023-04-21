package web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class CoordAchat {

    private GestionVente gestionVente;

    private Caddy caddy;

    public void ajouterDansCaddy(long refProd, int qte) {
        caddy.getMapRefQte().put(refProd, qte);
    }

    public List<Entry<Long, Integer>> getCaddyContent() {
        List<Entry<Long, Integer>> result;
        result = new ArrayList<Entry<Long, Integer>>(caddy.getMapRefQte().entrySet());
        return result;
    }

    public String getStatus() {
        return "<coordinateur ok>";
    }

    public void setCaddy(Caddy caddy) {
        this.caddy = caddy;
    }

    public Caddy getCaddy() {
        return caddy;
    }

    public void setGestionVente(GestionVente gestionVente) {
        this.gestionVente = gestionVente;
    }
}
