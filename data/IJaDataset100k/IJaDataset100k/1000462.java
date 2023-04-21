package cs.pancava.caltha;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

/**
 * <p><b>Trida popisujici vlastni vzhled - tema Metal.</b></p>
 *
 * @author Milan Vaclavik<br />
 * @version $Revision: 6 $<br />
 * $LastChangedBy: mihlon $
 */
public class MyTheme extends DefaultMetalTheme {

    /**
     * Prvni primarni barvu grafickeho tematu.
     */
    private final ColorUIResource primary1 = new ColorUIResource(255, 255, 0);

    /**
     * Druha primarni barvu grafickeho tematu.
     */
    private final ColorUIResource primary2 = new ColorUIResource(0, 255, 255);

    /**
     * Treti primarni barvu grafickeho tematu.
     */
    private final ColorUIResource primary3 = new ColorUIResource(255, 0, 255);

    /**
     * Prazdny konstruktor. :)
     */
    public MyTheme() {
    }

    /**
     * Vrati nazet grafickeho tematu.
     * @return String : Nazev grafickeho tematu.
     */
    @Override
    public final String getName() {
        return "MyTheme";
    }

    /**
     * Vrati prvni primarni barvu grafickeho tematu.
     * @return ColorUIResource : Prvni primarni barva temtu.
     */
    @Override
    protected final ColorUIResource getPrimary1() {
        return this.primary1;
    }

    /**
     * Vrati druhou primarni barvu grafickeho tematu.
     * @return ColorUIResource : Druhou primarni barva temtu.
     */
    @Override
    protected final ColorUIResource getPrimary2() {
        return this.primary2;
    }

    /**
     * Vrati treti primarni barvu grafickeho tematu.
     * @return ColorUIResource : Treti primarni barva temtu.
     */
    @Override
    protected final ColorUIResource getPrimary3() {
        return this.primary3;
    }
}
