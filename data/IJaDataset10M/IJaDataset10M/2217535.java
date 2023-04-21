package jp.ac.kobe_u.cs.prolog.lang;

public interface VariableTermLocation {

    final Object unboundInit = new Object() {

        @Override
        public String toString() {
            return "<?NULL-unboundInit?>";
        }
    };

    Object getVal();

    void setVal(Object o);

    boolean isBound();
}
