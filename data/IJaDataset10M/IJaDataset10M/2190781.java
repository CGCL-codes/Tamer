package org.jpropeller.properties.list;

import org.jpropeller.map.PropMap;
import org.jpropeller.name.PropName;
import org.jpropeller.properties.Prop;

/**
 * A {@link ListProp} that is not editable
 * 
 * Note that an {@link UneditableListProp} should return an unmodifiable list from
 * {@link ListProp#get()}
 * 
 * @author bwebster
 *
 * @param <T>
 * 		The type of value contained in the prop
 */
public interface UneditableListProp<T> extends ListProp<T> {

    /**
	 * The name of the prop
	 * This is used in the {@link PropMap} to look up {@link Prop}s via {@link PropMap#get(PropName)}
	 * @return
	 * 		Name of the prop
	 */
    public PropName<? extends UneditableListProp<T>, T> getName();
}
