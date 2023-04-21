package net.sf.jga.swing.spreadsheet;

import static net.sf.jga.fn.property.PropertyFunctors.invokeMethod;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.JApplet;
import javax.swing.JDesktopPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import net.sf.jga.fn.BinaryFunctor;
import net.sf.jga.fn.adaptor.ApplyBinary;
import net.sf.jga.fn.adaptor.ConstantBinary;
import net.sf.jga.fn.adaptor.Project1st;
import net.sf.jga.fn.adaptor.Project2nd;
import net.sf.jga.fn.property.ArrayUnary;

/**
 * An applet wrapper for Spreadsheet.
 * <p>
 * Copyright &copy; 2004-2005  David A. Hall
 * @author <a href="mailto:davidahall@users.sf.net">David A. Hall</a>
 */
public class Applet extends JApplet {

    static final long serialVersionUID = 1879123030509834030L;

    public Applet() {
    }

    private Spreadsheet _sheet;

    private JScrollPane _pane;

    private JDesktopPane _desktop;

    public void init() {
        printStartupHeader();
        _desktop = new JDesktopPane();
        setContentPane(_desktop);
        _sheet = new Spreadsheet(16, 16);
        _sheet.setPreferredScrollableViewportSize(new Dimension(400, 250));
        _sheet.setEditableByDefault(true);
        _sheet.setRowSelectionInterval(0, 0);
        _sheet.setColumnSelectionInterval(0, 0);
        Controller controller = new Controller(_sheet);
        final JPopupMenu popupMenu = new JPopupMenu("Popup Menu");
        popupMenu.add(new JMenuItem(controller.getCellRenameCmd()));
        popupMenu.add(new JMenuItem(controller.getCellFormatCmd()));
        popupMenu.add(new JMenuItem(controller.getCellTypeCmd()));
        _sheet.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    Spreadsheet sht = (Spreadsheet) e.getComponent();
                    Point p = e.getPoint();
                    int row = sht.rowAtPoint(p);
                    int col = sht.columnAtPoint(p);
                    sht.setRowSelectionInterval(row, row);
                    sht.setColumnSelectionInterval(col, col);
                    if (e.isPopupTrigger()) {
                        popupMenu.show(e.getComponent(), p.x, p.y);
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        _pane = new JScrollPane(_sheet);
        _desktop.add(_pane, javax.swing.JDesktopPane.DEFAULT_LAYER);
        _sheet.setStatusHandler(invokeMethod(JApplet.class, "showStatus", String.class).bind1st(this).compose(new ArrayUnary<String>()));
        controller.setPromptFunctor(buildPromptFunctor());
        controller.setErrorFunctor(buildErrorFunctor());
        String doc = getParameter("worksheet");
        if (doc != null) {
            try {
                URL docURL = new URL(getDocumentBase(), doc);
                Object obj = docURL.getContent();
                if (obj instanceof InputStream) {
                    _sheet.readSpreadsheet((InputStream) obj);
                }
            } catch (IOException x) {
                System.err.println(x.getMessage());
                x.printStackTrace();
            }
        }
    }

    public void start() {
        _pane.setBounds(_desktop.getBounds());
        _pane.setVisible(true);
        _sheet.requestFocusInWindow();
    }

    private static void printStartupHeader() {
        System.out.println("");
        System.out.println("/**");
        System.out.println(" * A Java Hacker's Worksheet");
        System.out.println(" * Copyright (c) 2004-2005  David A. Hall");
        System.out.println(" */");
        System.out.println("");
    }

    @SuppressWarnings("unchecked")
    public BinaryFunctor<String, String, String> buildPromptFunctor() {
        BinaryFunctor<JOptionPane, Object[], String> showInput = invokeMethod(JOptionPane.class, "showInternalInputDialog", Component.class, Object.class, String.class, Integer.TYPE, Icon.class, Object[].class, Object.class);
        ApplyBinary<String, String> sevenArgs = new ApplyBinary<String, String>(new ConstantBinary<String, String, Component>(_desktop), new Project1st<String, String>(), new ConstantBinary<String, String, String>(UIManager.getString("OptionPane.inputDialogTitle")), new ConstantBinary<String, String, Integer>(JOptionPane.QUESTION_MESSAGE), new ConstantBinary<String, String, Icon>(null), new ConstantBinary<String, String, Object[]>(null), new Project2nd<String, String>());
        return showInput.bind1st(null).compose(sevenArgs);
    }

    @SuppressWarnings("unchecked")
    public BinaryFunctor<String, String, ?> buildErrorFunctor() {
        BinaryFunctor<JOptionPane, Object[], String> showError = invokeMethod(JOptionPane.class, "showInternMessageDialog", Component.class, Object.class, String.class, Integer.TYPE);
        ApplyBinary<String, String> fourArgs = new ApplyBinary<String, String>(new ConstantBinary<String, String, Component>(_desktop), new Project1st<String, String>(), new Project2nd<String, String>(), new ConstantBinary<String, String, Integer>(JOptionPane.ERROR_MESSAGE));
        return showError.bind1st(null).compose(fourArgs);
    }
}
