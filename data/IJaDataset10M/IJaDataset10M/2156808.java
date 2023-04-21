package org.easy.eao.spring.hibernate;

import java.util.Collection;
import org.easy.eao.annotations.Persist;

public interface PersistGenericEao<T1, T2> {

    @Persist
    void generic_single_entity(T1 person);

    @Persist
    void generic_multi_entity(T1 person, T2 exam);

    @Persist
    void generic_variable(T1... persons);

    @Persist
    void generic_array(T2[] array);

    @Persist
    void generic_collection(Collection<T1> persons);
}
