package sippoint.data.message.header;

import sippoint.data.message.header.model.Header;

/**
 * @author Martin Hynar
 * 
 */
public class PriorityHeader extends Header {

    /**
	 * Header name.
	 */
    public static final String HEADER_NAME = "Priority";

    /**
	 * Header section: priority-value
	 */
    public static final String SECTION_PRIORITY_VALUE = "priority-value";

    /**
	 * Creates instance of Call-ID header.
	 */
    public PriorityHeader() {
        super();
        this.headerName = HEADER_NAME;
    }

    /**
	 * {@inheritDoc}
	 */
    public String getActualValue() {
        return getComposedValue();
    }

    /**
	 * {@inheritDoc}
	 */
    public String getComposedValue() {
        if (composedValue == null) {
            StringBuilder stringRepresentation = new StringBuilder();
            stringRepresentation.append(getPriorityValue());
            composedValue = stringRepresentation.toString();
        }
        return this.composedValue;
    }

    public void setPriorityValue(String priorityValue) {
        composedValue = null;
        actual().setSection(SECTION_PRIORITY_VALUE, priorityValue);
    }

    public String getPriorityValue() {
        return (String) actual().getSection(SECTION_PRIORITY_VALUE);
    }
}
