package de.dirkdittmar.flickr.group.comment.ui.worker;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JOptionPane;
import de.dirkdittmar.flickr.group.comment.ui.MainFrame;

/**
 * @author Dirk Dittmar
 * 
 */
public abstract class AbstractWorker {

    protected void showMessageDialog(final String message, final String title, final int messageType) {
        try {
            EventQueue.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    JOptionPane.showMessageDialog(MainFrame.getInstance(), message, title, messageType);
                }
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    protected int showConfirmDialog(final String message, final String title, final int optionType) {
        try {
            ConfirmDialogResultContainer confirmDialogResultContainer = new ConfirmDialogResultContainer(message, title, optionType);
            EventQueue.invokeAndWait(confirmDialogResultContainer);
            return confirmDialogResultContainer.getResult();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ConfirmDialogResultContainer implements Runnable {

        private String message;

        private String title;

        private int optionType;

        private int result = 0;

        public ConfirmDialogResultContainer(String message, String title, int optionType) {
            this.message = message;
            this.title = title;
            this.optionType = optionType;
        }

        @Override
        public void run() {
            result = JOptionPane.showConfirmDialog(MainFrame.getInstance(), message, title, optionType);
        }

        public int getResult() {
            return result;
        }
    }
}
