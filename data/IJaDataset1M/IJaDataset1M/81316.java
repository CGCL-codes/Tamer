package ingenias.generator.interpreter;

public interface Visitor {

    public void analyze(Object o);

    public Object getResult();
}
