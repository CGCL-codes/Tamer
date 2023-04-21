package ac.hiu.j314.elmve;

import java.io.*;

public class ReqBase extends Message {

    /** (arg:fuku) */
    ReqBase(Elm sender, Elm receiver, String methodName, Serializable arguments[]) {
        super(sender, receiver, methodName, arguments);
    }
}
