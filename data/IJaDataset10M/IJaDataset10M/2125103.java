package edu.pw.treegrid.server.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import edu.pw.treegrid.server.classmodel.BusinessModelData;
import edu.pw.treegrid.server.message.Hierarchy;
import edu.pw.treegrid.server.message.Path;
import edu.pw.treegrid.server.model.DomainField;
import edu.pw.treegrid.server.model.DomainObject;
import edu.pw.treegrid.server.reportmodel.Report;
import edu.pw.treegrid.server.reportmodel.ReportColumn;
import edu.pw.treegrid.server.reportmodel.ReportSimpleColumn;
import edu.pw.treegrid.shared.ReportColumnCategory;
import edu.pw.treegrid.shared.ReportColumnDescription;
import edu.pw.treegrid.shared.ReportColumnType;

@Ignore
public class ReportControllerTest {

    private static ReportController instance;

    private String reportId;

    private BusinessModelData data = new BusinessModelData();

    private List<Record> responseRecords;

    private Map<String, Object> firstRecordParams;

    private static DataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        ds.setUsername("crm_unit");
        ds.setPassword("crm_unit");
        ds.setUrl("jdbc:oracle:thin:@crm02.centrala.kbsa:1524/CRMREP");
        return ds;
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        instance = new ReportController(getDataSource());
        instance.getMarshaller().setRepositoryDirectory("../model/src/test/resources/repository/");
        instance.getMarshaller().deserializeClasses();
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testFetchMetaModel1() {
        reportId = "1";
        List<Record> records = instance.fetchMetaModel(reportId);
        Record r1 = records.get(4);
        Map<String, Object> params = r1.getParams();
        assertEquals(data.report1KlientNazwa.getName(), params.get(ReportColumnDescription.NAME));
        assertEquals(data.report1KlientNazwa.getImageSrc(), params.get(ReportColumnDescription.IMAGE_SRC));
        assertEquals(data.report1KlientNazwa.getTitle(), params.get(ReportColumnDescription.TITLE));
        assertEquals(data.report1KlientNazwa.getType(), params.get(ReportColumnDescription.TYPE));
        assertEquals(data.report1KlientNazwa.getCategory(), params.get(ReportColumnDescription.CATEGORY));
    }

    @Test
    public void testFetchMetaModelSpotkania() {
        reportId = "spotkaniaReport";
        List<Record> records = instance.fetchMetaModel(reportId);
        Record r1 = records.get(3);
        Map<String, Object> params = r1.getParams();
    }

    @Test
    public void testFetchData7_rd_r() {
        reportId = "7";
        String parentPath = "null";
        String hierarchy = data.report7RegionSpotkaniaAttribute.getName();
        hierarchy += ";";
        hierarchy += data.report7DoradcaSpotkaniaAttribute.getName();
        fetchData(parentPath, hierarchy);
        checkFirstRecordContainsKey(data.report7RegionSpotkaniaAttribute.getName());
        checkFirstRecordContainsKey(data.report7KlientDaneFinansoweAttribute.getName());
    }

    @Test
    public void testFetchData7_rd_rd() {
        reportId = "7";
        String parentPath = "null;11";
        String hierarchy = data.report7RegionSpotkaniaAttribute.getName();
        hierarchy += ";";
        hierarchy += data.report7DoradcaSpotkaniaAttribute.getName();
        fetchData(parentPath, hierarchy);
        checkFirstRecordContainsKey(data.report7DoradcaSpotkaniaAttribute.getName());
        checkFirstRecordContainsKey(data.report7KlientDaneFinansoweAttribute.getName());
    }

    @Test
    public void testFetchData7_rd_rdo() {
        reportId = "7";
        String parentPath = "null;11;100";
        String hierarchy = data.report7RegionSpotkaniaAttribute.getName();
        hierarchy += ";";
        hierarchy += data.report7DoradcaSpotkaniaAttribute.getName();
        fetchData(parentPath, hierarchy);
        checkFirstRecordContainsKey(data.report7RegionSpotkaniaAttribute.getName());
        checkFirstRecordContainsKey(data.report7DoradcaSpotkaniaAttribute.getName());
        checkFirstRecordContainsKey(data.report7KlientDaneFinansoweAttribute.getName());
        checkFirstRecordContainsKey(data.report7KlientNazwaAttribute.getName());
        checkFirstRecordContainsKey(data.report7TematAttribute.getName());
    }

