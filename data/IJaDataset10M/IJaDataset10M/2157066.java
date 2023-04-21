package org.stamppagetor.image;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;

/**
 * Image Picker class which uses polygon simplifying for finding crop areas. 
 */
public class ImagePickerPolygonSimplify implements ImagePickerInterface {

    private static final byte STA_TRANSPARENT = 0;

    private static final byte STA_IMAGE_PIXEL = 1;

    private static final byte STA_IMAGE_EDGE = 2;

    private static final byte STA_OUTSIDE_IMAGE = 3;

    private static final int PHASE_NOT_STARTED = 0;

    private static final int PHASE_LOADING = 1;

    private static final int PHASE_PREPARATIONS = 2;

    private static final int PHASE_FIND_ALPHA = 3;

    private static final int PHASE_MARK_ALPHA = 4;

    private static final int PHASE_FIND_STAMPS = 5;

    private static final int PHASE_CREATE_IMAGES = 6;

    private static final int PHASE_READY = 7;

    private static final int PHASE_FAILED = 8;

    /** phase names */
    private static final String[] PHASE_NAMES = { "Not started", "Loading image", "Preparations", "Findind background color", "Marking alpha color", "Find stamp areas", "Creating stamp images", "Ready", "Failed" };

    /** Phase effort steps */
    private static final int[] PHASE_TIMES = { 0, 40, 60, 70, 80, 85, 100, 100, 100 };

    /** Name of source image file */
    private File sourceFileName = null;

    /** Alpha color */
    private int alphaColor = 0;

    /** Original image */
    private BufferedImage origImage = null;

    /** Display and working image */
    private BufferedImage dispImage = null;

    /** Working image RGB array */
    private int[][] rgbBuffer = null;

    /** Working image status array */
    private byte[][] staBuffer = null;

    /** Crop areas. One area per a sub image */
    private final Vector<ImageArea> areas = new Vector<ImageArea>();

    /** Sub images */
    private final Vector<BufferedImage> images = new Vector<BufferedImage>();

    private final ColorCube colorCube = new ColorCube(4);

    /** @see org.stamppagetor.image.ImagePickerInterface#getImages() */
    public Vector<BufferedImage> getImages() {
        return this.images;
    }

    /** @see org.stamppagetor.image.ImagePickerInterface#getOriginalImage() */
    public BufferedImage getOriginalImage() {
        return this.dispImage;
    }

    /** @see org.stamppagetor.image.ImagePickerInterface#getPhaseName(int) */
    public String getPhaseName(int phaseIndex) {
        return PHASE_NAMES[phaseIndex];
    }

    /** @see org.stamppagetor.image.ImagePickerInterface#isFinalPhase(int) */
    public boolean isFinalPhase(int phaseIndex) {
        return (phaseIndex == PHASE_READY) || (phaseIndex == PHASE_FAILED);
    }

    /** @see org.stamppagetor.image.ImagePickerInterface#process(org.stamppagetor.image.ImagePickerProgressListener) */
    public void process(ImagePickerProgressListener listener) {
        try {
            for (int phase = PHASE_NOT_STARTED; phase != PHASE_READY; phase++) {
                runPhase(phase, listener);
                listener.imagePickerProgress(PHASE_TIMES[phase]);
            }
            listener.imagePickerPhase(PHASE_READY);
        } catch (IOException ex) {
            listener.imagePickerPhase(PHASE_FAILED);
        }
    }

    /** @see org.stamppagetor.image.ImagePickerInterface#setSourceFile(java.io.File) */
    public void setSourceFile(File file) {
        this.sourceFileName = file;
    }

    /** @see org.stamppagetor.image.ImagePickerInterface#getCropPolygons() */
    public Vector<Polygon> getCropPolygons() {
        final Vector<Polygon> res = new Vector<Polygon>();
        for (ImageArea area : this.areas) {
            res.add(area.createPolygon(1.0));
        }
        return res;
    }

    /** run a phase */
    private void runPhase(int phase, ImagePickerProgressListener listener) throws IOException {
        listener.imagePickerPhase(phase);
        switch(phase) {
            case PHASE_LOADING:
                loadingSourceImage();
                break;
            case PHASE_PREPARATIONS:
                prepareWorkingData();
                break;
            case PHASE_FIND_ALPHA:
                this.colorCube.reset();
                this.colorCube.countColors(this.rgbBuffer);
                this.alphaColor = this.colorCube.findMaxColor();
                this.colorCube.buildAlphaCube(this.alphaColor);
                break;
            case PHASE_MARK_ALPHA:
                markAlpha(this.rgbBuffer, this.staBuffer, this.alphaColor);
                break;
            case PHASE_FIND_STAMPS:
                findAreas(this.staBuffer);
                increaseAreas();
                removeUnwantedShapes();
                break;
            case PHASE_CREATE_IMAGES:
                sampleImages(this.alphaColor);
                updateDisplayImage();
                break;
        }
    }

