package com.phloc.commons.log.adapter;

import java.io.IOException;
import java.io.OutputStream;
import com.phloc.commons.log.ILogger;
import com.phloc.commons.log.LogLevel;
import com.phloc.commons.log.LoggerPool;

/**
 * A special output stream that redirects output to a certain Logger.
 * 
 * @author philip
 */
final class LoggerOutputStream extends OutputStream {

    private final ILogger m_aLogger;

    private final LogLevel m_aLogLevel;

    private final StringBuilder m_aSB = new StringBuilder(200);

    public LoggerOutputStream(final String sName, final LogLevel aLogLevel) {
        this(LoggerPool.getLogger(sName), aLogLevel);
    }

    public LoggerOutputStream(final ILogger aLogger, final LogLevel aLogLevel) {
        if (aLogger == null || aLogLevel == null) throw new IllegalArgumentException();
        m_aLogger = aLogger;
        m_aLogLevel = aLogLevel;
    }

    @Override
    public void write(final int b) throws IOException {
        if (b != '\r' && b != '\n') m_aSB.append((char) b); else flush();
    }

    @Override
    public void flush() {
        if (m_aSB.length() > 0) {
            m_aLogger.log(m_aLogLevel, "[sys] " + m_aSB.toString());
            m_aSB.delete(0, m_aSB.length());
        }
    }
}
