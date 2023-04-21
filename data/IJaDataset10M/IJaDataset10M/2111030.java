package ncbworld;

public class World {

    private int cycle;

    private int nEvaluations;

    private int nMAXcycles;

    private Population population;

    private boolean show;

    private String file;

    private FileWriter fw;

    private static World INSTANCE = null;

    private static synchronized void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new World();
        }
    }

    public static World getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }

    private World() {
        cycle = 0;
        nMAXcycles = 50;
        nEvaluations = 0;
        file = "results.txt";
    }

    public void cyclemm() {
        this.cycle++;
    }

    private void death() {
        this.population.death();
    }

    public int getCycle() {
        return cycle;
    }

    public int getNEvaluations() {
        return nEvaluations;
    }

    public int getNMAXcycles() {
        return nMAXcycles;
    }

    public void init() {
        fw = new FileWriter(file);
        cycle = 0;
        nEvaluations = 0;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void nEvaluationsmm() {
        this.nEvaluations++;
    }

    private void printCycle() {
        fw.println(cycle + " " + population.bestToString());
    }

    public void printBestChangeCycle() {
        fw.println(cycle + " " + population.bestToString());
    }

    private void printResults() {
        fw.println("\n\nResultados");
        fw.println(population.bestToString());
        fw.println("\n Evaluations: " + nEvaluations);
    }

    private void reproduction() {
        this.population.reproduction();
    }

    public int getLastUpdate() {
        return population.getLastUpdate();
    }

    public void runSinglePopulation(int nMAXCycles) {
        init();
        printWorld(nMAXCycles);
        printMutatorAgent();
        initPopulation();
        while (cycle <= nMAXcycles) {
            cyclemm();
            selection();
            reproduction();
            death();
        }
        printResults();
        close();
    }

    private void printWorld(int cycles) {
        StringBuilder sb = new StringBuilder();
        sb.append("Sigle: ");
        sb.append(cycles);
        fw.println(sb);
    }

    public void runMultiPopulation(int populations, int nMaxCyclesPopulations, int nMaxCyclesFinalPopulation) {
        init();
        printWorld(populations, nMaxCyclesPopulations, nMaxCyclesFinalPopulation);
        printMutatorAgent();
        Entity[] pe = new Entity[populations];
        for (int i = 0; i < pe.length; i++) {
            cycle = 0;
            System.out.println("Initial Population " + i);
            fw.println("\nInitial Population " + i);
            initPopulation();
            while (cycle <= nMaxCyclesPopulations) {
                cyclemm();
                selection();
                reproduction();
                death();
            }
            pe[i] = population.getBestEverEntity();
        }
        cycle = 0;
        System.out.println("\nFinal Population");
        population.setInitialPopulation(pe);
        fw.println("\n");
        fw.println(population);
        fw.println("Final Population");
        while (cycle <= nMaxCyclesFinalPopulation) {
            cyclemm();
            selection();
            reproduction();
            death();
        }
        printResults();
        close();
    }

    private void printWorld(int populations, int maxCyclesPopulations, int maxCyclesFinalPopulation) {
        StringBuilder sb = new StringBuilder();
        sb.append("Multi: p: ");
        sb.append(populations);
        sb.append(" mcp: ");
        sb.append(maxCyclesPopulations);
        sb.append(" mcfp: ");
        sb.append(maxCyclesFinalPopulation);
        fw.println(sb);
    }

    private void initPopulation() {
        population.initPopulation();
    }

    private void printMutatorAgent() {
        fw.println(MutatorAgent.getInstance());
    }

    private void close() {
        fw.close();
    }

    private void selection() {
        this.population.selection();
    }

    public void setNMAXcycles(int xcycles) {
        nMAXcycles = xcycles;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
