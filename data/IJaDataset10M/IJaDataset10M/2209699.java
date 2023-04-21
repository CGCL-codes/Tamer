package org.rip.pop.client;

import org.apache.commons.lang.ArrayUtils;
import org.rip.pop.popReply;
import org.rip.pop.popResponse;

public class open extends popCommand {

    @Override
    public String syntax() {
        return "OPEN <server> [port]";
    }

    public String[] args;

    @Override
    public boolean parse(String[] a) {
        if (ArrayUtils.isEmpty(a)) {
            return false;
        }
        if (a.length < 2) {
            return false;
        }
        args = a;
        return true;
    }

    @Override
    public boolean execute(popSession session) {
        String port = "110";
        if (args.length > 2) {
            port = args[2];
        }
        popReply reply = session.openConnection(args[1], port);
        if (reply.response == popResponse.OK) {
            session.displayMessage("ok");
            return true;
        }
        return false;
    }
}
