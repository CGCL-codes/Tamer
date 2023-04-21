package jimm.twice.ice.xml;

import jimm.twice.util.Logger;

public class Packaging extends Message {

    public Packaging(String style) throws IllegalArgumentException {
        super("icesub:packaging");
        setStyle(style);
    }

    public void setStyle(String style) throws IllegalArgumentException {
        if (!jimm.twice.ice.Packaging.STYLE_ICE.equals(style) && !jimm.twice.ice.Packaging.STYLE_RAW.equals(style) && !jimm.twice.ice.Packaging.STYLE_RSS.equals(style)) {
            String msg = "style must be STYLE_ICE, STYLE_RAW, or STYLE_RSS;" + " it was " + style;
            Logger.instance().log(Logger.ERROR, null, "Packaging.setStyle", msg);
            throw new IllegalArgumentException(msg);
        }
        setAttribute("style", style);
    }
}
