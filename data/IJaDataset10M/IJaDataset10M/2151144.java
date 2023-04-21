package tudolist;

import org.jgenesis.bd.PersistentBD;
import org.jgenesis.beanset.BeanManager;
import tudolist.bean.User;

public class TudoList {

    private static TudoList instace = new TudoList();

    private BeanManager beanManagerUser;

    private TudoList() {
        beanManagerUser = new BeanManager(User.class);
    }

    public static TudoList getInstace() {
        return instace;
    }

    public BeanManager getUserManager() {
        return beanManagerUser;
    }

    public BeanManager getTaskManager() {
        return null;
    }

    public BeanManager getTaskListManager() {
        return null;
    }

    public static void main(String args[]) {
        User user = new User();
        user.setAdministrator(true);
        user.setName("Lucas");
        user.setPassword("1234");
        PersistentBD.getInstance().save(user);
    }
}
