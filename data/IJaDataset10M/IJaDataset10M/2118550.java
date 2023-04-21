package lb.prove;

import java.lang.String;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.BinderNative;
import android.os.Parcel;

public interface IReadService extends android.os.IInterface {

    /** Local-side IPC implementation stub class. */
    public abstract static class Stub extends android.os.BinderNative implements lb.prove.IReadService {

        private static final java.lang.String DESCRIPTOR = "lb.prove.IReadService";

        /** Construct the stub at attach it to the interface. */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
 * Cast an IBinder object into an IReadService interface,
 * generating a proxy if needed.
 */
        public static lb.prove.IReadService asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            lb.prove.IReadService in = (lb.prove.IReadService) obj.queryLocalInterface(DESCRIPTOR);
            if ((in != null)) {
                return in;
            }
            return new lb.prove.IReadService.Stub.Proxy(obj);
        }

        public android.os.IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) {
            try {
                switch(code) {
                    case TRANSACTION_getPid:
                        {
                            int _result = this.getPid();
                            reply.writeInt(_result);
                            return true;
                        }
                    case TRANSACTION_setReadTimeout:
                        {
                            long _arg0;
                            _arg0 = data.readLong();
                            this.setReadTimeout(_arg0);
                            return true;
                        }
                    case TRANSACTION_setDeleteTimeout:
                        {
                            long _arg0;
                            _arg0 = data.readLong();
                            this.setDeleteTimeout(_arg0);
                            return true;
                        }
                    case TRANSACTION_setUrl:
                        {
                            java.lang.String _arg0;
                            _arg0 = data.readString();
                            this.setUrl(_arg0);
                            return true;
                        }
                }
            } catch (android.os.DeadObjectException e) {
            }
            return super.onTransact(code, data, reply, flags);
        }

        private static class Proxy implements lb.prove.IReadService {

            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public int getPid() throws android.os.DeadObjectException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                int _result;
                try {
                    mRemote.transact(Stub.TRANSACTION_getPid, _data, _reply, 0);
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            public void setReadTimeout(long minutes) throws android.os.DeadObjectException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeLong(minutes);
                    mRemote.transact(Stub.TRANSACTION_setReadTimeout, _data, null, 0);
                } finally {
                    _data.recycle();
                }
            }

            public void setDeleteTimeout(long days) throws android.os.DeadObjectException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeLong(days);
                    mRemote.transact(Stub.TRANSACTION_setDeleteTimeout, _data, null, 0);
                } finally {
                    _data.recycle();
                }
            }

            public void setUrl(java.lang.String newUrl) throws android.os.DeadObjectException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeString(newUrl);
                    mRemote.transact(Stub.TRANSACTION_setUrl, _data, null, 0);
                } finally {
                    _data.recycle();
                }
            }
        }

        static final int TRANSACTION_getPid = (IBinder.FIRST_CALL_TRANSACTION + 0);

        static final int TRANSACTION_setReadTimeout = (IBinder.FIRST_CALL_TRANSACTION + 1);

        static final int TRANSACTION_setDeleteTimeout = (IBinder.FIRST_CALL_TRANSACTION + 2);

        static final int TRANSACTION_setUrl = (IBinder.FIRST_CALL_TRANSACTION + 3);
    }

    public int getPid() throws android.os.DeadObjectException;

    public void setReadTimeout(long minutes) throws android.os.DeadObjectException;

    public void setDeleteTimeout(long days) throws android.os.DeadObjectException;

    public void setUrl(java.lang.String newUrl) throws android.os.DeadObjectException;
}
