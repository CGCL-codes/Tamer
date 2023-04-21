package org.sqlexp.enablement;

/**
 * Abstract class for specific database enablements.<br>
 * Implementation must provide other specific class instances :
 * entry point for all enablements.<br>
 * <br>
 * <b>Implementation of this class have to initialize
 * connectionProvider, serverStructureReader, languageStructure and queryExecutor.</b>
 * @author Matthieu Réjou
 */
public abstract class Enablement {

    private ConnectionDefinition connectionDefinition;

    private LanguageStructure languageStructure;

    private SyntaxDefinitionFactory syntaxDefinitionFactory;

    private DataManipulationFactory dataManipulationFactory;

    private DataDefinitionFactory dataDefinitionFactory;

    private ServerStructureReader serverStructureReader;

    private ServerStructureWriter serverStructureWriter;

    private QueryExecutor queryExecutor;

    protected Enablement(final ConnectionDefinition connectionDefinition, final LanguageStructure languageStructure, final SyntaxDefinitionFactory syntaxDefinitionFactory, final DataManipulationFactory dataManipulationFactory, final DataDefinitionFactory dataDefinitionFactory, final ServerStructureReader serverStructureReader, final ServerStructureWriter serverStructureWriter, final QueryExecutor queryExecutor) {
        this.connectionDefinition = connectionDefinition;
        this.dataManipulationFactory = dataManipulationFactory;
        this.dataDefinitionFactory = dataDefinitionFactory;
        this.serverStructureReader = serverStructureReader;
        this.serverStructureWriter = serverStructureWriter;
        this.languageStructure = languageStructure;
        this.syntaxDefinitionFactory = syntaxDefinitionFactory;
        this.queryExecutor = queryExecutor;
    }

    /**
	 * Gets the vendor specific connection definition.
	 * @return connection provider
	 */
    public final ConnectionDefinition getConnectionDefintion() {
        return connectionDefinition;
    }

    /**
	 * Gets the vendor specific language structure.
	 * @return the languageStructure
	 */
    public final LanguageStructure getLanguageStructure() {
        return languageStructure;
    }

    /**
	 * Gets the vendor specific syntax definition factory.
	 * @return the syntaxDefinitionFactory
	 */
    public final SyntaxDefinitionFactory getSyntaxDefinitionFactory() {
        return syntaxDefinitionFactory;
    }

    /**
	 * Gets the vendor specific data definition factory.
	 * @return the serverStructureReader
	 */
    public final DataDefinitionFactory getDataDefinitionFactory() {
        return dataDefinitionFactory;
    }

    /**
	 * Gets the vendor specific data definition factory.
	 * @return the serverStructureReader
	 */
    public final DataManipulationFactory getDataManipulationFactory() {
        return dataManipulationFactory;
    }

    /**
	 * Gets the vendor specific server structure reader.
	 * @return the serverStructureReader
	 */
    public final ServerStructureReader getServerStructureReader() {
        return serverStructureReader;
    }

    /**
	 * Gets the vendor specific server structure writer.
	 * @return the serverStructureWriter
	 */
    public final ServerStructureWriter getServerStructureWriter() {
        return serverStructureWriter;
    }

    /**
	 * Gets the vendor specific query executor.
	 * @return the queryExecutor
	 */
    public final QueryExecutor getQueryExecutor() {
        return queryExecutor;
    }

    @Override
    public final boolean equals(final Object obj) {
        return obj != null && obj.getClass() == getClass();
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }
}
