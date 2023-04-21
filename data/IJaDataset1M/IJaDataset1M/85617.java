package com.ixora.rms.ui.artefacts.dataview.wizard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;
import com.ixora.rms.ResourceId;
import com.ixora.common.ui.wizard.Wizard;
import com.ixora.common.ui.wizard.WizardStep;
import com.ixora.common.utils.Utils;
import com.ixora.rms.agents.impl.Counter;
import com.ixora.rms.client.model.SessionModel;
import com.ixora.rms.dataengine.definitions.FunctionDef;
import com.ixora.rms.dataengine.definitions.QueryDef;
import com.ixora.rms.dataengine.definitions.ResourceDef;
import com.ixora.rms.exception.RMSException;
import com.ixora.rms.repository.DataView;
import com.ixora.rms.ui.SessionTreeExplorer;
import com.ixora.rms.ui.artefacts.dataview.wizard.exception.NoResourcesDefined;
import com.ixora.rms.ui.dataviewboard.charts.definitions.ChartDef;
import com.ixora.rms.ui.dataviewboard.charts.definitions.RendererDef;
import com.ixora.rms.ui.dataviewboard.properties.definitions.PropertiesDef;
import com.ixora.rms.ui.dataviewboard.tables.definitions.TableDef;

/**
 * @author Daniel Moraru
 */
public class WizardStep4 extends WizardStep {

    private static final long serialVersionUID = -3128515942959621294L;

    private EntityRegexQueryDefPanel fEntityRegexPanel;

    private DistinctQueryDefPanel fDistinctPanel;

    private HostRegexQueryDefPanel fHostRegexPanel;

    private ResourceId fContext;

    private SessionModel fModel;

    private Wizard fWizard;

    private SessionTreeExplorer fExplorer;

    private WizardStep3.Data fStep3;

    public WizardStep4(Wizard wizard, ResourceId context, SessionModel model, SessionTreeExplorer explorer) {
        super("Data View Definition", "Set the properties for this data view");
        setLayout(new BorderLayout());
        fContext = context;
        fModel = model;
        fWizard = wizard;
        fExplorer = explorer;
        setPreferredSize(new Dimension(650, 450));
    }

    /**
	 * @see com.ixora.common.ui.wizard.WizardStep#activate(java.lang.Object)
	 */
    public void activate(Object obj) throws Exception {
        fStep3 = (WizardStep3.Data) obj;
        if (fDistinctPanel != null) {
            remove(fDistinctPanel);
        }
        if (fHostRegexPanel != null) {
            remove(fHostRegexPanel);
        }
        if (fEntityRegexPanel != null) {
            remove(fEntityRegexPanel);
        }
        if (fStep3.fStep2.fStep1.fType == WizardStep1.Data.Type.ENTITY_REGEX) {
            if (fEntityRegexPanel == null) {
                fEntityRegexPanel = new EntityRegexQueryDefPanel(fWizard, fModel, fContext, fExplorer);
            }
            add(fEntityRegexPanel, BorderLayout.CENTER);
        } else if (fStep3.fStep2.fStep1.fType == WizardStep1.Data.Type.DISTINCT) {
            if (fDistinctPanel == null) {
                fDistinctPanel = new DistinctQueryDefPanel(fWizard, fModel, fContext, fExplorer);
            }
            add(fDistinctPanel, BorderLayout.CENTER);
        } else if (fStep3.fStep2.fStep1.fType == WizardStep1.Data.Type.HOST_REGEX) {
            if (fHostRegexPanel == null) {
                fHostRegexPanel = new HostRegexQueryDefPanel(fWizard, fModel, fContext, fExplorer);
            }
            add(fHostRegexPanel, BorderLayout.CENTER);
        }
    }

