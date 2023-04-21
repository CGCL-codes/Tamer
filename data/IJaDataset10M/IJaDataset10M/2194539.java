package org.tuba.spatschorke.diploma.operation.ecoretools.arrange;

import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutService;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutType;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.swt.widgets.Display;
import org.tuba.data.Configuration;
import org.tuba.plugins.IArtefactOperation;
import org.tuba.plugins.IArtefactRepresentation;
import org.tuba.spatschorke.diploma.representation.ecoretoolsdiagram.EcoreToolsGMFDiagram;

public class Layouter implements IArtefactOperation {

    @Override
    public IArtefactRepresentation process(IArtefactRepresentation representation, Configuration configuration) {
        if (representation == null || !EcoreToolsGMFDiagram.TYPE.equals(representation.getType())) return null;
        EcoreToolsGMFDiagram diagramRepr = (EcoreToolsGMFDiagram) representation;
        final Diagram diagram = diagramRepr.getDiagram();
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                LayoutService.getInstance().layout(diagram, LayoutType.DEFAULT);
            }
        });
        return new EcoreToolsGMFDiagram(diagram);
    }
}
