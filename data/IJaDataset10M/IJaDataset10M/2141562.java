package net.sf.doolin.gui.display;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.sf.doolin.bus.support.DefaultSubscriberValidator;
import net.sf.doolin.gui.action.ActionContext;
import net.sf.doolin.gui.validation.support.ValidationSupportImpl;
import net.sf.doolin.gui.view.GUIView;
import net.sf.doolin.gui.window.GUIWindow;
import org.junit.Before;

/**
 * Root of tests for {@link DisplayStateHandler}.
 * 
 * @author Damien Coraboeuf
 * 
 */
public abstract class AbstractTestDisplayStateHandler {

    private ContextType context;

    private ActionContext actionContext;

    private GUIView<ContextType> view;

    @SuppressWarnings("unchecked")
    private GUIWindow window;

    /**
	 * Initialisation of the test application
	 */
    @SuppressWarnings("unchecked")
    @Before
    public void before() {
        this.context = new ContextType();
        this.actionContext = mock(ActionContext.class);
        when(this.actionContext.getData()).thenReturn(this.context);
        when(this.actionContext.getSubscriberValidator()).thenReturn(new DefaultSubscriberValidator());
        when(this.actionContext.getValidationSupport()).thenReturn(new ValidationSupportImpl(this.context, new DefaultSubscriberValidator()));
        this.window = mock(GUIWindow.class);
        when(this.window.isClosed()).thenReturn(false);
        this.view = mock(GUIView.class);
        when(this.view.getActionContext()).thenReturn(this.actionContext);
        when(this.view.getParentWindow()).thenReturn(this.window);
    }

    /**
	 * Creates the action context for the test
	 * 
	 * @return Action context
	 */
    protected ActionContext createActionContext() {
        return this.actionContext;
    }

    /**
	 * View
	 * 
	 * @return View
	 */
    protected GUIView<ContextType> getView() {
        return this.view;
    }
}
