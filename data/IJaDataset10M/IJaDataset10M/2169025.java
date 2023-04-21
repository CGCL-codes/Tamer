package gui.users.listing;

import gui.listing.ListingView;

public class ListingUserView extends ListingView {

    static String frameName = "Users Listing View";

    public ListingUserView(ListingUserModel model) {
        this(model, true);
    }

    public ListingUserView(ListingUserModel model, boolean withFrame) {
        super(model, frameName, withFrame);
        this.model = model;
        initComboBox();
    }

    @Override
    protected void initComboBox() {
        String columnName = "roleName";
        initComboBox(columnName, ((ListingUserModel) model).getRoles());
    }
}
