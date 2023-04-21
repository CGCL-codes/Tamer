package org.peaseplate.domain.conversion;

import org.peaseplate.Conversion;
import org.peaseplate.ConversionInitializer;
import org.peaseplate.service.ConversionService;

public class ToStringConversionInitializer implements ConversionInitializer {

    /**
	 * @see org.peaseplate.ConversionInitializer#initializeConversions(ConversionService)
	 */
    public void initializeConversions(ConversionService service) {
        service.add(Void.class, String.class, new Conversion<Void, String>() {

            public String convert(Void value) {
                return null;
            }
        });
        service.add(Object.class, String.class, new Conversion<Object, String>() {

            public String convert(Object value) {
                return value.toString();
            }
        });
    }
}
