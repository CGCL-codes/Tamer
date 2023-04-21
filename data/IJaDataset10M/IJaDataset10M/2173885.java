package digix.gui;

import digix.model.*;

/**
 * Az IGraphicalUserInterface-t implementalo osztaly
 * @author Andris
 */
public class ViewCreator implements IGraphicalUserInterface {

    /** ebbe pakolja bele a letrehozott nezeteket */
    protected ViewContainer container;

    public ViewContainer GetContainer() {
        return container;
    }

    public ViewCreator(ViewContainer container) {
        this.container = container;
    }

    public void createAndGateView(AndGate andGate) {
        AndGateView newView = new AndGateView(andGate);
        this.container.addView(newView);
    }

    public void createOrGateView(OrGate orGate) {
        OrGateView newView = new OrGateView(orGate);
        this.container.addView(newView);
    }

    public void createInverterView(Inverter inverter) {
        InverterView newView = new InverterView(inverter);
        this.container.addView(newView);
    }

    public void createWireView(Wire wire) {
        WireView newView = new WireView(wire);
        this.container.addView(newView);
    }

    public void createLedView(Led led) {
        LedView newView = new LedView(led);
        this.container.addView(newView);
    }

    public void createOscilloscopeView(Oscilloscope scope) {
        OscilloscopeView newView = new OscilloscopeView(scope);
        this.container.addView(newView);
    }

    public void createCompositOneView(CompositOne comp1) {
        CompositOneView newView = new CompositOneView(comp1);
        this.container.addView(newView);
    }

    public void createCompositeTwoView(CompositTwo comp2) {
        CompositTwoView newView = new CompositTwoView(comp2);
        this.container.addView(newView);
    }

    public void createSwitchView(Switch sw) {
        SwitchView newView = new SwitchView(sw);
        this.container.addView(newView);
    }

    public void createSignalGeneratorView(SignalGenerator sg) {
        SignalGeneratorView newView = new SignalGeneratorView(sg);
        this.container.addView(newView);
    }
}
