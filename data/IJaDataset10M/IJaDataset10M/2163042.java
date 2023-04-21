package org.broadleafcommerce.core.pricing.service.workflow;

import java.math.BigDecimal;
import org.broadleafcommerce.core.order.domain.FulfillmentGroup;
import org.broadleafcommerce.core.order.domain.FulfillmentGroupFee;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.workflow.BaseActivity;
import org.broadleafcommerce.core.workflow.ProcessContext;
import org.broadleafcommerce.money.Money;

public class TotalActivity extends BaseActivity {

    public ProcessContext execute(ProcessContext context) throws Exception {
        Order order = ((PricingContext) context).getSeedData();
        Money total = new Money(BigDecimal.ZERO);
        total = total.add(order.getSubTotal());
        total = total.subtract(order.getOrderAdjustmentsValue());
        total = total.add(order.getTotalTax());
        total = total.add(order.getTotalShipping());
        Money fees = new Money(BigDecimal.ZERO);
        for (FulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
            Money fgTotal = new Money(0D);
            fgTotal = fgTotal.add(fulfillmentGroup.getMerchandiseTotal());
            fgTotal = fgTotal.add(fulfillmentGroup.getShippingPrice());
            fgTotal = fgTotal.add(fulfillmentGroup.getTotalTax());
            for (FulfillmentGroupFee fulfillmentGroupFee : fulfillmentGroup.getFulfillmentGroupFees()) {
                fgTotal = fgTotal.add(fulfillmentGroupFee.getAmount());
                fees = fees.add(fulfillmentGroupFee.getAmount());
            }
            fulfillmentGroup.setTotal(fgTotal);
        }
        total = total.add(fees);
        order.setTotal(total);
        context.setSeedData(order);
        return context;
    }
}
