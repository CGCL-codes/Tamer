package fitlibrary.specify.select;

import fitlibrary.traverse.workflow.DoTraverse;

public class FirstSelectTraverse extends DoTraverse {

    public int count() {
        return 1;
    }

    public SecondSelect second() {
        return new SecondSelect();
    }
}
