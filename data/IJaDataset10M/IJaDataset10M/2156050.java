package lights.extensions;

import lights.interfaces.IField;

/**
 * This interface is implemented by all fields providing meta information
 * 
 * @author costa
 * 
 * Added "extends IField": one shouldn't need to "implement" both interfaces
 * @author babak
 */
public interface ILabelledField extends IField {

    /**
	 * Associates a label to this field
	 * 
	 * @param label
	 * @return itself
	 */
    IField setLabel(String label);

    /**
	 * 
	 * @return the metainformation associate to this field if any,
	 *            <code>null</code> otherwise
	 */
    String getLabel();
}
