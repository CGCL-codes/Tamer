package syntelos.dom;

/**
 * 
 * 
 * @author (jdp)
 * @since 1.5
 */
public class Text extends Node implements org.w3c.dom.Text {

    protected Text() {
        super(Name.Type.Text.Instance);
    }

    public Text(String value) {
        this();
        this.value = value;
    }

    protected Text(Name.Type name) {
        super(name);
    }

    protected Text(Name.Type name, String value) {
        super(name);
        this.value = value;
    }
}
