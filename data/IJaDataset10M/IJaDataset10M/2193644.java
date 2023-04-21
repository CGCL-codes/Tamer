package examples.goal.loopcheck;

import mcaplantlr.runtime.ANTLRStringStream;
import mcaplantlr.runtime.CommonTokenStream;
import goalc.parser.GOALCLexer;
import goalc.parser.GOALCParser;
import ail.others.MAS;
import mcapl.MCAPLcontroller;
import gov.nasa.jpf.jvm.Verify;
import goalc.GOALMASBuilder;

public class LoopMAS {

    public static void main(String[] args) {
        Verify.beginAtomic();
        int outputlevel = 1;
        int dinernumber = Integer.parseInt(args[0]);
        int propertynum = Integer.parseInt(args[1]);
        String masstring = "";
        try {
            if (dinernumber == 3) {
                System.out.println("Initialising 3 diners");
                masstring = "goalc.GoalEnvironment " + "GOAL " + ":name: ag1 " + ":beliefs " + "hungry " + ":Belief Rules: " + ":goals " + ":Conditional Actions: " + "if B hungry then adopt(hold(fork,left)) " + "if G hold(fork, Hand),  ~ B forksAvailable  then ins(fish) " + ":Action Specifications: " + "think {~ B hungry} {hungry} " + ":name: ag2 " + ":beliefs " + "hold(fork, right) " + "neighbour(ag2, right) " + "neighbour(ag3, left) " + "hungry " + "neighbours(ag2, ag3) " + ":Belief Rules: " + "n(X) :- neighbour(ag3, left) " + "forkAvailable(D)  :- hold(fork, D); on(fork, table, D) " + "forksAvailable :- forkAvailable(left), forkAvailable(right) " + ":goals " + ":Conditional Actions: " + "if B hungry then eat " + "if B hungry then adopt(hold(fork,left)) " + "if G hold(fork, Hand),  B neighbours(X, Y), B n(X) then send(new: [X Y], !hold(fork))  " + ":Action Specifications: " + "think {~ B hungry} {hungry} " + "eat {B hungry, B hold(fork, left), B hold(fork, right)} {~hungry} " + ":name: ag3 " + ":beliefs " + "hungry " + ":Belief Rules: " + ":goals " + ":Conditional Actions: " + "if B hungry then adopt(hold(fork,left)) " + "if G hold(fork, Hand),  ~ B forksAvailable  then ins(fish) " + ":Action Specifications: " + "think {~ B hungry} {hungry} ";
            }
            GOALCLexer lexer = new GOALCLexer(new ANTLRStringStream(masstring));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            GOALCParser parser = new GOALCParser(tokens);
            MAS mas = parser.mas();
            String propertystring = "";
            propertystring = "E(~B(ag1, hungry)))";
            MCAPLcontroller mccontrol = new MCAPLcontroller(mas, propertystring, outputlevel);
            Verify.endAtomic();
            mccontrol.begin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
