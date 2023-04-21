package net.sourceforge.simpleworklog.client.gwt.mocks;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Widget;
import net.sourceforge.simpleworklog.client.gwt.presenter.DashboardPresenter;
import net.sourceforge.simpleworklog.client.mocks.MockHasClickHandlers;

/**
 * @author Ignat Alexeyenko
 *         Date: Jul 22, 2010
 */
public class MockDashboardDisplay implements DashboardPresenter.Display {

    private MockHasClickHandlers mockHasClickHandlers = new MockHasClickHandlers();

    @Override
    public Widget asWidget() {
        return null;
    }

    @Override
    public HasClickHandlers getLogoutController() {
        return mockHasClickHandlers;
    }
}
