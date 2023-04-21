package xwh.jPiano;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Panel_Piano extends JPanel {

    private static final long serialVersionUID = 1L;

    private static int whiteKeysCount = 52;

    private static int blackKeysCount = 36;

    public static Key_Piano[] pianoKeys = new Key_Piano[whiteKeysCount + blackKeysCount];

    private final int kw_white = 16, kh_white = 75;

    private final int kw_black = 10, kh_black = 54;

    private final int y = 0;

    public Panel_Piano() {
        setLayout(null);
        setPreferredSize(new Dimension(whiteKeysCount * kw_white, kh_white + 10));
        int whiteIDs[] = { 0, 2, 4, 5, 7, 9, 11 };
        Key_Piano whiteKey_l_0 = new Key_Piano(0, y, kw_white, kh_white, 21, '3');
        Key_Piano whiteKey_l_1 = new Key_Piano(kw_white, y, kw_white, kh_white, 23, '1');
        Key_Piano whiteKey_l_87 = new Key_Piano(kw_white * 51, y, kw_white, kh_white, 108, '0');
        pianoKeys[0] = whiteKey_l_0;
        pianoKeys[2] = whiteKey_l_1;
        pianoKeys[87] = whiteKey_l_87;
        Key_Piano blackKeys_1 = new Key_Piano(kw_white - kw_black / 2, y, kw_black, kh_black, 22, '#');
        pianoKeys[1] = blackKeys_1;
        int x = kw_white * 2;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                Integer keyNum = i * 12 + whiteIDs[j] + 24;
                char ch = '0';
                switch(j) {
                    case 0:
                    case 3:
                        ch = '3';
                        break;
                    case 1:
                    case 4:
                    case 5:
                        ch = '2';
                        break;
                    case 2:
                    case 6:
                        ch = '1';
                        break;
                }
                Key_Piano whiteKey_l = new Key_Piano(x, y, kw_white, kh_white, keyNum, ch);
                pianoKeys[keyNum - 21] = whiteKey_l;
                x += kw_white;
            }
        }
        x = kw_white + kw_white - kw_black / 2;
        for (int i = 0; i < 7; i++) {
            Integer keyNum = i * 12 + 24;
            Key_Piano blackKeys1 = new Key_Piano((x += kw_white), y, kw_black, kh_black, keyNum + 1, '#');
            Key_Piano blackKeys2 = new Key_Piano((x += kw_white), y, kw_black, kh_black, keyNum + 3, '#');
            x += kw_white;
            Key_Piano blackKeys3 = new Key_Piano((x += kw_white), y, kw_black, kh_black, keyNum + 6, '#');
            Key_Piano blackKeys4 = new Key_Piano((x += kw_white), y, kw_black, kh_black, keyNum + 8, '#');
            Key_Piano blackKeys5 = new Key_Piano((x += kw_white), y, kw_black, kh_black, keyNum + 10, '#');
            pianoKeys[keyNum + 1 - 21] = blackKeys1;
            pianoKeys[keyNum + 3 - 21] = blackKeys2;
            pianoKeys[keyNum + 6 - 21] = blackKeys3;
            pianoKeys[keyNum + 8 - 21] = blackKeys4;
            pianoKeys[keyNum + 10 - 21] = blackKeys5;
            x += kw_white;
        }
        this.addKeys();
    }

    public void addKeys() {
        LineBorder lineBorder = new LineBorder(Color.gray, 1, true);
        for (int i = 0; i < pianoKeys.length; i++) {
            Key_Piano key = pianoKeys[i];
            if (key.blackOrWhite == Key_Piano.BLACK) {
                key.addMouseListener(mouseListener);
                this.add(key);
            }
        }
        for (int i = 0; i < pianoKeys.length; i++) {
            Key_Piano key = pianoKeys[i];
            if (key.blackOrWhite == Key_Piano.WHITE) {
                key.setBorder(lineBorder);
                key.addMouseListener(mouseListener);
                this.add(key);
            }
        }
    }

    private MouseListener mouseListener = new MouseAdapter() {

        private boolean mouseDown = false;

        private Key_Piano key_last;

        @Override
        public void mousePressed(MouseEvent e) {
            mouseDown = true;
            Key_Piano key = (Key_Piano) e.getComponent();
            key.on();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouseDown = false;
            Key_Piano key = (Key_Piano) e.getComponent();
            key.off();
            if (key_last != null && key_last.isDown) {
                key_last.off();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (mouseDown) {
                Key_Piano key = (Key_Piano) e.getComponent();
                key.on();
                key_last = key;
            }
        }

        public void mouseExited(MouseEvent e) {
            if (mouseDown) {
                Key_Piano key = (Key_Piano) e.getComponent();
                key.off();
            }
        }
    };
}
