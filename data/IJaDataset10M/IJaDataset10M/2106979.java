package uk.icat3.parametersearch.investigation;

import uk.icat3.exceptions.NoParameterTypeException;
import uk.icat3.exceptions.NoParametersException;
import uk.icat3.exceptions.ParameterSearchException;
import uk.icat3.search.parameter.util.ParameterSearch;
import java.util.ArrayList;
import java.util.List;
import junit.framework.JUnit4TestAdapter;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.icat3.entity.Investigation;
import uk.icat3.exceptions.RestrictionException;
import uk.icat3.parametersearch.BaseParameterSearchTest;
import uk.icat3.search.parameter.ParameterType;
import uk.icat3.search.InvestigationSearch;
import uk.icat3.util.InvestigationInclude;
import uk.icat3.util.Queries;

/**
 *
 * @author cruzcruz
 */
public class ListParameterTest extends BaseParameterSearchTest {

    @Test
    public void datafileParameterTest() throws ParameterSearchException, RestrictionException {
        List<ParameterSearch> lp = new ArrayList<ParameterSearch>();
        ParameterSearch pv3 = new ParameterSearch(ParameterType.DATAFILE, parameter.get("datafile1"));
        ParameterSearch pv4 = new ParameterSearch(ParameterType.DATAFILE, parameter.get("datafile2"));
        lp.add(pv3);
        lp.add(pv4);
        List<Investigation> li = (List<Investigation>) InvestigationSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, Queries.NO_RESTRICTION, InvestigationInclude.NONE, -1, -1, em);
        showInv(li);
        assertTrue("Results of investigations should not be ZERO", (li.size() == 1));
    }

    @Test
    public void datasetParameterTest() throws ParameterSearchException, RestrictionException {
        List<ParameterSearch> lp = new ArrayList<ParameterSearch>();
        ParameterSearch pv2 = new ParameterSearch(ParameterType.DATASET, parameter.get("dataset1"));
        ParameterSearch pv3 = new ParameterSearch(ParameterType.DATASET, parameter.get("dataset2"));
        lp.add(pv2);
        List<Investigation> li = (List<Investigation>) InvestigationSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, Queries.NO_RESTRICTION, InvestigationInclude.NONE, -1, -1, em);
        showInv(li);
        assertTrue("Results of investigations should not be ZERO", (li.size() == 1));
    }

    @Test
    public void sampleParameterTest() throws ParameterSearchException, RestrictionException {
        List<ParameterSearch> lp = new ArrayList<ParameterSearch>();
        ParameterSearch pv3 = new ParameterSearch(ParameterType.SAMPLE, parameter.get("sample1"));
        lp.add(pv3);
        List<Investigation> li = (List<Investigation>) InvestigationSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, Queries.NO_RESTRICTION, InvestigationInclude.NONE, -1, -1, em);
        showInv(li);
        assertFalse("Results of investigations should not be ZERO", (li.size() == 0));
    }

    @Test
    public void allParameterTest() throws ParameterSearchException, RestrictionException {
        List<ParameterSearch> lp = new ArrayList<ParameterSearch>();
        ParameterSearch pv1 = new ParameterSearch(ParameterType.SAMPLE, parameter.get("sample1"));
        ParameterSearch pv2 = new ParameterSearch(ParameterType.DATAFILE, parameter.get("datafile1"));
        ParameterSearch pv3 = new ParameterSearch(ParameterType.DATASET, parameter.get("dataset1"));
        lp.add(pv1);
        lp.add(pv2);
        lp.add(pv3);
        List<Investigation> li = (List<Investigation>) InvestigationSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, Queries.NO_RESTRICTION, InvestigationInclude.NONE, -1, -1, em);
        showInv(li);
        assertTrue("Results of investigations should not be ZERO", (li.size() == 1));
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ListParameterTest.class);
    }
}
