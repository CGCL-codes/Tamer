package clp.core;

import java.util.ArrayList;
import clp.metadata.MethodArgumentMetadata;

public class ModifiedArguments {

    private ArrayList<MethodArgumentMetadata> deletedArguments = new ArrayList<MethodArgumentMetadata>();

    private ArrayList<MethodArgumentMetadata> addedArguments = new ArrayList<MethodArgumentMetadata>();

    /**
	 * @return Returns the addedArguments.
	 */
    public ArrayList<MethodArgumentMetadata> getAddedArguments() {
        return addedArguments;
    }

    /**
	 * @param addedArguments The addedArguments to set.
	 */
    public void setAddedArguments(ArrayList<MethodArgumentMetadata> addedArguments) {
        this.addedArguments = addedArguments;
    }

    /**
	 * @return Returns the deletedArguments.
	 */
    public ArrayList<MethodArgumentMetadata> getDeletedArguments() {
        return deletedArguments;
    }

    /**
	 * @param deletedArguments The deletedArguments to set.
	 */
    public void setDeletedArguments(ArrayList<MethodArgumentMetadata> deletedArguments) {
        this.deletedArguments = deletedArguments;
    }
}
