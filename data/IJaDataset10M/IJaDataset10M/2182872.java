package org.zkoss.zkmob.factory;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import org.zkoss.zkmob.xml.Attributes;
import org.zkoss.zkmob.ZkComponent;
import org.zkoss.zkmob.UiManager;
import org.zkoss.zkmob.ui.ZkDesktop;
import org.zkoss.zkmob.ui.ZkGauge;

/**
 * An UiFactory that create a Gauge Ui component.
 * @author henrichen
 *
 */
public class GaugeFactory extends AbstractUiFactory {

    public GaugeFactory(String name) {
        super(name);
    }

    public ZkComponent create(ZkComponent parent, String tag, Attributes attrs, String hostURL, String pathURL) {
        final String id = attrs.getValue("id");
        final String label = attrs.getValue("lb");
        final String maxValueStr = attrs.getValue("mv");
        final String initialValueStr = attrs.getValue("iv");
        final String interactiveStr = attrs.getValue("it");
        final boolean interactive = "t".equalsIgnoreCase(interactiveStr);
        final int maxValue = Integer.parseInt(maxValueStr);
        final int initialValue = Integer.parseInt(initialValueStr);
        final ZkDesktop zk = ((ZkComponent) parent).getZkDesktop();
        final ZkGauge component = new ZkGauge(zk, id, label, interactive, maxValue, initialValue);
        UiManager.applyItemProperties(parent, component, attrs);
        return component;
    }
}
