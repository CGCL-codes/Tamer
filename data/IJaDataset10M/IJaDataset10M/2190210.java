package com.ecmdeveloper.plugin.search.figures;

import org.eclipse.draw2d.RectangleFigure;

/**
 * 
 * @author ricardo.belfor
 *
 */
public class QueryContainerFeedbackFigure extends RectangleFigure {

    public QueryContainerFeedbackFigure() {
        this.setFill(false);
        this.setXOR(true);
        setBorder(new QueryContainerFeedbackBorder());
    }
}
