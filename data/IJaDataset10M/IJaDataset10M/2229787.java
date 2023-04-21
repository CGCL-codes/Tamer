package com.google.gdata.data.finance;

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.extensions.Money;
import java.util.List;

/**
 * Today's gain for the portfolio or position.
 *
 * 
 */
@ExtensionDescription.Default(nsAlias = FinanceNamespace.GF_ALIAS, nsUri = FinanceNamespace.GF, localName = DaysGain.XML_NAME)
public class DaysGain extends ExtensionPoint {

    /** XML element name */
    static final String XML_NAME = "daysGain";

    /**
   * Default mutable constructor.
   */
    public DaysGain() {
        super();
    }

    @Override
    public void declareExtensions(ExtensionProfile extProfile) {
        if (extProfile.isDeclared(DaysGain.class)) {
            return;
        }
        extProfile.declare(DaysGain.class, Money.getDefaultDescription(true, true));
    }

    /**
   * Returns the monetary value of day's gain.
   *
   * @return monetary value of day's gain
   */
    public List<Money> getMoney() {
        return getRepeatingExtension(Money.class);
    }

    /**
   * Adds a new monetary value of day's gain.
   *
   * @param money monetary value of day's gain
   */
    public void addMoney(Money money) {
        getMoney().add(money);
    }

    /**
   * Returns whether it has the monetary value of day's gain.
   *
   * @return whether it has the monetary value of day's gain
   */
    public boolean hasMoney() {
        return hasRepeatingExtension(Money.class);
    }

    @Override
    protected void validate() {
    }

    /**
   * Returns the extension description, specifying whether it is required, and
   * whether it is repeatable.
   *
   * @param required   whether it is required
   * @param repeatable whether it is repeatable
   * @return extension description
   */
    public static ExtensionDescription getDefaultDescription(boolean required, boolean repeatable) {
        ExtensionDescription desc = ExtensionDescription.getDefaultDescription(DaysGain.class);
        desc.setRequired(required);
        desc.setRepeatable(repeatable);
        return desc;
    }

    @Override
    public String toString() {
        return "{DaysGain}";
    }
}
