package dict.me;

import dict.common.DictIndex;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

/** The dictionary midlet.
  * @author Daniel Stoinski
  * @version $Revision: 21 $
  */
public class DictMidlet extends MIDlet {

    /** The main dictionary form.
    */
    private DictForm m_form;

    /** Dictionary index.
    */
    private DictIndex m_index;

    /** Initializes the midlet.
    */
    public DictMidlet() {
        this.m_index = new DictIndex();
        this.m_form = new DictForm(this);
    }

    /** Called if the application starts.
    * Sets main form to an instance of DictForm.
    */
    public void startApp() {
        Display.getDisplay(this).setCurrent(this.m_form);
    }

    /** Does absolutely nothing.
    * @param unconditional ignored
    */
    public void destroyApp(boolean unconditional) {
    }

    /** Does absolutely nothing.
    */
    public void pauseApp() {
    }

    /** Index for the dictionary handled in this application.
    * @return the dictionary index
    */
    public final DictIndex getIndex() {
        return this.m_index;
    }
}
