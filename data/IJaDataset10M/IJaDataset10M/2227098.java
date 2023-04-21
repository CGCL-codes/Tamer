package android.os;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.test.suitebuilder.annotation.MediumTest;
import junit.framework.TestCase;

public class MessageQueueTest extends TestCase {

    private static class BaseTestHandler extends TestHandlerThread {

        Handler mHandler;

        int mLastMessage;

        int mCount;

        public BaseTestHandler() {
        }

        public void go() {
            mHandler = new Handler() {

                public void handleMessage(Message msg) {
                    BaseTestHandler.this.handleMessage(msg);
                }
            };
        }

        public void handleMessage(Message msg) {
            if (mCount <= mLastMessage) {
                if (msg.what != mCount) {
                    failure(new RuntimeException("Expected message #" + mCount + ", received #" + msg.what));
                } else if (mCount == mLastMessage) {
                    success();
                }
                mCount++;
            } else {
                failure(new RuntimeException("Message received after done, #" + msg.what));
            }
        }
    }

    @MediumTest
    public void testMessageOrder() throws Exception {
        TestHandlerThread tester = new BaseTestHandler() {

            public void go() {
                super.go();
                long now = SystemClock.uptimeMillis() + 200;
                mLastMessage = 4;
                mCount = 0;
                mHandler.sendMessageAtTime(mHandler.obtainMessage(2), now + 1);
                mHandler.sendMessageAtTime(mHandler.obtainMessage(3), now + 2);
                mHandler.sendMessageAtTime(mHandler.obtainMessage(4), now + 2);
                mHandler.sendMessageAtTime(mHandler.obtainMessage(0), now + 0);
                mHandler.sendMessageAtTime(mHandler.obtainMessage(1), now + 0);
            }
        };
        tester.doTest(1000);
    }

    @MediumTest
    public void testAtFrontOfQueue() throws Exception {
        TestHandlerThread tester = new BaseTestHandler() {

            public void go() {
                super.go();
                long now = SystemClock.uptimeMillis() + 200;
                mLastMessage = 3;
                mCount = 0;
                mHandler.sendMessageAtTime(mHandler.obtainMessage(3), now);
                mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(2));
                mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(0));
            }

            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(1));
                }
            }
        };
        tester.doTest(1000);
    }
}
