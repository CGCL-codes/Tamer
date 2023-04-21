package org.delafer.benchmark.ui.draft;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

public class Snippet220 {

    private static transient boolean ok = false;

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setBounds(10, 10, 350, 200);
        shell.setLayout(new FillLayout());
        final Image xImage = new Image(display, 16, 16);
        GC gc = new GC(xImage);
        gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
        gc.drawLine(1, 1, 14, 14);
        gc.drawLine(1, 14, 14, 1);
        gc.drawOval(2, 2, 11, 11);
        gc.dispose();
        final int IMAGE_MARGIN = 2;
        final Tree tree = new Tree(shell, SWT.VIRTUAL | SWT.CHECK | SWT.MULTI | SWT.BORDER);
        File[] roots = File.listRoots();
        tree.setData(roots);
        final Set<TreeItem> til = new HashSet<TreeItem>();
        tree.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                String string = event.detail == SWT.CHECK ? "Checked" : "Selected";
                System.out.println(event.item + " " + string);
                ok = !ok;
                for (TreeItem ti : til) {
                    ti.setGrayed(ok);
                }
            }
        });
        tree.addListener(SWT.SetData, new Listener() {

            public void handleEvent(Event event) {
                TreeItem item = (TreeItem) event.item;
                Widget parentItem = item.getParentItem();
                if (parentItem == null) parentItem = tree;
                int itemAt = event.index;
                til.add(item);
                File[] files = (File[]) parentItem.getData();
                File file = files[itemAt];
                item.setText(file.toString());
                if (file.isDirectory()) {
                    files = file.listFiles();
                    if (files != null) {
                        item.setData(files);
                        item.setItemCount(files.length);
                    }
                }
            }
        });
        tree.setItemCount(roots.length);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        xImage.dispose();
        display.dispose();
    }
}
