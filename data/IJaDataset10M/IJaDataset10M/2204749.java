package com.bkoenig.photo.toolkit.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import com.bkoenig.photo.toolkit.utils.Config;
import com.bkoenig.photo.toolkit.utils.Logger;

public class TabPhotoViewer {

    public TabPhotoViewer(TabFolder parent) {
        Logger.debug("creating TabPhotoViewer.");
        TabItem item = new TabItem(parent, SWT.NONE);
        item.setText(Config.getText("photoviever_title"));
        item.setToolTipText(Config.getText("photoviever_desc"));
        item.setImage(new Image(parent.getDisplay(), Config.APP_TAB_PHOTO));
        Composite tab = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.makeColumnsEqualWidth = false;
        tab.setLayout(gridLayout);
        item.setControl(tab);
    }
}
