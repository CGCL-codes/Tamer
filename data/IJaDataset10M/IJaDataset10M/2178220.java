package ch.heuscher.simple.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Important: the read does not change the _lCurrentPosition of the superclass -> only reads on the streamToProxy affect
 * this value, but the data number of bytes read is ouput correctly
 * 
 * @author Stephan@Heuscher.ch
 */
public class ContentInsertInputStream extends ContentChangeInputStream {

    protected long _lOffsetMark = -1;

    protected long _lOffsetAddedByInsert = 0;

    protected int _nEndOffsetMark = 0;

    protected int _nOffsetAtEnd = 0;

    protected StreamHook[] _endStreamHooks;

    /**
    * If the arrays overlap, then the values of the second array are taken
    * 
    * @param streamToProxy
    *           base stream
    * @param hooks
    *           pointers that point to the start of the inserting
    */
    public ContentInsertInputStream(InputStream streamToProxy, StreamHook[] hooks) {
        super(streamToProxy, null);
        ArrayList endDataHooks = new ArrayList();
        ArrayList normalHooks = new ArrayList();
        for (int i = 0; i < hooks.length; i++) {
            StreamHook hook = hooks[i];
            if (hook.getHookPos() == END_OF_STREAM) {
                endDataHooks.add(hook);
            } else {
                normalHooks.add(hook);
            }
        }
        if (endDataHooks.size() > 0) {
            this._endStreamHooks = (StreamHook[]) endDataHooks.toArray(new StreamHook[0]);
        }
        if (normalHooks.size() > 0) {
            setHooks((StreamHook[]) normalHooks.toArray(new StreamHook[0]));
        }
    }

    /**
    * Overriden because there are two types of hooks here, it suffices that one type is set
    */
    public StreamHook[] getHooks() {
        if (this._endStreamHooks == null) {
            return super.getHooks();
        }
        return super._hooks;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int nReadBytesBeforeInsert = len;
        for (int i = 0; i < getNumberOfHooks(); i++) {
            long relativeStart = getHooks()[i].getHookPos() - super._lCurrentPosition;
            if (relativeStart < 0) {
                relativeStart = Long.MAX_VALUE;
            }
            long relativeEndRead = len;
            nReadBytesBeforeInsert = (int) Math.min(nReadBytesBeforeInsert, Math.min(relativeEndRead, relativeStart));
        }
        int nBytesRead = 0;
        if (nReadBytesBeforeInsert > 0 && !this._bProxyIsEmpty) {
            nBytesRead = super.read(b, off, nReadBytesBeforeInsert);
            if (nBytesRead > 0) {
                len -= nBytesRead;
                off += nBytesRead;
            } else {
                nBytesRead = 0;
            }
        }
        int nBytesReadByInsert = 0;
        for (int i = 0; len > 0 && i < getNumberOfHooks(); i++) {
            long start = getHooks()[i].getHookPos();
            long relativeStart = super._lCurrentPosition + this._lOffsetAddedByInsert - start;
            long relativeLength = Math.min(len, getHooks()[i].getData().length - relativeStart);
            if (relativeStart >= 0 && relativeStart < getHooks()[i].getData().length) {
                int startReadOffsetInData = (int) relativeStart;
                int dataLengthToRead = (int) relativeLength;
                System.arraycopy(getHooks()[i].getData(), startReadOffsetInData, b, off, dataLengthToRead);
                nBytesReadByInsert += dataLengthToRead;
            }
        }
        this._lOffsetAddedByInsert += nBytesReadByInsert;
        nBytesRead += nBytesReadByInsert;
        len -= nBytesReadByInsert;
        off += nBytesReadByInsert;
        if (len > 0 && !this._bProxyIsEmpty) {
            int bytesReadFromUnderlyingStream = super.read(b, off, len);
            if (bytesReadFromUnderlyingStream != END_OF_STREAM) {
                nBytesRead += bytesReadFromUnderlyingStream;
                len -= nBytesRead;
                off += nBytesRead;
            }
        }
        if (len > 0 && this._endStreamHooks != null && super._lProxySize >= 0 && _bProxyIsEmpty) {
            for (int i = 0; i < this._endStreamHooks.length; i++) {
                StreamHook sHook = this._endStreamHooks[i];
                int lengthToRead = Math.min(len, sHook.getData().length - _nOffsetAtEnd);
                if (lengthToRead > 0) {
                    System.arraycopy(sHook.getData(), _nOffsetAtEnd, b, off, lengthToRead);
                    this._nOffsetAtEnd += lengthToRead;
                    nBytesReadByInsert += lengthToRead;
                    nBytesRead += lengthToRead;
                }
            }
        }
        if (this._bProxyIsEmpty && nBytesRead == 0) {
            return END_OF_STREAM;
        }
        return nBytesRead;
    }

    public synchronized void reset() throws IOException {
        if (markSupported()) {
            super.reset();
            this._lOffsetAddedByInsert = Math.max(0, this._lOffsetMark);
            this._nOffsetAtEnd = Math.max(0, this._nEndOffsetMark);
        }
    }

    public synchronized void mark(int readlimit) {
        if (markSupported()) {
            super.mark(readlimit);
            this._lOffsetMark = this._lOffsetAddedByInsert;
            this._nEndOffsetMark = this._nOffsetAtEnd;
        }
    }
}
