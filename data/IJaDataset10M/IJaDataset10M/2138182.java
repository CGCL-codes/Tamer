package com.bluebrim.content.server;

import java.util.*;
import org.w3c.dom.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.content.shared.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Abstract super class to CoImageContent, CoLayoutContent and CoTextContent.
 */
public abstract class CoContent extends CoObject implements CoContentIF {

    public static final String XML_NAME = "name";

    private String m_name = CoStringResources.getName(CoConstants.UNTITLED);

    private CoGOI m_goi;

    public CoContent() {
    }

    public CoContent(CoGOI goi) {
        m_goi = goi;
    }

    public final CoContentIF copy() {
        CoContent content = doClone();
        prepareCopy(content);
        return content;
    }

    private CoContent doClone() {
        try {
            CoContent content = (CoContent) clone();
            return content;
        } catch (CloneNotSupportedException ex) {
            CoAssertion.assertTrue(false, getClass() + ".clone failed.");
            return null;
        }
    }

    public String getFactoryKey() {
        return CoContentIF.FACTORY_KEY;
    }

    public void markDirty() {
        if (CoAssertion.SIMULATION_SUPPORT) CoAssertion.addChangedObject(this);
    }

    protected final void prepareCopy(CoContentIF copy) {
    }

    public long getCOI() {
        return m_goi.getCoi();
    }

    public CoGOI getGOI() {
        return m_goi;
    }

    public String getContentDescription() {
        return getType() + ": " + getName();
    }

    public String getName() {
        return m_name;
    }

    public final void setName(String name) {
        m_name = name;
        markDirty();
    }

    public abstract String getType();

    public void xmlInit(Map attributes, CoXmlContext context) {
        m_name = CoXmlUtilities.parseString((String) attributes.get(XML_NAME), m_name);
    }

    public void xmlVisit(CoXmlVisitorIF visitor) {
        visitor.exportAttribute(XML_NAME, m_name);
    }

    public boolean isRenameable() {
        return true;
    }

    public void xmlImportFinished(Node node, CoXmlContext context) throws CoXmlReadException {
    }
}
