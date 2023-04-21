package edu.pw.treegrid.server.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import edu.pw.treegrid.server.classmodel.BusinessModelData;
import edu.pw.treegrid.shared.Configuration;

public class ReportServletSWS {

    private Object p1Value = "p1Value";

    private String p1Key = "p1Key";

    private String p2Key = "p2Key";

    private Object p2Value = "p2Value";

    private Record record;

    private ReportServlet instance;

    private MyHttpRequest request;

    private MyHttpResponse response;

    private static DataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        ds.setUsername("sws");
        ds.setPassword("sws");
        ds.setUrl("jdbc:oracle:thin:@localhost:1521/xe");
        return ds;
    }

    @Before
    public void before() throws ParserConfigurationException, ServletException {
        record = new Record();
        record.addParam(p1Key, p1Value);
        record.addParam(p2Key, p2Value);
        instance = new ReportServlet() {

            @Override
            protected DataSource getDataSource() {
                return ReportServletSWS.getDataSource();
            }
        };
        instance._initBeans();
        instance.getMarshaller().setRepositoryDirectory("src/test/resources/repository_sws_java/");
        instance.getMarshaller().deserializeClasses();
        request = new MyHttpRequest();
        response = new MyHttpResponse();
    }

    @Test
    public void testFetchMetaModel() throws ServletException, IOException {
        request.uri = Configuration.META_MODEL_URL;
        request.reportId = "bazaKlientow";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }

    @Test
    public void testBledy() throws ServletException, IOException {
        request.uri = Configuration.META_MODEL_URL;
        String reports[] = { "bledy", "bledy2" };
        for (int j = 0; j < 5; ++j) {
            for (int i = 0; i < reports.length; ++i) {
                request.reportId = reports[i];
                instance.doGet(request, response);
                System.out.println("#" + j + " " + request.reportId);
            }
        }
    }

    @Test
    public void testFetchMetaModelDynamicTitle() throws ServletException, IOException {
        request.uri = Configuration.META_MODEL_URL;
        String reports[] = { "aktywacjaDoradca", "aktywacjaPortfel", "bazaFirm", "bazaKlientow", "bazaKlientowPS", "bazaProspektow", "dep_podsumowanie", "doch_wynik_netto_pods", "fenixWysylka", "fenixWysylka1", "fenixWysylka2", "fx_podsumowanie", "kre_podsumowanie", "pipeline_akw", "pipeline_kredytowy_ak", "pipeline_kredytowy_bak", "pipeline_kredytowy_bdk", "pipeline_kredytowy_kl", "pipeline_kredytowy", "pipeline_niekredytowy", "pot_dep_dzialanie", "pot_dep", "pot_fx_dzialanie", "pot_fx", "pot_leasing_potrzeby", "pot_leasing_przedmioty", "pot_leasing", "przegladDoradca", "rachunki", "rachunkiCOB", "raportSpotkan", "wnioskiCenowe", "wnioskiCenoweCOB", "wplywy_podsumowanie", "zaleglosci", "zdarzeniaDoradca", "zdarzeniaPortfel" };
        for (int j = 0; j < 5; ++j) {
            for (int i = 0; i < reports.length; ++i) {
                request.reportId = reports[i];
                instance.doGet(request, response);
                System.out.println("#" + j + " " + request.reportId);
            }
        }
    }

    @Test
    public void testFetchMetaModelAktywacja() throws ServletException, IOException {
        request.uri = Configuration.META_MODEL_URL;
        request.reportId = "aktywacjaDoradca";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }

    @Test
    public void testFetchMetaModelPotFxDzialanie() throws ServletException, IOException {
        request.uri = Configuration.META_MODEL_URL;
        request.reportId = "pot_fx_dzialanie";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }

    @Test
    public void testFetchMetaModelPipelineAkw() throws ServletException, IOException {
        request.uri = Configuration.META_MODEL_URL;
        request.reportId = "pipeline_akw";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }

    @Test
    public void testFetchMetaModelZdarzenia() throws ServletException, IOException {
        request.uri = Configuration.META_MODEL_URL;
        request.reportId = "zdarzeniaDoradca";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }

    @Test
    public void testFetchMetaModelPrzeglad() throws ServletException, IOException {
        request.uri = Configuration.META_MODEL_URL;
        request.reportId = "przegladDoradca";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }

    @Test
    public void testFetchMetaModel2() throws ServletException, IOException {
        request.uri = Configuration.META_MODEL_URL;
        request.reportId = "pipeline_kredytowy";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }

    @Test
    public void testFetchMetaModel3() throws ServletException, IOException {
        request.uri = Configuration.META_MODEL_URL;
        request.reportId = "pot_fx";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }

    @Test
    public void testFetchMetaModel4() throws ServletException, IOException {
        request.uri = Configuration.META_MODEL_URL;
        request.reportId = "pot_dep";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }

    @Test
    public void testFetchData() throws ServletException, IOException {
        request.uri = Configuration.DATA_URL;
        request.reportId = "pot_fx";
        request.hierarchy = "siec";
        request.parentPath = "null";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }

    @Test
    public void testFetchData_FX_c_() throws ServletException, IOException {
        request.uri = Configuration.DATA_URL;
        request.reportId = "fx_podsumowanie";
        request.hierarchy = "region";
        request.parentPath = "null";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }

    @Test
    public void testFetchData_BazaKlient_c_() throws ServletException, IOException {
        request.uri = Configuration.DATA_URL;
        request.reportId = "bazaKlientow";
        request.hierarchy = "region";
        request.parentPath = "null;61";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }

    @Test
    public void testFetchData_BazaKlient_cr_() throws ServletException, IOException {
        request.uri = Configuration.DATA_URL;
        request.reportId = "bazaKlientow";
        request.hierarchy = "region;zespol";
        request.parentPath = "null;61";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }

    @Test
    public void testFetchData_BazaKlient_crz_() throws ServletException, IOException {
        request.uri = Configuration.DATA_URL;
        request.reportId = "bazaKlientow";
        request.hierarchy = "region;zespol;portfel";
        request.parentPath = "null";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }

    @Test
    public void testFetchReportHierarchies() throws ServletException, IOException {
        System.out.println("testFetchReportHierarchies()");
        request.uri = Configuration.HIERARCHIES_URL;
        request.reportId = "bazaKlientow";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }

    @Test
    public void testFetchReportHierarchiesAktywacja() throws ServletException, IOException {
        System.out.println("testFetchReportHierarchies()");
        request.uri = Configuration.HIERARCHIES_URL;
        request.reportId = "aktywacjaDoradca";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }

    @Test
    public void testFetchReportHierarchiesPipelineAkw() throws ServletException, IOException {
        System.out.println("testFetchReportHierarchies()");
        request.uri = Configuration.HIERARCHIES_URL;
        request.reportId = "pipeline_akw";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }

    @Test
    public void testFetchReportHierarchiesZdarzenia() throws ServletException, IOException {
        System.out.println("testFetchReportHierarchies()");
        request.uri = Configuration.HIERARCHIES_URL;
        request.reportId = "zdarzeniaDoradca";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }

    @Test
    public void testFetchReportHierarchiesPrzeglad() throws ServletException, IOException {
        System.out.println("testFetchReportHierarchies()");
        request.uri = Configuration.HIERARCHIES_URL;
        request.reportId = "przegladDoradca";
        instance.doGet(request, response);
        String xml = response.stream.builder.toString();
        System.out.println(xml);
    }
}
