package de.schwarzrot.image.support;

/**
 * An efficient color quantization algorithm, adapted from the C++
 * implementation quantize.c in <a
 * href="http://www.imagemagick.org/">ImageMagick</a>. The pixels for an image
 * are placed into an oct tree. The oct tree is reduced in size, and the pixels
 * from the original image are reassigned to the nodes in the reduced tree.
 * <p>
 * 
 * Here is the copyright notice from ImageMagick:
 * 
 * <pre>
 *  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 *  %  Permission is hereby granted, free of charge, to any person obtaining a    %
 *  %  copy of this software and associated documentation files (&quot;ImageMagick&quot;),  %
 *  %  to deal in ImageMagick without restriction, including without limitation   %
 *  %  the rights to use, copy, modify, merge, publish, distribute, sublicense,   %
 *  %  and/or sell copies of ImageMagick, and to permit persons to whom the       %
 *  %  ImageMagick is furnished to do so, subject to the following conditions:    %
 *  %                                                                             %
 *  %  The above copyright notice and this permission notice shall be included in %
 *  %  all copies or substantial portions of ImageMagick.                         %
 *  %                                                                             %
 *  %  The software is provided &quot;as is&quot;, without warranty of any kind, express or %
 *  %  implied, including but not limited to the warranties of merchantability,   %
 *  %  fitness for a particular purpose and noninfringement.  In no event shall   %
 *  %  E. I. du Pont de Nemours and Company be liable for any claim, damages or   %
 *  %  other liability, whether in an action of contract, tort or otherwise,      %
 *  %  arising from, out of or in connection with ImageMagick or the use or other %
 *  %  dealings in ImageMagick.                                                   %
 *  %                                                                             %
 *  %  Except as contained in this notice, the name of the E. I. du Pont de       %
 *  %  Nemours and Company shall not be used in advertising or otherwise to       %
 *  %  promote the sale, use or other dealings in ImageMagick without prior       %
 *  %  written authorization from the E. I. du Pont de Nemours and Company.       %
 *  %                                                                             %
 *  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 * </pre>
 * 
 * 
 * @version 0.90 19 Sep 2000
 * @author <a href="http://www.gurge.com/amd/">Adam Doppelt</a>
 */
public class Quantize {

    static final boolean QUICK = true;

    static final int MAX_RGB = 255;

    static final int MAX_NODES = 266817;

    static final int MAX_TREE_DEPTH = 8;

    static int SQUARES[];

    static int SHIFT[];

    static {
        SQUARES = new int[MAX_RGB + MAX_RGB + 1];
        for (int i = -MAX_RGB; i <= MAX_RGB; i++) {
            SQUARES[i + MAX_RGB] = i * i;
        }
        SHIFT = new int[MAX_TREE_DEPTH + 1];
        for (int i = 0; i < MAX_TREE_DEPTH + 1; ++i) {
            SHIFT[i] = 1 << (15 - i);
        }
    }

    /**
     * Reduce the image to the given number of colors. The pixels are reduced in
     * place.
     * 
     * @return The new color palette.
     */
    public static int[] quantizeImage(int pixels[][], int max_colors) {
        Cube cube = new Cube(pixels, max_colors);
        cube.classification();
        cube.reduction();
        cube.assignment();
        return cube.colormap;
    }

    static class Cube {

        int pixels[][];

        int max_colors;

        int colormap[];

        Node root;

        int depth;

        int colors;

        int nodes;

        Cube(int pixels[][], int max_colors) {
            this.pixels = pixels;
            this.max_colors = max_colors;
            int i = max_colors;
            for (depth = 1; i != 0; depth++) {
                i /= 4;
            }
            if (depth > 1) {
                --depth;
            }
            if (depth > MAX_TREE_DEPTH) {
                depth = MAX_TREE_DEPTH;
            } else if (depth < 2) {
                depth = 2;
            }
            root = new Node(this);
        }

        void classification() {
            int pixels[][] = this.pixels;
            int width = pixels.length;
            int height = pixels[0].length;
            for (int x = width; x-- > 0; ) {
                for (int y = height; y-- > 0; ) {
                    int pixel = pixels[x][y];
                    int red = (pixel >> 16) & 0xFF;
                    int green = (pixel >> 8) & 0xFF;
                    int blue = (pixel >> 0) & 0xFF;
                    if (nodes > MAX_NODES) {
                        System.out.println("pruning");
                        root.pruneLevel();
                        --depth;
                    }
                    Node node = root;
                    for (int level = 1; level <= depth; ++level) {
                        int id = (((red > node.mid_red ? 1 : 0) << 0) | ((green > node.mid_green ? 1 : 0) << 1) | ((blue > node.mid_blue ? 1 : 0) << 2));
                        if (node.child[id] == null) {
                            new Node(node, id, level);
                        }
                        node = node.child[id];
                        node.number_pixels += SHIFT[level];
                    }
                    ++node.unique;
                    node.total_red += red;
                    node.total_green += green;
                    node.total_blue += blue;
                }
            }
        }

        void reduction() {
            int threshold = 1;
            while (colors > max_colors) {
                colors = 0;
                threshold = root.reduce(threshold, Integer.MAX_VALUE);
            }
        }

        /**
	 * The result of a closest color search.
	 */
        static class Search {

            int distance;

            int color_number;
        }

