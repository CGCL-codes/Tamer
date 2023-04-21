package potd;

import java.awt.*;

final class MoveCanvas extends Canvas {

    static final int BOX_TOP = 54;

    static final int BOX_WIDTH = 149;

    static final int BOX_HEIGHT = 290;

    static final int ROW_OFFSET = 22;

    static final int NUM_COLUMN = 3;

    static final int NUM_COLUMN_WIDTH = 23;

    static final int HEADER_TOP = 56;

    static final int HEADER_HEIGHT = 21;

    static final int W_COLUMN = 28;

    static final int B_COLUMN = 88;

    static final int MOVE_COLUMN_WIDTH = 58;

    static final int MOVE_ROW_TOP = 80;

    static final int MOVE_ROW_HEIGHT = 19;

    private int canvasWidth;

    private int canvasHeight;

    private int moveCoord[][];

    private int moveNumberMargin;

    private int headerBaseline;

    private int moveLeftMargin;

    private int moveBaseline[];

    private Font moveBoxFont;

    private FontMetrics mfm;

    private Image bufferImage;

    private Graphics bufferG;

    private Board bd;

    private SANdata san;

    private AppMain main;

    MoveCanvas(int i, int j, AppMain appmain) {
        canvasWidth = i;
        canvasHeight = j;
        main = appmain;
        moveCoord = new int[24][2];
        for (int k = 0; k < 24; k++) {
            moveCoord[k][0] = k % 2 != 0 ? 88 : 28;
            moveCoord[k][1] = 80 + 22 * (k / 2);
        }
    }

    void initMovePanel(SANdata sandata) {
        san = sandata;
        if (bufferG == null) {
            bufferImage = createImage(canvasWidth, canvasHeight);
            bufferG = bufferImage.getGraphics();
        }
        mfm = bufferG.getFontMetrics(AppMain.moveFont);
        if (mfm.stringWidth("1234567") < 54) {
            moveBoxFont = AppMain.moveFont;
        } else {
            mfm = bufferG.getFontMetrics(AppMain.smallMoveFont);
            moveBoxFont = AppMain.smallMoveFont;
        }
        moveNumberMargin = 3 + ((23 - mfm.stringWidth("12")) + 1) / 2 + mfm.stringWidth("12");
        headerBaseline = 56 + ((21 - mfm.getAscent()) + 1) / 2 + mfm.getAscent();
        moveLeftMargin = Math.max(((58 - mfm.stringWidth("1234567")) + 1) / 2, 2);
        moveBaseline = new int[12];
        moveBaseline[0] = 80 + ((19 - mfm.getAscent()) + 1) / 2 + mfm.getAscent();
        for (int i = 1; i < 12; i++) moveBaseline[i] = moveBaseline[i - 1] + 22;
        bufferG.setColor(Color.lightGray);
        bufferG.fillRect(0, 0, canvasWidth, canvasHeight);
        bufferG.setColor(Color.black);
        bufferG.setFont(AppMain.textFont);
        FontMetrics fontmetrics = bufferG.getFontMetrics(AppMain.textFont);
        bufferG.drawString(main.MOVELIST, (canvasWidth - fontmetrics.stringWidth(main.MOVELIST)) / 2, 39);
        drawMoveBox();
        repaint();
    }

    private void drawMoveBox() {
        bufferG.setColor(Color.white);
        bufferG.fillRect(0, 54, 149, 290);
        bufferG.setColor(Color.gray);
        bufferG.drawLine(0, 54, 147, 54);
        bufferG.drawLine(0, 54, 0, 343);
        bufferG.setColor(Color.black);
        bufferG.drawLine(1, 55, 146, 55);
        bufferG.drawLine(1, 55, 1, 342);
        bufferG.setColor(Color.lightGray);
        bufferG.drawLine(147, 55, 147, 342);
        bufferG.drawLine(1, 342, 146, 342);
        bufferG.drawLine(26, 56, 26, 342);
        bufferG.drawLine(87, 56, 87, 342);
        for (int i = 0; i < 12; i++) bufferG.drawLine(2, 78 + i * 22, 146, 78 + i * 22);
        bufferG.setColor(Color.gray);
        bufferG.drawLine(27, 56, 27, 342);
        bufferG.drawLine(2, 79, 146, 79);
        bufferG.setFont(moveBoxFont);
        bufferG.drawString(main.W_HEADER, 28 + ((58 - mfm.stringWidth(main.W_HEADER)) + 1) / 2, headerBaseline);
        bufferG.drawString(main.B_HEADER, 88 + ((58 - mfm.stringWidth(main.B_HEADER)) + 1) / 2, headerBaseline);
        for (int j = 0; j < 12; j++) bufferG.drawString(Integer.toString(j + 1), moveNumberMargin - mfm.stringWidth(Integer.toString(j + 1)), moveBaseline[j]);
    }

    void drawMoveList() {
        drawMoveBox();
        bufferG.setColor(Color.black);
        bufferG.setFont(moveBoxFont);
        for (int i = 0; i < san.getMoveListSize(); i++) bufferG.drawString(san.getMoveListElement(i), i % 2 != 0 ? 88 + moveLeftMargin : 28 + moveLeftMargin, moveBaseline[i / 2]);
        repaint();
    }

    public void paint(Graphics g) {
        update(g);
    }

    public void update(Graphics g) {
        if (bufferImage != null) g.drawImage(bufferImage, 0, 0, this);
    }

    public boolean mouseDown(Event event, int i, int j) {
        int k;
        if ((k = pixelToMove(i, j)) != -1) main.setToMove(main.getStartSide() != 0 ? k : k + 1);
        return true;
    }

    int pixelToMove(int i, int j) {
        for (int k = 0; k < 24; k++) if (i > moveCoord[k][0] && i < moveCoord[k][0] + 58 && j > moveCoord[k][1] && j < moveCoord[k][1] + 19) return k;
        return -1;
    }
}
