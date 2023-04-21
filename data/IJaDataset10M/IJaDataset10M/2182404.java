package de.flingelli.scrum.gui.backlogitem;

import guihelper.document.AUpdateDocumentListener;
import java.beans.PropertyChangeListener;
import de.flingelli.scrum.datastructure.BacklogItem;
import de.flingelli.scrum.datastructure.Product;
import de.flingelli.scrum.language.JastTranslation;
import javax.swing.event.DocumentListener;

@SuppressWarnings("serial")
public class BacklogItemRequirementsPanel extends ABacklogItemPanel implements PropertyChangeListener {

    public BacklogItemRequirementsPanel(Product product, BacklogItem backlogItem) {
        super(product, backlogItem, "backlogitem_requirments_textpane");
    }

    @Override
    protected void changeLanguage() {
        mLabel.setText(JastTranslation.getInstance().getValue("de.flingelli.scrum.gui.backlogitem.BacklogItemRequirementsPanel-Requirements"));
    }

    @Override
    protected void displayData() {
        mTextPane.getDocument().removeDocumentListener(mDocumentListener);
        mTextPane.setText(mBacklogItem.getRequirement());
        mTextPane.getDocument().addDocumentListener(mDocumentListener);
    }

    @Override
    protected DocumentListener createDocumentListener() {
        DocumentListener documentListener = new AUpdateDocumentListener() {

            @Override
            public void updateData() {
                mProductPropertyChangeSupport.setBacklogItemRequirement(mBacklogItem, mTextPane.getText());
            }
        };
        return documentListener;
    }
}
