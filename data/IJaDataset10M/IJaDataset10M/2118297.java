package com.dynamide;

import java.io.IOException;
import com.dynamide.datatypes.DatatypeException;
import com.dynamide.event.ScriptEvent;
import org.jdom.Element;
import org.jdom.JDOMException;

/** <p>Each widget type, represented by the WidgetType class, corresponds to an xml file, called
 *  something like com.dynamide.button (which may be physically stored as com.dynamide.button.xml), which defines the type.
 *  A page can have *instances* of Widgets, which use the class Widget.
 *  The legal properties of Widgets are found only in the WidgetType's xml file.
 *  The values in the WidgetType's xml file are the default values.   All properties found in
 *  the Page's persistence override these defaults.  There are no run-time-only properties -- all properties found
 *  are available at design-time.
 *  If a property is present, it will be shown in the PropertyPage.  (NOTE: This may be changed
 *  in a future build to allow a property to have a subproperty of "hidden", in which case it
 *  will be hidden from the property editor.)</p>
 *
 *  <p>The widget's type is determined by its "type" property.  This property is mutable in a
 *  widget instance, but has a subproperty of "readOnly", so that it can't be changed in the IDE.
 *  This solves lots of issues.  To change the Widget's type, you must drop a new Widget on the Page.
 *  Right now the rule is that the filename is the type, and all widgets are stored in one of the template directories.
 *  The storage may change, but the "type" property will stay the same.  For this reason, the "type"  property
 *  doesn't include ".xml".  For example, the "type" is "com.dynamide.button", NOT "com.dynamide.button.xml".</p>
 *
 *  <p>This class is also used for Page default properties.  The only Widget-specific methods are:
 *      generateEmptyWidgetFor(String type, String id); </p>
 */
public class WidgetType extends Persistent {

    public WidgetType(DynamideObject owner, String filename, Session session) throws JDOMException, IOException, DatatypeException {
        super(owner, filename, session);
        Element propertiesElement = getRootElement().getChild("properties");
        addProperties(this, propertiesElement, "WidgetType[" + filename + "]");
        m_sType = getPropertyDefaultValue("type");
        if (!filename.replace('\\', '/').endsWith(m_sType + ".xml")) {
            logError("WidgetType has conflicting type property(" + m_sType + ") and filename(" + filename + ")");
        }
        initializeForSession(session);
    }

    public void initializeForSession(Session session) {
        setSession(session);
        if (!m_onImportFired) fire_onImport();
    }

    private String m_sType = "WidgetType";

    public String getType() {
        return m_sType;
    }

    public String getDotName() {
        String sType = m_sType;
        return getOwner().getDotName() + '.' + '{' + sType + '}';
    }

    public Property getPropertyDefault(String name) {
        Property property = (Property) getPropertiesTable().get(name);
        if (property != null) {
            return (Property) property.clone();
        }
        return new Property(this);
    }

    public String getPropertyDefaultValue(String name) {
        String value = findPropertyDefaultValue(name);
        if (value == null) {
            return "";
        }
        return value;
    }

    public String findPropertyDefaultValue(String name) {
        Property property = (Property) getPropertiesTable().get(name);
        if (property != null) {
            return property.get("defaultValue").toString();
        }
        return null;
    }

    private boolean m_onImportFired = false;

    private boolean m_onLoadFired = false;

    /** Fires an event named for the Widget type, but fires it once per loading of widget instance
     *  on a Page, so it is a Widget-author plugable event.  It is called from the code that also fires [widgetID]_onLoad which is an application-programmer
     *  pluggable event.
     */
    public ScriptEvent fire_onLoad(Widget widget) {
        String widgetTypeName = AbstractWidget.widgetTypeToScriptName(getType());
        String eventName = widgetTypeName + "_onLoad";
        ScriptEvent ev = getSession().fireEvent(widget, eventName, "", "", "", getEventSource(eventName), getFilename(), false);
        m_onLoadFired = true;
        return ev;
    }

    public ScriptEvent fire_onImport() {
        String widgetTypeName = AbstractWidget.widgetTypeToScriptName(getType());
        String eventName = widgetTypeName + "_onImport";
        ScriptEvent ev = getSession().fireEvent(this, eventName, "", "", "", getEventSourceBody(eventName), getFilename(), true);
        m_onImportFired = true;
        return ev;
    }

    public ScriptEvent fire_onPropertyChanged(AbstractWidget widget, com.dynamide.event.ChangeEvent changeEvent) {
        System.out.println("in fire_onPropertyChanged: " + widget);
        String widgetTypeName = AbstractWidget.widgetTypeToScriptName(getType());
        String eventName = widgetTypeName + "_onPropertyChanged";
        ScriptEvent ev = getSession().fireEvent(widget, changeEvent, eventName, "", "", "", getEventSource(eventName), getFilename(), false, null, "");
        return ev;
    }

    /**  Produces something like this:
      *          &lt;widget id='"+id+"'  &gt;
      *             &lt;properties&gt;
      *                 &lt;property name='type'&gt;"+type+"&lt;/property&gt;
      *             &lt;/properties&gt;
      *          &lt;/widget&gt;
      */
    public static Element generateEmptyWidgetFor(String type, String id) {
        Element property = new Element("property");
        property.setAttribute("name", "type");
        property.addContent(type);
        Element properties = new Element("properties");
        properties.addContent(property);
        Element widget = new Element("widget");
        widget.setAttribute("id", id);
        widget.addContent(properties);
        return widget;
    }
}
