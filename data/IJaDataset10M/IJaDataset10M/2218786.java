package alice.semantics.data;

import alice.semantics.data.assertion.IndividualDescr;
import alice.semantics.data.template.Concept;
import alice.tuprolog.Struct;

/**
 * represents a Query data refers to an individual ( Result = individual )
 * @author nardinielena
 *
 */
@SuppressWarnings("serial")
public class QueryDataWithInd extends AbstractQueryData {

    private IndividualDescr individual;

    /**
	 * Construct specifying
	 * @param template : query template as concept
	 * @param individual : Reference individual
	 * @param struct : ontological structure
	 */
    public QueryDataWithInd(Concept template, IndividualDescr individual, Struct struct) {
        super(template, struct);
        this.individual = individual;
    }

    /**
	 * Gets the query data individual 
	 * @return
	 */
    public IndividualDescr getIndividual() {
        return individual;
    }

    /**
	 * gets the query data in semantic grammar
	 * @see Coordinazione semantica in Tucson (A.Panzavolta E.Nardini M.Viroli)
	 */
    public String toString() {
        return "semantic " + individual.getName() + " matching " + this.getTemplate();
    }
}
