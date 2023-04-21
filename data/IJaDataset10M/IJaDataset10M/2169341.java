package TangramBase;

import TangramCore.Database;
import TangramCore.Hladina;
import TangramCore.HladinaObrys;
import TangramCore.ObrazecListener;
import TangramCore.Skladacka;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.border.LineBorder;
import org.freehep.util.export.ExportDialog;

public class LayoutFrame extends JFrame implements DocumentWindow, ComponentListener, ObrazecListener {

    static final long serialVersionUID = 24362462L;

    private JMenuItem mniThis = null;

    private int iWindow = -1;

    boolean changed = true;

    String layoutFile = null;

    TangramFrame frmDatabase;

    ObrazecLayout obr;

    Skladacka skl;

    Database database;

    StyleDialog frmStyle;

    Dimension dim;

    public LayoutFrame(TangramFrame tf, Database d, int w, int h) {
        frmDatabase = tf;
        database = d;
        skl = database.getActSet();
        Container pane = getContentPane();
        pane.setLayout(null);
        pane.setBackground(Color.GRAY);
        setSize(800, 600);
        dim = new Dimension(w, h);
        obr = new ObrazecLayout(skl, Color.WHITE, (double) w / h, 800, 600);
        obr.setBorder(LineBorder.createBlackLineBorder());
        obr.addObrazecListener(this);
        pane.add(obr);
        obr.grabFocus();
        resize();
        setTitle();
        frmStyle = new StyleDialog(obr, this, true);
        TangramFrame.centerOnScreen(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                showDocument(false);
            }
        });
        addComponentListener(this);
    }

    public boolean hasChanged() {
        return false;
    }

    public void restoreState() {
        toFront();
    }

    private void setTitle() {
        super.setTitle("Layout Editor" + sDash + skl.nazev + sLFigureBracket + (layoutFile == null ? "new" : layoutFile) + (changed ? sChanged : sUpToDate) + sRFigureBracket);
        setMenuItemText();
    }

    private void setMenuItemText() {
        if (mniThis != null) mniThis.setText(sLNumberBracket + iWindow + (changed ? sChanged : sUpToDate) + sRNumberBracket + getTitle());
    }

    public void setMenuItem(int i, JMenuItem mni) {
        iWindow = i;
        mniThis = mni;
        setMenuItemText();
    }

    public boolean showDocument(boolean jak) {
        boolean ret = jak ? frmDatabase.windowOpened(this) : frmDatabase.windowClosed(this);
        setVisible(jak);
        if (jak) toFront(); else dispose();
        return ret;
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    ;

    public void componentShown(ComponentEvent e) {
    }

    private void resize() {
        Dimension dim = this.getContentPane().getSize();
        obr.nastavVelikost(dim.width, dim.height);
    }

    public void componentResized(ComponentEvent e) {
        resize();
        repaint();
    }

    public void changed() {
    }

    public void keyDown(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            ExportDialog export = new ExportDialog();
            export.showExportDialog(this, "Export view as ...", obr, "Layout");
        }
    }

    public void mouseDown(MouseEvent e) {
        Hladina h = obr.getHladinaFromPoint(e.getPoint());
        if (h == null) return;
        if (h instanceof HladinaObrys) {
            HladinaObrys ho = (HladinaObrys) h;
            if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                ho.prepniVzhled();
                obr.update();
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                TangramFrame.centerOnForm(this, frmStyle);
                frmStyle.show(ho);
            }
        }
    }
}
