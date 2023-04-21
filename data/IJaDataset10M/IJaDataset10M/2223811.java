package alice.tuprologx.pj.test.pjlibrary;

import alice.tuprologx.pj.model.*;
import alice.tuprologx.pj.annotations.*;
import alice.tuprologx.pj.engine.*;

/**
 *
 * @author maurizio
 */
@PrologClass
public abstract class TestLibrary {

    public int count = 45;

    @TRACE
    @PrologMethod(clauses = { "new_list(I):-new_object('java.util.ArrayList', [], L), L <- add(one), L <- add(two), L <- add(three), L <- iterator returns I." })
    public abstract <$L extends JavaObject<Term<?>>> $L new_list();

    @TRACE
    @PrologMethod(clauses = { "prova(Iterable, Element) :-Iterable <- hasNext, prova2(Iterable, Element).", "prova2(Iterable, Element) :- Iterable <- next returns Element.", "prova2(Iterable, Element) :- Iterable <- hasNext, prova2(Iterable, Element). ", "test(E, I) :- prova(I, E)." })
    public abstract <$X extends Term<?>, $L extends JavaObject<Term<?>>> Iterable<$X> test($L l);

    public static void main(String[] args) throws Exception {
        TestLibrary jl = PJ.newInstance(TestLibrary.class);
        JavaObject<Term<?>> ob = jl.new_list();
        System.out.println(ob);
        for (Term<?> t : jl.test(ob)) {
            System.out.println(t);
        }
    }
}
