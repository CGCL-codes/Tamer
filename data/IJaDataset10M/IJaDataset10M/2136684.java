package org.columba.core.gui.context;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.UIManager;
import org.columba.addressbook.gui.context.ContactContextualProvider;
import org.columba.api.command.ICommandReference;
import org.columba.api.command.IWorkerStatusController;
import org.columba.api.gui.frame.IDock;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.command.Command;
import org.columba.core.command.CommandProcessor;
import org.columba.core.command.DefaultCommandReference;
import org.columba.core.gui.context.api.IContextProvider;
import org.columba.core.gui.context.api.IContextualPanel;
import org.columba.core.logging.Logging;
import org.jdesktop.swingx.VerticalLayout;

public class ContextualPanel extends JPanel implements IContextualPanel {

    private IFrameMediator frameMediator;

    private List<IContextProvider> providerList = new Vector<IContextProvider>();

    private StackedBox box;

    private ContextualBar contextualBar;

    public ContextualPanel(IFrameMediator frameMediator) {
        super();
        this.frameMediator = frameMediator;
        contextualBar = new ContextualBar(frameMediator, this);
        setLayout(new BorderLayout());
        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        top.add(contextualBar, BorderLayout.CENTER);
        JPanel center = new JPanel();
        center.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        center.setLayout(new BorderLayout());
        box = new StackedBox();
        box.setBackground(UIManager.getColor("TextField.background"));
        JScrollPane pane = new JScrollPane(box);
        center.add(pane, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        if (Logging.DEBUG) register(new ContextDebugProvider());
        register(new ContactContextualProvider());
    }

    private void showDockingView() {
        if (frameMediator instanceof IDock) {
            ((IDock) frameMediator).showDockable(IDock.DOCKING_VIEW_CONTEXTUAL_PANEL);
        }
    }

    private void createStackedBox() {
        box.removeAll();
        Iterator<IContextProvider> it = providerList.listIterator();
        while (it.hasNext()) {
            IContextProvider p = it.next();
            p.clear();
            ResultBox resultBox = new ResultBox(p);
            box.addBox(resultBox);
        }
        validate();
        repaint();
    }

    public void search() {
        createStackedBox();
        showDockingView();
        SearchCommand command = new SearchCommand(new DefaultCommandReference());
        CommandProcessor.getInstance().addOp(command);
    }

    public void showResults() {
        Iterator<IContextProvider> it = providerList.listIterator();
        while (it.hasNext()) {
            IContextProvider p = it.next();
            p.showResult();
        }
    }

    public JComponent getView() {
        return this;
    }

    public void register(IContextProvider provider) {
        providerList.add(provider);
    }

    public void unregister(IContextProvider provider) {
        providerList.remove(provider);
    }

    private List<IContextProvider> createProviderList() {
        return providerList;
    }

    class SearchCommand extends Command {

        public SearchCommand(ICommandReference reference) {
            super(reference);
        }

        @Override
        public void execute(IWorkerStatusController worker) throws Exception {
            Iterator<IContextProvider> it = providerList.listIterator();
            while (it.hasNext()) {
                IContextProvider p = it.next();
                p.search(frameMediator.getSemanticContext(), 0, 5);
            }
        }

        @Override
        public void updateGUI() throws Exception {
            showResults();
        }
    }

    class StackedBox extends JPanel implements Scrollable {

        StackedBox() {
            setLayout(new VerticalLayout());
            setOpaque(true);
        }

        /**
		 * Adds a new component to this <code>StackedBox</code>
		 * 
		 * @param box
		 */
        public void addBox(JComponent box) {
            add(box);
        }

        /**
		 * @see Scrollable#getPreferredScrollableViewportSize()
		 */
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        /**
		 * @see Scrollable#getScrollableBlockIncrement(java.awt.Rectangle, int,
		 *      int)
		 */
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 10;
        }

        /**
		 * @see Scrollable#getScrollableTracksViewportHeight()
		 */
        public boolean getScrollableTracksViewportHeight() {
            if (getParent() instanceof JViewport) {
                return (((JViewport) getParent()).getHeight() > getPreferredSize().height);
            } else {
                return false;
            }
        }

        /**
		 * @see Scrollable#getScrollableTracksViewportWidth()
		 */
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        /**
		 * @see Scrollable#getScrollableUnitIncrement(java.awt.Rectangle, int,
		 *      int)
		 */
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 10;
        }
    }
}
