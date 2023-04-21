package org.ln.millesimus.gui.wizard.importing;

import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.JXList;
import org.ln.millesimus.application.FileManager;
import org.ln.millesimus.vo.PriceListItem;
import org.ln.swingy.wizard.BaseWizardPanel;
import org.ln.swingy.wizard.Finishable;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author luke
 *
 */
public class ExportSavePriceListPanel extends BaseWizardPanel implements Finishable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JXList successList;

    private JXList abortList;

    private JLabel successLabel;

    private JLabel abortLabel;

    private File input;

    private JProgressBar progressBar;

    private Task task;

    private DefaultListModel abortListModel;

    private DefaultListModel successListModel;

    private List<PriceListItem> priceListItems;

    private ResourceMap rm;

    protected void initComponents() {
        rm = Application.getInstance().getContext().getResourceMap(ExportWizard.class);
        successLabel = new JLabel("Saved");
        abortLabel = new JLabel("Aborted");
        abortListModel = new DefaultListModel();
        successListModel = new DefaultListModel();
        successList = new JXList(successListModel);
        successList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        successList.setVisibleRowCount(-1);
        abortList = new JXList(abortListModel);
        abortList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        abortList.setVisibleRowCount(-1);
        progressBar = new JProgressBar(0, 100);
        setHeaderTitle(rm.getString("text.header.title"));
        setHeaderDescription(rm.getString("text.header.message"));
        setHeaderIcon(rm.getIcon("header.icon"));
    }

    protected JPanel buildCenterPanel() {
        FormLayout layout = new FormLayout("default, $lcgap, default:grow, $lcgap, default", "default, $lgap, 2*(default, $lgap, f:p:g, $lgap) ");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        JScrollPane scroll = new JScrollPane(successList);
        scroll.setPreferredSize(new Dimension(300, 250));
        JScrollPane scroll1 = new JScrollPane(abortList);
        scroll1.setPreferredSize(new Dimension(300, 250));
        builder.add(progressBar, cc.xyw(1, 1, 5));
        builder.add(successLabel, cc.xyw(1, 3, 5));
        builder.add(scroll, cc.xyw(1, 5, 5));
        builder.add(abortLabel, cc.xyw(1, 7, 5));
        builder.add(scroll1, cc.xyw(1, 9, 5));
        return builder.getPanel();
    }

    @Override
    protected void afterBuildPanel() {
        rm.injectComponents(this);
    }

    /**
	 * @return the priceListItems
	 */
    public List<PriceListItem> getPriceListItems() {
        return priceListItems;
    }

    /**
	 * @param priceListItems the priceListItems to set
	 */
    public void setPriceListItems(List<PriceListItem> priceListItems) {
        this.priceListItems = priceListItems;
    }

    /**
	 * @param input
	 */
    public void setInput(File input) {
        this.input = input;
    }

    /**
     * @return the input
     */
    public File getInput() {
        return input;
    }

    public void onFinish() {
    }

    /**
	 * 
	 */
    public void doImport() {
        task = new Task();
        task.execute();
        setInputOk(true);
    }

    /**
     * @author luke
     *
     */
    private class Task extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            setProgress(0);
            progressBar.setIndeterminate(true);
            try {
                getInput().createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileManager.writeLine(getInput(), FileManager.priceListItemToCsv(getPriceListItems()));
            return null;
        }

        @Override
        public void done() {
            for (PriceListItem item : getPriceListItems()) {
                successListModel.addElement(item);
            }
            progressBar.setIndeterminate(false);
            progressBar.setValue(100);
            progressBar.setStringPainted(true);
            setCursor(null);
        }
    }
}
