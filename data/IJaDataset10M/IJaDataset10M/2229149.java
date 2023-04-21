package presentation.dialogs;

import core.SystemRegException;
import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JTextField;
import core.data_tier.entities.Action;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import core.data_tier.entities.Additional;
import core.data_tier.entities.Participant;
import java.awt.Dimension;
import javax.swing.JOptionPane;
import presentation.Globals;
import presentation.MainFrame;

/**
 *
 * @author Lahvi
 */
public class ParticipantDialog extends AbstractDialog {

    private JButton okBtn, backBtn, additionalBtn;

    private JTextField fName, lName, idNum, email, login, password;

    private Additional a;

    private Participant p;

    private Collection<Long> actions, chooseAct;

    public ParticipantDialog() {
        super();
        p = null;
        a = new Additional();
        this.actions = new ArrayList<Long>(Globals.getInstance().getLogedUser().getActions());
        this.chooseAct = new ArrayList<Long>();
        setTitle("Vytvoření nového účastníka");
        init();
        login.setEditable(false);
        password.setEditable(false);
    }

    public ParticipantDialog(Participant p) throws SystemRegException {
        this();
        if (p == null || p.getActionIDs() == null) {
            throw new SystemRegException("Chyba. Editovaný účastník je null.");
        }
        this.p = p;
        if (p.getAddParams() != null) {
            a = p.getAddParams();
        }
        setExistValues();
        setTitle("Upravení účastníka");
    }

    @Override
    protected void init() {
        setLayout(new BorderLayout());
        JPanel reqPanel = new JPanel(new BorderLayout());
        JPanel btnPanel = new JPanel(new GridLayout(2, 1));
        JPanel lblPanel = new JPanel(new GridLayout(7, 1));
        JPanel valuePanel = new JPanel(new GridLayout(7, 1));
        JPanel downBtnPanel = new JPanel(new GridLayout(1, 2));
        fName = new JTextField();
        JLabel _fName = new JLabel("Křestní jméno");
        lName = new JTextField();
        JLabel _lName = new JLabel("Příjmení");
        email = new JTextField();
        JLabel _email = new JLabel("Email");
        idNum = new JTextField();
        JLabel _idNum = new JLabel("Číslo OP");
        JLabel _actions = new JLabel("Akce");
        JLabel _login = new JLabel("Login");
        JLabel _pswd = new JLabel("Heslo");
        login = new JTextField();
        password = new JTextField();
        lblPanel.add(_fName);
        valuePanel.add(fName);
        lblPanel.add(_lName);
        valuePanel.add(lName);
        lblPanel.add(_email);
        valuePanel.add(email);
        lblPanel.add(_idNum);
        valuePanel.add(idNum);
        lblPanel.add(_login);
        valuePanel.add(login);
        lblPanel.add(_pswd);
        valuePanel.add(password);
        lblPanel.add(_actions);
        JButton actionBtn = new JButton(new AbstractAction("Přiřadit akce") {

            @Override
            public void actionPerformed(ActionEvent e) {
                new ChooseActionDialog(actions, chooseAct).setVisible(true);
            }
        });
        valuePanel.add(actionBtn);
        reqPanel.add(lblPanel, BorderLayout.LINE_START);
        reqPanel.add(valuePanel, BorderLayout.CENTER);
        reqPanel.setBorder(BorderFactory.createTitledBorder("Povinné údaje"));
        additionalBtn = new JButton(new AbstractAction("Přidat doplňující informace") {

            @Override
            public void actionPerformed(ActionEvent e) {
                new AdditionalDialog(a).setVisible(true);
            }
        });
        backBtn = new JButton(new AbstractAction("Zpět") {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        okBtn = new JButton(new AbstractAction("OK") {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    okAction();
                } catch (SystemRegException ex) {
                    Globals.showErr(ParticipantDialog.this, ex);
                }
            }
        });
        downBtnPanel.add(backBtn);
        downBtnPanel.add(okBtn);
        btnPanel.add(additionalBtn);
        btnPanel.add(downBtnPanel);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(5, 1, 5, 1));
        add(reqPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.PAGE_END);
        setSize(new Dimension(500, 330));
        setCenterPos();
    }

    private void okAction() throws SystemRegException {
        try {
            String firstName = fName.getText();
            String lastName = lName.getText();
            String pEmail = email.getText();
            String cardID = idNum.getText();
            String log = null, pswd = null;
            if (chooseAct.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || pEmail.isEmpty() || cardID.isEmpty()) {
                throw new SystemRegException("Musíte mít vyplněné všechny povinné parametry");
            }
            if (login.isEditable()) {
                log = login.getText();
                pswd = password.getText();
            }
            long pcardId = Long.parseLong(cardID);
            int res = MainFrame.showOptionDialog("Opravdu chcete uložit provedené změny?");
            if (res == JOptionPane.YES_OPTION) {
                if (p == null) {
                    Globals.getInstance().getParticipantOps().createParticipant(firstName, lastName, pEmail, pcardId, getChoosedActionIDs(), a);
                } else {
                    if (p.isCompleteReg()) {
                        Globals.getInstance().getParticipantOps().editLogins(p.getId(), log, pswd);
                    }
                    Globals.getInstance().getParticipantOps().addActions(p.getId(), getChoosedActionIDs());
                    Globals.getInstance().getParticipantOps().editParameters(pEmail, firstName, lastName, pcardId, p.getId());
                    Globals.getInstance().getParticipantOps().editAdditionals(p.getId(), a);
                }
                Globals.getInstance().refreshData();
                dispose();
            }
        } catch (NumberFormatException ex) {
            throw new SystemRegException("Číslo OP musí být celé číslo.");
        }
    }

    private void setExistValues() {
        fName.setText(p.getFirstName());
        lName.setText(p.getLastName());
        email.setText(p.getEmail());
        idNum.setText("" + p.getCardID());
        setParticipantActions();
        if (p.isCompleteReg()) {
            login.setEditable(true);
            password.setEditable(true);
            login.setText(p.getLogin());
            password.setText(p.getPassword());
        }
    }

    private void setParticipantActions() {
        Collection<Long> actionIDs = p.getActionIDs();
        for (Long long1 : actionIDs) {
            try {
                Action action = Globals.getInstance().getActionOps().getAction(long1);
                if (actions.contains(action.getID())) {
                    actions.remove(action.getID());
                    chooseAct.add(action.getID());
                }
            } catch (SystemRegException ex) {
                Globals.showErr(this, ex);
            }
        }
    }

    private Collection<Long> getChoosedActionIDs() throws SystemRegException {
        if (chooseAct.isEmpty()) throw new SystemRegException("Účastník musí mít přiřazenu alespoň jednu akci!");
        Collection<Long> actionIDs = new ArrayList<Long>(chooseAct);
        return actionIDs;
    }
}
