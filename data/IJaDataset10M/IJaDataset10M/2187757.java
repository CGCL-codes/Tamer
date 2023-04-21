package eu.redseeds.sc.editor.rsl.editors;

import eu.redseeds.scl.model.rsl.rslrequirements.requirementsspecifications.RequirementDTO;

public interface IRelatedRequirementsViewer {

    /**
	 * Update the view to reflect the fact that a related requirement was added 
	 * to the list
	 */
    public void addRelatedRequirement(RequirementDTO req);

    /**
	 * Update the view to reflect the fact that a related requirement was removed 
	 * from the list
	 */
    public void removeRelatedRequirement(RequirementDTO req);

    /**
	 * Update the view to reflect the fact that one of the related requirements
	 * was modified 
	 */
    public void updateRelatedRequirement(RequirementDTO req);
}