    @Test
    public void testFetchData7__() {
        reportId = "7";
        String parentPath = "null";
        String hierarchy = "";
        fetchData(parentPath, hierarchy);
        checkFirstRecordContainsKey(data.report7RegionSpotkaniaAttribute.getName());
        checkFirstRecordContainsKey(data.report7KlientDaneFinansoweAttribute.getName());
    }

    @Test
    public void testFetchData7_r_r() {
        reportId = "7";
        String parentPath = "null";
        String hierarchy = data.report7RegionSpotkaniaAttribute.getName();
        fetchData(parentPath, hierarchy);
        checkFirstRecordContainsKey(data.report7RegionSpotkaniaAttribute.getName());
        checkFirstRecordContainsKey(data.report7KlientDaneFinansoweAttribute.getName());
    }

    @Test
    public void testFetchData7_r_ro() {
        reportId = "7";
        String parentPath = "null;11";
        String hierarchy = data.report7RegionSpotkaniaAttribute.getName();
        fetchData(parentPath, hierarchy);
        checkFirstRecordContainsKey(data.report7RegionSpotkaniaAttribute.getName());
        checkFirstRecordContainsKey(data.report7KlientDaneFinansoweAttribute.getName());
        checkFirstRecordContainsKey(data.report7KlientNazwaAttribute.getName());
        checkFirstRecordContainsKey(data.report7TematAttribute.getName());
    }

    @Test
    public void testFetchData7_rzd_r() {
        reportId = "7";
        String parentPath = "null";
        String hierarchy = data.report7RegionSpotkaniaAttribute.getName();
        hierarchy += ";";
        hierarchy += data.report7ZespolSpotkaniaAttribute.getName();
        hierarchy += ";";
        hierarchy += data.report7DoradcaSpotkaniaAttribute.getName();
        fetchData(parentPath, hierarchy);
        checkFirstRecordContainsKey(data.report7RegionSpotkaniaAttribute.getName());
        checkFirstRecordContainsKey(data.report7KlientDaneFinansoweAttribute.getName());
    }

    @Test
    public void testFetchData7_rzd_rz() {
        reportId = "7";
        String parentPath = "null;11";
        String hierarchy = data.report7RegionSpotkaniaAttribute.getName();
        hierarchy += ";";
        hierarchy += data.report7ZespolSpotkaniaAttribute.getName();
        hierarchy += ";";
        hierarchy += data.report7DoradcaSpotkaniaAttribute.getName();
        fetchData(parentPath, hierarchy);
        checkFirstRecordContainsKey(data.report7ZespolSpotkaniaAttribute.getName());
        checkFirstRecordContainsKey(data.report7KlientDaneFinansoweAttribute.getName());
    }

    @Test
    public void testFetchData7_rzd_rzd() {
        reportId = "7";
        String parentPath = "null;11;111";
        String hierarchy = data.report7RegionSpotkaniaAttribute.getName();
        hierarchy += ";";
        hierarchy += data.report7ZespolSpotkaniaAttribute.getName();
        hierarchy += ";";
        hierarchy += data.report7DoradcaSpotkaniaAttribute.getName();
        fetchData(parentPath, hierarchy);
        checkFirstRecordContainsKey(data.report7DoradcaSpotkaniaAttribute.getName());
        checkFirstRecordContainsKey(data.report7KlientDaneFinansoweAttribute.getName());
    }

    @Test
    public void testFetchData7_rzd_rzdo() {
        reportId = "7";
        String parentPath = "null;11;111;100";
        String hierarchy = data.report7RegionSpotkaniaAttribute.getName();
        hierarchy += ";";
        hierarchy += data.report7ZespolSpotkaniaAttribute.getName();
        hierarchy += ";";
        hierarchy += data.report7DoradcaSpotkaniaAttribute.getName();
        fetchData(parentPath, hierarchy);
        checkFirstRecordContainsKey(data.report7RegionSpotkaniaAttribute.getName());
        checkFirstRecordContainsKey(data.report7ZespolSpotkaniaAttribute.getName());
        checkFirstRecordContainsKey(data.report7DoradcaSpotkaniaAttribute.getName());
        checkFirstRecordContainsKey(data.report7KlientDaneFinansoweAttribute.getName());
        checkFirstRecordContainsKey(data.report7KlientNazwaAttribute.getName());
        checkFirstRecordContainsKey(data.report7TematAttribute.getName());
    }

