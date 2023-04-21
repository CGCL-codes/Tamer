package basic.buy;

/**
 *
 * @author Admin
 */
public class OffersBean {

    private TreeController tree = null;

    public OffersBean() {
        System.out.println("constructor!!!");
        tree = new TreeController();
    }

    public TreeController getTree() {
        return tree;
    }

    public void setTree(TreeController tree) {
        this.tree = tree;
    }
}
