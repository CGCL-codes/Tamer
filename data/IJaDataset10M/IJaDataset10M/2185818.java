package de.tum.in.botl.metamodel;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/**
 * <p>�berschrift: Systementwicklungsprojekt</p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Organisation: </p>
 * @author Georgi Todorov
 * @version 1.0
 */
public interface BotlClass extends Comparable {

    public String getName();

    public Metamodel getMetamodel();

    public SortedSet getPrimaryKeys();

    public List getAttributes();

    public List getAllAttributes();

    public Attribute getAttribute(String name);

    public boolean hasPKs();

    public SortedSet getAllPrimaryKeys();

    public String getId();

    public List getSuperClasses();

    public List getAllSuperClasses();

    public Set getAllSubClasses();
}
