package Controllers.NewCard;

import Controllers.Main.MainWindowController;
import Interfaces.NewCard;
import Messages.OKMessage;
import Utils.Card;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

/**
 *
 * @author nrovinskiy
 */
public class NewCardController extends NewCard {

    private MainWindowController mwcHost;

    private Card crdNew;

    public NewCardController(MainWindowController host) {
        super(host);
        mwcHost = host;
        crdNew = null;
        this.setLocation(host.getLocation().x + 50, host.getLocation().y + 100);
        btnOK.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Enumeration<Card> enCards = mwcHost.getProfile().getCards();
                while (enCards.hasMoreElements()) {
                    if (enCards.nextElement().getName().trim().equals(txtName.getText().trim())) {
                        OKMessage okm = new OKMessage(mwcHost, "The card with name " + txtName.getText().trim() + " already exists!");
                        okm.setVisible(true);
                        okm = null;
                        return;
                    }
                }
                if (txtName.getText().trim().length() == 0) {
                    OKMessage okm = new OKMessage(mwcHost, "The \"Name\" and \"Initial amount of money\" fields are obligatory!");
                    okm.setVisible(true);
                    okm = null;
                } else {
                    try {
                        crdNew = new Card(txtName.getText(), Double.parseDouble(txtValue.getText()), mwcHost.getProfile());
                        mwcHost.getProfile().addCard(crdNew);
                        mwcHost.LoadCard(crdNew, true);
                        mwcHost.UpdateCardNumbers(false);
                        dispose();
                    } catch (NumberFormatException ex) {
                        OKMessage okm = new OKMessage(mwcHost, "The field \"Initial amount of money\" contains invalid characters!");
                        okm.setVisible(true);
                        okm = null;
                    }
                }
            }
        });
    }
}
