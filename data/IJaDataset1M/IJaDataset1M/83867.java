package annone.local.linker;

import java.util.List;

class Type {

    private final String name;

    private final List<Type> concretes;

    public Type(String name, List<Type> concretes) {
        this.name = name;
        this.concretes = concretes;
    }

    public String getName() {
        return name;
    }

    public List<Type> getConcretes() {
        return concretes;
    }
}
