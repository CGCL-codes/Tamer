package de.cinek.rssview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import de.cinek.rssview.images.IconContainer;
import de.cinek.rssview.images.IconSet;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.parsers.FeedParser;

public class RssChannelDialog extends RssDialog {

    public static final int YES_OPTION = 0;

    public static final int CANCEL_OPTION = 1;

    public static final int SECURE_INTERVAL = 15;

    private JTextField tf_name;

    private JTextField tf_url;

    private JTextField tf_articles;

    private JTextField tf_pollinterval;

    private JCheckBox cb_beep;

    private JCheckBox cb_remember;

    private JCheckBox cb_active;

    private RssChannel channel;

    private int dialogResult = CANCEL_OPTION;

    private static final ResourceBundle rb = ResourceBundle.getBundle("rssview");

    protected RssChannelDialog(RssView parent) {
        this(parent, rb.getString("Add_a_new_channel"), null, null);
    }

    protected RssChannelDialog(RssView parent, String url) {
        this(parent, rb.getString("Add_a_new_channel"), null, url);
    }

    protected RssChannelDialog(RssView parent, RssChannel channel) {
        this(parent, rb.getString("Modify_a_channel"), channel, null);
    }

    protected RssChannelDialog(RssView parent, String title, RssChannel channel, String url) {
        super(parent, title, true);
        this.channel = channel;
        initComponents(url);
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents(String url) {
        setResizable(false);
        Insets defaultInsets = new Insets(2, 2, 2, 2);
        getContentPane().setLayout(new BorderLayout());
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.insets = new Insets(2, 10, 2, 2);
        ;
        gbcLeft.anchor = GridBagConstraints.EAST;
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.insets = defaultInsets;
        gbcRight.anchor = GridBagConstraints.WEST;
        gbcRight.gridwidth = GridBagConstraints.REMAINDER;
        JPanel widgetpanel = new JPanel(new GridBagLayout());
        getContentPane().add(widgetpanel, BorderLayout.CENTER);
        ImageIcon icon;
        if (channel == null) {
            icon = IconContainer.getIconSet().getIcon(IconSet.ICON_NEW_CHANNEL_LARGE);
        } else {
            icon = IconContainer.getIconSet().getIcon(IconSet.ICON_EDIT_CHANNEL_LARGE);
        }
        JLabel imglabel = new JLabel(icon);
        imglabel.setOpaque(true);
        imglabel.setText(getTitle());
        imglabel.setHorizontalAlignment(JLabel.LEFT);
        imglabel.setBackground(Color.WHITE);
        gbcRight.weightx = 1.0;
        gbcRight.fill = GridBagConstraints.HORIZONTAL;
        gbcRight.insets = new Insets(0, 0, 10, 0);
        widgetpanel.add(imglabel, gbcRight);
        gbcRight.weightx = 0.0;
        gbcRight.fill = GridBagConstraints.NONE;
        gbcRight.insets = defaultInsets;
        JLabel lab = new JLabel(rb.getString("Channel_URL"));
        widgetpanel.add(lab, gbcLeft);
        gbcRight.gridwidth = 2;
        tf_url = (url == null) ? new JTextField(35) : new JTextField(url, 35);
        widgetpanel.add(tf_url, gbcRight);
        gbcRight.gridwidth = GridBagConstraints.REMAINDER;
        widgetpanel.add(new JButton(new CheckAction()), gbcRight);
        lab = new JLabel(rb.getString("Channel_name"));
        widgetpanel.add(lab, gbcLeft);
        tf_name = new JTextField(20);
        widgetpanel.add(tf_name, gbcRight);
        lab = new JLabel(rb.getString("View_articles"));
        widgetpanel.add(lab, gbcLeft);
        tf_articles = new JTextField(String.valueOf(RssSettings.DEFAULT_ARTICLESCOUNT), 3);
        widgetpanel.add(tf_articles, gbcRight);
        lab = new JLabel(rb.getString("Poll_interval"));
        widgetpanel.add(lab, gbcLeft);
        gbcRight.gridwidth = 1;
        tf_pollinterval = new JTextField(String.valueOf(RssSettings.DEFAULT_POLLINTERVAL / 60), 3);
        widgetpanel.add(tf_pollinterval, gbcRight);
        gbcRight.gridwidth = GridBagConstraints.REMAINDER;
        widgetpanel.add(new JLabel(rb.getString("minutes")), gbcRight);
        widgetpanel.add(new JLabel(rb.getString("Beep_on_new_article")), gbcLeft);
        cb_beep = new JCheckBox("", RssSettings.DEFAULT_BEEP);
        cb_beep.setHorizontalTextPosition(SwingConstants.LEFT);
        widgetpanel.add(cb_beep, gbcRight);
        widgetpanel.add(new JLabel(rb.getString("Remember_items")), gbcLeft);
        cb_remember = new JCheckBox("", RssSettings.DEFAULT_REMEMBER);
        cb_remember.setHorizontalTextPosition(SwingConstants.LEFT);
        widgetpanel.add(cb_remember, gbcRight);
        widgetpanel.add(new JLabel(rb.getString("Channel_active")), gbcLeft);
        cb_active = new JCheckBox("", true);
        cb_active.setHorizontalTextPosition(SwingConstants.LEFT);
        widgetpanel.add(cb_active, gbcRight);
        if (channel != null) {
            tf_name.setText(channel.getName());
            tf_url.setText(channel.getUrl());
            tf_articles.setText(String.valueOf(channel.getArticlesInView()));
            tf_pollinterval.setText(String.valueOf(channel.getPollInterval() / 60));
            cb_beep.setSelected(channel.isBeepEnabled());
            cb_remember.setSelected(channel.isRememberArticlesEnabled());
            cb_active.setSelected(channel.isActive());
        }
        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        String name = (channel == null) ? rb.getString("Add") : rb.getString("Modify");
        UpdateValuesAction yesAction = new UpdateValuesAction(this, name);
        buttonpanel.add(new JButton(yesAction));
        getContentPane().add(buttonpanel, BorderLayout.SOUTH);
        this.tf_name.setAction(yesAction);
        this.tf_articles.setAction(yesAction);
        this.tf_pollinterval.setAction(yesAction);
        this.tf_url.setAction(yesAction);
        buttonpanel.add(new JButton(new CancelAction()));
    }

    private String getChannelTitle(String url) {
        ChannelBuilder builder = new ChannelBuilder();
        try {
            builder.beginTransaction();
            ChannelIF parsedChannel = FeedParser.parse(builder, url);
            builder.endTransaction();
            return parsedChannel.getTitle();
        } catch (Exception ex) {
        }
        return null;
    }

    public int getDialogResult() {
        return this.dialogResult;
    }

    public static RssChannelDialog showDialog(RssView parent) {
        RssChannelDialog dialog = new RssChannelDialog(parent);
        dialog.show();
        return dialog;
    }

    public static RssChannelDialog showDialog(RssView parent, RssChannel channel) {
        RssChannelDialog dialog = new RssChannelDialog(parent, channel);
        dialog.show();
        return dialog;
    }

    public static RssChannelDialog showDialog(RssView parent, String url) {
        RssChannelDialog dialog = new RssChannelDialog(parent, url);
        dialog.show();
        return dialog;
    }

    private class CheckAction extends AbstractAction {

        public CheckAction() {
            super(rb.getString("check_url"));
        }

        public void actionPerformed(ActionEvent e) {
            String channelName = new String();
            channelName = getChannelTitle(tf_url.getText());
            if (channelName != null) {
                tf_name.setText(channelName);
            } else {
                tf_url.selectAll();
            }
        }
    }

    /**
	 * Getter for property subscription.
	 * @return Value of property subscription.
	 */
    public Channel getChannel() {
        return channel;
    }

    private class CancelAction extends AbstractAction {

        public CancelAction() {
            super(rb.getString("Cancel"));
        }

        public void actionPerformed(ActionEvent e) {
            dialogResult = CANCEL_OPTION;
            dispose();
        }
    }

    private class UpdateValuesAction extends AbstractAction {

        JDialog parent;

        public UpdateValuesAction(JDialog parent, String name) {
            super(name);
            this.parent = parent;
        }

        public void actionPerformed(ActionEvent e) {
            String name = tf_name.getText().trim();
            String url = tf_url.getText().trim();
            if (name.length() == 0 || url.length() == 0) {
                JOptionPane.showMessageDialog(parent, rb.getString("give_channel_and_URL"), rb.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            int articlecount = 0;
            int pollinterval = 0;
            boolean beep = cb_beep.isSelected();
            boolean remember = cb_remember.isSelected();
            boolean active = cb_active.isSelected();
            try {
                articlecount = Integer.parseInt(tf_articles.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, rb.getString("error_parssing_number"), rb.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                pollinterval = Integer.parseInt(tf_pollinterval.getText()) * 60;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, rb.getString("Could_not_parse_the_poll_interval"), rb.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (pollinterval == 0) {
                if (JOptionPane.showConfirmDialog(parent, rb.getString("poll_interval_0") + rb.getString("fetch_warning"), rb.getString("Poll_interval_warning"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) return;
            } else {
                if (pollinterval < SECURE_INTERVAL * 60) {
                    if (JOptionPane.showConfirmDialog(parent, rb.getString("small_interval") + rb.getString("block_warning"), rb.getString("Poll_interval_warning"), JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
                }
            }
            if (channel == null) {
                channel = new RssChannel();
            }
            channel.setName(name);
            channel.setUrl(url);
            channel.setArticlesInView(articlecount);
            channel.setPollInterval(pollinterval);
            channel.setBeepEnabled(beep);
            channel.setActive(active);
            channel.setRememberArticlesEnabled(remember);
            dialogResult = YES_OPTION;
            dispose();
        }
    }
}
