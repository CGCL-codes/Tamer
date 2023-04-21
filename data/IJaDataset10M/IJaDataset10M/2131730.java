package mswing;

/**
 * Champs de saisie d'un nombre d�cimal accompagn� d'un label.
 *
 * @author Emeric Vernat
 */
public class MDoubleLabelledField extends MLabelledField {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructeur.
	 */
    public MDoubleLabelledField() {
        super(new MDoubleField());
    }

    /**
	 * Retourne la valeur de la propri�t� double.
	 *
	 * @return Double
	 * @see #setDouble
	 */
    public Double getDouble() {
        return getDoubleField().getDouble();
    }

    /**
	 * Retourne la valeur de la propri�t� doubleField.
	 *
	 * @return mswing.MDoubleField
	 * @see mswing.MLabelledField#getField
	 */
    public MDoubleField getDoubleField() {
        return (MDoubleField) getField();
    }

    /**
	 * Retourne la valeur de la propri�t� fractionDigits.
	 *
	 * @return int
	 * @see #setFractionDigits
	 */
    public int getFractionDigits() {
        return getDoubleField().getFractionDigits();
    }

    /**
	 * Retourne la valeur de la propri�t� maximumValue.
	 *
	 * @return double
	 * @see #setMaximumValue
	 */
    public double getMaximumValue() {
        return getDoubleField().getMaximumValue();
    }

    /**
	 * Retourne la valeur de la propri�t� minimumValue.
	 *
	 * @return double
	 * @see #setMinimumValue
	 */
    public double getMinimumValue() {
        return getDoubleField().getMinimumValue();
    }

    /**
	 * D�finit la valeur de la propri�t� double. <BR>
	 * La d�finition de cette propri�t� lance un �v�nement PropertyChange ("double").
	 *
	 * @param newDouble Double
	 * @see #getDouble
	 */
    public void setDouble(Double newDouble) {
        getDoubleField().setDouble(newDouble);
    }

    /**
	 * D�finit la valeur de la propri�t� fractionDigits.
	 *
	 * @param newFractionDigits int
	 * @see #getFractionDigits
	 */
    public void setFractionDigits(int newFractionDigits) {
        getDoubleField().setFractionDigits(newFractionDigits);
    }

    /**
	 * D�finit la valeur de la propri�t� maximumValue.
	 *
	 * @param newMaximumValue double
	 * @see #getMaximumValue
	 */
    public void setMaximumValue(double newMaximumValue) {
        getDoubleField().setMaximumValue(newMaximumValue);
    }

    /**
	 * D�finit la valeur de la propri�t� minimumValue.
	 *
	 * @param newMinimumValue double
	 * @see #getMinimumValue
	 */
    public void setMinimumValue(double newMinimumValue) {
        getDoubleField().setMinimumValue(newMinimumValue);
    }
}
