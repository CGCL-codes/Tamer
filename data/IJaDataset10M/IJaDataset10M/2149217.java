package judge.object;

/**
* This interface needs to be implemented by anything that has a publicly
* accesible IContainer implementation.  All this really says is "why yes, 
* I have a container and here's how you get it."
* @see judge.server.commands.look 
*/
public interface IInventory {

    /** 
	* Used to retrieve the IContainer implementation used by
	* this object.
	* @return IContainer A generic Container implementation of some kind.
	*/
    public IContainer getInv();
}
