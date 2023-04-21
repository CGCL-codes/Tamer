package pl.edu.mimuw.loxim.data;

public class StructImpl extends GenericCollection {

    @Override
    public String toString() {
        return "Struct {" + data + " }";
    }

    @Override
    public <T> T mapTo(Class<T> clazz) throws IllegalArgumentException {
        return null;
    }
}
