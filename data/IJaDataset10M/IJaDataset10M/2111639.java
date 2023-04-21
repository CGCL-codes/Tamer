package ar.uba.fi.tonyvaliente.signature.query;

/**
 * Implementacion que aplica el operador logico OR
 * a los resultados de los filtros asociados.
 * @author dmorello
 */
final class SignatureANDFilter extends SignatureBinaryOperatorFilter {

    /**
	 * Crea una instancia que actua sobre dos filtros.
	 * @param filter1 un SignatureFilter cualquiera
	 * @param filter2 un SignatureFilter cualquiera
	 * @param negated flag de negacion
	 */
    public SignatureANDFilter(SignatureFilter filter1, SignatureFilter filter2, boolean negated) {
        super(filter1, filter2, negated);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;
        if (!(o instanceof SignatureANDFilter)) return false;
        return equals((SignatureANDFilter) o);
    }

    public boolean equals(SignatureANDFilter f) {
        return this.isNegated() == f.isNegated() && this.filter1.equals(f.filter1) && this.filter2.equals(f.filter2);
    }

    @Override
    public int hashCode() {
        return (filter1.hashCode() + "AND" + filter2.hashCode() + isNegated()).hashCode();
    }

    @Override
    public String toString() {
        String s = "(" + filter1.toString() + " AND " + filter2.toString() + ")";
        if (isNegated()) {
            return "NOT " + s;
        } else {
            return s;
        }
    }

    /**
	 * Implementacion del metodo de la superclase
	 * que aplica el operador AND a los dos operandos
	 */
    @Override
    protected boolean applyOperator(boolean eval1, boolean eval2) {
        return eval1 && eval2;
    }
}
