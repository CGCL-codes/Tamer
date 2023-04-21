package redora.client;

import redora.client.util.Field;
import java.util.HashMap;

/**
 * Light weight pojo to use in GWT client.
 * @author Nanjing RedOrange (www.red-orange.cn)
 */
public interface Persistable {

    /**
     * How the data of the object has been retrieved.
     */
    enum Scope {

        /**
         * Usually all the records. The information is displayed in a single record
         * layout.
         */
        Form, /**
         * When records are shown in a table view, usually 5 to 10 attributes per
         * record line.
         */
        Table, /**
         * When displayed in a drop down list. You want to show one or two fields.
         * When a reference object is displayed, display only the List scoped
         * attributes.
         */
        List, /**
         * This scope will retrieve the missing attributes (those that are lazy and
         * not set type.
         */
        Lazy
    }

    /** @return The id. */
    Long getId();

    /** @return The scope. */
    Scope scope();

    /** @return True when a field has changed. */
    HashMap<Field, Object> dirty();

    /** @return The list scope fields as string. */
    String toListString();
}
