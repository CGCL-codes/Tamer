package vista.graficador.uml;

import java.util.Iterator;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.ModelElement;
import org.uml.diagrammanagement.GraphEdge;
import org.uml.diagrammanagement.SemanticModelBridge;
import org.uml.diagrammanagement.Uml1SemanticModelBridge;
import util.EstereotipoUtil;
import vista.graficador.GrDiagrama;
import vista.graficador.GrLineaVisual;

/**
 *
 * @author Juan Timoteo Ponce Ortiz
 */
public class GrDependencia extends GrLineaVisual {

    /** Creates a new instance of GrDependencia */
    public GrDependencia(GrDiagrama graficador, GraphEdge edge) {
        super(graficador, edge);
        setTracedline(true);
        setArrow2(true);
    }

    protected void updateLineaVisual() {
        SemanticModelBridge bridge = modelo.getSemanticModel();
        if (bridge instanceof Uml1SemanticModelBridge) {
            Uml1SemanticModelBridge semanticBridge = (Uml1SemanticModelBridge) bridge;
            ModelElement me = semanticBridge.getElement();
            if (me instanceof Dependency) {
                Dependency dep = (Dependency) me;
                setTextoLinea(dep.getName(), "Name");
                String newStereo = "";
                String stereo = EstereotipoUtil.aCadena(dep.getStereotype());
                if (stereo != null && !stereo.trim().equals("")) newStereo = "<<" + stereo + ">>";
                setTextoLinea(newStereo, "Stereotype");
            }
        }
    }

    protected void updateLineaLogica() {
    }

    protected void finalizeCreacionLinea() throws Exception {
        Dependency logicalDependency = null;
        try {
            logicalDependency = (Dependency) ((Uml1SemanticModelBridge) getLineaLogica().getSemanticModel()).getElement();
        } catch (Exception e) {
        }
        if (logicalDependency != null) {
            Iterator iClient = logicalDependency.getClient().iterator();
            Iterator iSupplier = logicalDependency.getSupplier().iterator();
            ModelElement meClient = null;
            ModelElement meSupplier = null;
            if (!iClient.hasNext() && !iSupplier.hasNext()) {
                try {
                    meClient = ((Uml1SemanticModelBridge) getLineaLogica().getEdgeEnd1().getSemanticModel()).getElement();
                    meSupplier = ((Uml1SemanticModelBridge) getLineaLogica().getEdgeEnd2().getSemanticModel()).getElement();
                } catch (ClassCastException e) {
                    throw new Exception("Dependencies must involve Model Elements!");
                } catch (NullPointerException npe) {
                    throw new Exception("Dependencies must involve Model Elements!");
                }
            }
            if (meClient != null && meSupplier != null) {
                logicalDependency.getClient().add(meClient);
                logicalDependency.getSupplier().add(meSupplier);
            }
        }
    }
}
