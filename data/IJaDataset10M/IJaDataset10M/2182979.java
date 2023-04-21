package alfinal.view.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import sqlinterface.SQLDirectInterface;
import alfinal.model.AnnotatedImage;
import alfinal.model.Annotation;
import alfinal.model.util.NoticeMessages;
import alfinal.view.AnnotationPanel;
import alfinal.view.ViewObjects;

public class AnnotationPanelListener implements Serializable, ActionListener {

    private ViewObjects views;

    private SQLDirectInterface sql;

    /**
	 * Constructor, associates the given view objects with this listener
	 * and assigns the sql query engine
	 * 
	 * @param vobj
	 * 		View objects to associate with this listener
	 * @param sqlEngine
	 * 		SQL Query engine
	 */
    public AnnotationPanelListener(ViewObjects vobj, SQLDirectInterface sqlEngine) {
        this.views = vobj;
        this.sql = sqlEngine;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (this.views.imageDisplay.singleMode() == false) return;
        if (ae.getSource() == this.views.annotatePanel.getClearButton()) {
            this.views.annotatePanel.clear();
            this.views.imageDisplay.redrawClearPoly();
        } else if (ae.getSource() == this.views.annotatePanel.getConfirmButton()) {
            Annotation annotation = this.views.annotatePanel.getCurrentAnnotation();
            if (annotation != null) {
                System.out.println(annotation.toString());
                this.views.annotatePanel.getClearButton().doClick();
                AnnotatedImage image = this.views.imageDisplay.getCurrentPanel().getImage();
                if (image == null) return;
                if (image.getID() == null) {
                    boolean add = NoticeMessages.addAnnotatedImage();
                    if (add == true) {
                        image.setID(this.sql.AddAnnotatedImage(image));
                    } else return;
                }
                annotation.setID(this.sql.AddAnnotation(image, annotation));
                this.sql.getImageAnnotations(image, this.sql.getCategories());
            }
        } else if (ae.getSource() == this.views.annotatePanel.getLookupTypeButton()) {
            if (this.views.imageDisplay.singleMode() == true) {
                this.views.catTreeFrame.display(true);
            }
        } else if (ae.getSource() == this.views.annotatePanel.getPolygonModeButton()) {
            if (this.views.annotatePanel.inPolygonMode() == false) this.views.annotatePanel.startPolygonMode(); else {
                this.views.annotatePanel.escapePolygonMode();
                this.views.imageDisplay.redrawWithPoly(this.views.annotatePanel.getCurrentPolygon());
            }
        } else {
        }
    }
}
