package br.edu.ufcg.sri.armazenamento;

/**
 * 
 * @author Gustavo de Farias
 */
public class ArmazenadorException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7162397138946758927L;

    /**
	 * 
	 */
    public ArmazenadorException() {
    }

    /**
	 * @param arg0
	 */
    public ArmazenadorException(String arg0) {
        super(arg0);
    }

    /**
	 * @param arg0
	 */
    public ArmazenadorException(Throwable arg0) {
        super(arg0);
    }

    /**
	 * @param arg0
	 * @param arg1
	 */
    public ArmazenadorException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
