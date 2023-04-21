package ftraq.settings;

/**
  * A class for storing operating system identifiers.
  *
  * An operating system is identified by any number of system strings.
  * A system string is a combination of more or less unique information for
  * the current operating system.
  *
  * @author <A HREF="mailto:moeller@scireview.de">Erik Moeller</A>
  * @version 1.0
  */
public interface LgSystemID {

    /**
      * Returns the name of the operating system.
      * @return the name of the operating system
      */
    String getName();

    /**
      * Defines the name of the operating system.
      * @param i_name the name of the operating system
      */
    void setName(String i_name);

    /**
      * Returns a list of all system strings identifying this operating
      * system.
      * @return the list of system strings for this operating system
      */
    ftraq.util.String_List getSystemStringList();

    /**
      * Returns a string version of the operating system ID.
      * @return the operating system as String.
      */
    String toString();
}
