package com.philemonworks.pocogese.command;

import junit.framework.TestCase;
import com.philemonworks.pocogese.command.Command;
import com.philemonworks.pocogese.command.CommandXmlIO;
import com.philemonworks.pocogese.command.Reply;
import com.philemonworks.pocogese.command.ReplyXmlIO;
import com.philemonworks.pocogese.util.XMLWriter;

public class ReplyTest extends TestCase {

    public void testOk() {
        Command cmd = new Command("login");
        cmd.putParameter("name", "ernest");
        cmd.putParameter("password", "lisa");
        Reply reply = new Reply();
        reply.xml = CommandXmlIO.toXml(cmd);
        ReplyXmlIO io = new ReplyXmlIO();
        io.write(reply, new XMLWriter(System.out));
    }

    public void testOkNoContent() {
        Reply reply = new Reply();
        ReplyXmlIO io = new ReplyXmlIO();
        io.write(reply, new XMLWriter(System.out));
    }

    public void testOkErrror() {
        Reply reply = new Reply();
        reply.setError("Bummer, <db> lookup fail&d");
        ReplyXmlIO io = new ReplyXmlIO();
        io.write(reply, new XMLWriter(System.out));
    }
}
