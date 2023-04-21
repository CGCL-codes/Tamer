package org.cumt.view.editors.usecases;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import org.cumt.misc.Editor;
import org.cumt.misc.ValidationError;
import org.cumt.model.Model;
import org.cumt.model.analysis.usecases.Actor;
import org.cumt.view.editors.AttributesEditor;
import org.cumt.view.editors.ModelObjectEditor;

@SuppressWarnings("serial")
public class ActorEditor extends JTabbedPane implements Editor<Actor> {

    private ModelObjectEditor general = new ModelObjectEditor();

    private AttributesEditor attributes = new AttributesEditor();

    public ActorEditor() {
        addTab("General", general);
        addTab("Otros Atributos", attributes);
        setPreferredSize(new Dimension(450, 400));
    }

    public void setModel(Model model) {
        general.setModel(model);
        attributes.setModel(model);
    }

    public void set(Actor collector) {
        general.set(collector);
        attributes.set(collector);
    }

    public List<ValidationError> validateEdition() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        errors.addAll(general.validateEdition());
        errors.addAll(attributes.validateEdition());
        return errors;
    }

    public void get(Actor distribute) {
        general.get(distribute);
        attributes.get(distribute);
    }

    public JComponent getEditorComponent() {
        return this;
    }
}
