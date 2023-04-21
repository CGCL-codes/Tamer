package it.cnr.stlab.xd.plugin.editor.figures;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Rectangle;

public class WorkingOntologyFigure extends FreeformLayer {

    private Label ontologyURI = new Label();

    public WorkingOntologyFigure() {
        ontologyURI.setForegroundColor(FigureColors.DARK_GREY);
        setBorder(new MarginBorder(3));
        setLayoutManager(new XDLayout());
        add(ontologyURI);
        setConstraint(ontologyURI, new Rectangle(5, 5, -1, -1));
    }

    public void setOntologyURI(String uri) {
        ontologyURI.setText(uri);
    }

    public String getOntologyURI() {
        return ontologyURI.getText();
    }

    public void setLayout(Rectangle bounds) {
        setBounds(bounds);
    }
}
