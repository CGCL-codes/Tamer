package tico.editor.actions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import javax.swing.JOptionPane;
import tico.board.TBoardConstants;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.editor.TBoardContainer;
import tico.editor.TEditor;

/**
 * Action wich prints the current editor project. Each board of the editor
 * is printed in a different page.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TProjectPrintAction extends TEditorAbstractAction {

    /**
	 * Constructor for TProjectPrintAction.
	 * 
	 * @param editor The boards' editor
	 */
    public TProjectPrintAction(TEditor editor) {
        super(editor, TLanguage.getString("TProjectPrintAction.NAME"), TResourceManager.getImageIcon("archive-print-22.png"));
    }

    public void actionPerformed(ActionEvent e) {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        Book book = new Book();
        int pages = getEditor().getBoardContainerCount();
        for (int i = 0; i < pages; i++) {
            TBoardContainer boardContainer = getEditor().getBoardContainer(i);
            boardContainer.getBoard().clearSelection();
            Dimension boardSize = TBoardConstants.getSize(boardContainer.getBoard().getAttributes(null));
            PageFormat page = printJob.defaultPage();
            if (boardSize.getWidth() > boardSize.getHeight()) page.setOrientation(PageFormat.LANDSCAPE); else page.setOrientation(PageFormat.PORTRAIT);
            book.append(boardContainer, page);
        }
        printJob.setPageable(book);
        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, TLanguage.getString("TProjectPrintAction.PRINT_ERROR"), TLanguage.getString("TProjectPrintAction.ERROR") + "!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
