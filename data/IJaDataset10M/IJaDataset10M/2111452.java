package edu.gsbme.wasabi.UI.Dialog.FML.Cell;

import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Element;
import edu.gsbme.MMLParser2.Factory.FMLFactory;
import edu.gsbme.MMLParser2.Vocabulary.Attributes;
import edu.gsbme.wasabi.UI.UItype;

/**
 * Tetrahedron dialog
 * @author David
 *
 */
public class TetrahedronDialog extends CellTemplateDialog {

    /**
	 * Col: Name, pt1, pt2, pt3, pt4
	 * @param shell
	 * @param element
	 * @param type
	 */
    public TetrahedronDialog(Shell shell, Element element, UItype type) {
        super(shell, 600, 400, element, type, new int[] { 100, 100, 100, 100, 100 }, new String[] { "Name", "pt1", "pt2", "pt3", "pt4" }, false);
        setTitle("Tetrahedron Dialog");
        setDescription("");
        FMLFactory fmlFactory = new FMLFactory("", returnActiveEditorModel());
        if (type == UItype.NEW) {
            Element elem = fmlFactory.getFrameWorker().insertTetrahedronTag(returnElement(), "", "", "", "");
            modTracker.insertNewModification(returnElement(), elem);
            setElement(elem);
            insertObserveElement(elem);
            setContent(new String[1][5]);
        } else {
            setContent(new String[][] { { returnElement().getAttribute(Attributes.name.toString()), returnElement().getAttribute(Attributes.pt1.toString()), returnElement().getAttribute(Attributes.pt2.toString()), returnElement().getAttribute(Attributes.pt3.toString()), returnElement().getAttribute(Attributes.pt4.toString()) } });
        }
    }

    @Override
    public boolean commit_change() {
        return true;
    }

    @Override
    public void initial_load_UI() {
    }

    @Override
    public void refresh_UI() {
    }

    @Override
    public void save_content() {
    }
}
