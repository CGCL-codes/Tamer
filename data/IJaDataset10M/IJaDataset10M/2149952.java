package uk.icat3.parametersearch.exception;

import uk.icat3.exceptions.DatevalueException;
import uk.icat3.exceptions.NoSearchableParameterException;
import uk.icat3.exceptions.NullParameterException;
import uk.icat3.exceptions.CyclicException;
import uk.icat3.exceptions.NoNumericComparatorException;
import java.util.ArrayList;
import java.util.List;
import junit.framework.JUnit4TestAdapter;
import org.junit.Test;
import uk.icat3.entity.Datafile;
import uk.icat3.entity.Parameter;
import uk.icat3.entity.ParameterPK;
import static org.junit.Assert.*;
import uk.icat3.exceptions.NumericvalueException;
import uk.icat3.exceptions.ParameterNoExistsException;
import uk.icat3.parametersearch.BaseParameterSearchTest;
import uk.icat3.search.parameter.ParameterComparisonCondition;
import uk.icat3.search.parameter.ParameterLogicalCondition;
import uk.icat3.search.parameter.ParameterType;
import uk.icat3.search.parameter.ComparisonOperator;
import uk.icat3.search.parameter.util.ParameterSearch;
import uk.icat3.search.DatafileSearch;
import uk.icat3.util.DatafileInclude;
import uk.icat3.util.LogicalOperator;
import uk.icat3.util.Queries;

/**
 * Exception tests for Datafile searchs
 * 
 * @author cruzcruz
 */
public class DatafileExceptionTest extends BaseParameterSearchTest {

