package vilaug.peirce;

import jung.ext.elements.ElementDialog;
import javax.swing.JPanel;

public interface PeirceElementDialog extends ElementDialog {

    public void relationInvalidWarning();

    public boolean specifyRelation();

    public JPanel hintPanel();
}
