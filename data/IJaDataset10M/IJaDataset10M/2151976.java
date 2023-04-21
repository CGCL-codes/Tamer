package org.gnu.readline;

/**
 This class implements a typesafe enumeration of the backing libraries.

 @version $Revision: 1.4 $
 @author  $Author: bablokb $
*/
public final class ReadlineLibrary {

    /**
     Constant for fallback, pure Java implementation.
  */
    public static final ReadlineLibrary PureJava = new ReadlineLibrary("PureJava");

    /**
     Constant for GNU-Readline implementation.
  */
    public static final ReadlineLibrary GnuReadline = new ReadlineLibrary("JavaReadline");

    /**
     Constant for Editline implementation.
  */
    public static final ReadlineLibrary Editline = new ReadlineLibrary("JavaEditline");

    /**
     Constant for Getline implementation.
  */
    public static final ReadlineLibrary Getline = new ReadlineLibrary("JavaGetline");

    /**
     The name of the backing native library.
  */
    private String iName;

    /**
     Constructor. The constructor is private, so only the predefined
     constants are available.
  */
    private ReadlineLibrary(String name) {
        iName = name;
    }

    /**
     Query name of backing library.

     @return Name of backing library, or "PureJava", in case fallback
     implementation is used.
  */
    public String getName() {
        return iName;
    }

    /**
     Return ReadlineLibrary-object with given name.
     
     @return one of the predefined constants
  */
    public static ReadlineLibrary byName(String name) {
        if (name.equals("GnuReadline")) return GnuReadline; else if (name.equals("Editline")) return Editline; else if (name.equals("Getline")) return Getline; else if (name.equals("PureJava")) return PureJava;
        return null;
    }
}
