package info.joseluismartin.vaadin.ui.table;

import com.vaadin.ui.Button.ClickEvent;

/**
 * Actionf to apply the filter on PageableTable
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.1
 */
public class FindAction extends TableButtonListener {

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void buttonClick(ClickEvent event) {
        if (getTable().getFilterForm() != null) {
            getTable().getFilterForm().commit();
            getTable().getPaginator().firstPage();
        }
    }
}
