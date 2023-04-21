package darwevo.ui.species;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import darwevo.ui.gfx.SharedUiGfx;
import darwevo.ui.states.SelectedSpecies;
import darwevo.util.files.ExtensionFilter;

@SuppressWarnings("serial")
public final class SpeciesIconLabel extends JLabel implements MouseListener, Observer {

    private final SharedUiGfx uiGfx;

    private final SelectedSpecies selSpecies;

    private final String iconType;

    private final JFileChooser fileChooser;

    private static final String[] imageFileExtensions = { "tiff", "tif", "gif", "jpg", "jpeg", "png", "bmp" };

    private static final ExtensionFilter imageFileFilter = new ExtensionFilter(imageFileExtensions, "Image Files");

    public SpeciesIconLabel(final String tooltip, final String iconType, final SharedUiGfx gfx, final SelectedSpecies sel, final JFileChooser fc) {
        super(gfx.getNoSelectionIcon());
        uiGfx = gfx;
        selSpecies = sel;
        this.iconType = iconType;
        fileChooser = fc;
        setToolTipText(tooltip);
        addMouseListener(this);
        selSpecies.addObserver(this);
    }

    @Override
    public void mouseClicked(final MouseEvent ev) {
        if (selSpecies.getValue() == null) {
            return;
        }
        fileChooser.resetChoosableFileFilters();
        fileChooser.setFileFilter(imageFileFilter);
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
    }

    @Override
    public void mouseEntered(final MouseEvent ev) {
    }

    @Override
    public void mouseExited(final MouseEvent ev) {
    }

    @Override
    public void mousePressed(final MouseEvent ev) {
    }

    @Override
    public void mouseReleased(final MouseEvent ev) {
    }

    @Override
    public void update(final Observable o, final Object arg) {
        if (arg == null) {
            setIcon(uiGfx.getNoSelectionIcon());
        }
        final String iconName = iconType + ";" + (String) arg;
        if (uiGfx.getSpeciesIcon(iconName) == null) {
            setIcon(uiGfx.getNoSelectionIcon());
        } else {
            setIcon(new ImageIcon(uiGfx.getSpeciesIcon(iconName).getImage()));
        }
    }
}
