package org.argouml.uml.diagram.use_case.ui;

import org.argouml.ui.TablePanel;
import org.argouml.uml.diagram.static_structure.TableModelAssocByProps;
import org.argouml.uml.diagram.use_case.TableModelActorByProps;
import org.argouml.uml.diagram.use_case.TableModelUseCaseByProps;

public class TablePanelUMLUseCaseDiagram extends TablePanel {

    public TablePanelUMLUseCaseDiagram() {
        super("UMLStateDiagram");
    }

    public void initTableModels() {
        _tableModels.addElement(new TableModelActorByProps());
        _tableModels.addElement(new TableModelUseCaseByProps());
        _tableModels.addElement(new TableModelAssocByProps());
    }
}
