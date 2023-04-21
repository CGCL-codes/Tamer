package vademecum.protocol.io;

import java.util.Properties;
import javax.swing.ImageIcon;
import vademecum.protocol.resultitem.ResItem;
import vademecum.protocol.resultitem.ResLabel;
import vademecum.ui.VademecumWindowClickAction;

/**
 * This class collects all information needed to create
 * a ResultItem. It is used in ProtModel#load, but placed
 * here to avoid too much code in ProtModel.
 * 
 *
 */
public class FakeItem {

    private ImageIcon ico;

    private String itemtext;

    private Properties properties;

    public FakeItem() {
        properties = new Properties();
    }

    public void setIco(ImageIcon ico) {
        this.ico = ico;
    }

    public void setItemtext(String itemtext) {
        this.itemtext = itemtext;
    }

    public void addProperty(String key, String value) {
        this.properties.put(key, value);
    }

    public ResItem createItem() {
        ResLabel rl = new ResLabel(ico, itemtext);
        ResItem ri = new ResItem(rl);
        VademecumWindowClickAction vwca = new VademecumWindowClickAction(properties);
        ri.addAction("Open Viewer", vwca);
        ri.setProperties(properties);
        return ri;
    }
}
