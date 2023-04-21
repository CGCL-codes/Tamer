package au.gov.nla.aons.rest.domain;

import java.util.ArrayList;
import java.util.Collection;

public class RegistriesArrayList<E> extends ArrayList<E> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4835032111074987302L;

    public RegistriesArrayList() {
        super();
    }

    public RegistriesArrayList(Collection<E> c) {
        super(c);
    }

    public RegistriesArrayList(int initialCapacity) {
        super(initialCapacity);
    }
}
