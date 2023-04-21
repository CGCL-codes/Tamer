package gr.aueb.cs.nlg.NLFiles;

import java.io.*;
import gr.aueb.cs.nlg.Languages.*;

public class InterestNode extends ParameterNode {

    public InterestNode(String UT, String IV) {
        super(UT, IV);
    }

    public String getInterestValue() {
        return this.getParameter();
    }

    public void setInterestValue(String s) {
        this.setParameter(s);
    }

    public void print() {
        System.out.println("\t===InterestNode printing===");
        System.out.println("\t\tforUserType:" + getforUserType());
        System.out.println("\t\tInterestValue:" + getInterestValue());
        System.out.println("\t===/InterestNode printing===");
    }
}
