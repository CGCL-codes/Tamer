package jolie.net;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import jolie.Scanner;
import jolie.TempVariable;
import jolie.Variable;

public class SODEPProtocol implements CommProtocol {

    public SODEPProtocol clone() {
        return new SODEPProtocol();
    }

    public void send(OutputStream ostream, CommMessage message) throws IOException {
        String mesg = "operation:" + message.inputId() + '\n' + "values{";
        Variable var;
        Iterator<Variable> it = message.iterator();
        while (it.hasNext()) {
            var = it.next();
            if (var.isString() || !var.isDefined()) mesg += '"' + var.strValue() + '"'; else if (var.isInt()) mesg += var.intValue(); else throw new IOException("sodep packet creation: invalid variable type or undefined variable");
            if (it.hasNext()) mesg += ',';
        }
        mesg += '}';
        mesg = Scanner.addStringTerminator(mesg);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ostream));
        writer.write(mesg);
        writer.flush();
    }

    public CommMessage recv(InputStream istream) throws IOException {
        Scanner.Token token;
        CommMessage message = null;
        TempVariable var;
        boolean stop = false;
        Scanner scanner = new Scanner(istream, "network");
        token = scanner.getToken();
        if (token.type() == Scanner.TokenType.EOF) return null;
        if (token.type() != Scanner.TokenType.ID || !(token.content().equals("operation"))) throw new IOException("malformed SODEP packet. operation keyword expected");
        token = scanner.getToken();
        if (token.type() != Scanner.TokenType.COLON) throw new IOException("malformed SODEP packet. : expected");
        token = scanner.getToken();
        if (token.type() != Scanner.TokenType.ID) throw new IOException("malformed SODEP packet. operation identifier expected");
        message = new CommMessage(token.content());
        token = scanner.getToken();
        if (token.type() != Scanner.TokenType.ID || !(token.content().equals("values"))) throw new IOException("malformed SODEP packet. values keyword expected");
        token = scanner.getToken();
        if (token.type() != Scanner.TokenType.LCURLY) throw new IOException("malformed SODEP packet. { expected");
        token = scanner.getToken();
        while (token.type() != Scanner.TokenType.RCURLY && !stop) {
            if (token.type() == Scanner.TokenType.STRING) var = new TempVariable(token.content()); else if (token.type() == Scanner.TokenType.INT) var = new TempVariable(Integer.parseInt(token.content())); else throw new IOException("malformed SODEP packet. invalid variable type");
            message.addValue(var);
            token = scanner.getToken();
            if (token.type() != Scanner.TokenType.COMMA) stop = true; else token = scanner.getToken();
        }
        if (token.type() != Scanner.TokenType.RCURLY) throw new IOException("malformed SODEP packet. } expected");
        return message;
    }
}
