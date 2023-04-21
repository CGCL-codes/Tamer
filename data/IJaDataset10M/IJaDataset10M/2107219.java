package InternetHax;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * The console is used to print information on the screen for the player to see.
 * Information such as what attacks are being exicuted and how much damage has been dealt.
 * Note that at the moment it is very hardcoded and has been known to do slightly strange
 * things when run on a phone with different screen size than the emulator 
 * @author LoginError
 */
public class Console {

    final int BUFFER_SIZE = 140;

    StringBuffer buffer = new StringBuffer(BUFFER_SIZE);

    int width, height;

    int wrapPoints[] = new int[5];

    Image consoleImage;

    int charwidth, charheight;

    public Console(int width, int height, int charwidth, int charheight) {
        this.width = width;
        this.height = height;
        this.consoleImage = Image.createImage(width, height);
        this.charwidth = charwidth;
        this.charheight = charheight;
    }

    public synchronized void print(String thestring) {
        Toolbox.printDebug("Console: " + thestring);
        if (thestring.length() + buffer.length() >= BUFFER_SIZE) {
            buffer.delete(0, thestring.length());
            int firstspace = buffer.toString().indexOf(" ");
            if (firstspace != -1) {
                buffer.delete(0, firstspace + 1);
            }
        }
        buffer.append(thestring);
        updateImage(true);
    }

    public synchronized void drawHUD(Graphics g, int x, int y) {
        float hpPercent = 100 * ((float) Gamestate.theHero.getHP() / (float) Gamestate.theHero.getMaxHP());
        g.setColor(Gamestate.uiDetailColor);
        g.drawLine(0, y - 1, Gamestate.screenWidth, y - 1);
        g.setColor(Gamestate.uiBackgroundColor);
        g.fillRect(x, y, Gamestate.screenWidth, 20);
        g.setColor(Gamestate.uiDetailColor);
        g.drawRect(x + 15, y + 5, 100, 10);
        if (hpPercent > 50) {
            g.setColor(0, 255, 0);
        }
        if (hpPercent < 50) {
            g.setColor(0, 128, 128);
        }
        if (hpPercent < 30) {
            g.setColor(255, 0, 0);
        }
        g.fillRect(x + 16, y + 6, (int) hpPercent, 9);
        Gamestate.font.PrintString(g, 0, y + 7, "HP", 0, 0, Gamestate.fontColor);
        Gamestate.font.PrintString(g, x + 118, y + 6, Gamestate.theHero.getHP() + "/" + Gamestate.theHero.getMaxHP(), 0, 0, Gamestate.fontColor);
    }

    public synchronized void updateImage(boolean drawbox) {
        Graphics graphics = consoleImage.getGraphics();
        int x = 0, y = 0;
        String theString = buffer.toString();
        int charsleft = theString.length();
        if (drawbox) {
            graphics.setColor(Gamestate.uiBackgroundColor);
            graphics.fillRect(x, y, width, height);
        }
        int pixPerChar = charwidth;
        int currentChar = 0;
        int lastChar = 0;
        int line = 0;
        int strLength = theString.length();
        while (charsleft > 0) {
            currentChar += width / pixPerChar;
            if (currentChar >= strLength - 1) {
                currentChar = strLength - 1;
            } else {
                while (true) {
                    char c = theString.charAt(currentChar);
                    if (c != ' ') {
                        if (currentChar == 0) {
                            break;
                        }
                        currentChar--;
                    } else {
                        break;
                    }
                }
            }
            Gamestate.font.PrintString(graphics, x, y + line * 9 + 2, theString.substring(lastChar, currentChar + 1), 0, Font59.LEFT, Gamestate.fontColor);
            charsleft -= (currentChar - lastChar);
            lastChar = currentChar + 1;
            line++;
            if (currentChar == strLength - 1) {
                break;
            }
        }
    }

    public synchronized void draw(Graphics graphics, Font59 font, int x, int y, boolean drawbox) {
        graphics.setColor(Gamestate.uiBackgroundColor);
        graphics.drawLine(x, y - 2, width, y - 2);
        graphics.setColor(Gamestate.uiDetailColor);
        graphics.drawLine(x, y - 1, width, y - 1);
        graphics.drawImage(consoleImage, x, y, 0);
    }

    public synchronized String getContents() {
        return buffer.toString();
    }

    public synchronized void dumpConsole() {
        System.err.println("---" + buffer.toString() + "---\n");
    }
}
