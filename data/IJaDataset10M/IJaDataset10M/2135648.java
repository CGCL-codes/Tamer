package com.softwaresmithy.test;

import android.test.ActivityInstrumentationTestCase2;
import com.softwaresmithy.Wishlist;

public class WishlistTest extends ActivityInstrumentationTestCase2<Wishlist> {

    private Wishlist mWishlist;

    public WishlistTest() {
        super("com.softwaresmithy", Wishlist.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mWishlist = getActivity();
    }

    public void testWishlist() {
    }
}
