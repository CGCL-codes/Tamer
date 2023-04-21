package ui.view.swing.component.detail;

import javax.swing.JFormattedTextField;
import message.MessageId;
import model.JuridicPerson;
import model.money.MoneyAmount;
import ui.controller.manager.UIModelManager;
import ui.view.component.ClientDebtCancellationUI;
import ui.view.swing.SwingUI;
import ui.view.swing.util.objectpicker3.ObjectPicker3;

public class ClientDebtCancellationDialog extends StandardDetailDialog implements ClientDebtCancellationUI {

    private JFormattedTextField amountField;

    private ObjectPicker3 clientPicker;

    public ClientDebtCancellationDialog() {
        super(MessageId.clientDebtCancellation);
        initComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        amountField = SwingUI.instance().currencyField();
        clientPicker = new ObjectPicker3();
        centerPanel().add(SwingUI.instance().decorated(clientPicker, MessageId.client));
        centerPanel().add(SwingUI.instance().decorated(amountField, MessageId.amount));
    }

    public MoneyAmount getAmount() {
        return SwingUI.instance().moneyAmountFrom(amountField);
    }

    public JuridicPerson getClient() {
        return (JuridicPerson) clientPicker.getSelection();
    }

    public void setClientManager(UIModelManager manager) {
        clientPicker.setUIModelManager(manager);
    }
}
