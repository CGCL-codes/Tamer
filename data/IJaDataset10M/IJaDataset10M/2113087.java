package com.luxmedien.jbox.vo.html;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class XhtmlTable {

    private SortedSet<TdWrappedImage> tdList;

    private int imagesPerRow = 4;

    public SortedSet<TdWrappedImage> getTdList() {
        if (tdList == null) {
            tdList = new TreeSet<TdWrappedImage>();
        }
        return tdList;
    }

    public void setTdList(SortedSet<TdWrappedImage> pTdList) {
        tdList = pTdList;
    }

    public StringBuffer toHtml() {
        StringBuffer lRenderedHTML = new StringBuffer();
        lRenderedHTML.append("<table>");
        lRenderedHTML.append("\n");
        lRenderedHTML.append("<tr>");
        lRenderedHTML.append("\n");
        int lCounter = 0;
        if (tdList != null) for (final TdWrappedImage lWrappedImage : tdList) {
            lRenderedHTML.append(lWrappedImage.toHtml());
            lCounter++;
            if (lCounter >= imagesPerRow) {
                lRenderedHTML.append("</tr><tr>");
                lRenderedHTML.append("\n");
                lCounter = 0;
            }
        }
        lRenderedHTML.append("</tr>");
        lRenderedHTML.append("\n");
        lRenderedHTML.append("</table>");
        lRenderedHTML.append("\n");
        return lRenderedHTML;
    }

    public int getImagesPerRow() {
        return imagesPerRow;
    }

    public void setImagesPerRow(int pImagesPerRow) {
        imagesPerRow = pImagesPerRow;
    }
}