        void assignment() {
            colormap = new int[colors];
            colors = 0;
            root.colormap();
            int pixels[][] = this.pixels;
            int width = pixels.length;
            int height = pixels[0].length;
            Search search = new Search();
            for (int x = width; x-- > 0; ) {
                for (int y = height; y-- > 0; ) {
                    int pixel = pixels[x][y];
                    int red = (pixel >> 16) & 0xFF;
                    int green = (pixel >> 8) & 0xFF;
                    int blue = (pixel >> 0) & 0xFF;
                    Node node = root;
                    for (; ; ) {
                        int id = (((red > node.mid_red ? 1 : 0) << 0) | ((green > node.mid_green ? 1 : 0) << 1) | ((blue > node.mid_blue ? 1 : 0) << 2));
                        if (node.child[id] == null) {
                            break;
                        }
                        node = node.child[id];
                    }
                    if (QUICK) {
                        pixels[x][y] = node.color_number;
                    } else {
                        search.distance = Integer.MAX_VALUE;
                        node.parent.closestColor(red, green, blue, search);
                        pixels[x][y] = search.color_number;
                    }
                }
            }
        }

        /**
	 * A single Node in the tree.
	 */
        static class Node {

            Cube cube;

            Node parent;

            Node child[];

            int nchild;

            int id;

            int level;

            int mid_red;

            int mid_green;

            int mid_blue;

            int number_pixels;

            int unique;

            int total_red;

            int total_green;

            int total_blue;

            int color_number;

            Node(Cube cube) {
                this.cube = cube;
                this.parent = this;
                this.child = new Node[8];
                this.id = 0;
                this.level = 0;
                this.number_pixels = Integer.MAX_VALUE;
                this.mid_red = (MAX_RGB + 1) >> 1;
                this.mid_green = (MAX_RGB + 1) >> 1;
                this.mid_blue = (MAX_RGB + 1) >> 1;
            }

            Node(Node parent, int id, int level) {
                this.cube = parent.cube;
                this.parent = parent;
                this.child = new Node[8];
                this.id = id;
                this.level = level;
                ++cube.nodes;
                if (level == cube.depth) {
                    ++cube.colors;
                }
                ++parent.nchild;
                parent.child[id] = this;
                int bi = (1 << (MAX_TREE_DEPTH - level)) >> 1;
                mid_red = parent.mid_red + ((id & 1) > 0 ? bi : -bi);
                mid_green = parent.mid_green + ((id & 2) > 0 ? bi : -bi);
                mid_blue = parent.mid_blue + ((id & 4) > 0 ? bi : -bi);
            }

            /**
	     * Remove this child node, and make sure our parent absorbs our pixel
	     * statistics.
	     */
            void pruneChild() {
                --parent.nchild;
                parent.unique += unique;
                parent.total_red += total_red;
                parent.total_green += total_green;
                parent.total_blue += total_blue;
                parent.child[id] = null;
                --cube.nodes;
                cube = null;
                parent = null;
            }

            /**
	     * Prune the lowest layer of the tree.
	     */
            void pruneLevel() {
                if (nchild != 0) {
                    for (int id = 0; id < 8; id++) {
                        if (child[id] != null) {
                            child[id].pruneLevel();
                        }
                    }
                }
                if (level == cube.depth) {
                    pruneChild();
                }
            }

            /**
	     * Remove any nodes that have fewer than threshold pixels. Also, as
	     * long as we're walking the tree:
	     *  - figure out the color with the fewest pixels - recalculate the
	     * total number of colors in the tree
	     */
            int reduce(int threshold, int next_threshold) {
                if (nchild != 0) {
                    for (int id = 0; id < 8; id++) {
                        if (child[id] != null) {
                            next_threshold = child[id].reduce(threshold, next_threshold);
                        }
                    }
                }
                if (number_pixels <= threshold) {
                    pruneChild();
                } else {
                    if (unique != 0) {
                        cube.colors++;
                    }
                    if (number_pixels < next_threshold) {
                        next_threshold = number_pixels;
                    }
                }
                return next_threshold;
            }

            void colormap() {
                if (nchild != 0) {
                    for (int id = 0; id < 8; id++) {
                        if (child[id] != null) {
                            child[id].colormap();
                        }
                    }
                }
                if (unique != 0) {
                    int r = ((total_red + (unique >> 1)) / unique);
                    int g = ((total_green + (unique >> 1)) / unique);
                    int b = ((total_blue + (unique >> 1)) / unique);
                    cube.colormap[cube.colors] = (((0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0));
                    color_number = cube.colors++;
                }
            }

            void closestColor(int red, int green, int blue, Search search) {
                if (nchild != 0) {
                    for (int id = 0; id < 8; id++) {
                        if (child[id] != null) {
                            child[id].closestColor(red, green, blue, search);
                        }
                    }
                }
                if (unique != 0) {
                    int color = cube.colormap[color_number];
                    int distance = distance(color, red, green, blue);
                    if (distance < search.distance) {
                        search.distance = distance;
                        search.color_number = color_number;
                    }
                }
            }

            /**
	     * Figure out the distance between this node and som color.
	     */
            static final int distance(int color, int r, int g, int b) {
                return (SQUARES[((color >> 16) & 0xFF) - r + MAX_RGB] + SQUARES[((color >> 8) & 0xFF) - g + MAX_RGB] + SQUARES[((color >> 0) & 0xFF) - b + MAX_RGB]);
            }

            @Override
            public String toString() {
                StringBuffer buf = new StringBuffer();
                if (parent == this) {
                    buf.append("root");
                } else {
                    buf.append("node");
                }
                buf.append(' ');
                buf.append(level);
                buf.append(" [");
                buf.append(mid_red);
                buf.append(',');
                buf.append(mid_green);
                buf.append(',');
                buf.append(mid_blue);
                buf.append(']');
                return new String(buf);
            }
        }
    }
}
