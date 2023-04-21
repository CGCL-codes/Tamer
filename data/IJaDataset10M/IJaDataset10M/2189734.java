package druid.dialogs.er.viewprop.legend;

import java.awt.Dimension;
import javax.swing.JPanel;
import org.dlib.gui.FlexLayout;
import org.dlib.gui.TComboBox;
import org.dlib.gui.TLabel;
import druid.core.AttribSet;
import druid.data.er.Legend;
import druid.util.gui.ChangeSentinel;
import druid.util.gui.ImageFactory;

public class GeneralPanel extends JPanel {

    private TComboBox tcbLocation = new TComboBox();

    public GeneralPanel() {
        FlexLayout flexL = new FlexLayout(2, 1, 4, 4);
        flexL.setColProp(1, FlexLayout.EXPAND);
        setLayout(flexL);
        add("0,0", new TLabel("Location"));
        add("1,0,x", tcbLocation);
        tcbLocation.addItem(ImageFactory.BOX_NONE, Legend.NONE, "None");
        tcbLocation.addItem(ImageFactory.BOX_NW, Legend.NW, "North-West corner");
        tcbLocation.addItem(ImageFactory.BOX_NE, Legend.NE, "North-East corner");
        tcbLocation.addItem(ImageFactory.BOX_SW, Legend.SW, "South-West corner");
        tcbLocation.addItem(ImageFactory.BOX_SE, Legend.SE, "South-East corner");
        tcbLocation.setPreferredSize(new Dimension(100, 24));
        ChangeSentinel sent = ChangeSentinel.getInstance();
        tcbLocation.addItemListener(sent);
    }

    public void refresh(Legend legend) {
        AttribSet as = legend.attrSet;
        tcbLocation.setSelectedKey(as.getString("location"));
    }

    public void store(Legend legend) {
        AttribSet as = legend.attrSet;
        as.setString("location", tcbLocation.getSelectedKey());
    }
}
