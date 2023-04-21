package jp.co.withone.osgi.gadget.bigmouse.base;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * つぶやき画面制御用クラス
 * 
 * @author Sasaki Katsuyuki
 *
 */
public class MouseMain extends JFrame implements ActionListener, KeyListener {

    Twitter twitterMine;

    List<Status> listStatus;

    Status statusMine;

    User userMine;

    JFrame mainFrame;

    Container pane;

    JPanel panelMain;

    JPanel panel1st;

    JPanel panel2nd;

    JLabel labelIcon;

    JTextArea tareaTweet;

    JLabel labelCount;

    JLabel labelLastTweet;

    JButton btnTweet;

    SimpleDateFormat formatter;

    /**
	 * コンストラクタ
	 * 
	 * ログイン画面からTwitterオブジェクトを取得し、
	 * つぶやき画面の初期表示処理を行う。
	 * 
	 * @param twitter
	 */
    public MouseMain(Twitter twitter) {
        twitterMine = twitter;
        formatter = new SimpleDateFormat("yyyy/MM/dd(E) HH:mm:ss");
        try {
            listStatus = twitterMine.getUserTimeline();
        } catch (TwitterException e) {
            e.printStackTrace();
            showDialog("予期せぬエラーが発生しました!!");
        }
        statusMine = listStatus.get(0);
        userMine = statusMine.getUser();
        mainFrame = new JFrame("Big Mouse for " + userMine.getScreenName());
        pane = mainFrame.getContentPane();
        MouseCommon.listFrames.add(mainFrame);
        panelMain = new JPanel(new MigLayout());
        panel1st = new JPanel(new MigLayout());
        panel2nd = new JPanel(new MigLayout());
        labelIcon = new JLabel(new ImageIcon(userMine.getProfileImageURL()));
        labelLastTweet = new JLabel("<html>この前のつぶやき： " + formatter.format(statusMine.getCreatedAt()) + "<br/>" + statusMine.getText() + "</html>");
        labelLastTweet.setFont(new Font(Font.SERIF, Font.PLAIN, 10));
        tareaTweet = new JTextArea(4, 50);
        tareaTweet.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
        tareaTweet.setLineWrap(true);
        tareaTweet.addKeyListener(this);
        labelCount = new JLabel("140");
        labelCount.setFont(new Font(Font.SERIF, Font.BOLD, 26));
        btnTweet = new JButton("つぶやく");
        btnTweet.setPreferredSize(new Dimension(100, 50));
        btnTweet.addActionListener(this);
        panel1st.add(labelIcon, "");
        panel1st.add(labelLastTweet, "");
        panel2nd.add(labelCount, "wrap");
        panel2nd.add(tareaTweet, "wrap");
        panel2nd.add(btnTweet, "center");
        panelMain.add(panel1st, "wrap");
        panelMain.add(panel2nd, "");
        pane.add(panelMain);
        mainFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainFrame.setSize(760, 370);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    /**
	 * つぶやくボタン押下時の処理
	 * 
	 */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnTweet) {
            if (tareaTweet.getText().length() > 140) {
                showDialog("文字数が140字を超えています!!");
            } else if (tareaTweet.getText().length() == 0) {
                showDialog("何も入力されていません!!");
            } else {
                sendTweet();
            }
        }
    }

    /**
	 * つぶやき送信処理
	 * 
	 */
    public void sendTweet() {
        try {
            twitterMine.updateStatus(tareaTweet.getText());
            listStatus = twitterMine.getUserTimeline();
            statusMine = listStatus.get(0);
            tareaTweet.setText("");
            labelCount.setText("140");
            labelLastTweet.setText("<html>この前のつぶやき： " + formatter.format(statusMine.getCreatedAt()) + "<br/>" + statusMine.getText() + "</html>");
        } catch (TwitterException e1) {
            e1.printStackTrace();
            showDialog("送信できませんでした!!");
        }
    }

    /**
	 * つぶやき入力欄の残り文字数表示用 
	 * 
	 */
    @Override
    public void keyReleased(KeyEvent e) {
        labelCount.setText(Integer.toString(140 - tareaTweet.getText().length()));
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
	 * エラー時のダイアログ表示用
	 * 
	 * @param message ダイアログに表示するメッセージ
	 */
    private void showDialog(String message) {
        JDialog dialog = new JDialog(this, "Caution", true);
        JLabel diaLabel = new JLabel(message, SwingConstants.CENTER);
        dialog.getContentPane().add(diaLabel);
        dialog.setSize(300, 300);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this.mainFrame);
        dialog.show();
    }
}
