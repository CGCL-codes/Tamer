package org.jbuzz.awt;

/**
 * @author Lung-Kai Cheng (lkcheng@users.sourceforge.net)
 */
public class TextField extends TextComponent {

    int columns;

    char echoChar;

    public TextField() {
        this(null, 10);
    }

    public TextField(int columns) {
        this(null, columns);
    }

    public TextField(String text) {
        this(text, 10);
    }

    public TextField(String text, int columns) {
        this.setText(text);
        this.setColumns(columns);
        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
        this.setFocusable(true);
    }

    public int getColumns() {
        return this.columns;
    }

    public void setColumns(int columns) {
        if (columns < 0) {
            throw new IllegalArgumentException();
        }
        this.columns = columns;
    }

    public char getEchoChar() {
        return this.echoChar;
    }

    public void setEchoChar(char echoChar) {
        this.echoChar = echoChar;
    }

    public void paint(Graphics g) {
        byte[] textBytes = this.text.getBytes();
        if (this.echoChar != '\0') {
            for (int i = 0; i < textBytes.length; i++) {
                textBytes[i] = (byte) this.echoChar;
            }
        }
        StringBuffer string = null;
        if (textBytes.length < this.columns) {
            string = new StringBuffer(new String(textBytes));
            for (int i = textBytes.length; i < this.columns; i++) {
                string.append(' ');
            }
        } else {
            string = new StringBuffer(new String(textBytes, 0, this.columns));
        }
        g.drawString(this.decorate(string.toString()), this.x, this.y);
    }
}
