package edu.tufts.vue.ontology.action;

import tufts.vue.VueResources;
import edu.tufts.vue.ontology.ui.OntologyBrowser;
import edu.tufts.vue.ontology.ui.OntologyChooser2;

public class OntologyOpenAction extends tufts.vue.VueAction {

    private OntologyBrowser browser;

    public OntologyOpenAction(String label, OntologyBrowser browser) {
        super(label);
        this.browser = browser;
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        edu.tufts.vue.ontology.ui.OntologyChooser2 chooser = new OntologyChooser2(tufts.vue.VUE.getDialogParentAsFrame(), VueResources.getString("ontology.openaction"), browser);
    }
}
