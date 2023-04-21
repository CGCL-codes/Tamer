package fluid.java.util;

import java.util.Stack;
import fluid.ir.IRNode;
import fluid.java.JavaGlobals;
import fluid.java.JavaPromise;

public class TreeUtil implements JavaGlobals {

    public static Stack findPathUp(IRNode root, IRNode here) {
        Stack stack = new Stack();
        IRNode parent = JavaPromise.getParentOrPromisedFor(here);
        while (parent != null) {
            if (root == parent) {
                return stack;
            }
            stack.push(parent);
            here = parent;
            parent = JavaPromise.getParentOrPromisedFor(here);
        }
        return null;
    }
}