    /**
     * Parameter search not searchable
     */
    @Test
    public void noSearchableExceptionTest() {
        ParameterSearch pv3 = new ParameterSearch(ParameterType.DATAFILE, parameter.get("datafile1"));
        try {
            List<ParameterSearch> lp = new ArrayList<ParameterSearch>();
            ParameterSearch pv4 = new ParameterSearch(ParameterType.DATAFILE, parameter.get("datafile2"));
            pv3.getParam().setSearchable("N");
            lp.add(pv3);
            lp.add(pv4);
            DatafileSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, Queries.NO_RESTRICTION, DatafileInclude.NONE, 1, -1, em);
        } catch (Throwable ex) {
            assertEquals("Exception unexpected. ", NoSearchableParameterException.class, ex.getClass());
            return;
        } finally {
            pv3.getParam().setSearchable("Y");
        }
        assertTrue("NoSearchableParameterException expected.", false);
    }

    /**
     * The parameter contains a numberic value but the comparator is for a string.
     */
    @Test
    public void comparatorExceptionTest() {
        try {
            List<ParameterComparisonCondition> lc = new ArrayList<ParameterComparisonCondition>();
            ParameterComparisonCondition comp1 = new ParameterComparisonCondition();
            comp1.setParameterSearch(new ParameterSearch(ParameterType.DATAFILE, parameter.get("datafile1")));
            comp1.setComparator(ComparisonOperator.STARTS_WITH);
            comp1.setValue(new Double(3.14));
            lc.add(comp1);
            DatafileSearch.searchByParameterComparisonList(VALID_USER_FOR_INVESTIGATION, lc, Queries.NO_RESTRICTION, DatafileInclude.NONE, -1, -1, em);
        } catch (Throwable ex) {
            assertEquals("Exception unexpected. ", NoNumericComparatorException.class, ex.getClass());
            return;
        }
        assertTrue("NoNumericComparatorException expected.", false);
    }

    /**
     * Add the operator itself produces an cyclic execption.
     */
    @Test
    public void cyclicExceptionTest() {
        try {
            ParameterLogicalCondition op1 = new ParameterLogicalCondition(LogicalOperator.OR);
            ParameterLogicalCondition op2 = new ParameterLogicalCondition(LogicalOperator.OR);
            ParameterLogicalCondition op3 = new ParameterLogicalCondition(LogicalOperator.OR);
            ParameterLogicalCondition op4 = new ParameterLogicalCondition(LogicalOperator.OR);
            op2.add(pcDatafile.get(0));
            op2.add(pcDatafile.get(1));
            op4.add(pcDataset.get(0));
            op1.add(pcSample.get(0));
            op1.add(op2);
            op2.add(op3);
            op3.add(pcDataset.get(0));
            op3.add(op4);
            op4.add(op3);
            DatafileSearch.searchByParameterCondition(VALID_USER_FOR_INVESTIGATION, op1, Queries.NO_RESTRICTION, DatafileInclude.NONE, 1, -1, em);
        } catch (Throwable ex) {
            assertEquals("Exception unexpected. ", CyclicException.class, ex.getClass());
            return;
        }
        assertTrue("CyclicException expected.", false);
    }

    /**
     * Add the operator itself produces an cyclic execption.
     */
    @Test
    public void cyclicExceptionTest2() {
        try {
            ParameterLogicalCondition op1 = new ParameterLogicalCondition(LogicalOperator.OR);
            ParameterLogicalCondition op2 = new ParameterLogicalCondition(LogicalOperator.OR);
            ParameterLogicalCondition op3 = new ParameterLogicalCondition(LogicalOperator.OR);
            ParameterLogicalCondition op4 = new ParameterLogicalCondition(LogicalOperator.OR);
            op2.add(pcDatafile.get(0));
            op2.add(pcDatafile.get(1));
            op4.add(pcDataset.get(0));
            op1.add(pcSample.get(0));
            op1.add(op2);
            op2.add(op3);
            op3.add(pcDataset.get(0));
            op3.add(op4);
            op4.add(op1);
            DatafileSearch.searchByParameterCondition(VALID_USER_FOR_INVESTIGATION, op1, Queries.NO_RESTRICTION, DatafileInclude.NONE, 1, -1, em);
        } catch (Throwable ex) {
            assertEquals("Exception unexpected. ", CyclicException.class, ex.getClass());
            return;
        }
        assertTrue("CyclicException expected.", false);
    }

    /**
     * Parameter seach is set to NULL
     */
    @Test
    public void nullParameterExceptionTest() {
        try {
            List<ParameterComparisonCondition> lc = new ArrayList<ParameterComparisonCondition>();
            ParameterComparisonCondition comp1 = new ParameterComparisonCondition();
            comp1.setParameterSearch(null);
            comp1.setComparator(ComparisonOperator.EQUALS);
            comp1.setValue(new Double(3.14));
            lc.add(comp1);
            DatafileSearch.searchByParameterComparisonList(VALID_USER_FOR_INVESTIGATION, lc, Queries.NO_RESTRICTION, DatafileInclude.NONE, -1, -1, em);
        } catch (Throwable ex) {
            assertEquals("Exception unexpected. ", NullParameterException.class, ex.getClass());
            return;
        }
        assertTrue("NullParameterException expected.", false);
    }

    /**
     * Parameter is numeric, but value is a String.
     */
    @Test
    public void numericvalueExceptionTest() {
        try {
            List<ParameterComparisonCondition> lc = new ArrayList<ParameterComparisonCondition>();
            ParameterComparisonCondition comp1 = new ParameterComparisonCondition();
            comp1.setParameterSearch(new ParameterSearch(ParameterType.DATAFILE, parameter.get("datafile1")));
            comp1.setComparator(ComparisonOperator.EQUALS);
            comp1.setValue("fail");
            lc.add(comp1);
            DatafileSearch.searchByParameterComparisonList(VALID_USER_FOR_INVESTIGATION, lc, Queries.NO_RESTRICTION, DatafileInclude.NONE, -1, -1, em);
        } catch (Throwable ex) {
            assertEquals("Exception unexpected. ", NumericvalueException.class, ex.getClass());
            return;
        }
        assertTrue("NumericvalueException expected.", false);
    }

    /**
     * Time value is wrong
     */
    @Test
    public void datevalueFormatExceptionTest() {
        try {
            List<ParameterComparisonCondition> lc = new ArrayList<ParameterComparisonCondition>();
            ParameterComparisonCondition comp1 = new ParameterComparisonCondition();
            comp1.setParameterSearch(new ParameterSearch(ParameterType.DATAFILE, parameter.get("time1")));
            comp1.setComparator(ComparisonOperator.EQUALS);
            comp1.setValue("wrong format");
            lc.add(comp1);
            DatafileSearch.searchByParameterComparisonList(VALID_USER_FOR_INVESTIGATION, lc, Queries.NO_RESTRICTION, DatafileInclude.NONE, -1, -1, em);
        } catch (Throwable ex) {
            assertEquals("Exception unexpected. ", DatevalueException.class, ex.getClass());
            return;
        }
        assertTrue("DatevalueException expected.", false);
    }

    /**
     * Time value is wrong
     */
    @Test
    public void datevalueExceptionTest() {
        try {
            List<ParameterComparisonCondition> lc = new ArrayList<ParameterComparisonCondition>();
            ParameterComparisonCondition comp1 = new ParameterComparisonCondition();
            comp1.setParameterSearch(new ParameterSearch(ParameterType.DATAFILE, parameter.get("time1")));
            comp1.setComparator(ComparisonOperator.EQUALS);
            comp1.setValue(new Double(23));
            lc.add(comp1);
            DatafileSearch.searchByParameterComparisonList(VALID_USER_FOR_INVESTIGATION, lc, Queries.NO_RESTRICTION, DatafileInclude.NONE, -1, -1, em);
        } catch (Throwable ex) {
            assertEquals("Exception unexpected. ", DatevalueException.class, ex.getClass());
            return;
        }
        assertTrue("DatevalueException expected.", false);
    }

    /**
     * Parameter is not relevant for the parameter search.
     * Inside PARAMETER table there are 3 columns that indicates if
     * a parameter is relevant for DATAFILE, SAMPLE or INVESTIGATION.
     * These columns are IS_DATAFILE_PARAMETER, IS_SAMPLE_PARAMETER and
     * IS_INVESTIGATION_PARAMETER.
     */
    @Test
    public void noSearchabeException() {
        ParameterSearch pv3 = new ParameterSearch(ParameterType.DATAFILE, parameter.get("datafile1"));
        try {
            List<ParameterSearch> lp = new ArrayList<ParameterSearch>();
            Parameter param = new Parameter(parameter.get("datafile2").getParameterPK());
            ParameterSearch pv4 = new ParameterSearch(ParameterType.SAMPLE, param);
            lp.add(pv3);
            lp.add(pv4);
            DatafileSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, Queries.NO_RESTRICTION, DatafileInclude.NONE, 1, -1, em);
        } catch (Throwable ex) {
            assertEquals("Exception unexpected. ", NoSearchableParameterException.class, ex.getClass());
            return;
        }
        assertTrue("NoSearchableParameterException expected.", false);
    }

    /**
     * Parameter not exists
     */
    @Test
    public void parameterNoExistsException() {
        ParameterSearch pv3 = new ParameterSearch(ParameterType.DATAFILE, parameter.get("datafile1"));
        try {
            List<ParameterSearch> lp = new ArrayList<ParameterSearch>();
            Parameter param = new Parameter(new ParameterPK("noName", "noUnits"));
            param.setDatafileParameter(true);
            ParameterSearch pv4 = new ParameterSearch(ParameterType.DATAFILE, param);
            lp.add(pv3);
            lp.add(pv4);
            DatafileSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, Queries.NO_RESTRICTION, DatafileInclude.NONE, 1, -1, em);
        } catch (Throwable ex) {
            assertEquals("Exception unexpected. ", ParameterNoExistsException.class, ex.getClass());
            return;
        }
        assertTrue("ParameterNoExistsException expected.", false);
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DatafileExceptionTest.class);
    }
}
