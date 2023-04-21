package q10.GUI;

import q10.ArboriFunctionali;

public interface Command {

    /**
	 * 
	 * @return
	 */
    public ArboriFunctionali execute();

    /**
     * 
     * @return
     */
    public ArboriFunctionali unexecute();
}
