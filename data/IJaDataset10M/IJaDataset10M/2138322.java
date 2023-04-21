package navigators.smart.reconfiguration;

import navigators.smart.reconfiguration.views.View;

/**
 *
 * @author eduardo
 */
public class ReconfigurationTest {

    public ReconfigurationTest() {
    }

    public void run(int id) {
        Reconfiguration rec = new Reconfiguration(id);
        rec.setF(2);
        ReconfigureReply r = rec.execute();
        View v = r.getView();
        System.out.println("New view f: " + v.getF());
        rec.close();
    }

    public static void main(String[] args) {
        new ReconfigurationTest().run(Integer.parseInt(args[0]));
    }
}
