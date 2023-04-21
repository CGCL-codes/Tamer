package watij.utilities;

import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.com.IUnknown;

public class IUnknownBinder implements ComThreadBinder {

    Exception exception;

    IUnknown iUnknown;

    OleMessageLoop oleMessageLoop;

    public IUnknownBinder(OleMessageLoop oleMessageLoop) {
        this.oleMessageLoop = oleMessageLoop;
    }

    public IUnknown bind(final IUnknownFactory iUnknownFactory) throws Exception {
        oleMessageLoop.doInvokeAndWait(new Runnable() {

            public void run() {
                try {
                    iUnknown = iUnknownFactory.create();
                } catch (Exception e) {
                    exception = e;
                }
            }
        });
        if (exception != null) {
            throw exception;
        }
        return bind(iUnknown);
    }

    public IUnknown bind(IUnknown iUnknown) throws Exception {
        return oleMessageLoop.bindObject(iUnknown);
    }
}
