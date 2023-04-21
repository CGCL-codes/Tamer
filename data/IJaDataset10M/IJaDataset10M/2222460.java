package se.sics.cooja.radiomediums;

import java.util.Collection;
import org.jdom.Element;
import se.sics.cooja.Simulation;
import se.sics.cooja.interfaces.Radio;

public class DGRMDestinationRadio extends DestinationRadio {

    public double ratio = 1.0;

    public double signal = AbstractRadioMedium.SS_STRONG;

    public long delay = 0;

    public DGRMDestinationRadio() {
        super();
    }

    public DGRMDestinationRadio(Radio dest) {
        super(dest);
    }

    protected Object clone() {
        DGRMDestinationRadio clone = new DGRMDestinationRadio(this.radio);
        clone.ratio = this.ratio;
        clone.delay = this.delay;
        clone.signal = this.signal;
        return clone;
    }

    public Collection<Element> getConfigXML() {
        Collection<Element> config = super.getConfigXML();
        Element element;
        element = new Element("ratio");
        element.setText("" + ratio);
        config.add(element);
        element = new Element("signal");
        element.setText("" + signal);
        config.add(element);
        element = new Element("delay");
        element.setText("" + delay);
        config.add(element);
        return config;
    }

    public boolean setConfigXML(final Collection<Element> configXML, Simulation simulation) {
        if (!super.setConfigXML(configXML, simulation)) {
            return false;
        }
        for (Element element : configXML) {
            if (element.getName().equals("ratio")) {
                ratio = Double.parseDouble(element.getText());
            } else if (element.getName().equals("signal")) {
                signal = Double.parseDouble(element.getText());
            } else if (element.getName().equals("delay")) {
                delay = Long.parseLong(element.getText());
            }
        }
        return true;
    }
}
