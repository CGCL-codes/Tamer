package psychomad.lcvsica.sica;

/** 
 * This interface must be implements by all classes that wants to be notify
 * when a SICA change its values.
 * Furthermore the implementing class need to register itself to the SICA
 * instance using the addListener() method.
 */
public interface SicaListener {

    /** 
   * This method is called each time the SICA changed one (or more) of its
   * values.
   */
    public void sicaChanged();
}
