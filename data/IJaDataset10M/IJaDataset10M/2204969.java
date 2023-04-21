package com.portalwizard.gen;

import com.portalwizard.gen.wsdl.CommandOptions;
import com.portalwizard.util.ClassloaderUtil;
import com.portalwizard.util.JDOMUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.File;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Allen, Nov 12, 2006 9:21:56 AM
 */
public class GenerateCopybook extends Generator implements Observer {

    protected Log log = LogFactory.getLog(this.getClass());

    protected String propFileName = "cobol";

    public GenerateCopybook() {
        propFileName = CommandOptions.getOption("-properties", "cobol");
    }

    protected int getPictureColumn() {
        return (52);
    }

    protected int getBaseLevel() {
        return (10);
    }

    protected int getLevelIncrement() {
        return (5);
    }

    protected int getTabSize() {
        return (2);
    }

    protected String getSpaces(int size) {
        StringBuffer tab = new StringBuffer();
        for (int x = size; x > 0; x--) tab.append(" ");
        return (tab.toString());
    }

    public void generate(SchemaManager schema, String typeName, PrintStream out) throws Exception {
        schema.addObserver(this);
        Element message = schema.getElement(CommandOptions.getMessage());
        JDOMUtils.printDocument(message, new FileOutputStream(new File(typeName + ".temp.xml")));
        generate(message, out);
        schema.deleteObserver(this);
    }

    public void update(Observable o, Object arg) {
        if (o instanceof SchemaManager) {
            if (arg instanceof SchemaManager.AddChildEvent) {
                SchemaManager.AddChildEvent event = (SchemaManager.AddChildEvent) arg;
            } else if (arg instanceof SchemaManager.SetTypeEvent) {
                SchemaManager.SetTypeEvent event = (SchemaManager.SetTypeEvent) arg;
                event.element.setAttribute("type", event.type);
            } else if (arg instanceof SchemaManager.SetMinLengthEvent) {
                SchemaManager.SetMinLengthEvent event = (SchemaManager.SetMinLengthEvent) arg;
                event.element.setAttribute("minLength", event.minLength);
            } else if (arg instanceof SchemaManager.SetMaxLengthEvent) {
                SchemaManager.SetMaxLengthEvent event = (SchemaManager.SetMaxLengthEvent) arg;
                event.element.setAttribute("maxLength", event.maxLength);
            } else if (arg instanceof SchemaManager.SetMinOccursEvent) {
                SchemaManager.SetMinOccursEvent event = (SchemaManager.SetMinOccursEvent) arg;
                event.element.setAttribute("minOccurs", event.minOccurs);
            } else if (arg instanceof SchemaManager.SetMaxOccursEvent) {
                SchemaManager.SetMaxOccursEvent event = (SchemaManager.SetMaxOccursEvent) arg;
                event.element.setAttribute("maxOccurs", event.maxOccurs);
            }
        }
    }

    public void generate(Element message, PrintStream out) {
        out.println("       *******************************************************************************");
        out.println("       *");
        out.println("       *******************************************************************************");
        generate(0, message, out);
    }

    protected void generate(int level, Element node, PrintStream out) {
        StringBuffer buff;
        boolean groupItem = (node.getChildren().size() > 0);
        int occurs = getMaximumOccurs(node);
        if (occurs > 1) {
            buff = getCopybookIndentation(level);
            buff.append(String.valueOf(getBaseLevel() + (level * getLevelIncrement())));
            buff.append("  ");
            buff.append(getCobolName(node)).append("-CNT");
            buff.append(" ");
            for (int x = buff.length(); x < getPictureColumn() || (x % getTabSize()) > 0; x++) buff.append(" ");
            buff.append("PIC 9(5) COMP.");
            out.println(buff.toString());
        }
        buff = getCopybookIndentation(level);
        buff.append(String.valueOf(getBaseLevel() + (level * getLevelIncrement())));
        buff.append("  ");
        buff.append(getCobolName(node));
        if (occurs > 1) buff.append(MessageFormat.format(" OCCURS {0} TIMES", new Object[] { String.valueOf(occurs) }));
        if (groupItem) {
            buff.append(".");
            out.println(buff.toString());
            for (Iterator childItr = node.getChildren().iterator(); childItr.hasNext(); ) generate(level + 1, (Element) childItr.next(), out);
        } else {
            buff.append(" ");
            for (int x = buff.length(); x < getPictureColumn() || (x % getTabSize()) > 0; x++) buff.append(" ");
            buff.append(getPicture(node)).append(".");
            out.println(buff.toString());
        }
    }

    private StringBuffer getCopybookIndentation(int level) {
        StringBuffer buff = new StringBuffer("           ");
        for (int x = level; x > 0; x--) buff.append(getSpaces(getTabSize()));
        return buff;
    }

    protected String getCobolName(Element node) {
        String path = JDOMUtils.getXPath(node);
        String name = getVirtualPropertyValue(path);
        if (name == null) {
            name = node.getName().toUpperCase();
            PropertyHelper.logUnknownNames(node);
        }
        name = name.replaceAll("[.]", "-");
        return (name);
    }

    protected String getOccursClause(Element node) {
        int occurs = getMaximumOccurs(node);
        String mask = " OCCURS {0} TIMES";
        if (occurs > 1) return (MessageFormat.format(mask, new Object[] { String.valueOf(occurs) })); else return ("");
    }

    protected int getMaximumOccurs(Element node) {
        String occursString = node.getAttributeValue("maxOccurs", "1");
        occursString = getVirtualPropertyValue(JDOMUtils.getXPath(node) + ".maxOccurs", occursString);
        if ("unbounded".equals(occursString)) {
            PropertyHelper.logUnboundedOccurs(node);
            return (1);
        }
        try {
            int occurs = Integer.parseInt(occursString);
            return (occurs);
        } catch (NumberFormatException e) {
            log.warn("Unable to parse the element occurs [" + node.getName() + "::" + occursString + "]", e);
        }
        return (1);
    }

    protected String getPicture(Element node) {
        String contentType = node.getAttributeValue("type");
        String lengthString = getVirtualPropertyValue(JDOMUtils.getXPath(node) + ".maxLength", node.getAttributeValue("maxLength"));
        if (lengthString != null) {
            String mask = ClassloaderUtil.getStringResource(propFileName, "string", "PIC X({0})");
            String result = MessageFormat.format(mask, new Object[] { lengthString });
            return (result);
        } else {
            String value = ClassloaderUtil.getStringResource(propFileName, contentType);
            if (value == null) {
                PropertyHelper.logUnknownLengths(node);
                return ("Unknown Length");
            }
            return (value);
        }
    }

    protected String getVirtualPropertyValue(String name) {
        return (getVirtualPropertyValue(name, null));
    }

    protected String getVirtualPropertyValue(String name, String defaultValue) {
        String vname = String.valueOf(name);
        String value = null;
        while (vname != null) {
            log.debug("Looking for " + vname);
            if ((value = ClassloaderUtil.getStringResource(propFileName, vname)) != null) break;
            if (vname.indexOf("/") > -1) vname = vname.substring(vname.indexOf("/") + 1); else if (vname.indexOf("@") > -1) vname = vname.substring(vname.indexOf("@") + 1); else break;
        }
        if (value == null) return (defaultValue);
        return (value);
    }
}
