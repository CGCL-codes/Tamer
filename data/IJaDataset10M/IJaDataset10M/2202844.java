package jpatch.control.edit;

import javax.swing.tree.*;
import jpatch.entity.*;
import jpatch.boundary.sidebar.*;
import jpatch.boundary.*;

public class AtomicRemoveMaterial extends JPatchAtomicEdit implements JPatchRootEdit {

    private OLDMaterial material;

    public AtomicRemoveMaterial(OLDMaterial material) {
        this.material = material;
        redo();
    }

    public String getPresentationName() {
        return "remove material";
    }

    public void redo() {
        MainFrame.getInstance().getModel().removeMaterial(material);
        MainFrame.getInstance().getSideBar().replacePanel(new SidePanel());
        MainFrame.getInstance().getSideBar().clearDetailPanel();
        MainFrame.getInstance().getSideBar().validate();
    }

    public void undo() {
        MainFrame.getInstance().getModel().addMaterial(material);
        MainFrame.getInstance().getSideBar().replacePanel(new SidePanel());
        MainFrame.getInstance().getSideBar().clearDetailPanel();
        MainFrame.getInstance().getSideBar().validate();
    }

    public int sizeOf() {
        return 8 + 4;
    }

    public String getName() {
        return "remove material";
    }
}
