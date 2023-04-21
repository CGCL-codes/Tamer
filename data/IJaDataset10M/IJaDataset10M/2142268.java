package jboard.ui;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.BorderLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JLabel;
import jboard.util.SwingUtil;

/**
 * @author vashira
 *
 */
public class TokenPanel extends JPanel {

    private JPanel aiAvatarPanel;

    private JLabel aiScoreLabel;

    private JPanel playerAvatarPanel;

    private JLabel playerScoreLabel;

    public TokenPanel() {
        super();
        final GridLayout gridLayout = new GridLayout();
        gridLayout.setColumns(2);
        setLayout(gridLayout);
        final JPanel pPanel = new JPanel();
        pPanel.setLayout(new BorderLayout());
        pPanel.setBorder(new TitledBorder(new EtchedBorder(), "Player", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        add(pPanel);
        playerAvatarPanel = new JPanel();
        playerAvatarPanel.setBorder(new LineBorder(Color.black, 1, false));
        ImageIcon playerAvatar = SwingUtil.getIcon(TokenPanel.class.getClassLoader(), "img/avatar/player-avatar.gif");
        JLabel playerAvatarLabel = new JLabel(playerAvatar);
        playerAvatarPanel.add(playerAvatarLabel);
        pPanel.add(playerAvatarPanel, BorderLayout.NORTH);
        final JPanel numberPanel = new JPanel();
        pPanel.add(numberPanel, BorderLayout.CENTER);
        final JLabel label = new JLabel();
        numberPanel.add(label);
        label.setText("Player : ");
        playerScoreLabel = new JLabel();
        numberPanel.add(playerScoreLabel);
        playerScoreLabel.setText("8");
        final JPanel aiPanel = new JPanel();
        aiPanel.setLayout(new BorderLayout());
        aiPanel.setBorder(new TitledBorder(new EtchedBorder(), "Computer", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        add(aiPanel);
        aiAvatarPanel = new JPanel();
        aiAvatarPanel.setBorder(new LineBorder(Color.black, 1, false));
        ImageIcon aiAvatar = SwingUtil.getIcon(TokenPanel.class.getClassLoader(), "img/avatar/hi-avatar.gif");
        JLabel aiAvatarLabel = new JLabel(aiAvatar);
        aiAvatarPanel.add(aiAvatarLabel);
        aiPanel.add(aiAvatarPanel, BorderLayout.NORTH);
        final JPanel panel_1 = new JPanel();
        aiPanel.add(panel_1, BorderLayout.CENTER);
        final JLabel label_1 = new JLabel();
        panel_1.add(label_1);
        label_1.setText("Computer : ");
        aiScoreLabel = new JLabel();
        panel_1.add(aiScoreLabel);
        aiScoreLabel.setText("8");
    }

    public void changeAvatar(int status) {
        JLabel aiAvatarLabel = ThemeFactory.createAvatar(status);
        aiAvatarPanel.removeAll();
        aiAvatarPanel.add(aiAvatarLabel);
    }

    public void setAIScore(int tokenLeft) {
        aiScoreLabel.setText(new Integer(tokenLeft).toString());
    }

    public void setPlayerScore(int tokenLeft) {
        playerScoreLabel.setText(new Integer(tokenLeft).toString());
    }
}
