package jokeboxjunior.core.model.common.wishlist;

import cb_commonobjects.datastore.AttributeTypeEnum;
import java.util.ArrayList;
import jokeboxjunior.core.controller.AbstractController;
import jokeboxjunior.core.controller.MainController;
import jokeboxjunior.core.model.AbstractModelObject;

/**
 *
 * @author B1
 */
public class Wishlist extends AbstractModelObject {

    public static final String ATTRIB_NAME = "name";

    public Wishlist() {
        super("wishlists");
    }

    @Override
    protected void _addAttribs() {
        _addAttrib(ATTRIB_NAME, AttributeTypeEnum.ATT_TYPE_STRING, 100);
    }

    public String getName() {
        return this.getAttribString(ATTRIB_NAME);
    }

    public void setName(String Name) {
        this.setAttrib(ATTRIB_NAME, Name);
    }

    protected ArrayList<WishlistItem> Items = new ArrayList<WishlistItem>(0);

    public ArrayList<WishlistItem> getItems() {
        return Items;
    }

    public void setItemsCast(ArrayList<AbstractModelObject> thisItems) {
        this.Items = new ArrayList<WishlistItem>(thisItems.size());
        for (AbstractModelObject myObj : thisItems) {
            this.Items.add((WishlistItem) myObj);
        }
    }

    public void setItems(ArrayList<WishlistItem> thisItems) {
        this.Items = thisItems;
    }

    @Override
    public AbstractController getController() {
        return MainController.WishlistController;
    }
}
