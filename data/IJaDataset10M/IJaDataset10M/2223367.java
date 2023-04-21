package net.sourceforge.modelintegra.integrations.umltool.md.plugin.createmodel;

import java.util.ArrayList;
import java.util.Iterator;
import net.sourceforge.modelintegra.core.enumerations.TraceabilityType;
import net.sourceforge.modelintegra.core.metamodel.extension.util.MIModelHelper;
import net.sourceforge.modelintegra.core.metamodel.mimodel.TraceabilityElement;
import net.sourceforge.modelintegra.core.util.UtilityCommon;
import net.sourceforge.modelintegra.integrations.umltool.md.plugin.importdata.MDModelHelper;
import net.sourceforge.modelintegra.integrations.umltool.md.plugin.util.MDUtility;
import net.sourceforge.modelintegra.integrations.umltool.md.plugin.util.StereotypeUtility;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.openapi.uml.ModelElementsManager;
import com.nomagic.magicdraw.openapi.uml.ReadOnlyElementException;
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.uml2.ext.jmi.collections.UnionModelSet;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;
import com.nomagic.uml2.impl.ElementsFactory;

public class TraceabilityElement2Model {

    private static final Logger LOGGER = Logger.getLogger(TraceabilityElement2Model.class.getName());

    private static Element theTraceabilityElementPackage = null;

    private static Stereotype theTraceabilityElementStereotype = null;

    public static void run(int pTraceabilityType) {
        if (pTraceabilityType == TraceabilityType.REQ) {
            theTraceabilityElementPackage = MDModelHelper.getModelRequirementsPackage();
            theTraceabilityElementStereotype = StereotypeUtility.stereotypeRequirement;
        } else if (pTraceabilityType == TraceabilityType.TEST) {
            theTraceabilityElementPackage = MDModelHelper.getModelTestCasePackage();
            theTraceabilityElementStereotype = StereotypeUtility.stereotypeTestCase;
        } else if (pTraceabilityType == TraceabilityType.BPFunction) {
            theTraceabilityElementPackage = MDModelHelper.getModelBPPackage();
            theTraceabilityElementStereotype = StereotypeUtility.stereotypeBPFunction;
        } else {
            throw new RuntimeException();
        }
        ArrayList<String> listExistingTraceabilityElementString = new ArrayList<String>();
        ArrayList<Element> listExistingTraceabilityElement = new ArrayList<Element>();
        UnionModelSet ums = (UnionModelSet) theTraceabilityElementPackage.getOwnedElement();
        LOGGER.debug("Size: " + ums.size());
        Iterator itera = ums.iterator();
        while (itera.hasNext()) {
            Element aElement = (Element) itera.next();
            if (StereotypeUtility.hasStereotype(aElement, theTraceabilityElementStereotype)) {
                String aId = null;
                if (pTraceabilityType == TraceabilityType.REQ) {
                    aId = StereotypeUtility.getSlotRequirementReqId(aElement);
                } else if (pTraceabilityType == TraceabilityType.TEST) {
                    aId = StereotypeUtility.getSlotTestCaseId(aElement);
                } else if (pTraceabilityType == TraceabilityType.BPFunction) {
                    aId = StereotypeUtility.getSlotBPFunctionRepresentsElementWithID(aElement);
                }
                if (aId != null && aId.length() > 0) {
                    listExistingTraceabilityElementString.add(aId);
                    if (StereotypesHelper.canApplyStereotype(aElement, StereotypeUtility.stereotypeObsolete)) {
                        StereotypesHelper.addStereotype(aElement, StereotypeUtility.stereotypeObsolete);
                    } else {
                        LOGGER.error("stereotype not applicable!");
                    }
                    listExistingTraceabilityElement.add(aElement);
                }
            }
        }
        if (!SessionManager.getInstance().isSessionCreated()) {
            SessionManager.getInstance().createSession("newelement");
        }
        ElementsFactory f = Application.getInstance().getProject().getElementsFactory();
        EList listTraceabilityElement = null;
        if (pTraceabilityType == TraceabilityType.REQ) {
            listTraceabilityElement = MIModelHelper.getRequirementList();
        } else if (pTraceabilityType == TraceabilityType.TEST) {
            listTraceabilityElement = MIModelHelper.getTestCaseList();
        } else if (pTraceabilityType == TraceabilityType.BPFunction) {
            listTraceabilityElement = MIModelHelper.getBpfunctionList();
        }
        Iterator iter = listTraceabilityElement.iterator();
        while (iter.hasNext()) {
            TraceabilityElement te = (TraceabilityElement) iter.next();
            LOGGER.debug("Id:" + te.getId());
            if (!UtilityCommon.findStringInList(listExistingTraceabilityElementString, te.getId())) {
                LOGGER.debug("adding traceability element to model:" + te.getId());
                Class newClass1 = f.createClassInstance();
                String name = te.getName();
                if (name == null || name.length() == 0) name = te.getId();
                newClass1.setName(name);
                MDUtility.setFirstComment(newClass1, te.getDocumentation());
                StereotypesHelper.addStereotype(newClass1, theTraceabilityElementStereotype);
                if (pTraceabilityType == TraceabilityType.REQ) {
                    StereotypeUtility.setSlotRequirementReqId(newClass1, te.getId());
                } else if (pTraceabilityType == TraceabilityType.TEST) {
                    StereotypeUtility.setSlotTestCaseId(newClass1, te.getId());
                } else if (pTraceabilityType == TraceabilityType.BPFunction) {
                    StereotypeUtility.setSlotBPFunctionRepresentsElementWithID(newClass1, te.getId());
                }
                try {
                    ModelElementsManager.getInstance().addElement(newClass1, theTraceabilityElementPackage);
                } catch (ReadOnlyElementException e) {
                    LOGGER.error("model is read-only");
                    throw new RuntimeException();
                }
            } else {
                LOGGER.debug("traceability element already exists in model:" + te.getId());
                Iterator iterExist = listExistingTraceabilityElement.iterator();
                while (iterExist.hasNext()) {
                    Class newClass2 = (Class) iterExist.next();
                    String aId = null;
                    if (pTraceabilityType == TraceabilityType.REQ) {
                        aId = StereotypeUtility.getSlotRequirementReqId(newClass2);
                    } else if (pTraceabilityType == TraceabilityType.TEST) {
                        aId = StereotypeUtility.getSlotTestCaseId(newClass2);
                    } else if (pTraceabilityType == TraceabilityType.BPFunction) {
                        aId = StereotypeUtility.getSlotBPFunctionRepresentsElementWithID(newClass2);
                    }
                    if (aId.equals(te.getId()) && te.isFoundInSpec() == true) {
                        StereotypesHelper.addStereotype(newClass2, theTraceabilityElementStereotype);
                        StereotypesHelper.removeStereotype(newClass2, StereotypeUtility.stereotypeObsolete);
                        String name = te.getName();
                        if (name == null || name.length() == 0) name = te.getId();
                        newClass2.setName(name);
                        MDUtility.setFirstComment(newClass2, te.getDocumentation());
                    }
                }
            }
        }
        if (pTraceabilityType == TraceabilityType.REQ) {
            UpdateTraceabilityDiagram.update(TraceabilityType.REQ);
        } else if (pTraceabilityType == TraceabilityType.TEST) {
            UpdateTraceabilityDiagram.update(TraceabilityType.TEST);
        } else if (pTraceabilityType == TraceabilityType.BPFunction) {
            UpdateTraceabilityDiagram.update(TraceabilityType.BPFunction);
        }
        SessionManager.getInstance().closeSession();
    }
}
