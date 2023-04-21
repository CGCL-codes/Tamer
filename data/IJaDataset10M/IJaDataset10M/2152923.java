package org.rapla.mobile.android.test.widget;

import java.security.InvalidParameterException;
import org.rapla.mobile.android.test.test.FixtureHelper;
import org.rapla.mobile.android.widget.RaplaAttributeDate;
import android.test.AndroidTestCase;
import android.view.View;

/**
 * Unit test class for org.rapla.mobile.android.widget.RaplaAttributeDate
 * 
 * @see org.rapla.mobile.android.utility.factory.widget.RaplaAttributeDate
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class RaplaAttributeDateTest extends AndroidTestCase {

    protected RaplaAttributeDate attribute;

    protected void setUp() throws Exception {
        super.setUp();
        this.attribute = new RaplaAttributeDate(this.getContext(), FixtureHelper.createAttributeDate());
    }

    public void testConstructorShouldThrowInvalidParameterExceptionForAttributeOtherThanOfTypeDate() {
        try {
            new RaplaAttributeDate(this.getContext(), FixtureHelper.createAttributeInteger());
            fail();
        } catch (InvalidParameterException e) {
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetListViewItemShouldNotReturnNull() {
        View v = this.attribute.getListItemView();
        assertNotNull(v);
    }
}
