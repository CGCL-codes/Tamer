package sale;

/**
  * This class is a collection of constants that describe priorities used when registering validation processes
  * when deserializing MultiWindows and FormSheets.
  *
  * @author Steffen Zschaler
  * @version 2.0 17/08/1999
  * @since v2.0
  */
public final class OIV extends Object {

    /**
    * Normal priority: 0.
    */
    public static final int NORM_PRIO = 0;

    /**
    * FormSheet priority: 300.
    */
    public static final int FORMSHEET_PRIO = 300;

    /**
    * MultiWindow Handle priority: 200.
    */
    public static final int MULTIWINDOW_HANDLE_PRIO = 200;

    /**
    * MultiWindow priority: 100.
    */
    public static final int MULTIWINDOW_PRIO = 100;
}
