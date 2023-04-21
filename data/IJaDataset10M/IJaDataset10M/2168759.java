package jimagick.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import jimagick.gui.imageframe.ImageFrame;
import org.apache.log4j.Logger;

/**
 * The listener interface for receiving tagLayerButtonAction events.
 * The class that is interested in processing a tagLayerButtonAction
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addTagLayerButtonActionListener<code> method. When
 * the tagLayerButtonAction event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see TagLayerButtonActionEvent
 */
public class TagLayerButtonActionListener implements ActionListener {

    /** Static logger for this class. */
    private static final Logger logger = Logger.getLogger(TagLayerButtonActionListener.class);

    /**
	 * Makes the tag panel visible when the tag layer button is toggled.
	 */
    @Override
    public void actionPerformed(ActionEvent e) {
        AbstractButton abstractButton = (AbstractButton) e.getSource();
        boolean selected = abstractButton.getModel().isSelected();
        logger.debug("TagLayer::Action - selected=" + selected);
        ImageFrame parent = (ImageFrame) abstractButton.getTopLevelAncestor();
        parent.getTagPanel().setVisible(selected);
        parent.getTagPanel().validate();
    }
}
