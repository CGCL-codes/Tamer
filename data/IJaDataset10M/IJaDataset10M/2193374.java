package data;

import gui.GUI;

/**
 * @author ciru
 *
 */
public class Brazo extends Nodo {

    private static String conectaBrazo = new String("08e6");

    private static String extremoSuperior = new String("0832");

    private static String extremoInferior = new String("0835");

    private static String arribaIncremental = new String("0825");

    private static String abajoIncremental = new String("0826");

    private static String modoSeguidorOn = new String("0827");

    private static String modoSeguidorOff = new String("0828");

    public Brazo() {
        super();
    }

    /**
	 * @param gui2
	 */
    public Brazo(GUI gui2) {
        super.gui(gui2);
    }

    /**
	 * @param dirAux
	 */
    public Brazo(int[] dirAux) {
        super(dirAux);
    }

    public int[] getDireccion() {
        return super.getAddress();
    }

    public String getAbajoIncremental() {
        return abajoIncremental;
    }

    public String getArribaIncremental() {
        return arribaIncremental;
    }

    public String getConectaBrazo() {
        return conectaBrazo;
    }

    public String getExtremoInferior() {
        return extremoInferior;
    }

    public String getExtremoSuperior() {
        return extremoSuperior;
    }

    public String getModoSeguidorOff() {
        return modoSeguidorOff;
    }

    public String getModoSeguidorOn() {
        return modoSeguidorOn;
    }
}