    @Test
    public void testFetchDataBazaKlientow_c_empty() {
        reportId = "bazaKlientow";
        String parentPath = "null";
        String hierarchy = data.bazaKlientowCentrala.getName();
        fetchData(parentPath, hierarchy);
        checkFirstRecordContainsKey(data.bazaKlientowCentrala.getName(), true);
        checkFirstRecordContainsKey(data.bazaKlientowRegion.getName(), false);
        checkFirstRecordContainsKey(data.bazaKlientowZespol.getName(), false);
        checkFirstRecordContainsKey(data.bazaKlientowDoradca.getName(), false);
        checkFirstRecordContainsKey(data.bazaKlientowKlient.getName(), false);
    }

    @Test
    public void testFetchDataBazaKlientow_c_c_null() {
        reportId = "bazaKlientow";
        String parentPath = "null;null";
        String hierarchy = data.bazaKlientowCentrala.getName();
        fetchData(parentPath, hierarchy);
        checkFirstRecordContainsKey(data.bazaKlientowCentrala.getName(), true);
        checkFirstRecordContainsKey(data.bazaKlientowRegion.getName(), true);
        checkFirstRecordContainsKey(data.bazaKlientowZespol.getName(), true);
        checkFirstRecordContainsKey(data.bazaKlientowDoradca.getName(), true);
        checkFirstRecordContainsKey(data.bazaKlientowKlient.getName(), true);
    }

    @Test
    public void testFetchDataBazaKlientow_r_empty() {
        reportId = "bazaKlientow";
        String parentPath = "null";
        String hierarchy = data.bazaKlientowRegion.getName();
        fetchData(parentPath, hierarchy);
        checkFirstRecordContainsKey(data.bazaKlientowCentrala.getName(), false);
        checkFirstRecordContainsKey(data.bazaKlientowRegion.getName(), true);
        checkFirstRecordContainsKey(data.bazaKlientowZespol.getName(), false);
        checkFirstRecordContainsKey(data.bazaKlientowDoradca.getName(), false);
        checkFirstRecordContainsKey(data.bazaKlientowKlient.getName(), false);
    }

    @Test
    public void testFetchDataBazaKlientow_d_empty() {
        reportId = "bazaKlientow";
        String parentPath = "null";
        String hierarchy = data.bazaKlientowDoradca.getName();
        fetchData(parentPath, hierarchy);
        checkFirstRecordContainsKey(data.bazaKlientowCentrala.getName(), false);
        checkFirstRecordContainsKey(data.bazaKlientowRegion.getName(), false);
        checkFirstRecordContainsKey(data.bazaKlientowZespol.getName(), false);
        checkFirstRecordContainsKey(data.bazaKlientowDoradca.getName(), true);
        checkFirstRecordContainsKey(data.bazaKlientowKlient.getName(), false);
    }

    @Test
    public void testFetchDataBazaKlientow_zd_z() {
        reportId = "bazaKlientow";
        String parentPath = "null;111";
        String hierarchy = data.bazaKlientowZespol.getName();
        hierarchy += ";";
        hierarchy += data.bazaKlientowDoradca.getName();
        fetchData(parentPath, hierarchy);
        checkFirstRecordContainsKey(data.bazaKlientowCentrala.getName(), false);
        checkFirstRecordContainsKey(data.bazaKlientowRegion.getName(), false);
        checkFirstRecordContainsKey(data.bazaKlientowZespol.getName(), false);
        checkFirstRecordContainsKey(data.bazaKlientowDoradca.getName(), true);
        checkFirstRecordContainsKey(data.bazaKlientowKlient.getName(), false);
    }

