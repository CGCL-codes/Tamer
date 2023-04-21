package net.sourceforge.squirrel_sql.fw.datasetviewer;

import java.awt.Component;

public interface IDataSetModelConverter {

    /**
	 * Set the <TT>IDataSetModel</TT> to display
	 * data from.
	 * 
	 * @param	model	<TT>IDataSetModel</TT> containing the table data.
	 */
    void setDataSetModel(IDataSetModel model);

    /**
	 * Get the component for this converter.
	 */
    Component getComponent();
}
