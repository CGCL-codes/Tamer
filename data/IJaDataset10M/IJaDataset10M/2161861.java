package bfpl.gui;

public interface ZlsGuiModel {

    public static final int GLEIS_DIFF = 10;

    BfplGuiModel getBfplGuiModel();

    String getZlsName();

    /**
	 * Liefert das X-Offset für die Zuglaufstelle
	 */
    float getZuglaufstellenOffset();

    /**
	 * Liefert den Offset zur Gleisdarstellung
	 * 
	 * @param zlsName
	 * @return float
	 */
    float getGleisOffset(String zlsName);

    int getGleisCount();

    String getGleisName(int i);
}
