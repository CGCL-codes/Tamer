package jenes.algorithms;

import java.util.HashMap;
import java.util.Map;
import jenes.population.Fitness;
import jenes.GeneticAlgorithm;
import jenes.chromosome.Chromosome;
import jenes.population.Individual;
import jenes.population.Population;
import jenes.stage.AlgorithmStage;
import jenes.stage.ExclusiveDispenser;
import jenes.stage.Parallel;
import jenes.utils.Random;

/**
 * IslandGA implements a niche based algorithm
 *
 * @version 2.0
 * @since 2.0
 */
public class IslandGA<T extends Chromosome> extends GeneticAlgorithm<T> {

    /** The number of available niches. Each niche evolve a species on one iland */
    private int niches;

    /** 
     * How many individuals have to migrate between islands at each generation
     */
    private int migration;

    /**
     * Represent the paths between islands
     */
    private Graph geography;

    /** The elitism strategy enumeration */
    public static enum ReplacementStrategy {

        /** A number of individuals is randomly chosen, and substituted */
        RANDOM, /** Worst individuals are substituted */
        WORST
    }

    ;

    /** Replacement strategy */
    private ReplacementStrategy replacement;

    /** Islands, each made of a genetic algorithm */
    private GeneticAlgorithm<T>[] islands;

    /** The default number of niches */
    public static final int DEFAULT_NICHES = 5;

    /** The default migration rate between islands */
    public static final int DEFAULT_MIGRATION = 4;

    /** The default topology connecting islands */
    public static final Graph DEFAULT_GEOGRAPHY = Graph.buildRing(DEFAULT_NICHES, true);

    /** The default replacement strategy */
    public static final ReplacementStrategy DEFAULT_REPLACEMENT = ReplacementStrategy.WORST;

    /** The parallel stage connecting islands */
    private Parallel<T> archipelago;

    /** Used to perform migration between islands */
    private Individual<T> migrants[][];

    /**
     * Creates a IslandGA instance
     * 
     * @param fitness 
     */
    public IslandGA(final Fitness fitness) {
        this(fitness, null, DEFAULT_GENERATION_LIMIT, DEFAULT_NICHES, DEFAULT_MIGRATION, DEFAULT_GEOGRAPHY, DEFAULT_REPLACEMENT);
    }

    /**
     * Creates a IslandGA instance
     * 
     * @param fitness       the fitness object
     * @param population    initial population
     * @param genlimit      the generation limit
     * @param niches        the number of niches
     * @param migration     the migration rate
     * @param geography     topology connecting islands
     * @param rp            replacement strategy
     */
    public IslandGA(final Fitness fitness, final Population<T> population) {
        this(fitness, population, DEFAULT_GENERATION_LIMIT, DEFAULT_NICHES, DEFAULT_MIGRATION, DEFAULT_GEOGRAPHY, DEFAULT_REPLACEMENT);
    }

    /**
     * Creates a IslandGA instance
     * 
     * @param fitness       the fitness object
     * @param population    initial population
     * @param genlimit      the generation limit
     */
    public IslandGA(final Fitness fitness, final Population<T> population, final int genlimit) {
        this(fitness, population, genlimit, DEFAULT_NICHES, DEFAULT_MIGRATION, DEFAULT_GEOGRAPHY, DEFAULT_REPLACEMENT);
    }

    /**
     * Creates a IslandGA instance
     * 
     * @param fitness       the fitness object
     * @param population    initial population
     * @param genlimit      the generation limit
     * @param niches        the number of niches
     */
    public IslandGA(final Fitness fitness, final Population<T> population, final int genlimit, final int niches) {
        this(fitness, population, genlimit, niches, DEFAULT_MIGRATION, DEFAULT_GEOGRAPHY, DEFAULT_REPLACEMENT);
    }

    /**
     * Creates a IslandGA instance
     * 
     * @param fitness       the fitness object
     * @param population    initial population
     * @param genlimit      the generation limit
     * @param niches        the number of niches
     * @param migration     the migration rate
     */
    public IslandGA(final Fitness fitness, final Population<T> population, final int genlimit, final int niches, final int migration) {
        this(fitness, population, genlimit, niches, migration, DEFAULT_GEOGRAPHY, DEFAULT_REPLACEMENT);
    }

    /**
     * Creates a IslandGA instance
     * 
     * @param fitness       the fitness object
     * @param population    initial population
     * @param genlimit      the generation limit
     * @param niches        the number of niches
     * @param migration     the migration rate
     * @param geography     topology connecting islands
     */
    public IslandGA(final Fitness fitness, final Population<T> population, final int genlimit, final int niches, final int migration, final Graph geography) {
        this(fitness, population, genlimit, niches, migration, geography, DEFAULT_REPLACEMENT);
    }

