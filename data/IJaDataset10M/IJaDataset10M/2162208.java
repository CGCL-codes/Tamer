package org.webguitoolkit.ui.controls.table;

import org.webguitoolkit.ui.controls.form.ISelect;

/**
 * A table wide filter that can be applied.
 * 
 * @author Peter
 * 
 */
public interface ITableFilter extends ISelect {

    /**
	 * 
	 * @param ref
	 *            t.b.d.
	 */
    void setRef(String ref);

    /**
	 * 
	 * @param label
	 *            for the filter
	 */
    void setLabel(String label);

    /**
	 * 
	 * @param labelKey
	 *            resource key for the label
	 */
    void setLabelKey(String labelKey);
}