    @Test
    public void testFetchDataBazaProspektow_c_empty() {
        reportId = data.prospektyReport.getId();
        String parentPath = "null";
        String hierarchy = data.bazaProspektowCentrala.getName();
        fetchData(parentPath, hierarchy);
        checkFirstRecordContainsKey(data.bazaProspektowCentrala.getName(), true);
        checkFirstRecordContainsKey(data.bazaProspektowRegion.getName(), false);
        checkFirstRecordContainsKey(data.bazaProspektowZespol.getName(), false);
        checkFirstRecordContainsKey(data.bazaProspektowDoradca.getName(), false);
        checkFirstRecordContainsKey(data.bazaProspektowKlient.getName(), false);
    }

    @Test
    public void testFetchDataSpotkania_c_empty() {
        reportId = data.spotkaniaReport.getId();
        String parentPath = "null";
        String hierarchy = data.spotkaniaReportCentrala.getName();
        fetchData(parentPath, hierarchy);
        checkFirstRecordContainsKey(data.spotkaniaReportCentrala.getName(), true);
    }

    private void fetchData(String parentPath, String hierarchy) {
        responseRecords = instance.fetchData(reportId, parentPath, hierarchy);
        System.out.println("HIERARCHY: " + hierarchy + "\tPATH: " + parentPath);
        System.out.println("Records: " + responseRecords.size());
        System.out.println("-------------------------------------------------");
        firstRecordParams = responseRecords.get(0).getParams();
        for (String k : firstRecordParams.keySet()) {
            System.out.println(k + " -> " + firstRecordParams.get(k));
        }
        System.out.println("-------------------------------------------------");
    }

    private void checkFirstRecordContainsKey(String key) {
        checkFirstRecordContainsKey(key, true);
    }

    private void checkFirstRecordContainsKey(String key, boolean contains) {
        Object v = firstRecordParams.get(key);
        if (v == null) {
            v = "null";
        }
        boolean isEmptyValue = (v.toString().equals(""));
        if (contains && isEmptyValue) {
            fail("Dont contains key=" + key + " value=" + v);
        } else if (!contains && !isEmptyValue) {
            fail("Contains key=" + key + " value=" + v);
        }
    }

    @Test
    public void testConvertReportColumnToReportColumnDescription() {
        ReportColumn reportColumn = data.report1KlientNazwa;
        ReportColumnDescription column = instance.convert(reportColumn);
        ReportSimpleColumn rsc = reportColumn.getReportSimpleColumn();
        assertEquals(reportColumn.getName(), column.getName());
        assertEquals(reportColumn.getImageSrc(), column.getImageSrc());
        assertEquals(ReportColumnCategory.CANDITATE, column.getCategory());
        assertEquals(rsc.getTitle(), column.getTitle());
        assertEquals(ReportColumnType.TEXT, column.getType());
    }

    @Test
    public void testConvertReportColumnToReportColumnDescription2() {
        ReportColumn reportColumn = data.report7KlientDaneFinansoweAttribute;
        ReportColumnDescription column = instance.convert(reportColumn);
        ReportSimpleColumn rsc = reportColumn.getReportSimpleColumn();
        assertEquals(rsc.getName(), column.getName());
        assertEquals(reportColumn.getImageSrc(), column.getImageSrc());
        assertEquals(ReportColumnCategory.MEASURE, column.getCategory());
        assertEquals(rsc.getTitle(), column.getTitle());
        assertEquals(ReportColumnType.INTEGER, column.getType());
    }

    @Test
    public void testGetReportColumnDescriptions1() {
        Report report = data.report1;
        List<ReportColumnDescription> columns = instance.getReportColumnDescriptions(report);
        matches(columns, report.getColumns());
    }

