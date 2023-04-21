package gr.aueb.cs.nlg.NLFiles;

import java.util.HashMap;
import java.io.*;
import gr.aueb.cs.nlg.Languages.*;

public class Repetitions extends Parameters {

    public Repetitions() {
        super();
    }

    public HashMap<String, ParameterNode> getRepetitions() {
        return this.getParameters();
    }

    public void add(ParameterNode RN) {
        super.add(RN);
    }

    public void setRepetitions(HashMap<String, ParameterNode> map) {
        this.setParameters(map);
    }

    public void print() {
        System.out.println("===printing repetitions===");
        this.print();
        System.out.println("===/printing repetitions===");
    }
}
