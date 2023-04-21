package mwt.xml.xdbforms.xformlayer;

import mwt.xml.xdbforms.xformlayer.impl.XFormDocumentBuilderFactoryImpl;

/**
 * Progetto Master Web Technology
 * @author Gianfranco Murador, Cristian Castiglia, Matteo Ferri
 */
public abstract class XFormDocumentBuilderFactory {

    private static XFormDocumentBuilderFactory instance = null;

    public abstract XFormDocumentBuilder newXFormDocumentBuilder();

    public static synchronized XFormDocumentBuilderFactory newInstance() {
        if (instance == null) {
            instance = new XFormDocumentBuilderFactoryImpl();
        }
        return instance;
    }
}
