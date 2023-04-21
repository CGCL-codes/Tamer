package fr.thomascoffin.mocaf;

/**
 * This class provides an example of RELATIVE Width/Height usage,
 * using the EasyGBCFullExample system.
 * <p/>
 * This class is released by Thomas Coffin (thomas.coffin@gmail.com) under the <a href="http://www.gnu.org/copyleft/lesser.html" target="_blank">LGPL License</a>
 * as a component of the <a href="http://code.google.com/p/mocaf" target="_blank">mocaf project</a>
 * <p/>
 * (c) Thomas Coffin 2009.
 */
public class EasyGBCRelativeWidthAndHeightExample extends EasyGBCFullExample {

    protected void fill() {
        EasyGBC easyGBC = new EasyGBC().center().fillBoth().insets(0).noPad().normalWeight().size(1);
        add("X3,Y1,HRelative", easyGBC.x(3).y(1).height(EasyGBC.RELATIVE).width(1));
        add("X1,Y5,WRelative", easyGBC.x(1).y(5).width(EasyGBC.RELATIVE).height(1));
        add("X1,Y6,WRelative", easyGBC.x(1).y(6).width(EasyGBC.RELATIVE).height(1));
        add("X3,Y3", easyGBC.x(3).y(3).size(1));
        add("X4,Y5", easyGBC.x(4).y(5).size(1));
    }

    public static void main(String[] args) {
        new EasyGBCRelativeWidthAndHeightExample();
    }
}
