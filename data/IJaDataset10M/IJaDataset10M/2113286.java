package net.sf.appomatox.gui.diagramm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.NumberFormat;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import net.sf.appomatox.control.DiagrammChangedObserver;
import net.sf.appomatox.gui.AppoMainFrame;
import net.sf.appomatox.gui.DiagrammController;
import net.sf.appomatox.gui.TabManager;
import net.sf.appomatox.gui.diagramm.dma.DiagrammMouseAdapter;
import net.sf.appomatox.gui.diagramm.dma.DiagrammNull;
import net.sf.appomatox.gui.diagramm.dma.DiagrammVerschieben;
import net.sf.appomatox.gui.diagramm.dma.DiagrammZeigeAktPosition;
import net.sf.appomatox.gui.diagramm.dma.DiagrammZoom;
import net.sf.appomatox.gui.diagramm.elemente.DiagrammElementChooser;
import net.sf.appomatox.gui.printPreview.PrintPreview;
import net.sf.appomatox.utils.DateiFilter;
import net.sf.appomatox.utils.UniversalTransferable;
import net.sf.appomatox.utils.Utils;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.jdesktop.swingx.error.ErrorInfo;
import com.jgoodies.forms.factories.Borders;
import com.thoughtworks.xstream.converters.ConversionException;

public class DiagrammFenster extends JPanel implements DiagrammChangedObserver {

    private static Logger s_logger = Logger.getLogger(DiagrammFenster.class.getName());

    private final DiagrammController m_Ctrl;

    private Diagramm m_Diagramm = null;

    private JPanel m_pnlDiagramm = null;

    private JLabel m_lblAktPosition = new JLabel();

    private NumberFormat nf = NumberFormat.getNumberInstance();

    private DiagrammMouseAdapter m_dmaVerschiebeDiagramm;

    private DiagrammMouseAdapter m_dmaZoom;

    private DiagrammMouseAdapter m_dmaNull;

