package net.jfipa.io;

import java.io.*;

public final class DataSource {

    protected final int BUFSIZE = 4096;

    protected final int EOF = -1;

    protected String _name;

    protected Reader _input;

    protected int _currentLine = 1;

    protected StringBuffer _currentLineData = null;

    private boolean _startedFlag;

    protected int _buffPos = BUFSIZE;

    protected int _readLength;

    protected int _currentChar = -1;

    protected char[] _readBuffer = new char[BUFSIZE];

    public DataSource(File source) throws FileNotFoundException {
        _input = new FileReader(source);
        _name = "file `" + source.getName() + "'";
        _currentLineData = new StringBuffer();
        _startedFlag = false;
    }

    public DataSource(String source) {
        _input = new StringReader(source);
        _name = "string \"" + source + "\"";
        _currentLineData = new StringBuffer();
        _startedFlag = false;
    }

    public DataSource(Reader source) {
        _input = source;
        _name = "file `" + source.toString() + "'";
        _currentLineData = new StringBuffer();
        _startedFlag = false;
    }

    /**
     * @return String with line
     *
     * @throws IOException if read fails
     */
    public final String readLine() throws IOException, EOFException {
        try {
            _currentLineData.setLength(0);
            int readChar = read();
            if (readChar == EOF) {
                throw new EOFException("DataSource.readLine() - got to eof");
            }
            while ((readChar != EOF) && (readChar != ((int) '\n'))) {
                _currentLineData.append((char) readChar);
                readChar = read();
            }
            _currentChar = readChar;
            return _currentLineData.toString();
        } catch (IOException e) {
            throw new IOException("readLine() failed");
        }
    }

    /**
     *
     */
    public final int getCurrentChar() {
        return _currentChar;
    }

    /**
     *
     * @throws IOException if read fails
     */
    public final int read() throws IOException {
        if ((_buffPos == BUFSIZE)) {
            _readLength = _input.read(_readBuffer, 0, BUFSIZE);
            _buffPos = 0;
            if (_readLength == EOF) {
                return EOF;
            }
        }
        if (_buffPos == _readLength) {
            return EOF;
        }
        _currentChar = (int) _readBuffer[_buffPos++];
        if (_currentChar == '\n') _currentLine++;
        _startedFlag = true;
        return _currentChar;
    }

    public final int getCurrentLine() {
        return _currentLine;
    }

    public final String getName() {
        return _name;
    }

    public final void setName(String name) {
        _name = name;
    }

    public final boolean isStarted() {
        return _startedFlag;
    }
}
