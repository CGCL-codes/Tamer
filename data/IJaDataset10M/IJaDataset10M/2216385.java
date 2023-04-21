package uk.icat3.restriction;

import uk.icat3.parametersearch.*;
import java.util.ArrayList;
import java.util.Collection;
import static org.junit.Assert.*;
import java.util.Date;
import java.util.List;
import junit.framework.JUnit4TestAdapter;
import uk.icat3.exceptions.ParameterSearchException;
import org.junit.Test;
import uk.icat3.entity.Datafile;
import uk.icat3.entity.Dataset;
import uk.icat3.entity.FacilityUser;
import uk.icat3.entity.Investigation;
import uk.icat3.entity.Sample;
import uk.icat3.exceptions.RestrictionException;
import uk.icat3.manager.FacilityManager;
import uk.icat3.restriction.attribute.RestrictionAttributes;
import uk.icat3.search.DatafileSearch;
import uk.icat3.search.DatasetSearch;
import uk.icat3.search.InvestigationSearch;
import uk.icat3.search.SampleSearch;
import uk.icat3.search.parameter.ComparisonOperator;
import uk.icat3.search.parameter.ParameterCondition;
import uk.icat3.search.parameter.ParameterLogicalCondition;
import uk.icat3.search.parameter.ParameterType;
import uk.icat3.search.parameter.util.ParameterSearch;
import uk.icat3.util.DatafileInclude;
import uk.icat3.util.DatasetInclude;
import uk.icat3.util.InvestigationInclude;
import uk.icat3.util.LogicalOperator;
import uk.icat3.util.Queries;
import uk.icat3.util.SampleInclude;

/**
 * This class show some examples of search
 * 
 * @author cruzcruz
 */
public class UsesExamples extends BaseParameterSearchTest {

