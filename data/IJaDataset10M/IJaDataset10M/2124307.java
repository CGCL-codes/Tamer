/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.wat.main.charts;

/**
 *
 * @author Melerek
 */
public class PieDatasetInput {
    String nazwa;
    Float warto��;

    /**
     * Klasa obiektu zmiennej do diagramu ko�owego
     * @param Title - nazwa badanej zmiennej
     * @param value - warto�� zmiennej
     */

    public PieDatasetInput(String Title, Float value)
    {
        this.nazwa = Title;
        this.warto�� = value;
    }
}
