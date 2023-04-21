package uk.ac.man.cs.mig.coode.owlviz.ui.popup;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 23, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class BrowserTextPopupPage extends AbstractOWLInstanceStringPopupPage {

    public BrowserTextPopupPage(OWLModel kb) {
        super("Browser Text", 20, kb);
    }

    public String getDisplayText(RDFResource instance) {
        return instance.getBrowserText();
    }
}
