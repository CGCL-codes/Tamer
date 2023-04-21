package net.sourceforge.watershed;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;

/**
 *  WatershedStructure contains the pixels
 *  of the image ordered according to their
 *  grayscale value with a direct access to their
 *  neighbours.
 *  
 **/
public class WatershedStructure {

    private ArrayList watershedStructure;

    public WatershedStructure(short[] pixels, int width, int height) {
        Rectangle r = new Rectangle(0, 0, width, height);
        int offset, topOffset, bottomOffset, i;
        watershedStructure = new ArrayList(r.width * r.height);
        for (int y = r.y; y < (r.y + r.height); y++) {
            offset = y * width;
            for (int x = r.x; x < (r.x + r.width); x++) {
                i = offset + x;
                int indiceY = y - r.y;
                int indiceX = x - r.x;
                watershedStructure.add(new WatershedPixel(indiceX, indiceY, pixels[i]));
            }
        }
        for (int y = 0; y < r.height; y++) {
            offset = y * width;
            topOffset = offset + width;
            bottomOffset = offset - width;
            for (int x = 0; x < r.width; x++) {
                WatershedPixel currentPixel = (WatershedPixel) watershedStructure.get(x + offset);
                if (x + 1 < r.width) {
                    currentPixel.addNeighbour((WatershedPixel) watershedStructure.get(x + 1 + offset));
                    if (y - 1 >= 0) currentPixel.addNeighbour((WatershedPixel) watershedStructure.get(x + 1 + bottomOffset));
                    if (y + 1 < r.height) currentPixel.addNeighbour((WatershedPixel) watershedStructure.get(x + 1 + topOffset));
                }
                if (x - 1 >= 0) {
                    currentPixel.addNeighbour((WatershedPixel) watershedStructure.get(x - 1 + offset));
                    if (y - 1 >= 0) currentPixel.addNeighbour((WatershedPixel) watershedStructure.get(x - 1 + bottomOffset));
                    if (y + 1 < r.height) currentPixel.addNeighbour((WatershedPixel) watershedStructure.get(x - 1 + topOffset));
                }
                if (y - 1 >= 0) currentPixel.addNeighbour((WatershedPixel) watershedStructure.get(x + bottomOffset));
                if (y + 1 < r.height) currentPixel.addNeighbour((WatershedPixel) watershedStructure.get(x + topOffset));
            }
        }
        Collections.sort(watershedStructure);
    }

    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < watershedStructure.size(); i++) {
            ret.append(((WatershedPixel) watershedStructure.get(i)).toString());
            ret.append("\n");
            ret.append("Neighbours :\n");
            ArrayList neighbours = ((WatershedPixel) watershedStructure.get(i)).getNeighbours();
            for (int j = 0; j < neighbours.size(); j++) {
                ret.append(((WatershedPixel) neighbours.get(j)).toString());
                ret.append("\n");
            }
            ret.append("\n");
        }
        return ret.toString();
    }

    public int size() {
        return watershedStructure.size();
    }

    public WatershedPixel get(int i) {
        return (WatershedPixel) watershedStructure.get(i);
    }
}
