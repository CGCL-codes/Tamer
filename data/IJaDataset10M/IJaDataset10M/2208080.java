package org.eesgmbh.gimv.client.view;

import org.eesgmbh.gimv.client.presenter.BoundsShiftPresenter;
import org.eesgmbh.gimv.client.presenter.BoundsShiftPresenter.View;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Widget;

/**
 * A trivial implementation of the {@link View} interface
 * that contains a {@link Widget} that implements the {@link HasClickHandlers}
 * interface.
 *
 * @author Christian Seewald - EES GmbH - c.seewald@ees-gmbh.de
 *
 */
public class BoundsShiftViewImpl implements BoundsShiftPresenter.View {

    private final HasClickHandlers widget;

    public BoundsShiftViewImpl(HasClickHandlers widget) {
        this.widget = widget;
    }

    public void addClickHandler(ClickHandler clickHandler) {
        this.widget.addClickHandler(clickHandler);
    }
}
