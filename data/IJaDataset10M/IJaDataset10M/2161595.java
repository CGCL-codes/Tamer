package jde.juci.test;

import jde.juci.ConnectionFactory;

/**
 * Testing/example class for calling back to elisp through JUCI.
 *
 * @author <a href="mailto:nsieger@bitstream.net">Nick Sieger</a>
 * @version 1.0
 */
public class CallbackImpl implements Callback {

    /**
   * Creates a new <code>CallbackImpl</code> instance.
   *
   */
    public CallbackImpl() {
    }

    public String getMessage() {
        Prompt prompt = (Prompt) ConnectionFactory.getConnection(Prompt.class);
        return prompt.userInput();
    }

    public String getBufferContents() {
        Prompt prompt = (Prompt) ConnectionFactory.getConnection(Prompt.class);
        return prompt.bufferContents();
    }
}
