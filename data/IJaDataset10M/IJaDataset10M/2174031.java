package org.greatlogic.gae.dao;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import java.lang.annotation.*;

public class DAOAnnotations {

    /**
* Is used to specify that a field represents a list of child DAOs.
*/
    @Target({ FIELD })
    @Retention(RUNTIME)
    public @interface ChildList {

        /**
* Identifies the foreign key that represents this relationship.
*/
        public String foreignKeyName();
    }

    /**
 * Is used to specify that a field represents a property in an entity.
 */
    @Target({ FIELD })
    @Retention(RUNTIME)
    public @interface EntityProperty {

        /**
 * Indicates whether this property is to be used for generating the key for the entity.
 */
        public boolean key() default false;

        /**
 * The type of list represented by this property (for properties that contain lists of
 * values).
 */
        public EPropertyType listPropertyType() default EPropertyType.Unknown;

        /**
 * The maximum value for a numeric property.
 */
        public long maxLongValue() default Long.MAX_VALUE;

        /**
 * The maximum length for a string/text/blob property.
 */
        public int maxLength() default 0;

        /**
 * The minimum value for a numeric property.
 */
        public long minLongValue() default 0;

        /**
 * Indicates whether this property can be empty.
 */
        public boolean nullable() default true;

        /**
 * Indicates whether this property has to be unique with the namespace and kind.
 */
        public boolean unique() default false;

        /**
 * Indicates whether this property represents the version value for the entity. There should be only
 * one version property.
 */
        public boolean version() default false;
    }

    /**
 * Is used to specify a string constant that represents a foreign key name.
 */
    @Target({ FIELD })
    @Retention(RUNTIME)
    public @interface ForeignKeyName {

        /**
 * Indicates the key that will be used in a JSON object that includes a collection using this
 * foreign key.
 */
        public String jsonCollectionKey();
    }
}
