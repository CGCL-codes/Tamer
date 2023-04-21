package br.org.bertol.mestrado.util;

import java.io.File;
import java.util.HashMap;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import br.org.bertol.mestrado.AbstractStarter;
import br.org.bertol.mestrado.engine.exception.HeuristicsException;
import br.org.bertol.mestrado.engine.fitness.Objective;
import br.org.bertol.mestrado.engine.optimisation.hillclimbing.Operador;
import br.org.bertol.mestrado.engine.optimisation.pso.TypeMovement;

/**
 * @author contaqualquer
 */
public final class Configurator {

    /***/
    private final transient Configuration configuration;

    /***/
    private final transient HashMap<String, AntConfiguration> antMap = new HashMap<String, AntConfiguration>();

    /***/
    private final transient HashMap<String, PsoConfiguration> psoMap = new HashMap<String, PsoConfiguration>();

    /**
     * @param sourceFile
     *            Caminho para arquivo de entrada
     * @throws ConfigurationException
     *             Erro ao carregar arquivo
     */
    public Configurator(final String sourceFile) throws ConfigurationException {
        if (sourceFile == null) {
            configuration = new PropertiesConfiguration("pratico.properties");
        } else {
            configuration = new PropertiesConfiguration(sourceFile);
        }
    }

    /**
     * @param sourceFile
     *            Caminho
     *            para arquivo de entrada
     * @throws ConfigurationException
     *             Erro ao carregar arquivo
     */
    public Configurator(final File sourceFile) throws ConfigurationException {
        configuration = new PropertiesConfiguration(sourceFile);
    }

    /**
     * @throws ConfigurationException
     *             Erro ao carregar arquivo
     */
    public Configurator() throws ConfigurationException {
        configuration = new PropertiesConfiguration("pratico.properties");
    }

    /**
     * @return Nº de starts
     */
    public int getNumRestarts() {
        final int input = configuration.getInt("restarts");
        return input;
    }

    /**
     * @return Nº de iterações
     */
    @Deprecated
    public int getIteracoes() {
        final int input = configuration.getInt("iteracoes");
        return input;
    }

    /**
     * Propertie que indica se o processamento deve ser feito de forma
     * multithread. As threads são divididas de acordo com o número de sistemas.
     * @return boolean
     */
    public boolean isMultiThread() {
        return configuration.getBoolean("multithread");
    }

    /**
     * @return Array com os sistemsa a serem processados
     */
    public String[] getSystems() {
        final String[] input = configuration.getStringArray("sistemas");
        if (input.length == 0) {
            return input;
        }
        final String[] systems = new String[input.length];
        for (int i = 0; i < input.length; i++) {
            systems[i] = input[i];
        }
        return systems;
    }

    /**
     * @return Pega caminho onde est�o os arquivos de entrada
     */
    public String getSourcePath() {
        final String input = configuration.getString("sourcepath");
        return input;
    }

    /**
     * @return Pegar arquivo de saida no formato [arquivo]
     */
    public String getOutPutFile() {
        final String input = configuration.getString("outputfile");
        return input;
    }

    /**
     * Retrona um array com os objetivos do problema. No fomato
     * [objetivo],[objetivo] é mono objetivo. Quando for [objetivo#objetivo] é
     * multi objetivo
     * @return Array com os objetivos
     */
    public Objective[][] getObjectives() {
        final String[] input = configuration.getStringArray("objetivos");
        if (input.length == 0) {
            return new Objective[][] { Objective.values() };
        }
        final Objective[][] inputTypes = new Objective[input.length][];
        for (int i = 0; i < input.length; i++) {
            final String[] t = input[i].split("#");
            if (t.length == 1) {
                inputTypes[i] = new Objective[] { Objective.valueOf(input[i]) };
            } else {
                inputTypes[i] = Objective.valueOf(t);
            }
        }
        return inputTypes;
    }

    /**
     * @return Array com os tipos de movimentos
     */
    public TypeMovement[] getTypeMovements() {
        final String[] input = configuration.getStringArray("pso.movimentos");
        if (input.length == 0) {
            return TypeMovement.values();
        }
        final TypeMovement[] typeMovements = new TypeMovement[input.length];
        for (int i = 0; i < input.length; i++) {
            typeMovements[i] = TypeMovement.valueOf(input[i]);
        }
        return typeMovements;
    }

    /**
     * @return Array com os operadores
     */
    public Operador[] getOperacao() {
        final String[] input = configuration.getStringArray("hillclimbing.operadores");
        if (input.length == 0) {
            return Operador.values();
        }
        final Operador[] operador = new Operador[input.length];
        for (int i = 0; i < input.length; i++) {
            operador[i] = Operador.valueOf(input[i]);
        }
        return operador;
    }

    /**
     * @return Buscadores a serem iniciados
     * @throws HeuristicsException
     *             Caso tenha ocorrido algum erro durante a instanciação
     */
    public AbstractStarter<?>[] getEngineStarters() throws HeuristicsException {
        try {
            final String[] input = configuration.getStringArray("starters");
            if (input.length == 0) {
                throw new ClassNotFoundException("Especificar engines a serem usadas.");
            }
            final AbstractStarter<?>[] newInstances = new AbstractStarter<?>[input.length];
            for (int i = 0; i < input.length; i++) {
                final Class<?> klass = Class.forName(input[i]);
                newInstances[i] = (AbstractStarter<?>) klass.getConstructor(Configurator.class).newInstance(this);
            }
            return newInstances;
        } catch (Exception e) {
            throw new HeuristicsException(e);
        }
    }

    /**
     * Carrega as configurações de ant para un determinado sistema.
     * @param systemID
     *            Identificação do sistema
     * @return Objeto com configurações
     */
    public AntConfiguration loadAntConfiguration(final String systemID) {
        if (antMap.get(systemID) != null) {
            return antMap.get(systemID);
        }
        final AntConfiguration antConfiguration = new AntConfiguration();
        try {
            final Configuration antConfig = new PropertiesConfiguration(systemID + ".properties");
            antConfiguration.setIteracoes(antConfig.getInt("alg.iteracoes"));
            antConfiguration.setAlpha(antConfig.getFloat("ant.alpha"));
            antConfiguration.setBeta(antConfig.getFloat("ant.beta"));
            antConfiguration.setMinpheromone(antConfig.getFloat("ant.minpheromone"));
            antConfiguration.setEvaporation(antConfig.getFloat("ant.evaporation"));
            antConfiguration.setWellThreshold(antConfig.getFloat("ant.wellthreshold"));
            antConfiguration.setSidePopulation(SidePopulation.valueOf(antConfig.getString("population.pos")));
            antConfiguration.setUsage(antConfig.getFloat("population.usage"));
        } catch (ConfigurationException e) {
            Logger.getLogger(getClass()).error("Erro ao carregar configurações para sistema", e);
            System.exit(-10);
        }
        return antConfiguration;
    }

    /**
     * Carrega as configurações de ant para un determinado sistema.
     * @param systemID
     *            Identificação do sistema
     * @return Objeto com configurações
     */
    public PsoConfiguration loadPsoConfiguration(final String systemID) {
        if (psoMap.get(systemID) != null) {
            return psoMap.get(systemID);
        }
        final PsoConfiguration psoConfiguration = new PsoConfiguration();
        return psoConfiguration;
    }
}
