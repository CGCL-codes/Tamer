package jaguar;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * @author Grootswagers
 *
 */
public class JaguarRectangle extends Rectangle {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String name;

    private JaguarRectangle appTopRight;

    protected String[] scanline;

    protected String[] bwbgline;

    private JaguarRectangle logo;

    private int pc;

    private int bw;

    private String bg;

    /**
	 * 
	 */
    public JaguarRectangle(String name, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.name = name;
        appTopRight = null;
        logo = null;
        scanline = new String[height];
        pc = 0;
    }

    /**
	 * @param r JaguarRectangle
	 * @param deep boolean
	 * <p>
	 * copy constructor, set deep to true if you want a deepcopy
	 * 
	 */
    public JaguarRectangle(JaguarRectangle r, boolean deep) {
        this(r.name, r.x, r.y, r.width, r.height);
        if (deep) copyScanlines(r);
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "rect " + name + " " + x + " " + y + " " + width + " " + height;
    }

    public boolean isSameAsTile(JaguarRectangle r, int x, int y, int bw) {
        if (x + width > r.width) return false;
        if (y + height > r.height) return false;
        for (int i = 0; i < height; i++) {
            if (scanline[i] == null || r.scanline[y + i] == null) {
                if (scanline[i] != r.scanline[y + i]) return false;
            } else {
                if (bw == 0 && bg == null) {
                    if (!scanline[i].equals(r.scanline[y + i].substring(x, x + width))) return false;
                } else if (!bwbgline[i].equals(r.bwbgline[y + i].substring(x, x + width))) return false;
            }
        }
        return true;
    }

    private String makebwbg(String line, int bw, String bg) {
        if (line == null) return null;
        char[] c = line.toCharArray();
        if (bg == null) {
            for (int x = 0; x < c.length; x++) {
                int rgb = 0;
                switch(Jaguar.PALETTE_CHARS.indexOf(c[x])) {
                    case 0:
                        rgb = 0x00;
                        break;
                    case 1:
                        rgb = 0x80;
                        break;
                    case 2:
                        rgb = 0x80;
                        break;
                    case 3:
                        rgb = 0x80 * 2;
                        break;
                    case 4:
                        rgb = 0x80;
                        break;
                    case 5:
                        rgb = 0x80 * 2;
                        break;
                    case 6:
                        rgb = 0x80 * 2;
                        break;
                    case 7:
                        rgb = 0x7f * 3;
                        break;
                    case 8:
                        rgb = 0xbf * 3;
                        break;
                    case 9:
                        rgb = 0xff;
                        break;
                    case 10:
                        rgb = 0xff;
                        break;
                    case 11:
                        rgb = 0xff * 2;
                        break;
                    case 12:
                        rgb = 0xFF;
                        break;
                    case 13:
                        rgb = 0xff * 2;
                        break;
                    case 14:
                        rgb = 0xff * 2;
                        break;
                    case 15:
                        rgb = 0xff * 3;
                        break;
                }
                if (bw * 3 < rgb) c[x] = Jaguar.PALETTE_CHARS.charAt(15); else c[x] = Jaguar.PALETTE_CHARS.charAt(0);
            }
        } else {
            for (int x = 0; x < c.length; x++) {
                if (bg.indexOf(c[x]) > -1) c[x] = Jaguar.PALETTE_CHARS.charAt(15);
            }
        }
        return String.copyValueOf(c);
    }

    public void setScanline(int i, String line) {
        if (height != scanline.length) scanline = new String[height];
        if (i >= 0 && i < scanline.length) {
            if (line == null) {
                scanline[i] = null;
            } else {
                while (line.length() < width) line += " ";
                scanline[i] = line.substring(0, width);
            }
        }
    }

    /**
	 * @param y
	 * @return scanline[y]
	 */
    public String getScanline(int y) {
        if (y >= 0 && y < scanline.length) return scanline[y];
        return null;
    }

    /**
	 * @param r - JaguarRectangle where to get the scanlines
	 */
    public void copyScanlines(JaguarRectangle r) {
        if (height != scanline.length) scanline = new String[height];
        for (int i = 0; i < height; i++) scanline[i] = r.getScanline(i);
    }

    /**
	 * @return the appTopRight
	 */
    public JaguarRectangle getAppTopRight() {
        return appTopRight;
    }