    /**
	 * @see com.ixora.common.ui.wizard.WizardStep#deactivate()
	 */
    public Object deactivate() throws Exception {
        if (fStep3.fStep2.fStep1.fType == WizardStep1.Data.Type.ENTITY_REGEX) {
            return createDataViewEntityRegex();
        } else if (fStep3.fStep2.fStep1.fType == WizardStep1.Data.Type.DISTINCT) {
            return createDataViewDistinct();
        } else if (fStep3.fStep2.fStep1.fType == WizardStep1.Data.Type.HOST_REGEX) {
            return createDataViewHostRegex();
        }
        return null;
    }

    /**
	 * @return
	 * @throws RMSException
	 */
    private DataView createDataViewDistinct() throws RMSException {
        QueryDef qdef = createQueryDefDistinct();
        if (qdef != null) {
            if (fStep3.fStep2.fControlType == WizardStep2.Data.ControlType.PROPERTIES) {
                PropertiesDef pdef = new PropertiesDef(fStep3.fName, fStep3.fDescription, qdef, fStep3.fAuthor);
                pdef.setAgentVersions(fStep3.fAgentVersions);
                return pdef;
            } else if (fStep3.fStep2.fControlType == WizardStep2.Data.ControlType.CHART_TIMESERIES) {
                List<RendererDef> rdefs = createChartRenderersDistinct(qdef);
                if (rdefs != null) {
                    ChartDef chart = new ChartDef(fStep3.fName, fStep3.fDescription, qdef, rdefs, fStep3.fAuthor);
                    chart.setAgentVersions(fStep3.fAgentVersions);
                    return chart;
                }
            }
        }
        return null;
    }

    /**
	 * @return
	 * @throws RMSException
	 */
    private DataView createDataViewEntityRegex() throws RMSException {
        QueryDef qdef = null;
        if (fStep3.fStep2.fControlType == WizardStep2.Data.ControlType.CHART_BAR || fStep3.fStep2.fControlType == WizardStep2.Data.ControlType.CHART_TIMESERIES) {
            qdef = createQueryDefEntityRegex();
            if (qdef != null) {
                List<RendererDef> rdefs = createChartRenderersEntityRegex(qdef);
                if (rdefs != null) {
                    ChartDef chart = new ChartDef(fStep3.fName, fStep3.fDescription, qdef, rdefs, fStep3.fAuthor);
                    chart.setAgentVersions(fStep3.fAgentVersions);
                    return chart;
                }
            }
        } else if (fStep3.fStep2.fControlType == WizardStep2.Data.ControlType.TABLE) {
            qdef = createQueryDefEntityRegex();
            if (qdef != null) {
                String catId = getCategoryIdForRegex(qdef);
                List<String> cols = getRangesIdsForRegex(qdef);
                TableDef table = new TableDef(catId, cols, fStep3.fName, fStep3.fDescription, qdef, fStep3.fAuthor);
                table.setAgentVersions(fStep3.fAgentVersions);
                return table;
            }
        }
        return null;
    }

    /**
	 * @return
	 * @throws RMSException
	 */
    private DataView createDataViewHostRegex() throws RMSException {
        QueryDef qdef = null;
        if (fStep3.fStep2.fControlType == WizardStep2.Data.ControlType.TABLE) {
            qdef = createQueryDefHostRegex();
            if (qdef != null) {
                String catId = getCategoryIdForRegex(qdef);
                List<String> cols = getRangesIdsForRegex(qdef);
                TableDef table = new TableDef(catId, cols, fStep3.fName, fStep3.fDescription, qdef, fStep3.fAuthor);
                table.setAgentVersions(fStep3.fAgentVersions);
                return table;
            }
        } else if (fStep3.fStep2.fControlType == WizardStep2.Data.ControlType.CHART_BAR || fStep3.fStep2.fControlType == WizardStep2.Data.ControlType.CHART_TIMESERIES) {
            qdef = createQueryDefHostRegex();
            if (qdef != null) {
                List<RendererDef> rdefs = createChartRenderersHostRegex(qdef);
                if (rdefs != null) {
                    ChartDef chart = new ChartDef(fStep3.fName, fStep3.fDescription, qdef, rdefs, fStep3.fAuthor);
                    chart.setAgentVersions(fStep3.fAgentVersions);
                    return chart;
                }
            }
        }
        return null;
    }

