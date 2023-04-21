package core.view;

import java.util.Observable;
import core.View;
import core.var.ViewVarRouter;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;

/**
 * Tree show a tree
 * 
 * @since 23/10/2008
 * @version 1.0
 * @author gael
 */
public class Tree extends JTree implements JaxilComponent {

    private static final long serialVersionUID = -8668117003437421211L;

    private static final String FACTORY = "core.view.component.TreeFactory";

    private JPopupMenu popup;

    public Tree() {
        super(new Object[] {});
        setName("core.view.Tree");
    }

    public void addView(View v, String path) {
    }

    public Object getVar(String s) {
        return ViewVarRouter.getInstance().getVar(getComponents(), s);
    }

    public void removeView(String path) {
    }

    public void setComponentPopupMenu(JPopupMenu popup) {
        this.popup = popup;
    }

    public void update(Observable o, Object arg) {
        if (arg instanceof TreeModel) {
            setRootVisible(true);
            setModel((TreeModel) arg);
            validate();
            getParent().getParent().getParent().validate();
        }
    }

    @Override
    public String toString() {
        return getName();
    }

    public JPopupMenu getPopupMenu() {
        return popup;
    }

    @Override
    public String getComponentFactory() {
        return FACTORY;
    }
}
