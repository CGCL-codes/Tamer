package md.ui.graphics.uml;

import org.eclipse.swt.SWT;
import md.ui.graphics.Line;
import md.ui.graphics.Shape;

public class RelationshipAssociationClassConnexion extends Line {

    public RelationshipAssociationClassConnexion(Shape element1, Shape element2) {
        super(element1, element2);
        setLineStyle(SWT.LINE_DOT);
    }

    public Object getSelectable() {
        return null;
    }
}