    /**
	 * @param appTopRight the appTopRight to set
	 */
    public void setAppTopRight(JaguarRectangle appTopRight) {
        if (appTopRight != null) appTopRight.name = name + ".icon";
        this.appTopRight = appTopRight;
    }

    /**
	 * 
	 * @return the logo
	 */
    public JaguarRectangle getLogo() {
        return logo;
    }

    /**
	 * @param logo the logo to set
	 */
    public void setLogo(JaguarRectangle logo) {
        this.logo = logo;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @param r
	 * @param skip
	 * @param bw 
	 * @param bg 
	 * @return Rectangle with coordinates of r found or null
	 */
    public Rectangle coordinates(JaguarRectangle r, int skip, int bw, String bg) {
        if (width < r.width || height < r.height) return null;
        int c = skip;
        int h = height;
        int ph = r.height;
        int pw = r.width;
        if (bw > 0 || bg != null) {
            this.bw = 0;
            this.bg = null;
            if (bg != null && !bg.matches("[RGBCMYrgbcmy]+")) bg = bg.replace('D', Jaguar.PALETTE_CHARS.charAt(7)).replace('l', Jaguar.PALETTE_CHARS.charAt(8));
            makebwbglines(bw, bg);
            r.makebwbglines(bw, bg);
        }
        for (int y = 0; y < h - ph + 1; y++) {
            int x0 = -1;
            if ((y & 7) == 0) Jaguar.animateCaption(Jaguar.SCANNING, pc, getName());
            if (scanline[y] != null && r.scanline[0] != null) if (bw > 0) x0 = bwbgline[y].indexOf(r.bwbgline[0]); else x0 = scanline[y].indexOf(r.scanline[0]);
            while (x0 >= 0) {
                if (r.isSameAsTile(this, x0, y, bw)) if (c > 0) --c; else {
                    Rectangle d = new Rectangle(x0, y, pw, ph);
                    return d;
                }
                if (bw == 0 && bg == null) x0 = scanline[y].indexOf(r.scanline[0], x0 + 1); else x0 = bwbgline[y].indexOf(r.bwbgline[0], x0 + 1);
            }
        }
        return null;
    }

    private void makebwbglines(int bw, String bg) {
        if (bwbgline == null || bw != this.bw || bg != this.bg) {
            this.bw = bw;
            this.bg = bg;
            bwbgline = new String[scanline.length];
            for (int y = 0; y < scanline.length; y++) bwbgline[y] = makebwbg(scanline[y], bw, bg);
        }
    }

    /**
	 * @param pc the pc to set
	 */
    public void setPc(int pc) {
        this.pc = pc;
    }

    /**
	 * 
	 * @return BufferedImage or null when there is no image
	 */
    public BufferedImage getImage() {
        if (scanline == null || width == 0 || height == 0) return null;
        for (int y = 0; y < height; y++) {
            if (y >= scanline.length || scanline[y] == null) {
                return null;
            }
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        for (int y = 0; y < height; y++) {
            char[] c = scanline[y].toCharArray();
            for (int x = 0; x < width; x++) {
                int rgb = 0;
                switch(Jaguar.PALETTE_CHARS.indexOf(c[x])) {
                    case 0:
                        rgb = 0x000000;
                        break;
                    case 1:
                        rgb = 0x000080;
                        break;
                    case 2:
                        rgb = 0x008000;
                        break;
                    case 3:
                        rgb = 0x008080;
                        break;
                    case 4:
                        rgb = 0x800000;
                        break;
                    case 5:
                        rgb = 0x800080;
                        break;
                    case 6:
                        rgb = 0x808000;
                        break;
                    case 7:
                        rgb = 0x7f7f7f;
                        break;
                    case 8:
                        rgb = 0xbfbfbf;
                        break;
                    case 9:
                        rgb = 0x0000ff;
                        break;
                    case 10:
                        rgb = 0x00ff00;
                        break;
                    case 11:
                        rgb = 0x00ffff;
                        break;
                    case 12:
                        rgb = 0xFF0000;
                        break;
                    case 13:
                        rgb = 0xff00ff;
                        break;
                    case 14:
                        rgb = 0xffff00;
                        break;
                    case 15:
                        rgb = 0xffffff;
                        break;
                }
                image.setRGB(x, y, rgb);
            }
        }
        return image;
    }

    public String getText() {
        if (height < 3 || width < 3 || scanline == null) return "";
        for (int y = 0; y < height; y++) {
            if (y >= scanline.length || scanline[y] == null) {
                return "";
            }
        }
        int[] freq = colorcount();
        char[][] matrix = new char[height][width];
        int treshold = (width * height) / 3;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = freq[Jaguar.PALETTE_CHARS.indexOf(scanline[y].charAt(x))];
                matrix[y][x] = p > treshold ? ' ' : 'X';
            }
        }
        removeLongLines(matrix);
        int y1 = 0;
        String r = "";
        while (y1 < height && isEmpty(matrix, y1)) ++y1;
        while (y1 < height) {
            int y2 = y1;
            while (y2 < height && !isEmpty(matrix, y2)) ++y2;
            if (y2 - y1 > 4) r += getLineChars(matrix, y1, y2);
            y1 = y2;
            while (y1 < height && isEmpty(matrix, y1)) ++y1;
            r += "\n";
        }
        return r;
    }

