package it.battlehorse.scripting.registry;

/**
 * The scripting registry keeps a collection of the available scripts
 * 
 * @author battlehorse
 *
 */
public interface IScriptingRegistry {

    /**
	 * Returns the script associated with the given id, or {@code null} if no script is found. 
	 * 
	 * @param id the requested id
	 * @return the associated script
	 */
    public IScript getScriptFromId(String id);

    /**
	 * Returns the scriptObject associated with the given id, or {@code null} if no scriptObject is found. 
	 * 
	 * @param id the requested id
	 * @return the associated scriptObject
	 */
    public Object getScriptObject(String id);
}
