package fr.itris.glips.rtdaeditor.anim.widgets;

import javax.swing.*;
import fr.itris.glips.library.widgets.DashChooserWidget;
import fr.itris.glips.rtdaeditor.anim.*;
import java.awt.event.*;

/**
 * the class of the dash chooser
 * @author ITRIS, Jordi SUC
 */
public class DashChooser extends Widget {

    /**
	 * the dash chooser
	 */
    private DashChooserWidget dashChooserWidget = new DashChooserWidget();

    /**
	 * the listener
	 */
    private ActionListener listener = null;

    /**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 */
    protected DashChooser(boolean isEditor) {
        super(isEditor);
        buildWidget();
    }

    /**
	 * builds the widget
	 */
    protected void buildWidget() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(dashChooserWidget);
        if (isEditor) {
            listener = new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    if (getItem() != null) {
                        getItem().setValue(dashChooserWidget.getValue());
                    }
                    if (validateRunnable != null) {
                        validateRunnable.run();
                    }
                }
            };
        }
    }

    @Override
    protected void setItem(EditableItem item, Runnable validateRunnable) {
        super.setItem(item, validateRunnable);
        if (isEditor) {
            dashChooserWidget.removeListener(listener);
        }
        dashChooserWidget.init(getItem().getValue());
        if (isEditor) {
            dashChooserWidget.addListener(listener);
        }
    }
}