    /** 
	 * This method load the source image.
	 * The method setup several object variables.
	 * 
	 * @throws IOException
	 */
    private void loadingSourceImage() throws IOException {
        this.origImage = ImageIO.read(this.sourceFileName);
        final int origWidth = this.origImage.getWidth();
        final int origHeight = this.origImage.getHeight();
        final int w = 768;
        final int h = w * origHeight / origWidth;
        this.dispImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        this.rgbBuffer = new int[w][h];
        this.staBuffer = new byte[w][h];
        final Graphics g = this.dispImage.getGraphics();
        g.drawImage(this.origImage, 0, 0, w, h, 0, 0, origWidth, origHeight, null);
    }

    /** 
	 * This prepares the working data
	 */
    private void prepareWorkingData() {
        int x, y;
        final int h = this.dispImage.getHeight();
        final int w = this.dispImage.getWidth();
        final BufferedImage img = this.dispImage;
        final int[][] rgb = this.rgbBuffer;
        for (y = 0; y < h; y++) {
            for (x = 0; x < w; x++) {
                rgb[x][y] = img.getRGB(x, y) & 0x00F8F8F8;
            }
        }
        blurArrayImage(rgb, w, h);
        for (y = 0; y < h; y++) {
            for (x = 0; x < w; x++) {
                rgb[x][y] = rgb[x][y] & 0xFCFCFC;
            }
        }
    }

    /**
	 * Blur the given RGB array image
	 * 
	 * @param rgb
	 * @param w
	 * @param h
	 */
    private void blurArrayImage(final int[][] rgb, final int w, final int h) {
        int x, y;
        for (y = 1; y < h - 1; y++) {
            for (x = 1; x < w - 1; x++) {
                final int c = ((rgb[x - 1][y] & 0xFCFCFC) >> 2) + ((rgb[x + 1][y] & 0xFCFCFC) >> 2) + ((rgb[x][y - 1] & 0xFCFCFC) >> 2) + ((rgb[x][y + 1] & 0xFCFCFC) >> 2);
                rgb[x][y] = ((rgb[x][y] & 0xFEFEFE) >> 1) + ((c & 0xFEFEFE) >> 1);
            }
        }
    }

    /** 
	 * Updates the display image. So it tells something about the process
	 */
    private void updateDisplayImage() {
        int x, y;
        final int h = this.dispImage.getHeight();
        final int w = this.dispImage.getWidth();
        final BufferedImage img = this.dispImage;
        final int[][] rgb = this.rgbBuffer;
        for (y = 0; y < h; y++) {
            for (x = 0; x < w; x++) {
                final int lvl = this.colorCube.getAlphaLevel(rgb[x][y]) * 32;
                if (lvl > 0) img.setRGB(x, y, 0xFF000000 | (lvl << 16) | (lvl << 8) | (lvl << 0)); else img.setRGB(x, y, 0xFFFFA0A0);
            }
        }
    }

    /**
	 * This method marks pixels with alpha color on the status array
	 *  
	 * @param rgb    image array
	 * @param sta    status array
	 * @param alpha  alpha color (RGB)
	 */
    private void markAlpha(final int[][] rgb, final byte[][] sta, int alpha) {
        final int h = rgb[0].length;
        final int w = rgb.length;
        int x, y;
        for (y = 0; y < h; y++) {
            for (x = 0; x < w; x++) {
                if (this.colorCube.getAlphaLevel(rgb[x][y]) > 0) sta[x][y] = STA_TRANSPARENT; else sta[x][y] = STA_IMAGE_PIXEL;
            }
        }
    }

