package org.thechiselgroup.choosel.workbench.client.command.ui;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.IsWidget;

public class CommandPresenterTest {

    private interface CommandDisplay extends HasClickHandlers, IsWidget {
    }

    protected ClickHandler clickHandler;

    @Mock
    private Command command;

    @Mock
    private CommandDisplay display;

    private CommandPresenter presenter;

    @Test
    public void callExecuteOnClick() {
        clickHandler.onClick(new ClickEvent() {
        });
        verify(command, times(1)).execute();
    }

    @Test
    public void initialState() {
        verify(display).addClickHandler(any(ClickHandler.class));
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(display.addClickHandler(any(ClickHandler.class))).thenAnswer(new Answer<HandlerRegistration>() {

            @Override
            public HandlerRegistration answer(InvocationOnMock invocation) {
                clickHandler = (ClickHandler) invocation.getArguments()[0];
                return null;
            }
        });
        presenter = new CommandPresenter(display, command);
        presenter.init();
    }
}
