package converter.gui.dialog;

import converter.gui.SippXMLConverter;
import converter.gui.command.CloseCommand;
import converter.res.interfaces.IConstants;
import converter.res.interfaces.IOptionGUIElements;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Christian
 */
public class InsertTag extends JDialog implements IOptionGUIElements, IConstants {

    private JFrame parent;

    private JLabel lTag = new JLabel("Choose Tag to insert");

    private JComboBox cbTag = new JComboBox();

    private JButton bInsert = new JButton("Insert");

    private JButton bCancel = new JButton("Cancel");

    public InsertTag(JFrame parent) {
        this.parent = parent;
        makeLayout();
        addListeners();
        setModal(true);
        setResizable(false);
        setTitle("Insert new Tag");
        Point location = parent.getLocation();
        int width = (parent.getSize().width - this.getWidth()) / 2;
        int height = (parent.getSize().height - this.getHeight()) / 2;
        setLocation(width < 0 ? (int) (screenWidth - this.getWidth()) / 2 : width + location.x, height < 0 ? (int) (screenHeight - this.getHeight()) / 2 : height + location.y);
    }

    public void makeLayout() {
        JPanel pNorth = new JPanel(new GridLayout(2, 1));
        JPanel pSouth = new JPanel(new FlowLayout());
        Vector<String> data = new Vector<String>();
        data.add("Pause");
        data.add("Response Time Repartition");
        data.add("Call Time Repartition");
        data.add("send");
        data.add("recv");
        data.add("label");
        data.add("nop");
        data.add("sendCmd");
        data.add("recvCmd");
        data.add("Unknown Tag");
        data.add("action");
        cbTag = new JComboBox(data);
        cbTag.setSelectedIndex(0);
        cbDist.setSelectedIndex(0);
        JPanel pTag = new JPanel(new FlowLayout());
        pTag.add(lTag);
        pTag.add(cbTag);
        pNorth.add(pTag);
        pSouth.add(new JPanel(new FlowLayout()).add(bInsert));
        pSouth.add(new JPanel(new FlowLayout()).add(bCancel));
        add(pNorth, BorderLayout.NORTH);
        add(pPause, BorderLayout.CENTER);
        add(pSouth, BorderLayout.SOUTH);
        pack();
    }

