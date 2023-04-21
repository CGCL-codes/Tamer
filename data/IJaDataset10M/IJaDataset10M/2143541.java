package CLIPSJNI;

public class SymbolValue extends PrimitiveValue {

    /****************/
    public SymbolValue() {
        super(new String(""));
    }

    /****************/
    public SymbolValue(String value) {
        super(value);
    }

    /****************/
    public String lexemeValue() throws Exception {
        return (String) getValue();
    }

    /****************/
    public String symbolValue() throws Exception {
        return (String) getValue();
    }

    /***********/
    public void retain() {
    }

    /************/
    public void release() {
    }
}
