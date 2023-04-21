package com.iver.andami.messages;

public class MessageEvent {

    private String[] messages;

    private Throwable[] exceptions;

    public MessageEvent(String[] msgs, Throwable[] excps) {
        messages = msgs;
        exceptions = excps;
    }

    /**
	 * @return
	 */
    public Throwable[] getExceptions() {
        return exceptions;
    }

    /**
	 * @return
	 */
    public String[] getMessages() {
        return messages;
    }

    /**
	 * @param throwables
	 */
    public void setExceptions(Throwable[] throwables) {
        exceptions = throwables;
    }

    /**
	 * @param strings
	 */
    public void setMessages(String[] strings) {
        messages = strings;
    }
}
