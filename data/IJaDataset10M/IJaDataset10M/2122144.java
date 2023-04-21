package entagged.tageditor.tools.stringtransform.operations;

import entagged.tageditor.resources.LangageManager;
import entagged.tageditor.tools.stringtransform.TransformOperation;

/**
 * This operation will capitalize the first character of an entire string.
 * The rest will get lowercased.<br>
 * 
 * @author Rapha�l Slinckx
 */
public class CapitalizeFirstOp extends TransformOperation {

    /**
	 * Creates an instance.
	 * 
	 */
    public CapitalizeFirstOp() {
        super(4, 0);
    }

    /**
	 * (overridden)
	 * 
	 * @see entagged.tageditor.tools.stringtransform.TransformOperation#getDescription()
	 */
    public String getDescription() {
        return LangageManager.getProperty("case.capitalizefirst");
    }

    /**
	 * Capitalizes the first character of the given String. The rest will
	 * become lowercase<br>
	 * 
	 * @param value
	 *            The string whose first character is about to be converted.
	 * @return <code>value</code>, where the first character is asserted to
	 *         be uppercase and the rest to be lowercase.
	 */
    public String transform(String value) {
        if (value.length() <= 0) return value;
        value = value.toLowerCase();
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }
}
