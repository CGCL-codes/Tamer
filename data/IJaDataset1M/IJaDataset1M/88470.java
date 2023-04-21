package org.pojosoft.lms.content.scorm2004.sequencer;

/**
 * Provides an interface for the RTE to communicate navigation requests to the
 * sequencer<br><br>.
 * <p/>
 * <strong>Filename:</strong> Scorm2004Navigator.java<br><br>
 * <p/>
 * <strong>Description:</strong><br>
 * This interface represents the entry point to the Overall Sequencing Process
 * described in IMS ss.  The two <code>navigate</code> methods provide a way
 * for the RTE to signal a navigation request. Each <code>navigate</code>
 * method provides an <code>SeqLaunch</code> object, which contains the
 * information required by the RTE to launch the resource(s) associated with the
 * idenified activity, or an error code if any sequencing process fails.<br><br>
 * <p/>
 * When an navigation request does not result in an activity to be delivered,
 * an <code>ADLLaunch</code> object will still be returned by, however the value
 * its <code>launchErrorCode</code> field will contain a special value from the
 * <code>ADLLaunch.LAUNCH_[XXX]</code> enumeration.<br><br>
 * <p/>
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM Sample RTE 1.3. <br>
 * <br>
 * <p/>
 * <strong>Implementation Issues:</strong><br>
 * It is the responsibility of the implementation of this interface to
 * perform any and all prelauch actions to prepare the identifed activity (and
 * its resource(s)) for launch, prior to returning an <code>ADLLaunch</code>
 * object.<br><br>
 * <p/>
 * If the navigation event does not result in a deliverable activity, it is the
 * responsibily of the RTE to gracefully handle other results.<br><br>
 * <p/>
 * <strong>Known Problems:</strong><br><br>
 * <p/>
 * <strong>Side Effects:</strong><br><br>
 * <p/>
 * <strong>References:</strong><br>
 * <ul>
 * <li>IMS SS 1.0
 * <li>SCORM 1.3
 * </ul>
 */
public interface SeqNavigation {

    /**
   * Enumeration of possible navigation requests --
   * In this case, No navigation request is also valid.
   * <br>None
   * <br><b>0</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
    int NAV_NONE = 0;

    /**
   * Enumeration of possible navigation requests -- described in Navigation
   * Behavior (Section NB of the IMS SS Specification).
   * <br>Start
   * <br><b>1</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
    int NAV_START = 1;

    /**
   * Enumeration of possible navigation requests -- described in Navigation
   * Behavior (Section NB of the IMS SS Specification).
   * <br>Resume All
   * <br><b>2</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
    int NAV_RESUMEALL = 2;

    /**
   * Enumeration of possible navigation requests -- described in Navigation
   * Behavior (Section NB of the IMS SS Specification).
   * <br>Continue
   * <br><b>3</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
    int NAV_CONTINUE = 3;

    /**
   * Enumeration of possible navigation requests -- described in Navigation
   * Behavior (Section NB of the IMS SS Specification).
   * <br>Previous
   * <br><b>4</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
    int NAV_PREVIOUS = 4;

    /**
   * Enumeration of possible navigation requests -- described in Navigation
   * Behavior (Section NB of the IMS SS Specification).
   * <br>Abandon
   * <br><b>5</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
    int NAV_ABANDON = 5;

    /**
   * Enumeration of possible navigation requests -- described in Navigation
   * Behavior (Section NB of the IMS SS Specification).
   * <br>AbandonAll
   * <br><b>6</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
    int NAV_ABANDONALL = 6;

    /**
   * Enumeration of possible navigation requests -- described in Navigation
   * Behavior (Section NB of the IMS SS Specification).
   * <br>SuspendAll
   * <br><b>7</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
    int NAV_SUSPENDALL = 7;

    /**
   * Enumeration of possible navigation requests -- described in Navigation
   * Behavior (Section NB of the IMS SS Specification).
   * <br>Exit
   * <br><b>8</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
    int NAV_EXIT = 8;

    /**
   * Enumeration of possible navigation requests -- described in Navigation
   * Behavior (Section NB of the IMS SS Specification).
   * <br>ExitAll
   * <br><b>9</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
    int NAV_EXITALL = 9;

    int NAV_CHOICE = 400;

    /**
   * This method is used to inform the sequencer that a navigation request,
   * other than 'Choice' has occured.
   *
   * @param iRequest Indicates which navigation request should be processed.
   * @return Information about the 'Next' activity to delivery or a processing
   *         error.
   */
    ADLLaunch navigate(int iRequest);

    /**
   * This method is used to inform the sequencer that a 'Choice' navigation
   * request has occured.
   *
   * @param iTarget ID (<code>String</code>) of the target activity.
   * @return Information about the 'Next' activity to delivery or a processing
   *         error.
   */
    ADLLaunch navigateToActivity(String iTarget);
}
