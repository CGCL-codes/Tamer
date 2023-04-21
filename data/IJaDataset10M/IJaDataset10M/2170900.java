package it.xargon.lrpc;

import java.io.*;
import java.util.EnumSet;
import it.xargon.streams.Printable;
import it.xargon.util.Bitwise;

class LXmpInvocationAnswer implements Serializable, Printable {

    public enum AnswerType {

        FAILURE(0), SUCCESS(1), EXCEPTION(2), NOCLASS(3), NOMETHOD(3), NOOBJECT(4);

        private final byte v;

        private AnswerType(int id) {
            v = Bitwise.asByte(id);
        }

        public byte getId() {
            return v;
        }

        public static AnswerType getByName(String name) {
            for (AnswerType tp : EnumSet.allOf(AnswerType.class)) if (name.equals(tp.name())) return tp;
            return null;
        }

        public static AnswerType getById(int id) {
            for (AnswerType tp : EnumSet.allOf(AnswerType.class)) if (id == tp.getId()) return tp;
            return null;
        }
    }

    public AnswerType answtype = null;

    public LXmpObjectDescription content = null;

    public void printout(String indent, PrintWriter out) {
        out.print(indent);
        out.print("LXMP Answer to invocation: ");
        switch(answtype) {
            case FAILURE:
                out.println("FAILURE");
                break;
            case SUCCESS:
                out.println("SUCCESS");
                break;
            case EXCEPTION:
                out.println("EXCEPTION");
                break;
            case NOCLASS:
                out.println("NO SUCH CLASS");
                break;
            case NOOBJECT:
                out.println("NO SUCH OBJECT");
                break;
            case NOMETHOD:
                out.print("NO SUCH METHOD");
                break;
        }
        content.printout(indent + "  ", out);
    }
}
