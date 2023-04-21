package mimosa.ontology;

/**
 *
 * @author Jean-Pierre Muller
 */
public interface ConcreteModelChangeListener {

    public void entityAdded(ConcreteModelChangeEvent event);

    public void entityRemoved(ConcreteModelChangeEvent event);

    public void linkAdded(ConcreteModelChangeEvent event);

    public void linkRemoved(ConcreteModelChangeEvent event);

    public void outputAdded(ConcreteModelChangeEvent event);

    public void outputRemoved(ConcreteModelChangeEvent event);

    public void outputLinkAdded(ConcreteModelChangeEvent event);

    public void outputLinkRemoved(ConcreteModelChangeEvent event);
}