    public void addListeners() {
        bCancel.addActionListener(new CloseCommand(this, parent, false, true));
        addWindowListener(new CloseCommand(this, parent, false, true));
        cbTag.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                int index = cbTag.getSelectedIndex();
                remove(pPause);
                remove(pValue);
                remove(pSend);
                remove(pRecv);
                remove(pSendCmd);
                remove(pRecvCmd);
                remove(pNop);
                remove(pUnknown);
                remove(pLabel);
                remove(pAction);
                switch(index) {
                    case 0:
                        add(pPause, BorderLayout.CENTER);
                        break;
                    case 1:
                        add(pValue, BorderLayout.CENTER);
                        break;
                    case 2:
                        add(pValue, BorderLayout.CENTER);
                        break;
                    case 3:
                        add(pSend, BorderLayout.CENTER);
                        break;
                    case 4:
                        add(pRecv, BorderLayout.CENTER);
                        break;
                    case 5:
                        add(pLabel, BorderLayout.CENTER);
                        break;
                    case 6:
                        add(pNop, BorderLayout.CENTER);
                        break;
                    case 7:
                        add(pSendCmd, BorderLayout.CENTER);
                        break;
                    case 8:
                        add(pRecvCmd, BorderLayout.CENTER);
                        break;
                    case 9:
                        add(pUnknown, BorderLayout.CENTER);
                        break;
                    case 10:
                        add(pAction, BorderLayout.CENTER);
                        break;
                }
                pack();
                repaint();
            }
        });
        bInsert.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                StringBuffer temp = new StringBuffer("  <");
                if (((String) cbTag.getSelectedItem()).equals("Pause")) {
                    temp.append("pause");
                    if (!tfMilli.getText().isEmpty()) temp.append(" milliseconds=\"").append(tfMilli.getText()).append("\"");
                    if (!tfVariable.getText().isEmpty()) temp.append(" variable=\"").append(tfVariable.getText()).append("\"");
                    if (!tfNext[2].getText().isEmpty()) temp.append(" next=\"").append(tfNext[2].getText()).append("\"");
                    if (!((String) cbDist.getSelectedItem()).equals("none")) {
                        temp.append(" distribution=\"").append((String) cbDist.getSelectedItem()).append("\"");
                        temp.append(" ").append(tfDistPar.getText());
                    }
                    if (!cbSanityCheck.isSelected()) temp.append(" sanity_check=\"false\"");
                    if (cbCrlf[2].isSelected()) temp.append(" crlf=\"true\"");
                    if (!tfAddOptions[7].getText().isEmpty()) temp.append(" ").append(tfAddOptions);
                    temp.append(" />\n");
                }
                if (((String) cbTag.getSelectedItem()).equals("Response Time Repartition") || ((String) cbTag.getSelectedItem()).equals("Call Time Repartition")) {
                    temp.append((String) cbTag.getSelectedItem());
                    if (!tfValue.getText().equals("")) temp.append(" value=\"").append(tfValue.getText()).append("\"");
                    temp.append(" />\n");
                }
                if (((String) cbTag.getSelectedItem()).equals("send")) {
                    temp.append("send");
                    if (!tfRetrans.getText().isEmpty()) temp.append(" retrans=\"").append(tfRetrans.getText()).append("\"");
                    if (!tfStartRtd[0].getText().isEmpty()) temp.append(" start_rtd=\"").append(tfStartRtd[0].getText()).append(" \"");
                    if (!tfRtd[0].getText().isEmpty()) temp.append(" rtd=\"").append(tfRtd[0].getText()).append("\"");
                    if (cbRepeatRtd[0].isSelected()) temp.append(" repeat_rtd=\"true\"");
                    if (cbCrlf[0].isSelected()) temp.append(" crlf=\"true\"");
                    if (!tfLost[0].getText().isEmpty()) temp.append(" lost=\"").append(tfLost[0].getText()).append("\"");
                    if (!tfNext[0].getText().isEmpty()) temp.append(" next=\"").append(tfNext[0].getText()).append("\"");
                    if (!tfTest[1].getText().isEmpty()) temp.append(" test=\"").append(tfTest[0].getText()).append("\"");
                    if (!tfCounter[0].getText().isEmpty()) temp.append(" counter=\"").append(tfCounter[0].getText()).append("\"");
                    if (!tfStartTxn.getText().isEmpty()) temp.append(" start_txn=\"").append(tfStartTxn.getText()).append("\"");
                    if (!tfAddOptions[0].getText().isEmpty()) temp.append(" ").append(tfAddOptions[0].getText());
                    temp.append(">\n<![CDATA[\n\n  ]]>\n</send>\n");
                }
                if (((String) cbTag.getSelectedItem()).equals("recv")) {
                    System.out.println("recv!!!");
                    temp.append("recv");
                    if (!tfResponse.getText().isEmpty()) temp.append(" response=\"").append(tfResponse.getText()).append("\"");
                    if (!tfRequest.getText().isEmpty()) temp.append(" request=\"").append(tfRequest.getText()).append("\"");
                    if (cbOptional.isSelected()) temp.append(" optional=\"true\"");
                    if (cbCrlf[1].isSelected()) temp.append(" crlf=\"true\"");
                    if (cbRrs.isSelected()) temp.append(" rrs=\"true\"");
                    if (cbAuth.isSelected()) temp.append(" auth=\"true\"");
                    if (!tfStartRtd[1].getText().isEmpty()) temp.append(" start_rtd=\"").append(tfStartRtd[1].getText()).append("\"");
                    if (!tfRtd[1].getText().isEmpty()) temp.append(" rtd=\"").append(tfRtd[1].getText()).append("\"");
                    if (cbRepeatRtd[1].isSelected()) temp.append(" repeat_rtd=\"true\"");
                    if (!tfLost[1].getText().isEmpty()) temp.append(" lost=\"").append(tfLost[1].getText()).append("\"");
                    if (!tfTimeout.getText().isEmpty()) temp.append(" timeout=\"").append(tfTimeout.getText()).append("\"");
                    if (!tfOnTimeout.getText().isEmpty()) temp.append(" ontimeout=\"").append(tfOnTimeout.getText()).append("\"");
                    if (!tfNext[1].getText().isEmpty()) temp.append(" next=\"").append(tfNext[1].getText()).append("\"");
                    if (!tfTest[1].getText().isEmpty()) temp.append(" test=\"").append(tfTest[1].getText()).append("\"");
                    if (!tfChance.getText().isEmpty()) temp.append(" chance=\"").append(tfChance.getText()).append("\"");
                    if (!tfCounter[1].getText().isEmpty()) temp.append(" counter=\"").append(tfCounter[1].getText()).append("\"");
                    if (cbRegexMatch.isSelected()) temp.append(" regexp_match=\"true\"");
                    if (!tfResponseTxn.getText().isEmpty()) temp.append(" response_txn=\"").append(tfResponseTxn.getText()).append("\"");
                    if (!tfAddOptions[1].getText().isEmpty()) temp.append(" ").append(tfAddOptions[1].getText());
                    temp.append(">");
                    System.out.println("BEFORE ACTION: " + cbAction[0].isSelected());
                    if (cbAction[0].isSelected()) temp.append("\n    <action>\n    </action>\n");
                    temp.append("  </recv>\n");
                }
                if (((String) cbTag.getSelectedItem()).equals("label")) {
                    temp.append("label");
                    if (!tfID.getText().isEmpty()) temp.append(" id=\"").append(tfID.getText()).append("\"");
                    if (!tfAddOptions[5].getText().isEmpty()) temp.append(" ").append(tfAddOptions[5].getText());
                    temp.append(" />\n");
                }
                if (((String) cbTag.getSelectedItem()).equals("nop")) {
                    temp.append("nop");
                    if (!tfStartRtd[2].getText().isEmpty()) temp.append(" start_rtd=\"").append(tfStartRtd[2].getText()).append("\"");
                    if (!tfRtd[2].getText().isEmpty()) temp.append(" rtd=\"").append(tfRtd[2].getText()).append("\"");
                    if (!tfAddOptions[2].getText().isEmpty()) temp.append(" ").append(tfAddOptions[2].getText());
                    temp.append(">");
                    temp.append("  </nop>\n");
                }
                if (((String) cbTag.getSelectedItem()).equals("sendCmd")) {
                    temp.append("sendCmd");
                    if (!tfDest.getText().isEmpty()) temp.append(" src=\"").append(tfDest.getText()).append("\"");
                    if (!tfAddOptions[3].getText().isEmpty()) temp.append(" ").append(tfAddOptions[3].getText());
                    temp.append(">");
                    if (cbAction[1].isSelected()) temp.append("\n    <action>\n    </action>\n");
                    temp.append("  </sendCmd>\n");
                }
                if (((String) cbTag.getSelectedItem()).endsWith("recvCmd")) {
                    temp.append("recvCmd");
                    if (!tfSrc.getText().isEmpty()) temp.append(" src=\"").append(tfSrc.getText()).append("\"");
                    if (!tfAddOptions[4].getText().isEmpty()) temp.append(" ").append(tfAddOptions[4].getText());
                    temp.append(">");
                    if (cbAction[2].isSelected()) temp.append("\n    <action>\n    </action>\n");
                    temp.append("  </recvCmd>\n");
                }
                if (((String) cbTag.getSelectedItem()).equals("action")) {
                    temp.append("action");
                    if (!tfAddOptions[8].getText().isEmpty()) temp.append(" ").append(tfAddOptions[1].getText());
                    temp.append(">");
                    Object[] s = liAction.getSelectedValues();
                    if (liAction.getSelectedValues().length != 0) {
                        Properties action = ((SippXMLConverter) parent).getProperties();
                        for (Object current : s) temp.append("\n      " + action.getProperty((String) current));
                    }
                    temp.append("\n    </action>\n");
                }
                if (((String) cbTag.getSelectedItem()).equals("Unknown Tag")) {
                    temp.append(tfTagName.getText());
                    temp.append(" ").append(tfOptions.getText()).append("></").append(tfTagName.getText()).append(">\n");
                }
                ((SippXMLConverter) parent).setNewValue(new String(temp));
                dispose();
            }
        });
    }
}
