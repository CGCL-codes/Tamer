package at.jku.semwiq.mediator.dataset;

import at.jku.semwiq.mediator.Mediator;
import com.hp.hpl.jena.rdf.model.impl.ModelCom;

/**
 * @author dorgon, Andreas Langegger, al@jku.at
 *
 */
public class SemWIQVirtualModel extends ModelCom {

    public SemWIQVirtualModel(Mediator m) {
        super(new SemWIQVirtualGraph(m));
    }

    public SemWIQVirtualModel(SemWIQVirtualGraph base) {
        super(base);
    }

    public Mediator getMediator() {
        return ((SemWIQVirtualGraph) graph).getMediator();
    }
}
