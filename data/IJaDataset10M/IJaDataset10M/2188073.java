package org.gwtoolbox.widget.client.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * @author Uri Boness
 */
public interface DataGridImages extends ImageBundle {

    @Resource("org/gwtoolbox/widget/client/images/button-page-first.gif")
    AbstractImagePrototype button_pageFirst();

    @Resource("org/gwtoolbox/widget/client/images/button-page-first-disabled.gif")
    AbstractImagePrototype button_pageFirstDisabled();

    @Resource("org/gwtoolbox/widget/client/images/button-page-last.gif")
    AbstractImagePrototype button_pageLast();

    @Resource("org/gwtoolbox/widget/client/images/button-page-last-disabled.gif")
    AbstractImagePrototype button_pageLastDisabled();

    @Resource("org/gwtoolbox/widget/client/images/button-page-next.gif")
    AbstractImagePrototype button_pageNext();

    @Resource("org/gwtoolbox/widget/client/images/button-page-next-disabled.gif")
    AbstractImagePrototype button_pageNextDisabled();

    @Resource("org/gwtoolbox/widget/client/images/button-page-prev.gif")
    AbstractImagePrototype button_pagePrev();

    @Resource("org/gwtoolbox/widget/client/images/button-page-prev-disabled.gif")
    AbstractImagePrototype button_pagePrevDisabled();

    @Resource("org/gwtoolbox/widget/client/images/button-refresh.gif")
    AbstractImagePrototype button_refresh();

    public class Instance {

        private static DataGridImages images;

        public static synchronized DataGridImages get() {
            if (images == null) {
                images = (DataGridImages) GWT.create(DataGridImages.class);
            }
            return images;
        }
    }
}
