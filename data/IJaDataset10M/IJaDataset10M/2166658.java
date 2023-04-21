package monet.editors.model.fieldsDeclarations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import monet.editors.model.Declaration;
import monet.editors.model.DeclarationConstants;
import monet.editors.model.DeclarationModel;
import monet.editors.model.editions.PropEditionFieldSelect;
import monet.editors.model.propertiesDeclarations.Property;
import monet.editors.model.propertiesDeclarations.PropertyLabel;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DeclarationFieldSelect extends DeclarationField {

    public DeclarationFieldSelect(Declaration parent, Node decl) {
        super(parent, decl);
        NodeList childs = decl.getChildNodes();
        for (int i = 0; i < childs.getLength(); i++) {
            Node declaration = childs.item(i);
            if (declaration instanceof Element) {
                if (declaration.hasChildNodes()) {
                    if (declaration.getNodeName() == "edition") {
                        properties.add(new PropEditionFieldSelect(this, declaration));
                    }
                }
            }
        }
        Iterator<Property> iter = properties.iterator();
        while (iter.hasNext()) {
            Property prop = iter.next();
            if (prop instanceof PropertyLabel) this.name = ((PropertyLabel) prop).getValueProperty();
        }
    }

    public DeclarationFieldSelect(Declaration parent) {
        super(parent);
        this.type = DeclarationConstants.DECLARATION_FIELD_SELECT;
        Iterator<Property> iter = properties.iterator();
        while (iter.hasNext()) {
            Property prop = iter.next();
            if (prop instanceof PropertyLabel) this.name = ((PropertyLabel) prop).getValueProperty();
        }
        properties.add(new PropEditionFieldSelect(this));
    }

    @Override
    public DeclarationModel[] getChildren() {
        return NO_CHILDREN;
    }

    @Override
    public void removeFromParent() {
        ((Declaration) getParent()).removeChild(this);
    }

    @Override
    public void valuePropertyChanged(Property property) {
        if (property instanceof PropertyLabel) {
            if (((PropertyLabel) property).getNameProperty() == "label") this.setName(((PropertyLabel) property).getValueProperty());
        }
        ((Declaration) getParent()).valuePropertyChanged(this, property);
    }

    @Override
    public DeclarationModel[] getProperties() {
        List<DeclarationModel> children = new ArrayList<DeclarationModel>();
        children.addAll(this.properties);
        return children.toArray(new DeclarationModel[children.size()]);
    }

    @Override
    public void setName(String newName) {
        Iterator<Property> iter = properties.iterator();
        while (iter.hasNext()) {
            Property property = (Property) iter.next();
            if (property instanceof PropertyLabel) {
                PropertyLabel prop = (PropertyLabel) property;
                if (prop.getNameProperty() == "label") {
                    prop.setValueProperty(newName);
                }
            }
        }
        if (name.equals(newName)) return;
        name = newName;
        ((Declaration) getParent()).nameChanged(this);
    }

    @Override
    public void nameChanged(Declaration declaration) {
    }

    @Override
    public void removeChild(Declaration declaration) {
    }

    @Override
    public void childRemoved(Declaration declarationParent, Declaration declaration) {
    }

    @Override
    public void removeDeclaration(Declaration declaration) {
    }

    @Override
    public void addChild(Declaration decl) {
    }

    @Override
    public void childAdded(Declaration declaration, Declaration decl) {
    }

    @Override
    public void nameChanged(Declaration declaration, Declaration decl) {
    }

    @Override
    public void valuePropertyChanged(Declaration declaration, Property property) {
    }

    @Override
    public void valuePropertyChanged(Declaration declaration, Declaration declaration2) {
    }

    @Override
    public void valuePropertyChanged(Property property, Property property2) {
    }
}
