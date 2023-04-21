package orbe.gui.main;

import net.sf.doolin.bus.Bus;
import net.sf.doolin.bus.Subscriber;
import net.sf.doolin.gui.core.View;
import net.sf.doolin.gui.core.action.AbstractAction;
import net.sf.doolin.gui.core.support.GUIUtils;
import orbe.gui.IViews;
import orbe.gui.context.OrbeContext;
import orbe.gui.map.OrbeContent;
import orbe.gui.map.core.OrbeControler;
import orbe.gui.message.OrbeMessageContextChanged;
import orbe.gui.message.OrbeMessageContextDirty;

/**
 * Action for the main frame.
 * 
 * @author Damien Coraboeuf
 * @version $Id: AbstractActionMain.java,v 1.1 2006/10/03 13:46:12 guinnessman
 *          Exp $
 */
public abstract class AbstractActionMain extends AbstractAction {

    public AbstractActionMain() {
        Bus.get().subscribe(OrbeMessageContextChanged.class, new Subscriber<OrbeMessageContextChanged>() {

            public void receive(OrbeMessageContextChanged message) {
                setView(getMainFrame());
                onContextChanged();
            }
        });
        Bus.get().subscribe(OrbeMessageContextDirty.class, new Subscriber<OrbeMessageContextDirty>() {

            public void receive(OrbeMessageContextDirty message) {
                OrbeContext context = message.getValue();
                if (context == getContext()) {
                    setView(getMainFrame());
                    onContextDirtyChanged();
                }
            }
        });
    }

    protected OrbeControler getControler() {
        OrbeContent orbeContent = (OrbeContent) getView();
        return orbeContent.getPanel();
    }

    protected OrbeContext getContext() {
        return (OrbeContext) getViewData();
    }

    protected void onContextDirtyChanged() {
    }

    protected void onContextChanged() {
    }

    protected View getMainFrame() {
        return GUIUtils.getViewManager().getOpenedView(IViews.ID_FRAME_MAIN);
    }
}
