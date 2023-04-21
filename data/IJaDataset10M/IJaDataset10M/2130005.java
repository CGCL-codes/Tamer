package com.elibera.ccs.panel.std;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.elibera.ccs.dialog.DialogImageList;
import com.elibera.ccs.dialog.DialogImageUploadLocalImage;
import com.elibera.ccs.img.HelperOp;
import com.elibera.ccs.img.MLEImage;
import com.elibera.ccs.panel.HelperPanel;
import com.elibera.ccs.parser.InterfaceDocContainer;
import com.elibera.ccs.res.Msg;
import com.elibera.ccs.tagdata.DataRootEdit;
import com.elibera.ccs.tagdata.InterfaceDataDisplayType;

/**
 * @author meisi
 *
 */
public class PanelImageDisplayEdit extends JPanel {

    public InterfaceDataDisplayType data;

    private JButton jButton = null;

    private JButton jButton1 = null;

    private JButton jButton2 = null;

    private JButton jButton3 = null;

    private JPanel jPanel = null;

    private JPanel jPanel1 = null;

    private JScrollPane jScrollPane = null;

    private JLabel imageLabel = new JLabel();

    public InterfaceDocContainer container;

    private boolean showWord = true;

    private boolean showInternal = true;

    /**
	 * This is the default constructor
	 */
    public PanelImageDisplayEdit(InterfaceDataDisplayType data, InterfaceDocContainer container, boolean showWord, boolean showInternal) {
        super();
        this.container = container;
        this.showInternal = showInternal;
        this.showWord = showWord;
        initialize();
        init(data);
    }

    private void init(InterfaceDataDisplayType data) {
        getJButton2().setVisible(showInternal);
        getJButton3().setVisible(showWord);
        this.data = data;
        imageLabel.setIcon(null);
        if (data.getImage() != null) imageLabel.setIcon(new ImageIcon(data.getImage()));
        imageLabel.setText(data.getWord());
    }

    public void reset(InterfaceDataDisplayType data, InterfaceDocContainer container, boolean showWord, boolean showInternal) {
        this.container = container;
        this.showInternal = showInternal;
        this.showWord = showWord;
        init(data);
    }

    private void setImage(MLEImage img) {
        if (img == null || img.id == null || img.getImage() == null) return;
        data.storeDisplay(img.id, "l", img.getImage());
        if (data.getImage() != null) {
            imageLabel.setIcon(new ImageIcon(data.getImage()));
            data.doUpdateButton();
        }
    }

    private void setWord(String text) {
        data.setWord(text);
        imageLabel.setText(text);
        data.doUpdateButton();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.setRows(1);
        this.setLayout(gridLayout);
        HelperPanel.formatPanel(this);
        this.add(getJPanel(), null);
        this.add(getJPanel1(), null);
    }

    /**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            HelperPanel.formatButton(jButton);
            jButton.setText(Msg.getString("PanelImageDisplayEdit.BUTTON_TITEL_UPLOAD_NEW_IMAGE"));
            jButton.setToolTipText(Msg.getString("PanelImageDisplayEdit.BUTTON_TOOLTIP_UPLOAD_NEW_IMAGE"));
            jButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String imageAbsolutePath = HelperOp.getImageWithFileChoose(null, ((DataRootEdit) data).dialog);
                    if (imageAbsolutePath != null) {
                        MLEImage img = DialogImageUploadLocalImage.showDialog(container, imageAbsolutePath, false);
                        setImage(img);
                    }
                }
            });
        }
        return jButton;
    }

    /**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButton1() {
        if (jButton1 == null) {
            jButton1 = new JButton();
            HelperPanel.formatButton(jButton1);
            jButton1.setText(Msg.getString("PanelImageDisplayEdit.BUTTON_TITEL_CHOOSE_EXISTING_IMAGE"));
            jButton1.setToolTipText(Msg.getString("PanelImageDisplayEdit.BUTTON_TOOLTIP_CHOOSE_EXISTING_IMAGE"));
            jButton1.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setImage(DialogImageList.showDialog(container, container.getBinaryContainer().getAllImages(), 2, false));
                }
            });
        }
        return jButton1;
    }

    /**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButton2() {
        if (jButton2 == null) {
            jButton2 = new JButton();
            HelperPanel.formatButton(jButton2);
            jButton2.setText(Msg.getString("PanelImageDisplayEdit.BUTTON_TITEL_CHOOSE_INTERNAL_IMAGE"));
            jButton2.setToolTipText(Msg.getString("PanelImageDisplayEdit.BUTTON_TOOLTIP_CHOOSE_INTERNAL_IMAGE"));
            jButton2.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setImage(DialogImageList.showDialog(container, container.getBinaryContainer().getAllInternalImages(), 8, false));
                }
            });
        }
        return jButton2;
    }

    /**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButton3() {
        if (jButton3 == null) {
            jButton3 = new JButton();
            HelperPanel.formatButton(jButton3);
            jButton3.setText(Msg.getString("PanelImageDisplayEdit.BUTTON_TITEL_SET_WORD_AS_IMAGE"));
            jButton3.setToolTipText(Msg.getString("PanelImageDisplayEdit.BUTTON_TOOLTIP_SET_WORD_AS_IMAGE"));
            jButton3.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setWord(JOptionPane.showInputDialog(JOptionPane.getFrameForComponent(jPanel), Msg.getString("PanelImageDisplayEdit.BUTTON_SET_WORD_AS_IMAGE_DIALOG_INFO"), Msg.getString("PanelImageDisplayEdit.BUTTON_SET_WORD_AS_IMAGE_DIALOG_TITEL"), JOptionPane.PLAIN_MESSAGE));
                }
            });
        }
        return jButton3;
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            HelperPanel.formatPanelWithBorder(jPanel, Msg.getString("PanelImageDisplayEdit.BORDER_TITEL_ACTIONS"));
            jPanel.setLayout(new BoxLayout(getJPanel(), BoxLayout.Y_AXIS));
            jPanel.add(getJButton(), null);
            jPanel.add(Box.createRigidArea(new Dimension(5, 5)));
            jPanel.add(getJButton1(), null);
            jPanel.add(Box.createRigidArea(new Dimension(5, 5)));
            jPanel.add(getJButton2(), null);
            jPanel.add(Box.createRigidArea(new Dimension(5, 5)));
            jPanel.add(getJButton3(), null);
            jPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        }
        return jPanel;
    }

    /**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            GridLayout gridLayout1 = new GridLayout();
            gridLayout1.setRows(1);
            jPanel1 = new JPanel();
            jPanel1.setLayout(gridLayout1);
            HelperPanel.formatPanelWithBorder(jPanel1, Msg.getString("PanelImageDisplayEdit.BORDER_TITEL_PREVIEW"));
            jPanel1.add(getJScrollPane(), null);
        }
        return jPanel1;
    }

    /**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            imageLabel.setText("");
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(imageLabel);
        }
        return jScrollPane;
    }
}
