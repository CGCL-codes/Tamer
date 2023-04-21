package net.sealisland.swing.splash;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.Timer;
import net.sealisland.swing.action.ApplicationResources;
import net.sealisland.swing.action.ComponentAction;
import net.sealisland.swing.dialog.AbstractDialog;

public class AboutDialog extends AbstractDialog {

    private Timer timer;

    private JLabel iconLabel;

    private JTextPane textPane;

    private JViewport viewport;

    public AboutDialog(JFrame frame) {
        super(frame);
        setResizable(false);
        timer = new Timer(40, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateScroll();
            }
        });
        timer.setCoalesce(true);
        createComponents();
    }

    private void createComponents() {
        ApplicationResources resources = ApplicationResources.getDefaultInstance();
        setTitle(resources.getString("AboutDialog.title"));
        iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        iconLabel.setIcon(resources.getIcon("AboutDialog.icon"));
        textPane = new JTextPane();
        textPane.setOpaque(false);
        textPane.setContentType("text/html");
        textPane.setText(resources.getString("AboutDialog.text"));
        viewport = new JViewport();
        viewport.setView(textPane);
        viewport.setScrollMode(JViewport.BLIT_SCROLL_MODE);
        viewport.setPreferredSize(new Dimension(320, 160));
        getContentPane().add(iconLabel, BorderLayout.NORTH);
        getContentPane().add(viewport, BorderLayout.CENTER);
        buttonPanel.add(new JButton(resources.configure(new ComponentAction(getRootPane(), "cancel"), "AboutDialog.close")));
    }

    public void setIcon(Icon icon) {
        iconLabel.setIcon(icon);
    }

    public void setText(String text) {
        textPane.setText(text);
    }

    public void showDialog() {
        pack();
        setLocationRelativeTo(getOwner());
        resetScroll();
        timer.start();
        showDialogImpl();
        timer.stop();
    }

    private void resetScroll() {
        Point point = new Point(0, -viewport.getHeight());
        viewport.setViewPosition(point);
    }

    private void updateScroll() {
        Point point = viewport.getViewPosition();
        point.x = 0;
        point.y += 1;
        if (point.y > textPane.getHeight()) {
            point.y = -viewport.getHeight();
        }
        viewport.setViewPosition(point);
    }
}