    /**
	 * This is blood fill routine.
	 * It fills 'from' value pixels of status array with 'to' value. Starts from point x,y.
	 *  
	 * @param sta  status array
	 * @param x    stating point x coordinate
	 * @param y    stating point y coordinate
	 * @param w    width of image
	 * @param h    height of image
	 * @param from status of target area
	 * @param to   new status for target area 
	 */
    private void bloodAlpha(final byte[][] sta, int x, int y, final int w, final int h, final byte from, final byte to) {
        if (sta[x][y] != from) return;
        final int[] xp = new int[w * h];
        final int[] yp = new int[w * h];
        int sp = 0;
        xp[sp] = x;
        yp[sp] = y;
        sp++;
        while (sp > 0) {
            sp--;
            x = xp[sp];
            y = yp[sp];
            if (sta[x][y] == from) sta[x][y] = to;
            if ((x > 0) && (sta[x - 1][y] == from)) {
                sta[x - 1][y] = to;
                xp[sp] = x - 1;
                yp[sp] = y;
                sp++;
            }
            if ((y > 0) && (sta[x][y - 1] == from)) {
                sta[x][y - 1] = to;
                xp[sp] = x;
                yp[sp] = y - 1;
                sp++;
            }
            if ((x < w - 1) && (sta[x + 1][y] == from)) {
                sta[x + 1][y] = to;
                xp[sp] = x + 1;
                yp[sp] = y;
                sp++;
            }
            if ((y < h - 1) && (sta[x][y + 1] == from)) {
                sta[x][y + 1] = to;
                xp[sp] = x;
                yp[sp] = y + 1;
                sp++;
            }
        }
    }

    /**
	 * Creates ImageArea from the data of status array. 
	 *  
	 * @param sta  status array
	 * @param x    starting point of ImageArea
	 * @param y    starting point of ImageArea
	 * @return the new ImageArea
	 */
    private static ImageArea vectorize(final byte[][] sta, final int x, final int y) {
        final int EAST = 0;
        final int SOUTH = 1;
        final int WEST = 2;
        final int NORTH = 3;
        final int h = sta[0].length;
        final int w = sta.length;
        int xp = x;
        int yp = y;
        int dir = EAST;
        final ImageArea area = new ImageArea();
        area.addPoint(new Point(xp, yp));
        do {
            final boolean northIsPossible = (yp > 0) && (sta[xp][yp - 1] != STA_TRANSPARENT);
            final boolean eastIsPossible = ((xp + 1) < w) && (sta[xp + 1][yp] != STA_TRANSPARENT);
            final boolean southIsPossible = ((yp + 1) < h) && (sta[xp][yp + 1] != STA_TRANSPARENT);
            final boolean westIsPossible = (xp > 0) && (sta[xp - 1][yp] != STA_TRANSPARENT);
            switch(dir) {
                case EAST:
                    if (northIsPossible) {
                        yp--;
                        dir = NORTH;
                    } else if (eastIsPossible) {
                        xp++;
                        dir = EAST;
                    } else if (southIsPossible) {
                        yp++;
                        dir = SOUTH;
                    } else if (westIsPossible) {
                        xp--;
                        dir = WEST;
                    }
                    break;
                case SOUTH:
                    if (eastIsPossible) {
                        xp++;
                        dir = EAST;
                    } else if (southIsPossible) {
                        yp++;
                        dir = SOUTH;
                    } else if (westIsPossible) {
                        xp--;
                        dir = WEST;
                    } else if (northIsPossible) {
                        yp--;
                        dir = NORTH;
                    }
                    break;
                case WEST:
                    if (southIsPossible) {
                        yp++;
                        dir = SOUTH;
                    } else if (westIsPossible) {
                        xp--;
                        dir = WEST;
                    } else if (northIsPossible) {
                        yp--;
                        dir = NORTH;
                    } else if (eastIsPossible) {
                        xp++;
                        dir = EAST;
                    }
                    break;
                case NORTH:
                    if (westIsPossible) {
                        xp--;
                        dir = WEST;
                    } else if (northIsPossible) {
                        yp--;
                        dir = NORTH;
                    } else if (eastIsPossible) {
                        xp++;
                        dir = EAST;
                    } else if (southIsPossible) {
                        yp++;
                        dir = SOUTH;
                    }
                    break;
            }
            sta[xp][yp] = STA_IMAGE_EDGE;
            area.addPoint(new Point(xp, yp));
        } while (!((x == xp) && (y == yp)));
        return area;
    }

    /** 
	 * Finds imageAreas from the status array 
	 */
    private void findAreas(final byte[][] sta) {
        int x = 0;
        int y = 0;
        final int h = sta[0].length;
        final int w = sta.length;
        final int sizeLimit = (h + w) / 6;
        for (y = 0; y < h; y++) {
            for (x = 0; x < w; x++) {
                if (sta[x][y] == STA_IMAGE_PIXEL) {
                    final ImageArea area = vectorize(sta, x, y);
                    final int orgSize = area.getSize();
                    if (orgSize > sizeLimit) {
                        while (area.removeInnerCorners() > 0) {
                        }
                        while (area.joinPoints() > 0) {
                        }
                        while (area.removeInnerCorners() > 0) {
                        }
                        area.simplifyLines();
                        area.joinPoints();
                        area.simplifyLines();
                        final Point center = area.getMiddlePoint();
                        bloodAlpha(sta, center.x, center.y, w, h, STA_TRANSPARENT, STA_IMAGE_PIXEL);
                        bloodAlpha(sta, center.x, center.y, w, h, STA_IMAGE_PIXEL, STA_TRANSPARENT);
                        bloodAlpha(sta, center.x, center.y, w, h, STA_TRANSPARENT, STA_IMAGE_EDGE);
                        this.areas.add(area);
                    }
                }
            }
        }
    }

