package org.isi.monet.modelling.editor.model.declarations;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.isi.monet.modelling.core.project.MonetProjectSupport;
import org.isi.monet.modelling.editor.model.Constants;
import org.isi.monet.modelling.editor.model.Model;
import org.isi.monet.modelling.editor.model.declarations.container.Contain;
import org.isi.monet.modelling.editor.model.properties.Label;
import org.isi.monet.modelling.editor.model.properties.Labels;
import org.isi.monet.modelling.editor.model.properties.Property;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Container extends Declaration {

    private HashMap<String, String> defaultLanguages;

    private Label defaultLabel;

    private String defaultLanguage;

    public Container(Definition parent, Node node) {
        super(parent);
        this.type = Constants.DECLARATION_CONTAINER;
        defaultLanguages = new HashMap<String, String>();
        defaultLanguages.put("Spanish", "es");
        defaultLanguages.put("English", "en");
        defaultLanguages.put("German", "gr");
        defaultLanguages.put("Italian", "it");
        defaultLanguages.put("French", "fr");
        NodeList declarationsNodes = node.getChildNodes();
        for (int index = 0; index < declarationsNodes.getLength(); index++) {
            Node declaration = declarationsNodes.item(index);
            if (declaration instanceof Element) {
                if (declaration.getNodeName().equalsIgnoreCase(Constants.DECLARATION_CONTAINER_CONTAIN)) declarations.add(new Contain(this, declaration));
            }
        }
        List<Label> propLabels = getLabels();
        this.name = getDefaultLabel(propLabels);
    }

    @Override
    public void appendText(PrintWriter writer, String tabs) {
        writer.print(tabs + "<" + this.type + ">");
        writer.println();
        String indent = tabs + "\t";
        Iterator<Declaration> iterDeclarations = this.declarations.iterator();
        while (iterDeclarations.hasNext()) iterDeclarations.next().appendText(writer, indent);
        writer.print(tabs + "</" + this.type + ">");
        writer.println();
    }

    @Override
    public Model[] getChildren() {
        List<Model> children = new ArrayList<Model>();
        children.addAll(this.declarations);
        return children.toArray(new Model[children.size()]);
    }

    @Override
    public void removeDeclaration(Declaration declaration) {
        if (declarations.remove(declaration)) ((Definition) getParent()).nameChanged(this);
    }

    public void setName(String text) {
        if (name.equals(text)) return;
        name = text;
        setDefaultLabel(text);
        if (getParent() instanceof Declaration) ((Declaration) getParent()).nameChanged(this);
    }

    public void setName(Labels labels) {
        this.name = getDefaultLabel(labels.getLabels());
    }

    public void setDefaultLabel(String value) {
        defaultLabel.setValueProperty(value);
    }

    public void setDefaultLabel(Labels labels) {
        String value = getDefaultLabel(labels.getLabels());
        this.name = value;
    }

    public List<Label> getLabels() {
        List<Property> propsParent = ((Declaration) getParent()).getProperties();
        Iterator<Property> iterProperties = propsParent.iterator();
        while (iterProperties.hasNext()) {
            Property prop = iterProperties.next();
            if (Labels.class.isInstance(prop)) return ((Labels) prop).getLabels();
        }
        return null;
    }

    public String getDefaultLanguage() {
        IProject project = this.getProject();
        try {
            String defaultLanguage = project.getPersistentProperty(MonetProjectSupport.DEFAULT_LANGUAGE);
            return defaultLanguage;
        } catch (CoreException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getDefaultLabel(List<Label> labels) {
        if (labels == null) return "";
        IProject project = getProject();
        try {
            defaultLanguage = project.getPersistentProperty(MonetProjectSupport.DEFAULT_LANGUAGE);
            Iterator<Label> iterator = labels.iterator();
            while (iterator.hasNext()) {
                Label label = iterator.next();
                if (label.getLanguage().equalsIgnoreCase(defaultLanguages.get(defaultLanguage)) && label.getTypeProperty().equalsIgnoreCase("short")) {
                    defaultLabel = label;
                    return label.getValueProperty();
                }
            }
        } catch (CoreException e) {
            e.printStackTrace();
        }
        return "";
    }
}
