package org.ua.gtd.model;

import java.util.Set;

public interface ITag {

    public String getName();

    public void addComponent(IComponent component);

    public Set<IComponent> getComponents();

    public boolean hasComponent(IComponent component);
}
