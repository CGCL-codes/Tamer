package net.rptools.chartool.ui.charsheet.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JRadioButton;
import net.rptools.chartool.model.property.PropertyNode;
import net.rptools.chartool.ui.charsheet.CharSheetController;

/**
 * Character sheet version of the JRadioButton.
 *
 * @author Jay
 */
public class CharSheetRadioButton extends JRadioButton implements CharSheetComponent {

    /**
   * The string holding the action script. Needed for Abeille's serialization crap.
   */
    private String actionScript;

    /**
   * The script that handles the action event.
   */
    private CharSheetComponentScript actionScriptInstance = new CharSheetComponentScript();

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
   * The string holding the  script. Needed for Abeille's serialization crap.
   */
    private String onChangeScript;

    /**
   * The script called by delegates when the value is set.
   */
    private CharSheetComponentScript onChangeScriptInstance = new CharSheetComponentScript();

    /**
   * The data displayed in the table.
   */
    private PropertyNode data;

    /**
   * The override data type for showing a specific type of data.
   */
    private String overrideDataType;

    /**
   * The name of a property that contains the name of the parent map of the property to be displayed. 
   */
    private String indirectionProperty;

    /**
   * Add listener.
   */
    public CharSheetRadioButton() {
        addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent aE) {
                actionScriptInstance.executeEventScript(CharSheetRadioButton.this, aE, null, null, false, true);
            }
        });
        addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent aE) {
                onChangeScriptInstance.executeEventScript(CharSheetRadioButton.this, aE, null, null, false, true);
            }
        });
    }

    /** @return Getter for actionScript */
    public String getActionScript() {
        return actionScript;
    }

    /** @param aActionScript Setter for actionScript */
    public void setActionScript(String aActionScript) {
        actionScript = aActionScript;
        actionScriptInstance.setScriptValue(aActionScript);
    }

    /** @return Getter for data */
    public PropertyNode getData() {
        return data;
    }

    /** @param aData Setter for data */
    public void setData(PropertyNode aData) {
        data = aData;
    }

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

    /** @return Getter for onChangeScript */
    public String getOnChangeScript() {
        return onChangeScript;
    }

    /** @param aOnChangeScript Setter for the onChangeScript to set */
    public void setOnChangeScript(String aOnChangeScript) {
        onChangeScript = aOnChangeScript;
        onChangeScriptInstance.setScriptValue(aOnChangeScript);
    }

    /** @param aOverrideDataType Setter for the overrideDataType to set */
    public void setOverrideDataType(String aOverrideDataType) {
        overrideDataType = aOverrideDataType;
    }

    /** @param aIndirectionProperty Setter for the indirectionProperty to set */
    public void setIndirectionProperty(String aIndirectionProperty) {
        indirectionProperty = aIndirectionProperty;
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
