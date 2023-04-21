package com.rbnb.api;

import com.rbnb.compat.Utilities;

class Serialize {

    Serialize() {
        super();
    }

    static final void readOpenBracket(InputStream isI) throws com.rbnb.api.SerializeException, java.io.EOFException, java.io.IOException {
        if (isI.readChar() != '{') {
            throw new com.rbnb.api.SerializeException("The open bracket ({) that should mark the start of the " + "object was not seen.");
        }
    }

    static final int readParameter(String[] parametersI, InputStream isI) throws com.rbnb.api.SerializeException, java.io.EOFException, java.io.IOException {
        int parameterR = -1;
        isI.mark(8);
        char value;
        if (isI.getBinary()) {
            value = isI.readChar();
            if (Utilities.interrupted(Thread.currentThread())) {
                Thread.currentThread().interrupt();
            }
        } else {
            do {
                value = isI.readChar();
                if (Utilities.interrupted(Thread.currentThread())) {
                    Thread.currentThread().interrupt();
                }
            } while (Utilities.isWhitespace(value));
        }
        if (value != '}') {
            if (parametersI == null) {
                throw new com.rbnb.api.SerializeException("No parameters are accepted.");
            }
            isI.reset();
            if ((parameterR = isI.readParameter(parametersI)) == -1) {
                throw new com.rbnb.api.SerializeException("Unrecognizable parameter read from input stream.\n" + isI.getInfoMessage());
            }
        }
        return (parameterR);
    }

    static final void writeCloseBracket(OutputStream osI) throws java.io.IOException {
        osI.writeChar('}');
    }

    static final void writeOpenBracket(OutputStream osI) throws java.io.IOException {
        osI.writeChar('{');
    }
}
