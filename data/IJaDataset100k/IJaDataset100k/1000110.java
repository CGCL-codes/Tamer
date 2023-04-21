package movepackage.sub2;

import movepackage.sub1.MovedClass;

public class UserClass {

    void m(Object o) {
        Object tmp = ((MovedClass) o).getSelf();
    }
}
