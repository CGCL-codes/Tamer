package at.jku.semwiq.webapp.ice.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.jku.rdfstats.RDFStatsDataset;
import at.jku.rdfstats.RDFStatsModel;
import at.jku.rdfstats.RDFStatsModelException;
import at.jku.rdfstats.hist.ComparableDomainHistogram;
import at.jku.rdfstats.html.GenerateHTML;
import at.jku.semwiq.mediator.Mediator;
import at.jku.semwiq.mediator.registry.DataSourceRegistry;
import at.jku.semwiq.mediator.registry.model.DataSource;
import at.jku.semwiq.webapp.ice.model.Datasource;
import at.jku.semwiq.webapp.ice.model.WebAppHistogram;
import at.jku.semwiq.webapp.ice.model.WebAppRessource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;

/**
 * this class represents the handler for the jspx-document (semwiq_information.jspx) and handles 
 * the data needed for the content.
 * @author thomas
 *
 */
public class InformationHandler {

    private static final Logger log = LoggerFactory.getLogger(InformationHandler.class);

    private List datasourceList;

    private Datasource currentDatasource;

    private String semwiq_informationLastDownload;

    private boolean renderHistograms;

    private SemWIQHandler semwiqHandler;

    public void setSemwiqHandler(SemWIQHandler semwiqHandler) {
        this.semwiqHandler = semwiqHandler;
    }

    public InformationHandler() {
        loadDatasourceList();
        this.setRenderHistograms(false);
    }

    public String getSemwiq_informationState() {
        if (currentDatasource != null) return currentDatasource.getSemwiq_informationState(); else return "";
    }

    public void setSemwiq_informationState(String semwiq_informationState) {
        currentDatasource.setSemwiq_informationState(semwiq_informationState);
    }

    public String getSemwiq_informationURI() {
        if (currentDatasource != null) return currentDatasource.getSemwiq_informationURI(); else return "";
    }

    public void setSemwiq_informationURI(String semwiq_informationURI) {
        currentDatasource.setSemwiq_informationURI(semwiq_informationURI);
    }

    public String getSemwiq_informationURILabel() {
        if (currentDatasource != null) return currentDatasource.getSemwiq_informationURILabel(); else return "";
    }

    public void setSemwiq_informationURILabel(String semwiq_informationURILabel) {
        currentDatasource.setSemwiq_informationURILabel(semwiq_informationURILabel);
    }

    public String getSemwiq_informationEndpoint() {
        if (currentDatasource != null) return currentDatasource.getSemwiq_informationEndpoint(); else return "";
    }

    public void setSemwiq_informationEndpoint(String semwiq_informationEndpoint) {
        currentDatasource.setSemwiq_informationEndpoint(semwiq_informationEndpoint);
    }

    public String getSemwiq_informationEndpointLabel() {
        if (currentDatasource != null) return currentDatasource.getSemwiq_informationEndpointLabel(); else return "";
    }

    public void setSemwiq_informationEndpointLabel(String semwiq_informationEndpointLabel) {
        currentDatasource.setSemwiq_informationEndpointLabel(semwiq_informationEndpointLabel);
    }

    public String getSemwiq_informationProviderType() {
        if (currentDatasource != null && currentDatasource.getSemwiq_informationProviderType() != null) return currentDatasource.getSemwiq_informationProviderType().getLocalName(); else return "";
    }

    public String getSemwiq_informationProviderTypeURI() {
        if (currentDatasource != null && currentDatasource.getSemwiq_informationProviderType() != null) return currentDatasource.getSemwiq_informationProviderType().getURI(); else return "";
    }

    public void setSemwiq_informationProviderType(Resource type) {
        currentDatasource.setSemwiq_informationProviderType(type);
    }

    public String getSemwiq_informationProviderName() {
        if (currentDatasource != null) return currentDatasource.getSemwiq_informationProviderName(); else return "";
    }

