package org.epoline.phoenix.manager.shared;

/**
 * Creation date: (5/17/01 8:54:16 AM)
 *
 */
public class ItemOverlayInfo extends ItemOverlay {

    /**
	 * ItemOverlayInfo constructor comment.
	 */
    public ItemOverlayInfo() {
        super();
    }

    public ItemOverlayInfo(int pageNumber, org.epoline.phoenix.manager.shared.ItemOverlay.ItemPageType pageType, int xValue, int yValue, ItemOverlayLayout layout, String docCode, String formName, org.epoline.phoenix.manager.shared.ItemForm.ItemLanguageType formLanguage) {
        super(pageNumber, pageType, xValue, yValue, layout, docCode, formName, formLanguage);
    }
}
