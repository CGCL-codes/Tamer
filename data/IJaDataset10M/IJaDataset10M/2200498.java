package newsatort.ui.view.itemslist;

import java.util.List;
import newsatort.Application;
import newsatort.event.IEvent;
import newsatort.exception.ViewException;
import newsatort.feed.IFeed;
import newsatort.feed.IFeedManager;
import newsatort.feed.Item;
import newsatort.ui.view.AbstractView;
import newsatort.ui.view.ViewUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class ItemsListView extends AbstractView {

    public static final String ID = ItemsListView.class.getSimpleName();

    public static final String PARAMETER_LIST_ITEMS = "PARAMETER_LIST_ITEMS";

    public static final String PARAMETER_FEED = "PARAMETER_FEED";

    private CompositeTable compositeTable = null;

    private List<Item> listItems = null;

    private IFeed feed = null;

    static class UnifiedMouseListener implements MouseListener {

        private final Composite composite;

        public UnifiedMouseListener(Composite composite) {
            this.composite = composite;
        }

        @Override
        public void mouseDoubleClick(MouseEvent e) {
        }

        @Override
        public void mouseDown(MouseEvent e) {
            composite.forceFocus();
        }

        @Override
        public void mouseUp(MouseEvent e) {
        }
    }

    ;

    @SuppressWarnings("unchecked")
    @Override
    protected void fillParameters() {
        listItems = getParameter(PARAMETER_LIST_ITEMS, List.class, null);
        feed = getParameter(PARAMETER_FEED, IFeed.class, null);
    }

    @Override
    public void createLayout(Composite parent) throws ViewException {
        String title = feed != null ? feed.getName() : "Liste des nouveaux items";
        setPartName(title);
        parent.setLayout(ViewUtils.newGridLayout("numColumns", 1));
        ViewUtils.modifyColor(parent, null, SWT.COLOR_WHITE);
        final Composite titleComposite = new Composite(parent, SWT.NULL);
        titleComposite.setLayout(ViewUtils.newGridLayout("numColumns", 1));
        ViewUtils.modifyColor(titleComposite, null, SWT.COLOR_WIDGET_BACKGROUND);
        titleComposite.setLayoutData(ViewUtils.newGridData("horizontalAlignment", SWT.FILL, "grabExcessHorizontalSpace", true));
        Label label = new Label(titleComposite, SWT.CENTER);
        label.setText(title);
        ViewUtils.modifyFont(label, "arial", 12, SWT.BOLD);
        ViewUtils.modifyColor(label, SWT.COLOR_DARK_BLUE, SWT.COLOR_WIDGET_BACKGROUND);
        label.setLayoutData(ViewUtils.newGridData("horizontalAlignment", SWT.CENTER, "grabExcessHorizontalSpace", true));
        compositeTable = new CompositeTable(parent, SWT.NULL);
        compositeTable.setLayoutData(ViewUtils.newGridData("horizontalAlignment", SWT.FILL, "verticalAlignment", SWT.FILL, "grabExcessHorizontalSpace", true, "grabExcessVerticalSpace", true));
        new ItemsRow(compositeTable, SWT.NULL);
        compositeTable.setRunTime(true);
        compositeTable.setNumRowsInCollection(listItems.size());
        compositeTable.addRowContentProvider(new IRowContentProvider() {

            @Override
            public void refresh(CompositeTable sender, int currentObjectOffset, Control control) {
                ((ItemsRow) control).setText(listItems.get(currentObjectOffset));
            }
        });
    }

    @Override
    public void setFocus() {
        compositeTable.setFocus();
    }

    @Override
    public void dispose() {
        Application.getInstance().getManager(IFeedManager.class).removeObserver(this);
        Application.getInstance().getManager(IFeedManager.class).removeFeedObserver(feed, this);
        super.dispose();
    }

    @Override
    public void update(Object object, IEvent event) {
    }
}
