package com.bluebrim.text.impl.client;

import java.awt.*;
import javax.swing.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.browser.client.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.text.impl.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2000-08-17 11:40:30)
 * @author: Dennis
 */
public class CoTagChainCollectionUI extends CoAbstractCatalogUI {

    private class AvailableTagsListModel extends AbstractListModel {

        public int getSize() {
            com.bluebrim.text.shared.CoStyledTextPreferencesIF cp = (com.bluebrim.text.shared.CoStyledTextPreferencesIF) getDomain();
            return (cp == null) ? 0 : cp.getParagraphTagNames().size();
        }

        public Object getElementAt(int i) {
            com.bluebrim.text.shared.CoStyledTextPreferencesIF cp = (com.bluebrim.text.shared.CoStyledTextPreferencesIF) getDomain();
            return (cp == null) ? null : cp.getParagraphTagNames().get(i);
        }

        public void touch() {
            fireContentsChanged(this, 0, getSize() - 1);
        }
    }

    ;

    private AvailableTagsListModel m_availableTags = new AvailableTagsListModel();

    private int m_startOfMutableChains;

    /**
 * CoTagChainCollectionUI constructor comment.
 */
    public CoTagChainCollectionUI() {
        super();
    }

    protected CoListCatalogEditor createCatalogEditor() {
        return new CoGsListCatalogEditor(this) {

            public void enableDisableMenuItems() {
                super.enableDisableMenuItems();
                if (m_removeElementAction != null && m_removeElementAction.isEnabled()) m_removeElementAction.setEnabled(getCatalogList().getSelectedIndex() >= m_startOfMutableChains);
            }
        };
    }

    /**
 * createCatalogElementUI method comment.
 */
    protected com.bluebrim.gui.client.CoUserInterface createCatalogElementUI() {
        CoTagChainUI ui = new CoTagChainUI() {

            public boolean canBeEnabled() {
                return super.canBeEnabled() && (getCatalogList().getSelectedIndex() >= m_startOfMutableChains);
            }
        };
        ui.setAvailableTags(m_availableTags);
        return ui;
    }

    protected CoListValueable.Mutable createCatalogHolder() {
        return new CoAbstractListAspectAdaptor.Mutable(this, "ELEMENTS") {

            protected Object get(CoObjectIF subject) {
                return ((CoTagChainCollectionIF) subject).getChains();
            }

            public CoAbstractListModel.Mutable createListModel() {
                return new CoCollectionListModel.Mutable(this) {

                    protected boolean doRemoveElement(Object element) {
                        ((CoTagChainCollectionIF) getDomain()).removeChain((com.bluebrim.text.shared.CoTagChainIF) element);
                        return true;
                    }

                    protected void doAddElement(Object element) {
                        ((CoTagChainCollectionIF) getDomain()).addChain((com.bluebrim.text.shared.CoTagChainIF) element);
                    }
                };
            }
        };
    }

    protected ListCellRenderer createCatalogListCellRenderer(CoUserInterfaceBuilder builder) {
        return new CoImmutableAsItalicCatalogListCellRenderer() {

            protected boolean isMutable(Object value, int index) {
                return index >= m_startOfMutableChains;
            }
        };
    }

    protected Object getCatalogSubcanvasLayoutConstraint() {
        return BorderLayout.CENTER;
    }

    private CoTagChainCollectionIF getTagChainCollection() {
        return (CoTagChainCollectionIF) getDomain();
    }

    /**
 * newCatalogElement method comment.
 */
    protected com.bluebrim.browser.shared.CoCatalogElementIF newCatalogElement() {
        return ((CoTagChainCollectionIF) getDomain()).createChain(com.bluebrim.base.shared.CoStringResources.getName(com.bluebrim.base.shared.CoConstants.UNTITLED));
    }

    protected void postDomainChange(CoObjectIF d) {
        super.postDomainChange(d);
        updateAvailableTags();
        CoTagChainCollectionIF c = getTagChainCollection();
        m_startOfMutableChains = (c == null) ? 0 : c.getImmutableChainCount();
    }

    public void updateAvailableTags() {
        m_availableTags.touch();
    }

    public void valueHasChanged() {
        super.valueHasChanged();
        updateAvailableTags();
        CoTagChainCollectionIF c = getTagChainCollection();
        m_startOfMutableChains = (c == null) ? 0 : c.getImmutableChainCount();
    }
}
