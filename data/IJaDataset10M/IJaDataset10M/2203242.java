package uk.co.westhawk.examplev1;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import uk.co.westhawk.snmp.beans.*;
import uk.co.westhawk.snmp.pdu.*;
import uk.co.westhawk.snmp.stack.*;
import java.beans.*;

/**
 * <p>
 * The class OneNTSharedRes 
 * displays the information that is available from the bean.
 * </p>
 *
 * <p>
 * This class is used by testNTSharedResBean class. It uses the
 * OneNTSharedResBean to get its information.
 * </p>
 *
 * @see testNTSharedResBean
 * @see uk.co.westhawk.snmp.beans.OneNTSharedResBean
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 1.3 $ $Date: 2006/01/17 17:43:55 $
 */
public class OneNTSharedRes extends JDialog implements ActionListener, PropertyChangeListener {

    private static final String version_id = "@(#)$Id: OneNTSharedRes.java,v 1.3 2006/01/17 17:43:55 birgit Exp $ Copyright Westhawk Ltd";

    JPanel panel1 = new JPanel();

    GridBagLayout gridBagLayout1 = new GridBagLayout();

    JLabel label1 = new JLabel(" ");

    JLabel label2 = new JLabel(" ");

    JLabel label3 = new JLabel(" ");

    JLabel nameLabel = new JLabel(" ");

    JLabel pathLabel = new JLabel(" ");

    JLabel commentLabel = new JLabel(" ");

    JButton cancelButton = new JButton();

    uk.co.westhawk.snmp.beans.OneNTSharedResBean oneNTSharedResBean = new uk.co.westhawk.snmp.beans.OneNTSharedResBean();

    String host, port, community, interval;

    String index;

    /**
     * Constructor
     *
     * @param h the hostname
     * @param p the port no
     * @param com the community name
     * @param ind the index
     * @param ival the update interval
     *
     */
    public OneNTSharedRes(JFrame fr, String h, String p, String com, String ind, String ival) {
        super(fr, "One NT Resource", false);
        host = h;
        port = p;
        community = com;
        index = ind;
        interval = ival;
        this.setTitle("Host " + host);
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.setVisible(true);
        this.setSize(400, 150);
        oneNTSharedResBean.setHost(host);
        oneNTSharedResBean.setPort(port);
        oneNTSharedResBean.setCommunityName(community);
        oneNTSharedResBean.setUpdateInterval(interval);
        oneNTSharedResBean.setIndex(index);
        oneNTSharedResBean.action();
    }

    void jbInit() throws Exception {
        panel1.setLayout(gridBagLayout1);
        label1.setText(" Name ");
        label2.setText(" Path ");
        label3.setText(" Comment ");
        nameLabel.setBackground(Color.white);
        pathLabel.setBackground(Color.white);
        commentLabel.setBackground(Color.white);
        nameLabel.setOpaque(true);
        pathLabel.setOpaque(true);
        commentLabel.setOpaque(true);
        oneNTSharedResBean.addPropertyChangeListener(this);
        cancelButton.setText(" Cancel");
        cancelButton.addActionListener(this);
        this.getContentPane().add(panel1);
        panel1.add(label1, getGridBagConstraints2(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        panel1.add(label2, getGridBagConstraints2(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        panel1.add(label3, getGridBagConstraints2(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        panel1.add(nameLabel, getGridBagConstraints2(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        panel1.add(pathLabel, getGridBagConstraints2(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        panel1.add(commentLabel, getGridBagConstraints2(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        panel1.add(cancelButton, getGridBagConstraints2(0, 3, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
    }

    public void propertyChange(PropertyChangeEvent e) {
        oneNTSharedResBean_propertyChange(e);
    }

    public void actionPerformed(ActionEvent e) {
        cancelButton_actionPerformed(e);
    }

    void cancelButton_actionPerformed(ActionEvent e) {
        this.setVisible(false);
        oneNTSharedResBean.setRunning(false);
    }

    void oneNTSharedResBean_propertyChange(PropertyChangeEvent e) {
        boolean b;
        String can;
        nameLabel.setText(oneNTSharedResBean.getName());
        pathLabel.setText(oneNTSharedResBean.getPath());
        commentLabel.setText(oneNTSharedResBean.getComment() + " ");
    }

    GridBagConstraints getGridBagConstraints2(int x, int y, int w, int h, double wx, double wy, int anchor, int fill, Insets ins, int ix, int iy) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = x;
        gc.gridy = y;
        gc.gridwidth = w;
        gc.gridheight = h;
        gc.weightx = wx;
        gc.weighty = wy;
        gc.anchor = anchor;
        gc.fill = fill;
        gc.insets = ins;
        gc.ipadx = ix;
        gc.ipady = iy;
        return gc;
    }
}
