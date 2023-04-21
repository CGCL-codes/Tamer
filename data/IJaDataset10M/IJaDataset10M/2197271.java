package org.mitre.bio.phylo.dom.gui.event;

import java.io.Serializable;
import java.util.EventObject;
import org.mitre.bio.phylo.dom.gui.Tree2DPanelPreferences;

/**
 * Event Object for {@link Tree2DPanelPreference change events.
 *
 * @author Marc Colosimo
 * @copyright The MITRE Corporation 2007
 *
 * @version 1.0
 */
public class Tree2DPanelPreferenceChangeEvent extends EventObject implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PAINT_MODIFIED = "paint_modified";

    public static final String FONT_MODIFIED = "font_modified";

    public static final String STROKE_MODIFIED = "stroke_modified";

    public static final String SELECTION_COLOR_MODIFIED = "selection_color_modified";

    private String fEventType;

    private Tree2DPanelPreferences fPreferences;

    /**
         * Creates a new event generated from the given <code>Tree2DPanelPreferences</code>
         */
    public Tree2DPanelPreferenceChangeEvent(Tree2DPanelPreferences preferences, String eventType) {
        super(preferences);
        this.fPreferences = preferences;
        this.fEventType = eventType;
    }

    public String getEventType() {
        return this.fEventType;
    }

    public Tree2DPanelPreferences getPreferences() {
        return this.fPreferences;
    }
}
