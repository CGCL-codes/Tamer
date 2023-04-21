package dash.test.theEdge.reentrantClasses;

import dash.Component;
import dash.Obtain;

@Component
public class Child {

    @Obtain("parent_key")
    public Object str;

    @Obtain("parent_key")
    public Object str2;
}
