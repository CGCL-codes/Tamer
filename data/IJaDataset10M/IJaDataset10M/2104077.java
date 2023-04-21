package org.jsresources.apps.filterdesign.filter;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import org.jsresources.apps.jmvp.model.Model;
import org.jsresources.apps.jmvp.model.ModelEvent;
import org.jsresources.apps.jmvp.presenter.Presenter;
import org.jsresources.apps.jmvp.JTable.JTableView;
import org.jsresources.apps.jmvp.JTable.ModelListeningTableModel;
import org.jsresources.apps.jmvp.view.swing.TabView;

/**	A view to show calculated coefficients for FIR filters.

	@author Matthias Pfisterer
*/
public class FIRCoefficientsView extends JTableView implements TabView {

    /**	The constructor.
		A presenter object has to be passed. Passing <CODE>null</CODE>
		generates an exception. The presenter set this way can be
		queried using <CODE>getPresenter()</CODE>.
	
		@param presenter the presenter to associate with this view.
	
		@see #getPresenter()
	*/
    public FIRCoefficientsView(Presenter presenter) {
        super(presenter);
    }

    protected ModelListeningTableModel createTableModel(Model model) {
        FIRFilterModel firFilterModel = (FIRFilterModel) model;
        ModelListeningTableModel tableModel = new FIRCoefficientsTableModel(firFilterModel);
        return tableModel;
    }

    public String getTabName() {
        return "Coefficients";
    }

    public void modelChanged(ModelEvent event) {
    }
}
