package edu.ucla.stat.SOCR.touchgraph.graphlayout;

/** <p>An class for exceptions thrown during TouchGraph processing. </p>
  *
  * @author   Alexander Shapiro
  * @author   Murray Altheim
  * @version  1.22-jre1.1  $Id: TGException.java,v 1.1 2010/01/20 20:38:32 jiecui Exp $
  */
public class TGException extends Exception {

    /** An exception occurring when a Node already exists. */
    public static final int NODE_EXISTS = 1;

    /** An exception occurring when a Node doesn't exist. */
    public static final int NODE_DOESNT_EXIST = 2;

    /** An exception occurring when a Node is missing its required ID. */
    public static final int NODE_NO_ID = 3;

    /** An int containing an exception type identifier (ID). */
    protected int id = -1;

    /** The embedded Exception if tunnelling.
      * @serial The embedded exception if tunnelling, or null.
      */
    public Exception exception = null;

    /** Constructor for TGException with Exception ID.
      *
      * @param id The unique message identifier.
      */
    public TGException(int id) {
        super();
        this.id = id;
    }

    /** Constructor for TGException with Exception ID and error message String.
      *
      * @param id The unique message identifier.
      * @param message The Exception message.
      */
    public TGException(int id, String message) {
        super(message);
        this.id = id;
    }

    /** Constructor for TGException with an error message String.
      *
      * @param message The Exception message.
      */
    public TGException(String message) {
        super(message);
    }

    /** Constructor for TGException tunnelling the original Exception.
      *
      * @param exception The original Exception.
      */
    public TGException(Exception exception) {
        super();
        this.exception = exception;
    }

    /** If the message was expressed as a MessageId, return the original
      * id (e.g. "45"). 
      *
      * @return the exception identifier.
      */
    public int getId() {
        return id;
    }
}
