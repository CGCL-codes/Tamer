package ui.view.swing.component.search;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import message.MessageId;
import query.criteria.StockArticleSearchCriteria;
import ui.view.swing.SwingUI;

public class PricePercentageSearchPanel extends StandardSearchPanel implements StockArticleSearchCriteria {

    private JTextField nameField;

    public PricePercentageSearchPanel() {
        initComponents();
    }

    private void initComponents() {
        nameField = new JTextField();
        filtersPanel().add(SwingUI.instance().decorated(nameField, MessageId.name));
        resultsTable().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    public String getArticleName() {
        return nameField.getText();
    }
}