    /** 
	 * Crops RGB-array image from the source image.
	 * 
	 * @param area  - Crop area
	 * @param alpha - pixels that are ouside of the source image will got this color. 
	 * @return result RGB-array
	 */
    private int[][] sampleImage(ImageArea area, int alpha) {
        final int maxIndex = area.findIndexOfLongestLine();
        final Point a = area.getPoint((maxIndex + 0) % 4);
        final Point b = area.getPoint((maxIndex + 1) % 4);
        final Point c = area.getPoint((maxIndex + 2) % 4);
        final Point d = area.getPoint((maxIndex + 3) % 4);
        final double lab = a.distance(b);
        final double lbc = b.distance(c);
        final double lda = d.distance(a);
        final int w = 256;
        final int h = (int) (256f * (lbc + lda) / 2f / lab);
        final double dw = w;
        final double dh = h;
        final double ax = a.getX();
        final double bx = b.getX();
        final double cx = c.getX();
        final double dx = d.getX();
        final double ay = a.getY();
        final double by = b.getY();
        final double cy = c.getY();
        final double dy = d.getY();
        final int origWidth = this.origImage.getWidth(null);
        final int origHeight = this.origImage.getHeight(null);
        final int dispWidth = this.dispImage.getWidth();
        final int dispHeight = this.dispImage.getHeight();
        final int[][] rgb = new int[w][h];
        double kx, ky;
        int px, py;
        int x, y;
        for (x = 0; x < w; x++) {
            kx = x / dw;
            for (y = 0; y < h; y++) {
                ky = y / dh;
                px = (int) (origWidth * (((1f - kx) * ax + kx * bx) * (1f - ky) + ((1f - kx) * dx + kx * cx) * ky) / dispWidth);
                py = (int) (origHeight * (((1f - ky) * ay + ky * dy) * (1f - kx) + ((1f - ky) * by + ky * cy) * kx) / dispHeight);
                if ((px >= 0) && (py >= 0) && (px < origWidth) && (py < origHeight)) {
                    rgb[x][y] = this.origImage.getRGB(px, py);
                } else {
                    rgb[x][y] = alpha;
                }
            }
        }
        return rgb;
    }

    /**
	 * This method crops images from the source images.
	 * 
	 * @param alpha
	 */
    private void sampleImages(int alpha) {
        this.images.clear();
        for (ImageArea area : this.areas) {
            if (area.getSize() == 4) {
                final int[][] sample = sampleImage(area, alpha);
                final int w = sample.length;
                final int h = sample[0].length;
                final byte[][] sta = new byte[w][h];
                markAlpha(sample, sta, alpha);
                bloodAlpha(sta, 1, 1, w, h, STA_TRANSPARENT, STA_OUTSIDE_IMAGE);
                final BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                int x, y;
                for (x = 0; x < w; x++) {
                    for (y = 0; y < h; y++) {
                        if (sta[x][y] == STA_OUTSIDE_IMAGE) img.setRGB(x, y, 0x00FFFFFF); else img.setRGB(x, y, sample[x][y] | 0xFF000000);
                    }
                }
                this.images.add(img);
            }
        }
    }

    /**
	 * removes all other shapes except 4 corners.
	 */
    private void removeUnwantedShapes() {
        int i = 0;
        while (i < this.areas.size()) {
            final ImageArea area = this.areas.elementAt(i);
            if (area.getSize() != 4) {
                this.areas.remove(i);
            } else {
                i++;
            }
        }
    }

    /** increases size of all areas */
    private void increaseAreas() {
        for (ImageArea area : this.areas) {
            area.increaseArea(10);
        }
    }
}

/**
 * Helper class for solving background colors  
 */
class ColorCube {

    private final int bitsPerColor;

    private final int shift;

    private final int reso;

    private final int mask;

    private final int[][][] colorCube;

    private final byte[][][] alphaCube;

