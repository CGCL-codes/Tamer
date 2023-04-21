package cn.imgdpu.dialog;

import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import cn.imgdpu.net.GetBookInfo;
import cn.imgdpu.util.GeneralMethod;

public class BookInfoDialog extends Dialog {

    private static Table table;

    protected Object result;

    public static Shell shell;

    String url;

    Label bookName;

    /**
	 * Create the dialog
	 * 
	 * @param parent
	 * @param style
	 */
    public BookInfoDialog(Shell parent, int style) {
        super(parent, style);
    }

    /**
	 * Create the dialog
	 * 
	 * @param parent
	 */
    public BookInfoDialog(Shell parent) {
        this(parent, SWT.NONE);
    }

    /**
	 * Open the dialog
	 * 
	 * @return the result
	 */
    public Object open(String _url, String _bookName) {
        createContents();
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        url = _url;
        bookName.setText(_bookName);
        shell.setText("详情 - 读取中...");
        GetBookInfo getBookInfo = new GetBookInfo(url);
        getBookInfo.start();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        return result;
    }

    /**
	 * Create contents of the dialog
	 */
    protected void createContents() {
        shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setAlpha(250);
        GeneralMethod.getGeneralMethod().setDisLoc(shell, 345, 304);
        shell.setSize(345, 304);
        shell.setText("详情");
        bookName = new Label(shell, SWT.NONE);
        bookName.setBounds(10, 10, 321, 17);
        table = new Table(shell, SWT.FULL_SELECTION | SWT.BORDER);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setBounds(10, 33, 321, 204);
        table.setEnabled(false);
        final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
        newColumnTableColumn.setWidth(150);
        newColumnTableColumn.setText("收藏单位");
        final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
        newColumnTableColumn_1.setWidth(150);
        newColumnTableColumn_1.setText("状态");
        final Button closeBtn = new Button(shell, SWT.NONE);
        closeBtn.setText("关闭");
        closeBtn.setBounds(253, 243, 76, 22);
        closeBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent arg0) {
                shell.close();
            }
        });
    }

    public static void setBookInfo(ArrayList<String> book) {
        shell.setText("详情");
        table.setEnabled(true);
        if (book.size() < 2) shell.setText("读取失败"); else for (int i = 0; i < book.size(); i += 2) {
            final TableItem item = new TableItem(table, SWT.BORDER);
            item.setText(new String[] { book.get(i), book.get(i + 1) });
        }
    }
}
