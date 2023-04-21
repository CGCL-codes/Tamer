package cn._2dland.uploader.qq;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public class VerifyCodeDialog extends JDialog implements ActionListener, KeyListener, MouseListener {

    private static final long serialVersionUID = -3827471375433833114L;

    private static final String CAPTCHA_URL = "http://captcha.qq.com/getimage?aid=3000501";

    private HttpClient client = null;

    private ImagePanel imgPanel = null;

    private JTextField vcText = null;

    private String username = null;

    private String vcType = null;

    public VerifyCodeDialog(JFrame owner, HttpClient client) {
        super(owner, "请输入图中的验证码", true);
        initUI(owner);
        this.client = client;
    }

    public void loadImage(String user, String vcType) {
        this.username = user;
        this.vcType = vcType;
        try {
            StringBuffer buf = new StringBuffer(CAPTCHA_URL).append("&uin=").append(username).append("&vc_type=").append(vcType).append("&r=").append(Math.random());
            GetMethod imageGet = new GetMethod(buf.toString());
            int respCode = client.executeMethod(imageGet);
            if (respCode != HttpStatus.SC_OK) throw new Exception("获取图片失败！");
            BufferedImage image = ImageIO.read(imageGet.getResponseBodyAsStream());
            imageGet.releaseConnection();
            imageGet = null;
            imgPanel.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getVerifyCode() {
        return vcText.getText();
    }

    /** 初始化界面 */
    private void initUI(JFrame owner) {
        setBounds(0, 0, 156, 136);
        setResizable(false);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        imgPanel = new ImagePanel();
        imgPanel.setToolTipText("单击刷新图片");
        imgPanel.addMouseListener(this);
        getContentPane().add(imgPanel);
        imgPanel.setBounds(10, 10, 130, 60);
        vcText = new JTextField();
        vcText.addKeyListener(this);
        getContentPane().add(vcText);
        vcText.setBounds(10, 74, 60, 24);
        JButton okBtn = new JButton("确定");
        okBtn.addActionListener(this);
        getContentPane().add(okBtn);
        okBtn.setBounds(80, 74, 60, 24);
    }

    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            this.dispose();
        }
    }

    public void mouseClicked(MouseEvent e) {
        this.loadImage(username, vcType);
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}

class ImagePanel extends JPanel {

    private static final long serialVersionUID = 8474961835519704588L;

    private Image image = null;

    public void setImage(Image img) {
        this.image = img;
        repaint();
    }

    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
}
