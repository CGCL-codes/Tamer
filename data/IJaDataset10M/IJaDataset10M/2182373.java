package dev;

import java.io.*;
import org.vexi.tools.autodoc.parse.*;

public class PokeParser {

    static {
        new Parser((Reader) null);
    }

    public static void main(String[] args) throws ParseException {
        Parser.ReInit(new StringReader("@x(!=) weraoue"));
        Body body = Parser.Body();
        Attribute a = body.find("x");
        System.err.println(a.getArg(0));
    }
}