    private Action m_actVollbildmodusBeenden = new AbstractAction("Vollbildmodus beenden") {

        {
            putValue(Action.SMALL_ICON, Utils.getIcon("/res/16x16/back.png"));
            putValue(Action.LARGE_ICON_KEY, Utils.getIcon("/res/16x16/back.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice myDevice = ge.getDefaultScreenDevice();
            getPnlDiagramm().add(getDiagramm(), BorderLayout.CENTER);
            getDMANull().getAction("-").actionPerformed(e);
            myDevice.setFullScreenWindow(null);
            m_MainFrame.setVisible(true);
        }
    };

    private Action m_actDiagrammZentrieren = new AbstractAction("zentrieren") {

        {
            ImageIcon i = Utils.getIcon("/res/16x16/free_icon.png");
            putValue(Action.LARGE_ICON_KEY, i);
            putValue(Action.SMALL_ICON, i);
            putValue(Action.SHORT_DESCRIPTION, "Diagramm auf Standardansicht zur�cksetzen");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            m_Ctrl.setMinMaxX(-10, 10);
            m_Ctrl.setMinMaxY(-10, 10);
            repaint();
        }
    };

    private final TabManager m_TabManager;

    private final AppoMainFrame m_MainFrame;

    private Inhaltspanel m_Inhaltspanel;

    public DiagrammFenster(DiagrammController diagCtrl, AppoMainFrame mainFrame) {
        assert diagCtrl != null;
        assert mainFrame != null;
        m_MainFrame = mainFrame;
        m_TabManager = mainFrame.getTabManager();
        m_Ctrl = diagCtrl;
        m_Inhaltspanel = new Inhaltspanel(m_Ctrl);
        nf.setMinimumFractionDigits(2);
        nf.setGroupingUsed(true);
        initialize();
        m_Ctrl.setDiagrammChangedObserver(this);
    }

    private void initialize() {
        setLayout(new BorderLayout());
        JXTaskPaneContainer tpcEigenschaften = new JXTaskPaneContainer();
        tpcEigenschaften.add(getTPNavigation());
        tpcEigenschaften.add(getTPAufgaben());
        tpcEigenschaften.add(getTPElemente());
        tpcEigenschaften.setPreferredSize(new Dimension(200, 200));
        this.add(tpcEigenschaften, BorderLayout.WEST);
        this.add(getPnlDiagramm(), BorderLayout.CENTER);
    }

    private JXTaskPane getTPAufgaben() {
        JXTaskPane tp = new JXTaskPane();
        tp.setTitle("Diagramm");
        tp.add(new AbstractAction("Als Vollbild anzeigen") {

            {
                putValue(Action.SMALL_ICON, Utils.getIcon("/res/16x16/solidfilldlg.png"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice myDevice = ge.getDefaultScreenDevice();
                JWindow window = new JWindow(m_MainFrame);
                window.setLayout(new BorderLayout());
                window.add(getToolbarVollbildmodus(), BorderLayout.WEST);
                getPnlDiagramm().remove(getDiagramm());
                window.add(getDiagramm(), BorderLayout.CENTER);
                getDMANull().getAction("-").actionPerformed(e);
                if (myDevice.isFullScreenSupported()) {
                    m_MainFrame.setVisible(false);
                    myDevice.setFullScreenWindow(window);
                } else {
                    JOptionPane.showMessageDialog(DiagrammFenster.this, "Der Vollbildmodus wird von der Grafikkarte nicht unterst�tzt.", "Warnung", JOptionPane.WARNING_MESSAGE);
                    s_logger.info("Vollbildmodus wird vom GraphicsDevice nicht unterst�tzt. myDevice.isFullScreenSupported()==false");
                }
            }
        });
        tp.add(new AbstractAction("Speichern unter...") {

            {
                putValue(Action.SMALL_ICON, Utils.getIcon("/res/16x16/filesaveas.png"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setAcceptAllFileFilterUsed(false);
                DateiFilter filterPNG = new DateiFilter(new String[] { "png" }, "PNG-Dateien");
                fc.addChoosableFileFilter(filterPNG);
                DateiFilter filterADG = new DateiFilter(new String[] { "adg" }, "Appomatox Diagramm-Datei");
                fc.addChoosableFileFilter(filterADG);
                int ret = fc.showSaveDialog(DiagrammFenster.this);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    DateiFilter ff = (DateiFilter) fc.getFileFilter();
                    f = ff.getFileMitExtension(f);
                    if (f.exists()) {
                        int ret2 = JOptionPane.showConfirmDialog(DiagrammFenster.this, "Die Datei [" + f.getAbsolutePath() + "] existiert bereits.\nSoll sie �berschrieben werden?", "Datei �berschreiben?", JOptionPane.YES_NO_OPTION);
                        if (ret2 == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }
                    try {
                        if (filterPNG.accept(f)) {
                            BufferedImage img = getDiagramm().getImage(getDiagramm().getWidth(), getDiagramm().getHeight());
                            boolean success = ImageIO.write(img, "png", f);
                            assert success;
                        } else {
                            m_Ctrl.saveDiagrammAsObject(f);
                        }
                        JOptionPane.showMessageDialog(DiagrammFenster.this, "Diagramm wurde erfolgreich gespeichert", "Information", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception e1) {
                        ErrorInfo ei = Utils.getErrorInfo("Die Datei konnte nicht geschrieben werden.", e1);
                        JXErrorPane.showDialog(DiagrammFenster.this, ei);
                    }
                }
            }
        });
        tp.add(new AbstractAction("Laden...") {

            {
                putValue(Action.SMALL_ICON, Utils.getIcon("/res/16x16/fileopen.png"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                DateiFilter filter = new DateiFilter();
                filter.addExtension("adg");
                filter.setDescription("Appomatox-Diagramm");
                fc.setFileFilter(filter);
                int ret = fc.showOpenDialog(DiagrammFenster.this);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    try {
                        m_Ctrl.loadDiagrammAsObject(f);
                        getDiagramm().repaint();
                    } catch (ConversionException e1) {
                        ErrorInfo ei = Utils.getErrorInfo("Das Datenformat der Datei ist ung�ltig.", e1);
                        JXErrorPane.showDialog(DiagrammFenster.this, ei);
                    } catch (Exception e1) {
                        ErrorInfo ei = Utils.getErrorInfo("Die Datei konnte nicht gelesen werden.", e1);
                        JXErrorPane.showDialog(DiagrammFenster.this, ei);
                    }
                }
            }
        });
        tp.add(new AbstractAction("Drucken...") {

            {
                putValue(Action.SMALL_ICON, Utils.getIcon("/res/16x16/fileprint.png"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                new PrintPreview(null, getDiagramm()).setVisible(true);
            }
        });
        tp.add(new AbstractAction("In Zwischenablage kopieren") {

            {
                putValue(Action.SMALL_ICON, Utils.getIcon("/res/16x16/editcopy.png"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage img = getDiagramm().getImage(getDiagramm().getWidth(), getDiagramm().getHeight());
                UniversalTransferable transferable = new UniversalTransferable();
                transferable.addData(DataFlavor.imageFlavor, img);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(transferable, null);
            }
        });
        tp.add(new AbstractAction("L�schen...") {

            {
                putValue(Action.SMALL_ICON, Utils.getIcon("/res/16x16/cancel.png"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                int ret = JOptionPane.showConfirmDialog(DiagrammFenster.this, "Wollen Sie das Diagramm wirklich l�schen?", "Frage", JOptionPane.OK_CANCEL_OPTION);
                if (ret == JOptionPane.OK_OPTION) {
                    m_Ctrl.clear();
                    getDiagramm().repaint();
                }
            }
        });
        tp.add(new AbstractAction("Element hinzuf�gen...") {

            {
                putValue(Action.SMALL_ICON, Utils.getIcon("/res/16x16/Crystal_Clear_action_edit_add.png"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                Window root = (Window) DiagrammFenster.this.getTopLevelAncestor();
                DiagrammElementChooser dec = new DiagrammElementChooser(root, m_Ctrl);
                dec.setLocationByPlatform(true);
                dec.setModalityType(ModalityType.APPLICATION_MODAL);
                dec.setVisible(true);
            }
        });
        return tp;
    }

    private JToolBar getToolbarVollbildmodus() {
        JToggleButton tbVerschieben = Utils.createIconButton(getDMAVerschiebeDiagramm().getAction("Verschieben"));
        JToggleButton tbZoom = Utils.createIconButton(getDMAZoom().getAction("Zoomen"));
        JToggleButton tbNull = Utils.createIconButton(getDMANull().getAction("-"));
        ButtonGroup bg = new ButtonGroup();
        bg.add(tbVerschieben);
        bg.add(tbZoom);
        bg.add(tbNull);
        bg.setSelected(tbNull.getModel(), true);
        JToolBar toolbar = new JToolBar(SwingConstants.VERTICAL);
        toolbar.add(Utils.createIconButton(new JButton(), m_actVollbildmodusBeenden));
        toolbar.add(new JToolBar.Separator());
        toolbar.add(Utils.createIconButton(new JButton(), m_actDiagrammZentrieren));
        toolbar.add(new JToolBar.Separator());
        toolbar.add(tbVerschieben);
        toolbar.add(tbZoom);
        toolbar.add(tbNull);
        return toolbar;
    }

    private DiagrammMouseAdapter getDMAVerschiebeDiagramm() {
        if (m_dmaVerschiebeDiagramm == null) {
            m_dmaVerschiebeDiagramm = new DiagrammVerschieben(m_Ctrl, getDiagramm());
        }
        return m_dmaVerschiebeDiagramm;
    }

    private DiagrammMouseAdapter getDMAZoom() {
        if (m_dmaZoom == null) {
            m_dmaZoom = new DiagrammZoom(m_Ctrl, getDiagramm());
        }
        return m_dmaZoom;
    }

    private DiagrammMouseAdapter getDMANull() {
        if (m_dmaNull == null) {
            m_dmaNull = new DiagrammNull(m_Ctrl, getDiagramm());
        }
        return m_dmaNull;
    }

    private JXTaskPane getTPNavigation() {
        JToggleButton tbVerschieben = Utils.createIconButton(getDMAVerschiebeDiagramm().getAction("Verschieben"));
        JToggleButton tbZoom = Utils.createIconButton(getDMAZoom().getAction("Zoomen"));
        JToggleButton tbNull = Utils.createIconButton(getDMANull().getAction("-"));
        JPanel pnl = new JPanel();
        pnl.setOpaque(false);
        pnl.add(tbVerschieben);
        pnl.add(tbZoom);
        pnl.add(tbNull);
        pnl.add(Utils.createIconButton(new JButton(), m_actDiagrammZentrieren));
        ButtonGroup bg = new ButtonGroup();
        bg.add(tbVerschieben);
        bg.add(tbZoom);
        bg.add(tbNull);
        bg.setSelected(tbNull.getModel(), true);
        JXTaskPane tp = new JXTaskPane();
        tp.setTitle("Navigation");
        m_lblAktPosition.setText("( " + nf.format(0) + " | " + nf.format(0) + " )");
        tp.add(m_lblAktPosition);
        tp.add(pnl);
        tp.add(new AbstractAction("Diagrammma�e einstellen...") {

            {
                putValue(Action.SMALL_ICON, Utils.getIcon("/res/16x16/transform.png"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                DiagrammMasseBearbeitenDialog dmbd = new DiagrammMasseBearbeitenDialog(m_Ctrl, DiagrammFenster.this);
                JDialog dlg = dmbd.getDialog();
                dlg.setVisible(true);
            }
        });
        return tp;
    }

    private JXTaskPane getTPElemente() {
        JXTaskPane tp = new JXTaskPane();
        tp.setTitle("Elemente");
        tp.add(m_Inhaltspanel.getPanel());
        return tp;
    }

    private JPanel getPnlDiagramm() {
        if (m_pnlDiagramm == null) {
            m_pnlDiagramm = new JPanel();
            Border border = BorderFactory.createCompoundBorder(Borders.DIALOG_BORDER, new DropShadowBorder());
            m_pnlDiagramm.setBorder(border);
            m_pnlDiagramm.setOpaque(false);
            m_pnlDiagramm.setLayout(new BorderLayout());
            m_pnlDiagramm.add(getDiagramm(), BorderLayout.CENTER);
        }
        return m_pnlDiagramm;
    }

    private Diagramm getDiagramm() {
        if (m_Diagramm == null) {
            m_Diagramm = new Diagramm(m_Ctrl);
            MouseAdapter maZeigeAktuellePosition = new DiagrammZeigeAktPosition(m_Ctrl, m_Diagramm, m_lblAktPosition);
            m_Diagramm.addMouseMotionListener(maZeigeAktuellePosition);
        }
        return m_Diagramm;
    }

    @Override
    public void diagrammChanged() {
        if (!m_TabManager.isTabVisible(TabManager.DIAGRAMM)) {
            m_TabManager.addTab(TabManager.DIAGRAMM);
        }
        m_TabManager.bringTabToFront(TabManager.DIAGRAMM);
        m_Inhaltspanel.refresh();
        getDiagramm().repaint();
    }
}
