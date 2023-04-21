package utility;

public class Fciconv {

    public static final String FC_DEFAULT_DATA_ENCODING = "UTF-8";

    static boolean is_init = false;

    /***************************************************************************
  Must be called during the initialization phase of server and client to
  initialize the character encodings to be used.

  Pass an internal encoding of null to use the local encoding internally.
***************************************************************************/
    public static void init_character_encodings(String my_internal_encoding, boolean my_use_transliteration) {
        is_init = true;
    }
}
