package org.kablink.teaming.gwt.client.widgets;

import org.kablink.teaming.gwt.client.GwtTeaming;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author jwootton
 *
 */
public class EditDeleteControl extends Composite implements ClickHandler {

    private EditHandler m_editHandler;

    private DeleteHandler m_deleteHandler;

    private Anchor m_editAnchor;

    private Anchor m_deleteAnchor;

    /**
	 * This control has 2 images, an "edit" image and a "delete" image.  When the user clicks
	 * on the "edit" image, the editHandler will be called.  When the user clicks on the "delete"
	 * image, the deleteHandler will be called.
	 */
    public EditDeleteControl(EditHandler editHandler, DeleteHandler deleteHandler) {
        FlowPanel mainPanel;
        AbstractImagePrototype abstractImg;
        Image img;
        m_editHandler = editHandler;
        m_deleteHandler = deleteHandler;
        mainPanel = new FlowPanel();
        mainPanel.addStyleName("editDeleteControl");
        {
            m_editAnchor = new Anchor();
            m_editAnchor.addClickHandler(this);
            m_editAnchor.addStyleName("editDeleteControlAnchor");
            abstractImg = GwtTeaming.getImageBundle().edit10();
            img = abstractImg.createImage();
            img.addStyleName("margin-right-5");
            m_editAnchor.getElement().appendChild(img.getElement());
            mainPanel.add(m_editAnchor);
        }
        {
            m_deleteAnchor = new Anchor();
            m_deleteAnchor.addClickHandler(this);
            m_deleteAnchor.addStyleName("editDeleteControlAnchor");
            abstractImg = GwtTeaming.getImageBundle().delete10();
            img = abstractImg.createImage();
            m_deleteAnchor.getElement().appendChild(img.getElement());
            mainPanel.add(m_deleteAnchor);
        }
        initWidget(mainPanel);
    }

    public void onClick(ClickEvent event) {
        Object source;
        source = event.getSource();
        if (source == m_editAnchor) {
            if (m_editHandler != null) {
                int x;
                int y;
                x = event.getNativeEvent().getClientX();
                y = event.getNativeEvent().getClientY();
                m_editHandler.onEdit(x, y);
            }
            return;
        }
        if (source == m_deleteAnchor) {
            if (m_deleteHandler != null) {
                m_deleteHandler.onDelete();
            }
        }
    }
}