    /**
	 * constructor
	 * 
	 * @param bitsForOneColor - use value 4. High effect to memory usage.
	 */
    public ColorCube(int bitsForOneColor) {
        this.bitsPerColor = bitsForOneColor;
        this.shift = 8 - this.bitsPerColor;
        this.reso = 256 >> this.shift;
        this.mask = this.reso - 1;
        this.colorCube = new int[this.reso][this.reso][this.reso];
        this.alphaCube = new byte[this.reso][this.reso][this.reso];
    }

    /**
	 * Reset the object
	 */
    public void reset() {
        final int resolution = this.reso;
        int r, g, b;
        for (r = 0; r < resolution; r++) {
            for (g = 0; g < resolution; g++) {
                for (b = 0; b < resolution; b++) {
                    this.colorCube[r][g][b] = 0;
                    this.alphaCube[r][g][b] = 0;
                }
            }
        }
    }

    /**
	 * Counts colors from the given array
	 */
    public void countColors(int[][] rgb) {
        final int h = rgb[0].length;
        final int w = rgb.length;
        final int shft = this.shift;
        final int msk = this.mask;
        int x, y, c;
        int r, g, b;
        for (x = 0; x < w; x++) {
            for (y = 0; y < h; y++) {
                c = rgb[x][y];
                r = (c >> (16 + shft)) & msk;
                g = (c >> (8 + shft)) & msk;
                b = (c >> (0 + shft)) & msk;
                this.colorCube[r][g][b]++;
            }
        }
    }

    /**
	 * Finds the most used color area
	 * 
	 * @return
	 */
    public int findMaxColor() {
        final int resolution = this.reso;
        int r, g, b;
        int rm, gm, bm;
        long maxCount, count;
        maxCount = 0;
        rm = gm = bm = 0;
        for (r = 0; r < resolution; r++) {
            for (g = 0; g < resolution; g++) {
                for (b = 0; b < resolution; b++) {
                    count = this.colorCube[r][g][b];
                    if (count > maxCount) {
                        maxCount = count;
                        rm = r;
                        gm = g;
                        bm = b;
                    }
                }
            }
        }
        return (rm << (16 + this.shift)) + (gm << (8 + this.shift)) + (bm << (0 + this.shift));
    }

    /**
	 * Get alpha level of the given color
	 * 
	 * @param rgb
	 * @return alpha level. Bigger value means that color is closer to background color.
	 */
    public int getAlphaLevel(int rgb) {
        final int r = (rgb >> (16 + this.shift)) & this.mask;
        final int g = (rgb >> (8 + this.shift)) & this.mask;
        final int b = (rgb >> (0 + this.shift)) & this.mask;
        return this.alphaCube[r][g][b];
    }

    /** 
	 * Counts alpha levels for every color area.
	 * 
	 * @param alpha
	 */
    public void buildAlphaCube(int alpha) {
        final int ar = (alpha >> (16 + this.shift)) & this.mask;
        final int ag = (alpha >> (8 + this.shift)) & this.mask;
        final int ab = (alpha >> (0 + this.shift)) & this.mask;
        for (int i = -50; i < 50; i++) {
            final int r = ar + ar * i / 100;
            final int g = ag + ag * i / 100;
            final int b = ab + ab * i / 100;
            if ((r >= 0) && (g >= 0) && (b >= 0) && (r < this.reso) && (g < this.reso) && (b < this.reso)) {
                if (this.alphaCube[r][g][b] == 0) this.alphaCube[r][g][b]++;
            }
        }
        addAlphaLevelOfRadius(ar, ag, ab, 3, 1);
        addAlphaLevelOfRadius(ar, ag, ab, 2, 1);
        addAlphaLevelOfRadius(ar, ag, ab, 1, 1);
        this.alphaCube[ar][ag][ab] += 1;
    }

    /**
	 * increases alpha level of points near the point (ar,ag,ab) by value inc.
	 * @param ar
	 * @param ag
	 * @param ab
	 * @param rad
	 * @param inc
	 */
    private void addAlphaLevelOfRadius(int ar, int ag, int ab, int rad, int inc) {
        final int r1 = Math.max(ar - rad, 0);
        final int g1 = Math.max(ag - rad, 0);
        final int b1 = Math.max(ab - rad, 0);
        final int r2 = Math.min(ar + rad, this.reso - 1);
        final int g2 = Math.min(ag + rad, this.reso - 1);
        final int b2 = Math.min(ab + rad, this.reso - 1);
        int r, g, b;
        for (r = r1; r <= r2; r++) {
            for (g = g1; g <= g2; g++) {
                for (b = b1; b <= b2; b++) {
                    this.alphaCube[r][g][b] += inc;
                }
            }
        }
    }
}
