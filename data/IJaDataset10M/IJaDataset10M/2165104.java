package skycastle.sound;

import org.apache.commons.beanutils.*;
import skycastle.utils.linkedbeans.LinkedBeansContainer;
import skycastle.utils.linkedbeans.LinkedBeansContainerImpl;

/**
 * A module containing various signal generators, connected together.
 * <p/>
 * Can have arbitrary inputs, and one signal output plus arbitrary other outputs.
 *
 * @author Hans H�ggstr�m
 */
public class SignalModule implements Signal {

    private LinkedBeansContainer mySignalContainer = new LinkedBeansContainerImpl();

    private DynaClass myInputType = new BasicDynaClass("Input", null);

    private DynaClass myOutputType = new BasicDynaClass("Output", null, new DynaProperty[] { new DynaProperty("outputSignal", Signal.class) });

    private DynaBean myInputBean;

    private DynaBean myOutputBean;

    public SignalModule() {
        myInputBean = new BasicDynaBean(myInputType);
        myOutputBean = new BasicDynaBean(myOutputType);
        mySignalContainer.addBean(myInputBean);
        mySignalContainer.addBean(myOutputBean);
    }

    public void update(float timeStep_s) {
        for (Object bean : mySignalContainer.getBeans()) {
            if (bean instanceof Signal) {
                Signal signalBean = (Signal) bean;
                signalBean.update(timeStep_s);
            }
        }
    }

    public float getCurrentValue() {
        final Signal signal = (Signal) myOutputBean.get("outputSignal");
        return signal.getCurrentValue();
    }

    public DynaBean getInputBean() {
        return myInputBean;
    }

    public DynaBean getOutputBean() {
        return myOutputBean;
    }

    public LinkedBeansContainer getSignalContainer() {
        return mySignalContainer;
    }
}
