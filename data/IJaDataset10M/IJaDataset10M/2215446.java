package gov.nist.atlas.event;

/**
 * <p>This interface defines the methods to implement to be able to be notified
 * <strong>before</strong> the value of a Parameter will change.</p>
 *
 * <p>This ValueWillChangeListener can then be registered with the Parameter(s)
 * it is interested in monitoring and will then be notified each time an object
 * tries to modify the value of the monitored Parameter(s). This
 * ValueWillChangeListener has the opportunity to issue a veto to prevent the
 * change. This is especially helpful to perform validation before change.</p>
 *
 * @version $Revision: 1.2 $
 * @author Nicolas Radde
 *
 * @see ValueChangeEvent
 * @see gov.nist.atlas.Parameter
 */
public interface ValueWillChangeListener extends java.util.EventListener {

    /**
   * This method is called whenever a change is requested on the value of the
   * monitored Parameter(s), <strong>before</strong> the actual change thus
   * allowing this ValueWillChangeListener to issue a veto on the change by
   * throwing a ValueChangedVetoException. In this case, the change will be
   * vetoed and no value change will occur.
   *
   * @param event a ValueChangeEvent describing the requested change
   * @throws ValueChangedVetoException if the change is vetoed for any reason
   */
    public void valueWillChange(ValueChangeEvent event) throws ValueChangedVetoException;
}
