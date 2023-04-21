package suneido.language;

public class BuiltinFunction extends SuFunction {

    @Override
    public String typeName() {
        return "Builtin";
    }

    @Override
    public Object call(Object self, Object... args) {
        return null;
    }
}
