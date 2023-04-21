package imagemover;

import javax.swing.JButton;

/**
 *  Description of the Class
 *
 *@author     vassilidzuba
 *@created    24 mars 2001
 */
class TargetButton extends JButton {

    private Target _target;

    /**
	 *  Constructor for the TargetButton object
	 *
	 *@param  name  Description of Parameter
	 */
    public TargetButton(String name) {
        super(name);
    }

    /**
	 *  Sets the Target attribute of the TargetButton object
	 *
	 *@param  target  The new Target value
	 */
    public void setTarget(Target target) {
        _target = target;
    }

    /**
	 *  Gets the Target attribute of the TargetButton object
	 *
	 *@return    The Target value
	 */
    public Target getTarget() {
        return _target;
    }
}
