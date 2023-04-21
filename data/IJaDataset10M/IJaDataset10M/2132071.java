package net.sf.blunder.util.persistence.translator;

/**
 * Blunder translator generic interface.
 * 
 * @author <a href="mailto:lambrosi@users.sourceforge.net">Ambrosi Lucas</a>
 */
public interface ITranslator {

    /**
    * Translate from a persistent service to another.
    */
    public void translate();
}
