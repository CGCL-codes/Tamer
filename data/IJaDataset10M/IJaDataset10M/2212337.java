package es.eucm.eadventure.editor.gui.displaydialogs;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JPanel;
import es.eucm.eadventure.common.data.chapter.book.Book;
import es.eucm.eadventure.editor.control.controllers.book.BookDataControl;
import es.eucm.eadventure.editor.gui.otherpanels.bookpanels.BookPagePreviewPanel;

public class StyledBookDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    public StyledBookDialog(BookDataControl book) {
        super();
        if (book.getType() == Book.TYPE_PAGES) {
            BookPagePreviewPanel previewPanel = new BookPagePreviewPanel(book, true, 0);
            JPanel contentPane = new JPanel(null);
            contentPane.setPreferredSize(new Dimension(800, 600));
            previewPanel.setPreferredSize(new Dimension(800, 600));
            previewPanel.setBounds(0, 0, 800, 600);
            contentPane.add(previewPanel);
            this.setContentPane(previewPanel);
            this.pack();
            setResizable(false);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((screenSize.width - 800) / 2, (screenSize.height - 600) / 2);
            setEnabled(true);
            setVisible(true);
            setFocusable(true);
            requestFocus();
        }
    }
}
