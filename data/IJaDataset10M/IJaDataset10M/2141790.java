package ar.uba.fi.tonyvaliente.documents;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Representa un filtro que aplica el operador AND sobre los
 * elementos recuperados por otros dos filtros cualesquiera
 * Esta implementado de modo tal que no necesite generar
 * una nueva coleccion con los elementos comunes de ambos iteradores
 * @author dmorello
 */
public class DocumentANDFilter extends DocumentBinaryOperatorFilter {

    public DocumentANDFilter(DocumentFilter filter1, DocumentFilter filter2, boolean negated, DocumentHashing docHash) {
        super(filter1, filter2, negated, docHash);
    }

    /**
	 * Crea un ResultIterator capaz de aplicar el operador AND
	 * sobre los resultados devueltos por los ResultIterators
	 * correspondientes a los filtros asociados a esta instancia
	 */
    @Override
    protected DocumentQueryResultIterator createResultIterator(int totalDocumentos, DocumentIndex index) {
        DocumentQueryResultIterator it1 = filter1.getResultIterator();
        DocumentQueryResultIterator it2 = filter2.getResultIterator();
        return new DocumentANDResultIterator(it1, it2, totalDocumentos);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;
        if (!(o instanceof DocumentANDFilter)) return false;
        return equals((DocumentANDFilter) o);
    }

    public boolean equals(DocumentANDFilter f) {
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
	 * ResultIterator capaz de aplicar el operador AND
	 * sobre los resultados devueltos por los ResultIterators
	 * correspondientes a los filtros asociados a esta instancia
	 */
    private class DocumentANDResultIterator extends DocumentQueryResultIterator {

        private DocumentQueryResultIterator it1;

        private DocumentQueryResultIterator it2;

        private Boolean hasNextValue;

        private int id1;

        private int id2;

        private int nextId;

        /**
		 * Crea una instancia de la clase que aplica el operador AND
		 * sobre los elementos devueltos por otros dos iteradores
		 * @param it1 un ResultIterator cualquiera
		 * @param it2 un ResultIterator cualquiera
		 * @param totalDocumentos total de documentos del indice invertido
		 */
        public DocumentANDResultIterator(DocumentQueryResultIterator it1, DocumentQueryResultIterator it2, int totalDocumentos) {
            super(totalDocumentos, it1.index);
            this.it1 = it1;
            this.it2 = it2;
            this.hasNextValue = null;
            id1 = -1;
            id2 = 0;
            nextId = -1;
        }

        public boolean hasNext() {
            if (hasNextValue == null) {
                hasNextValue = getHasNextValue();
            }
            return hasNextValue.booleanValue();
        }

        public Integer next() {
            if (hasNext()) {
                hasNextValue = null;
                return nextId;
            } else {
                throw new NoSuchElementException("No hay mas elementos " + "en el iterador");
            }
        }

        /**
		 * Verifica si hay mas elementos comunes entre ambos iteradores
		 * avanzando al siguiente elemento de alguno de ellos si corresponde
		 * y devuelve el valor logico que indica si hay mas elementos o no.
		 * En caso de haber mas elementos, deja el valor del siguiente
		 * seteado en el atributo nextId.
		 * @return Boolean.TRUE si hay mas elementos,
		 * Boolean.FALSE si no hay mas
		 */
        private Boolean getHasNextValue() {
            boolean loop = true;
            nextId = -1;
            while (it1.hasNext() && it2.hasNext() && loop) {
                nextId = -1;
                if (id1 < id2) id1 = it1.next();
                if (id2 < id1) id2 = it2.next();
                if (id1 == id2) {
                    nextId = id2;
                    id1--;
                    loop = false;
                }
            }
            return Boolean.valueOf(nextId != -1);
        }
    }
}
