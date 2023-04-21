package mips;

import frame.Access;

public class AccessList extends frame.AccessList {

    public Access head;

    public AccessList tail;

    public AccessList(Access h, AccessList t) {
        head = h;
        tail = t;
    }
}