    /**
     * Creates a IslandGA instance
     * 
     * @param fitness       the fitness object
     * @param population    initial population
     * @param genlimit      the generation limit
     * @param niches        the number of niches
     * @param migration     the migration rate
     * @param geography     topology connecting islands
     * @param rp            replacement strategy
     */
    public IslandGA(final Fitness fitness, final Population<T> population, final int genlimit, final int niches, final int migration, final Graph geography, final ReplacementStrategy rp) {
        super(fitness, population, genlimit);
        this.niches = niches;
        this.migration = migration;
        this.geography = geography;
        this.replacement = rp;
        super.elitism = 0;
        this.migrants = new Individual[niches][migration];
        archipelago = new Parallel<T>(new ExclusiveDispenser<T>(niches) {

            private Map<Integer, Integer> map = new HashMap<Integer, Integer>();

            @Override
            public void preDistribute(Population<T> population) {
                for (Individual ind : population.getIndividuals()) {
                    Integer val = map.get(ind.getSpeciem());
                    if (val == null) {
                        val = 1;
                    } else {
                        val++;
                    }
                    map.put(ind.getSpeciem(), val);
                }
                this.map.clear();
            }

            @Override
            public int distribute(Individual<T> ind) {
                return ind.getSpeciem();
            }

            @Override
            public void preMerge(Population<T>[] branches) {
                migrate(branches);
            }
        });
        for (int i = 0; i < niches; i++) {
            this.archipelago.add(new AlgorithmStage<T>(new GeneticAlgorithm<T>()));
        }
        this.addStage(archipelago);
        this.islands = new GeneticAlgorithm[niches];
        if (this.geography.map.length != niches) {
            this.geography = Graph.buildRing(niches, true);
        }
        this.populateNiches();
    }

    /**
     * Creates a IslandGA instance with an empty fitness
     * 
     * @param fitness       the fitness object
     * @param population    initial population
     * @param genlimit      the generation limit
     * @param niches        the number of niches
     * @param algo          the algorithm to be used for all niches
     */
    public IslandGA(final Fitness fitness, final Population<T> population, final int genlimit, final int niches, final GeneticAlgorithm<T> algo) {
        this(fitness, population, genlimit, niches, algo, DEFAULT_MIGRATION, DEFAULT_GEOGRAPHY, DEFAULT_REPLACEMENT);
    }

    /**
     * Creates a IslandGA instance with an empty fitness
     * 
     * @param fitness       the fitness object
     * @param population    initial population
     * @param genlimit      the generation limit
     * @param niches        the number of niches
     * @param algo          the algorithm to be used for all niches
     * @param migration     the migration rate
     */
    public IslandGA(final Fitness fitness, final Population<T> population, final int genlimit, final int niches, final GeneticAlgorithm<T> algo, final int migration) {
        this(fitness, population, genlimit, niches, algo, migration, DEFAULT_GEOGRAPHY, DEFAULT_REPLACEMENT);
    }

    /**
     * Creates a IslandGA instance
     * 
     * @param fitness       the fitness object
     * @param population    initial population
     * @param genlimit      the generation limit
     * @param niches        the number of niches
     * @param algo          the algorithm to be used for all niches
     * @param migration     the migration rate
     * @param geography     topology connecting islands
     */
    public IslandGA(final Fitness fitness, final Population<T> population, final int genlimit, final int niches, final GeneticAlgorithm<T> algo, final int migration, final Graph geography) {
        this(fitness, population, genlimit, niches, algo, migration, geography, DEFAULT_REPLACEMENT);
    }

    /**
     * Creates a IslandGA instance
     * 
     * @param fitness       the fitness object
     * @param population    initial population
     * @param genlimit      the generation limit
     * @param niches        the number of niches
     * @param algo          the algorithm to be used for all niches
     * @param migration     the migration rate
     * @param geography     topology connecting islands
     * @param rp            replacement strategy
     */
    public IslandGA(final Fitness fitness, final Population<T> population, final int genlimit, final int niches, final GeneticAlgorithm<T> algo, final int migration, final Graph geography, final ReplacementStrategy rp) {
        this(fitness, population, genlimit, niches, migration, geography, rp);
        this.setAllIslands(algo);
    }

    /**
     * Sets the elitism for all the islands
     * @param elitism 
     */
    @Override
    public void setElitism(final int elitism) {
        for (GeneticAlgorithm<T> island : this.islands) {
            if (island != null) island.setElitism(elitism);
        }
    }

    /**
     * Sets the algorithm for a specific island
     * 
     * @param i     island index
     * @param algo  algorithm
     */
    public void setIsland(int i, GeneticAlgorithm<T> algo) {
        this.islands[i] = algo;
        this.archipelago.setBranch(i, algo != null ? new AlgorithmStage<T>(algo) : null);
    }

    /**
     * Sets the algorithm for all islands
     * 
     * @param algo  algorithm 
     */
    public void setAllIslands(GeneticAlgorithm<T> algo) {
        for (int i = 0; i < niches; ++i) {
            this.setIsland(i, algo);
        }
    }

    /**
     * Gets the list of algorithms used for the different islands
     * 
     * @return 
     */
    public GeneticAlgorithm<T>[] getAllIslands() {
        return this.islands;
    }

    /**
     * Distributes individuals among islands by setting their speciem
     */
    protected void populateNiches() {
        int len = this.initialPopulation.size();
        for (int i = 0; i < len; ++i) {
            this.initialPopulation.getIndividual(i).setSpeciem(i % this.niches);
        }
    }

