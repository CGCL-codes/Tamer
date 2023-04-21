package org.in4ama.editor.ui.preview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import net.xoetrope.xui.XProjectManager;
import org.apache.log4j.Logger;
import org.in4ama.editor.DialogMgr;
import org.in4ama.editor.project.ProjectHandler;
import org.in4ama.editor.ui.EditorToolbar;
import org.in4ama.editor.ui.ToolbarButton;
import org.jpedal.PdfDecoder;

public class JPedalPdfPagePanel extends JPanel implements ActionListener {

    private static final Logger logger = Logger.getLogger(JPedalPdfPagePanel.class);

    public static final int FIT_WIDTH = 0;

    public static final int FIT_HEIGHT = 1;

    public static final int FIT_FULL = 2;

    public static final int FIT_ZOOM = 3;

    private int fitType = FIT_HEIGHT;

    private int currentPage;

    private float scale = 1.0f;

    PdfDecoder decoder;

    JPanel decoderPanel;

    EditorToolbar toolbar;

    ToolbarButton btnFitWidth, btnFitHeight, btnFitFull, btnZoomIn, btnZoomOut;

    byte[] pdfInput;

    JScrollPane scroll;

    public JPedalPdfPagePanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        decoderPanel = new JPanel();
        decoder = new PdfDecoder();
        decoderPanel.add(decoder);
        scroll = new JScrollPane(decoderPanel);
        scroll.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        Container pane = new Container();
        pane.setLayout(new BorderLayout());
        pane.setBackground(Color.white);
        decoder.setBackground(new Color(220, 220, 220));
        decoder.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        createToolbar();
        add(scroll, BorderLayout.CENTER);
    }

    private void createToolbar() {
        toolbar = new EditorToolbar();
        toolbar.setOpaque(false);
        toolbar.setAlignment("left");
        add(toolbar, BorderLayout.NORTH);
        btnFitWidth = addToolbarButton("fitwidth_icon_16.png", 0);
        btnFitHeight = addToolbarButton("fitheight_icon_16.png", 40);
        btnFitFull = addToolbarButton("zoom_original.png", 80);
        btnZoomIn = addToolbarButton("zoom_in_icon_16.png", 120);
        btnZoomOut = addToolbarButton("zoom_out_icon_16.png", 160);
    }

    private ToolbarButton addToolbarButton(String icon, int x) {
        ToolbarButton button = new ToolbarButton();
        Icon ico = (Icon) XProjectManager.getCurrentProject().getIcon(icon);
        button.setIcon(ico);
        toolbar.add(button);
        button.setLocation(x, 0);
        button.addActionListener(this);
        return button;
    }

    public void setPDFInput(byte[] input) {
        pdfInput = input;
        try {
            decoder.openPdfArray(input);
            decoder.decodePage(1);
        } catch (Exception ex) {
            String msg = "Unable to set the PDF input.";
            logger.error(msg, ex);
            DialogMgr.showError(this, ex);
        }
        doLayout();
    }

    public void doLayout() {
        super.doLayout();
        if (fitType == FIT_WIDTH) {
            int xSize = 650;
            int width = xSize - 10;
            if (getWidth() > 0) width = getWidth() - 10;
            scale = (float) ((float) width / (float) xSize);
        } else if (fitType == FIT_HEIGHT) {
            int ySize = 935;
            int height = ySize - 10;
            if (getHeight() > 0) height = getHeight() - 10;
            scale = (float) ((float) height / (float) ySize);
        } else if (fitType == FIT_FULL) {
            scale = 1;
        }
        final float pageScale = scale;
        try {
            decoder.setPageParameters(pageScale, currentPage);
        } catch (Exception ex) {
            System.err.println("Couldn't set scaling for JPedal");
        }
        decoderPanel.doLayout();
        decoderPanel.validate();
        scroll.doLayout();
        scroll.validate();
    }

    public void clear() {
        decoder.closePdfFile();
    }

    public void setPage(int page) {
        try {
            currentPage = page;
            decoder.decodePage(page);
        } catch (Exception e) {
            logger.error("Unable set a page on the Pdf Page Panel.", e);
            DialogMgr.showError(this, e);
            try {
                ProjectHandler.getInstance().repaintEditor();
            } catch (Exception ex) {
                logger.error("Unable to repaint the editor.", ex);
            }
        }
    }

    public int getPageCount() {
        return decoder.getPageCount();
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(btnFitWidth)) {
            fitType = FIT_WIDTH;
        } else if (evt.getSource().equals(btnFitHeight)) {
            fitType = FIT_HEIGHT;
        } else if (evt.getSource().equals(btnFitFull)) {
            fitType = FIT_FULL;
        } else if (evt.getSource().equals(btnZoomIn)) {
            fitType = FIT_ZOOM;
            scale *= 1.25;
        } else if (evt.getSource().equals(btnZoomOut)) {
            fitType = FIT_ZOOM;
            scale /= 1.25;
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                doLayout();
            }
        });
    }
}
