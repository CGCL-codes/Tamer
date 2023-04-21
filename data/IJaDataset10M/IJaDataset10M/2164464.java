package fr.thomascoffin.mocaf;

/**
 * This class provides an example of RELATIVE X/Y usage,
 * using the EasyGBCFullExample system.
 * <p/>
 * This class is released by Thomas Coffin (thomas.coffin@gmail.com) under the <a href="http://www.gnu.org/copyleft/lesser.html" target="_blank">LGPL License</a>
 * as a component of the <a href="http://code.google.com/p/mocaf" target="_blank">mocaf project</a>
 * <p/>
 * (c) Thomas Coffin 2009.
 */
public class EasyGBCRelativeXYExample extends EasyGBCFullExample {

    protected void fill() {
        EasyGBC easyGBC = new EasyGBC().center().fillBoth().insets(0).noPad().normalWeight().size(1);
        add("X0,Y0", easyGBC.x0y0());
        add("X1,Y3", easyGBC.x(1).y(3));
        add("X3,Y3", easyGBC.x(3).y(3));
        add("XRelative,Y3", easyGBC.relativeX().y(3));
        add("XRelative,YRelative", easyGBC.relativeX().relativeY());
        add("X5,YRelative", easyGBC.x(5).relativeY());
        add("X3,YRelative", easyGBC.x(3).relativeY());
    }

    public static void main(String[] args) {
        new EasyGBCRelativeXYExample();
    }
}
