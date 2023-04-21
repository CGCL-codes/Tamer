package strudle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class MyTree {

    Tree tree;

    public MyTree(Composite parent, int style) {
        this.tree = new Tree(parent, style);
    }

    public MyTree(Tree tree) {
        this.tree = tree;
    }

    public void refresh(Composite w, TestBattery[] batterie) {
        Tree tree = new Tree((Composite) w, SWT.BORDER);
        for (int i = 0; i < batterie.length; i++) {
            TreeItem iItem = new TreeItem(tree, 0);
            iItem.setText(batterie[i].getNome());
            Test[] prove = batterie[i].getProve();
            for (int j = 0; j < prove.length; j++) {
                TreeItem jItem = new TreeItem(iItem, 0);
                jItem.setText(prove[j].getNome());
                Trial[] el = prove[j].getElementi();
                for (int l = 0; l < el.length; l++) {
                    TreeItem lItem = new TreeItem(jItem, 0);
                    lItem.setText(el[l].getElement());
                }
            }
        }
    }
}
