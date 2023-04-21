package markgame2dphys;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import net.phys2d.raw.Body;

public class Test3_Restitution extends MarkGame2DEngine {

    private static final long serialVersionUID = 1L;

    public static final int W = 640;

    public static final int H = 480;

    public List<MarkGameCircle> circles;

    public MarkGameBox ground1;

    public Test3_Restitution() {
        super(W, H, 0, 100, 20, 30);
        circles = new ArrayList<MarkGameCircle>();
        for (int i = -100; i <= 100; i += 25) {
            MarkGameCircle circle = new MarkGameCircle(10, 2);
            circle.body.setPosition(W / 2 + i, 200);
            circle.body.setRestitution((i + 100f) / 200f * 2);
            circles.add(circle);
            add(circle);
        }
        ground1 = new MarkGameBox(W - 10, 50, Body.INFINITE_MASS);
        ground1.body.setPosition(W / 2, H - 50);
        ground1.body.setRestitution(1f);
        add(ground1);
    }

    public void paint(MarkGameGraphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, W, H);
        for (MarkGameCircle circle : circles) {
            circle.paint(g);
        }
        ground1.paint(g);
    }

    public static void main(String[] args) {
        JFrame janela = new JFrame("teste1");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Test3_Restitution app = new Test3_Restitution();
        janela.getContentPane().add(app);
        janela.pack();
        janela.setVisible(true);
        janela.setLocationRelativeTo(null);
        janela.setExtendedState(JFrame.MAXIMIZED_BOTH);
        new Thread(app).start();
    }
}
