package Neuro;

import java.util.Vector;
import java.util.Enumeration;

/**
 * A neuron with it's input. This class is private within the package.
 * @see NeuronalNetwork
 */
class Neuron extends NeuronalElement {

    private int number = 0;

    private Vector inputs = null;

    private Vector signums = null;

    public Neuron(int number) throws NeuroException {
        if (number < 1) throw new NeuroException("neuron number must be >= 1");
        this.number = number;
        inputs = new Vector();
        signums = new Vector();
    }

    public void addSubelement(NeuronalElement el, int signum) throws NeuroException {
        if (!(signum == 1 || signum == -1)) throw new NeuroException("signum must be -1 or 1");
        inputs.add(el);
        signums.add(new Integer(signum));
    }

    public int value() throws NeuroException {
        int ret = 0;
        if (inputs.isEmpty()) throw new NeuroException("neuron " + number + " has no input");
        Enumeration i = inputs.elements();
        Enumeration s = signums.elements();
        while (i.hasMoreElements()) {
            ret += ((NeuronalElement) i.nextElement()).value() * ((Integer) s.nextElement()).intValue();
        }
        return (ret >= 1) ? 1 : 0;
    }
}
