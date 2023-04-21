package org.fernwood.jbasic.informats;

import org.fernwood.jbasic.informats.InputProcessor.Command;
import org.fernwood.jbasic.value.Value;

/**
 * INTEGER(length) Format<p>
 * 
 * This implements the INTEGER input format, which reads a textual
 * representation of a format and converts it to an integer value.  The
 * value can be signed.  If the length is "*" then a varying-width field
 * is searched for the first integer found.  If an explicit length is given
 * then a field of exactly that size is searched.  The integer MUST be
 * right-justified within the field.
 * @author cole
 * @version version 1.0 Jun 6, 2007
 *
 */
class IntegerFormat extends Informat {

    Value run(InputProcessor input, Command cmd) {
        int mark = input.mark();
        char ch = input.nextChar();
        int sign = 1;
        int v = 0;
        int len = cmd.length;
        input.fActive = false;
        if (len == Command.VARYING) len = input.buffer.length();
        while (len > 0) {
            if (Character.isWhitespace(ch)) {
                ch = input.nextChar();
                len--;
            } else break;
        }
        if (ch == '+') {
            ch = input.nextChar();
            len--;
        } else if (ch == '-') {
            sign = -1;
            len--;
            ch = input.nextChar();
        }
        int count = 0;
        while (count < len) {
            if (ch >= '0' && ch <= '9') {
                v = v * 10 + (ch - '0');
                ch = input.nextChar();
                count++;
            } else {
                if (cmd.length == Command.VARYING) break;
                input.reset(mark);
                return null;
            }
        }
        if (count > 0) {
            input.backup();
            return new Value(v * sign);
        }
        input.reset(mark);
        return null;
    }
}
