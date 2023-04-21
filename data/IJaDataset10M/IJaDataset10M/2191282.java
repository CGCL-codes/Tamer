package com.agimatec.sql;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * The SQLWriter is responsible for simplyfying
 * SQL-Generation by providing an API to write
 * to a SQL-String.
 * <p/>
 * $Author: stumm $
 *
 */
public class SQLWriter extends Writer {

    private StringWriter selectStream;

    private StringWriter stream;

    private AliasDictionary aliases;

    protected static final char parameterMarker = '?';

    private List parameters;

    private byte operatorState;

    public static final byte STATE_BEGIN = 0;

    public static final byte STATE_HAS_OP = 1;

    public static final byte STATE_NEEDS_OP = 2;

    /**
     * Initialize the new instance.
     */
    public SQLWriter() {
        this(new AliasDictionary());
    }

    /**
     * Initialize the new instance with a given AliasDictionary or null
     */
    public SQLWriter(final AliasDictionary useAliases) {
        this.stream = new StringWriter();
        this.selectStream = new StringWriter();
        aliases = useAliases;
        operatorState = STATE_BEGIN;
        parameters = new ArrayList();
    }

    /**
     * set the receivers selectStream as the stream and
     * the stream as the selectStream.
     * This method can be used to get the same APIs for writing
     * to the select stream as you have for writing to the stream,
     * if you know what you are currently writing on.
     */
    public void flipStreams() {
        final StringWriter temp = stream;
        stream = selectStream;
        selectStream = temp;
    }

    /**
     * Close the stream.
     */
    public void close() {
    }

    /**
     * Flush the stream. Delegate to the underlying
     * StringWriter.
     */
    public void flush() {
    }

    /**
     * Answer the receiver's alias dictionary.
     */
    public AliasDictionary getAliases() {
        return aliases;
    }

    /**
     * Set the receiver's alias dictionary.
     */
    public void setAliases(final AliasDictionary aAliases) {
        this.aliases = aAliases;
    }

    /**
     * Answer the receiver's string representation, i.e. answer
     * the content of the underlying StringWriter.
     */
    public String toString() {
        try {
            final StringWriter writer = new StringWriter();
            writer.write(getSelectString());
            getAliases().appendAliasListTo(writer);
            writer.write(getWhereString());
            return writer.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex.toString());
        }
    }

    public String getSelectString() {
        return selectStream.toString();
    }

    public String getWhereString() {
        return stream.toString();
    }

    /**
     * return the statement including parameter values instead of ?
     */
    public String getParameterizedStatement() {
        try {
            final StringWriter writer = new StringWriter();
            final SQLStringGenerator sqlGen = new SQLStringGenerator(toString(), writer, getParameters());
            sqlGen.parse();
            return writer.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex.toString());
        }
    }

    /**
     * Create an instance of SQLStatement from the receiver
     */
    public SQLStatement createStatement() {
        return new SQLStatement(toString(), getParameters());
    }

    /**
     * Write a portion of an array of characters. Delegate to
     * the underlying StringWriter.
     */
    public void write(final char[] cbuf, final int off, final int len) {
        stream.write(cbuf, off, len);
        operatorState = STATE_NEEDS_OP;
    }

    /**
     * Write a single character. Delegate to the underlying
     * StringWriter.
     */
    public void write(final int c) {
        stream.write(c);
        operatorState = STATE_NEEDS_OP;
    }

    /**
     * Write a string. Delegate to the underlying StrinWriter.
     */
    public void write(final String str) {
        stream.write(str);
        operatorState = STATE_NEEDS_OP;
    }

    /**
     * does not change operator state. just write str.
     *
     * @return this
     */
    public SQLWriter onSelect_write(final String str) {
        selectStream.write(str);
        return this;
    }

    /**
     * set the operator state to the given contant.
     * use when you known what you do!
     */
    public void setOperatorState(final byte aState) {
        operatorState = aState;
    }

    public byte getOperatorState() {
        return operatorState;
    }

    /**
     * Write a portion of a string. Delegate to the
     * underlying StringWriter.
     */
    public void write(final String str, final int off, final int len) {
        stream.write(str, off, len);
        operatorState = STATE_NEEDS_OP;
    }

    /**
     * Write an AND token to the stream, if neccessary
     */
    public SQLWriter writeAnd() {
        return writeOperator(" AND ");
    }

    public SQLWriter writeOperator(final String operator) {
        if (operatorState == STATE_BEGIN) {
            writeWhere();
        } else if (operatorState == STATE_NEEDS_OP) {
            stream.write(operator);
            operatorState = STATE_HAS_OP;
        }
        return this;
    }

    /**
     * Write an OR token to the stream, if neccessary
     */
    public SQLWriter writeOr() {
        return writeOperator(" OR ");
    }

    /**
     * Write a parameter marker to the stream.
     */
    public SQLWriter writeParameterMarker() {
        write(parameterMarker);
        return this;
    }

    /**
     * Add a parameter and write the parameter marker
     */
    public SQLWriter addParameter(final Object paramValue) {
        parameters.add(paramValue);
        return writeParameterMarker();
    }

    /**
     * return a collection of parameters provided to the reciever
     */
    public List getParameters() {
        return parameters;
    }

    /**
     * Write a WHERE token to the stream, if neccessary
     */
    public SQLWriter writeWhere() {
        if (operatorState == STATE_BEGIN || operatorState == STATE_NEEDS_OP) {
            stream.write(" WHERE ");
            operatorState = STATE_HAS_OP;
        }
        return this;
    }
}
