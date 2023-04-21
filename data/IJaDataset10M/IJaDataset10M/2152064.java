package net.sf.spring.batch.sample;

import net.sf.spring.batch.impl.BatchResourceValidation;

public class SimpleResourceValidation implements BatchResourceValidation {

    protected String value;

    protected String name;

    public boolean exectue(StringBuffer buffer, String mimeType) {
        buffer.append("Simple dummy validation");
        return value.equals("ok");
    }

    /**
	 * @param value the value to set
	 */
    public void setValue(String value) {
        this.value = value;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }
}
