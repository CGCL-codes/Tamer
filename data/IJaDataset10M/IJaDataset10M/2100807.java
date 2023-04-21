package HomeTask;

import java.util.Stack;

public class StackTest {

    public static void main(String[] args) {
        Stack<String> stack = new Stack<String>();
        for (String s : "Hello Java World !".split(" ")) {
            stack.push(s);
            System.out.print(s + " ");
        }
        System.out.println();
        while (!stack.empty()) System.out.print(stack.pop() + " ");
    }
}
