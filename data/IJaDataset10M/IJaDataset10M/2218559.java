package telespravochnik.model;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Описывает запись телефонного справочника.
 */
public class Запись implements Cloneable {

    private int индекс;

    private String имя;

    private String телефон;

    public String дайИмя() {
        return имя;
    }

    public void устИмя(String значение) {
        this.имя = значение;
    }

    /**
     * 
     * @return Индекс записи в аппарате.
     */
    public int дайИндекс() {
        return индекс;
    }

    public void устИндекс(int значение) {
        this.индекс = значение;
    }

    public String дайТелефон() {
        return телефон;
    }

    public void устТелефон(String значение) {
        this.телефон = значение;
    }

    @Override
    public Запись clone() {
        try {
            return (Запись) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Запись.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("индекс=%d, имя=%s, телефон=%s", индекс, имя, телефон);
    }
}
