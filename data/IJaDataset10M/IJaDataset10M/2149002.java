package controlers.champs_coeff;

import graphics.champs.Donnees;
import metier.coeff.Coeff;
import metier.coeff.Setter;

/**
 * Faire le lien entre des {@link Donnees} et le {@link Coeff}
 * correspondant.
 * 
 * @author Simon
 * 
 * @param <T> le type des donn�es concern�es par les {@link Donnees} et
 *  le {@link Coeff}
 */
public class LienDonneesCoeff<T> implements Lien {

    private Donnees<T> donnees;

    private Coeff<T> coeff;

    private boolean hasChangedSinceLastSave = false;

    private T valTemporaire = null;

    /**
	 * Instancie les donn�es avec les valeurs du coeff et cr�e le lien entre les
	 * 2.
	 * 
	 * @param <T>  le type des donn�es g�r�es par le coeff et les donn�es
	 * @param coeff le {@link Coeff} concern� par le lien
	 * @param donnees les {@link Donnees} concern�es par le lien
	 * @return le lien entre coeff et donnees
	 */
    public static <T> LienDonneesCoeff<T> creerLienDonneesCoeff(Donnees<T> donnees, Coeff<T> coeff) {
        try {
            donnees.setValT(coeff.getModif());
        } catch (IllegalArgumentException e) {
        }
        try {
            donnees.setValInitialeT(coeff.getInit());
        } catch (IllegalArgumentException e) {
        }
        return new LienDonneesCoeff<T>(donnees, coeff);
    }

    /**
	 * Cr�er une association entre des {@link Donnees} et un
	 * {@link Coeff}
	 * 
	 * @param donnees les {@link Donnees} faisant parti du lien
	 * @param coeff le {@link Coeff} faisant parti du lien
	 */
    private LienDonneesCoeff(Donnees<T> donnees, Coeff<T> coeff) {
        this.donnees = donnees;
        this.coeff = coeff;
        donnees.addValueChangeListener(this);
        valueChange();
        donnees.valueChange();
    }

    @Override
    public void annulerLien() {
        donnees.setCoherence(true);
        donnees.removeValueChangeListener(this);
    }

    @Override
    public boolean prepareSave() {
        boolean valideEtCoherent = donnees.isValideEtCoherent();
        if (valideEtCoherent) {
            valTemporaire = donnees.getValT();
        } else {
            valTemporaire = null;
        }
        return valideEtCoherent;
    }

    @Override
    public void confirmSave() {
        Setter.setModif(coeff, valTemporaire);
        hasChangedSinceLastSave = false;
    }

    @Override
    public boolean hasChangedSinceLastSave() {
        return hasChangedSinceLastSave;
    }

    @Override
    public void valueChange() {
        T enteredValue = null;
        try {
            enteredValue = donnees.getValT();
            if (enteredValue != null) {
                donnees.setValidite(true);
                donnees.setCoherence(coeff.isCorrect(enteredValue), coeff.getMsgIncoherent());
                Setter.setModif(coeff, enteredValue);
            } else {
                donnees.setValidite(false);
            }
        } catch (IllegalArgumentException e) {
            donnees.setValidite(false);
        }
        if (donnees.isValideEtCoherent()) {
            hasChangedSinceLastSave = !enteredValue.equals(coeff.getModif());
        }
    }

    @Override
    public void read() {
        try {
            donnees.setValT(coeff.getModif());
        } catch (IllegalArgumentException e) {
        }
        try {
            donnees.setValInitialeT(coeff.getInit());
        } catch (IllegalArgumentException e) {
        }
    }
}
