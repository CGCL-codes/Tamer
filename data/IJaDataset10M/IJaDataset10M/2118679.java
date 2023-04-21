package org.appspy.perf.servlet;

import java.io.BufferedReader;
import java.io.IOException;

public class AppSpyServletReader extends BufferedReader {

    protected int mTotalRead = 0;

    protected boolean mIsNested = false;

    public AppSpyServletReader(BufferedReader in) {
        super(in);
    }

    public int read() throws IOException {
        if (mIsNested) {
            return super.read();
        } else {
            mIsNested = true;
            IOException ioException = null;
            RuntimeException runtimeException = null;
            int read = 0;
            try {
                read = super.read();
                if (read > 0) {
                    mTotalRead = mTotalRead + 1;
                }
            } catch (IOException ex) {
                ioException = ex;
            } catch (RuntimeException ex) {
                runtimeException = ex;
            } finally {
                mIsNested = false;
                if (ioException != null) {
                    throw ioException;
                } else if (runtimeException != null) {
                    throw runtimeException;
                }
            }
            return read;
        }
    }

    public int read(char[] cbuf, int off, int len) throws IOException {
        if (mIsNested) {
            return super.read(cbuf, off, len);
        } else {
            mIsNested = true;
            IOException ioException = null;
            RuntimeException runtimeException = null;
            int read = 0;
            try {
                read = super.read(cbuf, off, len);
                if (read > 0) {
                    mTotalRead = mTotalRead + read;
                }
            } catch (IOException ex) {
                ioException = ex;
            } catch (RuntimeException ex) {
                runtimeException = ex;
            } finally {
                mIsNested = false;
                if (ioException != null) {
                    throw ioException;
                } else if (runtimeException != null) {
                    throw runtimeException;
                }
            }
            return read;
        }
    }

    public String readLine() throws IOException {
        if (mIsNested) {
            return super.readLine();
        } else {
            mIsNested = true;
            IOException ioException = null;
            RuntimeException runtimeException = null;
            String line = null;
            try {
                line = super.readLine();
                if (line != null) {
                    mTotalRead = mTotalRead + line.length();
                }
            } catch (IOException ex) {
                ioException = ex;
            } catch (RuntimeException ex) {
                runtimeException = ex;
            } finally {
                mIsNested = false;
                if (ioException != null) {
                    throw ioException;
                } else if (runtimeException != null) {
                    throw runtimeException;
                }
            }
            return line;
        }
    }

    public int read(char[] cbuf) throws IOException {
        if (mIsNested) {
            return super.read(cbuf);
        } else {
            mIsNested = true;
            IOException ioException = null;
            RuntimeException runtimeException = null;
            int read = 0;
            try {
                read = super.read(cbuf);
                if (read > 0) {
                    mTotalRead = mTotalRead + read;
                }
            } catch (IOException ex) {
                ioException = ex;
            } catch (RuntimeException ex) {
                runtimeException = ex;
            } finally {
                mIsNested = false;
                if (ioException != null) {
                    throw ioException;
                } else if (runtimeException != null) {
                    throw runtimeException;
                }
            }
            return read;
        }
    }

    public int getTotalRead() {
        return mTotalRead;
    }
}
