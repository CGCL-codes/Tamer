package org.xmlvm.ios;

import java.util.*;
import org.xmlvm.XMLVMSkeletonOnly;

@XMLVMSkeletonOnly
public class SKProductsRequest extends SKRequest {

    /**
	 * - (id)initWithProductIdentifiers:(NSSet *)productIdentifiers ;
	 */
    public SKProductsRequest(Set productIdentifiers) {
    }

    /** Default constructor */
    SKProductsRequest() {
    }

    /**
	 * @property(nonatomic, assign) id <SKProductsRequestDelegate> delegate ;
	 */
    public SKProductsRequestDelegate getRequestDelegate() {
        throw new RuntimeException("Stub");
    }

    /**
	 * @property(nonatomic, assign) id <SKProductsRequestDelegate> delegate ;
	 */
    public void setDelegate(SKProductsRequestDelegate delegate) {
        throw new RuntimeException("Stub");
    }
}
