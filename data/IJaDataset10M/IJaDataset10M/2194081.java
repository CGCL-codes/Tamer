package JOptFrame.Modules;

import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import JOptFrame.Heuristic;
import JOptFrame.HeuristicFactory;
import JOptFrame.InitialPopulation;
import JOptFrame.InitialSolution;
import JOptFrame.JOptFrameModule;
import JOptFrame.Memory;
import JOptFrame.Population;
import JOptFrame.Representation;
import JOptFrame.Solution;

public class ExecModule<R extends Representation, M extends Memory> extends JOptFrameModule<R, M> {

    public String id() {
        return "exec";
    }

    public String usage() {
        return "exec [ <initsol> id | <loadsol> id | <initpop> id popSize | <loadpop> id ] target_fo timelimit method [output_solution_name]";
    }

    public void run(Vector<JOptFrameModule<R, M>> all_modules, HeuristicFactory<R, M> factory, Map<String, String> dictionary, String input) {
        System.out.println("exec: " + input);
        Scanner scanner = new Scanner(input);
        if (!scanner.hasNext()) {
            System.out.println("Usage: " + usage());
            return;
        }
        String sol = scanner.next();
        if ((!sol.equals("initsol")) && (!sol.equals("loadsol")) && (!sol.equals("initpop")) && (!sol.equals("loadpop"))) {
            System.out.println("First parameter must be either 'initsol', 'loadsol', 'initpop', 'loadpop'!");
            System.out.println("Usage: " + usage());
            return;
        }
        String id = scanner.next();
        Solution<R> s = null;
        Population<R> p = null;
        if (sol.equals("loadsol")) {
            Scanner s2 = new Scanner(sol + " " + id);
            s = factory.read_loadsol(s2);
        }
        if (sol.equals("initsol")) {
            Scanner s2 = new Scanner(sol + " " + id);
            InitialSolution<R> initsol = factory.read_initsol(s2);
            s = initsol.generateSolution();
        }
        if (sol.equals("loadpop")) {
            Scanner s2 = new Scanner(sol + " " + id);
            p = factory.read_loadpop(s2);
        }
        if (sol.equals("initpop")) {
            Scanner s2 = new Scanner(sol + " " + id);
            InitialPopulation<R> initpop = factory.read_initpop(s2);
            int popSize = scanner.nextInt();
            p = initpop.generatePopulation(popSize);
        }
        double target_fo = scanner.nextDouble();
        double timelimit = scanner.nextDouble();
        Heuristic<R, M> method = factory.createHeuristic(scanner);
        String s_new_id = "";
        if (sol.equals("initsol") || sol.equals("loadsol")) {
            Solution<R> sFinal = method.search(s, timelimit, target_fo);
            int new_id = factory.add_loadsol(sFinal);
            s_new_id = "loadsol " + new_id;
            System.out.println("'" + s_new_id + "' added.");
        } else if (sol.equals("initpop") || sol.equals("loadpop")) {
            Population<R> pFinal;
            Population<R> pAux;
            pAux = (Population<R>) p.clone();
            pFinal = method.search(pAux, timelimit, target_fo);
            int new_id = factory.add_loadpop(pFinal);
            s_new_id = "loadpop " + new_id;
            System.out.println("'" + s_new_id + "' added.");
        }
        if (scanner.hasNext()) {
            String new_name = scanner.next();
            run_module("define", all_modules, factory, dictionary, new_name + " " + s_new_id);
        }
    }
}
