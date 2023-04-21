package net.sf.doolin.gui.model;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import net.sf.doolin.util.factory.DataFactory;
import net.sf.jstring.LocalizableException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Returns the value model for a list of enumeration values.
 * 
 * @author Damien Coraboeuf
 * 
 * @param <E>
 *            Type of enumeration
 */
public class EnumListDataFactory<E extends Enum<E>> implements DataFactory<EventList<E>> {

    private final Class<E> enumClass;

    private EventList<E> eventList;

    /**
	 * Constructor
	 * 
	 * @param enumClass
	 *            Enumeration class
	 */
    public EnumListDataFactory(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public EventList<E> create(Object context) {
        if (this.eventList != null) {
            return this.eventList;
        }
        try {
            Method valuesMethod = this.enumClass.getMethod("values");
            @SuppressWarnings("unchecked") E[] values = (E[]) valuesMethod.invoke(null);
            this.eventList = GlazedLists.eventList(Arrays.asList(values));
            return this.eventList;
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new LocalizableException("EnumModelFactory.CannotGetEnumValues", this.enumClass.getName(), ex);
        }
    }
}
