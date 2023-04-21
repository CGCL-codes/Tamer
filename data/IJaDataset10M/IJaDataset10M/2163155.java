package modelesObjet;

import java.util.Date;

public interface ModeleImageRadar {

    public ModeleMire getMire();

    public ModeleFondCarte getFondCarte();

    public ModeleEchelle getEchelle();

    public String getChaine();

    public ModeleListeDonnees getDonnees();

    public String getNomFichier();

    public ModeleListePixels getPixels();

    public int getType();

    public ModeleListePluviometres getPluviometres();

    public Date getDate();
}
