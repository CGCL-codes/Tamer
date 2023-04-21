package net.sourceforge.modelintegra.integrations.umltool.md.plugin.importdata;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.modelintegra.core.configuration.ConfigurationHelper;
import net.sourceforge.modelintegra.core.data.DataSingleton;
import net.sourceforge.modelintegra.core.metamodel.mimodel.Component;
import net.sourceforge.modelintegra.core.metamodel.mimodel.UC;
import net.sourceforge.modelintegra.core.metamodel.mimodel.UCControlFlow;
import net.sourceforge.modelintegra.core.metamodel.mimodel.UCFlow;
import net.sourceforge.modelintegra.core.metamodel.mimodel.UCTestCasePath;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.export.image.ImageExporter;
import com.nomagic.magicdraw.uml.symbols.DiagramPresentationElement;
import com.nomagic.magicdraw.uml.symbols.PresentationElement;
import com.nomagic.magicdraw.uml.symbols.paths.ControlFlowView;
import com.nomagic.uml2.ext.jmi.collections.UnionModelSet;
import com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ControlFlow;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Diagram;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class MDTestCaseDiagramGenerator {

    private static final Logger LOGGER = Logger.getLogger(MDTestCaseDiagramGenerator.class.getName());

    private static Application app = null;

    private static Project myProj = null;

    private static void exportDiagramOfTestCase(UCTestCasePath pUCTestCasePath) {
        LOGGER.setLevel(Level.WARN);
        LOGGER.debug(myProj.getTitle());
        EList listUCControlFlow = pUCTestCasePath.getPath();
        UCFlow aUCFlow = pUCTestCasePath.getUcflow();
        Element tmpActivity = (Element) myProj.getElementByID(aUCFlow.getToolId());
        LOGGER.debug(tmpActivity.toString());
        UnionModelSet childrenOfActivity = (UnionModelSet) tmpActivity.getOwnedElement();
        LOGGER.debug("childrenOfActivity Size:" + childrenOfActivity.size());
        Iterator iter = childrenOfActivity.iterator();
        while (iter.hasNext()) {
            Element el = (Element) iter.next();
            if (el instanceof Diagram) {
                LOGGER.debug("Flow Diagram found");
                Diagram oDia = (Diagram) el;
                DiagramPresentationElement dpe = Project.getProject(oDia).getDiagram(oDia);
                LOGGER.debug("tmpFlow: " + el.getHumanName() + " ID:" + el.getID());
                List col = new ArrayList();
                dpe.getContainer().collectSubManipulatedElements(col);
                LOGGER.debug("col: " + col.size());
                for (int i = 0; i < col.size(); i++) {
                    PresentationElement pe = (PresentationElement) col.get(i);
                    LOGGER.debug("pe: " + pe.getClassType().toString());
                    if (pe instanceof ControlFlowView) {
                        ControlFlowView cfv = (ControlFlowView) pe;
                        ControlFlow oCF = (ControlFlow) cfv.getElement();
                        LOGGER.debug("CF ID:" + oCF.getID());
                        Iterator iterUCF = listUCControlFlow.iterator();
                        while (iterUCF.hasNext()) {
                            UCControlFlow oUCF = (UCControlFlow) iterUCF.next();
                            if (oCF.getID().equals(oUCF.getToolId())) {
                                LOGGER.debug("Control Flow found - marked green");
                                cfv.setLineColor(Color.green);
                            }
                        }
                    }
                }
                File diagramFile = new File(ConfigurationHelper.INTERNAL_REPORT_OUTPUT_DIRECTORY_CONTENT + aUCFlow.getUc().getToolId() + "_tc_" + pUCTestCasePath.getNo() + ".png");
                try {
                    ImageExporter.export(dpe, ImageExporter.PNG, diagramFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < col.size(); i++) {
                    PresentationElement pe = (PresentationElement) col.get(i);
                    if (pe instanceof ControlFlowView) {
                        ControlFlowView cfv = (ControlFlowView) pe;
                        ControlFlow oCF = (ControlFlow) cfv.getElement();
                        if (oCF.getName().equals("N1")) {
                            cfv.setLineColor(Color.red);
                        } else {
                            cfv.setLineColor(Color.black);
                        }
                    }
                }
            }
        }
    }

    private static void iterate() {
        DataSingleton ds = DataSingleton.getInstance();
        Iterator iterComponents = ds.getMimodel().getComponent().iterator();
        while (iterComponents.hasNext()) {
            Component aComponent = (Component) iterComponents.next();
            Iterator iterUC = aComponent.getUc().iterator();
            while (iterUC.hasNext()) {
                UC aUC = (UC) iterUC.next();
                UCFlow aUCFlow = aUC.getUcflow();
                if (aUCFlow != null) {
                    Iterator iterTestCases = aUCFlow.getUctestcasepath().iterator();
                    while (iterTestCases.hasNext()) {
                        UCTestCasePath ucTestCasePath = (UCTestCasePath) iterTestCases.next();
                        exportDiagramOfTestCase(ucTestCasePath);
                    }
                }
            }
        }
    }

    public static void run() {
        if (ConfigurationHelper.TESTCASE_GENERATION_EXPORTDIAGRAMS) {
            LOGGER.info("Start: Generating Test-Case Diagrams");
            app = Application.getInstance();
            myProj = app.getProject();
            iterate();
            LOGGER.info("End: Generating Test-Case Diagrams");
        }
    }
}
