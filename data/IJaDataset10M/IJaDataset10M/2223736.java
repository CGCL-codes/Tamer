package de.javacus.grafmach.twoD;

/**
 * Grafik-Objekt, moeglichst einfach. Dieses Objekt kann sich nicht selber
 * zeichnen.
 * 
 * @author 2005 by Burkhard Loesel, www.spirit-and-emotion.de
 */
public interface IBasicGO {

    /**
	 * die Namen der elementaren Grafik-Elemente sichtbar machen, zum Debuggen.
	 */
    public void setGrafNamesVisible(boolean grafNamesVisible);

    public boolean getGrafNamesVisible();

    public void setGrafName(String grafName);

    public String getGrafName();

    /** 
	 * die bounding box um das Element.
	 * @return
	 */
    public IRect getLimits();

    public IBasicGO copyDeepFrom(IBasicGO iBasicGOSource);
}
