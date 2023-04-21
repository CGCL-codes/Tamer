package pierre.api;

import java.util.Arrays;
import java.util.ArrayList;
import pierre.system.BrowserServiceResources;
import pedro.system.PedroFormContext;
import pedro.soa.ontology.provenance.OntologyTermMetaData;
import pedro.soa.ontology.views.*;
import pedro.soa.ontology.sources.*;
import pedro.util.DisplayNameList;

public class OntologyServiceValues extends DataEntryOption {

    private PedroFormContext pedroFormContext;

    private OntologyService[] ontologyServices;

    private DisplayNameList ontologyServiceList;

    public OntologyServiceValues(PedroFormContext pedroFormContext, OntologyService[] ontologyServices) {
        this.pedroFormContext = pedroFormContext;
        this.ontologyServices = ontologyServices;
        ontologyServiceList = new DisplayNameList();
        for (int i = 0; i < ontologyServices.length; i++) {
            OntologySource ontologySource = ontologyServices[i].getSource();
            if (ontologySource == null) {
                OntologyViewer ontologyViewer = ontologyServices[i].getViewer();
                if (ontologyViewer.isWorking() == true) {
                    ontologyViewer.setOntologySource(pedroFormContext, ontologySource);
                }
            } else {
                if (ontologySource.isWorking() == true) {
                    ontologyServiceList.addItem(ontologyServices[i].getName(), ontologyServices[i]);
                }
            }
        }
        ontologyServiceList.sort();
        String name = BrowserServiceResources.getMessage("pierreService.api.dataEntryOptions.ontologyServices");
        setName(name);
    }

    /**
	* @return a list containing the names for a collection of
	* ontology services.
	*/
    public DisplayNameList getOntologyServiceList() {
        return ontologyServiceList;
    }

    /**
   * a utility method for extracting term labels from a given ontology source
   *
   * @param ontologySource an ontology source that the OntologyServiceValues
   * class can use to extract terms
   * @return a sorted array of strings representing the labels of ontology
   * terms provided by ontology sources.
   */
    public String[] getOntologyTerms(OntologySource ontologySource) {
        ArrayList wordPhrases = new ArrayList();
        try {
            OntologyTerm[] terms2 = ontologySource.getTerms(pedroFormContext);
        } catch (Exception err) {
            err.printStackTrace(System.out);
        }
        OntologyTerm[] terms = ontologySource.getTerms(pedroFormContext);
        for (int i = 0; i < terms.length; i++) {
            String label = terms[i].getLabel();
            if (wordPhrases.contains(label) == false) {
                wordPhrases.add(label);
            }
        }
        String[] results = (String[]) wordPhrases.toArray(new String[0]);
        Arrays.sort(results);
        return results;
    }
}
