package mjc.tds;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;

public class Info_Classe extends Info implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Info_Classe parente;

    protected boolean hasAParent;

    protected TDM constructeurs;

    protected TDA tda;

    protected TDM tdm;

    protected TDC tdc;

    protected int deplacement;

    protected int taille;

    protected int indiceMethodeSuivant;

    protected int indiceAttributSuivant;

    public Info_Classe(String nom, int deplacement) {
        super(nom);
        this.hasAParent = false;
        this.deplacement = deplacement;
        this.taille = 0;
        this.constructeurs = new TDM(this.nom);
        this.tda = new TDA();
        this.tdm = new TDM(this.nom);
        this.indiceMethodeSuivant = 0;
        this.indiceAttributSuivant = 0;
    }

    public Info_Classe(String nom, int deplacement, Info_Classe _parente) {
        super(nom);
        this.parente = _parente;
        this.hasAParent = true;
        this.constructeurs = new TDM(this.nom);
        this.tda = new TDA(_parente.tda);
        this.tdm = new TDM(_parente.tdm, _parente, _parente.getNom(), this.nom);
        this.deplacement = deplacement;
        this.taille = _parente.getTaille();
        this.indiceMethodeSuivant = this.parente.indiceMethodeSuivant;
        this.indiceAttributSuivant = this.parente.indiceAttributSuivant;
    }

    public Info_Methode getConstructeurGlobalement(CleMethode cleMethode) {
        Info_Methode constructeur = constructeurs.chercherLocalement(cleMethode);
        if (constructeur == null && this.hasAParent) {
            cleMethode.setNom(this.parente.getNom());
            return this.parente.getConstructeurGlobalement(cleMethode);
        } else {
            return constructeur;
        }
    }

    public Info_Methode getConstructeurSuper(CleMethode cleMethode) {
        if (this.hasAParent) {
            return this.parente.getConstructeurGlobalement(cleMethode);
        } else {
            return null;
        }
    }

    public Info_Methode getConstructeurLocalement(CleMethode cleMethode) {
        return constructeurs.chercherLocalement(cleMethode);
    }

    public void addConstructeur(CleMethode cleMethode, Info_Methode constructeur) {
        if (constructeur.getIndice() == this.indiceMethodeSuivant) {
            this.indiceMethodeSuivant++;
        }
        this.constructeurs.ajouter(cleMethode, constructeur);
    }

    public void removeConstructeur(CleMethode cleMethode) {
        this.constructeurs.removeMethode(cleMethode);
    }

    public Info_Methode getMethodeLocalement(CleMethode cleMethode) {
        return tdm.chercherLocalement(cleMethode);
    }

    public Info_Methode getMethodeGlobalement(CleMethode cleMethode) {
        return tdm.chercherGlobalement(cleMethode);
    }

    public Info_Methode getMethodeSuper(CleMethode cleMethode) {
        if (this.hasAParent) {
            return parente.getMethodeGlobalement(cleMethode);
        } else {
            return null;
        }
    }

    public Info_Attribut getAttributLocalement(String ident) {
        return tda.chercherLocalement(ident);
    }

    public Info_Attribut getAttributGlobalement(String ident) {
        return tda.chercherGlobalement(ident);
    }

    public Info_Attribut getAttributSuper(String ident) {
        if (this.hasAParent) {
            return parente.getAttributGlobalement(ident);
        } else {
            return null;
        }
    }

    public void addMethode(CleMethode cleMethode, Info_Methode methode) {
        if (methode.getIndice() == this.indiceMethodeSuivant) this.indiceMethodeSuivant++;
        this.tdm.ajouter(cleMethode, methode);
    }

    public void removeMethode(CleMethode cleMethode) {
        this.tdm.removeMethode(cleMethode);
    }

    public int getIndiceMethodeSuivant() {
        return this.indiceMethodeSuivant;
    }

    public int getIndiceAttributSuivant() {
        return this.indiceAttributSuivant;
    }

    public void addAttribut(String nom, Info_Attribut infos) {
        if (infos.getIndice() == this.indiceAttributSuivant) {
            this.indiceAttributSuivant++;
            taille += infos.getType().getTaille();
        }
        this.tda.ajouter(nom, infos);
    }

    /**Cherche si un identifiant est d�j� utilis� pour un nom de classe ou un attribut.
	 * @param nom Le nom que l'on cherche.
	 * @return true si l'identifiant existe d�j�, false sinon. 
	 */
    public boolean chercheSymboleLocal(String nom) {
        boolean found = false;
        if (tdc != null) found = (tdc.chercherLocalement(nom) != null);
        if (!found) found = (tda.chercherLocalement(nom) != null);
        return found;
    }

    public TDA getTDA() {
        return this.tda;
    }

    public TDM getTDM() {
        return this.tdm;
    }

    public TDC getTDC() {
        return this.tdc;
    }

    public int getDeplacement() {
        return this.deplacement;
    }

    public int getTaille() {
        return this.taille;
    }

    public Type getType() {
        return new Type(this.nom, this.taille);
    }

    public boolean hasAParent() {
        return this.hasAParent;
    }

    public Info_Classe getParent() {
        return this.parente;
    }

    public Type getParentType() {
        return this.parente.getType();
    }

    public boolean compareTo(Info_Classe classe) {
        return this.nom.equals(classe.getNom());
    }

    public boolean isAChildOf(String type) {
        if (this.nom.equals(type)) {
            return true;
        } else {
            if (this.hasAParent) {
                return this.parente.isAChildOf(type);
            } else {
                return false;
            }
        }
    }

    public boolean isAChildOf(Info_Classe classe) {
        return isAChildOf(classe.getNom());
    }

    public String toString() {
        return "Classe : " + nom + ((!hasAParent) ? "" : ". Extends : ") + ". D�placement : " + deplacement + "\n\t" + tda.toString() + "\n\t" + tdm.toString();
    }

    public void store() throws IOException {
        FileOutputStream WritenFile = new FileOutputStream("./" + this.nom + ".tds");
        ObjectOutputStream oos = new ObjectOutputStream(WritenFile);
        oos.writeObject(this);
        oos.close();
        WritenFile.close();
    }

    public Info_Classe load() {
        try {
            if (InfosGlobales.isSecondePasse()) return Info_Classe.load(this.nom); else return this;
        } catch (FileNotFoundException e) {
            return this;
        }
    }

    public static Info_Classe load(String nom) throws FileNotFoundException {
        Info_Classe classe = new Info_Classe(nom, 0);
        FileInputStream readFile;
        readFile = new FileInputStream("./" + nom + ".tds");
        try {
            ObjectInputStream ois = new ObjectInputStream(readFile);
            Object obj = ois.readObject();
            classe = (Info_Classe) obj;
            ois.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classe;
    }

    public String getSuperMethode(String nom) {
        return nom.split(".")[1];
    }

    public Info_Methode[] getAllConstructors() {
        return (Info_Methode[]) constructeurs.getAllMethods();
    }
}
