package com.sun.ebxml.registry.query.filter;

/**
 * Class Declaration for ClauseType
 * @see
 * @author Nikola Stojanovic
 */
public class ClauseType {

    boolean isReverseSelectNeeded = false;

    boolean isSubSelectNeeded = false;

    String parentJoinColumn = "id";

    String clause = null;
}
