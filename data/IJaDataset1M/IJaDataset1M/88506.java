package coffea.tools.capacities;

/**
 * Capacity of handling an element groupable in a handled group 
 * ({@link IGroupHandling})
 */
public interface IGroupableElementHandling extends IContainableElementHandling {

    public IGroupHandling getContainerHandler();
}
