package org.jfor.jfor.rtflib.rtfdoc;

/** 
 *	
 * 	This context is used to manage the "keepn" RTF attribute
 *  Used by ParagraphBuilder and JforCmd 
 *
 */
public class ParagraphKeeptogetherContext {

    private static int m_paraKeepTogetherOpen = 0;

    private static boolean m_paraResetProperties = false;

    private static ParagraphKeeptogetherContext m_instance = null;

    ParagraphKeeptogetherContext() {
    }

    /**
	 * Singelton.
	 *
	 * @return The instance of ParagraphKeeptogetherContext
	 */
    public static ParagraphKeeptogetherContext getInstance() {
        if (m_instance == null) m_instance = new ParagraphKeeptogetherContext();
        return m_instance;
    }

    /** Return the level of current "keep whith next" paragraph */
    public static int getKeepTogetherOpenValue() {
        return m_paraKeepTogetherOpen;
    }

    /** Open a new "keep whith next" paragraph */
    public static void KeepTogetherOpen() {
        m_paraKeepTogetherOpen++;
    }

    /** Close a "keep whith next" paragraph */
    public static void KeepTogetherClose() {
        if (m_paraKeepTogetherOpen > 0) {
            m_paraKeepTogetherOpen--;
            m_paraResetProperties = (m_paraKeepTogetherOpen == 0);
        }
    }

    /** Determine if the next paragraph must reset the properites */
    public static boolean paragraphResetProperties() {
        return m_paraResetProperties;
    }

    /** Reset the flag if the paragraph properties have been resested */
    public static void setParagraphResetPropertiesUsed() {
        m_paraResetProperties = false;
    }
}
