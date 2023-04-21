package jdbm.helper.maps;

/**
 *  Thrown to indicate that an attempt was made to retrieve a 
 *  non-existing mapping in a map.
 *
 *  @author     S&oslash;ren Bak
 *  @version    1.0     2002/30/12
 *  @since      1.0
 *  
 *  ********* JDBM Project Note *************
 *  This class was extracted from the pcj project (with permission)
 *  for use in jdbm only.  Modifications to original were performed
 *  by Kevin Day to make it work outside of the pcj class structure. 
 *  
 */
public class NoSuchMappingException extends RuntimeException {

    private static final long serialVersionUID = 1034866275435973951L;

    /**
     *  Creates a new exception with a specified detail message.
     *  The message indicates the key of the mapping that was
     *  not available.
     *
     *  @param      s
     *              the detail message.
     *
     *  @throws     NullPointerException
     *              if <tt>s</tt> is <tt>null</tt>.
     */
    public NoSuchMappingException(String s) {
        super(s);
    }
}
