package octopus.tools.Objects;

/**
* Object which handles everything that can be in a Combobox
*/
public class ObjectComboBox {

    private String _id = "";

    private String _label = "";

    /**
    * Simple Constructor
    */
    public ObjectComboBox() {
    }

    /**
    * Constructor
    * Of one Element in a Combobox
    */
    public ObjectComboBox(String _label, String _id) {
        this._id = _id;
        this._label = _label;
    }

    /**
    * Get the Id of the Element
    */
    public String getId() {
        return _id;
    }

    /**
    * Get the Label of the Element
    */
    public String getLabel() {
        return _label;
    }

    /**
    * Set the Id of the Element
    */
    public void setId(String id) {
        this._id = id;
    }

    /**
    * Set the Label of the Element
    */
    public void setLabel(String theLabel) {
        this._label = theLabel;
    }
}
