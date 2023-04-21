package org.eesgmbh.gimv.client.view;

import org.eesgmbh.gimv.client.presenter.ImageMoveOrZoomToggleButtonPresenter;
import org.eesgmbh.gimv.client.presenter.ImageMoveOrZoomToggleButtonPresenter.View;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ToggleButton;

/**
 * An implementation of {@link View} that uses two {@link ToggleButton}.
 *
 * @author Christian Seewald - EES GmbH - c.seewald@ees-gmbh.de
 */
public class ImageMoveOrZoomToggleButtonViewImpl implements ImageMoveOrZoomToggleButtonPresenter.View {

    private final ToggleButton moveToggleButton;

    private final ToggleButton zoomToggleButton;

    /**
	 * Instantiates the view impl.
	 *
	 * @param moveToggleButton
	 * @param zoomToggleButton
	 */
    public ImageMoveOrZoomToggleButtonViewImpl(ToggleButton moveToggleButton, ToggleButton zoomToggleButton) {
        this.moveToggleButton = moveToggleButton;
        this.zoomToggleButton = zoomToggleButton;
    }

    public void addMoveClickHandler(ClickHandler clickHandler) {
        this.moveToggleButton.addClickHandler(clickHandler);
    }

    public void addZoomClickHandler(ClickHandler clickHandler) {
        this.zoomToggleButton.addClickHandler(clickHandler);
    }

    public void toggleMove() {
        this.zoomToggleButton.setDown(false);
        this.moveToggleButton.setDown(true);
    }

    public void toggleZoom() {
        this.moveToggleButton.setDown(false);
        this.zoomToggleButton.setDown(true);
    }
}
