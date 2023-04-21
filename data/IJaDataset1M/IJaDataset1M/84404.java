package domain;

/**
 * Resultado obtido pela an�lise de um conjunto de medidas. Encapsula os valores
 * das medidas.
 * 
 * @author <a href="http://spycorp.org">SpyCorp</a>
 * @version 1.0
 * @see domain.Measurement
 */
public class Result {

    /**
	 * Chave referente ao resultado. Hora em an�lises di�rias, dia da semana em
	 * an�lises semanais, etc.
	 */
    private int key;

    /**
	 * Quantidade de a��es em teclado durante o per�odo.
	 */
    private long keyPressed;

    /**
	 * Quantidade de a��es em mouse durante o per�odo.
	 */
    private long mousePressed;

    /**
	 * Quantidade de minutos em que foi feita a coleta de dados.
	 */
    private long minutes;

    /**
	 * @return Chave referente ao resultado
	 */
    public int getKey() {
        return key;
    }

    /**
	 * @param key
	 *            Chave referente ao resultado
	 */
    public void setKey(int key) {
        this.key = key;
    }

    /**
	 * @return Quantidade de a��es em teclado durante o per�odo.
	 */
    public long getKeyPressed() {
        return keyPressed;
    }

    /**
	 * @param keyPressed
	 *            Quantidade de a��es em teclado durante o per�odo.
	 */
    public void setKeyPressed(long keyPressed) {
        this.keyPressed = keyPressed;
    }

    /**
	 * @return Quantidade de a��es em mouse durante o per�odo.
	 */
    public long getMousePressed() {
        return mousePressed;
    }

    /**
	 * @param mousePressed
	 *            Quantidade de a��es em mouse durante o per�odo.
	 */
    public void setMousePressed(long mousePressed) {
        this.mousePressed = mousePressed;
    }

    /**
	 * @return Quantidade de minutos em que foi feita a coleta de dados.
	 */
    public long getMinutes() {
        return minutes;
    }

    /**
	 * @param minutes
	 *            Quantidade de minutos em que foi feita a coleta de dados.
	 */
    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    public String toString() {
        return "Result: key=" + getKey() + ", minutes=" + getMinutes();
    }
}
