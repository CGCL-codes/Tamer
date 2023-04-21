package jahuwaldt.tools.NeuralNets;

/**
*   Type of neuron used for network input only (does
*   no calculations, it simply uses the given input
*   value as it's output value).
*
* <p> Modified by:  Joseph A. Huwaldt    </p>
*
*  @author    Joseph A. Huwaldt    Date:  May 18, 1999
*  @version   August 10, 2002
**/
public class InputNeuron extends Neuron {

    /**
	*  Default constructor for InputNeurons.
	**/
    public InputNeuron() {
        needsUpdating = false;
    }

    /**
	*  Constructor for an InputNeuron that takes a value for the output of the
	*  input neuron.
	**/
    public InputNeuron(float inputValue) {
        value = inputValue;
        needsUpdating = false;
    }

    /**
	*  Input neurons can not update themselves, so
	*  this function in the base class is overridden and
	*  does nothing here.
	**/
    public void setUpdate() {
    }
}
