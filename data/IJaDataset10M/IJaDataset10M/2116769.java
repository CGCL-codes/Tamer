package ru.yermak.bookkeeping.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.yermak.bookkeeping.common.Command;
import ru.yermak.bookkeeping.model.Currency;
import ru.yermak.bookkeeping.ui.ModalForm;
import ru.yermak.bookkeeping.ui.Selector;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

/**
 * User: harrier
 * Date: Aug 13, 2008
 */
@Scope("action")
@org.springframework.stereotype.Component
public class CurrencyEditCommand implements Command {

    @Autowired
    private CurrencyDao currencyDao;

    @Autowired
    private ModalForm modalForm;

    @Autowired
    @CurrencyQulifier
    private Selector selector;

    @Autowired
    @CurrencyQulifier("editForm")
    private JComponent currencyEditForm;

    @Autowired
    @CurrencyQulifier("code")
    private JTextField currencyCode;

    @Autowired
    @CurrencyQulifier("rate")
    private JTextField rateField;

    @Autowired
    @CurrencyQulifier("original")
    private JCheckBox originalCheckBox;

    @Transactional(propagation = Propagation.REQUIRED)
    public void execute(ActionEvent e) {
        Integer currencyId = selector.getSelectedId();
        Currency currency = currencyDao.getById(currencyId);
        currencyCode.setText(currency.getCode());
        originalCheckBox.setSelected(currency.isOriginal());
        rateField.setText(currency.getRate().toString());
        boolean result = modalForm.show(currencyEditForm);
        if (result) {
            currency.updateRate(new BigDecimal(rateField.getText()));
            currencyDao.updateCurrency(currency);
            selector.selectRow(currencyId);
        }
    }
}
