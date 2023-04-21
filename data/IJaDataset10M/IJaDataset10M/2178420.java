package org.nymph.error_test;

import android.os.IBinder;
import android.os.Parcel;
import android.os.DeadObjectException;

/**
 * 
 *
 * @author Sergey Krutsenko
 *
 * @version 1.0
 *
 */
public class ErrorHandlerProxy implements IErrorHandler {

    private IBinder mBinder;

    /**
	 * Create a new <code>ErrorHandlerProxy</code> instance 
	 * @param binder
	 */
    public ErrorHandlerProxy(IBinder binder) {
        super();
        mBinder = binder;
    }

    /**
	 * @param msg 
	 */
    public int setError(String tag, String msg, int level) throws android.os.DeadObjectException {
        Parcel data = android.os.Parcel.obtain();
        String[] messages = new String[3];
        messages[0] = tag;
        messages[1] = msg;
        messages[2] = String.valueOf(level);
        data.writeStringArray(messages);
        Parcel reply = android.os.Parcel.obtain();
        int result = -1;
        try {
            mBinder.transact(ErrorHandlerStub.TRANSACTION_setError, data, reply, 0);
            result = reply.readInt();
        } finally {
            reply.recycle();
            data.recycle();
        }
        return result;
    }

    /**
	 * @return 
	 */
    public IBinder asBinder() {
        return mBinder;
    }
}
