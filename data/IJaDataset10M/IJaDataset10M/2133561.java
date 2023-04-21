package net.sourceforge.transmogrify.symtab.parser;

public interface UpdateableObject {

    /**
   * notify the object that something else has updated it
   *
   * @param the time (a la System.currentTimeMillis()) that the
   *        update occurred
   */
    public void update(long lastUpdated);

    /**
   * whether or not this object is outofdate
   * according to whatever criteria it has set up.
   */
    public boolean isOutOfDate();
}
