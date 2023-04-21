package com.prolix.editor.interaction.config.comment;

import org.jdom.Element;
import com.prolix.editor.interaction.model.InteractionImpl;
import com.prolix.editor.interaction.model.test.TestInteraction;
import uk.ac.reload.straker.datamodel.IXMLMarshaller;
import uk.ac.reload.straker.datamodel.learningdesign.components.properties.LD_Property;
import uk.ac.reload.straker.datamodel.learningdesign.components.properties.LocalPersonalProperty;
import uk.ac.reload.straker.datamodel.learningdesign.components.properties.LocalRoleProperty;

/**
 * <<class description>>
 * 
 * @author Susanne Neumann, Stefan Zander, Philipp Prenner
 */
public class ConfigCommentTestSimple implements ConfigComment, IXMLMarshaller {

    public static final String XML_Name = "simple-test-comment";

    private TestInteraction interaction;

    private LD_Property commentProperty;

    /**
	 * 
	 */
    public ConfigCommentTestSimple(TestInteraction interaction) {
        this.interaction = interaction;
    }

    public boolean canMarshall2XML() {
        return true;
    }

    public String getXMLElementName() {
        return XML_Name;
    }

    public Element marshall2XML(Element parentElement) {
        Element element = new Element(getXMLElementName());
        parentElement.addContent(element);
        return element;
    }

    public void unmarshallXML(Element element) {
    }

    public void build() {
        if (interaction.isPersonal()) commentProperty = new LocalPersonalProperty(interaction.getLearningDesignDataModel()); else commentProperty = new LocalRoleProperty(interaction.getLearningDesignDataModel());
        commentProperty.setTitle(interaction.getName() + " - Comment");
        commentProperty.setDataType("text");
        interaction.getProperties().addChild(commentProperty);
        interaction.addPropertyToGroup(commentProperty);
    }

    public void clean() {
        interaction.getProperties().removeChild(commentProperty);
        commentProperty = null;
    }

    /**
	 * @return the commentProperty
	 */
    public LD_Property getCommentProperty() {
        return commentProperty;
    }

    public ConfigComment duplicate(InteractionImpl interaction) {
        ConfigCommentTestSimple copy = new ConfigCommentTestSimple((TestInteraction) interaction);
        return copy;
    }
}
