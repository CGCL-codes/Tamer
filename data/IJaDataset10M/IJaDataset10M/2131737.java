package edu.uci.ics.jung.visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.civi.util.GlobalFlags;

/**
 * @author Michael Scholz
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DetailViewer extends JPanel {

    private JLabel title;

    private JLabel summary;

    private JLabel metaData;

    private JLabel keywords;

    private Color bgColor;

    /**
	 * 
	 * @param backgroundColor
	 */
    public DetailViewer(Color backgroundColor) {
        super();
        this.setBackground(backgroundColor);
        bgColor = backgroundColor;
        init();
    }

    protected void init() {
        this.setLayout(new BorderLayout(5, 5));
        GlobalFlags flags = GlobalFlags.getInstance();
        setMinimumSize(new Dimension(flags.getScreenSize() * 100, 120));
        setMaximumSize(new Dimension(flags.getScreenSize() * 100, 120));
        setPreferredSize(new Dimension(flags.getScreenSize() * 100, 120));
        title = new JLabel("", JLabel.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 11));
        summary = new JLabel("", JLabel.LEFT);
        summary.setFont(new Font("Arial", Font.PLAIN, 11));
        metaData = new JLabel("", JLabel.LEFT);
        metaData.setFont(new Font("Arial", Font.PLAIN, 11));
        keywords = new JLabel("", JLabel.LEFT);
        keywords.setFont(new Font("Arial", Font.PLAIN, 11));
        JPanel mdPanel = new JPanel();
        mdPanel.setBackground(bgColor);
        mdPanel.setLayout(new BorderLayout(25, 5));
        mdPanel.add(metaData, BorderLayout.CENTER);
        mdPanel.add(keywords, BorderLayout.SOUTH);
        this.add(title, BorderLayout.NORTH);
        this.add(summary, BorderLayout.CENTER);
        this.add(mdPanel, BorderLayout.SOUTH);
    }

    public void setTitle(String s) {
        title.setText(s);
    }

    public void setSummary(String s) {
        s = parseTags(s);
        summary.setText("<html><p>" + s + "</p></html>");
    }

    public void setMetaData(String s) {
        metaData.setText(s);
    }

    public void setKeywords(String s) {
        keywords.setText(s);
    }

    public void reset() {
        title.setText("");
        summary.setText("");
        metaData.setText("");
        keywords.setText("");
    }

    protected String parseTags(String s) {
        s = s.replaceAll("&lt;", "<");
        s = s.replaceAll("&gt;", ">");
        s = s.replaceAll("&#39;", "\"");
        return s;
    }
}
