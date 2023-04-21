package com.mgiandia.library.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
* <p/>
* � ����� ��� �� �������� ���������
*/
public class LoanJFrame extends javax.swing.JFrame implements LoanView {

    /**
     * 
     */
    private static final long serialVersionUID = 2074418530429005163L;

    private JTextField borrowerNo;

    private JButton searchBorrower;

    private JButton searchItem;

    private JLabel firstNameLabel;

    private JLabel lastNameLabel;

    private JLabel borrowerNoLaber;

    private JButton cancelButton;

    private JButton loanButton;

    private JTextField bookTitle;

    private JLabel itemNoLabel;

    private JLabel titleLabel;

    private JTextField borrowerFirstName;

    private JTextField borrowerLastName;

    private JTextField itemNumber;

    private LoanPresenter presenter;

    public LoanJFrame() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
            getContentPane().setLayout(thisLayout);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            {
                itemNumber = new JTextField();
            }
            {
                searchItem = new JButton();
                searchItem.setText("Search");
                searchItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        searchItemActionPerformed(evt);
                    }
                });
            }
            {
                borrowerNoLaber = new JLabel();
                borrowerNoLaber.setText("Borrower No");
            }
            {
                borrowerNo = new JTextField();
            }
            {
                searchBorrower = new JButton();
                searchBorrower.setText("Search");
                searchBorrower.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        searchBorrowerActionPerformed(evt);
                    }
                });
            }
            {
                loanButton = new JButton();
                loanButton.setText("OK");
                loanButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        loanButtonActionPerformed(evt);
                    }
                });
            }
            {
                cancelButton = new JButton();
                cancelButton.setText("Cancel");
                cancelButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        cancelButtonActionPerformed(evt);
                    }
                });
            }
            {
                bookTitle = new JTextField();
            }
            {
                titleLabel = new JLabel();
                titleLabel.setText("Book Title");
            }
            {
                itemNoLabel = new JLabel();
                itemNoLabel.setText("Item Number");
            }
            {
                lastNameLabel = new JLabel();
                lastNameLabel.setText("Last Name");
            }
            {
                borrowerLastName = new JTextField();
            }
            {
                firstNameLabel = new JLabel();
                firstNameLabel.setText("First Name");
            }
            {
                borrowerFirstName = new JTextField();
            }
            thisLayout.setVerticalGroup(thisLayout.createSequentialGroup().add(7).add(thisLayout.createParallelGroup(GroupLayout.BASELINE).add(GroupLayout.BASELINE, borrowerNo, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).add(GroupLayout.BASELINE, borrowerNoLaber, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).add(GroupLayout.BASELINE, searchBorrower, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(thisLayout.createParallelGroup(GroupLayout.BASELINE).add(GroupLayout.BASELINE, borrowerLastName, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).add(GroupLayout.BASELINE, lastNameLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(thisLayout.createParallelGroup(GroupLayout.BASELINE).add(GroupLayout.BASELINE, borrowerFirstName, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).add(GroupLayout.BASELINE, firstNameLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).add(26).add(thisLayout.createParallelGroup(GroupLayout.BASELINE).add(GroupLayout.BASELINE, itemNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).add(GroupLayout.BASELINE, searchItem, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).add(GroupLayout.BASELINE, itemNoLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.UNRELATED).add(thisLayout.createParallelGroup(GroupLayout.BASELINE).add(GroupLayout.BASELINE, bookTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).add(GroupLayout.BASELINE, titleLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).add(27).add(thisLayout.createParallelGroup(GroupLayout.BASELINE).add(GroupLayout.BASELINE, cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).add(GroupLayout.BASELINE, loanButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addContainerGap(50, 50));
            thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup().addContainerGap().add(thisLayout.createParallelGroup().add(GroupLayout.LEADING, thisLayout.createSequentialGroup().add(firstNameLabel, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE).add(6)).add(GroupLayout.LEADING, lastNameLabel, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE).add(GroupLayout.LEADING, thisLayout.createSequentialGroup().add(borrowerNoLaber, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE).add(20)).add(GroupLayout.LEADING, thisLayout.createSequentialGroup().add(titleLabel, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE).add(10)).add(GroupLayout.LEADING, thisLayout.createSequentialGroup().add(itemNoLabel, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE).add(10))).addPreferredGap(LayoutStyle.RELATED).add(thisLayout.createParallelGroup().add(thisLayout.createSequentialGroup().add(borrowerFirstName, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE).add(0, 0, Short.MAX_VALUE)).add(thisLayout.createSequentialGroup().add(borrowerLastName, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE).add(0, 0, Short.MAX_VALUE)).add(thisLayout.createSequentialGroup().add(thisLayout.createParallelGroup().add(GroupLayout.LEADING, itemNumber, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE).add(GroupLayout.LEADING, borrowerNo, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(thisLayout.createParallelGroup().add(GroupLayout.LEADING, thisLayout.createSequentialGroup().add(searchItem, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE).add(0, 57, Short.MAX_VALUE)).add(GroupLayout.LEADING, thisLayout.createSequentialGroup().add(searchBorrower, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE).add(0, 58, Short.MAX_VALUE)).add(GroupLayout.LEADING, thisLayout.createSequentialGroup().add(17).add(loanButton, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.UNRELATED).add(cancelButton, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE).add(0, 0, Short.MAX_VALUE)))).add(thisLayout.createSequentialGroup().add(bookTitle, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE).add(0, 0, Short.MAX_VALUE))).addContainerGap(194, 194));
            pack();
            this.setSize(425, 302);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getBorrowerNo() {
        return Integer.parseInt(borrowerNo.getText());
    }

    public int getItemNumber() {
        return Integer.parseInt(itemNumber.getText());
    }

    public boolean isLoanActionEnabled() {
        return loanButton.isEnabled();
    }

    public void setLoanActionEnabled(boolean enabled) {
        loanButton.setEnabled(enabled);
    }

    public void setBookTitle(String name) {
        bookTitle.setText(name);
    }

    public void setBorrowerFirstName(String name) {
        borrowerFirstName.setText(name);
    }

    public void setBorrowerLastName(String name) {
        borrowerLastName.setText(name);
    }

    public void setPresenter(LoanPresenter presenter) {
        this.presenter = presenter;
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Library", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Library", JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchBorrowerActionPerformed(ActionEvent evt) {
        presenter.findBorrower();
    }

    private void searchItemActionPerformed(ActionEvent evt) {
        presenter.findItem();
    }

    public void open() {
        setVisible(true);
    }

    public void close() {
        dispose();
    }

    private void loanButtonActionPerformed(ActionEvent evt) {
        presenter.borrowItem();
    }

    private void cancelButtonActionPerformed(ActionEvent evt) {
        presenter.cancel();
    }
}
