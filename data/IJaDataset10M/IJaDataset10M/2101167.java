package com.bluebrim.paint.impl.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;

public class CoSpotColorUI extends com.bluebrim.paint.impl.client.CoColorChooserUI {

    public static final String SPOT_COLOR = "spot_color";

    /**
 * CoSpotColorUI constructor comment.
 */
    public CoSpotColorUI() {
        super();
    }

    /**
 * CoSpotColorUI constructor comment.
 * @param aDomainObject com.bluebrim.base.shared.CoObjectIF
 */
    public CoSpotColorUI(CoObjectIF aDomainObject) {
        super(aDomainObject);
    }

    protected CoLabel createLabel(CoUserInterfaceBuilder aBuilder) {
        CoLabel label = aBuilder.createLabel(com.bluebrim.paint.impl.client.CoColorUIResources.getName(SPOT_COLOR), this.colorPreviewIcon, JLabel.CENTER, PREVIEW);
        label.setFont(CoUIConstants.GARAMOND_18_LIGHT);
        label.setForeground(Color.black);
        return label;
    }

    protected void createListeners() {
        super.createListeners();
        ((CoTextField) getNamedWidget(CoConstants.NAME)).addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                colorEdited();
            }
        });
        ((CoTextField) getNamedWidget(CoConstants.NAME)).addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent e) {
                colorEdited();
            }
        });
    }

    protected void updateColorProxy() {
        super.updateColorProxy();
        ((com.bluebrim.paint.impl.shared.CoSpotColorIF) colorProxy).setName(((CoTextField) getNamedWidget(CoConstants.NAME)).getText());
    }

    protected void updateWidgets() {
        super.updateWidgets();
        if (getDomain() != null) {
            ((CoTextField) getNamedWidget(CoConstants.NAME)).setText(((com.bluebrim.paint.impl.shared.CoSpotColorIF) getDomain()).getName());
        }
    }
}