    private void removeLongLines(char[][] matrix) {
        int w = width / 3;
        int h = height / 3;
        for (int y = 0; y < height; y++) {
            int x1 = 0;
            while (x1 < width && matrix[y][x1] == ' ') ++x1;
            while (x1 < width) {
                int x2 = x1;
                while (x2 < width && matrix[y][x2] != ' ') ++x2;
                if (x2 - x1 > w) {
                    for (int i = x1; i < x2; i++) matrix[y][i] = 0;
                }
                x1 = x2;
                while (x1 < width && matrix[y][x1] == ' ') ++x1;
            }
        }
        for (int x = 0; x < width; x++) {
            int y1 = 0;
            while (y1 < height && matrix[y1][x] == ' ') ++y1;
            while (y1 < height) {
                int y2 = y1;
                while (y2 < height && matrix[y2][x] != ' ') ++y2;
                if (y2 - y1 > h) {
                    for (int i = y1; i < y2; i++) matrix[i][x] = ' ';
                }
                y1 = y2;
                while (y1 < height && matrix[y1][x] == ' ') ++y1;
            }
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix[y][x] == 0) matrix[y][x] = ' ';
            }
        }
    }

    /**
	 * @return array with color frequencys
	 */
    private int[] colorcount() {
        int[] c = new int[16];
        for (int i = 0; i < 16; i++) c[i] = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int p = Jaguar.PALETTE_CHARS.indexOf(scanline[y].charAt(x));
                ++c[p];
            }
        }
        return c;
    }

    private String getLineChars(char[][] matrix, int y1, int y2) {
        int x1 = 0;
        while (x1 < width && isBlanc(matrix, y1, y2, x1)) ++x1;
        if (x1 >= width) return "";
        int x2 = width;
        while (x2 > 0 && isBlanc(matrix, y1, y2, x2 - 1)) --x2;
        if (x2 == 0) return "";
        char[][] line = copyToLine(matrix, y1, y2, x1, x2);
        int wline = line[0].length;
        String r = "";
        x1 = 0;
        int pitch = 0;
        while (x1 < wline) {
            x2 = x1;
            while (x2 < wline && !isBlanc(line, 0, y2 - y1, x2)) ++x2;
            char ch = recognizeFirst(line, x1, x2);
            if (ch == 0) {
                int x3 = onebridge(line, x1, x2);
                if (x3 < x2) ch = recognizeFirst(line, x1, x3);
                if (ch == 0) {
                    x3 = rightbracket(line, x1, x2);
                    if (x3 < x2) ch = recognizeFirst(line, x1, x3);
                    if (ch == 0) {
                        x3 = farright(line, x1, x2);
                        if (x3 < x2) ch = recognizeFirst(line, x1, x3);
                    }
                }
                x2 = x3;
            }
            int w = x2 - x1;
            if (ch == 0) r += "?"; else {
                if (ch != '\'' || !r.endsWith("\"")) r += String.valueOf(ch);
                if (w > pitch) pitch = w;
            }
            x1 = x2;
            while (x1 < wline && isBlanc(line, 0, line.length, x1)) ++x1;
            if (ch == 0 && x1 - x2 >= pitch && x1 < wline) r += " ";
        }
        return r;
    }

    private char recognizeFirst(char[][] line, int x1, int x2) {
        char[][] m7x7 = oneChar(line, x1, x2);
        return recognizeChar(m7x7);
    }

    private int onebridge(char[][] line, int x1, int x2) {
        boolean sawMoreThanOne = false;
        for (int x = x1; x < x2; x++) {
            int possible = 1;
            for (int y = 0; y < line.length && possible > 0; y++) {
                char c = line[y][x];
                switch(possible) {
                    case 1:
                        if (c != ' ') possible = 2;
                        break;
                    case 2:
                        if (c == ' ') possible = 4; else possible = 3;
                        break;
                    case 3:
                    case 4:
                        if (c != ' ') possible = 0;
                        break;
                }
            }
            if (possible > 0) {
                if (sawMoreThanOne) return x;
            } else sawMoreThanOne = true;
        }
        return x2;
    }

    private int farright(char[][] line, int x1, int x2) {
        for (int x = x1; x < x2; x++) {
            boolean blocked = false;
            int w = x;
            int col = x;
            for (int y = 0; y < line.length && !blocked; y++) {
                if (line[y][col] != ' ') {
                    if (col > x1 && line[y][col - 1] == ' ') --col; else if (col < x2 - 1 && line[y][col + 1] == ' ') {
                        ++col;
                        if (col > w) w = col;
                    } else blocked = true;
                }
            }
            if (!blocked) {
                return w;
            }
        }
        return x2;
    }

    private int rightbracket(char[][] line, int x1, int x2) {
        for (int x = x1; x < x2 - 1; x++) {
            int possible = 1;
            for (int y = 0; y < line.length && possible > 0; y++) {
                char c1 = line[y][x];
                char c2 = line[y][x + 1];
                switch(possible) {
                    case 1:
                        if (c1 == c2) {
                            if (c1 != ' ') possible = 2;
                        } else possible = 0;
                        break;
                    case 2:
                        if (c1 == ' ' && c2 != ' ') possible = 4; else if (c1 != ' ' && c2 != ' ') possible = 3; else possible = 0;
                        break;
                    case 3:
                        if (c1 == ' ' && c2 != ' ') possible = 4; else possible = 0;
                        break;
                    case 4:
                        if (c1 == ' ' && c2 != ' ') possible = 4; else if (c1 != ' ' && c2 != ' ') possible = 5; else possible = 0;
                        break;
                    case 5:
                        if (c1 == ' ' && c2 == ' ') possible = 7; else if (c1 != ' ' && c2 != ' ') possible = 6; else possible = 0;
                        break;
                    case 6:
                        if (c1 == ' ' && c2 == ' ') possible = 7; else possible = 0;
                        break;
                    case 7:
                        if (c1 == ' ' && c2 == ' ') possible = 7; else possible = 0;
                        break;
                }
            }
            if (possible > 4 && x > x1 + line.length / 3) return x;
        }
        return x2;
    }

    /**
	 * 
	 * @param matrix
	 * @param y1
	 * @param y2
	 * @param x1
	 * @param x2
	 * @return line left adjusted
	 */
    private char[][] copyToLine(char[][] matrix, int y1, int y2, int x1, int x2) {
        int w = x2 - x1;
        int h = y2 - y1;
        char[][] line = new char[h][w];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                line[y][x] = matrix[y1 + y][x1 + x];
            }
        }
        System.out.println("// +-" + String.valueOf(line[0]).replace(' ', '-').replace('X', '-') + "-+");
        for (int y = 0; y < line.length; y++) System.out.println("// | " + String.valueOf(line[y]) + " |");
        System.out.println("// +-" + String.valueOf(line[0]).replace(' ', '-').replace('X', '-') + "-+");
        return line;
    }

    private char[][] oneChar(char[][] line, int x1, int x2) {
        int w = x2 - x1;
        char[][] charMatrix = new char[line.length][w];
        for (int y = 0; y < line.length; y++) {
            for (int x = 0; x < w; x++) {
                charMatrix[y][x] = line[y][x1 + x];
            }
        }
        return charMatrix;
    }

    /**
	 * This is the hard part, we got the character located in the image but have to recognize it
	 * @param matrix
	 * @return recognized character
	 */
    private char recognizeChar(char[][] matrix) {
        return new JaguarOCR(matrix).toChar();
    }

    private boolean isBlanc(char[][] matrix, int y1, int y2, int x) {
        for (int i = y1; i < y2; i++) if (matrix[i][x] != ' ') return false;
        return true;
    }

    private boolean isEmpty(char[][] matrix, int y) {
        for (int x = 0; x < width; x++) {
            if (matrix[y][x] != ' ') return false;
        }
        return true;
    }
}
