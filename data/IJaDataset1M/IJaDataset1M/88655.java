package org.strategyca.editor;

/**
 * @author Adolfo
 *
 */
public class Editor {

    private static Editor editor = null;

    /** 
	 * Constructor method 
	 */
    private Editor() {
        super();
    }

    /**
	 * @param args The command line arguments
	 */
    public static void main(final String[] args) {
    }

    /** 
	 * Singleton instance method 
	 * @return The editor instance
	 */
    public static Editor getEditor() {
        if (editor == null) {
            editor = new Editor();
            editor.init();
        }
        return editor;
    }

    /** 
	 * Singleton instance method 
	 */
    private void init() {
    }
}
