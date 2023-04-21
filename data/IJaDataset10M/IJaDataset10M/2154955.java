package mySBML;

import java.util.ArrayList;
import mySBML.primitiveTypes.SId;
import mySBML.utilities.SbmlProblem;

public abstract class SimpleSpeciesReference extends SIdElement {

    protected SId speciesID = new SId(this);

    public static final String ERROR_MSG_21111 = "The value of a SpeciesReference species attribute must be the identiﬁer of an existing Species in the model";

    @Override
    public Model getModel() {
        if (getParent() == null) return null;
        return getParent().getModel();
    }

    public SId getSpeciesID() {
        return speciesID;
    }

    public void setSpeciesID(String speciesID) {
        if (isRegistered()) getModel().getSidMap().removeReference(this.speciesID);
        this.speciesID.set(speciesID);
        if (isRegistered()) getModel().getSidMap().addReference(this.speciesID);
    }

    @Override
    public ArrayList<SbmlProblem> getSbmlProblems() {
        worstProblem = 0;
        ArrayList<SbmlProblem> problems = new ArrayList<SbmlProblem>();
        if (!id.isValid()) problems.add(createError(ERROR_MSG_INVALID_SID, getXPath()));
        if (getModel().getSidMap().duplicateSId(id)) problems.add(createError(ERROR_MSG_DUPLICATE_ID, getXPath()));
        if (checkSbmlError_ClassOfElement(speciesID, Species.class)) problems.add(createError(ERROR_MSG_21111, getXPath()));
        return problems;
    }
}
