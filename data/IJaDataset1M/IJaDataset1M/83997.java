package android.os;

import android.util.Log;
import android.util.LogPrinter;

/**
 * {@hide}
 *
 * Implement a state machine where each state is an object,
 * HandlerState. Each HandlerState must implement processMessage
 * and optionally enter/exit. When a state machine is created
 * the initial state must be set. When messages are sent to
 * a state machine the current state's processMessage method is
 * invoked. If this is the first message for this state the
 * enter method is called prior to processMessage and when
 * transtionTo is invoked the state's exit method will be
 * called after returning from processMessage.
 *
 * If a message should be handled in a different state the
 * processMessage method may call deferMessage. This causes
 * the message to be saved on a list until transitioning
 * to a new state, at which time all of the deferred messages
 * will be put on the front of the state machines queue and
 * processed by the new current state's processMessage
 * method.
 *
 * Below is an example state machine with two state's, S1 and S2.
 * The initial state is S1 which defers all messages and only
 * transition to S2 when message.what == TEST_WHAT_2. State S2
 * will process each messages until it receives TEST_WHAT_2
 * where it will transition back to S1:
<code>
     class StateMachine1 extends HandlerStateMachine {
        private static final int TEST_WHAT_1 = 1;
        private static final int TEST_WHAT_2 = 2;

        StateMachine1(String name) {
            super(name);
            setInitialState(mS1);
        }

        class S1 extends HandlerState {
            &amp;#064;Override public void enter(Message message) {
            }

            &amp;#064;Override public void processMessage(Message message) {
                deferMessage(message);
                if (message.what == TEST_WHAT_2) {
                    transitionTo(mS2);
                }
            }

            &amp;#064;Override public void exit(Message message) {
            }
        }

        class S2 extends HandlerState {
            &amp;#064;Override public void processMessage(Message message) {
                // Do some processing
                if (message.what == TEST_WHAT_2) {
                    transtionTo(mS1);
                }
            }
        }

        private S1 mS1 = new S1();
        private S2 mS2 = new S2();
    }
</code>
 */
public class HandlerStateMachine {

    private boolean mDbg = false;

    private static final String TAG = "HandlerStateMachine";

    private String mName;

    private SmHandler mHandler;

    private HandlerThread mHandlerThread;

    /**
     * Handle messages sent to the state machine by calling
     * the current state's processMessage. It also handles
     * the enter/exit calls and placing any deferred messages
     * back onto the queue when transitioning to a new state.
     */
    class SmHandler extends Handler {

        SmHandler(Looper looper) {
            super(looper);
        }

        /**
         * This will dispatch the message to the
         * current state's processMessage.
         */
        @Override
        public final void handleMessage(Message msg) {
            if (mDbg) Log.d(TAG, "SmHandler.handleMessage E");
            if (mDestState != null) {
                if (mDbg) Log.d(TAG, "SmHandler.handleMessage; new destation call enter");
                mCurrentState = mDestState;
                mDestState = null;
                mCurrentState.enter(msg);
            }
            if (mCurrentState != null) {
                if (mDbg) Log.d(TAG, "SmHandler.handleMessage; call processMessage");
                mCurrentState.processMessage(msg);
            } else {
                Log.e(TAG, "handleMessage: no current state, did you call setInitialState");
            }
            if (mDestState != null) {
                if (mDbg) Log.d(TAG, "SmHandler.handleMessage; new destination call exit");
                mCurrentState.exit(msg);
                while (mDeferredMessages != null) {
                    Message cur = mDeferredMessages;
                    mDeferredMessages = mDeferredMessages.next;
                    cur.when = 0;
                    if (mDbg) Log.d(TAG, "SmHandler.handleMessage; queue deferred message what=" + cur.what + " target=" + cur.target);
                    sendMessageAtFrontOfQueue(cur);
                }
                if (mDbg) Log.d(TAG, "SmHandler.handleMessage X");
            }
        }

        public HandlerState mCurrentState;

        public HandlerState mDestState;

        public Message mDeferredMessages;
    }

    /**
     * Create an active StateMachine, one that has a
     * dedicated thread/looper/queue.
     */
    public HandlerStateMachine(String name) {
        mName = name;
        mHandlerThread = new HandlerThread(name);
        mHandlerThread.start();
        mHandler = new SmHandler(mHandlerThread.getLooper());
    }

    /**
     * Get a message and set Message.target = this.
     */
    public final Message obtainMessage() {
        Message msg = Message.obtain(mHandler);
        if (mDbg) Log.d(TAG, "StateMachine.obtainMessage() EX target=" + msg.target);
        return msg;
    }

    /**
     * Get a message and set Message.target = this and
     * Message.what = what.
     */
    public final Message obtainMessage(int what) {
        Message msg = Message.obtain(mHandler, what);
        if (mDbg) {
            Log.d(TAG, "StateMachine.obtainMessage(what) EX what=" + msg.what + " target=" + msg.target);
        }
        return msg;
    }

    /**
     * Enqueue a message to this state machine.
     */
    public final void sendMessage(Message msg) {
        if (mDbg) Log.d(TAG, "StateMachine.sendMessage EX msg.what=" + msg.what);
        mHandler.sendMessage(msg);
    }

    /**
     * Enqueue a message to this state machine after a delay.
     */
    public final void sendMessageDelayed(Message msg, long delayMillis) {
        if (mDbg) {
            Log.d(TAG, "StateMachine.sendMessageDelayed EX msg.what=" + msg.what + " delay=" + delayMillis);
        }
        mHandler.sendMessageDelayed(msg, delayMillis);
    }

    /**
     * Set the initial state. This must be invoked before
     * and messages are sent to the state machine.
     */
    public void setInitialState(HandlerState initialState) {
        if (mDbg) {
            Log.d(TAG, "StateMachine.setInitialState EX initialState" + initialState.getClass().getName());
        }
        mHandler.mDestState = initialState;
    }

    /**
     * transition to destination state. Upon returning
     * from processMessage the current state's exit will
     * be executed and upon the next message arriving
     * destState.enter will be invoked.
     */
    public final void transitionTo(HandlerState destState) {
        if (mDbg) {
            Log.d(TAG, "StateMachine.transitionTo EX destState" + destState.getClass().getName());
        }
        mHandler.mDestState = destState;
    }

    /**
     * Defer this message until next state transition.
     * Upon transitioning all deferred messages will be
     * placed on the queue and reprocessed in the original
     * order. (i.e. The next state the oldest messages will
     * be processed first)
     */
    public final void deferMessage(Message msg) {
        if (mDbg) {
            Log.d(TAG, "StateMachine.deferMessage EX mDeferredMessages=" + mHandler.mDeferredMessages);
        }
        Message newMsg = obtainMessage();
        newMsg.copyFrom(msg);
        newMsg.next = mHandler.mDeferredMessages;
        mHandler.mDeferredMessages = newMsg;
    }

    /**
     * @return the name
     */
    public String getName() {
        return mName;
    }

    /**
     * @return Handler
     */
    public Handler getHandler() {
        return mHandler;
    }

    /**
     * @return if debugging is enabled
     */
    public boolean isDbg() {
        return mDbg;
    }

    /**
     * Set debug enable/disabled.
     */
    public void setDbg(boolean dbg) {
        mDbg = dbg;
        if (mDbg) {
            mHandlerThread.getLooper().setMessageLogging(new LogPrinter(Log.VERBOSE, TAG));
        } else {
            mHandlerThread.getLooper().setMessageLogging(null);
        }
    }
}
