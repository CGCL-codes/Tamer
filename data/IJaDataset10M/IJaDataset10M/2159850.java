package CommandesAlpha;

import editeur.EditeurAlpha;
import Commande.Commande;
import IHM.IHM_addon_Alpha;

public class new_macro implements Commande {

    private EditeurAlpha edit;

    private IHM_addon_Alpha ihm;

    public new_macro(EditeurAlpha ed, IHM_addon_Alpha i) {
        edit = ed;
        ihm = i;
    }

    public void execute() {
        ihm.debog("Commandes.new_macro");
        String macro = ihm.getMacroName();
        if (macro != "") {
            ihm.debog("Nouvelle macro: " + macro);
            System.out.println("# Warning insertions are statics. #");
            edit.newMacro(macro);
            edit.openMacro(macro);
        }
    }

    public String toString() {
        return "new_macro";
    }
}
