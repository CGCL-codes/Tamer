package metier;

import java.util.Comparator;
import java.util.List;
import metier.coeff.CoeffString;
import metier.coeff.Setter;

/**
 * Classe repr�sentant un mod�le d'a�ronef tel que d�crit par l'ensemble
 * des fichiers de synonymes.
 * 
 * @author Pierre
 *
 * @see Bada User Manual, section 6.3
 */
public class Aircraft {

    private CoeffString code = new CoeffString(4);

    private boolean directlySupported;

    private CoeffString fileName = new CoeffString(6, 6);

    private boolean isInitialBada;

    private CoeffString manufacturer = new CoeffString(18);

    private CoeffString name = new CoeffString(25);

    private List<VersionCode> codesVersions;

    /**
	 * Constructeur � partir des �l�ments essentiels (non calculables)
	 * 	pour la cr�ation d'un nouvel a�ronef (non initial BADA donc)
	 * @param code le code OACI de l'a�ronef
	 * @param directlySupported si l'a�ronef est synonyme ou non d'un a�ronef existant
	 * @param fileName le nom du fichier associ�
	 * @param manufacturer le fabriquant de l'appareil
	 * @param name le nom de l'a�ronef
	 */
    public Aircraft(String code, boolean directlySupported, String name, String manufacturer, String fileName) {
        this.setCode(code);
        this.setDirectlySupported(directlySupported);
        this.setFileName(fileName);
        this.setInitialBada(false);
        this.setManufacturer(manufacturer);
        this.setName(name);
    }

    /**
	 * Constructeur par d�faut
	 * <p><b>ATTENTION :</b> pensez dans ce cas � appeler toutes les fonctions set
	 * pour sp�cifier correctement un avion
	 */
    public Aircraft() {
    }

    /**
	 * Obtenir le code OACI de l'avion
	 * @return le code OACI de l'avion
	 */
    public CoeffString getCode() {
        return code;
    }

    /**
	 * Obtenir le nom du fichier o� se situe les informations concernant l'avion 
	 * @return le nom du fichier
	 */
    public CoeffString getFileName() {
        return fileName;
    }

    /**
	 * Obtenir le nom du fabriquant de l'avion
	 * @return le nom du fabriquant
	 */
    public CoeffString getManufacturer() {
        return manufacturer;
    }

    /**
	 * Obtenir le nom de l'avion
	 * @return le nom de l'avion
	 */
    public CoeffString getName() {
        return name;
    }

    /**
	 * Obtenir la liste des codes au cours des versions
	 * @return la liste des codes
	 */
    public List<VersionCode> getCodesVersions() {
        return codesVersions;
    }

    /**
	 * Savoir si l'avion est directement support� ou s'il est synonyme 
	 * d'un autre
	 * @return <code>true</code> si l'avion est directement support�
	 */
    public boolean isDirectlySupported() {
        return directlySupported;
    }

    /**
	 * Savoir si l'avion �tait dans la version initiale de BADA
	 * @return <code>true</code> si l'avion �tait dans la version 
	 * initiale de BADA
	 */
    public boolean isInitialBada() {
        return isInitialBada;
    }

    /**
	 * Affecte le code OACI � l'avion
	 * @param code le code OACI de l'avion
	 */
    public void setCode(String code) {
        Setter.set(this.code, code);
    }

    /**
	 * Pr�cise si l'avion est directement support� ou s'il est 
	 * synonyme d'un autre
	 * @param directlySupported <code>true</code> si directement support�
	 */
    public void setDirectlySupported(boolean directlySupported) {
        this.directlySupported = directlySupported;
    }

    /**
	 * Pr�cise le nom du fichier o� se trouve les coefficients de l'avion
	 * @param fileName le nom de l'avion
	 */
    public void setFileName(String fileName) {
        Setter.set(this.fileName, fileName);
    }

    /**
	 * Pr�cise si l'avion �tait pris en charge dans la version initiale 
	 * de BADA
	 * @param isInitialBada <code>true</code> si c'est le cas
	 */
    public void setInitialBada(boolean isInitialBada) {
        this.isInitialBada = isInitialBada;
    }

    /**
	 * Pr�cise le fabriquant de l'avion
	 * @param manufacturer le nom du fabriquant (sa marque)
	 */
    public void setManufacturer(String manufacturer) {
        Setter.set(this.manufacturer, manufacturer);
    }

    /**
	 * Pr�cise le mod�le de l'avion
	 * @param name le mod�le de l'avion
	 */
    public void setName(String name) {
        Setter.set(this.name, name);
    }

    /**
	 * Pr�cise la liste des codes au cours des versions
	 * @param list la liste des codes
	 */
    public void setVersionCode(List<VersionCode> list) {
        codesVersions = list;
    }

    /**
	 * Evalue si 2 a�ronefs sont �gaux
	 * <p>C'est le cas s'ils ont le m�me code
	 * @param ac l'a�ronef dont il faut �valuer l'�galit�
	 * @return true si ac est �gal � l'objet
	 */
    public boolean equals(Aircraft ac) {
        return ac.code == this.code;
    }

    /**
	 * Enum�ration des diff�rents champs pr�sents pour un avion
	 * 
	 * @author Pierre
	 *
	 */
    public enum AircraftFields {

        CODE, MANUFACTURER, NAME, FILE_NAME, ALL
    }

    /**
	 * Classe pour le tri des avions par leur code
	 * 
	 * @author Patrice
	 */
    public static class ComparatorAircraft implements Comparator<Aircraft> {

        @Override
        public int compare(Aircraft ac1, Aircraft ac2) {
            return ac1.code.getInit().compareToIgnoreCase(ac2.code.getInit());
        }
    }
}
