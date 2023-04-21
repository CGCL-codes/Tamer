package uk.co.ordnancesurvey.rabbitgui.rbtpatterncategory.itemlist;

import uk.ac.leeds.comp.ui.factory.UIModelFactory;
import uk.ac.leeds.comp.ui.itemlist.impl.ItemListModelImpl;
import uk.co.ordnancesurvey.rabbitgui.RbtTextKey;
import uk.co.ordnancesurvey.rabbitgui.rbtpatterncategory.item.RbtPatternCategoryItemModel;
import com.google.inject.Inject;

/**
 * Implements a UIModel for a list of {@link RbtPatternCategoryItemModel}s
 * 
 * @author rdenaux
 * 
 */
public abstract class RbtPatternCategoryListModelImpl extends ItemListModelImpl<RbtPatternCategoryItemModel> implements RbtPatternCategoryListModel {

    private static final long serialVersionUID = 6530139299892164488L;

    private final UIModelFactory modelFactory;

    @Inject
    public RbtPatternCategoryListModelImpl(UIModelFactory aModelFactory) {
        modelFactory = aModelFactory;
    }

    @Override
    protected RbtPatternCategoryItemModel createItemModel() {
        return modelFactory.createModel(RbtPatternCategoryItemModel.class);
    }

    @Override
    public String getShortListName() {
        return RbtTextKey.RABBIT_PATTERNS.getText();
    }
}
