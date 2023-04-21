package frege.rt;

/**
 * <p> Frege lambdas with arity 10. </p>
 *
 * <p> See {@link Lam1} for a general discussion of untyped function values. </p>
 *
 */
public abstract class Lam10 extends Lambda {

    /**
     * <p>Apply this function to an argument.</p>
     *
     * <p> This method creates an instance of {@link Lam9} that collects the
     * remaining arguments and, when evaluated, invokes the {@link Lam10#eval} method of this
     * class.</p>
     *
     * @return an instance of type <tt>Lam9</tt> that waits for the
     * remaining arguments to be supplied and calls back with all arguments.
     */
    public final Lam9 apply(final Lazy<FV> arg1) {
        return new Lam9() {

            public final Lazy<FV> eval(final Lazy<FV> arg10, final Lazy<FV> arg9, final Lazy<FV> arg8, final Lazy<FV> arg7, final Lazy<FV> arg6, final Lazy<FV> arg5, final Lazy<FV> arg4, final Lazy<FV> arg3, final Lazy<FV> arg2) {
                return Lam10.this.eval(arg10, arg9, arg8, arg7, arg6, arg5, arg4, arg3, arg2, arg1);
            }
        };
    }

    /**
     * <p>Apply this function to all its arguments at once.</p>
     *
     * <p> This method creates an instance of {@link Unknown} that,
     * when evaluated, invokes the {@link Lam10#eval} method of this
     * function.</p>
     *
     * Use of this method is preferrable if all arguments are known compared
     * to repeated invokation of the single argument form since intermediate
     * closure creation is saved.
     *
     * @return an instance of type <tt>Unknown&lt;FV&gt;</tt>
     */
    public final Unknown<FV> apply(final Lazy<FV> arg1, final Lazy<FV> arg2, final Lazy<FV> arg3, final Lazy<FV> arg4, final Lazy<FV> arg5, final Lazy<FV> arg6, final Lazy<FV> arg7, final Lazy<FV> arg8, final Lazy<FV> arg9, final Lazy<FV> arg10) {
        return new Unknown<FV>() {

            public final Lazy<FV> _v() {
                return Lam10.this.eval(arg10, arg9, arg8, arg7, arg6, arg5, arg4, arg3, arg2, arg1);
            }
        };
    }

    /**
     * <p> Run the function. </p>
     *
     * <p> The run method will be called by the {@link Lam9#eval} method
     * of the lambda object resulting from <tt>this.apply(...)</tt>.
     * It actually performs computation and
     * returns a result or another lazy value that will evaluate to the result.<br>
     * This method must be implemented by all subclasses.</p>
     *
     * <p>
     * Note that the arguments must be passed in reverse order. The reason is that
     * in this way the byte code for any intermediate closure will only have to
     * push its argument and invoke the next higher closure's <tt>eval</tt> method.
     * A reordering of the arguments on the stack will not be needed. This could save
     * a substantial amounts of memory writes (I hope).
     * </p>
     *
     *
     * @return boxed and possibly lazy result
     */
    public abstract Lazy<FV> eval(final Lazy<FV> arg10, final Lazy<FV> arg9, final Lazy<FV> arg8, final Lazy<FV> arg7, final Lazy<FV> arg6, final Lazy<FV> arg5, final Lazy<FV> arg4, final Lazy<FV> arg3, final Lazy<FV> arg2, Lazy<FV> arg1);
}
