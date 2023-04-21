package minesweeper;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;

/**
 * @author      M. Jumari 
 * @author      m.jumari@gmail.com 
 * @author      http://www.emje.net
 */
public class TFrmPermainanCustom extends Form implements CommandListener {

    private Minesweeper m;

    private Command cOK;

    private TextField tTinggi, tLebar, tBomb;

    private Image iError;

    public TFrmPermainanCustom(final Minesweeper m) {
        super("Permainan Baru");
        this.m = m;
        tTinggi = new TextField("Masukkan tinggi", "" + m.frmLevelPermainan.tinggi, 2, TextField.NUMERIC);
        tLebar = new TextField("Masukkan lebar", "" + m.frmLevelPermainan.lebar, 2, TextField.NUMERIC);
        tBomb = new TextField("Masukkan jumlah bomb", "" + m.frmLevelPermainan.bomb, 3, TextField.NUMERIC);
        append(tTinggi);
        append(tLebar);
        append(tBomb);
        cOK = new Command("OK", Command.BACK, 0);
        addCommand(cOK);
        setCommandListener(this);
        try {
            iError = Image.createImage("/images/error.png");
        } catch (Exception e) {
        }
    }

    public void show() {
        tTinggi.setString("" + m.frmLevelPermainan.tinggi);
        tLebar.setString("" + m.frmLevelPermainan.lebar);
        tBomb.setString("" + m.frmLevelPermainan.bomb);
        m.disp.setCurrent(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c.getLabel().equals(cOK.getLabel())) {
            m.frmLevelPermainan.tinggi = Integer.parseInt(tTinggi.getString());
            m.frmLevelPermainan.lebar = Integer.parseInt(tLebar.getString());
            m.frmLevelPermainan.bomb = Integer.parseInt(tBomb.getString());
            boolean ok = true;
            if (ok && (m.frmLevelPermainan.tinggi < m.frmPermainanBaru.game.MINE_MIN_SIZE || m.frmLevelPermainan.tinggi > 24)) {
                ok = false;
                m.disp.setCurrent(new Alert("Game Custom", "Tinggi minimal 9 maksimal 24", iError, AlertType.ERROR), this);
            }
            if (ok && (m.frmLevelPermainan.lebar < m.frmPermainanBaru.game.MINE_MIN_SIZE || m.frmLevelPermainan.lebar > m.frmPermainanBaru.game.MINE_MAX_SIZE)) {
                ok = false;
                m.disp.setCurrent(new Alert("Game Custom", "lebar minimal 9 maksimal 30", iError, AlertType.ERROR), this);
            }
            if (m.frmLevelPermainan.bomb < 10) {
                m.frmLevelPermainan.bomb = 10;
            }
            if ((m.frmLevelPermainan.tinggi - 1) * (m.frmLevelPermainan.lebar - 1) < m.frmLevelPermainan.bomb) {
                m.frmLevelPermainan.bomb = (m.frmLevelPermainan.tinggi - 1) * (m.frmLevelPermainan.lebar - 1);
            }
            if (ok) {
                m.frmPermainanBaru.show();
            }
        }
    }
}
