package org.proclos.etlcore.component;

public abstract class Locateable extends Configurable implements ILocateable {

    private String name = "default";

    private Locator locator = new Locator();

    private ILocateable context = null;

    public abstract String getOutline();

    public ILocateable getContext() {
        return context;
    }

    public void setContext(ILocateable context) {
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocator(Locator locator) {
        this.locator = locator;
    }

    public Locator getLocator() {
        return locator;
    }
}
