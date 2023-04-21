package pl.pyrkon.cm.client.discount.common;

import java.util.List;
import pl.pyrkon.cm.client.discount.data.Discount;
import pl.pyrkon.cm.client.discount.data.DiscountGrant;
import pl.pyrkon.cm.client.person.data.Participant;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DiscountServiceAsync {

    public abstract void listAvailableDiscounts(AsyncCallback<List<Discount>> callback);

    public abstract void grantDiscount(Participant person, Discount discount, String reason, AsyncCallback<DiscountGrant> callback);
}
