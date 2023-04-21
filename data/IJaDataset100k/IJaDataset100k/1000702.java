package view.window;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import presentation.*;

public class ApplicationWindow implements IView {

    protected Shell shell;

    private Text m_unsortedArray;

    private Text m_sortedArray;

    private Text m_arrayElements;

    private IActionHandler m_arrayGeneratorHandler;

    private IActionHandler m_sortArrayHandler;

    /**
	 * Launch the application.
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            ApplicationWindow window = new ApplicationWindow();
            new Presenter(window);
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Open the window.
	 */
    public void open() {
        Display display = Display.getDefault();
        createContents();
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
	 * Create contents of the window.
	 */
    protected void createContents() {
        shell = new Shell();
        shell.setSize(450, 300);
        shell.setText("SWT Application");
        Button m_rBtnMergeSort = new Button(shell, SWT.RADIO);
        m_rBtnMergeSort.setBounds(149, 93, 83, 16);
        m_rBtnMergeSort.setText("Merge Sort");
        Button m_rBtnQuickSort = new Button(shell, SWT.RADIO);
        m_rBtnQuickSort.setText("Quick Sort");
        m_rBtnQuickSort.setBounds(149, 71, 83, 16);
        m_unsortedArray = new Text(shell, SWT.BORDER);
        m_unsortedArray.setBounds(187, 131, 245, 19);
        m_sortedArray = new Text(shell, SWT.BORDER);
        m_sortedArray.setBounds(187, 187, 245, 19);
        m_arrayElements = new Text(shell, SWT.BORDER);
        m_arrayElements.setBounds(131, 17, 76, 19);
        Button btnNewButton = new Button(shell, SWT.NONE);
        btnNewButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                m_arrayGeneratorHandler.processAction();
            }
        });
        btnNewButton.setBounds(20, 129, 145, 23);
        btnNewButton.setText("Generate");
        Button btnStartSorting = new Button(shell, SWT.NONE);
        btnStartSorting.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                m_sortArrayHandler.processAction();
            }
        });
        btnStartSorting.setBounds(20, 185, 145, 23);
        btnStartSorting.setText("Start Sorting");
        Label lblEnterSizeOf = new Label(shell, SWT.NONE);
        lblEnterSizeOf.setBounds(20, 20, 109, 13);
        lblEnterSizeOf.setText("Enter Size of Array :");
        Label lblChooseSortingMethod = new Label(shell, SWT.NONE);
        lblChooseSortingMethod.setBounds(20, 73, 123, 13);
        lblChooseSortingMethod.setText("Choose Sorting Method:");
    }

    @Override
    public String getUnsortedArray() {
        return m_unsortedArray.getText();
    }

    @Override
    public void setUnsortedArray(String unsortedArray) {
        m_unsortedArray.setText(unsortedArray);
    }

    @Override
    public boolean mergeSortIsSelected() {
        return true;
    }

    @Override
    public boolean quickSortIsSelected() {
        return false;
    }

    @Override
    public void setSortedArray(String sortedArray) {
        m_sortedArray.setText(sortedArray);
    }

    @Override
    public String getArraySize() {
        return m_arrayElements.getText();
    }

    @Override
    public void generateRandomArray(IActionHandler handler) {
        m_arrayGeneratorHandler = handler;
    }

    @Override
    public void selectedArraySize(IActionHandler handler) {
    }

    @Override
    public void sortArray(IActionHandler handler) {
        m_sortArrayHandler = handler;
    }

    @Override
    public void sortingArray(IActionHandler handler) {
        m_arrayGeneratorHandler = handler;
    }
}