    public void setSemwiq_informationProviderName(String semwiq_informationProviderName) {
        currentDatasource.setSemwiq_informationProviderName(semwiq_informationProviderName);
    }

    public String getSemwiq_informationMaintainer() {
        if (currentDatasource != null) return currentDatasource.getSemwiq_informationMaintainer(); else return "";
    }

    public void setSemwiq_informationMaintainer(String semwiq_informationMaintainer) {
        currentDatasource.setSemwiq_informationMaintainer(semwiq_informationMaintainer);
    }

    public String getSemwiq_informationStatDate() {
        if (currentDatasource != null) return currentDatasource.getSemwiq_informationStatDate(); else return "";
    }

    public void setSemwiq_informationStatDate(String semwiq_informationStatDate) {
        currentDatasource.setSemwiq_informationStatDate(semwiq_informationStatDate);
    }

    public String getSemwiq_informationMonitoringProfile() {
        if (currentDatasource != null) return currentDatasource.getSemwiq_informationMonitoringProfile(); else return "";
    }

    public void setSemwiq_informationMonitoringProfile(String semwiq_informationMonitoringProfile) {
        currentDatasource.setSemwiq_informationMonitoringProfile(semwiq_informationMonitoringProfile);
    }

    public String getSemwiq_informationMaintainerSameAs() {
        if (currentDatasource != null) return currentDatasource.getSemwiq_informationMaintainerSameAs(); else return "";
    }

    public void setSemwiq_informationMaintainerSameAs(String semwiq_informationMaintainerSameAs) {
        currentDatasource.setSemwiq_informationMaintainerSameAs(semwiq_informationMaintainerSameAs);
    }

    public String getSemwiq_informationLastDownload() {
        return semwiq_informationLastDownload;
    }

    public void setSemwiq_informationLastDownload(String semwiq_informationLastDownload) {
        this.semwiq_informationLastDownload = semwiq_informationLastDownload;
    }

    public Datasource getCurrentDatasource() {
        return currentDatasource;
    }

    public void setCurrentDatasource(Datasource currentDatasource) {
        this.currentDatasource = currentDatasource;
    }

    public List getDatasourceList() {
        return datasourceList;
    }

    public boolean isRenderHistograms() {
        return renderHistograms;
    }

    public void setRenderHistograms(boolean renderHistograms) {
        this.renderHistograms = renderHistograms;
    }

    public List getDatasourceListItems() {
        List datasourceListItems = new ArrayList();
        for (Iterator iter = datasourceList.iterator(); iter.hasNext(); ) {
            Datasource ds = (Datasource) iter.next();
            DataSourceRegistry reg = semwiqHandler.getMediator().getDataSourceRegistry();
            try {
                DataSource semwiqDs = reg.getDataSourceByEndpointUri(ds.getSemwiq_informationEndpoint());
                datasourceListItems.add(new SelectItem(ds, semwiqDs.getSPARQLEndpointURL()));
            } catch (Exception e) {
                log.error("Failed to get data source for endpoint <" + ds.getSemwiq_informationEndpoint());
            }
        }
        return datasourceListItems;
    }

    public List getDatasourceWebappList() {
        if (currentDatasource != null) return currentDatasource.getResource(); else return new ArrayList();
    }

