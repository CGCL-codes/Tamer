package net.sourceforge.modelintegra.core.reporting.quick;

import java.util.Iterator;
import net.sourceforge.modelintegra.core.configuration.ConfigurationHelper;
import net.sourceforge.modelintegra.core.data.DataSingleton;
import net.sourceforge.modelintegra.core.metamodel.mimodel.Component;
import net.sourceforge.modelintegra.core.metamodel.mimodel.Entity;
import net.sourceforge.modelintegra.core.metamodel.mimodel.MIDiagram;
import net.sourceforge.modelintegra.core.metamodel.mimodel.MIEnumeration;
import net.sourceforge.modelintegra.core.reporting.ReportContainer;
import net.sourceforge.modelintegra.core.reporting.ReportContainer.G;
import net.sourceforge.modelintegra.core.reporting.snippet.CommonSnippets;
import net.sourceforge.modelintegra.core.reporting.snippet.ContentSnippets;
import net.sourceforge.modelintegra.core.reporting.util.contentexplorer.ContentBuilder;
import net.sourceforge.modelintegra.core.reporting.util.contentexplorer.ContentElement;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;

/**
 * Generating Quick Diagram Report
 */
public class GenerateQuickReportContent {

    private static final Logger LOGGER = Logger.getLogger(GenerateQuickReportContent.class.getName());

    private static DataSingleton ds = DataSingleton.getInstance();

    private static ReportContainer rc = ReportContainer.getInstance();

    private static ContentBuilder contentBuilder;

    private static ContentElement ceRoot;

    private static ContentElement ceModel;

    private static ContentElement ceComponents;

    private static ContentElement ceRoughDocuments;

    private static ContentElement ceRoughDocumentComponents;

    private static ContentElement ceRoughDocumentSubprojects;

    private static ContentElement ceDetailedDocuments;

    private static ContentElement ceDetailedDocumentComponents;

    private static ContentElement ceDetailedDocumentSubprojects;

    private static ContentElement ceTestCases;

    private static ContentElement ceMetrics;

    private static ContentElement ceCostEstimation;

    private static ContentElement ceTreaceability;

    private static ContentElement ceGlossary;

    private static ContentElement ceQuick;

    private static ContentElement ceReport;

    private static void iterateComponent() {
        if (ConfigurationHelper.REPORT_TYPE == ConfigurationHelper.ReportTypeEnum.QRCLASSDIAGRAM) {
            ceReport = ceQuick.addContentElement("Class Diagram Report", "qrclassdiagram.doc", false, ContentElement.TYPE.DOCUMENT);
            ceReport.setLink2("qrclassdiagram" + ".html");
            ceReport.setType2(ContentElement.TYPE.HTML);
        } else if (ConfigurationHelper.REPORT_TYPE == ConfigurationHelper.ReportTypeEnum.QRDIAGRAMSONLY) {
            ceReport = ceQuick.addContentElement("Diagrams Report", "qrdiagrams.doc", false, ContentElement.TYPE.DOCUMENT);
            ceReport.setLink2("qrdiagrams" + ".html");
            ceReport.setType2(ContentElement.TYPE.HTML);
        }
        EList listComponent = ds.getMimodel().getComponent();
        Iterator iterComponent = listComponent.iterator();
        while (iterComponent.hasNext()) {
            Component aComponent = (Component) iterComponent.next();
            reportComponent(aComponent);
        }
    }

    private static void reportComponent(Component pComponent) {
        rc.addElement(pComponent, CommonSnippets.printComponent(pComponent));
        Iterator iterDiagrams = pComponent.getDiagram().iterator();
        while (iterDiagrams.hasNext()) {
            MIDiagram aDia = (MIDiagram) iterDiagrams.next();
            rc.addElement(aDia, CommonSnippets.printComponentDiagram(aDia));
        }
        if (ConfigurationHelper.REPORT_TYPE == ConfigurationHelper.ReportTypeEnum.QRCLASSDIAGRAM) {
            iterateEntities(pComponent);
            iterateEnumerations(pComponent);
        }
    }

    private static void iterateEntities(Component pComponent) {
        EList listEntities = pComponent.getEntity();
        Iterator iterEntities = listEntities.iterator();
        while (iterEntities.hasNext()) {
            Entity aEntity = (Entity) iterEntities.next();
            if (ds.hClassesContainedInDiagrams.containsKey(aEntity.getToolId())) {
                reportEntities(pComponent, aEntity);
            }
        }
    }

    private static void reportEntities(Component pComponent, Entity pEntity) {
        String report = CommonSnippets.printEntity(pEntity, G.DETAILED);
        rc.addElement(pEntity, report);
    }

    private static void iterateEnumerations(Component pComponent) {
        EList listEnumerations = pComponent.getEnumeration();
        Iterator iterEnumerations = listEnumerations.iterator();
        while (iterEnumerations.hasNext()) {
            MIEnumeration aEnumeration = (MIEnumeration) iterEnumerations.next();
            if (ds.hClassesContainedInDiagrams.containsKey(aEnumeration.getToolId())) {
                reportEnumerations(pComponent, aEnumeration);
            }
        }
    }

    private static void reportEnumerations(Component pComponent, MIEnumeration pMIEnumeration) {
        String report = CommonSnippets.printEnumeration(pMIEnumeration);
        rc.addElement(pMIEnumeration, report);
    }

    private static void initContentTree() {
        contentBuilder.init();
        ceRoot = contentBuilder.getRootElement();
        ceModel = ceRoot.addContentElement("Modell", "nolink", true, ContentElement.TYPE.NONE);
        ceTestCases = ceRoot.addContentElement("Test Cases", "nolink", true, ContentElement.TYPE.NONE);
        ceMetrics = ceRoot.addContentElement("Metriken", "nolink", true, ContentElement.TYPE.NONE);
        ceCostEstimation = ceRoot.addContentElement("Aufwands- / Kostensch�tzung", "nolink", true, ContentElement.TYPE.NONE);
        ceTreaceability = ceRoot.addContentElement("Traceability / R�ckverfolgbarkeit", "nolink", true, ContentElement.TYPE.NONE);
        ceRoughDocuments = ceRoot.addContentElement("Fachgrobkonzepte", "nolink", true, ContentElement.TYPE.NONE);
        ceDetailedDocuments = ceRoot.addContentElement("Fachfeinkonzepte", "nolink", true, ContentElement.TYPE.NONE);
        ceGlossary = ceRoot.addContentElement("Glossar", "nolink", true, ContentElement.TYPE.NONE);
        ceQuick = ceRoot.addContentElement("Quick Reports", "nolink", true, ContentElement.TYPE.NONE);
        ceQuick.setMargintop(true);
        ceQuick.setOpened(true);
    }

    public static void generate() {
        initContentTree();
        iterateComponent();
        ContentSnippets.reportDocGeneration();
    }
}
