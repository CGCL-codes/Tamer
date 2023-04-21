package game.report.srobjects;

import game.report.OOOReportRenderer;
import game.report.SROOORenderer;

public class SRTextRendererOOO implements ISRObjectRenderer {

    protected SRText srText;

    protected SROOORenderer sroooRenderer;

    public SRTextRendererOOO(SRText srText, SROOORenderer sroooRenderer) {
        this.srText = srText;
        this.sroooRenderer = sroooRenderer;
    }

    public void render() {
        String text = OOOReportRenderer.toOOOText(srText.getText());
        OOOReportRenderer.addReportRow(1);
        sroooRenderer.append("<table:table-row table:style-name=\"ro1\">\n" + "  <table:table-cell   office:value-type=\"string\">\n" + "  <text:p>" + text + "</text:p> \n" + "  </table:table-cell>\n" + "  <table:table-cell table:number-columns-repeated=\"1023\" />\n" + "  </table:table-row>");
    }
}
