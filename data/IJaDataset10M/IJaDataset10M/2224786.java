package org.xito.dialog;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.security.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * A Panel that represents the standard layout of a Dialog with a Title, subtitle, and message panel area
 *
 * @author Deane Richan
 */
public class DialogPanel extends JPanel {

    public static final String DEFAULT_BUTTON = "default.button";

    protected ArrayList buttons;

    protected TitlePanel titlePanel;

    protected DialogDescriptor descriptor;

    /** Creates a new instance of DialogPanel */
    public DialogPanel(DialogDescriptor desc) {
        descriptor = desc;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        initTitlePanel();
        initMessage();
        initBottom();
    }

    /**
    * Build the Title Panel
    */
    private void initTitlePanel() {
        if (descriptor.getTitle() == null) return;
        if (descriptor.getIconComp() == null && descriptor.getIcon() == null) {
            if (descriptor.getMessageType() == DialogManager.ERROR_MSG) {
                descriptor.setIcon(DialogManager.ERROR_ICON);
            }
            if (descriptor.getMessageType() == DialogManager.INFO_MSG) descriptor.setIcon(DialogManager.INFO_ICON);
            if (descriptor.getMessageType() == DialogManager.WARNING_MSG) descriptor.setIcon(DialogManager.WARNING_ICON);
        }
        titlePanel = new TitlePanel();
        setTitles(descriptor.getTitle(), descriptor.getSubtitle());
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titlePanel, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);
    }

    /**
    * Build the Message Area
    */
    private void initMessage() {
        if (descriptor.getCustomPanel() != null) {
            add(descriptor.getCustomPanel(), BorderLayout.CENTER);
            return;
        }
        if (descriptor.getException() != null) {
            initExceptionMessage();
            return;
        }
        add(createMessageLabel(), BorderLayout.CENTER);
    }

    /**
    * Set the Titles on this Dialog Panel
    * If title is null then the Title from the Descriptor will be used
    */
    public void setTitles(String title, String subtitle) {
        if (titlePanel == null) return;
        if (title == null) {
            title = descriptor.getTitle();
        }
        titlePanel.titleLbl.setText(title);
        if (subtitle != null) titlePanel.subtitleLbl.setText(subtitle);
    }

    /** 
    * Create a Label that contains the message
    */
    private JLabel createMessageLabel() {
        if (descriptor.getIcon() == null) {
            if (descriptor.getMessageType() == DialogManager.ERROR_MSG) descriptor.setIcon(DialogManager.ERROR_ICON);
            if (descriptor.getMessageType() == DialogManager.INFO_MSG) descriptor.setIcon(DialogManager.INFO_ICON);
            if (descriptor.getMessageType() == DialogManager.WARNING_MSG) descriptor.setIcon(DialogManager.WARNING_ICON);
        }
        MessageLabel msgLabel = new MessageLabel();
        msgLabel.setVerticalAlignment(JLabel.TOP);
        msgLabel.setText(descriptor.getMessage());
        msgLabel.setBorder(new EmptyBorder(15, 15, 15, 15));
        if (titlePanel == null && descriptor.getIcon() != null) {
            msgLabel.setIcon(descriptor.getIcon());
        }
        return msgLabel;
    }

    /**
    * Build the bottom panel with the buttons
    */
    private void initBottom() {
        if (descriptor.getType() == DialogManager.NONE) return;
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bottomPanel.add(btnPanel, BorderLayout.EAST);
        if (descriptor.getBottomPanel() != null) {
            bottomPanel.add(descriptor.getBottomPanel());
        }
        add(bottomPanel, BorderLayout.SOUTH);
        if (descriptor.getShowButtonSeparator()) {
            bottomPanel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.NORTH);
        }
        buttons = new ArrayList();
        ButtonType btypes[] = descriptor.getButtonTypes();
        if (btypes != null && btypes.length > 0) {
            boolean defaultSet = false;
            for (int i = 0; i < btypes.length; i++) {
                JButton btn = new JButton(btypes[i].name);
                btn.putClientProperty(DialogManager.RESULT_KEY, new Integer(btypes[i].value));
                addActionListeners(btn, descriptor.getActionListeners());
                btn.setDefaultCapable(false);
                btnPanel.add(btn);
                buttons.add(btn);
                if (btypes[i].defaultButton && !defaultSet) {
                    btn.setDefaultCapable(true);
                    btn.putClientProperty(DEFAULT_BUTTON, new Boolean(true));
                    defaultSet = true;
                }
            }
            return;
        }
        JButton cancelBtn = new JButton(DialogResources.bundle.getString("cancel.txt"));
        if (descriptor.getType() == DialogManager.OK || descriptor.getType() == DialogManager.OK_CANCEL) {
            JButton btn = new JButton(DialogResources.bundle.getString("ok.txt"));
            btn.setPreferredSize(cancelBtn.getPreferredSize());
            btn.putClientProperty(DialogManager.RESULT_KEY, new Integer(DialogManager.OK));
            btn.putClientProperty(DEFAULT_BUTTON, new Boolean(true));
            addActionListeners(btn, descriptor.getActionListeners());
            buttons.add(btn);
            btn.setDefaultCapable(true);
        } else if (descriptor.getType() == DialogManager.YES_NO || descriptor.getType() == DialogManager.YES_NO_CANCEL) {
            JButton btn = new JButton(DialogResources.bundle.getString("yes.txt"));
            btn.setPreferredSize(cancelBtn.getPreferredSize());
            btn.putClientProperty(DialogManager.RESULT_KEY, new Integer(DialogManager.YES));
            btn.putClientProperty(DEFAULT_BUTTON, new Boolean(true));
            addActionListeners(btn, descriptor.getActionListeners());
            buttons.add(btn);
            btn.setDefaultCapable(true);
            btn = new JButton(DialogResources.bundle.getString("no.txt"));
            btn.setPreferredSize(cancelBtn.getPreferredSize());
            btn.putClientProperty(DialogManager.RESULT_KEY, new Integer(DialogManager.NO));
            addActionListeners(btn, descriptor.getActionListeners());
            buttons.add(btn);
        }
        if (descriptor.getType() == DialogManager.OK_CANCEL || descriptor.getType() == DialogManager.CANCEL || descriptor.getType() == DialogManager.YES_NO_CANCEL) {
            cancelBtn.putClientProperty(DialogManager.RESULT_KEY, new Integer(DialogManager.CANCEL));
            addActionListeners(cancelBtn, descriptor.getActionListeners());
            buttons.add(cancelBtn);
        }
        String os = System.getProperty("os.name");
        if (os.startsWith("Mac OS")) {
            for (int i = buttons.size(); i > 0; i--) {
                btnPanel.add((JButton) buttons.get(i - 1));
            }
        } else {
            for (int i = 0; i < buttons.size(); i++) {
                btnPanel.add((JButton) buttons.get(i));
            }
        }
    }

    public void initDefaultButton() {
        if (buttons == null) return;
        Iterator it = buttons.iterator();
        while (it.hasNext()) {
            JButton btn = (JButton) it.next();
            Boolean isDefault = (Boolean) btn.getClientProperty(DEFAULT_BUTTON);
            if (isDefault != null && isDefault.booleanValue()) {
                getRootPane().setDefaultButton(btn);
                break;
            }
        }
    }

    /**
    * Add collection of action listeners to a btn
    */
    private void addActionListeners(JButton btn, Collection listeners) {
        if (listeners != null) {
            Iterator it = descriptor.getActionListeners().iterator();
            while (it.hasNext()) {
                btn.addActionListener((ActionListener) it.next());
            }
        }
    }

    /**
    * Build an Exception Message Panel with Details
    */
    private void initExceptionMessage() {
        if (descriptor.getException() == null) {
            add(createMessageLabel(), BorderLayout.CENTER);
            return;
        }
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Description", createMessageLabel());
        JTextArea expTA = new JTextArea();
        expTA.setEditable(false);
        JScrollPane sp = new JScrollPane(expTA);
        Throwable t = descriptor.getException();
        expTA.append(t.getClass().getName() + ":");
        expTA.append((t.getMessage() != null ? t.getMessage() : ""));
        expTA.append("\n");
        while (t != null) {
            StackTraceElement st[] = t.getStackTrace();
            for (int i = 0; i < st.length; i++) {
                expTA.append("    at " + st[i].toString() + "\n");
            }
            t = t.getCause();
            if (t != null) {
                expTA.append("\nCaused By:" + t.getClass().getName() + ":");
                expTA.append((t.getMessage() != null ? t.getMessage() : ""));
                expTA.append("\n");
            }
        }
        tabs.addTab("Details", sp);
        tabs.setBorder(new EmptyBorder(5, 5, 5, 5));
        Dimension ps = tabs.getSize();
        if (ps.width < 300 || ps.height < 300) {
            ps = new Dimension(300, 300);
            tabs.setPreferredSize(ps);
        }
        add(tabs, BorderLayout.CENTER);
    }

    /*************************************************
    * Title Panel
    ************************************************/
    private class TitlePanel extends JPanel {

        private final Insets ICON_BORDER = new Insets(10, 10, 10, 10);

        JPanel textTitlePanel;

        Color gradientColor;

        float gradientOffsetRatio;

        JLabel titleLbl;

        JLabel subtitleLbl;

        JLabel iconLbl;

        public TitlePanel() {
            gradientColor = DialogPanel.this.descriptor.getGradiantColor();
            gradientOffsetRatio = DialogPanel.this.descriptor.getGradiantOffsetRatio();
            setLayout(new BorderLayout());
            textTitlePanel = new JPanel();
            textTitlePanel.setOpaque(false);
            add(textTitlePanel);
            setOpaque(true);
            setBackground(Color.white);
            AccessController.doPrivileged(new PrivilegedAction() {

                public Object run() {
                    textTitlePanel.setLayout(new TableLayout(DialogPanel.class.getResource("title_layout.html")));
                    return null;
                }
            });
            titleLbl = new JLabel();
            titleLbl.setFont(titleLbl.getFont().deriveFont(Font.BOLD));
            textTitlePanel.add("title", titleLbl);
            subtitleLbl = new JLabel();
            subtitleLbl.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            subtitleLbl.setForeground(Color.GRAY);
            textTitlePanel.add("subtitle", subtitleLbl);
            if (DialogPanel.this.descriptor.getIconComp() != null) {
                DialogPanel.this.descriptor.getIconComp().setBorder(new EmptyBorder(ICON_BORDER));
                add(DialogPanel.this.descriptor.getIconComp(), BorderLayout.EAST);
            } else if (DialogPanel.this.descriptor.getIcon() != null) {
                iconLbl = new JLabel();
                iconLbl.setIcon(DialogPanel.this.descriptor.getIcon());
                iconLbl.setBorder(new EmptyBorder(ICON_BORDER));
                add(iconLbl, BorderLayout.EAST);
            }
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2D = (Graphics2D) g;
            g2D.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
            g2D.setColor(Color.WHITE);
            g2D.fillRect(0, 0, getWidth(), getHeight());
            if (gradientColor != null) {
                Color c1 = new Color(0, 0, 0, 0);
                int offset = (int) (getWidth() * gradientOffsetRatio);
                GradientPaint gp = new GradientPaint(getWidth() - offset, 0, c1, getWidth(), 0, gradientColor);
                g2D.setPaint(gp);
                g2D.fillRect(0, 0, getWidth(), getHeight());
            }
            g2D.setColor(new Color(200, 200, 200));
            g2D.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        }
    }

    /**
    * Message Label Used to Paint Message. Uses Anti-Aliasing
    */
    private class MessageLabel extends JLabel {

        public MessageLabel() {
            super();
            setVerticalTextPosition(SwingConstants.TOP);
        }

        public void paint(Graphics g) {
            super.paint(g);
        }
    }
}
