package com.thyante.thelibrarian.dialogs;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import com.thyante.thelibrarian.action.DataEntryAddFileAction;
import com.thyante.thelibrarian.action.DataEntryRemoveFileAction;
import com.thyante.thelibrarian.components.IMediumGallery;
import com.thyante.thelibrarian.components.IMediumGalleryListener;
import com.thyante.thelibrarian.components.MediaGalleryGroup;
import com.thyante.thelibrarian.components.MediaGalleryItem;
import com.thyante.thelibrarian.model.specification.IItem;
import com.thyante.thelibrarian.model.specification.IMedium;
import com.thyante.thelibrarian.util.FileStorageUtil;
import com.thyante.thelibrarian.view.FormMediaGallery;

/**
 * The &quot;Media&quot; tab of the item data entry dialog.
 * @author Matthias-M. Christen
 */
public class DataEntryMediaTab implements IMediumGalleryListener {

    /**
	 * The currently active item
	 */
    protected IItem m_item;

    /**
	 * The actions allowing the user to add/remove items to/from the gallery
	 */
    protected IAction[] m_rgActions;

    /**
	 * The gallery object
	 */
    protected FormMediaGallery m_gallery;

    /**
	 * Flag indicating whether the gallery has changed
	 */
    protected boolean m_bIsDirty;

    /**
	 * Constructs the Media Files tab.
	 */
    public DataEntryMediaTab(IItem item) {
        m_item = item;
        m_gallery = new FormMediaGallery();
        m_rgActions = new IAction[] { new DataEntryAddFileAction(m_gallery), new DataEntryRemoveFileAction(m_gallery) };
        m_bIsDirty = false;
    }

    public void dispose() {
        m_gallery.dispose();
    }

    /**
	 * Creates the media tab user interface.
	 * @param cmpParent The parent composite
	 * @return The UI component
	 */
    public Composite createUI(Composite cmpParent) {
        Composite cmpPane = new Composite(cmpParent, SWT.NONE);
        GridLayout layout = new GridLayout(1, true);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        cmpPane.setLayout(layout);
        createToolbar(cmpPane);
        createGallery(cmpPane);
        if (m_item != null) {
            for (IMedium m : m_item.getMedia()) {
                MediaGalleryItem item = new MediaGalleryItem(m_gallery.getGroupForType(m.getType()), SWT.NONE);
                {
                    item.setImage(m.getImage(item.getDisplay(), IMedium.MediumImageType.TYPE_DISPLAY_IMAGE));
                    item.setText(FileStorageUtil.stripPath(m.getFilename()));
                }
                item.setData(m);
            }
        }
        return cmpPane;
    }

    /**
	 * Creates the toolbar.
	 */
    protected void createToolbar(final Composite cmpParent) {
        CoolBarManager mgrCoolbar = new CoolBarManager();
        ToolBarManager mgrToolbar = new ToolBarManager(SWT.FLAT);
        mgrCoolbar.add(mgrToolbar);
        for (IAction a : m_rgActions) mgrToolbar.add(a);
        CoolBar coolbar = mgrCoolbar.createControl(cmpParent);
        coolbar.setBackground(cmpParent.getBackground());
        coolbar.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        coolbar.addListener(SWT.Resize, new Listener() {

            public void handleEvent(Event event) {
                cmpParent.getShell().layout();
            }
        });
    }

    /**
	 * Creates the gallery
	 * @param cmpParent
	 */
    protected void createGallery(Composite cmpParent) {
        FormToolkit toolkit = new FormToolkit(cmpParent.getDisplay());
        ScrolledForm form = toolkit.createScrolledForm(cmpParent);
        form.getBody().setLayout(new GridLayout(1, true));
        form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        m_gallery.createGallery(form.getBody(), form, toolkit);
        for (MediaGalleryGroup g : m_gallery.getGroups()) g.addMediumGalleryListener(this);
    }

    /**
	 * Returns all the media file items in the gallery.
	 * @return A list of items
	 */
    public List<MediaGalleryItem> getItems() {
        List<MediaGalleryItem> list = new LinkedList<MediaGalleryItem>();
        for (MediaGalleryGroup group : m_gallery.getGroups()) for (MediaGalleryItem item : group.getItems()) list.add(item);
        return list;
    }

    public FormMediaGallery getGallery() {
        return m_gallery;
    }

    public boolean haveMediaChanged() {
        return m_bIsDirty;
    }

    public void onItemAdded(IMediumGallery gallery, MediaGalleryGroup group, MediaGalleryItem item) {
        m_bIsDirty = true;
    }

    public void onItemRemoved(IMediumGallery gallery, MediaGalleryGroup Group, MediaGalleryItem item) {
        m_bIsDirty = true;
    }
}
