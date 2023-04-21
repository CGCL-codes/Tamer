package net.infordata.em.crt5250;

import java.io.IOException;

/**
 * Interface that must be implemented to save XI5250 field status.
 * 
 * @see    XI5250Field#saveTo
 *
 * @version  
 * @author   Valentino Proietti - Infordata S.p.A.
 */
public interface XI5250FieldSaver {

    /**
   *
   */
    public void write(XI5250Field aField, String aStr) throws IOException;
}