    public void loadDatasourceList() {
        datasourceList = new ArrayList();
        Mediator mediator = semwiqHandler.getMediator();
        if (mediator == null || !mediator.isReady()) {
            semwiqHandler.setErrorMessage("Mediator is currently offline or not available.");
        } else {
            RDFStatsModel rdfStatsModel = mediator.getDataSourceRegistry().getRDFStatsModel();
            RDFStatsDataset dataset;
            List<DataSource> regDatasourceList;
            try {
                regDatasourceList = mediator.getDataSourceRegistry().getRegisteredDataSources();
            } catch (Exception e) {
                log.error("Failed to get list of data sources.", e);
                return;
            }
            String dsStatus;
            Resource providerType = null;
            for (Iterator iter = regDatasourceList.iterator(); iter.hasNext(); ) {
                DataSource regDs = (DataSource) iter.next();
                Datasource webDs = new Datasource();
                if (!regDs.isEnabled()) {
                    dsStatus = "DISABLED";
                } else if (!regDs.isAvailable()) {
                    dsStatus = "OFFLINE";
                } else {
                    dsStatus = "ONLINE";
                }
                webDs.setSemwiq_informationState(dsStatus);
                webDs.setSemwiq_informationEndpoint(regDs.getSPARQLEndpointURL());
                webDs.setSemwiq_informationEndpointLabel(regDs.getSPARQLEndpointURL());
                webDs.setSemwiq_informationURI(regDs.getUri());
                webDs.setSemwiq_informationURILabel(regDs.getUri());
                try {
                    dataset = rdfStatsModel.getDataset(regDs.getSPARQLEndpointURL());
                    if (dataset != null) {
                        webDs.setSemwiq_informationStatDate(dataset.getDate().toString());
                    }
                } catch (RDFStatsModelException e) {
                    log.error(e.getMessage(), e);
                }
                datasourceList.add(webDs);
            }
        }
    }

    /**
	 * adds a datasource (given from the DatasourcePopup) to the list
	 * @param datasource
	 */
    public void addDatasource(Datasource datasource) {
        datasourceList.add(datasource);
    }

    /**
	 *  delestes a datasource from the list (catalog jspx) when clicked on th "-"-button
	 * @return
	 */
    public String deleteDatasource() {
        if (currentDatasource != null) {
            datasourceList.remove(currentDatasource);
            return "ds deleted";
        } else {
            return "no ds";
        }
    }

    public void effectChangeListener(ValueChangeEvent event) {
    }

    public List getHistograms() {
        List<WebAppHistogram> waHistograms = new ArrayList<WebAppHistogram>();
        FacesContext context = FacesContext.getCurrentInstance();
        Map map = context.getExternalContext().getRequestParameterMap();
        String classURI = (String) map.get("classuri");
        String endpointURI = getSemwiq_informationEndpoint();
        Model tmp = ModelFactory.createDefaultModel();
        Resource cl = tmp.createResource(classURI);
        return waHistograms;
    }

    public String updateDatasource() {
        if (currentDatasource != null) {
            try {
                DataSourceRegistry thisReg = semwiqHandler.getMediator().getDataSourceRegistry();
                DataSource tmp = thisReg.getDataSourceByEndpointUri(currentDatasource.getSemwiq_informationEndpoint());
                thisReg.getMonitor().triggerUpdate(tmp);
                return "success";
            } catch (Exception e) {
                log.error(e.getMessage());
                return "failed";
            }
        } else {
            return "failed";
        }
    }

    private int percentComplete;

    public int getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
    }

    public void run() {
        for (int i = 0; i <= 100; i += 10) {
            try {
                Thread.sleep(300);
                setPercentComplete(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String navigationInformation() {
        Mediator mediator = semwiqHandler.getMediator();
        if (mediator == null || !mediator.isReady()) {
            semwiqHandler.setErrorMessage("Mediator is currently offline or not available.");
            return "failure";
        } else {
            return "navigation_admin_catalog";
        }
    }

    public String renderHist() {
        this.setRenderHistograms(true);
        return "success";
    }

    public String getHistogram() {
        if (currentDatasource == null) return "";
        RDFStatsModel model = semwiqHandler.getMediator().getDataSourceRegistry().getRDFStatsModel();
        try {
            if (model.getDataset(currentDatasource.getSemwiq_informationEndpoint()) == null) return "";
            return GenerateHTML.generateHTML(model, currentDatasource.getSemwiq_informationEndpoint(), true);
        } catch (RDFStatsModelException e) {
            log.error("Failed to generate histogram for view.", e);
            return e.getMessage();
        }
    }
}
