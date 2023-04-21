package converter.gui.dialog;

import converter.gui.SippXMLConverter;
import converter.res.interfaces.IConstants;
import converter.res.interfaces.IOptionGUIElements;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Christian
 */
public class EditTag extends JDialog implements IOptionGUIElements, IConstants {

    private JFrame parent;

    private Vector<String> regexps;

    private String line = "";

    private String tagName = "";

    private String addOptions = "";

    private Map<String, String> options = new HashMap<String, String>();

    private JButton bApply = new JButton("Apply");

    private JButton bCancel = new JButton("Cancel");

    class TagTitleLabel extends JLabel {

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 100);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Font plainFont = new Font("Verdana", Font.BOLD, 14);
            g.setFont(plainFont);
            java.awt.geom.Rectangle2D r = plainFont.getStringBounds(tagName, ((Graphics2D) g).getFontRenderContext());
            g.drawString(tagName, (int) ((EditTag.this.getPreferredSize().width - r.getWidth()) / 2), (int) ((getPreferredSize().height - r.getHeight()) / 2));
        }
    }

    private JLabel lTitle = new TagTitleLabel();

    public EditTag(String line, JFrame parent, Vector<String> regexps) {
        this.parent = parent;
        this.regexps = regexps;
        this.line = line;
        getTagName();
        getOptions();
        prepareTextFields();
        makeLayout();
        addListeners();
        setModal(true);
        setTitle("Edit Tag");
        setResizable(false);
        Point location = parent.getLocation();
        int width = (parent.getSize().width - this.getWidth()) / 2;
        int height = (parent.getSize().height - this.getHeight()) / 2;
        setLocation(width < 0 ? (int) (screenWidth - this.getWidth()) / 2 : width + location.x, height < 0 ? (int) (screenHeight - this.getHeight()) / 2 : height + location.y);
    }

    private void makeLayout() {
        if (tagName.equals("send")) add(pSend, BorderLayout.CENTER); else if (tagName.equals("recv")) add(pRecv, BorderLayout.CENTER); else if (tagName.equals("pause")) add(pPause, BorderLayout.CENTER); else if (tagName.equals("nop")) add(pNop, BorderLayout.CENTER); else if (tagName.equals("sendCmd")) add(pSendCmd, BorderLayout.CENTER); else if (tagName.equals("recvCmd")) add(pRecvCmd, BorderLayout.CENTER); else if (tagName.equals("label")) add(pLabel, BorderLayout.CENTER); else if (tagName.equals("Response Time Repartition") || tagName.equals("Call Length Repartition")) add(pValue, BorderLayout.CENTER); else if (tagName.equals("action")) add(pAction, BorderLayout.CENTER); else add(pUnknown, BorderLayout.CENTER);
        JPanel pSouth = new JPanel(new FlowLayout());
        pSouth.add(bApply);
        pSouth.add(bCancel);
        add(lTitle, BorderLayout.NORTH);
        add(pSouth, BorderLayout.SOUTH);
        pack();
    }

    private void addListeners() {
        bCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ((SippXMLConverter) parent).setCanceled(true);
                dispose();
            }
        });
        bApply.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                StringBuffer temp = new StringBuffer("<");
                if (tagName.equals("pause")) {
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
                    temp.append(" />");
                } else if (tagName.equals("Response Time Repartition") || tagName.equals("Call Time Repartition")) {
                    temp.append(tagName);
                    if (!tfValue.getText().equals("")) temp.append(" value=\"").append(tfValue.getText()).append("\"");
                    temp.append(" />");
                } else if (tagName.equals("send")) {
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
                    temp.append(">");
                } else if (tagName.equals("recv")) {
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
                    if (cbAction[0].isSelected() && !regexps.contains("<action>")) temp.append("\n    <action>\n    </action>\n  "); else if (!cbAction[0].isSelected() && regexps.contains("<action>")) temp.append("</recv>");
                    System.out.println("BOOLEAN VALUE: " + (!cbAction[0].isSelected() && regexps.contains("<action>")) + " " + !cbAction[0].isSelected() + " " + regexps.contains("<action>"));
                    System.out.println(temp);
                } else if (tagName.equals("label")) {
                    temp.append("label");
                    if (!tfID.getText().isEmpty()) temp.append(" id=\"").append(tfID.getText()).append("\"");
                    if (!tfAddOptions[5].getText().isEmpty()) temp.append(" ").append(tfAddOptions[5].getText());
                    temp.append(" />");
                } else if (tagName.equals("nop")) {
                    temp.append("nop");
                    if (!tfStartRtd[2].getText().isEmpty()) temp.append(" start_rtd=\"").append(tfStartRtd[2].getText()).append("\"");
                    if (!tfRtd[2].getText().isEmpty()) temp.append(" rtd=\"").append(tfRtd[2].getText()).append("\"");
                    if (!tfAddOptions[2].getText().isEmpty()) temp.append(" ").append(tfAddOptions[2].getText());
                    temp.append(">");
                    if (cbAction[1].isSelected()) temp.append("\n    <action>\n    </action>\n  ");
                } else if (tagName.equals("sendCmd")) {
                    temp.append("sendCmd");
                    if (!tfDest.getText().isEmpty()) temp.append(" src=\"").append(tfDest.getText()).append("\"");
                    if (!tfAddOptions[3].getText().isEmpty()) temp.append(" ").append(tfAddOptions[3].getText());
                    temp.append(">");
                } else if (tagName.endsWith("recvCmd")) {
                    temp.append("recvCmd");
                    if (!tfSrc.getText().isEmpty()) temp.append(" src=\"").append(tfSrc.getText()).append("\"");
                    if (!tfAddOptions[4].getText().isEmpty()) temp.append(" ").append(tfAddOptions[4].getText());
                    temp.append(">");
                    if (cbAction[2].isSelected()) temp.append("\n    <action>\n    </action>\n  ");
                } else if (tagName.endsWith("action")) {
                    temp.append("action");
                    if (!tfAddOptions[8].getText().isEmpty()) temp.append(" ").append(tfAddOptions[8].getText());
                    temp.append(">");
                    Object[] s = liAction.getSelectedValues();
                    if (liAction.getSelectedValues().length != 0) {
                        Properties action = ((SippXMLConverter) parent).getProperties();
                        for (Object current : s) temp.append("\n      " + action.getProperty((String) current));
                    }
                } else {
                    temp.append(tfTagName.getText());
                    temp.append(" ").append(tfOptions.getText()).append(">");
                }
                ((SippXMLConverter) parent).setNewValue(new String(temp));
                dispose();
            }
        });
    }

    private void prepareTextFields() {
        if (tagName.equals("Pause")) {
            tfMilli.setText(options.get("milliseconds"));
            tfVariable.setText(options.get("variable"));
            tfNext[2].setText(options.get("next"));
            cbDist.setSelectedItem(options.get("distribution"));
            if (!options.get("value").isEmpty()) tfDistPar.setText(tfDistPar.getText() + " value=\"" + options.get("value") + "\"");
            if (!options.get("min").isEmpty()) tfDistPar.setText(tfDistPar.getText() + " min=\"" + options.get("min") + "\"");
            if (!options.get("max").isEmpty()) tfDistPar.setText(tfDistPar.getText() + " max=\"" + options.get("max") + "\"");
            if (!options.get("mean").isEmpty()) tfDistPar.setText(tfDistPar.getText() + " mean=\"" + options.get("mean") + "\"");
            if (!options.get("stdev").isEmpty()) tfDistPar.setText(tfDistPar.getText() + " stdev=\"" + options.get("stdev") + "\"");
            if (!options.get("lambda").isEmpty()) tfDistPar.setText(tfDistPar.getText() + " lambda=\"" + options.get("lambda") + "\"");
            if (!options.get("k").isEmpty()) tfDistPar.setText(tfDistPar.getText() + " k=\"" + options.get("k") + "\"");
            if (!options.get("x_m").isEmpty()) tfDistPar.setText(tfDistPar.getText() + " x_m=\"" + options.get("x_m") + "\"");
            if (!options.get("p").isEmpty()) tfDistPar.setText(tfDistPar.getText() + " p=\"" + options.get("p") + "\"");
            if (!options.get("n").isEmpty()) tfDistPar.setText(tfDistPar.getText() + " n=\"" + options.get("n") + "\"");
            if (!options.get("theta").isEmpty()) tfDistPar.setText(tfDistPar.getText() + " theta=\"" + options.get("theta") + "\"");
            tfDistPar.setText(tfDistPar.getText().trim());
            cbSanityCheck.setSelected(options.get("sanity_check").equals("true") ? true : false);
            cbCrlf[2].setSelected(options.get("crlf").equals("true") ? true : false);
            tfAddOptions[7].setText(options.get(addOptions));
        } else if (tagName.equals("Response Time Repartition") || tagName.equals("Call Time Repartition")) {
            tfValue.setText(options.get("value"));
            tfAddOptions[6].setText(addOptions);
        } else if (tagName.equals("send")) {
            tfRetrans.setText(options.get("retrans"));
            tfStartRtd[0].setText(options.get("start_rtd"));
            tfRtd[0].setText(options.get("rtd"));
            cbRepeatRtd[0].setSelected(options.get("repeat_rtd").equals("true") ? true : false);
            cbCrlf[0].setSelected(options.get("crlf").equals("true") ? true : false);
            tfLost[0].setText(options.get("lost"));
            tfNext[0].setText(options.get("next"));
            tfTest[1].setText(options.get("test"));
            tfCounter[0].setText(options.get("counter"));
            tfStartTxn.setText(options.get("start_txn"));
            tfAddOptions[0].setText(addOptions);
        } else if (tagName.equals("recv")) {
            tfResponse.setText(options.get("response"));
            tfRequest.setText(options.get("request"));
            cbOptional.setSelected(options.get("optional").equals("true") ? true : false);
            cbCrlf[1].setSelected(options.get("crlf").equals("true") ? true : false);
            System.out.println("RRS: " + options.get("rrs"));
            cbRrs.setSelected(options.get("rrs").equals("true") ? true : false);
            cbAuth.setSelected(options.get("auth").equals("true") ? true : false);
            cbAction[0].setSelected(regexps.contains("<action>") ? true : false);
            tfStartRtd[1].setText(options.get("start_rtd"));
            tfRtd[1].setText(options.get("rtd"));
            cbRepeatRtd[1].setSelected(options.get("repeat_rtd").equals("true") ? true : false);
            tfLost[1].setText(options.get("lost"));
            tfTimeout.setText(options.get("timeout"));
            tfOnTimeout.setText(options.get("ontimeout"));
            tfNext[1].setText(options.get("next"));
            tfTest[1].setText(options.get("test"));
            tfChance.setText(options.get("chance"));
            tfCounter[1].setText(options.get("counter"));
            cbRegexMatch.setSelected(options.get("regexp_match").equals("true") ? true : false);
            tfResponseTxn.setText(options.get("response_txn"));
            tfAddOptions[1].setText(addOptions);
        } else if (tagName.equals("label")) {
            tfID.setText(options.get("id"));
            tfAddOptions[5].setText(addOptions);
        } else if (tagName.equals("nop")) {
            tfStartRtd[2].setText(options.get("start_rtd"));
            cbAction[1].setSelected(regexps.contains("<action>") ? true : false);
            tfRtd[2].setText(options.get("rtd"));
            tfAddOptions[2].setText(addOptions);
        } else if (tagName.equals("sendCmd")) {
            tfDest.setText(options.get("dest"));
            tfAddOptions[3].setText(addOptions);
        } else if (tagName.endsWith("recvCmd")) {
            tfSrc.setText(options.get("src"));
            cbAction[2].setSelected(regexps.contains("<action>") ? true : false);
            tfAddOptions[4].setText(addOptions);
        } else if (tagName.endsWith("action")) {
            Vector<Integer> selectedIndices = new Vector<Integer>();
            for (String s : regexps) {
                Properties action = ((SippXMLConverter) parent).getProperties();
                int i = 0;
                for (Entry<Object, Object> entry : action.entrySet()) {
                    System.out.println("comparing '" + s + "' with '" + entry.getValue() + "'");
                    if (entry.getValue().equals(s)) selectedIndices.add(i);
                    i++;
                }
            }
            int[] indices = new int[selectedIndices.size()];
            int i = 0;
            for (Integer integer : selectedIndices.toArray(new Integer[selectedIndices.size()])) {
                indices[i] = integer;
                i++;
            }
            System.out.println(selectedIndices);
            liAction.setSelectedIndices(indices);
            tfAddOptions[8].setText(addOptions);
        } else {
            tfTagName.setText(tagName);
            tfOptions.setText(addOptions);
        }
    }

    private void getTagName() {
        tagName = line.replaceFirst("(^ *<)([a-zA-Z ]+)( \\w+=.*$)", "$2");
        tagName = tagName.replaceFirst("(^ *<)([a-zA-Z]+)( ?/?>.*$)", "$2");
    }

    private void getOptions() {
        if (tagName.equals("send")) {
            options.put("retrans", "");
            options.put("start_rtd", "");
            options.put("rtd", "");
            options.put("repeat_rtd", "");
            options.put("crlf", "");
            options.put("lost", "");
            options.put("next", "");
            options.put("test", "");
            options.put("counter", "");
            options.put("start_txn", "");
        }
        if (tagName.equals("recv")) {
            options.put("response", "");
            options.put("request", "");
            options.put("optional", "");
            options.put("crlf", "");
            options.put("rrs", "");
            options.put("auth", "");
            options.put("start_rtd", "");
            options.put("rtd", "");
            options.put("repeat_rtd", "");
            options.put("lost", "");
            options.put("timeout", "");
            options.put("ontimeout", "");
            options.put("action", "");
            options.put("test", "");
            options.put("next", "");
            options.put("chance", "");
            options.put("counter", "");
            options.put("regexp_match", "");
            options.put("response_txn", "");
        }
        if (tagName.equals("pause")) {
            options.put("milliseconds", "");
            options.put("variable", "");
            options.put("distribution", "");
            options.put("crlf", "");
            options.put("next", "");
            options.put("sanity_check", "");
            options.put("value", "");
            options.put("min", "");
            options.put("max", "");
            options.put("mean", "");
            options.put("stdev", "");
            options.put("lambda", "");
            options.put("k", "");
            options.put("x_m", "");
            options.put("theta", "");
            options.put("p", "");
            options.put("n", "");
        }
        if (tagName.equals("nop")) {
            options.put("action", "");
            options.put("start_rtd", "");
            options.put("rtd", "");
        }
        if (tagName.equals("sendCmd")) {
            options.put("dest", "");
        }
        if (tagName.equals("recvCmd")) {
            options.put("action", "");
            options.put("src", "");
        }
        if (tagName.equals("label")) {
            options.put("id", "");
        }
        if (tagName.equals("Response Time Repartition") || tagName.equals("Call Length Repartition")) {
            options.put("value", "");
        }
        StringTokenizer st = new StringTokenizer(line.replaceFirst("(^ *<\\w+ )(\\w[^/]*/?>)(.*$)", "$2"), "=\"");
        while (st.hasMoreTokens()) {
            String key = st.nextToken().trim();
            if (!Character.isLetter(key.toCharArray()[0])) continue;
            String value = st.nextToken().trim();
            if (options.containsKey(key)) options.put(key, value); else addOptions += key + "=\"" + value + "\"";
        }
    }
}
