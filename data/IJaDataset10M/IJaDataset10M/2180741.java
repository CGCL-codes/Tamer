package org.openoffice.embed.oochem;

/**
 *
 * @author ariel
 */
public interface JmolConstants {

    /**
     *
     */
    String JMOL_EXTENSION_IDENTIFIER = "org.openoffice.embed.oochem.JmolEmbeddedObject";

    /**
     *
     */
    String JMOL_EMBEDDED_OBJECT_CLSID = "37D0956C-0DD6-4E6B-A6C5-33EB7D3E319A";

    /**
     *
     */
    String JMOL_EMBEDDED_OBJECT_SERVICE_NAME = "org.openoffice.embed.oochem.Factory" + "37D0956C0DD64E6BA6C533EB7D3E319A";

    /**
      *
      */
    String JMOL_WINDOW_STATE = "windowState";

    /**
      *
      */
    String JMOL_VARIABLE_STATE = "variableState";

    /**
     *
     */
    String JMOL_MODEL_STATE = "modelState";

    /**
     *
     */
    String JMOL_PERSPECTIVE_STATE = "perspectiveState";

    /**
     *
     */
    String JMOL_SELECTION_STATE = "selectionState";

    /**
     *
     */
    String JMOL_PROPERTY_PREFIX = "jmol.";

    /**
     *
     */
    String JMOL_MODEL_PROPERTY = JMOL_PROPERTY_PREFIX + "model";

    /**
     *
     */
    String JMOL_WINDOW_STATE_PROPERTY = JMOL_PROPERTY_PREFIX + JMOL_WINDOW_STATE;

    /**
     *
     */
    String JMOL_VARIABLE_STATE_PROPERTY = JMOL_PROPERTY_PREFIX + JMOL_VARIABLE_STATE;

    /**
     *
     */
    String JMOL_MODEL_STATE_PROPERTY = JMOL_PROPERTY_PREFIX + JMOL_MODEL_STATE;

    /**
     *
     */
    String JMOL_PERSPECTIVE_STATE_PROPERTY = JMOL_PROPERTY_PREFIX + JMOL_PERSPECTIVE_STATE;

    /**
     *
     */
    String JMOL_SELECTION_STATE_PROPERTY = JMOL_PROPERTY_PREFIX + JMOL_SELECTION_STATE;

    /**
     *
     */
    String JMOL_WINDOW_WIDTH_PROPERTY = "window.width";

    /**
     *
     */
    String JMOL_WINDOW_HEIGHT_PROPERTY = "window.height";

    /**
     *
     */
    String JMOL_WINDOW_X_PROPERTY = "window.x";

    /**
     *
     */
    String JMOL_WINDOW_Y_PROPERTY = "window.y";

    /**
     *
     */
    String JMOL_PROPERTIES_FILE = "jmol.properties";

    String STREAM_ELEMENT_CML_FILE = "content.cml";

    /**
     *
     */
    String STREAM_ELEMENT_JMOL_PROPERTIES_FILE = "jmol.properties";

    /**
     *
     */
    String JMOL_DEFAULT_IMAGE_FILE = "default.png";
}
