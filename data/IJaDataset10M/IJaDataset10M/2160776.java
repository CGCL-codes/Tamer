package org.google.translate.desktop.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

/**
 * This class makes it easy to drag and drop files from the operating
 * system to a Java program. Any <tt>java.awt.Component</tt> can be
 * dropped onto, but only <tt>javax.swing.JComponent</tt>s will indicate
 * the drop event with a changed border.
 * <p/>
 * To use this class, construct a new <tt>FileDrop</tt> by passing
 * it the target component and a <tt>Listener</tt> to receive notification
 * when file(s) have been dropped. Here is an example:
 * <p/>
 * <code><pre>
 *      JPanel myPanel = new JPanel();
 *      new FileDrop( myPanel, new FileDrop.Listener()
 *      {   public void filesDropped( java.io.File[] files )
 *          {
 *              // handle file drop
 *              ...
 *          }   // end filesDropped
 *      }); // end FileDrop.Listener
 * </pre></code>
 * <p/>
 * You can specify the border that will appear when files are being dragged by
 * calling the constructor with a <tt>javax.swing.border.Border</tt>. Only
 * <tt>JComponent</tt>s will show any indication with a border.
 * <p/>
 * You can turn on some debugging features by passing a <tt>PrintStream</tt>
 * object (such as <tt>System.out</tt>) into the full constructor. A <tt>null</tt>
 * value will result in no extra debugging information being output.
 * <p/>
 * <p/>
 * <p>I'm releasing this code into the Public Domain. Enjoy.
 * </p>
 * <p><em>Original author: Robert Harder, rharder@usa.net</em></p>
 * <p>2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.</p>
 *
 * @author Robert Harder
 * @author rharder@users.sf.net
 * @version 1.0.1
 */
