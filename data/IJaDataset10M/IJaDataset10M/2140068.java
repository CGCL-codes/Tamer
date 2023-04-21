package tirateima.gerador;

import java.util.ArrayList;
import java.util.List;

public class CommandStartFunction extends Command {

    private String name;

    private List<Object> args;

    public CommandStartFunction(String name, List<Object> args) {
        this.name = name;
        this.args = args;
    }

    @SuppressWarnings("unchecked")
    public void execute(Gerador g) throws TiraTeimaLanguageException {
        if (!g.declared_functions.containsKey(name)) {
            gerarErro("Função '" + name + "' não declarada!");
        }
        List<Object> values = new ArrayList<Object>();
        for (Object arg : args) {
            if (arg instanceof List<?>) values.add(getValue(g, (List<Object>) arg)); else values.add(arg);
        }
        g.mostrador.startFunction(g.declared_functions.get(name).newFunction(g, values));
    }
}
