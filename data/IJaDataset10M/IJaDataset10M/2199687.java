package uk.ac.manchester.mae.evaluation;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owl.model.OWLDescription;
import uk.ac.manchester.mae.ConflictStrategy;

/**
 * @author Luigi Iannone
 * 
 *         The University Of Manchester<br>
 *         Bio-Health Informatics Group<br>
 *         Apr 10, 2008
 */
public class FormulaModel {

    protected URI formulaURI;

    protected ConflictStrategy conflictStrategy = null;

    protected OWLDescription appliesTo = null;

    protected Set<BindingModel> bindings = new HashSet<BindingModel>();

    protected String formulaBody = null;

    protected StorageModel storageModel = null;

    /**
	 * @return the conflictStrategy
	 */
    public ConflictStrategy getConflictStrategy() {
        return this.conflictStrategy;
    }

    /**
	 * @param conflictStrategy
	 *            the conflictStrategy to set
	 */
    public void setConflictStrategy(ConflictStrategy conflictStrategy) {
        this.conflictStrategy = conflictStrategy;
    }

    /**
	 * @return the formulaBody
	 */
    public String getFormulaBody() {
        return this.formulaBody;
    }

    /**
	 * @param formulaBody
	 *            the formulaBody to set
	 */
    public void setFormulaBody(String formulaBody) {
        this.formulaBody = formulaBody;
    }

    /**
	 * @return the bindings
	 */
    public Set<BindingModel> getBindings() {
        return this.bindings;
    }

    /**
	 * @param bindings
	 *            the bindings to set
	 */
    public void setBindings(Set<BindingModel> bindings) {
        this.bindings = bindings;
    }

    public OWLDescription getAppliesTo() {
        return this.appliesTo;
    }

    public void setAppliesTo(OWLDescription appliesTo) {
        this.appliesTo = appliesTo;
    }

    /**
	 * @param storageModel
	 */
    public void setStorageModel(StorageModel storageModel) {
        this.storageModel = storageModel;
    }

    /**
	 * @return the storageModel
	 */
    public StorageModel getStorageModel() {
        return this.storageModel;
    }

    public URI getFormulaURI() {
        return this.formulaURI;
    }

    public void setFormulaURI(URI formulaURI) {
        this.formulaURI = formulaURI;
    }

    public String render(OWLModelManager manager) {
        StringBuilder toReturn = new StringBuilder();
        if (this.conflictStrategy != null) {
            toReturn.append("$");
            toReturn.append(this.conflictStrategy.toString());
            toReturn.append("$ ");
        }
        if (this.appliesTo != null) {
            String rendering = manager.getOWLObjectRenderer().render(this.appliesTo, manager.getOWLEntityRenderer());
            toReturn.append("APPLIESTO <");
            toReturn.append(rendering);
            toReturn.append("> ");
        }
        if (this.storageModel != null) {
            toReturn.append("STORETO <");
            toReturn.append(this.storageModel.getPropertyChainModel().render(manager));
            toReturn.append(">");
        }
        if (!this.bindings.isEmpty()) {
            toReturn.append("{");
            List<BindingModel> listedBindings = new ArrayList<BindingModel>(getBindings());
            for (int i = 0; i < listedBindings.size(); i++) {
                BindingModel bindingModel = listedBindings.get(i);
                toReturn.append(bindingModel.getIdentifier());
                toReturn.append("=");
                PropertyChainModel propertyChainModel = bindingModel.getPropertyChainModel();
                if (propertyChainModel != null) {
                    toReturn.append(propertyChainModel.render(manager));
                }
                if (i < listedBindings.size() - 1) {
                    toReturn.append(",");
                }
            }
            toReturn.append("}->");
        }
        toReturn.append(getFormulaBody());
        return toReturn.toString();
    }
}