    /**
	 * @param qdef
	 * @return
	 * @throws RMSException
	 */
    private List<RendererDef> createChartRenderersEntityRegex(QueryDef qdef) throws RMSException {
        List<RendererDef> rendef = new LinkedList<RendererDef>();
        List<String> rangeIDs = getRangesIdsForRegex(qdef);
        String domainID = getCategoryIdForRegex(qdef);
        RendererDef rdef = new RendererDef(fStep3.fStep2.fChartRenderer.getRenderer(), domainID, rangeIDs);
        rendef.add(rdef);
        return rendef;
    }

    /**
	 * @param qdef
	 * @return
	 * @throws RMSException
	 */
    private List<RendererDef> createChartRenderersHostRegex(QueryDef qdef) throws RMSException {
        List<ResourceDef> rdefs = qdef.getResources();
        checkForResourcesForRegex(rdefs);
        List<RendererDef> rendef = new LinkedList<RendererDef>();
        List<String> rangeIDs = getRangesIdsForRegex(qdef);
        String domainID = getCategoryIdForRegex(qdef);
        RendererDef rdef = new RendererDef(fStep3.fStep2.fChartRenderer.getRenderer(), domainID, rangeIDs);
        rendef.add(rdef);
        return rendef;
    }

    /**
	 * @param rdefs
	 * @throws NoResourcesDefined
	 */
    private void checkForResourcesForRegex(List<ResourceDef> rdefs) throws NoResourcesDefined {
        if (rdefs == null || rdefs.size() < 2) {
            throw new NoResourcesDefined();
        }
    }

    /**
	 * @param qdef
	 * @return
	 * @throws RMSException
	 */
    private List<RendererDef> createChartRenderersDistinct(QueryDef qdef) throws RMSException {
        if (fStep3.fStep2.fControlType == WizardStep2.Data.ControlType.CHART_TIMESERIES) {
            List<ResourceDef> rdefs = qdef.getResources();
            if (!Utils.isEmptyCollection(rdefs)) {
                List<RendererDef> rendef = new LinkedList<RendererDef>();
                List<String> rangeIDs = new LinkedList<String>();
                String domainID = null;
                for (ResourceDef rdef : rdefs) {
                    ResourceId rid = rdef.getResourceId();
                    if (rid.getRelativeRepresentation() == ResourceId.COUNTER && rid.getCounterId().equals(Counter.TIMESTAMP_ID)) {
                        domainID = rdef.getID();
                    } else {
                        rangeIDs.add(rdef.getID());
                    }
                }
                List<FunctionDef> fdefs = qdef.getFunctions();
                if (!Utils.isEmptyCollection(fdefs)) {
                    for (FunctionDef fdef : fdefs) {
                        rangeIDs.add(fdef.getID());
                    }
                }
                RendererDef rdef = new RendererDef(fStep3.fStep2.fChartRenderer.getRenderer(), domainID, rangeIDs);
                rendef.add(rdef);
                return rendef;
            }
        }
        return null;
    }

    /**
	 * @param property
	 * @return
	 * @throws RMSException
	 */
    private QueryDef createQueryDefDistinct() throws RMSException {
        List<ResourceDef> rdefs = fDistinctPanel.getResources();
        if (Utils.isEmptyCollection(rdefs)) {
            throw new NoResourcesDefined();
        }
        if (fStep3.fStep2.fControlType == WizardStep2.Data.ControlType.PROPERTIES) {
            ;
        } else if (fStep3.fStep2.fControlType == WizardStep2.Data.ControlType.CHART_TIMESERIES) {
            ResourceDef first = rdefs.get(0);
            ResourceId firstId = first.getResourceId();
            ResourceId time = new ResourceId(firstId.getHostId(), firstId.getAgentId(), firstId.getEntityId(), Counter.TIMESTAMP_ID);
            ResourceDef timeDef = new ResourceDef(time);
            timeDef.setID("time");
            timeDef.setIName(null);
            rdefs.add(timeDef);
        }
        QueryDef qdef = new QueryDef(fStep3.fName, rdefs, fDistinctPanel.getFunctions(), fDistinctPanel.getReactions());
        return qdef;
    }

