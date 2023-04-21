package br.com.nuccitec.jfera.jdo;

public final class Query implements IQuery {

    private final String filter;

    private final String parameters;

    private final String imports;

    private final String variables;

    public Query(String filter, String parameters, String imports, String variables) {
        super();
        this.filter = filter;
        this.parameters = parameters;
        this.imports = imports;
        this.variables = variables;
    }

    @Override
    public String getFilter() {
        return filter;
    }

    @Override
    public String getParameters() {
        return parameters;
    }

    @Override
    public String getImports() {
        return imports;
    }

    @Override
    public String getVariables() {
        return variables;
    }
}
