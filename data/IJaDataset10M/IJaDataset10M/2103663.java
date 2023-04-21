package newtonERP.viewers.firstStep;

import newtonERP.viewers.secondStep.MoneyViewer;
import newtonERP.viewers.secondStep.colorViewer.ColorViewer;
import newtonERP.viewers.viewables.BandDiagramViewable;

/**
 * Sert à voir les diagrammes à bandes
 * 
 * @author Guillaume Lacasse
 */
public class BandDiagramViewer {

    private static final double maxWidth = 640.0;

    /**
	 * @param entity diagramme à bande
	 * @return code html
	 */
    public static String getHtmlCode(BandDiagramViewable entity) {
        String html = "";
        double maximum = entity.getMaximumValue();
        for (String labelName : entity.getDiagramInfo().keySet()) {
            double currentValue = entity.getDiagramInfo().get(labelName);
            double currentWidth = currentValue / maximum * maxWidth;
            html += "<p>" + labelName + ": " + MoneyViewer.getHtmlCode(currentValue) + "</p>";
            html += "<div style=\"background-color:" + ColorViewer.getColor(labelName) + ";height:32px;border-style:solid;border-width:1px;width:" + Math.round(currentWidth) + "px\"></div>";
            html += "<hr />";
        }
        return html;
    }
}
