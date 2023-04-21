package org.agilercp.ui;

import static org.easymock.EasyMock.createNiceMock;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Heiko Seeberger
 */
public class BasePresenterTest {

    private final class MockPresenter extends BasePresenter<IView> {

        private boolean isAttachViewListenersCalled;

        MockPresenter(final IView view) {
            super(view);
        }

        @Override
        protected void attachViewListeners() {
            isAttachViewListenersCalled = true;
        }
    }

    private MockPresenter mockPresenter;

    private IView view;

    @Before
    public void setUp() throws Exception {
        view = createNiceMock(IView.class);
        mockPresenter = new MockPresenter(view);
    }

    @Test
    public void testBasePresenter() {
        assertTrue(mockPresenter.isAttachViewListenersCalled);
        assertNotNull(mockPresenter.getView());
        assertSame(view, mockPresenter.getView());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePresenterEx() {
        new BasePresenter<IView>(null) {

            @Override
            protected void attachViewListeners() {
            }
        };
    }
}
