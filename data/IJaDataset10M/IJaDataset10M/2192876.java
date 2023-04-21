package grafica.componenti.contenitori.contenitoreBase;

import java.awt.Container;
import javax.swing.JDialog;

public class ContainerBaseDialogo extends ContainerBase {

    private static final long serialVersionUID = 1L;

    @Override
    public int getMaxDimensionX(final Container container) {
        JDialog dialog = (JDialog) container;
        Container cont = dialog.getContentPane();
        return super.getMaxDimensionX(cont);
    }

    @Override
    public int getMaxDimensionY(final Container container) {
        JDialog dialog = (JDialog) container;
        Container cont = dialog.getContentPane();
        return super.getMaxDimensionY(cont) + 25;
    }
}
