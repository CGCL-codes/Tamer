package org.openi.chart;

import org.apache.log4j.Logger;
import org.jfree.data.category.DefaultCategoryDataset;
import org.openi.analysis.Analysis;
import org.openi.analysis.Datasource;
import org.openi.project.ProjectContext;
import org.openi.xmla.DatasetAdapter;
import org.openi.xmla.XmlaConnector;
import org.xml.sax.SAXException;
import com.tonbeller.jpivot.olap.model.OlapException;
import com.tonbeller.jpivot.olap.model.OlapModel;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * should this be a spring controller? 
 *
 */
public class StreamChartServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger(StreamChartServlet.class);

    public StreamChartServlet() {
        super();
    }

    private int parseInt(String value, int defaultValue) {
        int retVal = defaultValue;
        try {
            retVal = Integer.parseInt(value);
            if (retVal == 0) retVal = defaultValue;
        } catch (Exception e) {
            logger.debug(e);
        }
        return retVal;
    }

    /**
     * requires:
     * config parameter in request
     * valid projectContext in session
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        long start = System.currentTimeMillis();
        ProjectContext projectContext = (ProjectContext) request.getSession().getAttribute("projectContext");
        logger.debug("projectContext: " + projectContext.getProject().getProjectName());
        String config = request.getParameter("config");
        String mdx = request.getParameter("mdx");
        Analysis analysis = null;
        if (config != null && config.length() > 0) {
            logger.debug("found a config param, restoring analysis from file: " + config);
            analysis = restoreAnalysis(projectContext, config);
        } else if (mdx != null && mdx.length() > 0) {
            logger.debug("found a mdx param, generating analysis on the fly using mdx: " + mdx);
            String targetDatasource = request.getParameter("targetDatasource");
            String analysisName = request.getParameter("analysisName");
            analysis = new Analysis();
            analysis.setAnalysisTitle(analysisName);
            analysis.setDataSourceName(targetDatasource);
            analysis.setMdxQuery(mdx);
        }
        if (analysis == null) {
            try {
                response.sendRedirect("images/spacer.gif");
            } catch (IOException e) {
                logger.error(e);
            } finally {
                return;
            }
        }
        String datasourceName = analysis.getDataSourceName();
        logger.debug("using datasource name: " + datasourceName);
        Datasource datasource = projectContext.getDatasource(datasourceName);
        OlapModel olapModel = getDataset(datasource.getServer(), datasource.getCatalog(), analysis);
        try {
            int x = this.parseInt(request.getParameter("width"), 320);
            int y = this.parseInt(request.getParameter("height"), 240);
            EnhancedChartFactory.createChart(response.getOutputStream(), analysis, olapModel, x, y, request.getLocale());
        } catch (FileNotFoundException e) {
            throw new ServletException(e);
        } catch (IOException e) {
            throw new ServletException(e);
        } catch (OlapException e) {
            throw new ServletException(e);
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            logger.info("request completed in " + elapsed + "ms");
        }
    }

    /**
     * trap all exception? 
     * @param projectContext
     * @param analysisName
     * @return
     * @throws ServletException
     */
    private Analysis restoreAnalysis(ProjectContext projectContext, String analysisName) throws ServletException {
        Analysis analysis = null;
        try {
            analysis = projectContext.restoreAnalysis(analysisName);
        } catch (IOException e) {
        }
        return analysis;
    }

    /**
     * might want to trap all exceptions? 
     * @param xmlaUri
     * @param catalog
     * @param analysis
     * @return
     * @throws ServletException
     */
    private OlapModel getDataset(String xmlaUri, String catalog, Analysis analysis) throws ServletException {
        XmlaConnector connector = new XmlaConnector();
        String mdxQuery = analysis.getMdxQuery();
        OlapModel model = null;
        ;
        try {
            Datasource datasource = new Datasource(xmlaUri, catalog);
            model = connector.query(datasource, mdxQuery);
        } catch (IOException e) {
            throw new ServletException(e);
        } catch (OlapException e) {
            throw new ServletException(e);
        } catch (SAXException e) {
            throw new ServletException(e);
        }
        return model;
    }
}
