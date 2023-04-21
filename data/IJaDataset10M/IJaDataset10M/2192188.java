package atg.manager;

public class ProjectException extends Exception {

    public ProjectException(String pString) {
        super(pString);
    }

    public ProjectException(String pMessage, Exception pE) {
        super(pMessage, pE);
    }

    /**
   * 
   */
    private static final long serialVersionUID = -2161918689842977653L;
}
