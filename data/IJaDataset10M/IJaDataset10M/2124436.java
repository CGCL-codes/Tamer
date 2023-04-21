package com.ericsson.otp.erlang;

import java.io.Serializable;

/**
 * Provides a Java representation of Erlang integral types. 
 **/
public class OtpErlangChar extends OtpErlangLong implements Serializable, Cloneable {

    static final long serialVersionUID = 3225337815669398204L;

    /**
   * Create an Erlang integer from the given value.
   * 
   * @param c the char value to use.
  **/
    public OtpErlangChar(char c) {
        super(c);
    }

    /**
   * Create an Erlang integer from a stream containing an integer
   * encoded in Erlang external format.
   *
   * @param buf the stream containing the encoded value.
   * 
   * @exception OtpErlangDecodeException if the buffer does not
   * contain a valid external representation of an Erlang integer.
   *
   * @exception OtpErlangRangeException if the value is too large to
   * be represented as a char.
   **/
    public OtpErlangChar(OtpInputStream buf) throws OtpErlangRangeException, OtpErlangDecodeException {
        super(buf);
        char i = charValue();
    }
}
