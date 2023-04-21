package com.monad.homerun.pkg.x10;

import java.util.Properties;
import java.util.Map;
import java.io.IOException;
import com.monad.homerun.control.Control;
import com.monad.homerun.core.GlobalProps;
import com.monad.homerun.objmgt.ControlRuntime;
import com.monad.homerun.objmgt.RuntimeContext;
import com.monad.homerun.objmgt.impl.ControlBase;

/**
 * X10Actuator represents any simple X10 actuator, which category 
 * encompasses several families of X10 devices:
 * (1) 'appliance' modules, including models: AM486, AM14A etc.
 * (2) 'transceiver' modules, which double as appliance modules: TM751, etc.
 * (3) 'lamp' modules, including models: AM486, AM14A etc.
 * These all share the characteristics that they have a single house code
 * and device code, and respond to a small set of X10 commands.
 */
public class X10Actuator extends X10Device {

    private boolean twoWay = false;

    private boolean transceiver = false;

    public X10Actuator() {
        super();
    }

    public void init(Properties props, RuntimeContext context) {
        super.init(props, context);
        String twoWayStr = props.getProperty("twoWay");
        if (twoWayStr != null && twoWayStr.length() > 0) {
            twoWay = twoWayStr.equalsIgnoreCase("true");
        }
        String transStr = props.getProperty("transceiver");
        if (transStr != null && transStr.length() > 0) {
            transceiver = transStr.equalsIgnoreCase("true");
        }
        start();
    }

    public boolean isTwoWay() {
        return twoWay;
    }

    public boolean isTransceiver() {
        return transceiver;
    }

    public void onEvent(String address, Object event) {
    }

    public ControlRuntime getControlRuntime(Control control, Properties bindingProps) {
        ControlRuntime ctrlRt = null;
        String ctrlName = control.getControlName();
        if ("binary-switch".equals(ctrlName)) {
            ctrlRt = new X10SwitchControl(control);
        } else if ("dimmer".equals(ctrlName)) {
            ctrlRt = new X10DimmerControl(control);
        } else if (GlobalProps.DEBUG) {
            System.out.println("getControlRuntime control: " + ctrlName + " in X10Actuator - failed");
        }
        return ctrlRt;
    }

    private class X10SwitchControl extends ControlBase {

        public X10SwitchControl(Control control) {
            super(control);
        }

        public boolean controlAction(String code, Map<String, Object> parameters) throws IOException {
            return handler.processCommand(X10Actuator.this.getName(), code, parameters);
        }
    }

    private class X10DimmerControl extends ControlBase {

        public X10DimmerControl(Control control) {
            super(control);
        }

        public boolean controlAction(String code, Map<String, Object> parameters) throws IOException {
            String lvlStr = (String) parameters.get("level");
            if (lvlStr == null || lvlStr.length() == 0) {
                lvlStr = "5";
            }
            int level = Integer.parseInt(lvlStr);
            parameters.put("count", new Integer(level / 5));
            return handler.processCommand(X10Actuator.this.getName(), code, parameters);
        }
    }
}