    /**
     * Performs migration of individuals between islands
     * 
     * @param branches 
     */
    protected void migrate(Population<T>[] branches) {
        for (int i = 0; i < branches.length; ++i) {
            branches[i].sort();
            for (int j = 0; j < migration; ++j) {
                migrants[i][j] = branches[i].getIndividual(j);
            }
        }
        for (int sourceIsland = 0; sourceIsland < branches.length; ++sourceIsland) {
            boolean[] map = this.geography.map[sourceIsland];
            for (int targetIsland = 0; targetIsland < map.length; ++targetIsland) {
                if (map[targetIsland]) {
                    Population<T> dest = branches[targetIsland];
                    switch(this.replacement) {
                        case RANDOM:
                            for (int j = 0; j < migration; ++j) {
                                int k = Random.getInstance().nextInt(dest.size());
                                dest.getIndividual(k).setAs(migrants[sourceIsland][j]);
                            }
                            break;
                        case WORST:
                            for (int j = 0; j < migration; ++j) {
                                int k = dest.size() - 1 - j;
                                Individual<T> individual = dest.getIndividual(k);
                                int spec = individual.getSpeciem();
                                Individual immigrant = this.migrants[sourceIsland][j];
                                individual.setAs(immigrant);
                                individual.setSpeciem(spec);
                            }
                            break;
                    }
                }
            }
        }
    }

    /**
     * Return the topology connecting islands
     * 
     * @return 
     */
    public final Graph getGeography() {
        return geography;
    }

    /**
     * Sets the topology
     * 
     * @param geography 
     */
    public void setGeography(Graph geography) {
        this.geography = geography;
    }

    /**
     * Returns the migration rate
     * @return  rate
     */
    public final int getMigration() {
        return migration;
    }

    /**
     * Sets the migration rate
     * 
     * @param migration 
     */
    public void setMigration(int migration) {
        this.migration = migration;
        this.migrants = new Individual[niches][migration];
    }

    /**
     * Returns the number of niches
     * 
     * @return 
     */
    public final int getNiches() {
        return niches;
    }

    /**
     * Sets the number of niches
     * 
     * @param niches 
     */
    public void setNiches(int niches) {
        this.niches = niches;
        this.migrants = new Individual[niches][migration];
        this.populateNiches();
    }

    /**
     * Returns the replacement strategy
     * @return 
     */
    public final ReplacementStrategy getReplacement() {
        return replacement;
    }

    /**
     * Sets the replacement stategy
     * @param replacement 
     */
    public void setReplacement(ReplacementStrategy replacement) {
        this.replacement = replacement;
    }

    @Override
    public void setBiggerIsBetter(boolean flag) {
        super.setBiggerIsBetter(flag);
        for (GeneticAlgorithm<T> ga : islands) {
            ga.setBiggerIsBetter(flag);
        }
    }

    /**
     * The Graph represent the paths between islands in the algorithm landscape
     */
    public static class Graph {

        /** Connections */
        private boolean[][] map;

        /** Creates a graph */
        public Graph(int n) {
            this.map = new boolean[n][n];
        }

        /**
         * Sets a link between two nodes
         * 
         * @param from  starting node
         * @param to    ending node
         * @param flag  true, if there a link between the two nodes
         */
        public void setLink(int from, int to, boolean flag) {
            this.map[from][to] = flag;
        }

        /**
         * Return the link between two nodes
         * 
         * @param from  starting node
         * @param to    ending node
         * @return 
         */
        public boolean getLink(int from, int to) {
            return this.map[from][to];
        }

        /**
         * Builds a ring
         * 
         * @param n             number of nodes
         * @param bidirectional true, if for each edge, there is also its opposite
         * @return 
         */
        public static Graph buildRing(int n, boolean bidirectional) {
            Graph g = new Graph(n);
            for (int i = 0; i < n; ++i) {
                g.map[i][(i + 1) % n] = true;
                if (bidirectional) {
                    g.map[(i + 1) % n][i] = true;
                }
            }
            return g;
        }

        public static Graph buildGrid(int l, int m, boolean bidirectional) {
            Graph g = new Graph(l * m);
            for (int h = 0; h < l; ++h) {
                for (int k = 0; k < m; ++k) {
                    int i = h * m + k;
                    int ib = ((h + 1) % l) * m + k;
                    g.map[i][ib] = true;
                    int ir = h * m + (k + 1) % m;
                    g.map[i][ir] = true;
                    if (bidirectional) {
                        int it = ((h - 1) % l) * m + k;
                        g.map[i][ib] = true;
                        int il = h * m + (k - 1) % m;
                        g.map[i][ir] = true;
                    }
                }
            }
            return g;
        }

        public static Graph buildFull(int n) {
            Graph g = new Graph(n);
            for (int h = 0; h < n; ++h) {
                for (int k = 0; k < n; ++k) {
                    g.map[h][k] = true;
                }
                g.map[h][h] = false;
            }
            return g;
        }
    }
}
