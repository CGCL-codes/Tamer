package org.web3d.x3d.palette.items;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.openide.text.ActiveEditorDrop;
import org.openide.util.Exceptions;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.web3d.x3d.X3DDataObject;
import org.web3d.x3d.palette.X3DPaletteUtilities;
import org.web3d.x3d.palette.X3DPaletteUtilities.ElementLocation;
import org.web3d.x3d.palette.X3DXMLOutputter;
import org.web3d.x3d.sai.X3DFieldDefinition;
import org.web3d.x3d.types.X3DNode;
import org.web3d.x3d.types.X3DSchemaData;
import static org.web3d.x3d.types.X3DSchemaData.*;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import org.web3d.x3d.options.X3dOptions;
import org.web3d.x3d.types.X3DEnvironmentalSensorNode;
import org.web3d.x3d.types.X3DGroupingNode;
import org.web3d.x3d.types.X3DTransformNode;
import static org.web3d.x3d.types.X3DPrimitiveTypes.*;

/**
 * BaseX3DElement.java
 * Created on Sep 21, 2007, 10:00 AM
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey, Don Brutzman
 * @version $Id$
 */
public abstract class BaseX3DElement implements ActiveEditorDrop {

    protected static final String newLine = System.getProperty("line.separator");

    protected static final String linesep = newLine;

    private JTextComponent target;

    protected ElementLocation elementLocation;

    protected String parent;

    private boolean traceEventsSelectionAvailable = false;

    private String traceEventsTooltip = "";

    private boolean visualizationSelectionAvailable = false;

    private String visualizationTooltip = "";

    private boolean appendExtrusionCrossSection = false;

    private boolean insertCommas, insertLineBreaks, breakLinesAfterIndexSentinel = false;

    public BaseX3DElement() {
        if (this instanceof X3DNode) containerField = ((X3DNode) this).getDefaultContainerField();
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
    }

    @Override
    public boolean handleTransfer(JTextComponent targetComponent) {
        target = targetComponent;
        try {
            X3DPaletteUtilities.buildJdom(targetComponent);
            elementLocation = X3DPaletteUtilities.findSelectedElement(target);
        } catch (Exception ex) {
            System.err.println("Can't build jdom in BaseX3DElement");
        }
        initialize();
        USEvector = X3DPaletteUtilities.getUSEvector(targetComponent, getElementName());
        boolean accept = false;
        Class<? extends BaseCustomizer> bcust = getCustomizer();
        Method custFactMeth = null;
        if (bcust == null) custFactMeth = getCustomizerFactoryMethod();
        if (bcust == null && custFactMeth == null) {
            try {
                X3DPaletteUtilities.insert(createBody(), targetComponent, this);
                accept = true;
            } catch (BadLocationException ex) {
                accept = false;
            }
        } else {
            BaseCustomizer cust = null;
            if (bcust != null) {
                Constructor<? extends BaseCustomizer> cons;
                try {
                    try {
                        cons = bcust.getDeclaredConstructor(this.getClass(), JTextComponent.class);
                        cust = cons.newInstance(this, targetComponent);
                    } catch (NoSuchMethodException ex) {
                        cons = bcust.getDeclaredConstructor(this.getClass(), JTextComponent.class, X3DDataObject.class);
                        cust = cons.newInstance(this, targetComponent, X3DPaletteUtilities.getX3dDataObject(target));
                    }
                } catch (Exception ex) {
                    InputOutput io = IOProvider.getDefault().getIO("Messages", false);
                    String msg = ex.getLocalizedMessage();
                    io.getOut().println(msg != null ? msg : "Unspecified initialization error, possibly regarding node positioning: " + ex.toString() + "\n" + ex.getStackTrace());
                    ex.printStackTrace();
                    return false;
                }
            } else {
                try {
                    cust = (BaseCustomizer) custFactMeth.invoke(null, this, targetComponent);
                } catch (Exception ex) {
                    return false;
                }
            }
            BaseCustomizer.doDialogReturn retrn = cust.showDialog(X3dOptions.getShowNewlineOption());
            if (retrn != BaseCustomizer.doDialogReturn.CANCEL) {
                try {
                    String pre = "";
                    String post = "";
                    switch(retrn) {
                        case ACCEPT_PREPEND_LINEFEED:
                            pre = linesep;
                            break;
                        case ACCEPT_APPEND_LINEFEED:
                            post = linesep;
                            break;
                        case ACCEPT_BOTH_LINEFEEDS:
                            pre = linesep;
                            post = linesep;
                            break;
                    }
                    X3DPaletteUtilities.insert(pre + priorInsert() + createBody() + post, targetComponent, true, this);
                    postInsert(targetComponent);
                } catch (BadLocationException bad) {
                    accept = false;
                }
            } else accept = false;
        }
        return accept;
    }

    public void setLocation(ElementLocation location) {
        elementLocation = location;
        if (location != null) if (location.parent != null) parent = location.parent.name;
    }

    public void setParent(String p) {
        parent = p;
    }

    public abstract String getElementName();

    @SuppressWarnings("unchecked")
    public Class<? extends BaseCustomizer> getCustomizer() {
        Class<? extends BaseCustomizer> c = null;
        try {
            c = (Class<? extends BaseCustomizer>) Class.forName(getClass().getName() + "Customizer");
        } catch (Exception e) {
        }
        return c;
    }

    public Method getCustomizerFactoryMethod() {
        return null;
    }

    public abstract void initialize();

    /**
   * This is called before an insert;  a comment or ExternProtoDeclare might need prior insertion
   * @return empty string
   */
    public String priorInsert() {
        return "";
    }

    /**
   * This is called after a good insert;  subclasses may wish to be informed (e.g., X3D)
   * @param component 
   */
    public void postInsert(JTextComponent component) {
    }

    private String createHead() {
        StringBuilder sb = new StringBuilder();
        if (!getElementName().equals("DOCTYPE")) {
            sb.append("<");
            sb.append(getElementName());
        }
        if (isDEF()) sb.append(buildDEF()); else {
            sb.append(" USE='");
            sb.append(getDEFUSE());
            sb.append("'");
        }
        if (this.isUseContainerField()) sb.append(this.buildContainerField());
        if (!isDEF() && !getElementName().equals("DOCTYPE")) sb.append("/>");
        return sb.toString();
    }

    protected String createAttributes() {
        return "";
    }

    private String content = new String();

    public void setContent(String s) {
        content = s;
    }

    protected String getContent() {
        return content;
    }

