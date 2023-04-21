package de.tu_berlin.math.coga.common.debug;

import event.EventServer;
import event.MessageEvent;
import event.MessageEvent.MessageType;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * <p>The class {@code DebugStream} mirrors outputs on an {@link OutputStream}
 * to another {@link PrintStream}. This can be used to write both, file output
 * and command line output at the same time using the default
 * {@code System.out}</p>
 * <p>All texts are also sent to an {@link EventServer} and thus can be
 * received by some other logging components such as {@link gui.components.JLogPane}.</p>
 * @author Jan-Philipp Kappmeier
 */
public class DebugStream extends PrintStream {

    /** The message type that is created by this debug stream. */
    MessageType messageType = MessageType.Log;

    /** Temp string that is used if some messages are connected to one line. */
    String message = "";

    /**
	 * Creates messageType new instance of {@code DebugStream}.
	 * @param out the outputstream that is used
	 */
    public DebugStream(OutputStream out) {
        super(out);
    }

    /**
	 * Creates messageType new instance of {@code DebugStream}.
	 * @param out the outputstream that is used
	 * @param m the type of messages handeled by this debug stream.
	 */
    public DebugStream(OutputStream out, MessageType m) {
        super(out);
        this.messageType = m;
    }

    /** {@inheritDoc} */
    @Override
    public void print(boolean b) {
        super.print(b);
        message = message + b;
    }

    /** {@inheritDoc} */
    @Override
    public void print(char c) {
        super.print(c);
        message = message + c;
    }

    /** {@inheritDoc} */
    @Override
    public void print(int i) {
        super.print(i);
        message = message + i;
    }

    /** {@inheritDoc} */
    @Override
    public void print(long l) {
        super.print(l);
        message = message + l;
    }

    /** {@inheritDoc} */
    @Override
    public void print(float f) {
        super.print(f);
        message = message + f;
    }

    /** {@inheritDoc} */
    @Override
    public void print(double d) {
        super.print(d);
        message = message + d;
    }

    /** {@inheritDoc} */
    @Override
    public void print(char s[]) {
        super.print(s);
        message = message + s;
    }

    /** {@inheritDoc} */
    @Override
    public void print(String s) {
        super.print(s);
        message = message + s;
    }

    /** {@inheritDoc} */
    @Override
    public void print(Object obj) {
        super.print(obj);
        message = message + obj.toString();
    }

    /** {@inheritDoc} */
    @Override
    public void println() {
        super.println();
        this.sendMessage();
    }

    /** {@inheritDoc} */
    @Override
    public void println(boolean b) {
        super.println(b);
        sendMessage();
    }

    /** {@inheritDoc} */
    @Override
    public void println(char c) {
        super.println(c);
        sendMessage();
    }

    /** {@inheritDoc} */
    @Override
    public void println(int i) {
        super.println(i);
        sendMessage();
    }

    /** {@inheritDoc} */
    @Override
    public void println(long l) {
        super.println(l);
        sendMessage();
    }

    /** {@inheritDoc} */
    @Override
    public void println(float f) {
        super.println(f);
        sendMessage();
    }

    /** {@inheritDoc} */
    @Override
    public void println(double d) {
        super.println(d);
        sendMessage();
    }

    /** {@inheritDoc} */
    @Override
    public void println(char c[]) {
        super.println(c);
        sendMessage();
    }

    /** {@inheritDoc} */
    @Override
    public void println(String s) {
        super.println(s);
        sendMessage();
    }

    /** {@inheritDoc} */
    @Override
    public void println(Object obj) {
        super.println(obj);
        sendMessage();
    }

    /**
	 * Sends the temporally stored message to the event handler.
	 */
    private void sendMessage() {
        EventServer.getInstance().dispatchEvent(new MessageEvent<DebugStream>(this, messageType, message));
        message = "";
    }
}
