package be.djdb.mdi.window;

import javax.swing.JMenuBar;
import be.djdb.generators.Menu;
import be.djdb.mdi.Mydecorateblemdi;

/**
* @author Lieven Roegiers
* @copyright 2011
 * @patern decorator
 */
public abstract class MyabstractMultipleDocumentInterface implements Mydecorateblemdi {

    protected Mydecorateblemdi multipledocumentinterface;

    public MyabstractMultipleDocumentInterface(Mydecorateblemdi multipledocumentinterface, String resourcesbundle) {
        this.multipledocumentinterface = multipledocumentinterface;
    }

    public MyabstractMultipleDocumentInterface(Mydecorateblemdi multipledocumentinterface, Menu menu) {
        this.multipledocumentinterface = multipledocumentinterface;
    }

    public MyabstractMultipleDocumentInterface(Mydecorateblemdi multipledocumentinterface) {
        this.multipledocumentinterface = multipledocumentinterface;
    }
}
