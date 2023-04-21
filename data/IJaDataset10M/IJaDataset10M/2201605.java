package ucalgary.ebe.samples.collected;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Snippet110 {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        final Table table = new Table(shell, SWT.BORDER | SWT.V_SCROLL);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        final int rowCount = 64, columnCount = 4;
        for (int i = 0; i < columnCount; i++) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText("Column " + i);
        }
        for (int i = 0; i < rowCount; i++) {
            TableItem item = new TableItem(table, SWT.NONE);
            for (int j = 0; j < columnCount; j++) {
                item.setText(j, "Item " + i + "-" + j);
            }
        }
        for (int i = 0; i < columnCount; i++) {
            table.getColumn(i).pack();
        }
        Point size = table.computeSize(SWT.DEFAULT, 200);
        table.setSize(size);
        shell.pack();
        table.addListener(SWT.MouseDown, new Listener() {

            public void handleEvent(Event event) {
                Rectangle clientArea = table.getClientArea();
                Point pt = new Point(event.x, event.y);
                int index = table.getTopIndex();
                while (index < table.getItemCount()) {
                    boolean visible = false;
                    TableItem item = table.getItem(index);
                    for (int i = 0; i < columnCount; i++) {
                        Rectangle rect = item.getBounds(i);
                        if (rect.contains(pt)) {
                            System.out.println("Item " + index + "-" + i);
                        }
                        if (!visible && rect.intersects(clientArea)) {
                            visible = true;
                        }
                    }
                    if (!visible) return;
                    index++;
                }
            }
        });
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
