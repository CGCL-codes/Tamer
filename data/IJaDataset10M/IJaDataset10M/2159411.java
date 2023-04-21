package net.sourceforge.javautil.common;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

/**
 * Some basic helper classes with GUI that need to be here in the common library.
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: GUIUtil.java 2712 2011-01-03 00:32:01Z ponderator $
 */
public class GUIUtil {

    /**
	 * This will get the current screen size and center the window. This will silently
	 * fail if the {@link HeadlessException} is thrown when screen size is retreived.
	 * 
	 * @param window The window to center on the screen
	 */
    public static void center(Window window) {
        try {
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            window.setLocation((int) (screen.getWidth() - window.getWidth()) / 2, (int) (screen.getHeight() - window.getHeight()) / 2);
        } catch (HeadlessException e) {
        }
    }

    /**
	 * This is the 3rd version of SwingWorker (also known as
	 * SwingWorker 3), an abstract class that you subclass to
	 * perform GUI-related work in a dedicated thread.  For
	 * instructions on using this class, see:
	 * 
	 * http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
	 *
	 * Note that the API changed slightly in the 3rd version:
	 * You must now invoke start() on the SwingWorker after
	 * creating it.
	 */
    public abstract static class SwingWorker {

        private Object value;

        private Thread thread;

        /** 
	     * Class to maintain reference to current worker thread
	     * under separate synchronization control.
	     */
        private static class ThreadVar {

            private Thread thread;

            ThreadVar(Thread t) {
                thread = t;
            }

            synchronized Thread get() {
                return thread;
            }

            synchronized void clear() {
                thread = null;
            }
        }

        private ThreadVar threadVar;

        /** 
	     * Get the value produced by the worker thread, or null if it 
	     * hasn't been constructed yet.
	     */
        protected synchronized Object getValue() {
            return value;
        }

        /** 
	     * Set the value produced by worker thread 
	     */
        private synchronized void setValue(Object x) {
            value = x;
        }

        /** 
	     * Compute the value to be returned by the <code>get</code> method. 
	     */
        public abstract Object construct();

        /**
	     * Called on the event dispatching thread (not on the worker thread)
	     * after the <code>construct</code> method has returned.
	     */
        public void finished() {
        }

        /**
	     * A new method that interrupts the worker thread.  Call this method
	     * to force the worker to stop what it's doing.
	     */
        public void interrupt() {
            Thread t = threadVar.get();
            if (t != null) {
                t.interrupt();
            }
            threadVar.clear();
        }

