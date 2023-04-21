package org.datanucleus.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Person {

    @PrimaryKey
    long id;
}
