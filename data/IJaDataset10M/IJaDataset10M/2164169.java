package jbreeze4opixels.controller;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import jbreeze4opixels.layers.Resources;
import jbreeze4opixels.model.ImageListCellRenderer;
import jbreeze4opixels.model.ImagesListModel;
import jbreeze4opixels.model.LayerCombiner;
import jbreeze4opixels.view.AboutPanel;
import jbreeze4opixels.view.Gui;

public class AppController {

    private Gui gui = new Gui();

    private ImagesListModel seleionados = new ImagesListModel();

    private ImagesListModel disponiveis = new ImagesListModel();

    private LayerCombiner camadas;

    private JList dsp;

    private JList sel;

    public AppController() {
        this(false);
    }

    public AppController(final boolean isApplet) {
        disponiveis.addImages(Resources.INSTANCE.getShadowsImgs().toArray(new Image[0]));
        disponiveis.addImages(Resources.INSTANCE.getBodiesImgs().toArray(new Image[0]));
        disponiveis.addImages(Resources.INSTANCE.getClothesImgs().toArray(new Image[0]));
        disponiveis.addImages(Resources.INSTANCE.getEyesImgs().toArray(new Image[0]));
        disponiveis.addImages(Resources.INSTANCE.getHairImgs().toArray(new Image[0]));
        disponiveis.addImages(Resources.INSTANCE.getHeadImgs().toArray(new Image[0]));
        disponiveis.addImages(Resources.INSTANCE.getMiscImgs().toArray(new Image[0]));
        disponiveis.addImages(Resources.INSTANCE.getEtcImgs().toArray(new Image[0]));
        dsp = gui.getListaCamadasDisp();
        dsp.setCellRenderer(new ImageListCellRenderer());
        dsp.setModel(disponiveis);
        sel = gui.getListaCamadasSel();
        sel.setCellRenderer(new ImageListCellRenderer());
        sel.setModel(seleionados);
        camadas = gui.getResultPanel();
        dsp.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int i = dsp.getSelectedIndex();
                    if (i > -1) {
                        camadas.clear();
                        Image img = (Image) disponiveis.getElementAt(i);
                        seleionados.addImages(img);
                        updateDesenho();
                    }
                }
            }
        });
        sel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) removeSel();
            }
        });
        gui.getMntmUp().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                seleionados.up(sel.getSelectedIndex());
            }
        });
        gui.getMntmDown().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                seleionados.down(sel.getSelectedIndex());
            }
        });
        gui.getMntmRemove().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                removeSel();
            }
        });
        gui.getBtnZoomIn().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                camadas.zoomIn();
            }
        });
        gui.getBtnZoomOut().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                camadas.zoomOut();
            }
        });
        gui.getBtnReset().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                camadas.setZoom(1f);
            }
        });
        gui.getBtnNew().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                seleionados.clear();
                camadas.clear();
            }
        });
        gui.getBtnSave().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isApplet) {
                    String s = "Sadly Applet version can't save the character yet";
                    JOptionPane.showMessageDialog(gui, s);
                } else performSave();
            }
        });
        gui.getBtnAbout().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(gui, new AboutPanel());
            }
        });
    }

    private void removeSel() {
        int i = sel.getSelectedIndex();
        camadas.clear();
        seleionados.remove(i);
        updateDesenho();
    }

    private void performSave() {
        JFileChooser c = new JFileChooser();
        c.setDialogTitle("Choose a name for the exported png file");
        c.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".png");
            }

            @Override
            public String getDescription() {
                return "PNG Image files";
            }
        });
        int i = c.showSaveDialog(gui);
        if (i == JFileChooser.APPROVE_OPTION) {
            File f = c.getSelectedFile();
            try {
                ImageIO.write(camadas.exportChar(), "png", f);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void updateDesenho() {
        camadas.add(seleionados.getImages().toArray(new Image[0]));
    }

    public Gui getGui() {
        return gui;
    }
}
