package ceuclar.j2me.canvas;

import java.io.*;
import javax.microedition.lcdui.*;

public class MeuCanvas extends Canvas implements CommandListener {

    private TesteCanvas midlet;

    private int x, y;

    private Command cmdSair;

    private int largura, altura;

    public MeuCanvas(TesteCanvas midlet) {
        this.midlet = midlet;
        cmdSair = new Command("Sair", Command.EXIT, 0);
        largura = getWidth();
        altura = getHeight();
        x = largura / 2;
        y = altura / 2 + 30;
        setCommandListener(this);
        addCommand(cmdSair);
    }

    public void paint(Graphics g) {
        g.setColor(0, 0, 255);
        g.fillRect(0, 0, largura, altura);
        g.setColor(255, 255, 255);
        g.fillRoundRect(10, 10, largura - 20, altura - 20, 20, 20);
        g.setColor(0, 0, 0);
        Font f = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_ITALIC, Font.SIZE_LARGE);
        g.setFont(f);
        g.drawString("Exemplo Canvas!", largura / 2, altura / 2, Graphics.BASELINE | Graphics.HCENTER);
        try {
            Image img = Image.createImage("/duke.png");
            g.drawImage(img, x, y, Graphics.VCENTER | Graphics.HCENTER);
        } catch (IOException ex) {
            System.out.println("Erro:" + ex.getMessage());
        }
    }

    protected void keyPressed(int key) {
        switch(key) {
            case KEY_NUM2:
                y--;
                break;
            case KEY_NUM8:
                y++;
                break;
            case KEY_NUM4:
                x--;
                break;
            case KEY_NUM6:
                x++;
                break;
        }
        repaint();
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command == cmdSair) {
            midlet.destroyApp(false);
        }
    }
}
