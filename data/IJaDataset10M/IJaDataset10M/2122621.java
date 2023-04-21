package net.sf.xpontus.utils;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.JTextComponent;

/**
 * Hopefully his class will be doing lots of useful stuff
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class GUIUtils {

    private static FileFilter[] m_filters;

    private static final String ACTION_SWITCH_NEXT_WINDOW = "next-window";

    private static final String ACTION_SWITCH_PREVIOUS_WINDOW = "previous-window";

    private static Action nextWindowAction;

    private static Action previousWindowAction;

    private GUIUtils() {
    }

    public static FileFilter createFilter(final String description, final List patterns) {
        FileFilter m_filter = new FileFilter() {

            public boolean accept(File f) {
                WildcardFileFilter a = new WildcardFileFilter(patterns, IOCase.INSENSITIVE);
                if (f.isDirectory()) {
                    return true;
                }
                return a.accept(f);
            }

            public String getDescription() {
                return description;
            }
        };
        return m_filter;
    }

    private static void initFilters() {
        if (m_filters == null) {
            MimeTypesProvider provider = MimeTypesProvider.getInstance();
            ListOrderedMap types = provider.getTypes();
            m_filters = new FileFilter[types.size()];
            Iterator m_it = types.keySet().iterator();
            for (int i = 0; m_it.hasNext(); i++) {
                String mime = m_it.next().toString();
                List files = (List) types.get(mime);
                m_filters[i] = createFilter(mime, files);
            }
        }
    }

    public static void installDefaultFilters(JFileChooser chooser) {
        initFilters();
        for (FileFilter m_filter : m_filters) {
            chooser.addChoosableFileFilter(m_filter);
        }
        chooser.setFileFilter(chooser.getAcceptAllFileFilter());
    }

    /**
     * Return a font
     * @param fontString the font information
     * @return a font from a string descriptor
     */
    public static Font getFontFromString(String fontString) {
        String[] m_table = fontString.split(",");
        String m_family = m_table[0];
        int m_style = Integer.parseInt(m_table[1]);
        int m_size = Integer.parseInt(m_table[2]);
        Font m_font = new Font(m_family, m_style, m_size);
        return m_font;
    }

    private static void initSwitchWindowActions() {
        nextWindowAction = new net.sf.xpontus.actions.impl.SwitchWindowAction(ACTION_SWITCH_NEXT_WINDOW, true);
        previousWindowAction = new net.sf.xpontus.actions.impl.SwitchWindowAction(ACTION_SWITCH_NEXT_WINDOW, false);
    }

    public static void installWindowSwitcher(JTextComponent editor) {
        if (nextWindowAction == null) {
            initSwitchWindowActions();
        }
        editor.getActionMap().put(ACTION_SWITCH_NEXT_WINDOW, nextWindowAction);
        editor.getActionMap().put(ACTION_SWITCH_PREVIOUS_WINDOW, previousWindowAction);
        editor.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, ActionEvent.CTRL_MASK), ACTION_SWITCH_NEXT_WINDOW);
        editor.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, ActionEvent.CTRL_MASK), ACTION_SWITCH_PREVIOUS_WINDOW);
    }

    /**
     * Returns a String from a font
     * @param m_font The font to convert
     * @return a String from a font
     */
    public static String fontToString(Font m_font) {
        return new StringBuilder(m_font.getFamily()).append(",").append(m_font.getStyle()).append(",").append(m_font.getSize()).toString();
    }

    /**
     * This function calls Component.setFocusableWindowState (in java >= 1.4) to  keep GUI
     * consistent with java 1.3.x
     *
     * @param aObj TODO: DOCUMENT ME!
     * @param aFlag TODO: DOCUMENT ME!
     */
    public static final void setFocusableWindowState(Window aObj, boolean aFlag) {
        try {
            aObj.getClass().getMethod("setFocusableWindowState", new Class[] { Boolean.TYPE }).invoke(aObj, new Object[] { aFlag ? Boolean.TRUE : Boolean.FALSE });
        } catch (java.lang.NoSuchMethodException ex) {
        } catch (java.lang.IllegalAccessException ex) {
        } catch (java.lang.reflect.InvocationTargetException ex) {
        }
    }

    public static void installDragAndDropSupport(Component component) {
        new DropTarget(component, new DropTargetAdapter() {

            public void drop(DropTargetDropEvent e) {
                try {
                    Transferable t = e.getTransferable();
                    e.acceptDrop(e.getDropAction());
                    DataFlavor[] flavors = e.getCurrentDataFlavors();
                    URL fileURL = null;
                    List fileList = null;
                    String dndString = null;
                    DataFlavor urlFlavor = null;
                    DataFlavor listFlavor = null;
                    DataFlavor stringFlavor = null;
                    if (flavors != null) {
                        for (int i = 0; i < flavors.length; i++) {
                            if (flavors[i].getRepresentationClass() == java.net.URL.class) {
                                urlFlavor = flavors[i];
                            } else if (flavors[i].getRepresentationClass() == java.util.List.class) {
                                listFlavor = flavors[i];
                            } else if (flavors[i].getRepresentationClass() == java.lang.String.class) {
                                stringFlavor = flavors[i];
                            }
                        }
                    } else {
                        System.out.println("flavors == null");
                        return;
                    }
                    if (stringFlavor != null) {
                        try {
                            Object obj = e.getTransferable().getTransferData(stringFlavor);
                            if (obj instanceof String) {
                                dndString = (String) obj;
                            }
                        } catch (Throwable tr) {
                        }
                        if (dndString != null) {
                            StringReader reader = new StringReader(dndString.trim());
                            BufferedReader mReader = new BufferedReader(reader);
                            String line = null;
                            while ((line = mReader.readLine()) != null) {
                                URL url = new URL(line.trim());
                                System.out.println("String");
                                if (url.getProtocol().equals("file")) {
                                    DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer().createEditorFromFile(new File(url.getPath()));
                                } else {
                                    DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer().createEditorFromURL(url);
                                }
                            }
                            IOUtils.closeQuietly(mReader);
                            IOUtils.closeQuietly(reader);
                        }
                    }
                    try {
                        Object obj = e.getTransferable().getTransferData(urlFlavor);
                        if (obj instanceof URL) {
                            fileURL = (URL) obj;
                        }
                    } catch (Throwable tr) {
                    }
                    if (fileURL != null) {
                        URL url = fileURL;
                        DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer().createEditorFromURL(url);
                    }
                    try {
                        Object obj = e.getTransferable().getTransferData(listFlavor);
                        if (obj instanceof java.util.List) {
                            fileList = (java.util.List) obj;
                        }
                    } catch (Throwable tr) {
                    }
                    if (fileList != null) {
                        Iterator it = fileList.iterator();
                        while (it.hasNext()) {
                            Object file = it.next();
                            if (file instanceof File) {
                                DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer().createEditorFromFile((File) file);
                            }
                        }
                    }
                    try {
                        e.dropComplete(true);
                    } catch (Throwable tr) {
                        tr.printStackTrace();
                    }
                } catch (Exception ex) {
                }
            }
        });
    }
}
