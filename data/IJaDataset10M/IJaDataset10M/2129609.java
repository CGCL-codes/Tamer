package net.sourceforge.jsfannonition.resourceresolver;

public interface ResourceResolver {

    Object resolve(String name, String className);
}
