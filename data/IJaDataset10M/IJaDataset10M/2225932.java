package org.nakedobjects.reflector.original.control;

/**
 * Details about a class's members.
 */
public interface InfoAccessor {

    String getDescription();

    String getHelp();

    String getName();

    boolean isAuthorised();
}
