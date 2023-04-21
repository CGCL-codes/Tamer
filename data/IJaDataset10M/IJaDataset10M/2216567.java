package org.ucdetector.example.bugs;

/**
 * Bug 2779970   Compile error for: static variable
 * <p>
 * Submitted: Nobody/Anonymous ( nobody ) - 2009-04-24 00:59
 * <p>
 * It also suggests changing a variable in a non-static class used by a
 * static inner class from protected to private. This gives compiler error - 
 * non-static variable ... cannot be referenced from a static context.
 * <p>
 * @see https://sourceforge.net/tracker/?func=detail&aid=2779970&group_id=219599&atid=1046865
 * <p>
 * See also bug: [ 2804064 ] Access to enclosing type - make 2743908 configurable
 * <p>
 * Browse code at:
 * http://ucdetector.svn.sourceforge.net/viewvc/ucdetector/trunk/org.ucdetector.example/example/org/ucdetector/example/bugs/Bug2779970.java?revision=964&view=markup
 */
public class Bug2779970 {

    protected final int dontMakeMePrivate = 1;

    public static void main(String[] args) {
        System.out.println(new Bug2779970().dontMakeMePrivate);
        new StaticClass().foo();
    }

    static class StaticClass {

        protected void foo() {
            System.out.println(new Bug2779970().dontMakeMePrivate);
        }
    }
}
