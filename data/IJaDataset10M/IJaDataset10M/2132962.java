package jamsa.rcp.downloader.views;

import jamsa.rcp.downloader.Messages;
import jamsa.rcp.downloader.models.Task;
import jamsa.rcp.downloader.utils.Logger;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

/**
 * 任务详细信息视图
 * 
 * @author 朱杰
 * 
 */
public class TaskInfoView extends ViewPart {

    private static final Logger logger = new Logger(TaskInfoView.class);

    public static final String ID = "jamsa.rcp.downloader.views.TaskInfoView";

    private TableViewer tableViewer;

    private Task task;

    private ISelectionListener selectionListener = new ISelectionListener() {

        public void selectionChanged(IWorkbenchPart part, ISelection selection) {
            if (selection instanceof IStructuredSelection) {
                IStructuredSelection incoming = (IStructuredSelection) selection;
                if (incoming.size() == 1 && incoming.getFirstElement() instanceof Task) {
                    task = (Task) incoming.getFirstElement();
                    tableViewer.setInput(task);
                    logger.info("当前选中任务：" + task.getFileName());
                }
            }
        }
    };

    @Override
    public void createPartControl(Composite parent) {
        tableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
        Table table = tableViewer.getTable();
        TableColumn column = new TableColumn(table, SWT.NONE);
        column.setText(Messages.TaskInfoView_Key);
        column.setWidth(100);
        column = new TableColumn(table, SWT.NONE);
        column.setText(Messages.TaskInfoView_Value);
        column.setWidth(300);
        table.setLinesVisible(true);
        tableViewer.setLabelProvider(new ViewLabelProvider());
        tableViewer.setContentProvider(new ViewContentProvider());
        getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(selectionListener);
    }

    public void dispose() {
        getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(selectionListener);
        super.dispose();
    }

    @Override
    public void setFocus() {
        tableViewer.getTable().setFocus();
    }

    class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            if (element instanceof Object[]) {
                Object[] props = (Object[]) element;
                if (columnIndex < (props.length)) {
                    return String.valueOf(props[columnIndex]);
                }
            }
            return null;
        }
    }

    class ViewContentProvider implements IStructuredContentProvider {

        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof Task) {
                Task task = (Task) inputElement;
                Object[] result = new Object[9];
                result[0] = new String[] { Messages.TaskInfoView_FileName, task.getFileName() };
                result[1] = new String[] { Messages.TaskInfoView_SavePath, task.getFilePath() };
                result[2] = new String[] { Messages.TaskInfoView_FileSize, task.getFileSize() + "" };
                result[3] = new String[] { Messages.TaskInfoView_AverageSpeed, task.getAverageSpeed() + "k/s" };
                result[4] = new String[] { Messages.TaskInfoView_TotalTime, task.getTotalTime() / 1000 + "s" };
                switch(task.getStatus()) {
                    case Task.STATUS_ERROR:
                        result[5] = new String[] { Messages.TaskInfoView_Status, Messages.TaskInfoView_Error };
                        break;
                    case Task.STATUS_FINISHED:
                        result[5] = new String[] { Messages.TaskInfoView_Status, Messages.TaskInfoView_Finish };
                        break;
                    case Task.STATUS_RUNNING:
                        result[5] = new String[] { Messages.TaskInfoView_Status, Messages.TaskInfoView_Running };
                        break;
                    case Task.STATUS_STOP:
                        result[5] = new String[] { Messages.TaskInfoView_Status, Messages.TaskInfoView_Stop };
                        break;
                    default:
                        result[5] = new String[] { Messages.TaskInfoView_Status, "" };
                        break;
                }
                result[6] = new String[] { Messages.TaskInfoView_URL, task.getFileUrl() };
                result[7] = new String[] { Messages.TaskInfoView_FileType, task.getFileType() };
                result[8] = new String[] { Messages.TaskInfoView_Memo, task.getMemo() };
                return result;
            }
            return null;
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            tableViewer.refresh();
        }
    }
}
