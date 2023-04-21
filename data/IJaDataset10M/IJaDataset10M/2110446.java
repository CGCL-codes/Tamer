package net.sf.easyactionj.gui.execption;

/**
 * Define a exceção, caso um componente chamado pelo finder não esteja
 * registrado.
 *
 * @see net.sf.easyactionj.gui.utils.ComponentFinder#getComponentByName(java.lang.String)
 * @see net.sf.easyactionj.gui.utils.ComponentFinder#getComponentsByName(java.lang.String)
 * 
 * @author flaviocorrea
 */
public class ComponentNotFoundException extends RuntimeException {

    public ComponentNotFoundException(String string) {
        super(string);
    }

    public ComponentNotFoundException(Throwable arg0) {
        super(arg0);
    }

    public ComponentNotFoundException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
