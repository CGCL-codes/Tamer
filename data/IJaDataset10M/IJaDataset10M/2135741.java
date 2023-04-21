package galaxiia.ui.configuration;

import java.net.InetAddress;
import java.util.*;

class IdentificationServeur {

    private final Set<InetAddress> adresses = new HashSet<InetAddress>();

    private final int port;

    private byte[] id;

    private String adresse;

    private String nomCarte;

    private int nombreJoueurs;

    private int nombreJoueursMaximum;

    private boolean valide = false;

    byte[] getId() {
        return id;
    }

    Set<InetAddress> getAdresses() {
        return adresses;
    }

    String getAdresse() {
        return adresse;
    }

    String getNomCarte() {
        return nomCarte;
    }

    int getPort() {
        return port;
    }

    int getNombreJoueurs() {
        return nombreJoueurs;
    }

    int getNombreJoueursMaximum() {
        return nombreJoueursMaximum;
    }

    boolean isValide() {
        return valide;
    }

    void setValide(boolean valide) {
        this.valide = valide;
    }

    void setNombreJoueurs(int nombreJoueurs) {
        this.nombreJoueurs = nombreJoueurs;
    }

    void setNombreJoueursMaximum(int nombreJoueursMaximum) {
        this.nombreJoueursMaximum = nombreJoueursMaximum;
    }

    void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    void setNomCarte(String nomCarte) {
        this.nomCarte = nomCarte;
    }

    void setId(byte[] id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IdentificationServeur) {
            IdentificationServeur serveur = (IdentificationServeur) obj;
            if (serveur.id != null && id != null) {
                return Arrays.equals(serveur.id, id);
            } else if (serveur.id == null && id == null) {
                return serveur.adresse.equals(adresse) && serveur.port == port;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return Arrays.hashCode(id);
        } else {
            return adresse.hashCode() ^ port;
        }
    }

    IdentificationServeur(byte[] id, String adresse, int port) {
        if (adresse == null) {
            throw new NullPointerException();
        }
        this.id = id;
        this.adresse = adresse;
        this.port = port;
    }

    IdentificationServeur(byte[] id, InetAddress adresse, int port) {
        this(id, adresse.getHostAddress(), port);
    }

    IdentificationServeur(String adresse, int port) {
        this(null, adresse, port);
    }
}
