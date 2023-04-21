package OKBC;

import java.util.*;
import java.io.*;

public class NotAFrameType extends AbstractError {

    public Node frame_type = null;

    public NotAFrameType() {
    }

    public NotAFrameType(Node the_frame_type, Node the_kb) {
        frame_type = the_frame_type;
        kb = the_kb;
    }

    public NotAFrameType(Node the_frame_type, Node the_kb, Node the_continuable, Node the_error_message) {
        frame_type = the_frame_type;
        kb = the_kb;
        continuable = the_continuable;
        error_message = the_error_message;
    }

    public void init_from_plist(Node plist) {
        frame_type = plistGet(Symbol.keyword("FRAME-TYPE"), plist);
        kb = plistGet(Symbol.keyword("KB"), plist);
        super.init_from_plist(plist);
    }

    public Node decode_to_plist() {
        return Cons.append_cons(Cons.list(Symbol.keyword("FRAME-TYPE"), (frame_type == null ? Node._NIL : frame_type), Symbol.keyword("KB"), (kb == null ? Node._NIL : kb)), super.decode_to_plist());
    }

    public String report() {
        return "Entities of frame-type " + frame_type + " are not represented as frames in " + kb;
    }
}
