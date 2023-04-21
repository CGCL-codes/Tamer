package framework.libraries;

import framework.GroupCommEventArgs;

/**
* Interface allowing event triggering. Any protocol defined as <i>common code</i>
* must accept a parameter of this type in its constructor, and will can its only 
* method whenever it needs to trigger an event.
*/
public interface Trigger {

    /**
	* Triggers an event of type <i>type</i>. The argument of type GroupCommEventArgs will be 
	* conveyed with the event and delivered to the receiving handler.
	*  
	* The <i>type</i> is defined in Constants {@link framework.Constants}
	*/
    public void trigger(int type, GroupCommEventArgs args);
}
