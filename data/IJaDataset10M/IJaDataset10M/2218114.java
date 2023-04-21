package com.anaxima.eslink.sasparser;

import java.io.FileReader;
import com.anaxima.eslink.sasparser.nodes.ASTSasBaseUnit;
import com.anaxima.eslink.sasparser.nodes.SasBaseParserVisitor;
import com.anaxima.eslink.sasparser.sasbase.SasBaseParser;
import com.anaxima.eslink.sasparser.sasbase.TokenMgrError;

/**
 * TODO Add type comment.
 * 
 * @author Thomas Vater
 */
public class Frontend {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        System.out.println("Reading from file input " + args[0]);
        try {
            SasBaseParser t = new SasBaseParser(new FileReader(args[0]), new ParseUtil());
            ASTSasBaseUnit n = t.SasBaseUnit();
            SasBaseParserVisitor v = new SasBaseParserDumpVisitor();
            n.jjtAccept(v, null);
            System.out.println("Thank you.");
        } catch (TokenMgrError tokenError) {
            System.out.println(tokenError);
        } catch (Exception e) {
            System.out.println("Oops.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
