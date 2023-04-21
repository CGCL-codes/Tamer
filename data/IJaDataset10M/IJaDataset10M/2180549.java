package de.fhg.igd.gps.maps;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * @author Jan Peters <jpeters@igd.fhg.de>
 */
public class HtmlImageMapInfo {

    protected ArrayList entries_;

    protected boolean isCoordinateMap_;

    protected String coordinateMapLink_;

    protected String imageLink_;

    protected String name_;

    protected int width_;

    protected int height_;

    protected int border_;

    protected HtmlImageMapInfo() {
    }

    public HtmlImageMapInfo(String name, String imageLink) {
        entries_ = new ArrayList();
        name_ = name;
        imageLink_ = imageLink;
        isCoordinateMap_ = false;
        coordinateMapLink_ = null;
        width_ = -1;
        height_ = -1;
        border_ = -1;
    }

    public void setIsMap(String mapLink) {
        isCoordinateMap_ = true;
        coordinateMapLink_ = mapLink;
    }

    public void setBorder(int border) {
        border_ = border;
    }

    public void setImageSize(int width, int height) {
        width_ = width;
        height_ = height;
    }

    public boolean addEntry(HtmlImageMapEntry entry) {
        if (isCoordinateMap_ || entry == null) {
            return false;
        }
        entries_.add(entry);
        return true;
    }

    public void adjustShapes(int deltax, int deltay) {
        HtmlImageMapEntry entry;
        Iterator it;
        for (it = entries_.iterator(); it.hasNext(); ) {
            entry = (HtmlImageMapEntry) it.next();
            entry.adjustShape(deltax, deltay);
        }
    }

    public int getWidth() {
        return width_;
    }

    public int getHeight() {
        return height_;
    }

    public boolean isCoordinateMap() {
        return isCoordinateMap_;
    }

    public String toHtml() {
        HtmlImageMapEntry entry;
        StringBuffer imageMap;
        Iterator it;
        String str;
        long time;
        imageMap = new StringBuffer();
        time = System.currentTimeMillis();
        if (isCoordinateMap_) {
            imageMap.append("<a href=\"" + coordinateMapLink_ + "\">");
        }
        imageMap.append("<img src=\"" + imageLink_ + "\"");
        if (width_ != -1 && height_ != -1) {
            imageMap.append(" width=\"" + width_ + "\"");
            imageMap.append(" height=\"" + height_ + "\"");
        }
        if (border_ != -1) {
            imageMap.append(" border=\"" + border_ + "\"");
        }
        imageMap.append(" alt=\"" + name_ + "\"");
        if (isCoordinateMap_) {
            imageMap.append(" ismap>\n");
        } else {
            imageMap.append(" usemap=\"#ImageMap-" + time + "\">\n");
        }
        if (isCoordinateMap_) {
            imageMap.append("</a>");
        } else {
            imageMap.append("<map name=\"ImageMap-" + time + "\">\n");
            for (it = entries_.iterator(); it.hasNext(); ) {
                entry = (HtmlImageMapEntry) it.next();
                str = entry.toHtml();
                if (str != null) {
                    imageMap.append(str + "\n");
                }
            }
            imageMap.append("</map>\n");
        }
        return imageMap.toString();
    }

    public String toString() {
        return toHtml();
    }
}
