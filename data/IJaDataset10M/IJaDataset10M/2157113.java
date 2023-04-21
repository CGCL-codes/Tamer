package JOptFrame;

import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;
import JOptFrame.Modules.BuildModule;
import JOptFrame.Modules.CheckModule;
import JOptFrame.Modules.DefineModule;
import JOptFrame.Modules.DictionaryModule;
import JOptFrame.Modules.EvaluateModule;
import JOptFrame.Modules.ExecModule;
import JOptFrame.Modules.ExportModule;
import JOptFrame.Modules.HelpModule;
import JOptFrame.Modules.PrintModule;
import JOptFrame.Modules.ReadModule;
import JOptFrame.Modules.TestModule;
import JOptFrame.Modules.UsageModule;

public class JOptFrame<R extends Representation, M extends Memory> {

    private Vector<JOptFrameModule<R, M>> modules;

    private Map<String, String> dictionary;

    public HeuristicFactory<R, M> factory;

    public JOptFrame() {
        factory = new HeuristicFactory<R, M>();
        modules = new Vector<JOptFrameModule<R, M>>();
        loadDefaultModules();
        dictionary = new HashMap<String, String>();
    }

    public JOptFrame(HeuristicFactory<R, M> f) {
        factory = f;
        modules = new Vector<JOptFrameModule<R, M>>();
        loadDefaultModules();
        dictionary = new HashMap<String, String>();
    }

    public String version() {
        return "JOptFrame - Development Version \nhttp://sourceforge.net/projects/joptframe/";
    }

    void loadModule(JOptFrameModule<R, M> module) {
        modules.add(module);
    }

    void loadDefaultModules() {
        modules.clear();
        loadModule(new BuildModule<R, M>());
        loadModule(new CheckModule<R, M>());
        loadModule(new DefineModule<R, M>());
        loadModule(new DictionaryModule<R, M>());
        loadModule(new EvaluateModule<R, M>());
        loadModule(new ExecModule<R, M>());
        loadModule(new ExportModule<R, M>());
        loadModule(new HelpModule<R, M>());
        loadModule(new PrintModule<R, M>());
        loadModule(new ReadModule<R, M>());
        loadModule(new TestModule<R, M>());
        loadModule(new UsageModule<R, M>());
    }

    JOptFrameModule<R, M> getModule(String module) {
        for (int i = 0; i < modules.size(); i++) if (module == modules.get(i).id()) return modules.get(i);
        return null;
    }

    public void execute() {
        System.out.println("Welcome to " + version());
        Scanner scanner = new Scanner(System.in);
        String line;
        while (true) {
            System.out.print(">");
            line = scanner.nextLine();
            Scanner s2 = new Scanner(line);
            String command = s2.next();
            if (command.equals("quit") || command.equals("q") || command.equals("exit")) break;
            if (command.equals("version") || command.equals("v")) {
                System.out.println(version());
                continue;
            }
            boolean notfound = true;
            for (int i = 0; i < modules.size(); i++) if (command.equals(modules.get(i).id())) {
                if (s2.hasNext()) {
                    String r = modules.get(i).preprocess(dictionary, s2.nextLine());
                    if (r.equals("INVALID_PARAM")) {
                        System.out.println("Population size has to be, at least, equal 2.");
                        notfound = false;
                        break;
                    }
                    modules.get(i).run(modules, factory, dictionary, r);
                } else if (modules.get(i).id().equals("help") || modules.get(i).id().equals("dictionary")) {
                    modules.get(i).run(modules, factory, dictionary, "");
                } else {
                    System.out.println("Usage: " + modules.get(i).usage());
                }
                notfound = false;
                break;
            }
            if (notfound) {
                System.out.println("Sorry, i couldn't understand the command '" + command + "'.");
                System.out.println("Please, type 'help' or type the command again.");
            }
        }
        System.out.println("Goodbye.");
    }

    public void execute(String line) {
        Scanner s2 = new Scanner(line);
        String command = s2.next();
        if (command.equals("quit") || command.equals("q") || command.equals("exit")) return;
        if (command.equals("version") || command.equals("v")) {
            System.out.println(version());
            return;
        }
        boolean notfound = true;
        for (int i = 0; i < modules.size(); i++) if (command.equals(modules.get(i).id())) {
            String r = modules.get(i).preprocess(dictionary, s2.toString());
            if (r.equals("INVALID_PARAM")) {
                System.out.println("Population size has to be, at least, equal 2.");
                System.exit(1);
            }
            modules.get(i).run(modules, factory, dictionary, r);
            notfound = false;
            break;
        }
        if (notfound) {
            System.out.println("Sorry, i couldn't understand the command '" + command + "'.");
            System.out.println("Please, type 'help' or type the command again.");
        }
    }
}
