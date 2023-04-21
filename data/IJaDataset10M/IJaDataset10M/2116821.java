package jmax.ui;

import javax.swing.*;
import jmax.mda.*;

/** 
 * A UIComponent  to edit MaxDataObject.
 *
 */
public interface Editor extends UIComponent {

    /**
   * This method return the data instance the editor is editing.
   */
    public abstract MaxDataObject getDataObject();
}
