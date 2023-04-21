package net.jadoth.experimental.aspectorientation.test;

import net.jadoth.experimental.aspectorientation.MethodCall;

/**
 * @author Thomas M�nz
 *
 */
public class MyClass {

    public StringBuilder doStuff(final int i, final float f, final String s) throws NullPointerException {
        return MyAspects.getMethodExecutionContext(this).executeMethod(new doStuff(i, f, s));
    }

    protected class doStuff extends MethodCall<StringBuilder> {

        protected final int i;

        protected final float f;

        protected final String s;

        protected doStuff(final int i, final float f, final String s) {
            this.i = i;
            this.f = f;
            this.s = s;
        }

        @Override
        public StringBuilder execute() throws NullPointerException {
            if (s == null) {
                throw new NullPointerException("s may not be null");
            }
            final StringBuilder sb = new StringBuilder(1024);
            sb.append("i is " + i + ", f is " + f + ", s is \"" + s + "\"");
            return sb;
        }
    }
}
