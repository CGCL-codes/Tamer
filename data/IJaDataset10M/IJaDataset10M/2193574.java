package yati.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import yati.data.Block;
import yati.data.Spielfeld;
import yati.game.IGame;

public class SimpleGameCanvas extends Canvas implements IYatiCanvas {

    private Graphics buffer;

    private Image next_block_bg, score, gitter, offImage, block_red, block_green, block_blue, block_pink, block_purple, block_yellow, block_turquoise, border;

    private Image block_red_s, block_green_s, block_blue_s, block_pink_s, block_purple_s, block_yellow_s, block_turquoise_s;

    private Image one, two, three, four, five, six, seven, eight, nine, zero;

    private IGame game;

    private Font f = new Font(Font.SANS_SERIF, Font.ITALIC, 20);

    public SimpleGameCanvas() {
        this.setFocusable(false);
        this.setPreferredSize(new Dimension(220, 490));
        this.setBackground(Color.BLACK);
        this.next_block_bg = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/next_block_bg.png"));
        this.score = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/score.png"));
        this.border = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/border.png"));
        this.gitter = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/background.png"));
        this.block_red = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/block_red.png"));
        this.block_green = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/block_green.png"));
        this.block_blue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/block_blue.png"));
        this.block_pink = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/block_pink.png"));
        this.block_purple = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/block_purple.png"));
        this.block_yellow = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/block_yellow.png"));
        this.block_turquoise = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/block_turquoise.png"));
        this.block_red_s = this.block_red.getScaledInstance(10, 10, Image.SCALE_AREA_AVERAGING);
        this.block_green_s = this.block_green.getScaledInstance(10, 10, Image.SCALE_AREA_AVERAGING);
        this.block_blue_s = this.block_blue.getScaledInstance(10, 10, Image.SCALE_AREA_AVERAGING);
        this.block_pink_s = this.block_pink.getScaledInstance(10, 10, Image.SCALE_AREA_AVERAGING);
        this.block_purple_s = this.block_purple.getScaledInstance(10, 10, Image.SCALE_AREA_AVERAGING);
        this.block_yellow_s = this.block_yellow.getScaledInstance(10, 10, Image.SCALE_AREA_AVERAGING);
        this.block_turquoise_s = this.block_turquoise.getScaledInstance(10, 10, Image.SCALE_AREA_AVERAGING);
        this.one = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/numbers/1.png"));
        this.two = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/numbers/2.png"));
        this.three = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/numbers/3.png"));
        this.four = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/numbers/4.png"));
        this.five = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/numbers/5.png"));
        this.six = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/numbers/6.png"));
        this.seven = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/numbers/7.png"));
        this.eight = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/numbers/8.png"));
        this.nine = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/numbers/9.png"));
        this.zero = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("yati/media/numbers/0.png"));
    }

    @Override
    public void setGame(IGame game) {
        this.game = game;
    }

    @Override
    public void update(Graphics g) {
        this.paint(g);
    }

    @Override
    public void paint(Graphics g) {
        if (game == null) return;
        if (buffer == null) {
            offImage = createImage(200, 400);
            buffer = offImage.getGraphics();
        }
        if (buffer != null) bufferPaint();
        if (offImage != null) {
            g.setFont(f);
            g.drawImage(score, 0, 0, this);
            g.drawImage(drawScore(), 15, 15, this);
            g.drawImage(drawBlock(game.getNext_block()), 165, 15, this);
            g.drawImage(border, 0, 70, this);
            g.drawImage(offImage, 10, 80, this);
        }
    }

    private void bufferPaint() {
        Spielfeld s = game.getField();
        Block b = game.getCurrent_block();
        buffer.clearRect(0, 0, 200, 400);
        buffer.drawImage(gitter, 0, 0, this);
        int[][] feldData = s.getFeld();
        int[][] blockData = b.getBlock_data();
        int x = b.getPosition().x;
        int y = b.getPosition().y;
        for (int j = 0; j < feldData.length; ++j) {
            for (int i = 0; i < feldData[0].length; ++i) {
                if ((x <= i && i <= x + 3) && (y <= j && j <= y + 3)) {
                    if (blockData[j - y][i - x] > 0) {
                        buffer.drawImage(getIm(blockData[j - y][i - x]), i * 20, j * 20, this);
                    } else {
                        if (feldData[j][i] > 0) {
                            buffer.drawImage(getIm(feldData[j][i]), i * 20, j * 20, this);
                        }
                    }
                } else {
                    if (feldData[j][i] > 0) {
                        buffer.drawImage(getIm(feldData[j][i]), i * 20, j * 20, this);
                    }
                }
            }
        }
    }

    private Image drawBlock(Block b) {
        Image temp = createImage(40, 40);
        temp.getGraphics().drawImage(next_block_bg, 0, 0, this);
        for (int j = 0; j < b.getBlock_data().length; j++) {
            for (int i = 0; i < b.getBlock_data()[0].length; ++i) {
                if (b.getBlock_data()[j][i] > 0) {
                    temp.getGraphics().drawImage(getSmallIm(b.getBlock_data()[j][i]), i * 10, j * 10, this);
                }
            }
        }
        return temp;
    }

    private Image drawScore() {
        Image temp = createImage(150, 40);
        Graphics g = temp.getGraphics();
        String score = game.getScore().toString();
        int offset = 6 - score.length();
        for (int i = 0; i < offset; ++i) {
            g.drawImage(this.zero, i * 25, 0, this);
        }
        for (int i = 0; i < score.length(); ++i) {
            switch(score.charAt(i)) {
                case '1':
                    g.drawImage(this.one, (offset * 25) + i * 25, 0, this);
                    break;
                case '2':
                    g.drawImage(this.two, (offset * 25) + i * 25, 0, this);
                    break;
                case '3':
                    g.drawImage(this.three, (offset * 25) + i * 25, 0, this);
                    break;
                case '4':
                    g.drawImage(this.four, (offset * 25) + i * 25, 0, this);
                    break;
                case '5':
                    g.drawImage(this.five, (offset * 25) + i * 25, 0, this);
                    break;
                case '6':
                    g.drawImage(this.six, (offset * 25) + i * 25, 0, this);
                    break;
                case '7':
                    g.drawImage(this.seven, (offset * 25) + i * 25, 0, this);
                    break;
                case '8':
                    g.drawImage(this.eight, (offset * 25) + i * 25, 0, this);
                    break;
                case '9':
                    g.drawImage(this.nine, (offset * 25) + i * 25, 0, this);
                    break;
                case '0':
                    g.drawImage(this.zero, (offset * 25) + i * 25, 0, this);
                    break;
            }
        }
        return temp;
    }

    private Image getIm(int type) {
        switch(type) {
            case 1:
                return block_red;
            case 2:
                return block_green;
            case 3:
                return block_blue;
            case 4:
                return block_yellow;
            case 5:
                return block_purple;
            case 6:
                return block_pink;
            case 7:
                return block_turquoise;
            default:
                return block_red;
        }
    }

    private Image getSmallIm(int type) {
        switch(type) {
            case 1:
                return block_red_s;
            case 2:
                return block_green_s;
            case 3:
                return block_blue_s;
            case 4:
                return block_yellow_s;
            case 5:
                return block_purple_s;
            case 6:
                return block_pink_s;
            case 7:
                return block_turquoise_s;
            default:
                return block_red_s;
        }
    }
}
