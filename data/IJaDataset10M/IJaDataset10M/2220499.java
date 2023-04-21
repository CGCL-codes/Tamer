package org.adapit.wctoolkit.uml.ext.core;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import org.adapit.wctoolkit.models.config.ApplicationConfiguration;
import org.adapit.wctoolkit.models.config.ApplicationReport;
import org.adapit.wctoolkit.models.config.IgnoredElementException;
import org.adapit.wctoolkit.models.util.AllElements;
import org.adapit.wctoolkit.models.xml.ExportInterceptor;
import org.w3c.dom.Node;

@SuppressWarnings({ "serial", "unchecked" })
@Entity
public class TagDefinition extends ElementImpl implements NamedElement, Serializable {

    @Column(length = 60)
    private String tagType;

    @Column(length = 100)
    private String alternativeName = null;

    @Override
    public void merge(IElement el) throws Exception {
        super.merge(el);
    }

    public TagDefinition() {
        super();
        setResizableIcon(false);
        setIcon("/icons/tagdefinition2.png");
        tagType = "Element";
    }

    public TagDefinition(IElement parent) {
        super(parent);
        setResizableIcon(false);
        setIcon("/icons/tagdefinition2.png");
        tagType = "Element";
    }

    public String exportXMI1_3(int tab) throws Exception {
        String str = "";
        ExportInterceptor ei = ApplicationConfiguration.getInstance().getUsedExportInterceptors().get(getClass());
        if (ei != null) return ei.exportXmi(this, tab);
        return str;
    }

    public String exportXMI1_2(int tab) throws Exception {
        String str = "";
        ExportInterceptor ei = ApplicationConfiguration.getInstance().getUsedExportInterceptors().get(getClass());
        if (ei != null) return ei.exportXmi(this, tab);
        str += '\n';
        for (int i = 0; i < tab; i++) {
            str += '\t';
        }
        strBuffer.append(str);
        String oldName = name;
        if (alternativeName != null) name = alternativeName;
        strBuffer.append("<UML:TagDefinition" + super.getExportAttributes());
        name = oldName;
        strBuffer.append(" isSpecification=\"false\" ");
        if (this.tagType != null) strBuffer.append(" tagType=\"" + tagType + "\""); else strBuffer.append(" tagType=\"String\"");
        strBuffer.append(" />");
        return "";
    }

    public String exportXMI1_1(int tab) throws Exception {
        String str = "";
        ExportInterceptor ei = ApplicationConfiguration.getInstance().getUsedExportInterceptors().get(getClass());
        if (ei != null) return ei.exportXmi(this, tab);
        str += '\n';
        for (int i = 0; i < tab; i++) {
            str += '\t';
        }
        strBuffer.append(str);
        String oldName = name;
        if (alternativeName != null) name = alternativeName;
        strBuffer.append("<UML:TagDefinition" + super.getExportAttributes());
        name = oldName;
        strBuffer.append(" isSpecification=\"false\" ");
        if (this.tagType != null) strBuffer.append(" tagType=\"" + tagType + "\""); else strBuffer.append(" tagType=\"String\"");
        strBuffer.append(" />");
        return "";
    }

    public void importXMI1_2(Node element) {
        String nodeName = element.getNodeName();
        if (nodeName.equalsIgnoreCase("UML:TagDefinition")) {
            if (element.getAttributes() != null && element.getAttributes().getLength() > 0) try {
                try {
                    try {
                        this.id = element.getAttributes().getNamedItem("xmi.id").getNodeValue();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    try {
                        this.setName(element.getAttributes().getNamedItem("name").getNodeValue());
                        ApplicationReport.getInstance().getLogTextArea().setText("..." + messages.getString("org.adapit.wctoolkit.uml.ext.Importing") + " " + messages.getString("org.adapit.wctoolkit.uml.ext.core.TagDefinition") + " " + getName());
                        logger.debug("..." + messages.getString("org.adapit.wctoolkit.uml.ext.Importing") + " " + messages.getString("org.adapit.wctoolkit.uml.ext.core.TagDefinition") + " " + getName());
                    } catch (Exception e1) {
                    }
                    try {
                        this.tagType = element.getAttributes().getNamedItem("tagType").getNodeValue();
                    } catch (Exception e) {
                    }
                    AllElements.getInstance().getElements().put(getId(), this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception ex) {
                if (!(ex instanceof IgnoredElementException)) ex.printStackTrace();
            }
        }
    }

    public void importXMI1_1(Node element) throws Exception {
        String nodeName = element.getNodeName();
        if (nodeName.equalsIgnoreCase("UML:Constraint")) {
            if (element.getAttributes() != null && element.getAttributes().getLength() > 0) try {
                try {
                    try {
                        this.id = element.getAttributes().getNamedItem("xmi.id").getNodeValue();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    try {
                        this.setName(element.getAttributes().getNamedItem("name").getNodeValue());
                        ApplicationReport.getInstance().getLogTextArea().setText("..." + messages.getString("org.adapit.wctoolkit.uml.ext.Importing") + " Constraint/TaggedValue " + getName());
                        logger.debug("..." + messages.getString("org.adapit.wctoolkit.uml.ext.Importing") + " Constraint/TaggedValue " + getName());
                    } catch (Exception e1) {
                    }
                    AllElements.getInstance().getElements().put(getId(), this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception ex) {
                if (!(ex instanceof IgnoredElementException)) ex.printStackTrace();
            }
        }
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public String getAlternativeName() {
        return alternativeName;
    }

    public void setAlternativeName(String alternativeName) {
        this.alternativeName = alternativeName;
    }

    @Override
    public void setAsText(String name) {
        this.name = name;
    }
}
