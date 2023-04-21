package org.dbe.composer.wfengine.bpeladmin.war.graph.bpel.controller;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import org.dbe.composer.wfengine.bpeladmin.war.graph.bpel.figure.SdlBpelActivityContainerFigure;
import org.dbe.composer.wfengine.bpeladmin.war.graph.bpel.figure.SdlBpelFigureBase;
import org.dbe.composer.wfengine.bpeladmin.war.graph.bpel.figure.SdlContainerFigure;
import org.dbe.composer.wfengine.bpeladmin.war.graph.ui.SdlIcon;
import org.dbe.composer.wfengine.bpeladmin.war.graph.ui.figure.SdlGraphFigure;

/**
 * Base controller for BPEL container type definitions.
 */
public abstract class SdlBpelContainerController extends SdlBpelControllerBase {

    /**
     * Default constructor.
     */
    public SdlBpelContainerController() {
        super();
    }

    /**
     * Overrides method to Activity container which has its icon label in the top (North)
     * and the content (container) in the bottom (Center).
     */
    protected SdlGraphFigure createFigure() {
        SdlGraphFigure figure = createContainerFigure();
        if (getStateAdornmentIconImage() != null) {
            SdlIcon stateIcon = new SdlIcon(getStateAdornmentIconImage());
            setStateImageIcon(stateIcon);
            ((SdlBpelFigureBase) figure).getLabel().add(stateIcon);
        }
        SdlGraphFigure contents = createContentFigure();
        contents.setLayout(getContentLayoutManager(contents));
        setContentFigure(contents);
        figure.add(contents, BorderLayout.CENTER);
        return figure;
    }

    /**
     * Creates and returns the main figure for this controller.
     * @return main figure for controller
     */
    protected SdlGraphFigure createContainerFigure() {
        SdlBpelActivityContainerFigure figure = new SdlBpelActivityContainerFigure(getLabelText(), getActivityIconImage());
        figure.setEvaluated(isExecuted());
        return figure;
    }

    /**
     * Creates and returns the container which holds the children.
     * @return contents figure.
     */
    protected SdlGraphFigure createContentFigure() {
        SdlGraphFigure contents = new SdlContainerFigure("CONTENTS_" + getLabelText());
        return contents;
    }

    /**
     * Returns the layout manager used for the contents figure.
     * @param aForFigure the content figure for which the layout manager is used.
     * @return layout manager
     */
    protected abstract LayoutManager getContentLayoutManager(SdlGraphFigure aForFigure);
}
