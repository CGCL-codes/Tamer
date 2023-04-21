package q_impress.pmi.lib.project;

import q_impress.pmi.lib.tasks.AbstractTask;

/**
 * A concrete implementation of a Modeling Project.
 * @author Mauro Luigi Drago
 *
 */
public class ModelingProject extends AbstractResource implements IModelingProject {

    private ILocationSolver locationSolver = new SimpleLocationSolver();

    /**
	 * Constructs a new ModelingProject which uses a SimpleLocationSolver.
	 * During the creation, a unique identifier is automatically generated and assigned to it.
	 */
    public ModelingProject() {
        super();
        try {
            INameManager nameManager = new SimpleNameManager();
            this.structure = new CompositeResourceStructure(this, nameManager);
            this.structure.setParent(this);
            super.setId(this, nameManager.generateName());
        } catch (NamingException e) {
        }
    }

    @Override
    public String getQualifiedId() {
        return this.getId();
    }

    @Override
    public void setId(INamingContext context, String id) throws NamingException {
        throw new NamingException("Modeling projects are naming contexts, they autoassign and id by themselves.");
    }

    @Override
    public ILocationSolver getLocationSolver() {
        return this.locationSolver;
    }

    @Override
    public void setLocationSolver(ILocationSolver locationSolver) {
        this.locationSolver = locationSolver;
    }

    @Override
    public IResource findResourceAbsolute(String absoluteId) {
        if (absoluteId.contains(Identifiable.CONTEXT_SEPARATOR)) {
            String firstPart = absoluteId.substring(0, absoluteId.indexOf(Identifiable.CONTEXT_SEPARATOR));
            if (firstPart.equals(this.getId())) return structure.findResource(absoluteId.substring(firstPart.length() + 1));
        } else {
            if (absoluteId.equals(this.getId())) return this;
        }
        return this.getRoot().findResourceAbsolute(absoluteId);
    }

    @Override
    protected boolean isResourceCompatible(IResource resource) {
        if (super.isResourceCompatible(resource) == false) return false;
        if (resource instanceof AbstractResource) return true;
        if (resource instanceof AbstractTask) return true;
        return false;
    }
}
