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
    Float wartoœæ;

    /**
     * Klasa obiektu zmiennej do diagramu ko³owego
     * @param Title - nazwa badanej zmiennej
     * @param value - wartoœæ zmiennej
     */

    public PieDatasetInput(String Title, Float value)
    {
        this.nazwa = Title;
        this.wartoœæ = value;
    }
}
