package foa.preview;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Emiliano Grigis
 * @version 0.1.2
*/
public class NextPageAction extends AbstractAction {

    private PreviewFrame frame;

    private PageNumberField pnf;

    public NextPageAction(Container c, PageNumberField pageNumberField) {
        super("Next Page", new ImageIcon(c.getClass().getResource("/foa/images/preview/next.gif")));
        frame = (PreviewFrame) c;
        pnf = pageNumberField;
    }

    public void actionPerformed(ActionEvent e) {
        frame.getPreviewToolBar().setEnabledFirstPage(true);
        frame.getPreviewToolBar().setEnabledPreviousPage(true);
        PreviewRenderer pr = frame.getPreviewRenderer();
        boolean last = pr.displayNextPage();
        TreeRenderer tr = frame.getTreeRenderer();
        tr.displayNextTree();
        pnf.setValue(pr.pageNumber());
        if (last) {
            frame.getPreviewToolBar().setEnabledNextPage(false);
            frame.getPreviewToolBar().setEnabledLastPage(false);
        } else {
            frame.getPreviewToolBar().setEnabledNextPage(true);
            frame.getPreviewToolBar().setEnabledLastPage(true);
        }
    }
}
