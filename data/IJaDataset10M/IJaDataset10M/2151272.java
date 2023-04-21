package examples.pulsar.v01;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import lib.visual.vanilla.IVanilla;
import anima.factory.IGlobalFactory;
import anima.factory.context.componentContext.ComponentContextFactory;

public class AppPulsar01 implements ActionListener {

    private boolean visible = true;

    private IVanilla base;

    private IVisualPulsar visualPulsar;

    public static void main(String[] args) {
        new AppPulsar01();
    }

    public AppPulsar01() {
        try {
            Timer clock = new Timer(1000, this);
            clock.start();
            IGlobalFactory factory = ComponentContextFactory.createGlobalFactory();
            base = (IVanilla) factory.createInstance("http://purl.org/NET/dcc/lib.visual.vanilla.Vanilla");
            visualPulsar = (IVisualPulsar) factory.createInstance("http://purl.org/NET/dcc/examples.pulsar.v01.VisualPulsarComponent");
            visualPulsar.connect(base);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent arg0) {
        visible = !visible;
        if (visible) visualPulsar.showPulsar(); else visualPulsar.hidePulsar();
    }
}
