package math;

/**
* Eine Operation berechnet einen Teil einer Formel.
* Eine typische Operation ist z.B. die Addition "+", sie z�hlt zwei
* Zahlen zusammen.
*/
public interface Operation {

    /**
    * Je h�he die Priorit�t einer Operation ist, desto fr�her wird
    * sie berechnet.
    * @return Die Priorit�t
    */
    public int getPriority();

    /**
    * Gibt das Symbol an, welche diese Operation symbolisieren. F�r eine
    * Addition w�re dies "+".
    * @return Das Symbol, welches mindestens aus einem Buchstaben bestehen muss
    */
    public String getSymbol();

    /**
    * Einige Operationen k�nnen auch als Pr�fix verwendet werden (z.B. "-" wird
    * in der Formel "-2" als Pr�fix verwendet). Dieser boolean gibt an, ob diese
    * Operation als Pr�fix verwendet werden kann.<br>
    * Sollte die Operation als Pr�fix benutzt werden, so wird ihre Methode
    * {@link #calculate(float, float)} aufgerufen, mit <code>a = 0</code>.
    * @return true, falls diese Operation Pr�fix sein kann.
    */
    public boolean asPrefix();

    /**
    * F�hrt die Berechnung durch, welche von dieser Operation dargestellt
    * wird.
    * @param a Das erste Argument
    * @param b Das zweite Argument
    * @return Das Ergebnis dieser Operation
    */
    public float calculate(float a, float b);
}
