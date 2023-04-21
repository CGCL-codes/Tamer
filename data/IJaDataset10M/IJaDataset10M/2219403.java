package org.amse.audalov.view.actions;

import java.awt.event.*;
import javax.swing.*;
import org.amse.audalov.model.*;
import org.amse.audalov.view.*;

public abstract class AbstractFileGeoAction extends AbstractGeoAction {

    public AbstractFileGeoAction(View view, String caption, String imageName) {
        super(view, caption, imageName);
    }
}
