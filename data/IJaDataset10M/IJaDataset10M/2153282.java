package com.google.code.lf.gfm.model.grisbi;

import java.io.File;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.google.code.lf.commons.util.ToStringHelper;
import com.google.code.lf.gfm.model.grisbi.banque.Banques;
import com.google.code.lf.gfm.model.grisbi.categorie.Categories;
import com.google.code.lf.gfm.model.grisbi.compte.Comptes;
import com.google.code.lf.gfm.model.grisbi.devise.Devises;
import com.google.code.lf.gfm.model.grisbi.echeance.Echeances;
import com.google.code.lf.gfm.model.grisbi.etat.Etats;
import com.google.code.lf.gfm.model.grisbi.exercice.Exercices;
import com.google.code.lf.gfm.model.grisbi.imputation.Imputations;
import com.google.code.lf.gfm.model.grisbi.rapprochement.Rapprochements;
import com.google.code.lf.gfm.model.grisbi.tiers.TiersListe;

/**
 * Bean représentant le fichier de données <a href="http://grisbi.org/">Grisbi</a>.
 * <br/>Le binding bean/xml utilise <a href="https://jaxb.dev.java.net/">JAXB</a>.
 * <br/>
 * <br/>Rmq: la modélisation est adaptée à une version de <a href="http://grisbi.org">Grisbi</a> : {@link #GrisbiSupportedVersion}=<code>{@value #GrisbiSupportedVersion}</code>.
 * 
 * @author gael.lorent-fonfrede
 */
@XmlRootElement(name = "Grisbi")
@XmlAccessorType(XmlAccessType.NONE)
@SuppressWarnings("unused")
public class GrisbiFile {

    /** Valeur d'encodage utilisée dans le fichier Grisbi. */
    public static final String GrisbiFileEncoding = "US-ASCII";

    /** Version de Grisbi pour laquelle la modélisation est adaptée. */
    public static final String GrisbiSupportedVersion = "0.5.9";

    /** Chaîne d'indentation utilisée dans les {@link GrisbiFile}. */
    public static String grisbiIndentString = "  ";

    /** Référence du fichier source. Peut-être <code>null</code>. */
    private File fileReference;

    @XmlElement(name = "Generalites")
    private GrisbiFileGeneralites generalites;

    @XmlElement(name = "Comptes")
    private Comptes comptes;

    @XmlElement(name = "Echeances")
    private Echeances echeances;

    @XmlElement(name = "Tiers")
    private TiersListe tiers;

    @XmlElement(name = "Categories")
    private Categories categories;

    @XmlElement(name = "Imputations")
    private Imputations imputations;

    @XmlElement(name = "Devises")
    private Devises devises;

    @XmlElement(name = "Banques")
    private Banques banques;

    @XmlElement(name = "Exercices")
    private Exercices exercices;

    @XmlElement(name = "Rapprochements")
    private Rapprochements rapprochements;

    @XmlElement(name = "Etats")
    private Etats etats;

    @Override
    public String toString() {
        return new StringBuilder("GrisbiFile").append(fileReference != null ? new StringBuilder("(fileReference: ").append(fileReference.getAbsolutePath()).append(")").toString() : "").toString();
    }

    /**
	 * Méthode <code>toString()</code> utilisant la réflection pour détailler l'objet.
	 * <br/>Noter toutefois que tous les beans sous-jacents à celui-ci surchargent la méthode {@link Object#toString()} par cette méthode avec réflexion.
	 * 
	 * @see ToStringHelper#toString()
	 */
    public String toStringWithReflection() {
        return ToStringHelper.toString(this);
    }

    /**
	 * @return {@link #generalites}.
	 * @see GrisbiFileGeneralites.
	 */
    public GrisbiFileGeneralites getGeneralites() {
        return generalites;
    }

    /**
	 * @return {@link #comptes}, jamais <code>null</code>.
	 * @see Comptes.
	 */
    public Comptes getComptes() {
        if (comptes == null) comptes = new Comptes();
        return comptes;
    }

    /**
	 * @param comptes Le {@link #comptes} à modifier.
	 * @see Comptes.
	 */
    public void setComptes(final Comptes comptes) {
        this.comptes = comptes;
    }

    /**
	 * @return {@link #tiers}, jamais <code>null</code>.
	 * @see TiersListe.
	 */
    public TiersListe getTiers() {
        if (tiers == null) tiers = new TiersListe();
        return tiers;
    }

    /**
	 * @param tiers Le {@link #tiers} à modifier.
	 * @see TiersListe.
	 */
    public void setTiers(final TiersListe tiers) {
        this.tiers = tiers;
    }

    /**
	 * @return {@link #categories}, jamais <code>null</code>.
	 * @see Categories.
	 */
    public Categories getCategories() {
        if (categories == null) categories = new Categories();
        return categories;
    }

    /**
	 * @param categories Le {@link #categories} à modifier.
	 * @see Categories.
	 */
    public void setCategories(final Categories categories) {
        this.categories = categories;
    }

    /**
	 * @return {@link #fileReference}.
	 */
    public File getFileReference() {
        return fileReference;
    }

    /**
	 * @param La {@link #fileReference} à modifier.
	 */
    public void setFileReference(final File fileReference) {
        this.fileReference = fileReference;
    }
}
