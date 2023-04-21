package com.orm.orm.availabilityProcessor;

import com.orm.Structure.Entity.Foundation.Amount;
import com.orm.inventory.IResource;

public interface IAvailabilityControl {

    boolean isValid();

    Amount getBidPrice();
}
