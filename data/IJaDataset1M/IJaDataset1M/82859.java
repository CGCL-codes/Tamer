package ABTOOLS.ANTLR_TOOLS;

import ABTOOLS.DEBUGGING.*;
import java.io.*;

public class Metrics {

    private DEBUG debugging;

    protected int nbLine = 0;

    protected int nbLineComment = 0;

    protected int nbLineInst = 0;

    protected int nbVarGlobal = 0;

    protected int nbCVarGlobal = 0;

    protected int nbConstant = 0;

    protected int nbAConstant = 0;

    protected int nbSet = 0;

    protected int nbService = 0;

    public void setDebug(DEBUG newdebug) {
        debugging = newdebug;
    }

    public void reinit() {
        nbLine = 0;
        nbLineComment = 0;
        nbLineInst = 0;
        nbVarGlobal = 0;
        nbCVarGlobal = 0;
        nbConstant = 0;
        nbAConstant = 0;
        nbSet = 0;
        nbService = 0;
    }

    public void addLine() {
        nbLine = nbLine + 1;
    }

    public void addLineComment() {
        nbLineComment = nbLineComment + 1;
    }

    public void addLineInst() {
        nbLineInst = nbLineInst + 1;
    }

    public void addVarGlobal() {
        nbVarGlobal = nbVarGlobal + 1;
    }

    public void addCVarGlobal() {
        nbCVarGlobal = nbCVarGlobal + 1;
    }

    public void addAConstant() {
        nbAConstant = nbAConstant + 1;
    }

    public void addConstant() {
        nbConstant = nbConstant + 1;
    }

    public void addSet() {
        nbSet = nbSet + 1;
    }

    public void addService() {
        nbService = nbService + 1;
    }

    public void Print() {
        System.out.println("BEGIN METRIC\n");
        System.out.println("+-------------------------------------------\n");
        System.out.println("Line               : " + nbLine + " Comment= " + nbLineComment + " Intsruction= " + nbLineInst + "\n");
        System.out.println("Concrete CONSTANTS : " + nbConstant + "ABSTRACT CONSTANTS=" + nbAConstant + "\n");
        System.out.println("Nb VAR G           : " + nbVarGlobal + "  Nb Concrete VAR G :" + nbCVarGlobal + "\n");
        System.out.println("Nb Set             : " + nbSet + "\n");
        System.out.println("Nb Service         : " + nbService + "\n");
        System.out.println("+-------------------------------------------\n");
        System.out.println("END METRIC\n");
    }
}
