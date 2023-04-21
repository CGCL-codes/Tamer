package xmlutils;

import java.util.Stack;

class CountingStack<e> extends Stack<e> {

    public int count() {
        return elementCount;
    }
}