    @Test
    public void keywords() throws RestrictionException {
        RestrictionLogicalCondition restInstrumentCond = new RestrictionLogicalCondition();
        restInstrumentCond.setOperator(LogicalOperator.AND);
        RestrictionLogicalCondition restCycleCond = new RestrictionLogicalCondition();
        restCycleCond.setOperator(LogicalOperator.AND);
        RestrictionComparisonCondition comparisonInstr;
        comparisonInstr = new RestrictionComparisonCondition();
        comparisonInstr.setRestrictionAttribute(RestrictionAttributes.KEYWORD);
        comparisonInstr.setComparisonOperator(ComparisonOperator.CONTAINS);
        comparisonInstr.setValue("keyword number");
        RestrictionLogicalCondition r2 = new RestrictionLogicalCondition();
        restInstrumentCond.setOperator(LogicalOperator.AND);
        RestrictionLogicalCondition r3 = new RestrictionLogicalCondition();
        restInstrumentCond.setOperator(LogicalOperator.AND);
        restInstrumentCond.getRestConditions().add(comparisonInstr);
        restInstrumentCond.getRestConditions().add(restCycleCond);
        restCycleCond.getRestConditions().add(r2);
        r2.getRestConditions().add(r3);
        Collection li = InvestigationSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restInstrumentCond, em);
        Collection ldat = DatasetSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restInstrumentCond, em);
        Collection ldaf = DatafileSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restInstrumentCond, em);
        Collection ls = SampleSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restInstrumentCond, em);
        assertEquals("Number of Investigation for Keyword 'keyword number'", 1, li.size());
        assertEquals("Number of Datasets for Keyword 'keyword number'", 2, ldat.size());
        assertEquals("Number of Datafiles for Keyword 'keyword number'", 3, ldaf.size());
        assertEquals("Number of Samples for Keyword 'keyword number'", 1, ls.size());
    }

    @Test
    public void keywords2() throws RestrictionException {
        RestrictionLogicalCondition restInstrumentCond = new RestrictionLogicalCondition();
        restInstrumentCond.setOperator(LogicalOperator.AND);
        RestrictionLogicalCondition restCycleCond = new RestrictionLogicalCondition();
        restCycleCond.setOperator(LogicalOperator.AND);
        RestrictionComparisonCondition comparisonInstr;
        comparisonInstr = new RestrictionComparisonCondition();
        comparisonInstr.setRestrictionAttribute(RestrictionAttributes.KEYWORD);
        comparisonInstr.setComparisonOperator(ComparisonOperator.STARTS_WITH);
        comparisonInstr.setValue("my keyword");
        RestrictionLogicalCondition r2 = new RestrictionLogicalCondition();
        restInstrumentCond.setOperator(LogicalOperator.AND);
        ParameterLogicalCondition op1 = new ParameterLogicalCondition(LogicalOperator.OR);
        op1.add(pcDataset.get(0));
        op1.add(pcDataset.get(1));
        restInstrumentCond.getRestConditions().add(comparisonInstr);
        restInstrumentCond.getRestConditions().add(restCycleCond);
        restCycleCond.getRestConditions().add(r2);
        Collection li = InvestigationSearch.searchByParameterCondition(VALID_USER_FOR_INVESTIGATION, op1, restInstrumentCond, InvestigationInclude.NONE, Queries.NO_PAGINATION, Queries.NO_PAGINATION, em);
        Collection ldat = DatasetSearch.searchByParameterCondition(VALID_USER_FOR_INVESTIGATION, op1, restInstrumentCond, DatasetInclude.NONE, Queries.NO_PAGINATION, Queries.NO_PAGINATION, em);
        Collection ldaf = DatafileSearch.searchByParameterCondition(VALID_USER_FOR_INVESTIGATION, op1, restInstrumentCond, DatafileInclude.NONE, Queries.NO_PAGINATION, Queries.NO_PAGINATION, em);
        Collection ls = SampleSearch.searchByParameterCondition(VALID_USER_FOR_INVESTIGATION, op1, restInstrumentCond, SampleInclude.NONE, Queries.NO_PAGINATION, Queries.NO_PAGINATION, em);
        assertEquals("Number of Investigation for Keyword 'keyword number'", 1, li.size());
        assertEquals("Number of Datasets for Keyword 'keyword number'", 1, ldat.size());
        assertEquals("Number of Datafiles for Keyword 'keyword number'", 2, ldaf.size());
        assertEquals("Number of Samples for Keyword 'keyword number'", 1, ls.size());
    }

    @Test
    public void andOrNested() throws RestrictionException {
        RestrictionLogicalCondition restInstrumentCond = new RestrictionLogicalCondition();
        restInstrumentCond.setOperator(LogicalOperator.AND);
        RestrictionLogicalCondition restCycleCond = new RestrictionLogicalCondition();
        restCycleCond.setOperator(LogicalOperator.AND);
        RestrictionComparisonCondition comparisonInstr;
        comparisonInstr = new RestrictionComparisonCondition();
        comparisonInstr.setRestrictionAttribute(RestrictionAttributes.INVESTIGATION_INSTRUMENT);
        comparisonInstr.setComparisonOperator(ComparisonOperator.EQUALS);
        comparisonInstr.setValue("instrument");
        RestrictionLogicalCondition r2 = new RestrictionLogicalCondition();
        restInstrumentCond.setOperator(LogicalOperator.AND);
        restInstrumentCond.getRestConditions().add(comparisonInstr);
        restInstrumentCond.getRestConditions().add(restCycleCond);
        restCycleCond.getRestConditions().add(r2);
        Collection li = InvestigationSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restInstrumentCond, em);
        assertEquals("Number of investigation per instrument", 2, li.size());
    }

    /**
     * Restriction logical condition example
     *
     * @throws ParameterSearchException
     * @throws RestrictionException
     */
    @Test
    public void restrinINCondition() throws ParameterSearchException, RestrictionException {
        Collection<Long> lid = new ArrayList<Long>();
        lid.add((long) 7928950);
        lid.add((long) 7928951);
        lid.add((long) 7928952);
        List<ParameterSearch> lp = new ArrayList<ParameterSearch>();
        ParameterSearch pv1 = new ParameterSearch(ParameterType.DATAFILE, parameter.get("datafile2_1"));
        lp.add(pv1);
        RestrictionComparisonCondition restriction1 = new RestrictionComparisonCondition(RestrictionAttributes.DATAFILE_ID, ComparisonOperator.IN, lid);
        Collection ldf = DatasetSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restriction1, DatasetInclude.NONE, 1, -1, em);
    }

    /**
     * Restriction logical condition example
     * 
     * @throws ParameterSearchException
     * @throws RestrictionException
     */
    @Test
    public void restrictionLogicalCondition() throws ParameterSearchException, RestrictionException {
        RestrictionComparisonCondition restriction1 = new RestrictionComparisonCondition(RestrictionAttributes.INVESTIGATION_TITLE, ComparisonOperator.STARTS_WITH, "Investigation 1");
        restriction1.setSensitive(true);
        List<String> inList = new ArrayList<String>();
        inList.add("nexus");
        inList.add("cosa");
        RestrictionLogicalCondition restricLog = new RestrictionLogicalCondition(LogicalOperator.AND).add(new RestrictionComparisonCondition(RestrictionAttributes.INVESTIGATION_START_DATE, ComparisonOperator.GREATER_THAN, new Date(0))).add(new RestrictionComparisonCondition(RestrictionAttributes.DATAFILE_FORMAT_TYPE, ComparisonOperator.IN, inList)).add(new RestrictionComparisonCondition(RestrictionAttributes.DATASET_TYPE, ComparisonOperator.EQUALS, "experiment_raw")).add(new RestrictionLogicalCondition(LogicalOperator.OR).add(restriction1).add(new RestrictionComparisonCondition(RestrictionAttributes.DATASET_NAME, ComparisonOperator.ENDS_WITH, "bLue")));
        List<ParameterSearch> lp = new ArrayList<ParameterSearch>();
        ParameterSearch pv1 = new ParameterSearch(ParameterType.DATAFILE, parameter.get("datafile2_1"));
        lp.add(pv1);
        List<Dataset> lds = (List<Dataset>) DatasetSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, DatasetInclude.NONE, 1, -1, em);
        List<Datafile> ldf = (List<Datafile>) DatafileSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, DatafileInclude.NONE, 1, -1, em);
        List<Sample> ls = (List<Sample>) SampleSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, SampleInclude.NONE, 1, -1, em);
        List<Investigation> li = (List<Investigation>) InvestigationSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, InvestigationInclude.NONE, 1, -1, em);
        assertEquals("Results of Datasets incorrect.", 2, lds.size());
        assertEquals("Results of Datafiles incorrect.", 2, ldf.size());
        assertEquals("Results of Investigations incorrect.", 1, li.size());
        assertEquals("Results of Samples incorrect.", 1, ls.size());
    }

    @Test
    public void inSensitiveCondition() throws ParameterSearchException, RestrictionException {
        RestrictionLogicalCondition restricLog = new RestrictionLogicalCondition(LogicalOperator.AND).add(new RestrictionComparisonCondition(RestrictionAttributes.INVESTIGATION_TITLE, ComparisonOperator.STARTS_WITH, "INVestiGation 1")).add(new RestrictionComparisonCondition(RestrictionAttributes.DATASET_NAME, ComparisonOperator.ENDS_WITH, "BlUE"));
        List<Dataset> lds = (List<Dataset>) DatasetSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restricLog, DatasetInclude.NONE, 1, -1, em);
        List<Datafile> ldf = (List<Datafile>) DatafileSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restricLog, DatafileInclude.NONE, 1, -1, em);
        List<Sample> ls = (List<Sample>) SampleSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restricLog, SampleInclude.NONE, 1, -1, em);
        List<Investigation> li = (List<Investigation>) InvestigationSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restricLog, InvestigationInclude.NONE, 1, -1, em);
        assertEquals("Results of Datasets incorrect.", 2, lds.size());
        assertEquals("Results of Datafiles incorrect.", 3, ldf.size());
        assertEquals("Results of Investigations incorrect.", 1, li.size());
        assertEquals("Results of Samples incorrect.", 1, ls.size());
    }

    @Test
    public void sensitiveCondition() throws ParameterSearchException, RestrictionException {
        RestrictionLogicalCondition restricLog = new RestrictionLogicalCondition(LogicalOperator.AND).add(new RestrictionComparisonCondition(RestrictionAttributes.INVESTIGATION_TITLE, ComparisonOperator.STARTS_WITH, "InvestiGation 1")).add(new RestrictionComparisonCondition(RestrictionAttributes.DATASET_NAME, ComparisonOperator.ENDS_WITH, "blue"));
        List<Dataset> lds = (List<Dataset>) DatasetSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restricLog, DatasetInclude.NONE, 1, -1, em);
        List<Datafile> ldf = (List<Datafile>) DatafileSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restricLog, DatafileInclude.NONE, 1, -1, em);
        List<Sample> ls = (List<Sample>) SampleSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restricLog, SampleInclude.NONE, 1, -1, em);
        List<Investigation> li = (List<Investigation>) InvestigationSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restricLog, InvestigationInclude.NONE, 1, -1, em);
        assertEquals("Results of Datasets incorrect.", 2, lds.size());
        assertEquals("Results of Datafiles incorrect.", 3, ldf.size());
        assertEquals("Results of Investigations incorrect.", 1, li.size());
        assertEquals("Results of Samples incorrect.", 1, ls.size());
    }

    /**
     * Restriction order by and Not exapmle
     * @throws ParameterSearchException
     * @throws RestrictionException
     */
    @Test
    public void restrictionNotOrder() throws ParameterSearchException, RestrictionException {
        RestrictionComparisonCondition restriction1 = new RestrictionComparisonCondition(RestrictionAttributes.INVESTIGATION_TITLE, ComparisonOperator.STARTS_WITH, "Investigation 1");
        List<String> inList = new ArrayList<String>();
        inList.add("NeXus");
        inList.add("no type");
        RestrictionLogicalCondition restricLog = new RestrictionLogicalCondition(LogicalOperator.AND).add(RestrictionCondition.Not(new RestrictionComparisonCondition(RestrictionAttributes.INVESTIGATION_START_DATE, ComparisonOperator.EQUALS, new Date(0)))).add(new RestrictionComparisonCondition(RestrictionAttributes.DATAFILE_FORMAT_TYPE, ComparisonOperator.IN, inList)).add(new RestrictionComparisonCondition(RestrictionAttributes.DATASET_TYPE, ComparisonOperator.EQUALS, "experiment_raw")).add(new RestrictionLogicalCondition(LogicalOperator.OR).add(restriction1).add(new RestrictionComparisonCondition(RestrictionAttributes.DATASET_NAME, ComparisonOperator.ENDS_WITH, "blue")));
        List<ParameterSearch> lp = new ArrayList<ParameterSearch>();
        ParameterSearch pv1 = new ParameterSearch(ParameterType.DATAFILE, parameter.get("datafile2_1"));
        lp.add(pv1);
        restricLog.setOrderByDesc(RestrictionAttributes.DATASET_NAME);
        List<Dataset> lds = (List<Dataset>) DatasetSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, DatasetInclude.NONE, 1, -1, em);
        restricLog.setOrderByDesc(null);
        List<Datafile> ldf = (List<Datafile>) DatafileSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, DatafileInclude.NONE, 1, -1, em);
        List<Sample> ls = (List<Sample>) SampleSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, SampleInclude.NONE, 1, -1, em);
        List<Investigation> li = (List<Investigation>) InvestigationSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, InvestigationInclude.NONE, 1, -1, em);
        assertEquals("Results of Datasets incorrect.", 2, lds.size());
        assertEquals("Results of Datafiles incorrect.", 2, ldf.size());
        assertEquals("Results of Investigations incorrect.", 1, li.size());
        assertEquals("Results of Samples incorrect.", 1, ls.size());
        assertEquals("First dataset name incorrect.", "dataset_3 blue", lds.get(0).getName());
    }

    /**
     * Restriction order by and Not exapmle
     * @throws ParameterSearchException
     * @throws RestrictionException
     */
    @Test
    public void restrictionOrderByDesc() throws ParameterSearchException, RestrictionException {
        RestrictionCondition restricLog = RestrictionCondition.orderByDesc(RestrictionAttributes.DATASET_NAME);
        List<ParameterSearch> lp = new ArrayList<ParameterSearch>();
        ParameterSearch pv1 = new ParameterSearch(ParameterType.DATAFILE, parameter.get("datafile2_1"));
        lp.add(pv1);
        List<Dataset> lds = (List<Dataset>) DatasetSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, DatasetInclude.NONE, 1, -1, em);
        restricLog.setOrderByAttribute(null);
        List<Datafile> ldf = (List<Datafile>) DatafileSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, DatafileInclude.NONE, 1, -1, em);
        List<Sample> ls = (List<Sample>) SampleSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, SampleInclude.NONE, 1, -1, em);
        List<Investigation> li = (List<Investigation>) InvestigationSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, InvestigationInclude.NONE, 1, -1, em);
        assertEquals("Results of Datasets incorrect.", 3, lds.size());
        assertEquals("Results of Datafiles incorrect.", 3, ldf.size());
        assertEquals("Results of Investigations incorrect.", 2, li.size());
        assertEquals("Results of Samples incorrect.", 2, ls.size());
    }

    @Test
    public void orderByAsc() throws ParameterSearchException, RestrictionException {
        RestrictionCondition restricLog = RestrictionCondition.orderByAsc(RestrictionAttributes.DATASET_NAME);
        List<ParameterSearch> lp = new ArrayList<ParameterSearch>();
        ParameterSearch pv1 = new ParameterSearch(ParameterType.DATAFILE, parameter.get("datafile2_1"));
        lp.add(pv1);
        List<Dataset> lds = (List<Dataset>) DatasetSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, DatasetInclude.NONE, 1, -1, em);
        restricLog.setOrderByAttribute(null);
        List<Datafile> ldf = (List<Datafile>) DatafileSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, DatafileInclude.NONE, 1, -1, em);
        List<Sample> ls = (List<Sample>) SampleSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, SampleInclude.NONE, 1, -1, em);
        List<Investigation> li = (List<Investigation>) InvestigationSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, InvestigationInclude.NONE, 1, -1, em);
        assertEquals("Results of Datasets incorrect.", 3, lds.size());
        assertEquals("Results of Datafiles incorrect.", 3, ldf.size());
        assertEquals("Results of Investigations incorrect.", 2, li.size());
        assertEquals("Results of Samples incorrect.", 2, ls.size());
        assertEquals("Second dataset name incorrect.", "dataset_2 red", lds.get(1).getName());
    }

    @Test
    public void maxResults() throws ParameterSearchException, RestrictionException {
        RestrictionCondition restricLog = RestrictionCondition.orderByAsc(RestrictionAttributes.DATASET_NAME);
        restricLog.setMaxResults(2);
        List<ParameterSearch> lp = new ArrayList<ParameterSearch>();
        ParameterSearch pv1 = new ParameterSearch(ParameterType.DATAFILE, parameter.get("datafile2_1"));
        lp.add(pv1);
        List<Dataset> lds = (List<Dataset>) DatasetSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, DatasetInclude.NONE, -1, -1, em);
        List<Datafile> ldf = (List<Datafile>) DatafileSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, DatafileInclude.NONE, -1, -1, em);
        List<Sample> ls = (List<Sample>) SampleSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, SampleInclude.NONE, -1, -1, em);
        List<Investigation> li = (List<Investigation>) InvestigationSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, InvestigationInclude.NONE, -1, -1, em);
        assertEquals("Results of Datasets incorrect.", 2, lds.size());
        assertEquals("Results of Datafiles incorrect.", 2, ldf.size());
        assertEquals("Results of Investigations incorrect.", 2, li.size());
        assertEquals("Results of Samples incorrect.", 2, ls.size());
        assertEquals("Second dataset name incorrect.", "dataset_2 red", lds.get(1).getName());
    }

    @Test
    public void firstResults() throws ParameterSearchException, RestrictionException {
        RestrictionComparisonCondition restricLog = new RestrictionComparisonCondition(RestrictionAttributes.INVESTIGATION_TITLE, ComparisonOperator.STARTS_WITH, "Investigation");
        restricLog.setFirstResult(1);
        restricLog.setOrderByAsc(true);
        restricLog.setOrderByAsc(RestrictionAttributes.DATASET_NAME);
        List<Dataset> lds = (List<Dataset>) DatasetSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restricLog, DatasetInclude.NONE, -1, -1, em);
        restricLog.setOrderByAsc(RestrictionAttributes.DATAFILE_NAME);
        List<Datafile> ldf = (List<Datafile>) DatafileSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restricLog, DatafileInclude.NONE, -1, -1, em);
        restricLog.setOrderByAsc(RestrictionAttributes.SAMPLE_NAME);
        List<Sample> ls = (List<Sample>) SampleSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restricLog, SampleInclude.NONE, -1, -1, em);
        restricLog.setOrderByAsc(RestrictionAttributes.INVESTIGATION_TITLE);
        List<Investigation> li = (List<Investigation>) InvestigationSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restricLog, InvestigationInclude.NONE, -1, -1, em);
        assertEquals("Results of Datasets incorrect.", 2, lds.size());
        assertEquals("Dataset names incorrect.", "dataset_2 red", lds.get(0).getName());
        assertEquals("Results of Datafiles incorrect.", 3, ldf.size());
        assertEquals("Datafile names incorrect.", "datafile_1Dat3", ldf.get(0).getName());
        assertEquals("Results of Investigations incorrect.", 1, li.size());
        assertTrue("Investigation names incorrect.", li.get(0).getTitle().startsWith("Investigation 2"));
        assertEquals("Results of Samples incorrect.", 1, ls.size());
        assertEquals("Sample names incorrect.", "Sample_2", ls.get(0).getName());
    }

    /**
     * Restriction comparison example
     * 
     * @throws ParameterSearchException
     * @throws RestrictionException
     */
    @Test
    public void restrictionComparisonCondition() throws ParameterSearchException, RestrictionException {
        RestrictionLogicalCondition restricLog = new RestrictionLogicalCondition(LogicalOperator.AND).add(RestrictionCondition.Not(new RestrictionComparisonCondition(RestrictionAttributes.INVESTIGATION_TITLE, ComparisonOperator.CONTAINS, "gation 1")));
        List<ParameterSearch> lp = new ArrayList<ParameterSearch>();
        ParameterSearch pv1 = new ParameterSearch(ParameterType.DATAFILE, parameter.get("datafile2_1"));
        lp.add(pv1);
        List<Dataset> lds = (List<Dataset>) DatasetSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, DatasetInclude.NONE, 1, -1, em);
        List<Datafile> ldf = (List<Datafile>) DatafileSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, DatafileInclude.NONE, 1, -1, em);
        List<Sample> ls = (List<Sample>) SampleSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, SampleInclude.NONE, 1, -1, em);
        List<Investigation> li = (List<Investigation>) InvestigationSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, restricLog, InvestigationInclude.NONE, 1, -1, em);
        assertEquals("Results of Datafiles incorrect.", 1, ldf.size());
        assertEquals("Results of Datasets incorrect.", 1, lds.size());
        assertEquals("Results of Investigations incorrect.", 1, li.size());
        assertEquals("Results of Samples incorrect.", 1, ls.size());
    }

    @Test
    public void returnIdsTest() throws ParameterSearchException, RestrictionException {
        List<ParameterSearch> lp = new ArrayList<ParameterSearch>();
        ParameterSearch pv1 = new ParameterSearch(ParameterType.DATAFILE, parameter.get("datafile2_1"));
        lp.add(pv1);
        RestrictionCondition cond = new RestrictionCondition();
        cond.setReturnLongId(true);
        List li = (List) InvestigationSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, cond, InvestigationInclude.NONE, Queries.NO_LIMITED_RESULTS, Queries.NO_LIMITED_RESULTS, em);
        List lds = (List) DatasetSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, cond, DatasetInclude.NONE, Queries.NO_LIMITED_RESULTS, Queries.NO_LIMITED_RESULTS, em);
        List ldf = (List) DatafileSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, cond, DatafileInclude.NONE, Queries.NO_LIMITED_RESULTS, Queries.NO_LIMITED_RESULTS, em);
        List ls = (List) SampleSearch.searchByParameterList(VALID_USER_FOR_INVESTIGATION, lp, cond, SampleInclude.NONE, Queries.NO_LIMITED_RESULTS, Queries.NO_LIMITED_RESULTS, em);
        assertEquals("Results of Datasets incorrect.", 3, lds.size());
        assertEquals("Results of Datafiles incorrect.", 3, ldf.size());
        assertEquals("Results of Investigations incorrect.", 2, li.size());
        assertEquals("Results of Samples incorrect.", 2, ls.size());
        assertEquals("Dataset return type is not Long", Long.class, lds.get(0).getClass());
        assertEquals("Datafile return type is not Long", Long.class, ldf.get(0).getClass());
        assertEquals("Investigation return type is not Long", Long.class, li.get(0).getClass());
        assertEquals("Sample return type is not Long", Long.class, ls.get(0).getClass());
    }

    @Test
    public void restrictionConditionTest() throws ParameterSearchException, RestrictionException {
        RestrictionComparisonCondition restriction1 = new RestrictionComparisonCondition(RestrictionAttributes.INVESTIGATION_TITLE, ComparisonOperator.CONTAINS, "gation 2");
        RestrictionLogicalCondition restricLog = new RestrictionLogicalCondition(LogicalOperator.OR).add(RestrictionCondition.Not(restriction1)).add(new RestrictionComparisonCondition(RestrictionAttributes.DATASET_NAME, ComparisonOperator.ENDS_WITH, "blue"));
        restricLog.setOrderByAsc(RestrictionAttributes.DATASET_NAME);
        ParameterLogicalCondition op1 = new ParameterLogicalCondition(LogicalOperator.OR);
        op1.add(pcDataset.get(0));
        op1.add(pcDataset.get(1));
        op1.add(pcSample.get(0));
        op1.add(pcDatafile.get(1));
        List<Dataset> lds = (List<Dataset>) DatasetSearch.searchByParameterCondition(VALID_USER_FOR_INVESTIGATION, op1, restricLog, DatasetInclude.NONE, 1, -1, em);
        List<Datafile> ldf = (List<Datafile>) DatafileSearch.searchByParameterCondition(VALID_USER_FOR_INVESTIGATION, op1, restricLog, DatafileInclude.NONE, 1, -1, em);
        List<Investigation> li = (List<Investigation>) InvestigationSearch.searchByParameterCondition(VALID_USER_FOR_INVESTIGATION, op1, restricLog, InvestigationInclude.NONE, 1, -1, em);
        List<Sample> ls = (List<Sample>) SampleSearch.searchByParameterCondition(VALID_USER_FOR_INVESTIGATION, op1, restricLog, SampleInclude.NONE, 1, -1, em);
        assertEquals("Results of Datasets incorrect.", 1, lds.size());
        assertEquals("Results of Datafiles incorrect.", 2, ldf.size());
        assertEquals("Results of Investigations incorrect.", 1, li.size());
        assertEquals("Results of Samples incorrect.", 1, ls.size());
    }

    @Test
    public void notRestrictionConditionTest() throws ParameterSearchException, RestrictionException {
        RestrictionComparisonCondition restriction1 = new RestrictionComparisonCondition(RestrictionAttributes.INVESTIGATION_TITLE, ComparisonOperator.CONTAINS, "gation 2");
        RestrictionLogicalCondition restricLog = new RestrictionLogicalCondition(LogicalOperator.OR).add(new RestrictionLogicalCondition(LogicalOperator.OR).add(restriction1)).add(new RestrictionComparisonCondition(RestrictionAttributes.DATASET_NAME, ComparisonOperator.ENDS_WITH, "blue"));
        restricLog.setOrderByAsc(RestrictionAttributes.DATASET_NAME);
        ParameterLogicalCondition op1 = new ParameterLogicalCondition(LogicalOperator.OR);
        op1.add(pcSample.get(1));
        List<Dataset> lds = (List<Dataset>) DatasetSearch.searchByParameterCondition(VALID_USER_FOR_INVESTIGATION, ParameterCondition.Not(op1), restricLog, DatasetInclude.NONE, 1, -1, em);
        restricLog.setOrderByAttribute(null);
        List<Datafile> ldf = (List<Datafile>) DatafileSearch.searchByParameterCondition(VALID_USER_FOR_INVESTIGATION, op1, restricLog, DatafileInclude.NONE, 1, -1, em);
        List<Investigation> li = (List<Investigation>) InvestigationSearch.searchByParameterCondition(VALID_USER_FOR_INVESTIGATION, op1, restricLog, InvestigationInclude.NONE, 1, -1, em);
        for (Investigation i : li) {
            System.out.println(i.getId() + "-> " + i.getTitle());
        }
        List<Sample> ls = (List<Sample>) SampleSearch.searchByParameterCondition(VALID_USER_FOR_INVESTIGATION, op1, restricLog, SampleInclude.NONE, 1, -1, em);
        assertEquals("Results of Datasets incorrect.", 2, lds.size());
        assertEquals("Results of Datafiles incorrect.", 3, ldf.size());
        assertEquals("Results of Investigations incorrect.", 1, li.size());
        assertEquals("Results of Samples incorrect.", 1, ls.size());
    }

    /**
     * Restriction logical condition example
     *
     * @throws ParameterSearchException
     * @throws RestrictionException
     */
    @Test
    public void betweenDates() throws ParameterSearchException, RestrictionException {
        RestrictionComparisonCondition restriction1 = new RestrictionComparisonCondition(RestrictionAttributes.INVESTIGATION_START_DATE, ComparisonOperator.BETWEEN, new Date(0), "2050-01-01 ");
        RestrictionLogicalCondition restricLog = new RestrictionLogicalCondition(LogicalOperator.AND).add(new RestrictionLogicalCondition(LogicalOperator.AND).add(restriction1).add(new RestrictionComparisonCondition(RestrictionAttributes.DATASET_NAME, ComparisonOperator.ENDS_WITH, "blue")));
        List<Dataset> lds = (List<Dataset>) DatasetSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restricLog, DatasetInclude.NONE, 1, -1, em);
        List<Datafile> ldf = (List<Datafile>) DatafileSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restricLog, DatafileInclude.NONE, 1, -1, em);
        List<Sample> ls = (List<Sample>) SampleSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restricLog, SampleInclude.NONE, 1, -1, em);
        List<Investigation> li = (List<Investigation>) InvestigationSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, restricLog, InvestigationInclude.NONE, 1, -1, em);
        assertEquals("Results of Datasets incorrect.", 2, lds.size());
        assertEquals("Results of Datafiles incorrect.", 3, ldf.size());
        assertEquals("Results of Investigations incorrect.", 1, li.size());
        assertEquals("Results of Samples incorrect.", 1, ls.size());
    }

    /**
     * Restriction logical condition example
     *
     * @throws ParameterSearchException
     * @throws RestrictionException
     */
    @Test
    public void facilityUser() throws ParameterSearchException, RestrictionException {
        String contain = "n";
        RestrictionComparisonCondition compFirstName = new RestrictionComparisonCondition();
        compFirstName.setRestrictionAttribute(RestrictionAttributes.INVESTIGATOR_USER_FIRST_NAME);
        compFirstName.setComparisonOperator(ComparisonOperator.STARTS_WITH);
        compFirstName.setValue(contain);
        RestrictionComparisonCondition compMiddleName = new RestrictionComparisonCondition();
        compMiddleName.setRestrictionAttribute(RestrictionAttributes.INVESTIGATOR_USER_MIDDLE_NAME);
        compMiddleName.setComparisonOperator(ComparisonOperator.STARTS_WITH);
        compMiddleName.setValue(contain);
        RestrictionComparisonCondition compLastName = new RestrictionComparisonCondition();
        compLastName.setRestrictionAttribute(RestrictionAttributes.INVESTIGATOR_USER_LAST_NAME);
        compLastName.setComparisonOperator(ComparisonOperator.STARTS_WITH);
        compLastName.setValue(contain);
        RestrictionLogicalCondition logCond = new RestrictionLogicalCondition();
        logCond.setOperator(LogicalOperator.OR);
        logCond.setMaxResults(15);
        List<FacilityUser> lf = (List<FacilityUser>) FacilityManager.searchByRestriction(logCond, em);
        for (FacilityUser user : lf) System.out.println(user.getFacilityUserId());
    }

    @Test
    public void equalString() throws RestrictionException {
        RestrictionComparisonCondition r = new RestrictionComparisonCondition();
        r.setRestrictionAttribute(RestrictionAttributes.INVESTIGATION_INSTRUMENT);
        r.setComparisonOperator(ComparisonOperator.EQUALS);
        r.setValue("instrument");
        Collection li = InvestigationSearch.searchByRestriction(VALID_USER_FOR_INVESTIGATION, r, em);
        assertEquals("Number of investigation per instrument", 2, li.size());
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(UsesExamples.class);
    }
}
