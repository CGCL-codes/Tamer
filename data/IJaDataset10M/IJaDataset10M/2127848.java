package FDUFW.localhost.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 * 
 * @author BLingSoft
 * 
 * 右下角弹出式提示框 1.自动上升 2.停留一段时间，本例子中5秒 3.自动下降直至消失
 * 
 * 4.线程控制窗口的出现和消失，同时添加鼠标事件控制，可以提前使提示框消失 5.鼠标事件结合自己的需求实现，此处只是实现一个点击事件
 * 
 * @Time 2010-01-29
 * @JDK VERSION 6.0
 * @Copy Right By BLingSoft
 */
public class RightCornerPopMessage extends JWindow implements Runnable, MouseListener {

    private static final long serialVersionUID = -3564453685861233338L;

    private Integer screenWidth;

    private Integer screenHeight;

    private Integer windowWidth = 200;

    private Integer windowHeight = 100;

    private Integer bottmToolKitHeight;

    private Integer stayTime = 5000;

    private Integer x;

    private Integer y;

    private String title = "温馨提示";

    private String message = "一个小小的提示消息例子！";

    private JPanel mainPanel;

    private JLabel titleLabel;

    private JPanel titlePanel;

    private JLabel messageLabel;

    private JPanel messagePanel;

    public RightCornerPopMessage() {
        this.init();
        Thread thread = new Thread(this);
        thread.start();
    }

    public RightCornerPopMessage(String _title, String _message) {
        title = _title;
        message = _message;
        this.init();
        Thread thread = new Thread(this);
        thread.start();
    }

    private void init() {
        bottmToolKitHeight = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration()).bottom;
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = dimension.width;
        screenHeight = dimension.height;
        x = screenWidth - windowWidth;
        y = screenHeight;
        this.setLocation(x, y - bottmToolKitHeight - windowHeight);
        mainPanel = new JPanel(new BorderLayout());
        titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.RED);
        titlePanel.add(titleLabel);
        messageLabel = new JLabel(message);
        messagePanel = new JPanel();
        messagePanel.add(messageLabel);
        messagePanel.setBackground(Color.YELLOW);
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        this.setSize(windowWidth, windowHeight);
        this.setAlwaysOnTop(false);
        this.getContentPane().add(mainPanel);
        this.addMouseListener(this);
        Toolkit.getDefaultToolkit().beep();
        this.setVisible(true);
    }

    public void run() {
        Integer delay = 10;
        Integer step = 1;
        Integer end = windowHeight + bottmToolKitHeight;
        while (true) {
            try {
                step++;
                y = y - 1;
                this.setLocation(x, y);
                if (step > end) {
                    Thread.sleep(stayTime);
                    break;
                }
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        step = 1;
        while (true) {
            try {
                step++;
                y = y + 1;
                this.setLocation(x, y);
                if (step > end) {
                    this.dispose();
                    break;
                }
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        this.dispose();
        JOptionPane.showMessageDialog(this, new JLabel(message), title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public static void main(String[] args) {
        new RightCornerPopMessage();
    }
}
