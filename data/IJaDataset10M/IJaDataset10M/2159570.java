package org.apache.bcel.util;

import java.util.Stack;
import org.apache.bcel.classfile.JavaClass;

/** 
 * Utility class implementing a (typesafe) stack of JavaClass objects.
 *
 * @version $Id: ClassStack.java 386056 2006-03-15 11:31:56Z tcurdt $
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A> 
 * @see Stack
 */
public class ClassStack implements java.io.Serializable {

    private Stack stack = new Stack();

    public void push(JavaClass clazz) {
        stack.push(clazz);
    }

    public JavaClass pop() {
        return (JavaClass) stack.pop();
    }

    public JavaClass top() {
        return (JavaClass) stack.peek();
    }

    public boolean empty() {
        return stack.empty();
    }
}