        /**
	     * Return the value created by the <code>construct</code> method.  
	     * Returns null if either the constructing thread or the current
	     * thread was interrupted before a value was produced.
	     * 
	     * @return the value created by the <code>construct</code> method
	     */
        public Object get() {
            while (true) {
                Thread t = threadVar.get();
                if (t == null) {
                    return getValue();
                }
                try {
                    t.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
        }

        /**
	     * Start a thread that will call the <code>construct</code> method
	     * and then exit.
	     */
        public SwingWorker() {
            final Runnable doFinished = new Runnable() {

                public void run() {
                    finished();
                }
            };
            Runnable doConstruct = new Runnable() {

                public void run() {
                    try {
                        setValue(construct());
                    } finally {
                        threadVar.clear();
                    }
                    SwingUtilities.invokeLater(doFinished);
                }
            };
            Thread t = new Thread(doConstruct);
            threadVar = new ThreadVar(t);
        }

        /**
	     * Start the worker thread.
	     */
        public void start() {
            Thread t = threadVar.get();
            if (t != null) {
                t.start();
            }
        }
    }

    /**
	 * A facility for making {@link GridBagLayout} component
	 * additions much more straight forward and programatically
	 * simpler. 
	 *
	 * @author elponderador
	 * @author $Author: ponderator $
	 * @version $Id: GUIUtil.java 2712 2011-01-03 00:32:01Z ponderator $
	 */
    public static class GridBagManager {

        private Container target;

        private GridBagConstraints gbc = new GridBagConstraints();

        public GridBagManager(Container target) {
            this.target = target;
            this.target.setLayout(new GridBagLayout());
        }

        public void setSpacing(int width, int height) {
            gbc.ipadx = width;
            gbc.ipady = height;
        }

        public void fill(int x, int y, double fillWidth, double fillHeight) {
            this.fill(x, y, 1, 1, fillWidth, fillHeight);
        }

        public void fill(int x, int y, int colspan, int rowspan, double fillWidth, double fillHeight) {
            this.add(new JLabel(), x, y, colspan, rowspan, fillWidth, fillHeight, null, null);
        }

        /**
		 * 1 rowspan, 1 colspan, default alignment and no fill.
		 * 
		 * @see #add(Component, int, int, int, int, boolean, boolean, String, String)
		 */
        public void add(Component cmp, int x, int y) {
            this.add(cmp, x, y, 1, 1, 0, 0, null, null);
        }

        /**
		 * Default alignment, no fill width or height.
		 *
		 * @see #add(Component, int, int, int, int, boolean, boolean, String, String)
		 */
        public void add(Component cmp, int x, int y, int cellspan, int rowspan) {
            this.add(cmp, x, y, cellspan, rowspan, 0, 0, null, null);
        }

        /**
		 * 1 rowspan, 1 colspan, default alignment.
		 *
		 * @see #add(Component, int, int, int, int, boolean, boolean, String, String)
		 */
        public void add(Component cmp, int x, int y, double fillWidth, double fillHeight) {
            this.add(cmp, x, y, 1, 1, fillWidth, fillHeight, null, null);
        }

        /**
		 * 1 rowspan, 1 colspan, no fill width or height.
		 *
		 * @see #add(Component, int, int, int, int, boolean, boolean, String, String)
		 */
        public void add(Component cmp, int x, int y, String horizontalAlignment, String verticalAlignment) {
            this.add(cmp, x, y, 1, 1, 0, 0, horizontalAlignment, verticalAlignment);
        }

        /**
		 * Default alignment.
		 *
		 * @see #add(Component, int, int, int, int, boolean, boolean, String, String)
		 */
        public void add(Component cmp, int x, int y, int cellspan, int rowspan, double fillWidth, double fillHeight) {
            this.add(cmp, x, y, cellspan, rowspan, fillWidth, fillHeight, null, null);
        }

        /**
		 * Add the grid bag constrained component to the target passed to the constructor
		 * {@link #GridBagManager(Container)}.
		 * 
		 * @param cmp The component to add
		 * @param x The grid column to place the component
		 * @param y The grid row to place the component
		 * @param cellspan The amount of columns the component should occupy
		 * @param rowspan The amount of rows the component should occupy
		 * @param fillWidth 0 = no width fill, positive number is the weight for filling extra width space
		 * @param fillHeight 0 = no height fill, positive number is the weight for filling extra height space
		 * @param horizontalAlignment The width/horizontal alignment ("center", "right", "left"), null = "center"
		 * @param verticalAlignment The height/vertical alignment ("center", "top", "bottom"), null = "center"
		 */
        public void add(Component cmp, int x, int y, int cellspan, int rowspan, double fillWidth, double fillHeight, String horizontalAlignment, String verticalAlignment) {
            gbc.gridx = x;
            gbc.gridy = y;
            gbc.gridwidth = cellspan;
            gbc.gridheight = rowspan;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;
            gbc.weighty = 0;
            if (fillWidth > 0 && fillHeight > 0) {
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = fillWidth;
                gbc.weighty = fillHeight;
            } else if (fillWidth > 0) {
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = fillWidth;
            } else if (fillHeight > 0) {
                gbc.fill = GridBagConstraints.VERTICAL;
                gbc.weighty = fillHeight;
            }
            gbc.anchor = this.getPosition(verticalAlignment, horizontalAlignment);
            target.add(cmp, gbc);
        }

        /**
		 * @param vertical The string vertical alignment, null if default
		 * @param horizontal The string horizontal alignment, null if default
		 * @return The {@link GridBagConstraints} alignment corresponding to the combination
		 * of the two alignments. 
		 */
        protected int getPosition(String vertical, String horizontal) {
            int h = this.getHorizontalAlignment(horizontal);
            int v = this.getVerticalAlignment(vertical);
            if (h == -1 && v == -1) return GridBagConstraints.CENTER;
            if (h == -1) h = 2;
            if (v == -1) v = 2;
            if (v == 0 && h == 0) return GridBagConstraints.FIRST_LINE_START;
            if (v == 0 && h == 1) return GridBagConstraints.FIRST_LINE_END;
            if (v == 0 && h == 2) return GridBagConstraints.PAGE_START;
            if (v == 1 && h == 0) return GridBagConstraints.LAST_LINE_START;
            if (v == 1 && h == 1) return GridBagConstraints.LAST_LINE_END;
            if (v == 1 && h == 2) return GridBagConstraints.PAGE_END;
            if (v == 2 && h == 0) return GridBagConstraints.LINE_START;
            if (v == 2 && h == 1) return GridBagConstraints.LINE_END;
            return GridBagConstraints.CENTER;
        }

        /**
		 * @param alignment The string vertical alignment
		 * @return -1 if null, 0 if "top", 1 if "bottom" or 2 for all other values
		 */
        protected int getVerticalAlignment(String alignment) {
            if (alignment == null) return -1;
            if ("top".equalsIgnoreCase(alignment)) return 0; else if ("bottom".equalsIgnoreCase(alignment)) return 1;
            return 2;
        }

        /**
		 * @param alignment The string horizontal alignment
		 * @return -1 if null, 0 if "left", 1 if "right or 2 for all other values
		 */
        protected int getHorizontalAlignment(String alignment) {
            if (alignment == null) return -1;
            if ("left".equalsIgnoreCase(alignment)) return 0; else if ("right".equalsIgnoreCase(alignment)) return 1;
            return 2;
        }
    }
}
