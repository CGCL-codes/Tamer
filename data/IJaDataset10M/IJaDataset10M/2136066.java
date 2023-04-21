package util;

import javax.swing.JTextField;

/**
 *
 * @author Laura
 */
public class Pos {

    private double art1;

    private double art2;

    private double tornillo;

    public Pos(double art1, double art2, double tornillo) {
        this.art1 = art1;
        this.art2 = art2;
        this.tornillo = tornillo;
    }

    public Pos(JTextField textCuanto1, JTextField textCuanto2, JTextField textDistancia) {
        this.art1 = Double.parseDouble(textCuanto1.getText());
        this.art2 = Double.parseDouble(textCuanto2.getText());
        this.tornillo = Double.parseDouble(textDistancia.getText());
    }

    public double getArt1() {
        return art1;
    }

    public void setArt1(double art1) {
        this.art1 = art1;
    }

    public double getArt2() {
        return art2;
    }

    public void setArt2(double art2) {
        this.art2 = art2;
    }

    public double getTornillo() {
        return tornillo;
    }

    public void setTornillo(double tornillo) {
        this.tornillo = tornillo;
    }
}
