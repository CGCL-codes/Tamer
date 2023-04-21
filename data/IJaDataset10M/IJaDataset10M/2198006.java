package com.itextpdf.demo.speakers;

import com.itextpdf.devoxx.properties.Dimensions;
import com.itextpdf.devoxx.properties.MyColors;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class TabColoring extends PdfPageEventHelper {

    /** Rectangle for the tab. */
    protected PdfArray tabOdd = new PdfArray();

    /** Rectangle for the tab. */
    protected PdfArray tabEven = new PdfArray();

    /** A graphics state */
    protected PdfGState state = new PdfGState();

    public TabColoring() {
        state.setFillOpacity(0.5f);
        state.setBlendMode(PdfGState.BM_MULTIPLY);
    }

    /**
	 * Changes the vertices of the tab that has to be colored.
	 * @param s the name of a tab
	 */
    public void setTabs(String s) {
        tabOdd = Dimensions.getTab(false, s);
        tabEven = Dimensions.getTab(true, s);
    }

    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte content = writer.getDirectContentUnder();
        content.saveState();
        content.setGState(state);
        content.setColorFill(MyColors.getColor("TAB"));
        PdfArray vertices = (writer.getPageNumber() % 2 == 0) ? tabEven : tabOdd;
        content.moveTo(vertices.getAsNumber(0).floatValue(), vertices.getAsNumber(1).floatValue());
        for (int v = 2; v < vertices.size(); v += 2) {
            content.lineTo(vertices.getAsNumber(v).floatValue(), vertices.getAsNumber(v + 1).floatValue());
        }
        content.fill();
        content.restoreState();
    }
}
