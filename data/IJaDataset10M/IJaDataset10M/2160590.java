package com.bluebrim.base.shared;

/**
 	Utvidgning av CoObjectIF f�r de verksamhetsobjekt som instansieras via en factoryklass. 
 	Definierar metoden #getFactoryKey som skall svara med en str�ng som �r den 
 	nyckel som anv�nds av factoryklassen.
 */
public interface CoFactoryElementIF {

    public String getFactoryKey();
}
