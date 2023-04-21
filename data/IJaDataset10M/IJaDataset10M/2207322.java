package org.makagiga.commons.color;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.Icon;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import org.makagiga.commons.UI;
import org.makagiga.commons.swing.MPanel;

/**
 * @since 3.8, 4.0 (org.makagiga.commons.color package)
 */
public abstract class MColorChooserPanel extends AbstractColorChooserPanel {

    private final String displayName;

    public MColorChooserPanel(final String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public Icon getLargeDisplayIcon() {
        return null;
    }

    @Override
    public Icon getSmallDisplayIcon() {
        return null;
    }

    public void setSelectedColor(final Color value) {
        getColorSelectionModel().setSelectedColor(value);
    }

    /**
	 * Overriden to set empty border (of size {@link MPanel#DEFAULT_CONTENT_MARGIN})
	 * and border layout with gap of size {@code 5}.
	 */
    @Override
    protected void buildChooser() {
        setBorder(UI.createEmptyBorder(MPanel.DEFAULT_CONTENT_MARGIN));
        setLayout(new BorderLayout(5, 5));
    }
}