    private void matches(List<ReportColumnDescription> columns, List<ReportColumn> reportColumns) {
        assertEquals(columns.size(), reportColumns.size() + 3);
        for (ReportColumn rc : reportColumns) {
            boolean found = false;
            for (ReportColumnDescription rcd : columns) {
                if (matches(rc, rcd)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                fail("Not found: " + rc.toString());
            }
        }
    }

    private boolean matches(ReportColumn rc, ReportColumnDescription rcd) {
        ReportSimpleColumn rsc = rc.getReportSimpleColumn();
        if (rsc.getCategory() != null && !rsc.getCategory().equals(rcd.getCategory())) {
            return false;
        }
        if (rc.getImageSrc() != null && !rc.getImageSrc().equals(rcd.getImageSrc())) {
            return false;
        }
        if (rc.getName() != null && !rc.getName().equals(rcd.getName())) {
            return false;
        }
        if (rsc.getTitle() != null && !rsc.getTitle().equals(rcd.getTitle())) {
            return false;
        }
        if (rsc.getType() != null && !rsc.getType().equals(rcd.getType())) {
            return false;
        }
        return true;
    }

    @Test
    public void testBuildPathString() {
        String s1 = "s1";
        String s2 = "s2";
        Path path = new Path();
        path.addNode(s1);
        path.addNode(s2);
        String pathString = instance.buildPathString(path);
        assertEquals(s1 + ReportController.PATH_SEPARATOR + s2, pathString);
    }

    @Test
    public void testBuildPathFromString() {
        String s1 = "s1";
        String s2 = "s2";
        String p = "null" + ReportController.PATH_SEPARATOR + s1 + ReportController.PATH_SEPARATOR + s2;
        Path path = instance.buildPathFromString(p);
        assertEquals(3, path.getPath().size());
        assertEquals(s1, path.getPath().get(1));
        assertEquals(s2, path.getPath().get(2));
    }

    @Test
    public void testBuildHierarchyFromEmptyString() {
        String h = "";
        Hierarchy hierarchy = instance.buildHierarchyFromString(data.report7, h);
        assertEquals(0, hierarchy.getGroupColumns().size());
    }

    @Test
    public void testBuildHierarchyFromString() {
        String h1 = data.report7ZespolSpotkaniaAttribute.getName();
        String h2 = data.report7DoradcaSpotkaniaAttribute.getName();
        String h = h1 + ReportController.PATH_SEPARATOR + h2;
        Hierarchy hierarchy = instance.buildHierarchyFromString(data.report7, h);
        assertEquals(2, hierarchy.getGroupColumns().size());
        assertEquals(data.report7ZespolSpotkaniaAttribute, hierarchy.getGroupColumns().get(0));
        assertEquals(data.report7DoradcaSpotkaniaAttribute, hierarchy.getGroupColumns().get(1));
    }

    @Test
    public void testConvertDomainObjectToRecord() {
        DomainObject object = new DomainObject();
        DomainField f1 = new DomainField();
        DomainField f2 = new DomainField();
        f1.setDomainobject(object);
        ReportColumn c1 = data.report7KlientNazwaAttribute;
        f1.setReportColumn(c1);
        String c1_value = "f1";
        f1.setValue(c1_value);
        f2.setDomainobject(object);
        ReportColumn c2 = data.report7KlientDaneFinansoweAttribute;
        f2.setReportColumn(c2);
        String c2_value = "f2";
        f2.setValue(c2_value);
        String name = "name";
        object.setName(name);
        boolean isFolder = true;
        object.setFolder(isFolder);
        String imageSrc = "imageSrc";
        object.setImageSrc(imageSrc);
        Path parentPath = new Path();
        parentPath.addNode("1");
        object.setParentPath(parentPath);
        String parentPathString = instance.buildPathString(parentPath);
        Path path = new Path(parentPath);
        path.addNode("2");
        object.setPath(path);
        String pathString = instance.buildPathString(path);
        object.addDomainField(f1);
        object.addDomainField(f2);
        Record record = instance.convert(object);
        Map<String, Object> params = record.getParams();
        assertEquals(name, params.get(ReportColumnDescription.NAME_COLUMN));
        assertEquals(imageSrc, params.get(ReportColumnDescription.IMAGE_SRC));
        assertEquals(isFolder, params.get(ReportColumnDescription.FOLDER_COLUMN));
        assertEquals(pathString, params.get(ReportColumnDescription.PATH_COLUMN));
        assertEquals(parentPathString, params.get(ReportColumnDescription.PARENT_PATH_COLUMN));
        assertEquals(c1_value, params.get(c1.getName()));
        assertEquals(c2_value, params.get(c2.getName()));
    }
}