    public String createBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(createHead());
        if (isDEF()) {
            String attrs = createAttributes().trim();
            if (attrs.length() > 0) {
                sb.append(" ");
                sb.append(attrs);
            }
            String containedContent = getContent();
            if (containedContent == null) containedContent = "";
            if (getElementName().equals("DOCTYPE")) {
            } else if (containedContent.length() <= 0) {
                sb.append("/>");
            } else {
                sb.append(">");
                sb.append(containedContent);
                sb.append("</");
                sb.append(getElementName());
                sb.append(">");
            }
        }
        try {
            if (isTraceEventsSelectionAvailable()) {
                if (!getElementName().equals("ROUTE") && getDEFUSE().isEmpty()) {
                    sb.append("\n    <!-- DEF attribute must be defined for this node in order to connect event tracing -->\n");
                } else if (getElementName().equals("ROUTE")) {
                    ROUTE route = (ROUTE) this;
                    String fromNode = route.getFromNode();
                    String toNode = route.getToNode();
                    String fromField = route.getFromField();
                    String toField = route.getToField();
                    String eventType = route.getEventType();
                    String scriptName = "Trace_ROUTE_" + fromNode + "." + fromField + "_TO_" + toNode + "." + toField;
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='reportInterval' accessType='initializeOnly' type='SFTime' value='1.0' appinfo='Sampling frequency in seconds (0 means all values)'/>\n").append("  <field name='traceValue' accessType='inputOnly' type='").append(eventType).append("'/>\n").append("  <field name='timeStampPreviousReport' accessType='initializeOnly' type='SFTime' value='-1'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function traceValue (eventValue, timeStamp) {\n").append("      // input eventValue received for trace field\n").append("      if (timeStamp - timeStampPreviousReport >= reportInterval) {\n").append("        Browser.print ('").append(scriptName).append(" type=").append(eventType).append(" value=' + eventValue");
                    if (!eventType.equals("SFBool") && !eventType.equals("SFInt32") && !eventType.equals("SFFloat")) sb.append(".toString()");
                    sb.append(" + '\\n');\n").append("        timeStampPreviousReport = timeStamp;\n").append("      }\n").append("    }\n").append("    function timeOfDay (someTime) {\n").append("      hh = Math.floor (someTime /(60*60)) % 24;\n").append("      mm = Math.floor (someTime / 60)     % 60;\n").append("      ss = Math.floor (someTime)          % 60;\n").append("      if (hh < 9) hour   = '0' + hh;\n").append("      else        hour   =       hh;\n").append("      if (mm < 9) minute = '0' + mm;\n").append("      else        minute =       mm;\n").append("      if (ss < 9) second = '0' + ss;\n").append("      else        second =       ss;\n").append("      return '(' + hour + ':' + minute + ':' + second + ' GMT)';\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='").append(fromField).append("' fromNode='").append(fromNode).append("' toField='traceValue' toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("TimeSensor")) {
                    String scriptName = "Trace_TimeSensor_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <!-- Trace TimeSensor values on X3D browser console -->\n").append("  <field name='reportInterval' accessType='initializeOnly' type='SFTime' value='1.0' appinfo='Sampling frequency in seconds (0 means all values)'/>\n").append("  <field name='fraction'       accessType='inputOnly' type='SFFloat'/>\n").append("  <field name='cycleInterval'  accessType='inputOnly' type='SFTime'/>\n").append("  <field name='startTime'      accessType='inputOnly' type='SFTime'/>\n").append("  <field name= 'stopTime'      accessType='inputOnly' type='SFTime'/>\n").append("  <field name='pauseTime'      accessType='inputOnly' type='SFTime'/>\n").append("  <field name='resumeTime'     accessType='inputOnly' type='SFTime'/>\n").append("  <field name='cycleTime'      accessType='inputOnly' type='SFTime'/>\n").append("  <field name='elapsedTime'    accessType='inputOutput' type='SFTime' value='-1'/>\n").append("  <field name='time'           accessType='inputOutput' type='SFTime' value='-1'/>\n").append("  <field name='enabled'        accessType='inputOnly' type='SFBool'/>\n").append("  <field name='loop'           accessType='inputOnly' type='SFBool'/>\n").append("  <field name='isActive'       accessType='inputOnly' type='SFBool'/>\n").append("  <field name='isPaused'       accessType='inputOnly' type='SFBool'/>\n").append("  <field name='timeStampPreviousReport' accessType='initializeOnly' type='SFTime' value='-1'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function fraction (eventValue, timeStamp) {\n").append("      // input eventValue received for trace field\n").append("      if (timeStamp - timeStampPreviousReport >= reportInterval) {\n").append("        Browser.print ('").append(scriptName).append(" type=SFFloat fraction_changed=' + eventValue + '\\n');\n").append("        timeStampPreviousReport = timeStamp;\n").append("      }\n").append("    }\n").append("    function cycleInterval (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFTime cycleInterval=' + eventValue.toString() + ' ' + timeOfDay (eventValue) + '\\n');\n").append("    }\n").append("    function startTime (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFTime startTime=' + eventValue.toString() + ' ' + timeOfDay (eventValue) + '\\n');\n").append("    }\n").append("    function stopTime (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFTime stopTime=' + eventValue.toString() + ' ' + timeOfDay (eventValue) + '\\n');\n").append("    }\n").append("    function pauseTime (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFTime pauseTime=' + eventValue.toString() + ' ' + timeOfDay (eventValue) + '\\n');\n").append("    }\n").append("    function resumeTime (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFTime resumeTime=' + eventValue.toString() + ' ' + timeOfDay (eventValue) + '\\n');\n").append("    }\n").append("    function cycleTime (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFTime cycleTime=' + eventValue + ' ' + timeOfDay(eventValue) + '\\n');\n").append("    }\n").append("    function enabled (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool enabled=' + eventValue + '\\n');\n").append("    }\n").append("    function loop (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool loop=' + eventValue + '\\n');\n").append("    }\n").append("    function isActive (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool isActive=' + eventValue + '\\n');\n").append("    }\n").append("    function isPaused (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool isPaused=' + eventValue + '\\n');\n").append("    }\n").append("    function timeOfDay (someTime) {\n").append("      hh = Math.floor (someTime /(60*60)) % 24;\n").append("      mm = Math.floor (someTime / 60)     % 60;\n").append("      ss = Math.floor (someTime)          % 60;\n").append("      if (hh < 9) hour   = '0' + hh;\n").append("      else        hour   =       hh;\n").append("      if (mm < 9) minute = '0' + mm;\n").append("      else        minute =       mm;\n").append("      if (ss < 9) second = '0' + ss;\n").append("      else        second =       ss;\n").append("      return '(' + hour + ':' + minute + ':' + second + ' GMT)';\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<!-- Authors can remove ROUTEs for fields that do not need to be traced, but unused fields do not report anyway -->\n").append("<!-- TODO BSContact TimeSensor errors: support missing for pauseTime, resumeTime, elapsedTime, isPaused  :(  -->\n").append("<ROUTE fromField='fraction_changed' fromNode='").append(getDEFUSE()).append("' toField='fraction'      toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='cycleInterval'    fromNode='").append(getDEFUSE()).append("' toField='cycleInterval' toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='startTime'        fromNode='").append(getDEFUSE()).append("' toField='startTime'     toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='stopTime'         fromNode='").append(getDEFUSE()).append("' toField='stopTime'      toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='cycleTime'        fromNode='").append(getDEFUSE()).append("' toField='cycleTime'     toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='time'             fromNode='").append(getDEFUSE()).append("' toField='time'          toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='enabled'          fromNode='").append(getDEFUSE()).append("' toField='enabled'       toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='loop'             fromNode='").append(getDEFUSE()).append("' toField='loop'          toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='isActive'         fromNode='").append(getDEFUSE()).append("' toField='isActive'      toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("TouchSensor")) {
                    String scriptName = "Trace_TouchSensor_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <field name='reportInterval' accessType='initializeOnly' type='SFTime' value='1.0' appinfo='Sampling frequency in seconds (0 means all values)'/>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='description'         accessType='inputOnly' type='SFString'/>\n").append("  <field name='enabled'             accessType='inputOnly' type='SFBool'/>\n").append("  <field name='isActive'            accessType='inputOnly' type='SFBool'/>\n").append("  <field name='isOver'              accessType='inputOnly' type='SFBool'/>\n").append("  <field name='touchTime'           accessType='inputOnly' type='SFTime'/>\n").append("  <field name='hitPoint_changed'    accessType='inputOnly' type='SFVec3f'/>\n").append("  <field name='hitNormal_changed'   accessType='inputOutput' type='SFVec3f' value='-1 -1 -1'/>\n").append("  <field name='hitTexCoord_changed' accessType='inputOutput' type='SFVec2f' value='-1 -1'/>\n").append("  <field name='timeStampPreviousReport' accessType='initializeOnly' type='SFTime' value='-1'/>\n").append("  <field name='hitPoint_Previous'       accessType='initializeOnly' type='SFVec3f' value='-1 -1 -1'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function description (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFString description=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function enabled (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool enabled=' + eventValue + '\\n');\n").append("    }\n").append("    function isActive (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool isActive=' + eventValue + '\\n');\n").append("    }\n").append("    function isOver (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool isOver=' + eventValue + '\\n');\n").append("    }\n").append("    function touchTime (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFTime touchTime=' + eventValue + ' ' + timeOfDay (eventValue) + '\\n') + '\\n';\n").append("    }\n").append("    function hitPoint_changed (eventValue, timeStamp) {\n").append("      if ((timeStamp - timeStampPreviousReport >= reportInterval) &&\n").append("          ((hitPoint_changed.x != hitPoint_Previous.x) || (hitPoint_changed.y != hitPoint_Previous.y) || (hitPoint_changed.z != hitPoint_Previous.z))) {\n").append("        Browser.print ('").append(scriptName).append(" type=SFVec3f    hitPoint_changed=' + eventValue.toString() + '\\n');\n").append("        Browser.print ('").append(scriptName).append(" type=SFVec3f   hitNormal_changed=' + eventValue.toString() + '\\n');\n").append("        Browser.print ('").append(scriptName).append(" type=SFVec2f hitTexCoord_changed=' + eventValue.toString() + '\\n');\n").append("        timeStampPreviousReport = timeStamp;\n").append("        hitPoint_Previous       = eventValue;\n").append("      }\n").append("    }\n").append("    function timeOfDay (someTime) {\n").append("      hh = Math.floor (someTime /(60*60)) % 24;\n").append("      mm = Math.floor (someTime / 60)     % 60;\n").append("      ss = Math.floor (someTime)          % 60;\n").append("      if (hh < 9) hour   = '0' + hh;  ").append("      else        hour   =       hh;\n").append("      if (mm < 9) minute = '0' + mm;  ").append("      else        minute =       mm;\n").append("      if (ss < 9) second = '0' + ss;  ").append("      else        second =       ss;\n").append("      return '(' + hour + ':' + minute + ':' + second + ' GMT)';\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='description'         fromNode='").append(getDEFUSE()).append("' toField='description'         toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='enabled'             fromNode='").append(getDEFUSE()).append("' toField='enabled'             toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='isActive'            fromNode='").append(getDEFUSE()).append("' toField='isActive'            toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='isOver'              fromNode='").append(getDEFUSE()).append("' toField='isOver'              toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='touchTime'           fromNode='").append(getDEFUSE()).append("' toField='touchTime'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='hitNormal_changed'   fromNode='").append(getDEFUSE()).append("' toField='hitNormal_changed'   toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='hitPoint_changed'    fromNode='").append(getDEFUSE()).append("' toField='hitPoint_changed'    toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='hitTexCoord_changed' fromNode='").append(getDEFUSE()).append("' toField='hitTexCoord_changed' toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("CylinderSensor")) {
                    String scriptName = "Trace_CylinderSensor_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <field name='reportInterval' accessType='initializeOnly' type='SFTime' value='1.0' appinfo='Sampling frequency in seconds (0 means all values)'/>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='description'         accessType='inputOnly' type='SFString'/>\n").append("  <field name='enabled'             accessType='inputOnly' type='SFBool'/>\n").append("  <field name='isActive'            accessType='inputOnly' type='SFBool'/>\n").append("  <field name='isOver'              accessType='inputOnly' type='SFBool'/>\n").append("  <field name='rotation_changed'    accessType='inputOnly' type='SFRotation'/>\n").append("  <field name='autoOffset'          accessType='inputOnly' type='SFBool'/>\n").append("  <field name='axisRotation'        accessType='inputOnly' type='SFRotation'/>\n").append("  <field name='diskAngle'           accessType='inputOnly' type='SFFloat'/>\n").append("  <field name='maxAngle'            accessType='inputOnly' type='SFFloat'/>\n").append("  <field name='minAngle'            accessType='inputOnly' type='SFFloat'/>\n").append("  <field name='offset'              accessType='inputOnly' type='SFFloat'/>\n").append("  <field name='trackPoint_changed'  accessType='inputOnly' type='SFVec3f'/>\n").append("  <field name='timeStampPreviousReport' accessType='initializeOnly' type='SFTime' value='-1'/>\n").append("  <field name='rotation_Previous'       accessType='initializeOnly' type='SFRotation' value='0 1 0 0'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function description (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFString description=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function enabled (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool enabled=' + eventValue + '\\n');\n").append("    }\n").append("    function isActive (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool isActive=' + eventValue + '\\n');\n").append("    }\n").append("    function isOver (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool isOver=' + eventValue + '\\n');\n").append("    }\n").append("    function rotation_changed (eventValue, timeStamp) {\n").append("      if ((timeStamp - timeStampPreviousReport >= reportInterval) &&\n").append("          ((rotation_changed.x != rotation_Previous.x) || (rotation_changed.y != rotation_Previous.y) || (rotation_changed.z != rotation_Previous.z) || (rotation_changed.angle != rotation_Previous.angle))) {\n").append("        Browser.print ('").append(scriptName).append(" type=SFRotation    rotation_changed=' + eventValue.toString() + '\\n');\n").append("        timeStampPreviousReport = timeStamp;\n").append("        rotation_Previous       = eventValue;\n").append("      }\n").append("    }\n").append("    function timeOfDay (someTime) {\n").append("      hh = Math.floor (someTime /(60*60)) % 24;\n").append("      mm = Math.floor (someTime / 60)     % 60;\n").append("      ss = Math.floor (someTime)          % 60;\n").append("      if (hh < 9) hour   = '0' + hh;  ").append("      else        hour   =       hh;\n").append("      if (mm < 9) minute = '0' + mm;  ").append("      else        minute =       mm;\n").append("      if (ss < 9) second = '0' + ss;  ").append("      else        second =       ss;\n").append("      return '(' + hour + ':' + minute + ':' + second + ' GMT)';\n").append("    }\n").append("    function autoOffset (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool autoOffset=' + eventValue + '\\n') + '\\n';\n").append("    }\n").append("    function axisRotation (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFRotation axisRotation=' + eventValue.toString() + '\\n') + '\\n';\n").append("    }\n").append("    function diskAngle (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFFloat diskAngle=' + eventValue + '\\n') + '\\n';\n").append("    }\n").append("    function maxAngle (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFFloat maxAngle=' + eventValue + '\\n') + '\\n';\n").append("    }\n").append("    function minAngle (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFFloat minAngle=' + eventValue + '\\n') + '\\n';\n").append("    }\n").append("    function offset (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFFloat offset=' + eventValue + '\\n') + '\\n';\n").append("    }\n").append("    function trackPoint_changed (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFVec3f trackPoint_changed=' + eventValue.toString() + '\\n') + '\\n';\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='description'         fromNode='").append(getDEFUSE()).append("' toField='description'         toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='enabled'             fromNode='").append(getDEFUSE()).append("' toField='enabled'             toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='isActive'            fromNode='").append(getDEFUSE()).append("' toField='isActive'            toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='isOver'              fromNode='").append(getDEFUSE()).append("' toField='isOver'              toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='rotation_changed'    fromNode='").append(getDEFUSE()).append("' toField='rotation_changed'    toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='autoOffset'          fromNode='").append(getDEFUSE()).append("' toField='autoOffset'          toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='axisRotation'        fromNode='").append(getDEFUSE()).append("' toField='axisRotation'        toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='diskAngle'           fromNode='").append(getDEFUSE()).append("' toField='diskAngle'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='maxAngle'            fromNode='").append(getDEFUSE()).append("' toField='maxAngle'            toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='minAngle'            fromNode='").append(getDEFUSE()).append("' toField='minAngle'            toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='offset'              fromNode='").append(getDEFUSE()).append("' toField='offset'              toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='trackPoint_changed'  fromNode='").append(getDEFUSE()).append("' toField='trackPoint_changed'  toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("PlaneSensor")) {
                    String scriptName = "Trace_PlaneSensor_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <field name='reportInterval' accessType='initializeOnly' type='SFTime' value='1.0' appinfo='Sampling frequency in seconds (0 means all values)'/>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='description'         accessType='inputOnly' type='SFString'/>\n").append("  <field name='enabled'             accessType='inputOnly' type='SFBool'/>\n").append("  <field name='isActive'            accessType='inputOnly' type='SFBool'/>\n").append("  <field name='isOver'              accessType='inputOnly' type='SFBool'/>\n").append("  <field name='translation_changed' accessType='inputOnly' type='SFVec3f'/>\n").append("  <field name='autoOffset'          accessType='inputOnly' type='SFBool'/>\n").append("  <field name='axisRotation'        accessType='inputOnly' type='SFRotation'/>\n").append("  <field name='maxPosition'         accessType='inputOnly' type='SFVec3f'/>\n").append("  <field name='minPosition'         accessType='inputOnly' type='SFVec3f'/>\n").append("  <field name='offset'              accessType='inputOnly' type='SFVec3f'/>\n").append("  <field name='trackPoint_changed'  accessType='inputOnly' type='SFVec3f'/>\n").append("  <field name='timeStampPreviousReport' accessType='initializeOnly' type='SFTime' value='-1'/>\n").append("  <field name='translation_Previous'       accessType='initializeOnly' type='SFVec3f' value='-1 -1 -1'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function description (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFString description=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function enabled (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool enabled=' + eventValue + '\\n');\n").append("    }\n").append("    function isActive (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool isActive=' + eventValue + '\\n');\n").append("    }\n").append("    function isOver (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool isOver=' + eventValue + '\\n');\n").append("    }\n").append("    function translation_changed (eventValue, timeStamp) {\n").append("      if ((timeStamp - timeStampPreviousReport >= reportInterval) &&\n").append("          ((translation_changed.x != translation_Previous.x) || (translation_changed.y != translation_Previous.y) || (translation_changed.z != translation_Previous.z))) {\n").append("        Browser.print ('").append(scriptName).append(" type=SFVec3f    translation_changed=' + eventValue.toString() + '\\n');\n").append("        timeStampPreviousReport = timeStamp;\n").append("        translation_Previous       = eventValue;\n").append("      }\n").append("    }\n").append("    function timeOfDay (someTime) {\n").append("      hh = Math.floor (someTime /(60*60)) % 24;\n").append("      mm = Math.floor (someTime / 60)     % 60;\n").append("      ss = Math.floor (someTime)          % 60;\n").append("      if (hh < 9) hour   = '0' + hh;  ").append("      else        hour   =       hh;\n").append("      if (mm < 9) minute = '0' + mm;  ").append("      else        minute =       mm;\n").append("      if (ss < 9) second = '0' + ss;  ").append("      else        second =       ss;\n").append("      return '(' + hour + ':' + minute + ':' + second + ' GMT)';\n").append("    }\n").append("    function autoOffset (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool autoOffset=' + eventValue + '\\n') + '\\n';\n").append("    }\n").append("    function axisRotation (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFRotation axisRotation=' + eventValue.toString() + '\\n') + '\\n';\n").append("    }\n").append("    function maxPosition (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFVec3f maxPosition=' + eventValue.toString() + '\\n') + '\\n';\n").append("    }\n").append("    function minPosition (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFVec3f minPosition=' + eventValue.toString() + '\\n') + '\\n';\n").append("    }\n").append("    function offset (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFVec3f offset=' + eventValue.toString() + '\\n') + '\\n';\n").append("    }\n").append("    function trackPoint_changed (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFVec3f trackPoint_changed=' + eventValue.toString() + '\\n') + '\\n';\n").append("    }\n").append("    function translation_changed (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFVec3f translation_changed=' + eventValue.toString() + '\\n') + '\\n';\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='description'         fromNode='").append(getDEFUSE()).append("' toField='description'         toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='enabled'             fromNode='").append(getDEFUSE()).append("' toField='enabled'             toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='isActive'            fromNode='").append(getDEFUSE()).append("' toField='isActive'            toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='isOver'              fromNode='").append(getDEFUSE()).append("' toField='isOver'              toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='translation_changed' fromNode='").append(getDEFUSE()).append("' toField='translation_changed' toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='autoOffset'          fromNode='").append(getDEFUSE()).append("' toField='autoOffset'          toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='axisRotation'        fromNode='").append(getDEFUSE()).append("' toField='axisRotation'        toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='maxPosition'         fromNode='").append(getDEFUSE()).append("' toField='maxPosition'         toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='minPosition'         fromNode='").append(getDEFUSE()).append("' toField='minPosition'         toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='offset'              fromNode='").append(getDEFUSE()).append("' toField='offset'              toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='trackPoint_changed'  fromNode='").append(getDEFUSE()).append("' toField='trackPoint_changed'  toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("SphereSensor")) {
                    String scriptName = "Trace_SphereSensor_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <field name='reportInterval' accessType='initializeOnly' type='SFTime' value='1.0' appinfo='Sampling frequency in seconds (0 means all values)'/>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='description'             accessType='inputOnly' type='SFString'/>\n").append("  <field name='enabled'                 accessType='inputOnly' type='SFBool'/>\n").append("  <field name='isActive'                accessType='inputOnly' type='SFBool'/>\n").append("  <field name='isOver'                  accessType='inputOnly' type='SFBool'/>\n").append("  <field name='rotation_changed'        accessType='inputOnly' type='SFRotation'/>\n").append("  <field name='offset'                  accessType='inputOnly' type='SFRotation'/>\n").append("  <field name='trackPoint_changed'      accessType='inputOnly' type='SFVec3f'/>\n").append("  <field name='timeStampPreviousReport' accessType='initializeOnly' type='SFTime' value='-1'/>\n").append("  <field name='rotation_Previous'       accessType='initializeOnly' type='SFRotation' value='0 1 0 0'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function description (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFString description=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function enabled (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool enabled=' + eventValue + '\\n');\n").append("    }\n").append("    function isActive (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool isActive=' + eventValue + '\\n');\n").append("    }\n").append("    function isOver (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool isOver=' + eventValue + '\\n');\n").append("    }\n").append("    function rotation_changed (eventValue, timeStamp) {\n").append("      if ((timeStamp - timeStampPreviousReport >= reportInterval) &&\n").append("          ((rotation_changed.x != rotation_Previous.x) || (rotation_changed.y != rotation_Previous.y) || (rotation_changed.z != rotation_Previous.z) || (rotation_changed.angle != rotation_Previous.angle))) {\n").append("        Browser.print ('").append(scriptName).append(" type=SRotation rotation_changed=' + eventValue.toString() + ' ' + timeOfDay (timeStamp) + '\\n');\n").append("        timeStampPreviousReport = timeStamp;\n").append("        rotation_Previous    = eventValue;\n").append("      }\n").append("    }\n").append("    function timeOfDay (someTime) {\n").append("      hh = Math.floor (someTime /(60*60)) % 24;\n").append("      mm = Math.floor (someTime / 60)     % 60;\n").append("      ss = Math.floor (someTime)          % 60;\n").append("      if (hh < 9) hour   = '0' + hh;  ").append("      else        hour   =       hh;\n").append("      if (mm < 9) minute = '0' + mm;  ").append("      else        minute =       mm;\n").append("      if (ss < 9) second = '0' + ss;  ").append("      else        second =       ss;\n").append("      return '(' + hour + ':' + minute + ':' + second + ' GMT)';\n").append("    }\n").append("    function offset (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFRotation offset=' + eventValue.toString() + '\\n') + '\\n';\n").append("    }\n").append("    function trackPoint_changed (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFVec3f trackPoint_changed=' + eventValue.toString() + '\\n') + '\\n';\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='description'         fromNode='").append(getDEFUSE()).append("' toField='description'         toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='enabled'             fromNode='").append(getDEFUSE()).append("' toField='enabled'             toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='isActive'            fromNode='").append(getDEFUSE()).append("' toField='isActive'            toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='isOver'              fromNode='").append(getDEFUSE()).append("' toField='isOver'              toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='rotation_changed'    fromNode='").append(getDEFUSE()).append("' toField='rotation_changed'    toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='offset'              fromNode='").append(getDEFUSE()).append("' toField='offset'              toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='trackPoint_changed'  fromNode='").append(getDEFUSE()).append("' toField='trackPoint_changed'  toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("ScalarInterpolator")) {
                    String scriptName = "Trace_ScalarInterpolator_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <field name='reportInterval' accessType='initializeOnly' type='SFTime' value='1.0' appinfo='Sampling frequency in seconds (0 means all values)'/>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='value_changed'           accessType='inputOnly' type='SFFloat'/>\n").append("  <field name='key'                     accessType='inputOnly' type='MFFloat'/>\n").append("  <field name='keyValue'                accessType='inputOnly' type='MFFloat'/>\n").append("  <field name='timeStampPreviousReport' accessType='initializeOnly' type='SFTime' value='-1'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function value_changed (eventValue, timeStamp) {\n").append("      if (timeStamp - timeStampPreviousReport >= reportInterval) {\n").append("        Browser.print ('").append(scriptName).append(" type=SFFloat value_changed=' + eventValue + '\\n');\n").append("        timeStampPreviousReport = timeStamp;\n").append("      }\n").append("    }\n").append("    function key (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFFloat key=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function keyValue (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFFloat keyValue=' + eventValue.toString() + '\\n');\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='value_changed' fromNode='").append(getDEFUSE()).append("' toField='value_changed' toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='key'           fromNode='").append(getDEFUSE()).append("' toField='key'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='keyValue'      fromNode='").append(getDEFUSE()).append("' toField='keyValue'      toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("PositionInterpolator2D")) {
                    String scriptName = "Trace_PositionInterpolator2D_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <field name='reportInterval' accessType='initializeOnly' type='SFTime' value='1.0' appinfo='Sampling frequency in seconds (0 means all values)'/>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='value_changed'           accessType='inputOnly' type='SFVec2f'/>\n").append("  <field name='key'                     accessType='inputOnly' type='MFFloat'/>\n").append("  <field name='keyValue'                accessType='inputOnly' type='MFVec2f'/>\n").append("  <field name='timeStampPreviousReport' accessType='initializeOnly' type='SFTime' value='-1'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function value_changed (eventValue, timeStamp) {\n").append("      if (timeStamp - timeStampPreviousReport >= reportInterval) {\n").append("        Browser.print ('").append(scriptName).append(" type=SFVec2f value_changed=' + eventValue.toString() + '\\n');\n").append("        timeStampPreviousReport = timeStamp;\n").append("      }\n").append("    }\n").append("    function key (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFFloat key=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function keyValue (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFVec2f keyValue=' + eventValue.toString() + '\\n');\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='value_changed' fromNode='").append(getDEFUSE()).append("' toField='value_changed' toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='key'           fromNode='").append(getDEFUSE()).append("' toField='key'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='keyValue'      fromNode='").append(getDEFUSE()).append("' toField='keyValue'      toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("PositionInterpolator")) {
                    String scriptName = "Trace_PositionInterpolator_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <field name='reportInterval' accessType='initializeOnly' type='SFTime' value='1.0' appinfo='Sampling frequency in seconds (0 means all values)'/>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='value_changed'           accessType='inputOnly' type='SFVec3f'/>\n").append("  <field name='key'                     accessType='inputOnly' type='MFFloat'/>\n").append("  <field name='keyValue'                accessType='inputOnly' type='MFVec3f'/>\n").append("  <field name='timeStampPreviousReport' accessType='initializeOnly' type='SFTime' value='-1'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function value_changed (eventValue, timeStamp) {\n").append("      if (timeStamp - timeStampPreviousReport >= reportInterval) {\n").append("        Browser.print ('").append(scriptName).append(" type=SFVec3f value_changed=' + eventValue.toString() + '\\n');\n").append("        timeStampPreviousReport = timeStamp;\n").append("      }\n").append("    }\n").append("    function key (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFFloat key=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function keyValue (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFVec3f keyValue=' + eventValue.toString() + '\\n');\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='value_changed' fromNode='").append(getDEFUSE()).append("' toField='value_changed' toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='key'           fromNode='").append(getDEFUSE()).append("' toField='key'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='keyValue'      fromNode='").append(getDEFUSE()).append("' toField='keyValue'      toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("NormalInterpolator")) {
                    String scriptName = "Trace_NormalInterpolator_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <field name='reportInterval' accessType='initializeOnly' type='SFTime' value='1.0' appinfo='Sampling frequency in seconds (0 means all values)'/>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='value_changed'           accessType='inputOnly' type='MFVec3f'/>\n").append("  <field name='key'                     accessType='inputOnly' type='MFFloat'/>\n").append("  <field name='keyValue'                accessType='inputOnly' type='MFVec3f'/>\n").append("  <field name='timeStampPreviousReport' accessType='initializeOnly' type='SFTime' value='-1'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function value_changed (eventValue, timeStamp) {\n").append("      if (timeStamp - timeStampPreviousReport >= reportInterval) {\n").append("        Browser.print ('").append(scriptName).append(" type=MFVec3f value_changed=' + eventValue.toString() + '\\n');\n").append("        timeStampPreviousReport = timeStamp;\n").append("      }\n").append("    }\n").append("    function key (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFFloat key=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function keyValue (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFVec3f keyValue=' + eventValue.toString() + '\\n');\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='value_changed' fromNode='").append(getDEFUSE()).append("' toField='value_changed' toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='key'           fromNode='").append(getDEFUSE()).append("' toField='key'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='keyValue'      fromNode='").append(getDEFUSE()).append("' toField='keyValue'      toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("ColorInterpolator")) {
                    String scriptName = "Trace_ColorInterpolator_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <field name='reportInterval' accessType='initializeOnly' type='SFTime' value='1.0' appinfo='Sampling frequency in seconds (0 means all values)'/>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='value_changed'           accessType='inputOnly' type='SFColor'/>\n").append("  <field name='key'                     accessType='inputOnly' type='MFFloat'/>\n").append("  <field name='keyValue'                accessType='inputOnly' type='MFColor'/>\n").append("  <field name='timeStampPreviousReport' accessType='initializeOnly' type='SFTime' value='-1'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function value_changed (eventValue, timeStamp) {\n").append("      if (timeStamp - timeStampPreviousReport >= reportInterval) {\n").append("        Browser.print ('").append(scriptName).append(" type=SFColor value_changed=' + eventValue.toString() + '\\n');\n").append("        timeStampPreviousReport = timeStamp;\n").append("      }\n").append("    }\n").append("    function key (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFFloat key=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function keyValue (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFColor keyValue=' + eventValue.toString() + '\\n');\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='value_changed' fromNode='").append(getDEFUSE()).append("' toField='value_changed' toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='key'           fromNode='").append(getDEFUSE()).append("' toField='key'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='keyValue'      fromNode='").append(getDEFUSE()).append("' toField='keyValue'      toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("OrientationInterpolator")) {
                    String scriptName = "Trace_OrientationInterpolator_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <field name='reportInterval' accessType='initializeOnly' type='SFTime' value='1.0' appinfo='Sampling frequency in seconds (0 means all values)'/>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='value_changed'           accessType='inputOnly' type='SFRotation'/>\n").append("  <field name='key'                     accessType='inputOnly' type='MFFloat'/>\n").append("  <field name='keyValue'                accessType='inputOnly' type='MFRotation'/>\n").append("  <field name='timeStampPreviousReport' accessType='initializeOnly' type='SFTime' value='-1'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function value_changed (eventValue, timeStamp) {\n").append("      if (timeStamp - timeStampPreviousReport >= reportInterval) {\n").append("        Browser.print ('").append(scriptName).append(" type=SFRotation value_changed=' + eventValue.toString() + '\\n');\n").append("        timeStampPreviousReport = timeStamp;\n").append("      }\n").append("    }\n").append("    function key (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFFloat key=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function keyValue (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFRotation keyValue=' + eventValue.toString() + '\\n');\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='value_changed' fromNode='").append(getDEFUSE()).append("' toField='value_changed' toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='key'           fromNode='").append(getDEFUSE()).append("' toField='key'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='keyValue'      fromNode='").append(getDEFUSE()).append("' toField='keyValue'      toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("CoordinateInterpolator")) {
                    String scriptName = "Trace_CoordinateInterpolator_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <field name='reportInterval' accessType='initializeOnly' type='SFTime' value='1.0' appinfo='Sampling frequency in seconds (0 means all values)'/>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='value_changed'           accessType='inputOnly' type='MFVec3f'/>\n").append("  <field name='key'                     accessType='inputOnly' type='MFFloat'/>\n").append("  <field name='keyValue'                accessType='inputOnly' type='MFVec3f'/>\n").append("  <field name='timeStampPreviousReport' accessType='initializeOnly' type='SFTime' value='-1'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function value_changed (eventValue, timeStamp) {\n").append("      if (timeStamp - timeStampPreviousReport >= reportInterval) {\n").append("        Browser.print ('").append(scriptName).append(" type=MFVec3f value_changed=' + eventValue.toString() + '\\n');\n").append("        timeStampPreviousReport = timeStamp;\n").append("      }\n").append("    }\n").append("    function key (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFFloat key=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function keyValue (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFVec3f keyValue=' + eventValue.toString() + '\\n');\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='value_changed' fromNode='").append(getDEFUSE()).append("' toField='value_changed' toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='key'           fromNode='").append(getDEFUSE()).append("' toField='key'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='keyValue'      fromNode='").append(getDEFUSE()).append("' toField='keyValue'      toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("CoordinateInterpolator2D")) {
                    String scriptName = "Trace_CoordinateInterpolator2D_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <field name='reportInterval' accessType='initializeOnly' type='SFTime' value='1.0' appinfo='Sampling frequency in seconds (0 means all values)'/>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='value_changed'           accessType='inputOnly' type='MFVec2f'/>\n").append("  <field name='key'                     accessType='inputOnly' type='MFFloat'/>\n").append("  <field name='keyValue'                accessType='inputOnly' type='MFVec2f'/>\n").append("  <field name='timeStampPreviousReport' accessType='initializeOnly' type='SFTime' value='-1'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function value_changed (eventValue, timeStamp) {\n").append("      if (timeStamp - timeStampPreviousReport >= reportInterval) {\n").append("        Browser.print ('").append(scriptName).append(" type=MFVec2f value_changed=' + eventValue.toString() + '\\n');\n").append("        timeStampPreviousReport = timeStamp;\n").append("      }\n").append("    }\n").append("    function key (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFFloat key=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function keyValue (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFVec2f keyValue=' + eventValue.toString() + '\\n');\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='value_changed' fromNode='").append(getDEFUSE()).append("' toField='value_changed' toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='key'           fromNode='").append(getDEFUSE()).append("' toField='key'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='keyValue'      fromNode='").append(getDEFUSE()).append("' toField='keyValue'      toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("BooleanSequencer")) {
                    String scriptName = "Trace_BooleanSequencer_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <field name='reportInterval' accessType='initializeOnly' type='SFTime' value='1.0' appinfo='Sampling frequency in seconds (0 means all values)'/>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='value_changed'           accessType='inputOnly' type='SFBool'/>\n").append("  <field name='key'                     accessType='inputOnly' type='MFFloat'/>\n").append("  <field name='keyValue'                accessType='inputOnly' type='MFBool'/>\n").append("  <field name='timeStampPreviousReport' accessType='initializeOnly' type='SFTime' value='-1'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function value_changed (eventValue, timeStamp) {\n").append("      if (timeStamp - timeStampPreviousReport >= reportInterval) {\n").append("        Browser.print ('").append(scriptName).append(" type=SFBool value_changed=' + eventValue + '\\n');\n").append("        timeStampPreviousReport = timeStamp;\n").append("      }\n").append("    }\n").append("    function key (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFFloat key=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function keyValue (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFBool keyValue=' + eventValue.toString() + '\\n');\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='value_changed' fromNode='").append(getDEFUSE()).append("' toField='value_changed' toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='key'           fromNode='").append(getDEFUSE()).append("' toField='key'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='keyValue'      fromNode='").append(getDEFUSE()).append("' toField='keyValue'      toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("IntegerSequencer")) {
                    String scriptName = "Trace_IntegerSequencer_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <field name='reportInterval' accessType='initializeOnly' type='SFTime' value='1.0' appinfo='Sampling frequency in seconds (0 means all values)'/>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='value_changed'           accessType='inputOnly' type='SFInt32'/>\n").append("  <field name='key'                     accessType='inputOnly' type='MFFloat'/>\n").append("  <field name='keyValue'                accessType='inputOnly' type='MFInt32'/>\n").append("  <field name='timeStampPreviousReport' accessType='initializeOnly' type='SFTime' value='-1'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function value_changed (eventValue, timeStamp) {\n").append("      if (timeStamp - timeStampPreviousReport >= reportInterval) {\n").append("        Browser.print ('").append(scriptName).append(" type=SFInt32 value_changed=' + eventValue + '\\n');\n").append("        timeStampPreviousReport = timeStamp;\n").append("      }\n").append("    }\n").append("    function key (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFFloat key=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function keyValue (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFInt32 keyValue=' + eventValue.toString() + '\\n');\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='value_changed' fromNode='").append(getDEFUSE()).append("' toField='value_changed' toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='key'           fromNode='").append(getDEFUSE()).append("' toField='key'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='keyValue'      fromNode='").append(getDEFUSE()).append("' toField='keyValue'      toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("BooleanFilter")) {
                    String scriptName = "Trace_BooleanFilter_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='inputTrue'           accessType='inputOnly' type='SFBool'/>\n").append("  <field name='inputFalse'          accessType='inputOnly' type='SFBool'/>\n").append("  <field name='inputNegate'         accessType='inputOnly' type='SFBool'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function inputTrue (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool inputTrue=' + eventValue + '\\n');\n").append("    }\n").append("    function inputFalse (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool inputFalse=' + eventValue + '\\n');\n").append("    }\n").append("    function inputNegate (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool inputNegate=' + eventValue + '\\n');\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='inputTrue'      fromNode='").append(getDEFUSE()).append("' toField='inputTrue'      toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='inputFalse'     fromNode='").append(getDEFUSE()).append("' toField='inputFalse'     toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='inputNegate'    fromNode='").append(getDEFUSE()).append("' toField='inputNegate'    toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("BooleanToggle")) {
                    String scriptName = "Trace_BooleanToggle_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='toggle'           accessType='inputOnly' type='SFBool'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function toggle (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool toggle=' + eventValue + '\\n');\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='toggle'      fromNode='").append(getDEFUSE()).append("' toField='toggle'      toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("BooleanTrigger")) {
                    String scriptName = "Trace_BooleanTrigger_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='triggerTrue'           accessType='inputOnly' type='SFBool'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function toggle (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool triggerTrue=' + eventValue + '\\n');\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='triggerTrue'      fromNode='").append(getDEFUSE()).append("' toField='triggerTrue'      toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("IntegerTrigger")) {
                    String scriptName = "Trace_IntegerTrigger_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='integerKey'           accessType='inputOnly' type='SFInt32'/>\n").append("  <field name='triggerValue'         accessType='inputOnly' type='SFInt32'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function integerKey (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFInt32 integerKey=' + eventValue + '\\n');\n").append("    }\n").append("    function triggerValue (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFInt32 triggerValue=' + eventValue + '\\n');\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='integerKey'        fromNode='").append(getDEFUSE()).append("' toField='integerKey'        toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='triggerValue'      fromNode='").append(getDEFUSE()).append("' toField='triggerValue'      toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("TimeTrigger")) {
                    String scriptName = "Trace_TimeTrigger_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='triggerTime'           accessType='inputOnly' type='SFTime'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function triggerTime (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFTime triggerTime=' + eventValue + ' ' + timeOfDay (eventValue) + '\\n');\n").append("    }\n").append("    function timeOfDay (someTime) {\n").append("      hh = Math.floor (someTime /(60*60)) % 24;\n").append("      mm = Math.floor (someTime / 60)     % 60;\n").append("      ss = Math.floor (someTime)          % 60;\n").append("      if (hh < 9) hour   = '0' + hh;  ").append("      else        hour   =       hh;\n").append("      if (mm < 9) minute = '0' + mm;  ").append("      else        minute =       mm;\n").append("      if (ss < 9) second = '0' + ss;  ").append("      else        second =       ss;\n").append("      return '(' + hour + ':' + minute + ':' + second + ' GMT)';\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='triggerTime'      fromNode='").append(getDEFUSE()).append("' toField='triggerTime'      toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("KeySensor")) {
                    String scriptName = "Trace_KeySensor_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='enabled'           accessType='inputOnly' type='SFBool'/>\n").append("  <field name='isActive'          accessType='inputOnly' type='SFBool'/>\n").append("  <field name='actionKeyPress'    accessType='inputOnly' type='SFInt32'/>\n").append("  <field name='actionKeyRelease'  accessType='inputOnly' type='SFInt32'/>\n").append("  <field name='keyPress'          accessType='inputOnly' type='SFString'/>\n").append("  <field name='keyRelease'        accessType='inputOnly' type='SFString'/>\n").append("  <field name='altKey'            accessType='inputOnly' type='SFBool'/>\n").append("  <field name='controlKey'        accessType='inputOnly' type='SFBool'/>\n").append("  <field name='shiftKey'          accessType='inputOnly' type='SFBool'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function enabled (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool enabled=' + eventValue + '\\n');\n").append("    }\n").append("    function isActive (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool isActive=' + eventValue + '\\n');\n").append("    }\n").append("    function actionKeyPress (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFInt32 actionKeyPress=' + eventValue + ' ' + keyCode (eventValue) + '\\n');\n").append("    }\n").append("    function actionKeyRelease (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFInt32 actionKeyRelease=' + eventValue + ' ' + keyCode (eventValue) + '\\n');\n").append("    }\n").append("    function keyPress (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFString keyPress=' + eventValue + '\\n');\n").append("    }\n").append("    function keyRelease (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFString keyRelease=' + eventValue + '\\n');\n").append("    }\n").append("    function altKey (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool altKey=' + eventValue + '\\n');\n").append("    }\n").append("    function controlKey (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool controlKey=' + eventValue + '\\n');\n").append("    }\n").append("    function shiftKey (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool shiftKey=' + eventValue + '\\n');\n").append("    }\n").append("    function keyCode (eventValue) {\n").append("      var codeArray = ['F1','F2','F3','F4','F5','F6','F7','F8','F9','F10','F11','F12','HOME','END','PGUP','PGDN','UP','DOWN','LEFT','RIGHT'];\n").append("      if ((eventValue >= 1) && (eventValue <= 20)) return codeArray[eventValue];\n").append("      else return 'UNKNOWN';\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='enabled'             fromNode='").append(getDEFUSE()).append("' toField='enabled'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='isActive'            fromNode='").append(getDEFUSE()).append("' toField='isActive'          toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='actionKeyPress'      fromNode='").append(getDEFUSE()).append("' toField='actionKeyPress'    toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='actionKeyRelease'    fromNode='").append(getDEFUSE()).append("' toField='actionKeyRelease'  toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='keyPress'            fromNode='").append(getDEFUSE()).append("' toField='keyPress'          toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='keyRelease'          fromNode='").append(getDEFUSE()).append("' toField='keyRelease'        toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='altKey'              fromNode='").append(getDEFUSE()).append("' toField='altKey'            toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='controlKey'          fromNode='").append(getDEFUSE()).append("' toField='controlKey'        toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='shiftKey'            fromNode='").append(getDEFUSE()).append("' toField='shiftKey'          toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("StringSensor")) {
                    String scriptName = "Trace_StringSensor_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='enabled'           accessType='inputOnly' type='SFBool'/>\n").append("  <field name='isActive'          accessType='inputOnly' type='SFBool'/>\n").append("  <field name='deletionAllowed'   accessType='inputOnly' type='SFBool'/>\n").append("  <field name='enteredText'       accessType='inputOnly' type='SFString'/>\n").append("  <field name='finalText'         accessType='inputOnly' type='SFString'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function enabled (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool enabled=' + eventValue + '\\n');\n").append("    }\n").append("    function isActive (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool isActive=' + eventValue + '\\n');\n").append("    }\n").append("    function deletionAllowed (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool deletionAllowed=' + eventValue + '\\n');\n").append("    }\n").append("    function enteredText (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFString enteredText=' + eventValue + '\\n');\n").append("    }\n").append("    function finalText (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFString finalText=' + eventValue + '\\n');\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='enabled'             fromNode='").append(getDEFUSE()).append("' toField='enabled'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='isActive'            fromNode='").append(getDEFUSE()).append("' toField='isActive'          toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='deletionAllowed'     fromNode='").append(getDEFUSE()).append("' toField='deletionAllowed'   toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='enteredText'         fromNode='").append(getDEFUSE()).append("' toField='enteredText'       toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='finalText'           fromNode='").append(getDEFUSE()).append("' toField='finalText'         toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("Collision")) {
                    String scriptName = "Trace_Collision_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='enabled'           accessType='inputOnly' type='SFBool'/>\n").append("  <field name='isActive'          accessType='inputOnly' type='SFBool'/>\n").append("  <field name='collideTime'       accessType='inputOnly' type='SFTime'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function enabled (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool enabled=' + eventValue + '\\n');\n").append("    }\n").append("    function isActive (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool isActive=' + eventValue + '\\n');\n").append("    }\n").append("    function collideTime (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFTime collideTime=' + eventValue + ' ' + timeOfDay (eventValue) + '\\n');\n").append("    }\n").append("    function timeOfDay (someTime) {\n").append("      hh = Math.floor (someTime /(60*60)) % 24;\n").append("      mm = Math.floor (someTime / 60)     % 60;\n").append("      ss = Math.floor (someTime)          % 60;\n").append("      if (hh < 9) hour   = '0' + hh;  ").append("      else        hour   =       hh;\n").append("      if (mm < 9) minute = '0' + mm;  ").append("      else        minute =       mm;\n").append("      if (ss < 9) second = '0' + ss;  ").append("      else        second =       ss;\n").append("      return '(' + hour + ':' + minute + ':' + second + ' GMT)';\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='enabled'          fromNode='").append(getDEFUSE()).append("' toField='enabled'          toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='isActive'         fromNode='").append(getDEFUSE()).append("' toField='isActive'         toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='collideTime'      fromNode='").append(getDEFUSE()).append("' toField='collideTime'      toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("LOD")) {
                    String scriptName = "Trace_LOD_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='level_changed'       accessType='inputOnly' type='SFInt32'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function level_changed (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFInt32 level_changed=' + eventValue + '\\n');\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='level_changed'      fromNode='").append(getDEFUSE()).append("' toField='level_changed'      toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("NavigationInfo")) {
                    String scriptName = "Trace_NavigationInfo_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='bindTime'           accessType='inputOnly' type='SFTime'/>\n").append("  <field name='isBound'            accessType='inputOnly' type='SFBool'/>\n").append("  <field name='transitionTime'     accessType='inputOnly' type='SFTime'/>\n").append("  <field name='transitionComplete' accessType='inputOnly' type='SFBool'/>\n").append("  <field name='avatarSize'         accessType='inputOnly' type='MFFloat'/>\n").append("  <field name='headlight'          accessType='inputOnly' type='SFBool'/>\n").append("  <field name='speed'              accessType='inputOnly' type='SFFloat'/>\n").append("  <field name='transitionType'     accessType='inputOnly' type='MFString'/>\n").append("  <field name='type'               accessType='inputOnly' type='MFString'/>\n").append("  <field name='visibilityLimit'    accessType='inputOnly' type='SFFloat'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function bindTime (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFTime bindTime=' + eventValue + ' ' + timeOfDay (eventValue) + '\\n');\n").append("    }\n").append("    function isBound (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool isBound=' + eventValue + '\\n');\n").append("    }\n").append("    function transitionTime (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFTime transitionTime=' + eventValue + ' ' + timeOfDay (eventValue) + '\\n');\n").append("    }\n").append("    function transitionComplete (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool transitionComplete=' + eventValue + '\\n');\n").append("    }\n").append("    function avatarSize (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFFloat avatarSize=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function headlight (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool headlight=' + eventValue + '\\n');\n").append("    }\n").append("    function speed (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFFloat speed=' + eventValue + '\\n');\n").append("    }\n").append("    function transitionType (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFString transitionType=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function type (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFString type=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function visibilityLimit (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFFloat visibilityLimit=' + eventValue + '\\n');\n").append("    }\n").append("    function timeOfDay (someTime) {\n").append("      hh = Math.floor (someTime /(60*60)) % 24;\n").append("      mm = Math.floor (someTime / 60)     % 60;\n").append("      ss = Math.floor (someTime)          % 60;\n").append("      if (hh < 9) hour   = '0' + hh;  ").append("      else        hour   =       hh;\n").append("      if (mm < 9) minute = '0' + mm;  ").append("      else        minute =       mm;\n").append("      if (ss < 9) second = '0' + ss;  ").append("      else        second =       ss;\n").append("      return '(' + hour + ':' + minute + ':' + second + ' GMT)';\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='bindTime'           fromNode='").append(getDEFUSE()).append("' toField='bindTime'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='isBound'            fromNode='").append(getDEFUSE()).append("' toField='isBound'            toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='transitionTime'     fromNode='").append(getDEFUSE()).append("' toField='transitionTime'     toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='transitionComplete' fromNode='").append(getDEFUSE()).append("' toField='transitionComplete' toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='avatarSize'         fromNode='").append(getDEFUSE()).append("' toField='avatarSize'         toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='headlight'          fromNode='").append(getDEFUSE()).append("' toField='headlight'          toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='speed'              fromNode='").append(getDEFUSE()).append("' toField='speed'              toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='transitionType'     fromNode='").append(getDEFUSE()).append("' toField='transitionType'     toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='type'               fromNode='").append(getDEFUSE()).append("' toField='type'               toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='visibilityLimit'    fromNode='").append(getDEFUSE()).append("' toField='visibilityLimit'    toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("Viewpoint")) {
                    String scriptName = "Trace_Viewpoint_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='bindTime'           accessType='inputOnly' type='SFTime'/>\n").append("  <field name='isBound'            accessType='inputOnly' type='SFBool'/>\n").append("  <field name='description'        accessType='inputOnly' type='SFString'/>\n").append("  <field name='fieldOfView'        accessType='inputOnly' type='SFFloat'/>\n").append("  <field name='centerOfRotation'   accessType='inputOnly' type='SFVec3f'/>\n").append("  <field name='position'           accessType='inputOnly' type='SFVec3f'/>\n").append("  <field name='orientation'        accessType='inputOnly' type='SFRotation'/>\n").append("  <field name='jump'               accessType='inputOnly' type='SFBool'/>\n").append("  <field name='retainUserOffsets'  accessType='inputOnly' type='SFBool'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function bindTime (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFTime bindTime=' + eventValue + ' ' + timeOfDay (eventValue) + '\\n');\n").append("    }\n").append("    function isBound (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool isBound=' + eventValue + '\\n');\n").append("    }\n").append("    function description (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFString description=' + eventValue + '\\n');\n").append("    }\n").append("    function fieldOfView (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFFloat fieldOfView=' + eventValue + '\\n');\n").append("    }\n").append("    function centerOfRotation (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFVec3f centerOfRotation=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function position (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFVec3f position=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function orientation (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFRotation orientation=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function jump (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool jump=' + eventValue + '\\n');\n").append("    }\n").append("    function retainUserOffsets(eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool retainUserOffsets=' + eventValue + '\\n');\n").append("    }\n").append("    function timeOfDay (someTime) {\n").append("      hh = Math.floor (someTime /(60*60)) % 24;\n").append("      mm = Math.floor (someTime / 60)     % 60;\n").append("      ss = Math.floor (someTime)          % 60;\n").append("      if (hh < 9) hour   = '0' + hh;  ").append("      else        hour   =       hh;\n").append("      if (mm < 9) minute = '0' + mm;  ").append("      else        minute =       mm;\n").append("      if (ss < 9) second = '0' + ss;  ").append("      else        second =       ss;\n").append("      return '(' + hour + ':' + minute + ':' + second + ' GMT)';\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='bindTime'           fromNode='").append(getDEFUSE()).append("' toField='bindTime'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='isBound'            fromNode='").append(getDEFUSE()).append("' toField='isBound'            toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='description'        fromNode='").append(getDEFUSE()).append("' toField='description'        toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='fieldOfView'        fromNode='").append(getDEFUSE()).append("' toField='fieldOfView'        toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='centerOfRotation'   fromNode='").append(getDEFUSE()).append("' toField='centerOfRotation'   toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='position'           fromNode='").append(getDEFUSE()).append("' toField='position'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='orientation'        fromNode='").append(getDEFUSE()).append("' toField='orientation'        toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='jump'               fromNode='").append(getDEFUSE()).append("' toField='jump'               toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='retainUserOffsets'  fromNode='").append(getDEFUSE()).append("' toField='retainUserOffsets'  toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("OrthoViewpoint")) {
                    String scriptName = "Trace_OrthoViewpoint_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='bindTime'           accessType='inputOnly' type='SFTime'/>\n").append("  <field name='isBound'            accessType='inputOnly' type='SFBool'/>\n").append("  <field name='description'        accessType='inputOnly' type='SFString'/>\n").append("  <field name='fieldOfView'        accessType='inputOnly' type='MFFloat'/>\n").append("  <field name='centerOfRotation'   accessType='inputOnly' type='SFVec3f'/>\n").append("  <field name='position'           accessType='inputOnly' type='SFVec3f'/>\n").append("  <field name='orientation'        accessType='inputOnly' type='SFRotation'/>\n").append("  <field name='jump'               accessType='inputOnly' type='SFBool'/>\n").append("  <field name='retainUserOffsets'  accessType='inputOnly' type='SFBool'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function bindTime (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFTime bindTime=' + eventValue + ' ' + timeOfDay (eventValue) + '\\n');\n").append("    }\n").append("    function isBound (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool isBound=' + eventValue + '\\n');\n").append("    }\n").append("    function description (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFString description=' + eventValue + '\\n');\n").append("    }\n").append("    function fieldOfView (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=MFFloat fieldOfView=' + eventValue + '\\n');\n").append("    }\n").append("    function centerOfRotation (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFVec3f centerOfRotation=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function position (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFVec3f position=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function orientation (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFRotation orientation=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function jump (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool jump=' + eventValue + '\\n');\n").append("    }\n").append("    function retainUserOffsets(eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool retainUserOffsets=' + eventValue + '\\n');\n").append("    }\n").append("    function timeOfDay (someTime) {\n").append("      hh = Math.floor (someTime /(60*60)) % 24;\n").append("      mm = Math.floor (someTime / 60)     % 60;\n").append("      ss = Math.floor (someTime)          % 60;\n").append("      if (hh < 9) hour   = '0' + hh;  ").append("      else        hour   =       hh;\n").append("      if (mm < 9) minute = '0' + mm;  ").append("      else        minute =       mm;\n").append("      if (ss < 9) second = '0' + ss;  ").append("      else        second =       ss;\n").append("      return '(' + hour + ':' + minute + ':' + second + ' GMT)';\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='bindTime'           fromNode='").append(getDEFUSE()).append("' toField='bindTime'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='isBound'            fromNode='").append(getDEFUSE()).append("' toField='isBound'            toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='description'        fromNode='").append(getDEFUSE()).append("' toField='description'        toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='fieldOfView'        fromNode='").append(getDEFUSE()).append("' toField='fieldOfView'        toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='centerOfRotation'   fromNode='").append(getDEFUSE()).append("' toField='centerOfRotation'   toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='position'           fromNode='").append(getDEFUSE()).append("' toField='position'           toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='orientation'        fromNode='").append(getDEFUSE()).append("' toField='orientation'        toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='jump'               fromNode='").append(getDEFUSE()).append("' toField='jump'               toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='retainUserOffsets'  fromNode='").append(getDEFUSE()).append("' toField='retainUserOffsets'  toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                } else if (getElementName().equals("ViewpointGroup")) {
                    String scriptName = "Trace_ViewpointGroup_" + getDEFUSE();
                    sb.append("\n").append("<Group>\n").append("<!-- ======= ").append(getElementName()).append(" Trace ============================================== -->\n").append("<Script DEF='").append(scriptName).append("' mustEvaluate='true'>\n").append("  <!-- Trace ROUTEd values on X3D browser console -->\n").append("  <field name='description'        accessType='inputOnly' type='SFString'/>\n").append("  <field name='center'             accessType='inputOnly' type='SFVec3f'/>\n").append("  <field name='displayed'          accessType='inputOnly' type='SFBool'/>\n").append("  <field name='retainUserOffsets'  accessType='inputOnly' type='SFBool'/>\n").append("  <field name='size'               accessType='inputOnly' type='SFVec3f'/>\n").append("  <![CDATA[\n").append("  ecmascript:\n").append("    function description (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFString description=' + eventValue + '\\n');\n").append("    }\n").append("    function center (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFVec3f center=' + eventValue.toString() + '\\n');\n").append("    }\n").append("    function displayed (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool displayed=' + eventValue + '\\n');\n").append("    }\n").append("    function retainUserOffsets(eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFBool retainUserOffsets=' + eventValue + '\\n');\n").append("    }\n").append("    function size (eventValue) {\n").append("      Browser.print ('").append(scriptName).append(" type=SFVec3f size=' + eventValue.toString() + '\\n');\n").append("    }\n").append("  ]]>\n").append("</Script>\n").append("<ROUTE fromField='description'        fromNode='").append(getDEFUSE()).append("' toField='description'        toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='center'             fromNode='").append(getDEFUSE()).append("' toField='center'             toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='displayed'          fromNode='").append(getDEFUSE()).append("' toField='displayed'          toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='retainUserOffsets'  fromNode='").append(getDEFUSE()).append("' toField='retainUserOffsets'  toNode='").append(scriptName).append("'/>\n").append("<ROUTE fromField='size'               fromNode='").append(getDEFUSE()).append("' toField='size'               toNode='").append(scriptName).append("'/>\n").append("<!-- ======= ").append(getElementName()).append(" Trace block complete ===================================================== -->").append("</Group>\n");
                }
            }
        } catch (Exception ex) {
            System.out.println("BaseX3dElement traceEvent exception " + ex);
            ex.printStackTrace();
        }
        try {
            if (getElementName().equals("ProtoDeclare")) {
                PROTODECLARE protoDeclare = (PROTODECLARE) this;
                if (protoDeclare.getAppendedContent().length() > 0) {
                    sb.append(protoDeclare.getAppendedContent());
                }
            }
            if (getElementName().equals("ExternProtoDeclare")) {
                EXTERNPROTODECLARE externProtoDeclare = (EXTERNPROTODECLARE) this;
                if (externProtoDeclare.getAppendedContent().length() > 0) {
                    sb.append(externProtoDeclare.getAppendedContent());
                }
            }
            String lineColor = X3dOptions.getVisualizeLineColor();
            String shapeColor = X3dOptions.getVisualizeShapeColor();
            String shapeTransparency = X3dOptions.getVisualizeTransparency();
            String DEFlabel = "";
            if (isDEF() && (getDEFUSE().length() > 0)) DEFlabel = " DEF='" + getDEFUSE() + "'";
            if (getElementName().equals("Shape") || getElementName().equals("Group") || getElementName().equals("Switch") || getElementName().equals("Transform") || getElementName().equals("EspduTransform") || getElementName().equals("ReceiverPdu") || getElementName().equals("SignalPdu") || getElementName().equals("TransmitterPdu") || getElementName().equals("Anchor") || getElementName().equals("Billboard") || getElementName().equals("Collision") || getElementName().equals("LOD") || getElementName().equals("Inline") || getElementName().equals("StaticGroup") || getElementName().equals("GeoLocation") || getElementName().equals("GeoLOD") || getElementName().equals("GeoTransform") || getElementName().equals("CADAssembly") || getElementName().equals("CADFace") || getElementName().equals("CADLayer") || getElementName().equals("CADPart") || getElementName().equals("HAnimHumanoid") || getElementName().equals("HAnimJoint") || getElementName().equals("HAnimSegment") || getElementName().equals("HAnimSite") || getElementName().equals("CollidableOffset") || getElementName().equals("CollidableShape") || getElementName().equals("CollisionSpace")) {
                if (isVisualizationSelectionAvailable()) {
                    String translationValue = "";
                    String transformCenter = "";
                    String rotationValue = "";
                    String rotationX = "";
                    String rotationY = "";
                    String rotationZ = "";
                    String rotationAngle = "";
                    String scaleX = "";
                    String scaleY = "";
                    String scaleZ = "";
                    String scaleOrientationX = "";
                    String scaleOrientationY = "";
                    String scaleOrientationZ = "";
                    String scaleOrientationAngle = "";
                    X3DGroupingNode groupingNode = (X3DGroupingNode) this;
                    String boundingBoxSize = groupingNode.getBboxSizeX() + " " + groupingNode.getBboxSizeY() + " " + groupingNode.getBboxSizeZ();
                    String boundingBoxCenter = groupingNode.getBboxCenterX() + " " + groupingNode.getBboxCenterY() + " " + groupingNode.getBboxCenterZ();
                    String nameLabel = "";
                    if (getElementName().equals("CADAssembly")) nameLabel = " name='" + ((CADASSEMBLY) this).getName() + "'"; else if (getElementName().equals("CADFace")) nameLabel = " name='" + ((CADFACE) this).getName() + "'"; else if (getElementName().equals("CADLayer")) nameLabel = " name='" + ((CADLAYER) this).getName() + "'"; else if (getElementName().equals("CADPart")) nameLabel = " name='" + ((CADPART) this).getName() + "'"; else if (getElementName().equals("HAnimHumanoid")) nameLabel = " name='" + ((HANIMHUMANOID) this).getName() + "'"; else if (getElementName().equals("HAnimDisplacer")) nameLabel = " name='" + ((HANIMDISPLACER) this).getName() + "'"; else if (getElementName().equals("HAnimJoint")) nameLabel = " name='" + ((HANIMJOINT) this).getName() + "'"; else if (getElementName().equals("HAnimSegment")) nameLabel = " name='" + ((HANIMSEGMENT) this).getName() + "'"; else if (getElementName().equals("HAnimSite")) nameLabel = " name='" + ((HANIMSITE) this).getName() + "'";
                    boolean transformType = (getElementName().equals("Transform") || getElementName().equals("CADPart") || getElementName().equals("EspduTransform") || getElementName().equals("GeoLOD") || getElementName().equals("GeoTransform") || getElementName().equals("HAnimHumanoid") || getElementName().equals("HAnimJoint") || getElementName().equals("HAnimSite"));
                    if (!boundingBoxSize.equals(TRANSFORM_ATTR_BBOXSIZE_DFLT) || (transformType && X3dOptions.getVisualizeCoordinateAxes())) {
                        if (getElementName().equals("Transform") || getElementName().equals("CADPart") || getElementName().equals("EspduTransform") || getElementName().equals("HAnimHumanoid") || getElementName().equals("HAnimJoint") || getElementName().equals("HAnimSite")) {
                            translationValue = " translation='" + ((X3DTransformNode) this).getTranslationX() + " " + ((X3DTransformNode) this).getTranslationY() + " " + ((X3DTransformNode) this).getTranslationZ() + "'";
                            transformCenter = " center='" + ((X3DTransformNode) this).getCenterX() + " " + ((X3DTransformNode) this).getCenterY() + " " + ((X3DTransformNode) this).getCenterZ() + "'";
                            rotationValue = " rotation='" + ((X3DTransformNode) this).getRotationX() + " " + ((X3DTransformNode) this).getRotationY() + " " + ((X3DTransformNode) this).getRotationZ() + " " + ((X3DTransformNode) this).getRotationAngle() + "'";
                            rotationX = ((X3DTransformNode) this).getRotationX();
                            rotationY = ((X3DTransformNode) this).getRotationY();
                            rotationZ = ((X3DTransformNode) this).getRotationZ();
                            rotationAngle = ((X3DTransformNode) this).getRotationAngle();
                            scaleX = ((X3DTransformNode) this).getScaleX();
                            scaleY = ((X3DTransformNode) this).getScaleY();
                            scaleZ = ((X3DTransformNode) this).getScaleZ();
                            scaleOrientationX = ((X3DTransformNode) this).getScaleOrientationX();
                            scaleOrientationY = ((X3DTransformNode) this).getScaleOrientationY();
                            scaleOrientationZ = ((X3DTransformNode) this).getScaleOrientationZ();
                            scaleOrientationAngle = ((X3DTransformNode) this).getScaleOrientationAngle();
                        } else if (getElementName().equals("GeoTransform")) {
                            translationValue = " translation='" + ((GEOTRANSFORM) this).getTranslationX() + " " + ((GEOTRANSFORM) this).getTranslationY() + " " + ((GEOTRANSFORM) this).getTranslationZ() + "'";
                            transformCenter = " geoCenter='" + ((GEOTRANSFORM) this).getGeoCenterX() + " " + ((GEOTRANSFORM) this).getGeoCenterY() + " " + ((GEOTRANSFORM) this).getGeoCenterZ() + "'";
                            rotationValue = " rotation='" + ((GEOTRANSFORM) this).getRotationX() + " " + ((GEOTRANSFORM) this).getRotationY() + " " + ((GEOTRANSFORM) this).getRotationZ() + " " + ((GEOTRANSFORM) this).getRotationAngle() + "'";
                            rotationX = ((GEOTRANSFORM) this).getRotationX();
                            rotationY = ((GEOTRANSFORM) this).getRotationY();
                            rotationZ = ((GEOTRANSFORM) this).getRotationZ();
                            rotationAngle = ((GEOTRANSFORM) this).getRotationAngle();
                            scaleX = ((GEOTRANSFORM) this).getScaleX();
                            scaleY = ((GEOTRANSFORM) this).getScaleY();
                            scaleZ = ((GEOTRANSFORM) this).getScaleZ();
                            scaleOrientationX = ((GEOTRANSFORM) this).getScaleOrientationX();
                            scaleOrientationY = ((GEOTRANSFORM) this).getScaleOrientationY();
                            scaleOrientationZ = ((GEOTRANSFORM) this).getScaleOrientationZ();
                            scaleOrientationAngle = ((GEOTRANSFORM) this).getScaleOrientationAngle();
                        } else if (getElementName().equals("GeoLOD")) {
                            transformCenter = " center='" + ((GEOLOD) this).getCenterX() + " " + ((GEOLOD) this).getCenterY() + " " + ((GEOLOD) this).getCenterZ() + "'";
                        }
                        String scale = scaleX + " " + scaleY + " " + scaleZ;
                        String scaleOrientation = scaleOrientationX + " " + scaleOrientationY + " " + scaleOrientationZ + " " + scaleOrientationAngle;
                        String scaleOrientationAngleDegrees = "";
                        String scaleOrientationOffsetDegrees = "";
                        if (scaleOrientationAngle.length() > 0) {
                            scaleOrientationAngleDegrees = singleDigitFormat.format(Double.parseDouble(scaleOrientationAngle) * 180.0 / Math.PI);
                            scaleOrientationOffsetDegrees = Double.parseDouble(scaleOrientationX) * 1.2 + " " + Double.parseDouble(scaleOrientationY) * 1.2 + " " + Double.parseDouble(scaleOrientationZ) * 1.2;
                        }
                        sb.append("\n").append("    <Switch whichChoice='0' class='visualization.").append(getElementName()).append("'>\n" + "      <Group>\n");
                        if (transformType && X3dOptions.getVisualizeCoordinateAxes()) {
                            sb.append("        <Transform translation").append(transformCenter.substring(transformCenter.indexOf("="))).append(">\n");
                            if (!getElementName().equals("GeoLOD") && (scale.equals(TRANSFORM_ATTR_SCALE_DFLT) || ((Double.parseDouble(scaleX) > 1.3) && (Double.parseDouble(scaleY) > 1.3) && (Double.parseDouble(scaleZ) > 1.3)))) {
                                sb.append("          <!-- CoordinateAxes show local ").append(getElementName()).append(transformCenter).append(" in local coordinate system -->\n" + "          <TouchSensor description=\"").append(getElementName()).append(DEFlabel).append(nameLabel).append(translationValue).append(" ").append(transformCenter).append(" for rotation and scaling\"/>\n" + "          <Inline url='\"../../X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.x3d\" \"../../../Savage/Tools/Authoring/CoordinateAxes.x3d\" \"http://X3dGraphics.com/examples/X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.x3d\" \"https://savage.nps.edu/Savage/Tools/Authoring/CoordinateAxes.x3d\" \"../../X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.wrl\" \"../../../Savage/Tools/Authoring/CoordinateAxes.wrl\" \"http://X3dGraphics.com/examples/X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.wrl\" \"https://savage.nps.edu/Savage/Tools/Authoring/CoordinateAxes.wrl\"'/>\n");
                                double a, b, a1, a2, a3, b1, b2, b3, a_dot_b, axis1, axis2, axis3, angle;
                                a1 = 1.0;
                                a2 = 0.0;
                                a3 = 0.0;
                                b1 = Double.valueOf(rotationX);
                                b2 = Double.valueOf(rotationY);
                                b3 = Double.valueOf(rotationZ);
                                a = Math.sqrt(a1 * a1 + a2 * a2 + a3 * a3);
                                b = Math.sqrt(b1 * b1 + b2 * b2 + b3 * b3);
                                a_dot_b = a1 * b1 + a2 * b2 + a3 * b3;
                                axis1 = a2 * b3 - a3 * b2;
                                axis2 = a3 * b1 - a1 * b3;
                                axis3 = a1 * b2 - a2 * b1;
                                double axisLength = Math.sqrt(axis1 * axis1 + axis2 * axis2 + axis3 * axis3);
                                if (axisLength > 0.0) {
                                    axis1 /= axisLength;
                                    axis2 /= axisLength;
                                    axis3 /= axisLength;
                                }
                                if ((a != 0.0) && (b != 0.0)) {
                                    angle = Math.acos(a_dot_b / (a * b));
                                } else angle = 0.0;
                                if ((axis1 == 0.0) && (axis2 == 0.0) && (axis3 == 0.0)) {
                                    axis2 = 1.0;
                                    angle = 0.0;
                                }
                                if (axis1 == -0.0) axis1 = +0.0;
                                if (axis2 == -0.0) axis2 = +0.0;
                                if (axis3 == -0.0) axis3 = +0.0;
                                if ((axis1 < 0.0) && (axis2 == 0.0) && (axis3 == 0.0)) {
                                    axis1 = -axis1;
                                    angle = -angle;
                                } else if ((axis1 == 0.0) && (axis2 < 0.0) && (axis3 == 0.0)) {
                                    axis2 = -axis2;
                                    angle = -angle;
                                } else if ((axis1 == 0.0) && (axis2 == 0.0) && (axis3 < 0.0)) {
                                    axis3 = -axis3;
                                    angle = -angle;
                                }
                                String axisRotation = " rotation='" + axis1 + " " + axis2 + " " + axis3 + " " + fiveDigitFormat.format(angle) + "'";
                                String angleDegrees = singleDigitFormat.format(Double.parseDouble(rotationAngle) * 180.0 / Math.PI);
                                sb.append("          <Transform ").append(axisRotation).append(">\n").append("            <TouchSensor description=\"").append(getElementName()).append(DEFlabel).append(nameLabel).append(" axis of").append(rotationValue).append(" (").append(angleDegrees).append(" degrees)\"/>\n").append("            <Transform translation='0.6 0 0'>\n").append("              <Transform rotation='0 0 1 1.57'>\n").append("                <Shape>\n").append("                  <Cylinder height='1.2' radius='.02'/>\n").append("                  <Appearance>\n").append("                    <Material diffuseColor='").append(lineColor).append("'/>\n").append("                  </Appearance>\n").append("                </Shape>\n").append("              </Transform>\n").append("            </Transform>\n").append("            <Transform translation='1.35 0 0'>\n").append("              <Billboard axisOfRotation='0 0 0'>\n").append("                <Shape>\n").append("                  <Text string='&quot;").append(angleDegrees).append("&#176;&quot;'>\n").append("                    <FontStyle justify='&quot;MIDDLE&quot; &quot;MIDDLE&quot;' size='0.2'/>\n").append("                  </Text>\n").append("                  <Appearance>\n").append("                    <Material diffuseColor='").append(lineColor).append("'/>\n").append("                  </Appearance>\n").append("                </Shape>\n").append("              <Shape>\n").append("                <Box size='0.6 0.3 0.001'/>\n").append("                <Appearance>\n").append("                  <Material transparency='1'/>\n").append("                </Appearance>\n").append("              </Shape>\n").append("              </Billboard>\n").append("            </Transform>\n").append("          </Transform>\n");
                            }
                            if (!scale.equals(TRANSFORM_ATTR_SCALE_DFLT) && X3dOptions.getVisualizeCoordinateAxes()) {
                                sb.append("        <Group>\n").append("          <!-- Scaled wireframe axes (XYZ=RGB) shows non-unit scale and scaleOrientation axis of ").append(getElementName()).append(" transformation -->\n").append("          <Shape>\n").append("            <IndexedLineSet colorIndex='0 1 2 3' colorPerVertex='false' coordIndex='0 1 -1 0 2 -1 0 3 -1 0 4 -1'>\n").append("              <Coordinate point='0 0 0 ").append(scaleX).append(" 0 0 0 ").append(scaleY).append(" 0 0 0 ").append(scaleZ).append(" ").append(scaleOrientationX).append(" ").append(scaleOrientationY).append(" ").append(scaleOrientationZ).append("'/>\n").append("              <Color color='0.8 0 0 0 0.8 0 0 0 0.8 ").append(lineColor).append("'/>\n").append("            </IndexedLineSet>\n").append("          </Shape>\n").append("          <TouchSensor description=\"").append(getElementName()).append(DEFlabel).append(nameLabel).append(" scale='").append(scale).append("' scaleOrientation='").append(scaleOrientation).append("' (").append(scaleOrientationAngleDegrees).append(" degrees)\"/>\n").append("          <Transform translation='").append(scaleOrientationOffsetDegrees).append("'>\n").append("            <Billboard axisOfRotation='0 0 0'>\n").append("              <Shape>\n").append("                <Text string='&quot;").append(scaleOrientationAngleDegrees).append("&#176;&quot;'>\n").append("                  <FontStyle justify='&quot;MIDDLE&quot; &quot;MIDDLE&quot;' size='0.2'/>\n").append("                </Text>\n").append("                <Appearance>\n").append("                  <Material diffuseColor='").append(lineColor).append("'/>\n").append("                </Appearance>\n").append("              </Shape>\n").append("              <Shape>\n").append("                <Box size='0.6 0.3 0.001'/>\n").append("                <Appearance>\n").append("                  <Material transparency='1'/>\n").append("                </Appearance>\n").append("              </Shape>\n").append("            </Billboard>\n").append("          </Transform>\n").append("        </Group>\n");
                            }
                            sb.append("        </Transform>\n");
                        }
                        if (!boundingBoxSize.equals(TRANSFORM_ATTR_BBOXSIZE_DFLT) && X3dOptions.getVisualizeCoordinateAxes()) {
                            sb.append("        <Transform translation='").append(boundingBoxCenter).append("'>\n").append("          <!-- Unit wireframe axes (XYZ=RGB) show local center of ").append(getElementName()).append(" bounding box -->\n").append("          <Shape>\n").append("            <IndexedLineSet colorIndex='0 1 2' colorPerVertex='false' coordIndex='0 1 -1 0 2 -1 0 3 -1'>\n").append("              <Coordinate point='0 0 0 1 0 0 0 1 0 0 0 1'/>\n").append("              <Color color='0.8 0 0 0 0.8 0 0 0 0.8'/>\n").append("            </IndexedLineSet>\n").append("          </Shape>\n");
                            sb.append("          <TouchSensor description=\"").append(getElementName()).append(DEFlabel).append(nameLabel).append(" bounding box bboxCenter='").append(boundingBoxCenter).append("' bboxSize='").append(boundingBoxSize).append("'\"/>\n").append("          <Shape>\n").append("            <Box size='").append(boundingBoxSize).append("' solid='false'/>\n").append("            <Appearance>\n").append("              <Material diffuseColor='").append(shapeColor).append("'  transparency='").append(shapeTransparency).append("'/>\n").append("            </Appearance>\n").append("          </Shape>\n").append("          <Transform scale='").append((new SFFloat(groupingNode.getBboxSizeX())).getValue() / 2.0f).append(" ").append((new SFFloat(groupingNode.getBboxSizeY())).getValue() / 2.0f).append(" ").append((new SFFloat(groupingNode.getBboxSizeZ())).getValue() / 2.0f).append("'>\n").append("            <!-- scaled wireframe IndexedLineSet to match preceding Box -->\n").append("            <Shape>\n").append("              <IndexedLineSet coordIndex='0 1 2 3 0 -1 4 5 6 7 4 -1 0 4 -1 1 5 -1 2 6 -1 3 7'>\n").append("                <Coordinate point='-1 1 1 1 1 1 1 1 -1 -1 1 -1 -1 -1 1 1 -1 1 1 -1 -1 -1 -1 -1'/>\n").append("              </IndexedLineSet>\n").append("              <Appearance>\n").append("                <Material emissiveColor='").append(shapeColor).append("'/>\n").append("              </Appearance>\n").append("            </Shape>\n").append("          </Transform>\n").append("        </Transform>\n");
                        }
                        sb.append("      </Group>\n").append("    </Switch>");
                    }
                }
            }
            if (getElementName().equals("ProximitySensor") || getElementName().equals("VisibilitySensor") || getElementName().equals("TransformSensor")) {
                if (isVisualizationSelectionAvailable()) {
                    X3DEnvironmentalSensorNode environmentalSensorNode = (X3DEnvironmentalSensorNode) this;
                    String sensorSize = environmentalSensorNode.getSizeX() + " " + environmentalSensorNode.getSizeY() + " " + environmentalSensorNode.getSizeZ();
                    String sensorCenter = environmentalSensorNode.getCenterX() + " " + environmentalSensorNode.getCenterY() + " " + environmentalSensorNode.getCenterZ();
                    if (!sensorSize.equals(PROXIMITYSENSOR_ATTR_SIZE_DFLT)) {
                        sb.append("\n").append("    <Switch whichChoice='0' class='visualization.").append(getElementName()).append("'>\n").append("      <Transform translation='").append(sensorCenter).append("'>\n");
                        if (X3dOptions.getVisualizeCoordinateAxes()) {
                            sb.append("        <!-- Unit wireframe axes (XYZ=RGB) show local center of ").append(getElementName()).append(" sensed volume -->\n").append("        <Shape>\n").append("          <IndexedLineSet colorIndex='0 1 2' colorPerVertex='false' coordIndex='0 1 -1 0 2 -1 0 3 -1'>\n").append("            <Coordinate point='0 0 0 1 0 0 0 1 0 0 0 1'/>\n").append("            <Color color='0.8 0 0 0 0.8 0 0 0 0.8'/>\n").append("          </IndexedLineSet>\n").append("        </Shape>\n");
                        }
                        sb.append("        <TouchSensor description=\"ProximitySensor").append(DEFlabel).append(" sensing volume, center='").append(sensorCenter).append(" size='").append(sensorSize).append("'\"/>\n").append("        <Shape>\n").append("          <Box size='").append(sensorSize).append("' solid='false'/>\n").append("          <Appearance>\n").append("            <Material diffuseColor='").append(shapeColor).append("'  transparency='").append(shapeTransparency).append("'/>\n").append("          </Appearance>\n").append("        </Shape>\n").append("        <Transform scale='").append((new SFFloat(environmentalSensorNode.getSizeX())).getValue() / 2.0f).append(" ").append((new SFFloat(environmentalSensorNode.getSizeY())).getValue() / 2.0f).append(" ").append((new SFFloat(environmentalSensorNode.getSizeZ())).getValue() / 2.0f).append("'>\n").append("          <!-- scaled wireframe IndexedLineSet to match preceding Box -->\n").append("          <Shape>\n").append("            <IndexedLineSet coordIndex='0 1 2 3 0 -1 4 5 6 7 4 -1 0 4 -1 1 5 -1 2 6 -1 3 7'>\n").append("              <Coordinate point='-1 1 1 1 1 1 1 1 -1 -1 1 -1 -1 -1 1 1 -1 1 1 -1 -1 -1 -1 -1'/>\n").append("            </IndexedLineSet>\n").append("            <Appearance>\n").append("              <Material emissiveColor='").append(shapeColor).append("'/>\n").append("            </Appearance>\n").append("          </Shape>\n").append("        </Transform>\n").append("      </Transform>\n").append("    </Switch>");
                    }
                }
            }
            if (getElementName().equals("PointLight")) {
                if (isVisualizationSelectionAvailable()) {
                    POINTLIGHT pointLight = (POINTLIGHT) this;
                    String lightColors = pointLight.getColor0() + " " + pointLight.getColor1() + " " + pointLight.getColor2();
                    String lightLocation = pointLight.getLocationX() + " " + pointLight.getLocationY() + " " + pointLight.getLocationZ();
                    String lightSphereTransparency = X3dOptions.getVisualizeTransparency();
                    sb.append("\n").append("    <Switch whichChoice='0' class='visualization.").append(getElementName()).append("'>\n").append("      <!-- PointLight outline: simple Sphere -->\n").append("      <Transform translation='").append(lightLocation).append("'>\n");
                    if (X3dOptions.getVisualizeCoordinateAxes()) {
                        sb.append("        <!-- here is local center of ").append(getElementName()).append(" coordinate system -->\n").append("        <Inline url='\"../../X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.x3d\" \"../../../Savage/Tools/Authoring/CoordinateAxes.x3d\" \"http://X3dGraphics.com/examples/X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.x3d\" \"https://savage.nps.edu/Savage/Tools/Authoring/CoordinateAxes.x3d\" \"../../X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.wrl\" \"../../../Savage/Tools/Authoring/CoordinateAxes.wrl\" \"http://X3dGraphics.com/examples/X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.wrl\" \"https://savage.nps.edu/Savage/Tools/Authoring/CoordinateAxes.wrl\"'/>\n");
                    }
                    sb.append("        <TouchSensor description=\"PointLight").append(DEFlabel).append(" spherical volume, location='").append(lightLocation).append("' radius='").append(pointLight.getRadius()).append("' color='").append(lightColors).append("'\"/>\n").append("        <Shape>\n").append("          <Sphere radius='").append(pointLight.getRadius()).append("' solid='false'/>\n").append("          <Appearance>\n").append("            <Material").append(" emissiveColor='").append(lightColors).append("' transparency='").append(lightSphereTransparency).append("'/>\n").append("          </Appearance>\n").append("        </Shape>\n").append("      </Transform>\n").append("    </Switch>");
                }
            }
            if (getElementName().equals("SpotLight")) {
                if (isVisualizationSelectionAvailable()) {
                    SPOTLIGHT spotLight = (SPOTLIGHT) this;
                    String lightOutlineTransparency = X3dOptions.getVisualizeTransparency();
                    String lightDirection = spotLight.getDirectionX() + " " + spotLight.getDirectionY() + " " + spotLight.getDirectionZ();
                    String lightLocation = spotLight.getLocationX() + " " + spotLight.getLocationY() + " " + spotLight.getLocationZ();
                    String lightColors = spotLight.getColorRed() + " " + spotLight.getColorGreen() + " " + spotLight.getColorBlue();
                    double cutOffAngleScaleFactor;
                    double beamWidthScaleFactor;
                    if (Double.parseDouble(spotLight.getCutOffAngle()) <= 1.5621) cutOffAngleScaleFactor = Double.parseDouble(spotLight.getRadius()) * Math.tan(Double.parseDouble(spotLight.getCutOffAngle())); else cutOffAngleScaleFactor = Double.parseDouble(spotLight.getRadius()) * Math.tan(1.5621);
                    if (Double.parseDouble(spotLight.getBeamWidth()) <= 1.5621) beamWidthScaleFactor = Double.parseDouble(spotLight.getRadius()) * Math.tan(Double.parseDouble(spotLight.getBeamWidth())); else beamWidthScaleFactor = Double.parseDouble(spotLight.getRadius()) * Math.tan(1.5621);
                    if (beamWidthScaleFactor > cutOffAngleScaleFactor) beamWidthScaleFactor = cutOffAngleScaleFactor;
                    double deltaX, deltaY, deltaZ, horizontalDistance;
                    double horizontalAngle, pitchAngle = 0.0;
                    deltaX = Double.parseDouble(spotLight.getDirectionX());
                    deltaY = Double.parseDouble(spotLight.getDirectionY());
                    deltaZ = Double.parseDouble(spotLight.getDirectionZ());
                    horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
                    if (horizontalDistance == 0.0) {
                        horizontalAngle = 0.0;
                    } else {
                        horizontalAngle = Math.atan2(-deltaZ, deltaX) - (Math.PI / 2.0);
                    }
                    if (deltaY == 0.0) {
                        pitchAngle = 0.0;
                    } else {
                        pitchAngle = Math.atan2(deltaY, horizontalDistance);
                    }
                    sb.append("\n" + "    <Switch whichChoice='0' class='visualization.").append(getElementName()).append("'>\n").append("      <!-- SpotLight outline, patterned after http://X3dGraphics.com/examples/X3dForWebAuthors/Chapter11-LightingEnvironmentalEffects/LightOutlineGeometry.x3d -->\n").append("      <!-- Spotlight direction=").append(lightDirection).append(", horizontalAngle=").append(horizontalAngle).append(" radians=").append(singleDigitFormat.format(horizontalAngle * 180.0 / Math.PI)).append(" degrees").append(", pitchAngle=").append(pitchAngle).append(" radians=").append(pitchAngle * 180.0 / Math.PI).append(" degrees").append(" -->\n").append("      <Transform rotation='0 1 0 ").append(horizontalAngle).append("' ").append("translation='").append(lightLocation).append("'>\n").append("        <Transform rotation='1 0 0 ").append(pitchAngle).append("'>\n");
                    if (X3dOptions.getVisualizeCoordinateAxes()) {
                        sb.append("          <!-- here is local center of ").append(getElementName()).append(" coordinate system, with local X axis pointed along direction vector -->\n").append("          <Inline url='\"../../X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.x3d\" \"../../../Savage/Tools/Authoring/CoordinateAxes.x3d\" \"http://X3dGraphics.com/examples/X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.x3d\" \"https://savage.nps.edu/Savage/Tools/Authoring/CoordinateAxes.x3d\" \"../../X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.wrl\" \"../../../Savage/Tools/Authoring/CoordinateAxes.wrl\" \"http://X3dGraphics.com/examples/X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.wrl\" \"https://savage.nps.edu/Savage/Tools/Authoring/CoordinateAxes.wrl\"'/>\n");
                    }
                    sb.append("          <!-- Spotlight inner beamWidth=").append(spotLight.getBeamWidth()).append(" radians=").append(singleDigitFormat.format((new SFFloat(spotLight.getBeamWidth()).getValue() * 180.0 / Math.PI))).append(" degrees, ").append("radius=").append(spotLight.getRadius()).append(", beamWidthScaleFactor=radius*tan(beamWidth)=").append(singleDigitFormat.format(beamWidthScaleFactor)).append(" -->\n").append("          <Transform scale='").append(singleDigitFormat.format(beamWidthScaleFactor)).append(" ").append(singleDigitFormat.format(beamWidthScaleFactor)).append(" ").append(spotLight.getRadius()).append("'>\n").append("            <!-- Beam CircleLines made out of 24 line segments, turned into a cone with ").append(X3dOptions.getVisualizeConeLines()).append(" side lines -->\n").append("            <Shape>\n").append("              <IndexedLineSet coordIndex='0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 -1 ");
                    if ((new SFInt32(X3dOptions.getVisualizeConeLines())).getValue() == 2) {
                        sb.append("25 0 -1 25 12 -1'>\n");
                    } else if ((new SFInt32(X3dOptions.getVisualizeConeLines())).getValue() == 3) {
                        sb.append("25 0 -1 25 8 -1 25 16 -1'>\n");
                    } else if ((new SFInt32(X3dOptions.getVisualizeConeLines())).getValue() == 4) {
                        sb.append("25 0 -1 25 6 -1 25 12 -1 25 18 -1'>\n");
                    } else if ((new SFInt32(X3dOptions.getVisualizeConeLines())).getValue() == 6) {
                        sb.append("25 0 -1 25 4 -1 25 8 -1 25 12 -1 25 16 -1 25 20 -1'>\n");
                    } else if ((new SFInt32(X3dOptions.getVisualizeConeLines())).getValue() == 12) {
                        sb.append("25 0 -1 25 2 -1 25 4 -1 25 6 -1 25 8 -1 25 10 -1 25 12 -1 25 14 -1 25 16 -1 25 18 -1 25 20 -1 25 22 -1'>\n");
                    } else if ((new SFInt32(X3dOptions.getVisualizeConeLines())).getValue() == 24) {
                        sb.append("25 0 -1 25 1 -1 25 2 -1 25 3 -1 25 4 -1 25 5 -1 25 6 -1 25 7 -1 25 8 -1 25 9 -1 25 10 -1 25 11 -1 25 12 -1 25 13 -1 25 14 -1 25 15 -1 25 16 -1 25 17 -1 25 18 -1 25 19 -1 25 20 -1 25 21 -1 25 22 -1 25 23 -1'>\n");
                    } else {
                        sb.append("25 0 -1 25 3 -1 25 6 -1 25 9 -1 25 12 -1 25 15 -1 25 18 -1 25 21 -1'>\n");
                    }
                    sb.append("                <Coordinate point='0 1 -1 0.259 0.966 -1 0.5 0.866 -1 0.707 0.707 -1 0.866 0.5 -1 0.966 0.259 -1 1 0 -1 0.966 -0.259 -1 0.866 -0.5 -1 0.707 -0.707 -1 0.5 -0.866 -1 0.259 -0.966 -1 0 -1 -1 -0.259 -0.966 -1 -0.5 -0.866 -1 -0.707 -0.707 -1 -0.866 -0.5 -1 -0.966 -0.259 -1 -1 -0 -1 -0.966 0.259 -1 -0.866 0.5 -1 -0.707 0.707 -1 -0.5 0.866 -1 -0.259 0.966 -1 0 1 -1 0 0 0 0 0 -1'/>\n").append("              </IndexedLineSet>\n").append("              <Appearance>\n").append("                <Material diffuseColor='0 0 0' emissiveColor='").append(lightColors).append("'/>\n").append("              </Appearance>\n").append("            </Shape>\n");
                    if (X3dOptions.getVisualizeCenterLine()) {
                        sb.append("            <!-- center line -->\n").append("            <Shape>\n").append("              <IndexedLineSet coordIndex='0 1 -1'>\n").append("                <Coordinate point='0 0 0 0 0 -1'/>\n").append("              </IndexedLineSet>\n").append("              <Appearance>\n").append("                <Material diffuseColor='0 0 0' emissiveColor='1 1 1'/>\n").append("              </Appearance>\n").append("            </Shape>\n");
                    }
                    sb.append("          </Transform>\n").append("          <!-- Spotlight outer cutOffAngle=").append(spotLight.getCutOffAngle()).append(" radians=").append(singleDigitFormat.format(new SFFloat(spotLight.getCutOffAngle()).getValue() * 180.0 / Math.PI)).append(" degrees, ").append("radius=").append(spotLight.getRadius()).append("m, cutOffAngleScaleFactor=radius*tan(cutOffAngle)=").append(singleDigitFormat.format(cutOffAngleScaleFactor)).append(" -->\n").append("          <Transform scale='").append(singleDigitFormat.format(cutOffAngleScaleFactor)).append(" ").append(spotLight.getRadius()).append("'>\n").append("            <Transform rotation='1 0 0 1.57079' translation='0 0 -0.5'>\n").append("              <TouchSensor description=\"Spotlight").append(DEFlabel).append(" conical volume, location='").append(lightLocation).append("' direction='").append(lightDirection).append("' radius='").append(spotLight.getRadius()).append("' color='").append(lightColors).append("'\"/>\n").append("              <Shape>\n").append("                <Cone bottom='false' height='1'/>\n").append("                <Appearance>\n").append("                   <Material diffuseColor='0 0 0' emissiveColor='").append((new SFFloat(spotLight.getColorRed())).getValue() * 0.25f).append(" ").append((new SFFloat(spotLight.getColorGreen())).getValue() * 0.25f).append(" ").append((new SFFloat(spotLight.getColorBlue())).getValue() * 0.25f).append("' transparency='").append(lightOutlineTransparency).append("'/>\n").append("                </Appearance>\n").append("              </Shape>\n").append("            </Transform>\n").append("          </Transform>\n").append("        </Transform>\n").append("      </Transform>\n").append("    </Switch>");
                }
            }
            if (getElementName().equals("Sound")) {
                if (isVisualizationSelectionAvailable()) {
                    SOUND sound = (SOUND) this;
                    String soundLocation = sound.getLocationX() + " " + sound.getLocationY() + " " + sound.getLocationZ();
                    String soundDirection = sound.getDirectionX() + " " + sound.getDirectionY() + " " + sound.getDirectionZ();
                    double innerEllipseRed = Double.parseDouble(X3dOptions.getVisualizeLineColorRed());
                    double innerEllipseGreen = Double.parseDouble(X3dOptions.getVisualizeLineColorGreen());
                    double innerEllipseBlue = Double.parseDouble(X3dOptions.getVisualizeLineColorBlue());
                    double outerEllipseRed = Double.parseDouble(X3dOptions.getVisualizeShapeColorRed());
                    double outerEllipseGreen = Double.parseDouble(X3dOptions.getVisualizeShapeColorGreen());
                    double outerEllipseBlue = Double.parseDouble(X3dOptions.getVisualizeShapeColorBlue());
                    double soundTransparency = Double.parseDouble(X3dOptions.getVisualizeTransparency());
                    String minSoundColors = innerEllipseRed + " " + innerEllipseGreen + " " + innerEllipseBlue;
                    String maxSoundColors = outerEllipseRed + " " + outerEllipseGreen + " " + outerEllipseBlue;
                    String minTransparency = Double.toString(soundTransparency);
                    String maxTransparency = Double.toString(soundTransparency + (1.0 - soundTransparency) * 0.5);
                    double minBack = Double.parseDouble(sound.getMinBack());
                    double minFront = Double.parseDouble(sound.getMinFront());
                    double maxBack = Double.parseDouble(sound.getMaxBack());
                    double maxFront = Double.parseDouble(sound.getMaxFront());
                    double minLength = minBack + minFront;
                    double maxLength = maxBack + maxFront;
                    double minWidth, maxWidth;
                    if (minBack == 0.0) minWidth = minFront; else if (minFront == 0.0) minWidth = minBack; else minWidth = Math.sqrt(minBack * minFront);
                    if (maxBack == 0.0) maxWidth = maxFront; else if (maxFront == 0.0) maxWidth = maxBack; else maxWidth = Math.sqrt(maxBack * maxFront);
                    String minCenterLocation = singleDigitFormat.format(-minBack + minLength / 2.0) + " 0 0";
                    String maxCenterLocation = singleDigitFormat.format(-maxBack + maxLength / 2.0) + " 0 0";
                    double deltaX, deltaY, deltaZ, horizontalDistance;
                    double horizontalAngle, pitchAngle = 0.0;
                    deltaX = Double.parseDouble(sound.getDirectionX());
                    deltaY = Double.parseDouble(sound.getDirectionY());
                    deltaZ = Double.parseDouble(sound.getDirectionZ());
                    horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
                    if (horizontalDistance == 0.0) {
                        horizontalAngle = 0.0;
                    } else {
                        horizontalAngle = Math.atan2(-deltaZ, deltaX) - (Math.PI / 2.0);
                    }
                    if (deltaY == 0.0) {
                        pitchAngle = 0.0;
                    } else {
                        pitchAngle = Math.atan2(deltaY, horizontalDistance);
                    }
                    sb.append("\n").append("    <Switch whichChoice='0' class='visualization.").append(getElementName()).append("'>\n").append("      <!-- Sound outlines, patterned after http://X3dGraphics.com/examples/X3dForWebAuthors/Chapter12-EnvironmentSensorSound/SoundAudioClip.x3d -->\n").append("      <!-- Approximate ellipsoidal footprints and envelope using Cylinders and Spheres, respectively -->\n").append("      <!-- Sound direction is along local x axis, so minLength = (minBack).append(minFront) and minRadius = minLength/2  -->\n").append("      <!-- and so width, height dimensions ~= maximum-attenuation halfwidth = maxWidth ~= 45 along y, z axes -->\n").append("      <!-- Ellipsoid focus is (0 0 0) but geometric center offset for spheroidal approximation is (-minBack).append(minRadius) -->\n").append("      <Transform rotation='0 1 0 ").append(horizontalAngle).append("' ").append("translation='").append(soundLocation).append("'>\n").append("        <Transform rotation='1 0 0 ").append(pitchAngle).append("'>\n");
                    if (X3dOptions.getVisualizeCoordinateAxes()) {
                        sb.append("          <!-- here is local center of ").append(getElementName()).append(" coordinate system asound ellipse, with local X axis pointed along direction vector -->\n").append("          <Inline url='\"../../X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.x3d\" \"../../../Savage/Tools/Authoring/CoordinateAxes.x3d\" \"http://X3dGraphics.com/examples/X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.x3d\" \"https://savage.nps.edu/Savage/Tools/Authoring/CoordinateAxes.x3d\" \"../../X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.wrl\" \"../../../Savage/Tools/Authoring/CoordinateAxes.wrl\" \"http://X3dGraphics.com/examples/X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.wrl\" \"https://savage.nps.edu/Savage/Tools/Authoring/CoordinateAxes.wrl\"'/>\n");
                    }
                    sb.append("          <Transform translation='").append(minCenterLocation).append("' ").append("scale='").append(minLength).append(" ").append(minWidth).append(" ").append(minWidth).append("'>\n").append("            <Collision enabled='false'>\n").append("              <TouchSensor description=\"Sound").append(DEFlabel).append(" inner ellipsoidal volume, location='").append(soundLocation).append("' direction='").append(soundDirection).append("' minBack='").append(sound.getMinBack()).append("' minFront='").append(sound.getMinFront()).append("'\"/>\n").append("              <Shape>\n").append("                <Cylinder radius='0.5' height='0.04' side='false'/>\n").append("                <Appearance>\n").append("                  <Material diffuseColor='").append(minSoundColors).append("' emissiveColor='").append(minSoundColors).append("' transparency='").append(minTransparency).append("'/>\n").append("                </Appearance>\n").append("              </Shape>\n").append("              <Shape>\n").append("                <Sphere radius='0.5'/>\n").append("                <Appearance>\n").append("                <Material diffuseColor='").append(minSoundColors).append("' emissiveColor='").append(minSoundColors).append("' transparency='").append(minTransparency).append("'/>\n").append("                </Appearance>\n").append("              </Shape>\n").append("            </Collision>\n").append("          </Transform>\n").append("          <Transform translation='").append(maxCenterLocation).append("' ").append("scale='").append(maxLength).append(" ").append(maxWidth).append(" ").append(maxWidth).append("'>\n").append("            <Collision enabled='false'>\n").append("              <TouchSensor description=\"Sound").append(DEFlabel).append(" outer ellipsoidal volume, location='").append(soundLocation).append("' direction='").append(soundDirection).append("' maxBack='").append(sound.getMaxBack()).append("' maxFront='").append(sound.getMaxFront()).append("'\"/>\n").append("              <Shape>\n").append("                <Cylinder radius='0.5' height='0.02' side='false'/>\n").append("                <Appearance>\n").append("                  <Material diffuseColor='").append(maxSoundColors).append("' emissiveColor='").append(maxSoundColors).append("' transparency='").append(maxTransparency).append("'/>\n").append("                </Appearance>\n").append("              </Shape>\n").append("              <Shape>\n").append("                <Sphere radius='0.5'/>\n").append("                <Appearance>\n").append("                <Material diffuseColor='").append(maxSoundColors).append("' emissiveColor='").append(maxSoundColors).append("' transparency='").append(maxTransparency).append("'/>\n").append("                </Appearance>\n").append("              </Shape>\n").append("            </Collision>\n").append("          </Transform>\n").append("        </Transform>\n").append("      </Transform>\n").append("    </Switch>");
                }
            }
            if (getElementName().equals("Viewpoint") || getElementName().equals("OrthoViewpoint") || getElementName().equals("GeoViewpoint")) {
                if (isVisualizationSelectionAvailable()) {
                    sb.append(PROTOTYPE_ViewFrustum.getExternProtoDeclare()).append("\n");
                    sb.append("    <Switch whichChoice='0' class='visualization.").append(getElementName()).append("'>\n");
                    if (X3dOptions.getVisualizeCoordinateAxes()) {
                        sb.append("      <!-- here is local center of ").append(getElementName()).append(" coordinate system, with local X axis pointed along direction vector -->\n").append("      <Inline url='\"../../X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.x3d\" \"../../../Savage/Tools/Authoring/CoordinateAxes.x3d\" \"http://X3dGraphics.com/examples/X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.x3d\" \"https://savage.nps.edu/Savage/Tools/Authoring/CoordinateAxes.x3d\" \"../../X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.wrl\" \"../../../Savage/Tools/Authoring/CoordinateAxes.wrl\" \"http://X3dGraphics.com/examples/X3dForWebAuthors/Chapter03-Grouping/CoordinateAxes.wrl\" \"https://savage.nps.edu/Savage/Tools/Authoring/CoordinateAxes.wrl\"'/>\n");
                    }
                    sb.append("      <ProtoInstance name='ViewFrustum'>\n").append("        <fieldValue name='ViewpointNode'>\n").append("          <Viewpoint ");
                    sb.append(this.createAttributes()).append("/>\n");
                    sb.append("      </fieldValue>\n").append("        <fieldValue name='visible' value='true'/>\n").append("        <fieldValue name='lineColor' value='").append(lineColor).append("'/>\n").append("        <fieldValue name='frustumColor' value='").append(shapeColor).append("'/>\n").append("        <fieldValue name='transparency' value='").append(shapeTransparency).append("'/>\n").append("        <fieldValue name='aspectRatio' value='0.75'/>\n").append("        <!-- can also modify corresponding NavigationInfo to match ViewFrustrum\n").append("          <fieldValue name='NavigationInfoNode'>\n").append("            <NavigationInfo DEF='TestNavigationInfo' avatarSize='1 1.6 0.75' visibilityLimit='15'/>\n").append("          </fieldValue>\n").append("        -->\n").append("      </ProtoInstance>\n").append("      <!-- Example use: http://x3dgraphics.com/examples/X3dForWebAuthors/Chapter14-Prototypes/ViewFrustumExample.x3d -->\n").append("    </Switch>\n");
                }
            }
            if (getElementName().equals("Extrusion")) {
                if (isVisualizationSelectionAvailable()) {
                    sb.insert(0, "<!-- ");
                    sb.append("\n      replaced by follow-on ExtrusionCrossSection visualization -->");
                    setAppendExtrusionCrossSection(true);
                }
            }
        } catch (Exception ex) {
            System.out.println("BaseX3dElement visualization exception " + ex);
            ex.printStackTrace();
        }
        return sb.toString();
    }

    /**
   * Does not rebuild JDOM document
   * @param targ
   * @param excludeList
   * @return
   */
    protected HashMap<String, Vector<Element>> getNodeMap(JTextComponent targ, Vector<String> excludeList) {
        org.jdom.Document doc = X3DPaletteUtilities.getJdom(targ);
        HashMap<String, Vector<Element>> hm = new HashMap<String, Vector<Element>>();
        addMappedElems(doc.getRootElement(), hm, false, excludeList);
        return hm;
    }

    protected HashMap<String, Vector<Element>> getNodeMap(JTextComponent targ) {
        return getNodeMap(targ, null);
    }

    @SuppressWarnings("unchecked")
    protected void addMappedElems(Element par, HashMap<String, Vector<Element>> hm, boolean includeParent, Vector<String> excludeList) {
        if (includeParent) if (!hmPut(par, hm, excludeList)) return;
        List<Element> lis = (List<Element>) par.getChildren();
        for (Element child : lis) {
            addMappedElems(child, hm, true, excludeList);
        }
    }

    private boolean hmPut(Element elem, HashMap<String, Vector<Element>> hm, Vector<String> excludeList) {
        if (excludeList != null) for (String nono : excludeList) {
            if (nono.equals(elem.getName())) {
                return false;
            }
        }
        Vector<Element> v = hm.get(elem.getName());
        if (v == null) hm.put(elem.getName(), v = new Vector<Element>());
        v.add(elem);
        return true;
    }

    /**
   * We are a "connect" node; return all field elements within the parent of my inclosing IS node 
   * @return vector of JDOM field elements
   */
    @SuppressWarnings("unchecked")
    protected Vector getFieldElementsWithinISparent() {
        Vector v = new Vector();
        Element el = this.elementLocation.element;
        if (!el.getName().equals(IS_ELNAME)) {
            el = el.getParentElement();
            if (!el.getName().equals(IS_ELNAME)) {
                el = el.getParentElement();
                if (!el.getName().equals(IS_ELNAME)) {
                    el = null;
                }
            }
        }
        if (el != null) {
            Element parentEl = el.getParentElement();
            List lis = null;
            if (parentEl.getName().equals(SCRIPT_ELNAME)) lis = parentEl.getChildren(FIELD_ELNAME); else lis = buildFieldPanList(parentEl.getName());
            v.addAll(lis);
        }
        return v;
    }

    Vector<FieldPan> buildFieldPanList(String name) {
        Vector<FieldPan> vec = new Vector<FieldPan>();
        X3DFieldDefinition[] fd = X3DNode.getNodeFields(name);
        for (X3DFieldDefinition fld : fd) {
            vec.add(new FieldPan(fld.getName(), fld.getFieldTypeString(), ROUTECustomizer.accessToString(fld.getAccessType())));
        }
        return vec;
    }

    @SuppressWarnings("unchecked")
    public boolean isConnectedWithIS() {
        Element fieldElem = this.elementLocation.element;
        String fieldName = fieldElem.getAttributeValue(FIELD_ATTR_NAME_NAME);
        if (fieldName == null || fieldName.length() <= 0) return false;
        Element e2 = fieldElem.getParentElement();
        if (e2 == null) return false;
        List<Element> lis = (List<Element>) e2.getChildren(IS_ELNAME);
        if (lis == null || lis.size() <= 0) return false;
        for (Element ch : lis) {
            List<Element> connectList = (List<Element>) ch.getChildren(CONNECT_ELNAME);
            if (connectList == null || connectList.size() <= 0) continue;
            for (Element connElem : connectList) {
                Attribute attr = connElem.getAttribute(CONNECT_ATTR_NODEFIELD_NAME);
                if (attr != null && attr.getValue().equals(fieldName)) return true;
            }
        }
        return false;
    }

    public boolean isExternProtoDeclareChild() {
        Element el = this.elementLocation.element;
        Element par = el.getParentElement();
        return par != null && par.getName().equals(EXTERNPROTODECLARE_ELNAME);
    }

    /**
   * We are a "connect" node; return all field elements within my enclosing ProtoDeclare/ProtoInterface
   * @return vector of JDOM field elements
   */
    @SuppressWarnings("unchecked")
    protected Vector<Element> getFieldElementsWithinProtoInterfaceOrInstance() {
        Vector<Element> v = new Vector<Element>();
        Element el = this.elementLocation.element;
        while (el.getParentElement() != null) {
            el = el.getParentElement();
            if (el.getName().equals(PROTODECLARE_ELNAME)) {
                el = el.getChild(PROTOINTERFACE_ELNAME);
                if (el != null) {
                    List<Element> lis = el.getChildren(FIELD_ELNAME);
                    v.addAll(lis);
                    break;
                }
            } else if (el.getName().equals(PROTOINSTANCE_ELNAME)) {
                List<Element> lis = el.getChildren(FIELDVALUE_ELNAME);
                v.addAll(lis);
                break;
            }
        }
        return v;
    }

    /**
   * Return all nodes either beneath a node in the marker list, if the current location
   * lies beneath, or from the root, if the current location is not within a marker element.
   * Used for finding legal routes within/without a ProtoBody.
   * @param targ
   * @param markerList
   * @return
   */
    protected Vector<Element> getSpecialNodeList(JTextComponent targ, Vector<String> markerList) {
        Vector<Element> v = new Vector<Element>();
        Element localroot = null;
        Element el = this.elementLocation.element;
        big: while (el != null) {
            localroot = el;
            for (String s : markerList) if (s.equals(el.getName())) break big;
            el = el.getParentElement();
        }
        addElems(localroot, v, false, markerList);
        return v;
    }

    /**
   * Get a complete list, except for branches whose names are in the list
   * Does not rebuild JDOM document
   * @param excludeList
   * @param targ
   * @return list
   */
    protected Vector<Element> getNodeList(JTextComponent targ, Vector<String> excludeList) {
        org.jdom.Document doc = X3DPaletteUtilities.getJdom(targ);
        Vector<Element> v = new Vector<Element>();
        addElems(doc.getRootElement(), v, false, excludeList);
        return v;
    }

    protected Vector<Element> getNodeList(JTextComponent targ) {
        return getNodeList(targ, null);
    }

    @SuppressWarnings("unchecked")
    private void addElems(Element par, Vector<Element> v, boolean includeParent, Vector<String> excludeList) {
        if (includeParent) if (!vPut(par, v, excludeList)) return;
        List<Element> lis = (List<Element>) par.getChildren();
        for (Element child : lis) {
            addElems(child, v, true, excludeList);
        }
    }

    /**
   * @param parentEl
   * @param v
   * @param excludeList
   * @return added (t) or excluded (f)
   */
    private boolean vPut(Element elem, Vector<Element> v, Vector<String> excludeList) {
        if (excludeList != null) {
            for (String nono : excludeList) {
                if (nono.equals(elem.getName())) return false;
            }
        }
        v.add(elem);
        return true;
    }

    /**
   * hasDEF names which are within an enclosing ProtoBody are legal.  If the current
   * dropped location or existing element is within a protobody, use only those nodes.
   * Else use all nodes outside of all protobodies
   * enclosed, return
   * @param target
   * @param limits 
   * @return
   */
    protected Vector<Element> getLimitedNodeList(JTextComponent target, String[] limits) {
        return null;
    }

    private static Vector<String> noProtoDecls;

    static {
        noProtoDecls = new Vector<String>();
        noProtoDecls.add(PROTODECLARE_ELNAME);
        noProtoDecls.add(EXTERNPROTODECLARE_ELNAME);
    }

    public void initializeFromDom(org.w3c.dom.Document doc, JTextComponent comp) {
    }

    public void initializeFromJdom(org.jdom.Element root, JTextComponent comp) {
        initialize();
        USEvector = X3DPaletteUtilities.getUSEvector(comp, getElementName());
        org.jdom.Attribute attr = root.getAttribute("USE");
        if (attr != null) {
            setDEFUSE(attr.getValue());
            setDEF(false);
        } else if ((attr = root.getAttribute("DEF")) != null) {
            setDEFUSE(attr.getValue());
            USEvector.remove(attr.getValue());
            setDEF(true);
        } else {
            setDEFUSE(null);
            setDEF(true);
        }
        if ((attr = root.getAttribute("containerField")) != null) {
            setUseContainerField(true);
            setContainerField(attr.getValue());
        }
        setupContent(root);
    }

    protected void setupContent(org.jdom.Element root) {
        @SuppressWarnings("unchecked") List<Object> lis = root.getContent();
        String contentText = null;
        if (lis.size() > 0) {
            try {
                StringWriter sw = new StringWriter();
                XMLOutputter xout = new X3DXMLOutputter();
                xout.outputElementContent(root, sw);
                contentText = sw.toString();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        if (contentText != null && contentText.length() > 0) setContent(contentText);
    }

    protected boolean hasDEF = true;

    protected String DEFUSE = "";

    private Vector USEvector;

    public boolean isDEF() {
        return hasDEF;
    }

    public void setDEF(boolean isdef) {
        hasDEF = isdef;
    }

    public String getDEFUSE() {
        if (DEFUSE == null) return ""; else return DEFUSE;
    }

    public void setDEFUSE(String DEFUSE) {
        this.DEFUSE = DEFUSE;
    }

    public Vector getUSEVector() {
        return USEvector;
    }

    public void setUSEVector(Vector v) {
        USEvector = v;
    }

    public String buildDEF() {
        String df = getDEFUSE();
        if (isDEF() && (df != null && df.length() > 0)) {
            StringBuilder sb = new StringBuilder();
            sb.append(" DEF='");
            sb.append(df);
            sb.append("'");
            return sb.toString();
        }
        return "";
    }

    private boolean useContainerField = false;

    private String containerField = "";

    public boolean isUseContainerField() {
        return useContainerField;
    }

    public void setUseContainerField(boolean b) {
        useContainerField = b;
    }

    public String getContainerField() {
        return containerField;
    }

    public void setContainerField(String s) {
        containerField = s;
    }

    public String buildContainerField() {
        String cf = getContainerField();
        if (isUseContainerField() && (cf != null && cf.length() > 0)) {
            StringBuilder sb = new StringBuilder();
            sb.append(" containerField='");
            sb.append(cf);
            sb.append("'");
            return sb.toString();
        }
        return "";
    }

    private int sequenceNum = 0;

    private String sequenceString = "";

    protected String getSequenceString() {
        String ret = sequenceString;
        sequenceString = "_" + ++sequenceNum;
        return ret;
    }

    public static String[] parseSentences(String str) {
        String s = str.trim();
        if (s.startsWith("\"")) s = s.substring(1);
        if (s.endsWith("\"")) s = s.substring(0, s.length() - 1);
        return s.split("\"\\s*\"");
    }

    public static String[] parseUrlsIntoStringArray(String xstr) {
        if (xstr == null || xstr.length() <= 0) return new String[0];
        String s = xstr.trim();
        String regEx = "\"+\\s*\"*";
        if ((s.charAt(0) == '"') || (s.charAt(0) == ',')) {
            while (s.length() > 0 && ((s.charAt(0) == '"') || (s.charAt(0) == ','))) {
                s = s.substring(1);
            }
        } else if (s.charAt(0) == '\'') {
            regEx = "'+\\s*'*";
            while (s.length() > 0 && s.charAt(0) == '\'') {
                s = s.substring(1);
            }
        }
        String[] sa = s.split(regEx);
        int size = sa.length;
        for (int i = 0; i < sa.length; i++) {
            if ((sa[i] != null) && (sa[i].replace(",", "").trim().length()) == 0) {
                for (int j = i + 1; j < sa.length; j++) {
                    sa[j - 1] = sa[j];
                }
                sa[sa.length - 1] = null;
                size--;
            }
        }
        String[] newArray = new String[size];
        System.arraycopy(sa, 0, newArray, 0, size);
        return newArray;
    }

    /**
   * Properly split MFString into String[] array of SFString elements, do not remove surrounding "quote" marks
   * @param inputString  X3D MFString input value, held as a Java String
   * @return String[] array holding SFString elements
   */
    public static String[] parseIntoStringArray(String inputString) {
        return parseIntoStringArray(inputString, false);
    }

    /**
   * Properly split MFString into String[] array of SFString elements
   * @param inputString  X3D MFString input value, held as a Java String
   * @param removeQuotes Whether to remove "quote" marks surrounding SFString elements
   * @return String[] array holding SFString elements
   */
    public static String[] parseIntoStringArray(String inputString, boolean removeQuotes) {
        String[] sa = new String[0];
        ArrayList<String> stringList = new ArrayList<String>();
        StringBuffer newString = new StringBuffer();
        boolean betweenStrings = true;
        if (inputString.isEmpty()) {
            return sa;
        }
        if (!inputString.contains("\"")) {
            stringList.add(inputString);
        } else {
            for (int i = 0; i < inputString.length(); i++) {
                while (betweenStrings && (inputString.substring(i, i + 1).trim().length() == 0) && (i < inputString.length() - 1)) {
                    i++;
                }
                betweenStrings = false;
                if (inputString.charAt(i) == '\"') {
                    if ((i < inputString.length() - 1)) i++;
                } else {
                    System.out.println("new SFString element within MFString started illegally at character index " + i);
                }
                while (i < inputString.length()) {
                    if ((inputString.length() > i + 2) && inputString.substring(i, i + 2).equalsIgnoreCase("\\\"")) {
                        newString.append(inputString.substring(i, i + 2));
                        i = i + 2;
                    } else if ((inputString.length() == i + 2) && inputString.substring(i, i + 2).equalsIgnoreCase("\\\"")) {
                        newString.append(inputString.charAt(i));
                        i++;
                        System.out.println("parseIntoStringArray() encountered SFString terminated by escaped quote \\\" mismatch: " + inputString);
                    } else if ((inputString.length() >= i + 2) && inputString.substring(i, i + 2).equalsIgnoreCase("\\\\")) {
                        newString.append(inputString.substring(i, i + 2));
                        i = i + 2;
                    } else if (inputString.charAt(i) == '\"') {
                        betweenStrings = true;
                        stringList.add(newString.toString());
                        newString = new StringBuffer();
                        break;
                    } else {
                        newString.append(inputString.charAt(i));
                        i++;
                    }
                }
                if ((i == inputString.length() - 1) && (newString.length() > 0)) {
                    System.out.println("parseIntoStringArray() encountered unterminated SFString, mismatched quotes: " + inputString);
                    stringList.add(newString.toString());
                } else if (newString.length() > 0) stringList.add(newString.toString());
            }
        }
        sa = new String[stringList.size()];
        int index = 0;
        for (String s : stringList) {
            StringBuilder escapedString = new StringBuilder();
            for (int c = 0; c < s.length(); c++) {
                if (((s.length() - c) >= 2) && s.substring(c).startsWith("\\\\")) {
                    escapedString.append('\\');
                    c = c + 1;
                } else {
                    if (((s.length() - c) >= 2) && s.substring(c).startsWith("\\\"")) {
                        escapedString.append('"');
                        c = c + 1;
                    } else {
                        escapedString.append(s.charAt(c));
                    }
                }
            }
            sa[index] = escapedString.toString();
            if (removeQuotes) {
                if ((sa[index].length() > 0) && (sa[index].charAt(0) == '"')) sa[index] = sa[index].substring(1);
                if ((sa[index].length() > 0) && (sa[index].charAt(sa[index].length() - 1) == '"')) sa[index] = sa[index].substring(0, sa[index].length() - 1);
            }
            index++;
        }
        return sa;
    }

    public static String concatStringArray(String[] sa) {
        return concatStringArray(sa, 0);
    }

    public static String concatStringArray(String[] sa, int firstIdx) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String s : sa) {
            if (i++ < firstIdx) continue;
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    protected String validateTripleNumbers(String s) {
        return validateNumbersX(s, 3);
    }

    protected String validateEvenNumbers(String s) {
        return validateNumbersX(s, 2);
    }

    protected String validateNumbers(String s) {
        return validateNumbersX(s, -1);
    }

    protected String validateNumbersX(String s, int x) {
        s = s.trim();
        if (s.length() <= 0) return s;
        String[] sa = X3DSchemaData.parseX(s);
        if (x > 0) if (sa.length % x != 0) throw new IllegalArgumentException("Wrong number of elements in number array");
        StringBuilder sb = new StringBuilder();
        int n = 0;
        for (String str : sa) {
            try {
                Float.parseFloat(str);
                sb.append(str);
                if (x > 0) {
                    if (++n % x == 0) sb.append(", "); else sb.append(" ");
                } else sb.append(" ");
            } catch (Exception e) {
                throw new IllegalArgumentException("Non-numeric data detected");
            }
        }
        if (x > 0 && sb.length() > 0) sb.setLength(sb.length() - 2); else sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    protected String validateIntegers(String s) {
        if (s.length() <= 0) return s;
        String[] sa = X3DSchemaData.parseX(s);
        StringBuilder sb = new StringBuilder();
        int n = 0;
        for (String str : sa) {
            try {
                Integer.parseInt(str);
                sb.append(str);
                sb.append(" ");
            } catch (Exception e) {
                throw new IllegalArgumentException("Non-numeric data detected");
            }
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    protected String validatePositiveIntegersOrMinusOne(String s) {
        String ss = validateIntegers(s);
        String[] sa = X3DSchemaData.parseX(ss);
        StringBuilder sb = new StringBuilder();
        if (sa.length == 1) {
            if (Integer.parseInt(sa[0]) == -1) return ss;
        }
        for (String str : sa) {
            if (Integer.parseInt(str) < 0) throw new IllegalArgumentException("Negative number detected");
        }
        return ss;
    }

    protected SFFloat[][] makeSFFloatArray(String[][] saa) {
        SFFloat[][] faa = new SFFloat[saa.length][saa[0].length];
        int r = faa.length;
        int c = faa[0].length;
        int i = 0;
        for (int ir = 0; ir < r; ir++) for (int ic = 0; ic < c; ic++) faa[ir][ic] = buildSFFloat(saa[ir][ic]);
        return faa;
    }

    protected SFFloat[][] makeSFFloatArray(String[] sa, int width) {
        SFFloat[][] faa = new SFFloat[sa.length / width][width];
        int r = faa.length;
        int c = faa[0].length;
        int i = 0;
        for (int ir = 0; ir < r; ir++) for (int ic = 0; ic < c; ic++) faa[ir][ic] = buildSFFloat(sa[i++]);
        return faa;
    }

    protected SFDouble[][] makeSFDoubleArray(String[][] saa) {
        SFDouble[][] daa = new SFDouble[saa.length][saa[0].length];
        int r = daa.length;
        int c = daa[0].length;
        int i = 0;
        for (int ir = 0; ir < r; ir++) for (int ic = 0; ic < c; ic++) daa[ir][ic] = buildSFDouble(saa[ir][ic]);
        return daa;
    }

    protected SFDouble[][] makeSFDoubleArray(String[] sa, int width) {
        SFDouble[][] daa = new SFDouble[sa.length / width][width];
        int r = daa.length;
        int c = daa[0].length;
        int i = 0;
        for (int ir = 0; ir < r; ir++) for (int ic = 0; ic < c; ic++) daa[ir][ic] = buildSFDouble(sa[i++]);
        return daa;
    }

    protected SFInt32[][] makeSFIntArray(String[][] saa) {
        SFInt32[][] iaa = new SFInt32[saa.length][saa[0].length];
        int r = iaa.length;
        int c = iaa[0].length;
        int i = 0;
        for (int ir = 0; ir < r; ir++) for (int ic = 0; ic < c; ic++) iaa[ir][ic] = buildSFInt(saa[ir][ic]);
        return iaa;
    }

    protected SFInt32[][] makeSFIntArray(String[] sa, int width) {
        SFInt32[][] iaa = new SFInt32[sa.length / width][width];
        int r = iaa.length;
        int c = iaa[0].length;
        int i = 0;
        for (int ir = 0; ir < r; ir++) for (int ic = 0; ic < c; ic++) iaa[ir][ic] = buildSFInt(sa[i++]);
        return iaa;
    }

    protected String[][] makeStringArray(SFFloat[][] sffaa) {
        String[][] sa = new String[sffaa.length][sffaa[0].length];
        int r = sffaa.length;
        int c = sffaa[0].length;
        int i = 0;
        for (int ir = 0; ir < r; ir++) for (int ic = 0; ic < c; ic++) sa[ir][ic] = sffaa[ir][ic].toString();
        return sa;
    }

    protected String[][] makeStringArray(SFDouble[][] sfdaa) {
        String[][] sa = new String[sfdaa.length][sfdaa[0].length];
        int r = sfdaa.length;
        int c = sfdaa[0].length;
        int i = 0;
        for (int ir = 0; ir < r; ir++) for (int ic = 0; ic < c; ic++) sa[ir][ic] = sfdaa[ir][ic].toString();
        return sa;
    }

    protected String[][] makeStringArray(SFInt32[][] iaa) {
        String[][] sa = new String[iaa.length][iaa[0].length];
        int r = iaa.length;
        int c = iaa[0].length;
        int i = 0;
        for (int ir = 0; ir < r; ir++) for (int ic = 0; ic < c; ic++) sa[ir][ic] = iaa[ir][ic].toString();
        return sa;
    }

    protected boolean arraysIdenticalOrNull(boolean[] ba, boolean[] ba2) {
        if (ba.length == 0 && ba2.length == 0) return true;
        if (ba.length != ba2.length) return false;
        int c = ba.length;
        for (int i = 0; i < c; i++) if (!(ba[i] == ba2[i])) return false;
        return true;
    }

    protected boolean arraysIdenticalOrNull(SFFloat[] fa1, SFFloat[] fa2) {
        if ((fa1.length == 0) && (fa2.length == 0)) return true;
        if (fa1.length != fa2.length) return false;
        int c = fa1.length;
        for (int i = 0; i < c; i++) {
            if (!fa1[i].equals(fa2[i])) return false;
        }
        return true;
    }

    protected boolean arraysIdenticalOrNull(SFDouble[] da1, SFDouble[] da2) {
        if ((da1.length == 0) && (da2.length == 0)) return true;
        if (da1.length != da2.length) return false;
        int c = da1.length;
        for (int i = 0; i < c; i++) {
            if (!da1[i].equals(da2[i])) return false;
        }
        return true;
    }

    protected boolean arraysIdenticalOrNull(SFInt32[] intAr, SFInt32[] intAr2) {
        if (intAr.length == 0 && intAr2.length == 0) return true;
        if (intAr.length != intAr2.length) return false;
        int c = intAr.length;
        for (int i = 0; i < c; i++) {
            if (!intAr[i].equals(intAr2[i])) return false;
        }
        return true;
    }

    protected boolean arraysIdenticalOrNull(SFFloat[][] array1, SFFloat[][] array2) {
        if ((array1 == null) || (array1.length == 0)) {
            if ((array2 == null) || (array2.length == 0)) return true; else return false;
        }
        if ((array2 == null) || (array2.length == 0)) {
            if ((array1 == null) || (array1.length == 0)) return true; else return false;
        }
        if (array1.length != array2.length) return false;
        if (array1[0].length != array2[0].length) return false;
        int r = array1.length;
        int c = array1[0].length;
        if (r == 0) return true;
        for (int ir = 0; ir < r; ir++) for (int ic = 0; ic < c; ic++) if (!array1[ir][ic].equals(array2[ir][ic])) return false;
        return true;
    }

    protected boolean arraysIdenticalOrNull(SFDouble[][] vec1, SFDouble[][] vect2) {
        if (vec1.length == 0 && vect2.length == 0) return true;
        if (vec1.length != vect2.length) return false;
        if (vec1[0].length != vect2[0].length) return false;
        int r = vec1.length;
        int c = vec1[0].length;
        if (r == 0) return true;
        for (int ir = 0; ir < r; ir++) for (int ic = 0; ic < c; ic++) if (!vec1[ir][ic].equals(vect2[ir][ic])) return false;
        return true;
    }

    protected String formatStringArray(String[] sa, boolean newInsertCommas, boolean newInsertLineBreaks) {
        insertCommas = newInsertCommas;
        insertLineBreaks = newInsertLineBreaks;
        return formatStringArray(sa);
    }

    protected String formatStringArray(String[] sa) {
        StringBuilder sb = new StringBuilder();
        if ((sa.length > 0) && insertLineBreaks) sb.append("\n");
        for (int i = 0; i < sa.length; i++) {
            String value = sa[i];
            StringBuilder escapedValue = new StringBuilder();
            for (int c = 0; c < value.length(); c++) {
                if (value.charAt(c) == '"') {
                    escapedValue.append('\\');
                    escapedValue.append("\"");
                } else if (value.charAt(c) == '&') escapedValue.append("&amp;"); else if (value.charAt(c) == '\'') escapedValue.append("&apos;"); else if (value.charAt(c) == '\\') escapedValue.append("\\\\"); else if ((int) (value.charAt(c)) > 127) escapedValue.append("&#").append((int) (value.charAt(c))).append(";"); else escapedValue.append(value.charAt(c));
            }
            sb.append("\"");
            sb.append(escapedValue);
            sb.append("\"");
            if ((i < sa.length - 1) && insertCommas) sb.append(",");
            if (insertLineBreaks) sb.append("\n"); else if (i < sa.length - 1) sb.append(" ");
        }
        return sb.toString();
    }

    protected String formatBooleanArray(boolean[] ba, boolean newInsertCommas, boolean newInsertLineBreaks) {
        insertCommas = newInsertCommas;
        insertLineBreaks = newInsertLineBreaks;
        return formatBooleanArray(ba);
    }

    protected String formatBooleanArray(String[] ba, boolean newInsertCommas, boolean newInsertLineBreaks) {
        insertCommas = newInsertCommas;
        insertLineBreaks = newInsertLineBreaks;
        return formatBooleanArray(ba);
    }

    protected String formatBooleanArray(boolean[] ba) {
        StringBuilder sb = new StringBuilder();
        if ((ba.length > 0) && insertLineBreaks) sb.append("\n");
        for (int i = 0; i < ba.length; i++) {
            sb.append(ba[i]);
            if ((i < ba.length - 1) && insertCommas) sb.append(",");
            if (insertLineBreaks) sb.append("\n"); else if (i < ba.length - 1) sb.append(" ");
        }
        return sb.toString();
    }

    protected String formatBooleanArray(String[] ba) {
        StringBuilder sb = new StringBuilder();
        if ((ba.length > 0) && insertLineBreaks) sb.append("\n");
        for (int i = 0; i < ba.length; i++) {
            sb.append(ba[i]);
            if ((i < ba.length - 1) && insertCommas) sb.append(",");
            if (insertLineBreaks) sb.append("\n"); else if (i < ba.length - 1) sb.append(" ");
        }
        return sb.toString();
    }

    protected String formatFloatArray(SFFloat[] sffa, boolean newInsertCommas, boolean newInsertLineBreaks) {
        insertCommas = newInsertCommas;
        insertLineBreaks = newInsertLineBreaks;
        return formatFloatArray(sffa);
    }

    protected String formatFloatArray(SFFloat[] sffa) {
        StringBuilder sb = new StringBuilder();
        if (sffa.length == 0) return "";
        if ((sffa.length > 0) && insertLineBreaks) sb.append("\n");
        for (int i = 0; i < sffa.length; i++) {
            sb.append(sffa[i].toString());
            if ((i < sffa.length - 1) && insertCommas) sb.append(",");
            if (insertLineBreaks) sb.append("\n"); else if (i < sffa.length - 1) sb.append(" ");
        }
        return sb.toString();
    }

    protected String formatDoubleArray(SFDouble[] sfdaa, boolean newInsertCommas, boolean newInsertLineBreaks) {
        insertCommas = newInsertCommas;
        insertLineBreaks = newInsertLineBreaks;
        return formatDoubleArray(sfdaa);
    }

    protected String formatDoubleArray(SFDouble[] sfda) {
        StringBuilder sb = new StringBuilder();
        if (sfda.length == 0) return "";
        if (insertLineBreaks) sb.append("\n");
        for (int i = 0; i < sfda.length; i++) {
            sb.append(fiveDigitFormat.format(sfda[i]));
            if ((i < sfda.length - 1) && insertCommas) sb.append(",");
            if (insertLineBreaks) sb.append("\n"); else if (i < sfda.length - 1) sb.append(" ");
        }
        return sb.toString();
    }

    protected String formatIndexArray(String intArrayString, boolean newBreakLinesAfterIndexSentinel) {
        insertCommas = false;
        insertLineBreaks = false;
        breakLinesAfterIndexSentinel = newBreakLinesAfterIndexSentinel;
        return formatIntArray(parseToSFIntArray(parseX(intArrayString)));
    }

    protected String formatIntArray(SFInt32[] ia, boolean newInsertCommas, boolean newInsertLineBreaks) {
        insertCommas = newInsertCommas;
        insertLineBreaks = newInsertLineBreaks;
        return formatIntArray(ia);
    }

    protected String formatIntArray(SFInt32[] ia) {
        StringBuilder sb = new StringBuilder();
        if ((ia.length > 0) && insertLineBreaks) sb.append("\n");
        for (int i = 0; i < ia.length; i++) {
            sb.append(ia[i].toString());
            if ((i < ia.length - 1) && insertCommas) sb.append(",");
            if (insertLineBreaks || (breakLinesAfterIndexSentinel && (ia[i].getValue() <= -1))) sb.append("\n"); else if (i < ia.length - 1) sb.append(" ");
        }
        breakLinesAfterIndexSentinel = false;
        return sb.toString();
    }

    /**
   * Insert commas and/or line breaks into string as indicated
   * @param inputString the input string
   * @param doInsertCommas whether to insert commas
   * @param doInsertLineBreaks whether to insert line breaks
   * @param count how many substrings per line
   * @return resulting string
   */
    protected String formatStringElements(String inputString, boolean doInsertCommas, boolean doInsertLineBreaks, int count) {
        StringBuilder sb = new StringBuilder();
        insertCommas = doInsertCommas;
        insertLineBreaks = doInsertLineBreaks;
        String[] stringArray = inputString.split(" ");
        if (insertLineBreaks) sb.append("\n");
        for (int i = 0; i < stringArray.length; i++) {
            sb.append(stringArray[i]).append(" ");
            if (((i + 1) % count) == 0) {
                sb.setLength(sb.length() - 1);
                if ((i < stringArray.length - 1) && insertCommas) sb.append(",");
                if (insertLineBreaks) sb.append("\n"); else if (i < stringArray.length - 1) sb.append(" ");
            }
        }
        return sb.toString();
    }

    protected String formatFloatArray(SFFloat[][] faa, boolean newInsertCommas, boolean newInsertLineBreaks) {
        insertCommas = newInsertCommas;
        insertLineBreaks = newInsertLineBreaks;
        return formatFloatArray(faa);
    }

    protected String formatFloatArray(SFFloat[][] sffaa) {
        StringBuilder sb = new StringBuilder();
        if (sffaa.length == 0) return "";
        int r = sffaa.length;
        int c = sffaa[0].length;
        int i = 0;
        if (insertLineBreaks) sb.append("\n");
        for (int ir = 0; ir < r; ir++) {
            for (int ic = 0; ic < c; ic++) {
                sb.append(sffaa[ir][ic].toString());
                sb.append(" ");
            }
            sb.setLength(sb.length() - 1);
            if ((ir < r - 1) && insertCommas) sb.append(",");
            if (insertLineBreaks) sb.append("\n"); else if (ir < r - 1) sb.append(" ");
        }
        return sb.toString();
    }

    protected String formatDoubleArray(SFDouble[][] sfdaa, boolean newInsertCommas, boolean newInsertLineBreaks) {
        insertCommas = newInsertCommas;
        insertLineBreaks = newInsertLineBreaks;
        return formatDoubleArray(sfdaa);
    }

    protected String formatDoubleArray(SFDouble[][] sfdaa) {
        StringBuilder sb = new StringBuilder();
        if (sfdaa.length == 0) return "";
        int r = sfdaa.length;
        int c = sfdaa[0].length;
        int i = 0;
        if (insertLineBreaks) sb.append("\n");
        for (int ir = 0; ir < r; ir++) {
            for (int ic = 0; ic < c; ic++) {
                sb.append(sfdaa[ir][ic].toString());
                sb.append(" ");
            }
            sb.setLength(sb.length() - 1);
            if ((ir < r - 1) && insertCommas) sb.append(",");
            if (insertLineBreaks) sb.append("\n"); else if (ir < r - 1) sb.append(" ");
        }
        return sb.toString();
    }

    protected SFFloat[] parseToSFFloatArray(String[] sa) {
        SFFloat[] fa = new SFFloat[sa.length];
        for (int i = 0; i < fa.length; i++) {
            fa[i] = buildSFFloat(sa[i]);
        }
        return fa;
    }

    protected SFDouble[] parseToSFDoubleArray(String[] sa) {
        SFDouble[] da = new SFDouble[sa.length];
        for (int i = 0; i < da.length; i++) {
            da[i] = buildSFDouble(sa[i]);
        }
        return da;
    }

    protected SFFloat[][] parseToSFFloatTable(String[] sa, int numColumns) {
        SFFloat[][] faa = new SFFloat[sa.length / numColumns][numColumns];
        int r = faa.length;
        if (r <= 0) return faa;
        int c = faa[0].length;
        int i = 0;
        for (int ir = 0; ir < r; ir++) for (int ic = 0; ic < c; ic++) faa[ir][ic] = buildSFFloat(sa[i++]);
        return faa;
    }

    protected SFDouble[][] parseToSFDoubleTable(String[] sa, int numColumns) {
        SFDouble[][] daa = new SFDouble[sa.length / numColumns][numColumns];
        int r = daa.length;
        if (r <= 0) return daa;
        int c = daa[0].length;
        int i = 0;
        for (int ir = 0; ir < r; ir++) for (int ic = 0; ic < c; ic++) daa[ir][ic] = buildSFDouble(sa[i++]);
        return daa;
    }

    protected SFFloat buildSFFloat(String s) {
        return new SFFloat(s, null, null);
    }

    protected SFDouble buildSFDouble(String s) {
        return new SFDouble(s, null, null);
    }

    protected SFInt32[] parseToSFIntArray(String[] sa) {
        SFInt32[] ia = new SFInt32[sa.length];
        for (int i = 0; i < ia.length; i++) ia[i] = buildSFInt(sa[i]);
        return ia;
    }

    protected boolean[] parseToBooleanArray(String[] stringArray) {
        String[] sa = stringArray;
        boolean capitalizationCorrected = false;
        boolean illegalValueFound = false;
        for (int i = 0; i < sa.length; i++) {
            if (sa[i].equalsIgnoreCase("TRUE") && !sa[i].equals("true")) {
                sa[i] = "true";
                capitalizationCorrected = true;
            } else if (sa[i].equalsIgnoreCase("FALSE") && !sa[i].equals("false")) {
                sa[i] = "false";
                capitalizationCorrected = true;
            } else if (!sa[i].equals("true") && !sa[i].equals("false")) {
                illegalValueFound = true;
            }
        }
        if (capitalizationCorrected) System.out.println("MFBool capitalization corrected (must be 'true' or 'false'), parseToBooleanArray(" + stringArray.toString() + ")");
        if (illegalValueFound) System.out.println("MFBool illegal value found (must be 'true' or 'false'), will be treated as false. parseToBooleanArray(" + stringArray.toString() + ")");
        boolean[] ba = new boolean[sa.length];
        for (int i = 0; i < ba.length; i++) ba[i] = Boolean.parseBoolean(sa[i]);
        return ba;
    }

    protected SFInt32 buildSFInt(String s) {
        return new SFInt32(s, null, null);
    }

    public String quotify(String s) {
        StringBuilder sb = new StringBuilder();
        String[] sa = s.split("\\s+");
        for (String str : sa) {
            sb.append('\"');
            sb.append(str);
            sb.append("\" ");
        }
        return sb.toString().trim();
    }

    public static String unQuotify(String s) {
        StringBuilder sb = new StringBuilder();
        String[] sa = s.split("[\"\\s]");
        for (String str : sa) {
            if (str.length() <= 0) continue;
            sb.append(str);
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    /**
     * @return visualizationSelectionAvailable whether Visualize checkbox is visible
     */
    public boolean isVisualizationSelectionAvailable() {
        return visualizationSelectionAvailable;
    }

    /**
     * @param aVisualizationSelectionAvailable the visualizationSelectionAvailable value to set
     */
    public void setVisualizationSelectionAvailable(boolean aVisualizationSelectionAvailable) {
        visualizationSelectionAvailable = aVisualizationSelectionAvailable;
    }

    /**
     * @return the visualizationTooltip
     */
    public String getVisualizationTooltip() {
        return visualizationTooltip;
    }

    /**
     * @param aVisualizationTooltip the visualizationTooltip to set
     */
    public void setVisualizationTooltip(String aVisualizationTooltip) {
        visualizationTooltip = aVisualizationTooltip;
    }

    /**
     * @return the traceEventsSelectionAvailable
     */
    public boolean isTraceEventsSelectionAvailable() {
        return traceEventsSelectionAvailable;
    }

    /**
     * @param traceEventsSelectionAvailable whether Trace checkbox is visible
     */
    public void setTraceEventsSelectionAvailable(boolean traceEventsSelectionAvailable) {
        this.traceEventsSelectionAvailable = traceEventsSelectionAvailable;
    }

    /**
     * @return the traceEventsTooltip
     */
    public String getTraceEventsTooltip() {
        return traceEventsTooltip;
    }

    /**
     * @param traceEventsTooltip the traceEventsTooltip to set
     */
    public void setTraceEventsTooltip(String traceEventsTooltip) {
        this.traceEventsTooltip = traceEventsTooltip;
    }

    /**
     * @return the appendExtrusionCrossSection
     */
    public boolean isAppendExtrusionCrossSection() {
        return appendExtrusionCrossSection;
    }

    /**
     * @param appendExtrusionCrossSection the appendExtrusionCrossSection to set
     */
    public void setAppendExtrusionCrossSection(boolean appendExtrusionCrossSection) {
        this.appendExtrusionCrossSection = appendExtrusionCrossSection;
    }

    protected boolean geoSystemEqualsDefault(String[] geoSystem) {
        if (GEOCOORDINATE_ATTR_GEOSYSTEM_DFLT.length != geoSystem.length) return false;
        for (String g : GEOCOORDINATE_ATTR_GEOSYSTEM_DFLT) {
            int i;
            for (i = 0; i < geoSystem.length; i++) {
                if (geoSystem[i].equals(g)) {
                    break;
                }
            }
            if (i >= geoSystem.length) return false;
        }
        return true;
    }
}
