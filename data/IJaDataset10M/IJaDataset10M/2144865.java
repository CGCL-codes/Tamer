package com.gmvc.client.app;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.gmvc.client.base.Popupable;
import com.gmvc.client.meta.Right;
import com.gmvc.client.util.Tags;
import com.google.gwt.resources.client.ImageResource;

/**
 * Ana menude gosterilecek hosgeldiniz sayfasini icerir
 * 
 * @see BaseAppModel
 *
 * @author mdpinar
 * 
 */
class WellcomePage implements Popupable {

    private ContentPanel cp;

    WellcomePage(ContentPanel cp) {
        this.cp = cp;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int getHeight() {
        return 0;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public ImageResource getIcon() {
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Right getRight() {
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String getTitle() {
        return Tags.get("wellcome");
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int getWidth() {
        return 0;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public ContentPanel popup() {
        return cp;
    }
}
