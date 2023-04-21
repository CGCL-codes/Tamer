package org.argouml.uml.cognitive.critics;

import org.argouml.application.api.Argo;
import org.argouml.cognitive.Decision;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.critics.Critic;
import org.argouml.kernel.Project;
import org.argouml.ocl.OCLEvaluator;
import org.argouml.ui.ProjectBrowser;
import org.tigris.gef.util.VectorSet;
import ru.novosoft.uml.foundation.core.MElement;

/** "Abstract" Critic subclass that captures commonalities among all
 *  critics in the UML domain.  This class also defines and registers
 *  the categories of design decisions that the critics can
 *  address.
 *
 * @see org.argouml.cognitive.Designer
 * @see org.argouml.cognitive.DecisionModel
 */
public class CrUML extends Critic {

    public static final Decision decINHERITANCE = new Decision("decision.inheritance", 5);

    public static final Decision decCONTAINMENT = new Decision("decision.containment", 5);

    public static final Decision decPATTERNS = new Decision("decision.design-patterns", 5);

    public static final Decision decRELATIONSHIPS = new Decision("decision.relationships", 5);

    public static final Decision decSTORAGE = new Decision("decision.storage", 5);

    public static final Decision decBEHAVIOR = new Decision("decision.behavior", 5);

    public static final Decision decINSTANCIATION = new Decision("decision.instantiation", 5);

    public static final Decision decNAMING = new Decision("decision.naming", 5);

    public static final Decision decMODULARITY = new Decision("decision.modularity", 5);

    public static final Decision decCLASS_SELECTION = new Decision("decision.class-selection", 5);

    public static final Decision decEXPECTED_USAGE = new Decision("decision.expected-usage", 5);

    public static final Decision decMETHODS = new Decision("decision.methods", 5);

    public static final Decision decCODE_GEN = new Decision("decision.code-generation", 5);

    public static final Decision decPLANNED_EXTENSIONS = new Decision("decision.planned-extensions", 5);

    public static final Decision decSTEREOTYPES = new Decision("decision.stereotypes", 5);

    public static final Decision decSTATE_MACHINES = new Decision("decision.mstate-machines", 5);

    public static final String CRITICS_SITE = "http://www.ics.uci.edu/pub/arch/uml/critics/";

    /** Static initializer for this class. Called when the class is
   *  loaded (which is before any subclass instances are instanciated). */
    static {
        Designer d = Designer.theDesigner();
        d.startConsidering(decCLASS_SELECTION);
        d.startConsidering(decNAMING);
        d.startConsidering(decSTORAGE);
        d.startConsidering(decINHERITANCE);
        d.startConsidering(decCONTAINMENT);
        d.startConsidering(decPLANNED_EXTENSIONS);
        d.startConsidering(decSTATE_MACHINES);
        d.startConsidering(decPATTERNS);
        d.startConsidering(decRELATIONSHIPS);
        d.startConsidering(decINSTANCIATION);
        d.startConsidering(decMODULARITY);
        d.startConsidering(decEXPECTED_USAGE);
        d.startConsidering(decMETHODS);
        d.startConsidering(decCODE_GEN);
        d.startConsidering(decSTEREOTYPES);
    }

    public CrUML() {
    }

    public void setResource(String key) {
        String head = Argo.localize("Cognitive", key + "_head");
        super.setHeadline(head);
        String desc = Argo.localize("Cognitive", key + "_desc");
        super.setDescription(desc);
    }

    /**
   *   Will be deprecated in good time
   */
    public final void setHeadline(String s) {
        String className = getClass().getName();
        setResource(className.substring(className.lastIndexOf('.') + 1));
    }

    public boolean predicate(Object dm, Designer dsgr) {
        Project p = ProjectBrowser.TheInstance.getProject();
        if (p.isInTrash(dm)) {
            return NO_PROBLEM;
        } else return predicate2(dm, dsgr);
    }

    public boolean predicate2(Object dm, Designer dsgr) {
        return super.predicate(dm, dsgr);
    }

    private String _cachedMoreInfoURL = null;

    public String getMoreInfoURL(VectorSet offenders, Designer dsgr) {
        if (_cachedMoreInfoURL == null) {
            String clsName = getClass().getName();
            clsName = clsName.substring(clsName.lastIndexOf(".") + 1);
            _cachedMoreInfoURL = CRITICS_SITE + clsName + ".html";
        }
        return _cachedMoreInfoURL;
    }

    private static final String OCL_START = "<ocl>";

    private static final String OCL_END = "</ocl>";

    /**
     * Expand text with ocl brackets in it.
     * No recursive expansion.
     */
    public String expand(String res, VectorSet offs) {
        if (offs.size() == 0) return res;
        Object off1 = offs.firstElement();
        if (!(off1 instanceof MElement)) return res;
        StringBuffer beginning = new StringBuffer("");
        int matchPos = res.indexOf(OCL_START);
        while (matchPos != -1) {
            int endExpr = res.indexOf(OCL_END, matchPos + 1);
            if (endExpr == -1) break;
            if (matchPos > 0) beginning.append(res.substring(0, matchPos));
            String expr = res.substring(matchPos + OCL_START.length(), endExpr);
            String evalStr = OCLEvaluator.SINGLETON.evalToString(off1, expr);
            if (expr.endsWith("") && evalStr.equals("")) evalStr = "(anon)";
            beginning.append(evalStr);
            res = res.substring(endExpr + OCL_END.length());
            matchPos = res.indexOf(OCL_START);
        }
        if (beginning.length() == 0) return res; else return beginning.append(res).toString();
    }
}
