package fr.amille.animebrowser.view.buttons;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.List;
import java.util.TooManyListenersException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import fr.amille.animebrowser.model.serie.Serie;
import fr.amille.animebrowser.model.serie.listener.SerieListener;
import fr.amille.animebrowser.model.util.ViewColorConstant;
import fr.amille.animebrowser.model.util.ViewSizeConstant;
import fr.amille.animebrowser.view.AnimeBrowserCommonView;

/**
 * 
 * @author amille
 * 
 */
@SuppressWarnings("serial")
public class SerieButton extends JButton implements SerieListener, MouseListener, AnimeBrowserCommonView, DropTargetListener {

    private Color selectedColor = ViewColorConstant.VERY_DARK_GRAY;

    private Color normalColor = ViewColorConstant.GRAY;

    private boolean selected = false;

    private Serie serie;

    public SerieButton(final Serie serie) {
        super(serie.getFirstSerieTitle());
        this.serie = serie;
        this.setVerticalTextPosition(SwingConstants.BOTTOM);
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.setContentAreaFilled(true);
        this.setFocusPainted(false);
        final Dimension dimension = new Dimension(ViewSizeConstant.SERIE_BUTTON_W, ViewSizeConstant.SERIE_BUTTON_H);
        this.setPreferredSize(dimension);
        this.setMinimumSize(dimension);
        this.setMaximumSize(dimension);
        this.setImageIcon(this.serie.getSeriePicture());
        this.setBackground(this.normalColor);
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        this.setForeground(ViewColorConstant.BLACK);
        this.addMouseListener(this);
        this.serie.addSerieListener(this);
        this.setContentAreaFilled(false);
        final DropTarget serieButtonDropTarget = new DropTarget();
        this.setDropTarget(serieButtonDropTarget);
        try {
            this.getDropTarget().addDropTargetListener(this);
        } catch (final TooManyListenersException e) {
            e.printStackTrace();
        }
    }

    private void changeListeNomsSerie(final Serie serie) {
        this.setText(serie.getFirstSerieTitle());
    }

    @Override
    public void dragEnter(final DropTargetDragEvent dtde) {
    }

    @Override
    public void dragExit(final DropTargetEvent dte) {
    }

    @Override
    public void dragOver(final DropTargetDragEvent dtde) {
    }

    @Override
    public void drop(final DropTargetDropEvent dropTargetDropEvent) {
        final Transferable transferable = dropTargetDropEvent.getTransferable();
        if (transferable != null) {
            if (dropTargetDropEvent.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                try {
                    dropTargetDropEvent.acceptDrop(dropTargetDropEvent.getDropAction());
                    @SuppressWarnings("unchecked") final List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    if ((files != null)) {
                        for (final File file : files) {
                            this.dropFile(file);
                        }
                    }
                    dropTargetDropEvent.dropComplete(true);
                } catch (final java.io.IOException e) {
                    e.printStackTrace();
                } catch (final UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (final ClassCastException e) {
                    e.printStackTrace();
                }
            } else if (dropTargetDropEvent.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            } else {
                dropTargetDropEvent.rejectDrop();
            }
        }
    }

    @Override
    public void dropActionChanged(final DropTargetDragEvent dtde) {
    }

    private void dropFile(final File file) {
        this.getSerie().setSeriePicturePath(file.getAbsolutePath());
    }

    public Color getNormalColor() {
        return this.normalColor;
    }

    public Color getSelectedColor() {
        return this.selectedColor;
    }

    public Serie getSerie() {
        return this.serie;
    }

    @Override
    public boolean isSelected() {
        return this.selected;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        if (!this.isSelected()) {
            this.setOpaque(true);
            this.setForeground(ViewColorConstant.WHITE);
        }
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        if (!this.isSelected()) {
            this.setOpaque(false);
            this.setForeground(ViewColorConstant.BLACK);
        }
    }

    @Override
    public void mousePressed(final MouseEvent e) {
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
    }

    @Override
    public void prepareForClean() {
        this.removeMouseListener(this);
        this.serie.removeSerieListener(this);
        this.selectedColor = null;
        this.normalColor = null;
        this.serie = null;
    }

    @Override
    public void serieChange() {
        this.changeListeNomsSerie(this.serie);
        this.setImageIcon(this.serie.getSeriePicture());
        if (this.getRootPane() != null) {
            this.getRootPane().repaint();
            this.getRootPane().revalidate();
        }
    }

    private void setImageIcon(final ImageIcon imageIcon) {
        if (imageIcon != null) {
            this.setIcon(imageIcon);
        }
    }

    public void setNormalColor(final Color normalColor) {
        this.normalColor = normalColor;
    }

    @Override
    public void setSelected(final boolean selected) {
        this.selected = selected;
        if (!this.isSelected()) {
            this.setBackground(this.normalColor);
            this.setOpaque(false);
            this.setForeground(ViewColorConstant.BLACK);
        } else {
            this.setBackground(this.selectedColor);
            this.setOpaque(true);
            this.setForeground(ViewColorConstant.WHITE);
        }
    }

    public void setSelectedColor(final Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public void setSerie(final Serie serie) {
        if (serie != this.serie) {
            if (this.serie != null) {
                this.serie.removeSerieListener(this);
            }
            if (serie != null) {
                serie.addSerieListener(this);
            }
            this.serie = serie;
            this.serieChange();
        }
    }
}
