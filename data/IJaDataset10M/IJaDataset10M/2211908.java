package xwh.jPiano;

import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JLabel;
import xwh.jPiano.util.ImgUtil;

public class Key_Piano extends JLabel {

    private static final long serialVersionUID = 1L;

    public static final int BLACK = 1, WHITE = 2;

    private static int change = 3;

    private static Map<String, Icon> images = new HashMap<String, Icon>();

    static {
        images.put("0", ImgUtil.getImageIcon(("pianoKeys/0.png")));
        images.put("1", ImgUtil.getImageIcon(("pianoKeys/1.png")));
        images.put("2", ImgUtil.getImageIcon(("pianoKeys/2.png")));
        images.put("3", ImgUtil.getImageIcon(("pianoKeys/3.png")));
        images.put("#", ImgUtil.getImageIcon(("pianoKeys/#.png")));
        images.put("0_down", ImgUtil.getImageIcon(("pianoKeys/0_down.png")));
        images.put("1_down", ImgUtil.getImageIcon(("pianoKeys/1_down.png")));
        images.put("2_down", ImgUtil.getImageIcon(("pianoKeys/2_down.png")));
        images.put("3_down", ImgUtil.getImageIcon(("pianoKeys/3_down.png")));
        images.put("#_down", ImgUtil.getImageIcon(("pianoKeys/#_down.png")));
    }

    int keyNum;

    public int blackOrWhite;

    public char ch;

    public boolean isDown = false;

    private int length;

    public Key_Piano(int x, int y, int width, int height, int num, char ch) {
        keyNum = num;
        setLocation(x, y);
        setSize(width, height);
        this.setAlignmentX(TOP_ALIGNMENT);
        this.setAlignmentY(TOP_ALIGNMENT);
        this.ch = ch;
        this.length = height;
        this.blackOrWhite = (ch == '#' ? BLACK : WHITE);
        this.setIcon(images.get(ch + ""));
    }

    public void setImage_down() {
        this.setIcon(images.get(ch + "_down"));
        if (this.blackOrWhite == WHITE) {
            this.setSize(this.getWidth(), length + change);
        }
    }

    public void setImage_up() {
        this.setIcon(images.get(ch + ""));
        if (this.blackOrWhite == WHITE) {
            this.setSize(this.getWidth(), length);
        }
    }

    public void on() {
        isDown = true;
        this.setImage_down();
        DeviceManage.nodeOn_left(this.keyNum);
    }

    public void off() {
        isDown = false;
        this.setImage_up();
        DeviceManage.nodeOff_left(this.keyNum);
    }
}
