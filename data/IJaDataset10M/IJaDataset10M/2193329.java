package edu.gsbme.wasabi.UI.Views.Gyoza2D;

import java.util.Observable;
import java.util.Observer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import edu.gsbme.gyoza2d.GraphGenerator.GraphGenerator;
import edu.gsbme.gyoza2d.GraphGenerator.ViewStateConfig;
import edu.gsbme.wasabi.UI.editor.MultiPageEditor;

/**
 * 2D Graph option support view
 * @author David
 *
 */
public class GraphOptionView extends ViewPart implements Observer {

    Table table;

    ViewStateConfig current_config;

    public void createPartControl(Composite parent) {
        getSite().getWorkbenchWindow().getPartService().addPartListener(partListener);
        parent.setLayout(new FillLayout());
        table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        initTable();
    }

    private void initTable() {
        TableColumn col = new TableColumn(table, SWT.LEFT);
        col.setResizable(true);
        col.setText("Property");
        col.setWidth((int) (table.getSize().x * 0.4));
        table.showColumn(col);
        col = new TableColumn(table, SWT.LEFT);
        col.setResizable(true);
        col.setText("Value");
        col.setWidth((int) (table.getSize().x * 0.55));
        table.showColumn(col);
    }

    private void resizeCol() {
        TableColumn col = table.getColumn(0);
        col.setWidth((int) (table.getSize().x * 0.4));
        col = table.getColumn(1);
        col.setWidth((int) (table.getSize().x * 0.55));
    }

    private void setupObserver(ViewStateConfig config) {
        if (this.current_config != null) current_config.deleteObserver(this);
        this.current_config = config;
        config.addObserver(this);
    }

    private void removeObserver() {
        current_config.deleteObserver(this);
    }

    public void initialUIContent(ViewStateConfig config) {
        String[] property = new String[] { "Element :", "Layout :", "Layout Routing :", "Zoom :" };
        table.removeAll();
        if (config == null) {
            for (int i = 0; i < property.length; i++) {
                TableItem item = new TableItem(table, SWT.NULL);
                item.setText(0, property[i]);
            }
        } else {
            TableItem item = new TableItem(table, SWT.NULL);
            item.setText(0, property[0]);
            item.setText(1, config.getCurrentElement().getTagName());
            item = new TableItem(table, SWT.NULL);
            item.setText(0, property[1]);
            item.setText(1, config.getCurrentLayoutAlgorithm().getLayoutName());
            item = new TableItem(table, SWT.NULL);
            item.setText(0, property[2]);
            item.setText(1, config.getCurrentLayoutRounting().toString());
            item = new TableItem(table, SWT.NULL);
            item.setText(0, property[3]);
            item.setText(1, "" + config.getZoomLevel());
        }
    }

    @Override
    public void setFocus() {
    }

    public void dispose() {
        if (current_config != null) current_config.deleteObserver(this);
        getSite().getWorkbenchWindow().getPartService().removePartListener(partListener);
        super.dispose();
    }

    private IPartListener partListener = new IPartListener() {

        @Override
        public void partActivated(IWorkbenchPart part) {
            if (!(part instanceof MultiPageEditor)) return;
            GraphGenerator g = ((MultiPageEditor) part).returnGraphEditor().returnGraphGenerator();
            resizeCol();
            if (g != null) {
                setupObserver(g.returnCurrentViewState());
                initialUIContent(g.returnCurrentViewState());
            } else initialUIContent(null);
        }

        @Override
        public void partBroughtToTop(IWorkbenchPart part) {
            if (!(part instanceof MultiPageEditor)) return;
        }

        @Override
        public void partClosed(IWorkbenchPart part) {
            if (!(part instanceof MultiPageEditor)) return;
            GraphGenerator g = ((MultiPageEditor) part).returnGraphEditor().returnGraphGenerator();
            removeObserver();
        }

        @Override
        public void partDeactivated(IWorkbenchPart part) {
            if (!(part instanceof MultiPageEditor)) return;
            GraphGenerator g = ((MultiPageEditor) part).returnGraphEditor().returnGraphGenerator();
        }

        @Override
        public void partOpened(IWorkbenchPart part) {
            if (!(part instanceof MultiPageEditor)) return;
            GraphGenerator g = ((MultiPageEditor) part).returnGraphEditor().returnGraphGenerator();
            resizeCol();
            if (g != null) {
                setupObserver(g.returnCurrentViewState());
                initialUIContent(g.returnCurrentViewState());
            } else initialUIContent(null);
        }
    };

    @Override
    public void update(Observable o, Object arg) {
        initialUIContent(current_config);
    }
}
