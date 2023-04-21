package refactortip;

/**
 * RefactorTip Class
 * @author jt
 */
public class RefactorTip {

    private String bob;

    public RefactorTip() {
        bob = "Mr.Bob";
    }

    public RefactorTip(String bob) {
        this.bob = bob;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RefactorTip rt = new RefactorTip();
        System.out.println(rt);
    }

    public String getBob() {
        return bob;
    }

    public void setBob(String bob) {
        this.bob = bob;
    }

    @Override
    public String toString() {
        return bob;
    }
}