@SuppressWarnings("ALL")
public class FileDrop {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileDrop.class);

    private static final MatteBorder MATTE_BORDER = BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(0f, 0f, 1f, 0.25f));

    private transient javax.swing.border.Border normalBorder;

    private transient java.awt.dnd.DropTargetListener dropListener;

    /**
     * Discover if the running JVM is modern enough to have drag and drop.
     */
    private static Boolean supportsDnD;

    /**
     * Full constructor with a specified border and debugging optionally turned on.
     * With Debugging turned on, more status messages will be displayed to
     * <tt>out</tt>. A common way to use this constructor is with
     * <tt>System.out</tt> or <tt>System.err</tt>. A <tt>null</tt> value for
     * the parameter <tt>out</tt> will result in no debugging output.
     *
     * @param c          Component on which files will be dropped.
     * @param listener   Listens for <tt>filesDropped</tt>.
     * @since 1.0
     */
    public FileDrop(final java.awt.Component c, final Listener listener) {
        if (supportsDnD()) {
            dropListener = new java.awt.dnd.DropTargetListener() {

                public void dragEnter(java.awt.dnd.DropTargetDragEvent evt) {
                    log("FileDrop: dragEnter event.");
                    if (isDragOk(evt)) {
                        if (c instanceof javax.swing.JComponent) {
                            javax.swing.JComponent jc = (javax.swing.JComponent) c;
                            normalBorder = jc.getBorder();
                            log("FileDrop: normal border saved.");
                            jc.setBorder(MATTE_BORDER);
                            log("FileDrop: drag border set.");
                        }
                        evt.acceptDrag(java.awt.dnd.DnDConstants.ACTION_COPY);
                        log("FileDrop: event accepted.");
                    } else {
                        evt.rejectDrag();
                        log("FileDrop: event rejected.");
                    }
                }

                public void dragOver(java.awt.dnd.DropTargetDragEvent evt) {
                }

                public void drop(java.awt.dnd.DropTargetDropEvent evt) {
                    log("FileDrop: drop event.");
                    try {
                        java.awt.datatransfer.Transferable tr = evt.getTransferable();
                        if (tr.isDataFlavorSupported(java.awt.datatransfer.DataFlavor.javaFileListFlavor)) {
                            evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
                            log("FileDrop: file list accepted.");
                            @SuppressWarnings({ "unchecked" }) java.util.List<File> fileList = (List<File>) tr.getTransferData(java.awt.datatransfer.DataFlavor.javaFileListFlavor);
                            Collections.reverse(fileList);
                            java.io.File[] files = fileList.toArray(new java.io.File[fileList.size()]);
                            if (listener != null) listener.filesDropped(evt, files);
                            evt.getDropTargetContext().dropComplete(true);
                            log("FileDrop: drop complete.");
                        } else {
                            DataFlavor[] flavors = tr.getTransferDataFlavors();
                            boolean handled = false;
                            for (DataFlavor flavor : flavors) {
                                if (flavor.isRepresentationClassReader()) {
                                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                                    log("FileDrop: reader accepted.");
                                    Reader reader = flavor.getReaderForText(tr);
                                    BufferedReader br = new BufferedReader(reader);
                                    if (listener != null) listener.filesDropped(evt, createFileArray(br));
                                    evt.getDropTargetContext().dropComplete(true);
                                    log("FileDrop: drop complete.");
                                    handled = true;
                                    break;
                                }
                            }
                            if (!handled) {
                                log("FileDrop: not a file list or reader - abort.");
                                evt.rejectDrop();
                            }
                        }
                    } catch (java.io.IOException io) {
                        LOGGER.error("Exception handling event " + evt, io);
                        evt.rejectDrop();
                    } catch (java.awt.datatransfer.UnsupportedFlavorException ufe) {
                        LOGGER.error("FileDrop: UnsupportedFlavorException " + evt, ufe);
                        evt.rejectDrop();
                    } finally {
                        if (c instanceof javax.swing.JComponent) {
                            javax.swing.JComponent jc = (javax.swing.JComponent) c;
                            jc.setBorder(normalBorder);
                            log("FileDrop: normal border restored.");
                        }
                    }
                }

                public void dragExit(java.awt.dnd.DropTargetEvent evt) {
                    log("FileDrop: dragExit event.");
                    if (c instanceof javax.swing.JComponent) {
                        javax.swing.JComponent jc = (javax.swing.JComponent) c;
                        jc.setBorder(normalBorder);
                        log("FileDrop: normal border restored.");
                    }
                }

                public void dropActionChanged(java.awt.dnd.DropTargetDragEvent evt) {
                    log("FileDrop: dropActionChanged event.");
                    if (isDragOk(evt)) {
                        evt.acceptDrag(java.awt.dnd.DnDConstants.ACTION_COPY);
                        log("FileDrop: event accepted.");
                    } else {
                        evt.rejectDrag();
                        log("FileDrop: event rejected.");
                    }
                }
            };
            makeDropTarget(c, true);
        } else {
            log("FileDrop: Drag and drop is not supported with this JVM");
        }
    }

    private static boolean supportsDnD() {
        if (supportsDnD == null) {
            boolean support;
            try {
                Class.forName("java.awt.dnd.DnDConstants");
                support = true;
            } catch (Exception e) {
                support = false;
            }
            supportsDnD = support;
        }
        return supportsDnD;
    }

    private static final String ZERO_CHAR_STRING = "" + (char) 0;

    private static File[] createFileArray(BufferedReader bReader) {
        try {
            List<File> list = new java.util.ArrayList<File>();
            java.lang.String line;
            while ((line = bReader.readLine()) != null) {
                try {
                    if (ZERO_CHAR_STRING.equals(line)) continue;
                    java.io.File file = new java.io.File(new java.net.URI(line));
                    list.add(file);
                } catch (Exception ex) {
                    log("Error with " + line + ": " + ex.getMessage());
                }
            }
            return list.toArray(new File[list.size()]);
        } catch (IOException ex) {
            log("FileDrop: IOException");
        }
        return new File[0];
    }

    private void makeDropTarget(final Component c, boolean recursive) {
        final java.awt.dnd.DropTarget dt = new java.awt.dnd.DropTarget();
        try {
            dt.addDropTargetListener(dropListener);
        } catch (java.util.TooManyListenersException e) {
            e.printStackTrace();
            log("FileDrop: Drop will not work due to previous error. Do you have another listener attached?");
        }
        c.addHierarchyListener(new java.awt.event.HierarchyListener() {

            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                log("FileDrop: Hierarchy changed.");
                java.awt.Component parent = c.getParent();
                if (parent == null) {
                    c.setDropTarget(null);
                    log("FileDrop: Drop target cleared from component.");
                } else {
                    new java.awt.dnd.DropTarget(c, dropListener);
                    log("FileDrop: Drop target added to component.");
                }
            }
        });
        if (c.getParent() != null) new java.awt.dnd.DropTarget(c, dropListener);
        if (recursive && (c instanceof java.awt.Container)) {
            java.awt.Container cont = (java.awt.Container) c;
            java.awt.Component[] comps = cont.getComponents();
            for (Component comp : comps) {
                makeDropTarget(comp, recursive);
            }
        }
    }

    /**
     * Determine if the dragged data is a file list.
     * @param evt The event
     * @return Return if dragged data is a file list
     */
    private boolean isDragOk(final DropTargetDragEvent evt) {
        boolean ok = false;
        java.awt.datatransfer.DataFlavor[] flavors = evt.getCurrentDataFlavors();
        int i = 0;
        while (!ok && i < flavors.length) {
            final DataFlavor curFlavor = flavors[i];
            if (curFlavor.equals(java.awt.datatransfer.DataFlavor.javaFileListFlavor) || curFlavor.isRepresentationClassReader()) {
                ok = true;
            }
            i++;
        }
        if (LOGGER.isDebugEnabled()) {
            if (flavors.length == 0) log("FileDrop: no data flavors.");
            for (i = 0; i < flavors.length; i++) log(flavors[i].toString());
        }
        return ok;
    }

    /**
     * Outputs <tt>message</tt> to <tt>out</tt> if it's not null.
     *
     * @param message The message to log
     */
    private static void log(String message) {
        LOGGER.debug(message);
    }

    /**
     * Removes the drag-and-drop hooks from the component and optionally
     * from the all children. You should call this if you add and remove
     * components after you've set up the drag-and-drop.
     * This will recursively unregister all components contained within
     * <var>c</var> if <var>c</var> is a {@link java.awt.Container}.
     *
     * @param c The component to unregister as a drop target
     * @since 1.0
     */
    public static boolean remove(java.awt.Component c) {
        return remove(c, true);
    }

    /**
     * Removes the drag-and-drop hooks from the component and optionally
     * from the all children. You should call this if you add and remove
     * components after you've set up the drag-and-drop.
     *
     *
     * @param c         The component to unregister
     * @param recursive Recursively unregister components within a container
     * @since 1.0
     */
    public static boolean remove(Component c, boolean recursive) {
        if (supportsDnD()) {
            log("FileDrop: Removing drag-and-drop hooks.");
            c.setDropTarget(null);
            if (recursive && (c instanceof java.awt.Container)) {
                java.awt.Component[] comps = ((java.awt.Container) c).getComponents();
                for (Component comp : comps) {
                    remove(comp, recursive);
                }
                return true;
            } else return false;
        } else return false;
    }

    /**
     * Implement this inner interface to listen for when files are dropped. For example
     * your class declaration may begin like this:
     * <code><pre>
     *      public class MyClass implements FileDrop.Listener
     *      ...
     *      public void filesDropped( java.io.File[] files )
     *      {
     *          ...
     *      }   // end filesDropped
     *      ...
     * </pre></code>
     *
     * @since 1.1
     */
    public static interface Listener {

        /**
         * This method is called when files have been successfully dropped.
         *
         *
         * @param evt
         * @param files An array of <tt>File</tt>s that were dropped.
         * @since 1.0
         */
        public abstract void filesDropped(DropTargetDropEvent evt, File[] files);
    }

    /**
     * This is the event that is passed to the
     * {@link FileDropListener#filesDropped filesDropped(...)} method in
     * your {@link FileDropListener} when files are dropped onto
     * a registered drop target.
     * <p/>
     * <p>I'm releasing this code into the Public Domain. Enjoy.</p>
     *
     * @author Robert Harder
     * @author rob@iharder.net
     * @version 1.2
     */
    public static class Event extends java.util.EventObject {

        private java.io.File[] files;

        /**
         * Constructs an {@link Event} with the array
         * of files that were dropped and the
         * {@link FileDrop} that initiated the event.
         *
         * @param files The array of files that were dropped
         * @source The event source
         * @since 1.1
         */
        public Event(java.io.File[] files, Object source) {
            super(source);
            this.files = files;
        }

        /**
         * Returns an array of files that were dropped on a
         * registered drop target.
         *
         * @return array of files that were dropped
         * @since 1.1
         */
        public java.io.File[] getFiles() {
            return files;
        }
    }

    /**
     * At last an easy way to encapsulate your custom objects for dragging and dropping
     * in your Java programs!
     * When you need to create a {@link java.awt.datatransfer.Transferable} object,
     * use this class to wrap your object.
     * For example:
     * <pre><code>
     *      ...
     *      MyCoolClass myObj = new MyCoolClass();
     *      Transferable xfer = new TransferableObject( myObj );
     *      ...
     * </code></pre>
     * Or if you need to know when the data was actually dropped, like when you're
     * moving data out of a list, say, you can use the {@link TransferableObject.Fetcher}
     * inner class to return your object Just in Time.
     * For example:
     * <pre><code>
     *      ...
     *      final MyCoolClass myObj = new MyCoolClass();
     * <p/>
     *      TransferableObject.Fetcher fetcher = new TransferableObject.Fetcher()
     *      {   public Object getObject(){ return myObj; }
     *      }; // end fetcher
     * <p/>
     *      Transferable xfer = new TransferableObject( fetcher );
     *      ...
     * </code></pre>
     * <p/>
     * The {@link java.awt.datatransfer.DataFlavor} associated with
     * {@link TransferableObject} has the representation class
     * <tt>net.iharder.dnd.TransferableObject.class</tt> and MIME type
     * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
     * This data flavor is accessible via the static
     * {@link #DATA_FLAVOR} property.
     * <p/>
     * <p/>
     * <p>I'm releasing this code into the Public Domain. Enjoy.</p>
     *
     * @author Robert Harder
     * @author rob@iharder.net
     * @version 1.2
     */
    public static class TransferableObject implements java.awt.datatransfer.Transferable {

        /**
         * The MIME type for {@link #DATA_FLAVOR} is
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @since 1.1
         */
        public static final String MIME_TYPE = "application/x-net.iharder.dnd.TransferableObject";

        /**
         * The default {@link java.awt.datatransfer.DataFlavor} for
         * {@link TransferableObject} has the representation class
         * <tt>net.iharder.dnd.TransferableObject.class</tt>
         * and the MIME type
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @since 1.1
         */
        public static final java.awt.datatransfer.DataFlavor DATA_FLAVOR = new java.awt.datatransfer.DataFlavor(FileDrop.TransferableObject.class, MIME_TYPE);

        private Fetcher fetcher;

        private Object data;

        private java.awt.datatransfer.DataFlavor customFlavor;

        /**
         * Creates a new {@link TransferableObject} that wraps <var>data</var>.
         * Along with the {@link #DATA_FLAVOR} associated with this class,
         * this creates a custom data flavor with a representation class
         * determined from <code>data.getClass()</code> and the MIME type
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @param data The data to transfer
         * @since 1.1
         */
        public TransferableObject(Object data) {
            this.data = data;
            this.customFlavor = new java.awt.datatransfer.DataFlavor(data.getClass(), MIME_TYPE);
        }

        /**
         * Creates a new {@link TransferableObject} that will return the
         * object that is returned by <var>fetcher</var>.
         * No custom data flavor is set other than the default
         * {@link #DATA_FLAVOR}.
         *
         * @param fetcher The {@link Fetcher} that will return the data object
         * @see Fetcher
         * @since 1.1
         */
        public TransferableObject(Fetcher fetcher) {
            this.fetcher = fetcher;
        }

        /**
         * Creates a new {@link TransferableObject} that will return the
         * object that is returned by <var>fetcher</var>.
         * Along with the {@link #DATA_FLAVOR} associated with this class,
         * this creates a custom data flavor with a representation class <var>dataClass</var>
         * and the MIME type
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @param dataClass The {@link java.lang.Class} to use in the custom data flavor
         * @param fetcher   The {@link Fetcher} that will return the data object
         * @see Fetcher
         * @since 1.1
         */
        public TransferableObject(Class dataClass, Fetcher fetcher) {
            this.fetcher = fetcher;
            this.customFlavor = new java.awt.datatransfer.DataFlavor(dataClass, MIME_TYPE);
        }

        /**
         * Returns the custom {@link java.awt.datatransfer.DataFlavor} associated
         * with the encapsulated object or <tt>null</tt> if the {@link Fetcher}
         * constructor was used without passing a {@link java.lang.Class}.
         *
         * @return The custom data flavor for the encapsulated object
         * @since 1.1
         */
        public java.awt.datatransfer.DataFlavor getCustomDataFlavor() {
            return customFlavor;
        }

        /**
         * Returns a two- or three-element array containing first
         * the custom data flavor, if one was created in the constructors,
         * second the default {@link #DATA_FLAVOR} associated with
         * {@link TransferableObject}, and third the
         * {@link java.awt.datatransfer.DataFlavor.stringFlavor}.
         *
         * @return An array of supported data flavors
         * @since 1.1
         */
        public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors() {
            if (customFlavor != null) return new java.awt.datatransfer.DataFlavor[] { customFlavor, DATA_FLAVOR, java.awt.datatransfer.DataFlavor.stringFlavor }; else return new java.awt.datatransfer.DataFlavor[] { DATA_FLAVOR, java.awt.datatransfer.DataFlavor.stringFlavor };
        }

        /**
         * Returns the data encapsulated in this {@link TransferableObject}.
         * If the {@link Fetcher} constructor was used, then this is when
         * the {@link Fetcher#getObject getObject()} method will be called.
         * If the requested data flavor is not supported, then the
         * {@link Fetcher#getObject getObject()} method will not be called.
         *
         * @param flavor The data flavor for the data to return
         * @return The dropped data
         * @since 1.1
         */
        public Object getTransferData(java.awt.datatransfer.DataFlavor flavor) throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException {
            if (flavor.equals(DATA_FLAVOR)) return fetcher == null ? data : fetcher.getObject();
            if (flavor.equals(java.awt.datatransfer.DataFlavor.stringFlavor)) return fetcher == null ? data.toString() : fetcher.getObject().toString();
            throw new java.awt.datatransfer.UnsupportedFlavorException(flavor);
        }

        /**
         * Returns <tt>true</tt> if <var>flavor</var> is one of the supported
         * flavors. Flavors are supported using the <code>equals(...)</code> method.
         *
         * @param flavor The data flavor to check
         * @return Whether or not the flavor is supported
         * @since 1.1
         */
        public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor) {
            if (flavor.equals(DATA_FLAVOR)) return true;
            if (flavor.equals(java.awt.datatransfer.DataFlavor.stringFlavor)) return true;
            return false;
        }

        /**
         * Instead of passing your data directly to the {@link TransferableObject}
         * constructor, you may want to know exactly when your data was received
         * in case you need to remove it from its source (or do anyting else to it).
         * When the {@link #getTransferData getTransferData(...)} method is called
         * on the {@link TransferableObject}, the {@link Fetcher}'s
         * {@link #getObject getObject()} method will be called.
         *
         * @author Robert Harder
         * @version 1.1
         * @copyright 2001
         * @since 1.1
         */
        public static interface Fetcher {

            /**
             * Return the object being encapsulated in the
             * {@link TransferableObject}.
             *
             * @return The dropped object
             * @since 1.1
             */
            public abstract Object getObject();
        }
    }
}
