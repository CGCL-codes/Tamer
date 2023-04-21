package Controllers.CardPane;

import Controllers.*;
import Controllers.Main.MainWindowController;
import Messages.OKMessage;
import Utils.DateTool;
import Utils.Transaction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

/**
 *
 * @author nrovinskiy
 */
public class AddListener implements ActionListener {

    MainWindowController mwcHost;

    CardPaneController cpcCard;

    public AddListener(MainWindowController host, CardPaneController cardpane) {
        mwcHost = host;
        cpcCard = cardpane;
    }

    public void actionPerformed(ActionEvent e) {
        if (Validate()) {
            try {
                cpcCard.addTransaction(new Transaction(Transaction.ID_NOT_SET, Double.parseDouble(cpcCard.getAmount().trim().replace(',', '.')), cpcCard.getPurpose(), DateTool.getDate(cpcCard.getDate())));
            } catch (ParseException e1) {
            }
            cpcCard.ClearTransactionBlock();
        }
    }

    private boolean Validate() {
        if ((cpcCard.getPurpose() == null) || (cpcCard.getPurpose().trim().length() == 0)) {
            OKMessage okm = new OKMessage(mwcHost, "The field reason is obligatory!");
            okm.setVisible(true);
            okm = null;
            return false;
        }
        if (cpcCard.getAmount().trim().length() == 0) {
            OKMessage okm = new OKMessage(mwcHost, "The field amount is obligatory!");
            okm.setVisible(true);
            okm = null;
            return false;
        }
        try {
            Double.parseDouble(cpcCard.getAmount().trim().replace(',', '.'));
            DateTool.getDate(cpcCard.getDate());
            return true;
        } catch (NumberFormatException ex) {
            OKMessage okm = new OKMessage(mwcHost, "<html>The field amount contains invalid caracter(s), it should be in ###.## format!</html>");
            okm.setVisible(true);
            okm = null;
            return false;
        } catch (ParseException ex1) {
            OKMessage okm = new OKMessage(mwcHost, "The field Date contains incorrect date!");
            okm.setVisible(true);
            okm = null;
            return false;
        }
    }
}
