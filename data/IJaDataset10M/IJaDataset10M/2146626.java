package net.sf.appomatox.bibliothek.geometrie.planimetrie;

public class Trapez extends Figur {

    private Double m_dA;

    private Double m_dC;

    private Double m_dH;

    private Double m_dM;

    private Double m_dFlaeche;

    /**
	 * Erzeugt eine neue Instanz. Alle Parameter sind optional. Die nicht 
	 * angegebenen Werte werden aus den angegebenen wenn m�glich berechnet.
	 * @param a
	 * @param c
	 * @param h
	 * @param m
	 * @param flaeche
	 */
    public Trapez(Double a, Double c, Double h, Double m, Double flaeche) {
        m_dA = a;
        m_dC = c;
        m_dH = h;
        m_dM = m;
        m_dFlaeche = flaeche;
        check();
    }

    @Override
    protected void doCheck() {
        if (m_dA != null && m_dC != null && m_dM == null) {
            m_dM = (m_dA + m_dC) / 2.0;
        }
        if (m_dM != null && m_dC != null && m_dA == null) {
            m_dA = (2.0 * m_dM) - m_dC;
        }
        if (m_dM != null && m_dA != null && m_dC == null) {
            m_dC = (2.0 * m_dM) - m_dA;
        }
        if (m_dFlaeche != null && m_dH != null && m_dM == null) {
            m_dM = m_dFlaeche / m_dH;
        }
        if (m_dM != null && m_dH != null && m_dFlaeche == null) {
            m_dFlaeche = m_dM * m_dH;
        }
        if (m_dFlaeche != null && m_dM != null && m_dH == null) {
            m_dH = m_dFlaeche / m_dM;
        }
    }

    /**
	 * Liefert die L�nge der Seite a
	 * @return (erw�hnt)
	 */
    public Double getA() {
        return m_dA;
    }

    /**
	 * Liefert die L�nge der Seite c
	 * @return (erw�hnt)
	 */
    public Double getC() {
        return m_dC;
    }

    /**
	 * Liefert die L�nge der H�he
	 * @return (erw�hnt)
	 */
    public Double getH() {
        return m_dH;
    }

    /**
	 * Liefert die L�nge der Mittellinie
	 * @return (erw�hnt)
	 */
    public Double getM() {
        return m_dM;
    }

    /**
	 * Liefert den Fl�cheninhalt in FE
	 * @return (erw�hnt)
	 */
    public Double getFlaeche() {
        return m_dFlaeche;
    }

    @Override
    public boolean isVollstaendigBerechnet() {
        return (m_dA != null && m_dC != null && m_dH != null && m_dM != null && m_dFlaeche != null);
    }
}
