package eu.redseeds.scl.model.impl.rsl.rsldomainelements.terms;

import de.uni_koblenz.jgralab.Graph;
import eu.redseeds.scl.impl.rsl.rsldomainelements.terms.PrepositionImpl;
import eu.redseeds.scl.model.rsl.rsldomainelements.terms.PrepositionDTO;

public class PrepositionDTOImpl extends PrepositionImpl implements PrepositionDTO {

    public PrepositionDTOImpl(int arg0, Graph arg1) {
        super(arg0, arg1);
    }

    @Override
    public void setName(String name) {
        super.setName(name.trim());
    }

    @Override
    public String toString() {
        return super.getName() == null ? "" : super.getName();
    }
}
