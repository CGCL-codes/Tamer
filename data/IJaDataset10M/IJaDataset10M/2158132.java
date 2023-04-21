package com.fuckingbrit.q3astats;

/**
  * The hit class is provided to track the number of hits on a given location.
  *<p>
  *<b><i>Note:</i></b> A hit location always starts at 1. If you MUST add all
  * locations to a LogOwner, then you'll have to manualy set them to 0 when you
  * batch add them.
  * @author Michael Jervis (mike@fuckingbrit.com)
  * @version 1.0
  */
public class Hit {

    /** The location hit. */
    public String location;

    /** The count of hits on that location. */
    public int hits = 1;

    /**
      * @param where Location of Hit.
      */
    public Hit(String where) {
        location = where;
    }
}
