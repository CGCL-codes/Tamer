package de.shandschuh.jaolt.gui.listener.editauction.shippingfeespanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import de.shandschuh.jaolt.gui.editauction.ShippingServiceJComboBox;

public class JComboBoxChangeListener implements ActionListener {

    private ShippingServiceJComboBox shippingServiceJComboBox;

    public JComboBoxChangeListener(ShippingServiceJComboBox shippingServiceJComboBox) {
        this.shippingServiceJComboBox = shippingServiceJComboBox;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        boolean visible = shippingServiceJComboBox.getSelectedIndex() != 0;
        shippingServiceJComboBox.getPriceJTextField().setVisible(visible);
        shippingServiceJComboBox.getAddPriceJCheckBoxPriceField().setVisible(visible);
        shippingServiceJComboBox.getCurrencySymbolJLabel().setVisible(visible);
        shippingServiceJComboBox.getMaximumDurationJComboBox().setVisible(visible);
        JScrollPane scrollPane = shippingServiceJComboBox.getCountryJCheckBoxesJScrollPane();
        if (scrollPane != null) {
            scrollPane.setVisible(visible);
        }
    }
}
