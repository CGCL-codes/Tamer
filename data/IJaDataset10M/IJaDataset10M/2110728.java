package de.tum.in.botl.metamodel;

/**
 * <p>�berschrift: Systementwicklungsprojekt</p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Organisation: </p>
 * @author Georgi Todorov
 * @version 1.0
 */
public interface Attribute extends Comparable {

    public String getName();

    public String getDefaultValue();

    public Type getType();

    public String getId();
}
