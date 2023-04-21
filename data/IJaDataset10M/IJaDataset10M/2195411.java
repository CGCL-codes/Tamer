package presentationtier.titles;

import businesstier.usecaserealisation.spravatitulu.AbstractVyhledatTitulModelHolder;
import businesstier.usecaserealisation.spravatitulu.IVyhledatTitulUCC;
import businesstier.usecaserealisation.spravatitulu.VyhledatTitulUCC;
import java.awt.event.ActionEvent;
import presentationtier.FormAbstractAction;

/**
 *
 * @author lucky
 */
public class FindTitleAction extends FormAbstractAction {

    private FindTitleForm frame;

    private IVyhledatTitulUCC ucc;

    public FindTitleAction(FindTitleForm frame, AbstractVyhledatTitulModelHolder model) {
        super("Najdi Titul");
        this.frame = frame;
        this.ucc = new VyhledatTitulUCC(model);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String idString = this.frame.getTitleID();
        int id;
        try {
            id = Integer.parseInt(idString);
            this.ucc.vyhledatTitulPodleId(id);
        } catch (NumberFormatException ex) {
            System.err.println("Chybne zadane ID");
        }
    }
}
