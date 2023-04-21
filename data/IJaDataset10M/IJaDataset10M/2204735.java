package model;

public class Girl extends Kid {

    /**
	 * A l�ny �v�d�s csoki-automat�t�l �s a kuty�t�l val� megijed�s��rt felel�s f�ggv�ny
	 * @param intensity az ijeszt�s m�rt�ke
	 * @param type az ijeszt� objektum t�pusa
	 */
    public void disturbed(float intensity, String type) {
        if ((intensity >= 0.5 && type == "ChocolateAutomat") || type == "Dog") {
            release();
        }
    }

    /**
	 * Visszaadja az �v�d�s nem�t
	 */
    public String getSex() {
        return "Girl";
    }
}