    /**
	 * @return
	 * @throws RMSException
	 */
    private QueryDef createQueryDefEntityRegex() throws RMSException {
        List<ResourceDef> rdefs = fEntityRegexPanel.getResources();
        checkForResourcesForRegex(rdefs);
        if (fStep3.fStep2.fControlType == WizardStep2.Data.ControlType.CHART_TIMESERIES) {
            ResourceDef cat = rdefs.get(rdefs.size() - 1);
            ResourceId catId = cat.getResourceId();
            ResourceId time = new ResourceId(catId.getHostId(), catId.getAgentId(), catId.getEntityId(), Counter.TIMESTAMP_ID);
            ResourceDef timeDef = new ResourceDef(time);
            timeDef.setID("time");
            timeDef.setIName(null);
            rdefs.remove(rdefs.size() - 1);
            rdefs.add(timeDef);
        }
        QueryDef qdef = new QueryDef(fStep3.fName, rdefs, fEntityRegexPanel.getFunctions(), fEntityRegexPanel.getReactions());
        return qdef;
    }

    /**
	 * @return
	 * @throws RMSException
	 */
    private QueryDef createQueryDefHostRegex() throws RMSException {
        List<ResourceDef> rdefs = fHostRegexPanel.getResources();
        checkForResourcesForRegex(rdefs);
        if (fStep3.fStep2.fControlType == WizardStep2.Data.ControlType.CHART_TIMESERIES) {
            ResourceDef rd = rdefs.get(0);
            ResourceId rid = rd.getResourceId();
            ResourceId time = new ResourceId(rid.getHostId(), rid.getAgentId(), rid.getEntityId(), Counter.TIMESTAMP_ID);
            ResourceDef timeDef = new ResourceDef(time);
            timeDef.setID("time");
            timeDef.setIName(null);
            rdefs.remove(rdefs.size() - 1);
            rdefs.add(timeDef);
        }
        QueryDef qdef = new QueryDef(fStep3.fName, rdefs, fHostRegexPanel.getFunctions(), fHostRegexPanel.getReactions());
        return qdef;
    }

    /**
	 * @param qdef
	 * @return
	 */
    private String getCategoryIdForRegex(QueryDef qdef) {
        List<ResourceDef> rdefs = qdef.getResources();
        ResourceDef rdef = rdefs.get(rdefs.size() - 1);
        return rdef.getID();
    }

    /**
	 * @param qdef
	 * @return
	 * @throws NoResourcesDefined
	 */
    private List<String> getRangesIdsForRegex(QueryDef qdef) throws NoResourcesDefined {
        List<String> rangeIDs = new LinkedList<String>();
        @SuppressWarnings("unused") String domainID = null;
        List<ResourceDef> rdefs = qdef.getResources();
        checkForResourcesForRegex(rdefs);
        int size = rdefs.size();
        for (int i = 0; i < size; i++) {
            if (i < size - 1) {
                rangeIDs.add(rdefs.get(i).getID());
            } else {
                domainID = rdefs.get(i).getID();
            }
        }
        List<FunctionDef> fdefs = qdef.getFunctions();
        if (!Utils.isEmptyCollection(fdefs)) {
            for (FunctionDef def : fdefs) {
                rangeIDs.add(def.getID());
            }
        }
        return rangeIDs;
    }
}
