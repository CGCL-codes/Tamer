package net.rptools.chartool.ui.charsheet.component;

import javax.swing.JTextArea;
import net.rptools.chartool.ui.charsheet.CharSheetController;

/**
 * Extension of the label that can have it's contents set based on the value of its property
 * 
 * @author Jay
 */
public class CharSheetTextArea extends JTextArea implements CharSheetComponent {

    /**
   * The string holding the on load script. Needed for Abeille's serialization crap.
   */
    private String onLoadScript;

    /**
   * The script that is called when the component is first created.
   */
    private CharSheetComponentScript onLoadScriptInstance = new CharSheetComponentScript();

    /**
   * The string holding the action script. Needed for Abeille's serialization crap.
   */
    private String onSetValueScript;

    /**
   * The script called by delegates when the value is set.
   */
    private CharSheetComponentScript onSetValueScriptInstance = new CharSheetComponentScript();

    /**
   * The name of a property that contains the name of the parent map of the property to be displayed. 
   */
    private String indirectionProperty;

    /**
   * The override data type for showing a specific type of data.
   */
    private String overrideDataType;

    /**
   * @return Getter for onLoadScript
   */
    public String getOnLoadScript() {
        return onLoadScript;
    }

    /**
   * @param aOnLoadScript Setter for the onLoadScript to set
   */
    public void setOnLoadScript(String aOnLoadScript) {
        onLoadScript = aOnLoadScript;
        onLoadScriptInstance.setScriptValue(aOnLoadScript);
    }

    /**
   * @return Getter for onSetValueScript
   */
    public String getOnSetValueScript() {
        return onSetValueScript;
    }

    /**
   * @param aOnSetValueScript Setter for the onSetValueScript to set
   */
    public void setOnSetValueScript(String aOnSetValueScript) {
        onSetValueScript = aOnSetValueScript;
        onSetValueScriptInstance.setScriptValue(aOnSetValueScript);
    }

    /** @param aIndirectionProperty Setter for the indirectionProperty to set */
    public void setIndirectionProperty(String aIndirectionProperty) {
        indirectionProperty = aIndirectionProperty;
    }

    /** @param aOverrideDataType Setter for the overrideDataType to set */
    public void setOverrideDataType(String aOverrideDataType) {
        overrideDataType = aOverrideDataType;
    }

    /**
   * @see net.rptools.chartool.ui.charsheet.component.CharSheetComponent#getCharSheetController()
   */
    public CharSheetController getCharSheetController() {
        return (CharSheetController) getClientProperty(CharSheetController.class.getName());
    }

    /**
   * @see net.rptools.chartool.ui.charsheet.component.CharSheetComponent#executeOnLoadScript()
   */
    public boolean executeOnLoadScript() {
        return onLoadScriptInstance.executeEventScript(this, null, null, null, false, true);
    }

    /**
   * @see net.rptools.chartool.ui.charsheet.component.CharSheetComponent#executeOnSetValueScript(Object)
   */
    public boolean executeOnSetValueScript(Object value) {
        return onSetValueScriptInstance.executeEventScript(this, null, value, null, false, true);
    }

    /**
   * @see net.rptools.chartool.ui.charsheet.component.CharSheetComponent#getOverrideDataType()
   */
    public String getOverrideDataType() {
        return overrideDataType;
    }

    /**
   * @see net.rptools.chartool.ui.charsheet.component.CharSheetComponent#getIndirectionProperty()
   */
    public String getIndirectionProperty() {
        return indirectionProperty;
    }
}
