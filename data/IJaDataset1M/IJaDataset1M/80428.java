package cn.houseout.snapscreen.conf;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Properties;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class GlobeConfDialog extends JDialog {

    private static final long serialVersionUID = -6045519255059445428L;

    private JButton okBtn;

    private JButton cancelBtn;

    private JPanel configPane;

    private String imageFormat = GlobeProperties.JPEG;

    private String snapMethod = GlobeProperties.DRAG;

    private JTextField storeDirField;

    private JTextField imagePrefixField;

    private JTextField imageWidthField;

    private JTextField imageHeightField;

    private JRadioButton dragRBtn = null;

    private JRadioButton fixedRBtn = null;

    private JRadioButton jpgRBtn = null;

    private JRadioButton pngRBtn = null;

    private JCheckBox hideDialogCheckBox = null;

    public GlobeConfDialog(JFrame parent) {
        super(parent, "Globe config");
        JPanel btnPanel = new JPanel();
        btnPanel.add(getOkBtn());
        btnPanel.add(getCancelBtn());
        getOkBtn().setPreferredSize(getCancelBtn().getPreferredSize());
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(btnPanel, BorderLayout.SOUTH);
        contentPanel.add(getConfigPane(), BorderLayout.CENTER);
        loadData();
        getContentPane().add(contentPanel);
        setSize(650, 300);
        centerToScreen();
    }

    private void loadData() {
        getImageHeightField().setText(GlobeProperties.getInstance().getImageHeight() + "");
        getImageWidthField().setText(GlobeProperties.getInstance().getImageWidth() + "");
        getImagePrefixField().setText(GlobeProperties.getInstance().getImagePrefix());
        getStoreDirField().setText(GlobeProperties.getInstance().getStoreDir());
        String imageFormat = GlobeProperties.getInstance().getImageFormat();
        if (imageFormat != null && imageFormat.equals(GlobeProperties.PNG)) {
            pngRBtn.setSelected(true);
        } else {
            jpgRBtn.setSelected(true);
        }
        String snapMethod = GlobeProperties.getInstance().getSnapMethod();
        if (snapMethod != null && snapMethod.equals(GlobeProperties.FIXED)) {
            fixedRBtn.setSelected(true);
        } else {
            dragRBtn.setSelected(true);
        }
        boolean hideDialog = GlobeProperties.getInstance().isHideDialog();
        this.hideDialogCheckBox.setSelected(hideDialog);
    }

    public JPanel getConfigPane() {
        if (configPane == null) {
            configPane = new JPanel(new GridBagLayout());
            GridBagConstraints g = new GridBagConstraints();
            g.insets = new Insets(2, 2, 2, 2);
            g.anchor = GridBagConstraints.EAST;
            g.weightx = 0;
            g.gridx = 0;
            g.gridy = 0;
            configPane.add(new JLabel("image store format:"), g);
            g.gridy = 1;
            configPane.add(new JLabel("image prefix:"), g);
            g.gridy = 2;
            configPane.add(new JLabel("image store directory:"), g);
            g.gridy = 3;
            g.gridwidth = 2;
            g.weightx = 1;
            g.fill = GridBagConstraints.HORIZONTAL;
            configPane.add(getSnapMethodPane(), g);
            g.gridy = 4;
            hideDialogCheckBox = new JCheckBox("hide dialog when snapping");
            configPane.add(hideDialogCheckBox, g);
            g.weightx = 1;
            g.gridx = 1;
            g.gridy = 0;
            g.fill = GridBagConstraints.BOTH;
            configPane.add(getImageFormatPane(), g);
            g.gridy = 1;
            configPane.add(getImagePrefixField(), g);
            g.gridy = 2;
            configPane.add(getImageStoreDirPane(), g);
            g.gridy = 3;
        }
        return configPane;
    }

    public JPanel getImageFormatPane() {
        JPanel imageFormatPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jpgRBtn = new JRadioButton(GlobeProperties.JPEG);
        jpgRBtn.setSelected(true);
        jpgRBtn.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    imageFormat = GlobeProperties.JPEG;
                }
            }
        });
        pngRBtn = new JRadioButton(GlobeProperties.PNG);
        pngRBtn.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    imageFormat = GlobeProperties.PNG;
                }
            }
        });
        ButtonGroup group = new ButtonGroup();
        group.add(jpgRBtn);
        group.add(pngRBtn);
        imageFormatPane.add(jpgRBtn);
        imageFormatPane.add(pngRBtn);
        return imageFormatPane;
    }

    public JPanel getImageStoreDirPane() {
        JPanel imageFormatPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton selectBtn = new JButton("select...");
        selectBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectDir();
            }
        });
        imageFormatPane.add(getStoreDirField());
        imageFormatPane.add(selectBtn);
        return imageFormatPane;
    }

    protected void selectDir() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int answer = chooser.showOpenDialog(this);
        if (answer == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath() + System.getProperty("file.separator");
            getStoreDirField().setText(path);
        }
    }

    public JPanel getSnapMethodPane() {
        dragRBtn = new JRadioButton(GlobeProperties.DRAG);
        dragRBtn.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    snapMethod = GlobeProperties.DRAG;
                    getImageWidthField().setEditable(false);
                    getImageHeightField().setEditable(false);
                }
            }
        });
        dragRBtn.setSelected(true);
        fixedRBtn = new JRadioButton(GlobeProperties.FIXED);
        fixedRBtn.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    snapMethod = GlobeProperties.FIXED;
                    getImageWidthField().setEditable(true);
                    getImageHeightField().setEditable(true);
                    if (getImageWidthField().getText().trim().length() == 0) {
                        getImageWidthField().setText("200");
                    }
                    if (getImageHeightField().getText().trim().length() == 0) {
                        getImageHeightField().setText("100");
                    }
                }
            }
        });
        ButtonGroup group = new ButtonGroup();
        group.add(dragRBtn);
        group.add(fixedRBtn);
        JPanel fixedPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fixedPane.add(fixedRBtn);
        fixedPane.add(new JLabel("width (pixels)"));
        fixedPane.add(getImageWidthField());
        fixedPane.add(new JLabel("height (pixels)"));
        fixedPane.add(getImageHeightField());
        JPanel snapMethodPane = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(2, 2, 2, 2);
        g.anchor = GridBagConstraints.WEST;
        g.weightx = 0;
        g.gridx = 0;
        g.gridy = 0;
        snapMethodPane.add(dragRBtn, g);
        g.gridy = 1;
        snapMethodPane.add(fixedPane, g);
        snapMethodPane.setBorder(new TitledBorder("snap method"));
        return snapMethodPane;
    }

    public JButton getOkBtn() {
        if (okBtn == null) {
            okBtn = new JButton("ok");
            okBtn.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    okAction();
                }
            });
        }
        return okBtn;
    }

    protected void okAction() {
        Properties p = new Properties();
        p.put(GlobeProperties.IMAGE_FORMAT_KEY, imageFormat);
        p.put(GlobeProperties.SNAP_METHOD_KEY, snapMethod);
        p.put(GlobeProperties.IMAGE_WIDTH_KEY, getImageWidthField().getText().trim());
        p.put(GlobeProperties.IMAGE_HEIGHT_KEY, getImageHeightField().getText().trim());
        String storeDir = getStoreDirField().getText().trim();
        p.put(GlobeProperties.STORE_DIR_KEY, storeDir.length() == 0 ? "." : storeDir);
        String imagePrefix = getImagePrefixField().getText().trim();
        p.put(GlobeProperties.IMAGE_PREFIX, imagePrefix.length() == 0 ? GlobeProperties.PREFIX : imagePrefix);
        p.put(GlobeProperties.HIDE_DIALOG, hideDialogCheckBox.isSelected() + "");
        GlobeProperties.getInstance().save(p);
        exitAction();
    }

    public JButton getCancelBtn() {
        if (cancelBtn == null) {
            cancelBtn = new JButton("cancel");
            cancelBtn.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    exitAction();
                }
            });
        }
        return cancelBtn;
    }

    private void exitAction() {
        this.dispose();
        this.setVisible(false);
    }

    public JTextField getImagePrefixField() {
        if (imagePrefixField == null) {
            imagePrefixField = new JTextField(24);
        }
        return imagePrefixField;
    }

    public JTextField getStoreDirField() {
        if (storeDirField == null) {
            storeDirField = new JTextField(24);
        }
        return storeDirField;
    }

    public JTextField getImageWidthField() {
        if (imageWidthField == null) {
            imageWidthField = new JTextField(6);
        }
        return imageWidthField;
    }

    public JTextField getImageHeightField() {
        if (imageHeightField == null) {
            imageHeightField = new JTextField(6);
        }
        return imageHeightField;
    }

    private void centerToScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension componentSize = getSize();
        if (componentSize.height > screenSize.height) {
            componentSize.height = screenSize.height;
        }
        if (componentSize.width > screenSize.width) {
            componentSize.width = screenSize.width;
        }
        setLocation((screenSize.width - componentSize.width) / 2, (screenSize.height - componentSize.height) / 2);
    }
}
