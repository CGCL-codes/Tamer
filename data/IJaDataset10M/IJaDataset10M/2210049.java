package net.ideaity.jxmenu;

import net.ideaity.util.Initializable;
import net.ideaity.jxmenu.JxMenuContext;
import net.ideaity.jxmenu.JxMenuFactory;

/**
* Interface for defining a JxMenu.
*
* @author Sloan Seaman (sloan@sgi.net)
* @version .05b
*/
public interface JxMenu extends Initializable {

    /**
	* Execute the JxMenuFactory object and return whatever it created
	*
	* @returns The final object. Usually a JComponent of some type
	*/
    public Object process(JxMenuFactory aFactory);
}
