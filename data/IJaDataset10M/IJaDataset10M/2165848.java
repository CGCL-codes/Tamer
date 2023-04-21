package pt.iscte.dsi.taa.policies.accessibility.classes;

import pt.iscte.dsi.taa.qualifiers.AccessibleFrom;

public class Bank {

    @AccessibleFrom({ Mortgage.class })
    public boolean hasSufficientSavings(Customer customer, int amount) {
        System.out.println("Check bank for " + customer.getName());
        return amount > minimum_amount_for_mortgage;
    }

    @AccessibleFrom({ Mortgage.class })
    public final Integer minimum_amount_for_mortgage = 5000;
}
