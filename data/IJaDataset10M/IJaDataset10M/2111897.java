package org.isi.monet.modelling.editor.model.properties.documents;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.isi.monet.modelling.core.project.MonetProjectSupport;
import org.isi.monet.modelling.editor.model.Constants;
import org.isi.monet.modelling.editor.model.declarations.Declaration;
import org.isi.monet.modelling.editor.model.properties.Code;
import org.isi.monet.modelling.editor.model.properties.Label;
import org.isi.monet.modelling.editor.model.properties.Labels;
import org.isi.monet.modelling.editor.model.properties.Name;
import org.isi.monet.modelling.editor.model.properties.Property;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Configuration extends Property {

    private HashMap<String, String> defaultLanguages;

    private Label defaultLabel;

    private String defaultLanguage;

    private Labels allLabels;

    private List<String> languages;

    public Configuration(Declaration parent) {
        super(parent);
        this.typeProperty = Constants.PROPERTY_CONFIGURATION;
        this.valueProperty = "";
        this.properties.add(new Code(this));
        this.properties.add(new Name(this, ""));
        defaultLanguages = new HashMap<String, String>();
        defaultLanguages.put("Spanish", "es");
        defaultLanguages.put("English", "en");
        defaultLanguages.put("German", "gr");
        defaultLanguages.put("Italian", "it");
        defaultLanguages.put("French", "fr");
        languages = getLanguagesPropertyValue();
        Labels labels = new Labels(this);
        setLabels(labels);
        properties.add(allLabels);
        List<Label> propLabels = getLabels();
        this.valueProperty = getDefaultLabel(propLabels);
    }

    public Configuration(Declaration parent, Node node) {
        super(parent);
        this.typeProperty = Constants.PROPERTY_CONFIGURATION;
        this.valueProperty = "";
        defaultLanguages = new HashMap<String, String>();
        defaultLanguages.put("Spanish", "es");
        defaultLanguages.put("English", "en");
        defaultLanguages.put("German", "gr");
        defaultLanguages.put("Italian", "it");
        defaultLanguages.put("French", "fr");
        languages = getLanguagesPropertyValue();
        Labels labels = new Labels(this);
        NamedNodeMap attributes = node.getAttributes();
        Node attr = attributes.getNamedItem("code");
        if (attr != null) properties.add(new Code(this, attr.getNodeValue())); else properties.add(new Code(this, ""));
        attr = attributes.getNamedItem("name");
        if (attr != null) properties.add(new Name(this, attr.getNodeValue())); else properties.add(new Name(this, ""));
        NodeList declarationsNodes = node.getChildNodes();
        for (int index = 0; index < declarationsNodes.getLength(); index++) {
            Node declaration = declarationsNodes.item(index);
            if (declaration instanceof Element) {
                if (declaration.getNodeName() == "label") labels.addNewLabel(new Label(labels, declaration));
            }
        }
        setLabels(labels);
        properties.add(allLabels);
        List<Label> propLabels = getLabels();
        this.valueProperty = getDefaultLabel(propLabels);
    }

    public List<Label> getLabels() {
        List<Property> propsParent = this.getProperties();
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

    @Override
    public void valueChanged(Property property) {
        if (Labels.class.isInstance(property)) setValueProperty(((Labels) property));
        if (getParent() instanceof Declaration) ((Declaration) getParent()).valuePropertyChanged(this); else ((Property) getParent()).valueChanged(this);
    }

    private void setValueProperty(Labels labels) {
        this.valueProperty = getDefaultLabel(labels.getLabels());
    }

    public void setDefaultLabel(String value) {
        defaultLabel.setValueProperty(value);
    }

    public void setDefaultLabel(Labels labels) {
        String value = getDefaultLabel(labels.getLabels());
        this.valueProperty = value;
    }

    private void setLabels(Labels labels) {
        allLabels = new Labels(this);
        Iterator<String> iterLanguage = languages.iterator();
        while (iterLanguage.hasNext()) {
            String language = defaultLanguages.get(iterLanguage.next());
            if (language != null) checkLanguage(language, labels);
        }
    }

    private void checkLanguage(String language, Labels labels) {
        boolean bShort = false;
        boolean bLong = false;
        Iterator<Label> iterLabel = labels.getLabels().iterator();
        while (iterLabel.hasNext()) {
            Label label = iterLabel.next();
            if (label.getLanguage().equalsIgnoreCase(language)) {
                if (label.getTypeProperty().equalsIgnoreCase("short")) {
                    bShort = true;
                    allLabels.addNewLabel(label);
                } else {
                    bLong = true;
                    allLabels.addNewLabel(label);
                }
            }
        }
        if (!bShort) allLabels.addNewLabel(new Label(allLabels, language, "short"));
        if (!bLong) allLabels.addNewLabel(new Label(allLabels, language, "long"));
    }

    private List<String> getLanguagesPropertyValue() {
        IResource resource = (IResource) project;
        try {
            List<String> values = new ArrayList<String>();
            if (resource.getPersistentProperty(MonetProjectSupport.LANGUAGE_ENGLISH) != null) values.add(resource.getPersistentProperty(MonetProjectSupport.LANGUAGE_ENGLISH));
            if (resource.getPersistentProperty(MonetProjectSupport.LANGUAGE_SPANISH) != null) values.add(resource.getPersistentProperty(MonetProjectSupport.LANGUAGE_SPANISH));
            if (resource.getPersistentProperty(MonetProjectSupport.LANGUAGE_ITALIAN) != null) values.add(resource.getPersistentProperty(MonetProjectSupport.LANGUAGE_ITALIAN));
            if (resource.getPersistentProperty(MonetProjectSupport.LANGUAGE_FRENCH) != null) values.add(resource.getPersistentProperty(MonetProjectSupport.LANGUAGE_FRENCH));
            if (resource.getPersistentProperty(MonetProjectSupport.LANGUAGE_GERMAN) != null) values.add(resource.getPersistentProperty(MonetProjectSupport.LANGUAGE_GERMAN));
            return values;
        } catch (CoreException e) {
        }
        return null;
    }

    @Override
    public void appendText(PrintWriter writer, String tabs) {
        writer.print(tabs + "<" + this.typeProperty + " ");
        Iterator<Property> iterProperties = this.properties.iterator();
        while (iterProperties.hasNext()) {
            Property property = iterProperties.next();
            if (Code.class.isInstance(property) || Name.class.isInstance(property)) {
                property.appendText(writer, "");
            }
        }
        writer.println(">");
        String indent = tabs + "\t";
        iterProperties = this.properties.iterator();
        while (iterProperties.hasNext()) {
            Property property = iterProperties.next();
            if (!Code.class.isInstance(property) && !Name.class.isInstance(property)) {
                property.appendText(writer, indent);
            }
        }
        writer.print(tabs + "</" + this.typeProperty + ">");
        writer.println();
    }
}
