package de.paragon.explorer.event;

import de.paragon.explorer.Explorer;
import de.paragon.explorer.figure.ExplorerFigure;
import de.paragon.explorer.figure.ExplorerFigureBuilder;
import de.paragon.explorer.figure.ListBoxFigure;
import de.paragon.explorer.figure.ListBoxFigureBuilder;
import de.paragon.explorer.figure.TextBoxFigure;
import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.ExplorerModelBuilder;
import de.paragon.explorer.model.ObjectModel;
import de.paragon.explorer.util.ConnectionBuilder;
import de.paragon.explorer.util.StandardEnumeration;

/**
 * Kommentar: Diese Klasse ist speziell dafuer da, Popupmenu-Events zu empfangen
 * und an die entsprechenden Builder weiterzuleiten.
 */
public class ExplorerPopupActionConverter {

    private static ExplorerPopupActionConverter singleton;

    public static ExplorerPopupActionConverter getInstance() {
        return ExplorerPopupActionConverter.getSingleton();
    }

    private static ExplorerPopupActionConverter getSingleton() {
        if (ExplorerPopupActionConverter.singleton == null) {
            ExplorerPopupActionConverter.setSingleton(new ExplorerPopupActionConverter());
        }
        return ExplorerPopupActionConverter.singleton;
    }

    private static void setSingleton(ExplorerPopupActionConverter builder) {
        ExplorerPopupActionConverter.singleton = builder;
    }

    public ExplorerPopupActionConverter() {
        super();
    }

    /**
	 * Kommentar: Diese Methode oeffnet einfach einen neuen PARAGON-Explorer
	 * fuer das Objekt, das mit dem ObjectModel der ListBoxFigure verbunden ist.
	 */
    public void exploreObjectOf(ListBoxFigure liBoFi) {
        Explorer.explore(liBoFi);
    }

    private ConnectionBuilder getConnectionBuilder() {
        return ConnectionBuilder.getInstance();
    }

    private ExplorerFigureBuilder getExplorerFigureBuilder() {
        return ExplorerFigureBuilder.getInstance();
    }

    private ExplorerModelBuilder getExplorerModelBuilder() {
        return ExplorerModelBuilder.getInstance();
    }

    private ListBoxFigureBuilder getListBoxFigureBuilder() {
        return ListBoxFigureBuilder.getInstance();
    }

    /**
	 * Diese Methode loescht eine einzelne Connection. Zunaechst werden die
	 * Referenzen auf die Connection von dem AttributeModell und von der
	 * ExplorerFigure entfernt. Dann werden die Referenzen von den beiden
	 * Objectmodellen, auf die bzw. von denen eine Referenz dargestellt wird,
	 * geloescht.
	 */
    public void hideAllReferences(ListBoxFigure liBoFi) {
        AttributeModel attrModl = null;
        StandardEnumeration attrModls = ((ObjectModel) liBoFi.getModel()).getAttributeModels();
        while (attrModls.hasMoreElements()) {
            attrModl = ((AttributeModel) attrModls.nextElement());
            this.removeConnection((TextBoxFigure) attrModl.getFigure());
        }
    }

    private void initializeListBoxFigureAndUpdateExplorerFigure(ListBoxFigure liBoFi) {
        this.getListBoxFigureBuilder().initializeListBoxFigure(liBoFi);
        this.getExplorerFigureBuilder().update((ExplorerFigure) liBoFi.getParent());
    }

    /**
	 * Kommentar: Diese Methode setzt die uebergebene ListBoxFigure nach ganz
	 * hinten und zeichnet dann alles neu.
	 */
    public void placeBehind(ListBoxFigure liBoFi) {
        this.getExplorerFigureBuilder().placeBehind(liBoFi);
        this.getExplorerFigureBuilder().update((ExplorerFigure) liBoFi.getParent());
    }

    public void removeChildListBoxFigure(ListBoxFigure liBoFi) {
        ObjectModel objModl = (ObjectModel) liBoFi.getModel();
        objModl.getExplorerModel().removeObjectModel(objModl);
        this.getExplorerFigureBuilder().removeFromExplorer((ListBoxFigure) liBoFi.getModel().getFigure());
        this.getConnectionBuilder().removeConnections(objModl);
    }

    /**
	 * Diese Methode loescht eine einzelne Connection. Zunaechst werden die
	 * Referenzen auf die Connection von dem AttributeModell und von der
	 * ExplorerFigure entfernt. Dann werden die Referenzen von den beiden
	 * Objectmodellen, auf die bzw. von denen eine Referenz dargestellt wird,
	 * geloescht.
	 */
    public void removeConnection(TextBoxFigure teBoFi) {
        this.getConnectionBuilder().removeSingleConnection((AttributeModel) teBoFi.getModel());
        ListBoxFigure liBoFi = (ListBoxFigure) teBoFi.getParent();
        this.getExplorerFigureBuilder().update((ExplorerFigure) liBoFi.getParent());
    }

    /**
	 * Ein ObjectModel wird so geloescht: Zunaechst werden die Referenzen der
	 * entsprechenden Containerobjekten auf die ListBoxFigure bzw. das
	 * Object-Model geloescht. Dann werden die Referenzen von den obigen
	 * Containerobjekten auf die Connections geloescht, ebenso die des
	 * AttributeModels und die der Object- Models. Schliesslich wird die
	 * Zeichnung geloescht, und es wird neu gezeichnet.
	 */
    public void removeListBoxFigure(ListBoxFigure liBoFi) {
        ExplorerFigure tempExplFig = (ExplorerFigure) liBoFi.getParent();
        this.getExplorerModelBuilder().removeFromExplorer((ObjectModel) liBoFi.getModel());
        if (tempExplFig != null) {
            this.getExplorerFigureBuilder().update(tempExplFig);
        }
    }

    /**
	 * Kommentar: Diese Methode geht so vor: Die Methode
	 * setAllAttributesUnvisible macht folgendes: Zunaechst werden alle
	 * AttributeModels, die in der ListBoxFigure enthalten sind, auf unvisible
	 * gesetzt. Dann wird die Weite von allen Teilen der ListBoxFigure auf 0
	 * (null) gesetzt, und die Orte werden gleich dem Ort der ListBoxFigure
	 * gesetzt. Mit allen TeilFiguren, die TextBoxFigure sind, geschieht dann
	 * folgendes: Die DisplayBox wird erneut berechnet. Die Berechnung der
	 * DisplayBox hat zur Folge, dass der Ort der Figure wieder auf 0,0 gesetzt
	 * wird. Also wird die TextBoxFigure wieder um die Position der
	 * ListBoxFigure verschoben.
	 * 
	 * Nach der Methode setAllAttributesUnvisible sind alle Voraussetzungen
	 * geschaffen, um initializeListBoxFigure anzuwenden. Diese Methode setzt
	 * alle Weiten auf ein einheit- liches Mass, naemlich das Maximum aller
	 * sicht- baren Figuren. Anschliessend werden alle Figuren je nach ihrer
	 * Erscheinung (Stelle in der Liste und (Sichtbarkeit) richtig po-
	 * sitioniert.
	 * 
	 * Schliesslich wird noch upgedated (oh Gott!), d.h. geloescht und neu
	 * gezeichnet.
	 */
    public void setAllAttributesUnvisibleOf(ListBoxFigure liBoFi) {
        this.getListBoxFigureBuilder().setAllAttributesUnvisible(liBoFi);
        this.initializeListBoxFigureAndUpdateExplorerFigure(liBoFi);
    }

    /**
	 * Kommentar: Diese Methode geht so vor: Die Methode setAllAttributesVisible
	 * macht folgendes: Zunaechst werden alle AttributeModels auf visible
	 * gesetzt. Dann wird die Weite von allen Teilen der ListBoxFigure auf 0
	 * (null) gesetzt, und die Orte werden gleich dem Ort der ListBoxFigure
	 * gesetzt. Mit allen TeilFiguren, die TextBoxFigure sind, geschieht dann
	 * folgendes: Die DisplayBox wird erneut berechnet. Die Be- rechnung der
	 * DisplayBox hat zur Folge, dass der Ort der Figure wieder auf 0,0 gesetzt
	 * wird. Also wird die TextBoxFigure wieder um die Position der
	 * ListBoxFigure verschoben.
	 * 
	 * Nach der Methode setAllAttributesUnvisible sind alle Voraussetzungen
	 * geschaffen, um initializeListBoxFigure anzuwenden. Diese Methode setzt
	 * alle Weiten auf ein einheit- liches Mass, naemlich das Maximum aller
	 * sicht- baren Figuren. Anschliessend werden alle Figuren je nach ihrer
	 * Erscheinung (Stelle in der Liste und (Sichtbarkeit) richtig po-
	 * sitioniert.
	 * 
	 * Schliesslich wird noch upgedated (oh Gott!), d.h. geloescht und neu
	 * gezeichnet.
	 */
    public void setAllAttributesVisibleOf(ListBoxFigure liBoFi) {
        this.getListBoxFigureBuilder().setAllAttributesVisible(liBoFi);
        this.initializeListBoxFigureAndUpdateExplorerFigure(liBoFi);
    }

    /**
	 * Kommentar: Diese Methode geht so vor: Die Methode
	 * setAllExploredAttributesVisible macht folgendes: Zunaechst werden alle
	 * AttributeModels, die in der ListBoxFigure enthalten sind und nicht
	 * explored sind,auf unvisible gesetzt. Dann wird die Weite von allen Teilen
	 * der ListBoxFigure auf 0 (null) gesetzt, und die Orte werden gleich dem
	 * Ort der ListBoxFigure gesetzt. Mit allen TeilFiguren, die TextBoxFigure
	 * sind, geschieht dann folgendes: Die DisplayBox wird erneut berechnet. Die
	 * Be- rechnung der DisplayBox hat zur Folge, dass der Ort der Figure wieder
	 * auf 0,0 gesetzt wird. Also wird die TextBoxFigure wieder um die Position
	 * der ListBoxFigure verschoben.
	 * 
	 * Nach der Methode setAllAttributesUnvisible sind alle Voraussetzungen
	 * geschaffen, um initializeListBoxFigure anzuwenden. Diese Methode setzt
	 * alle Weiten auf ein einheit- liches Mass, naemlich das Maximum aller
	 * sicht- baren Figuren. Anschliessend werden alle Figuren je nach ihrer
	 * Erscheinung (Stelle in der Liste und (Sichtbarkeit) richtig po-
	 * sitioniert.
	 * 
	 * Schliesslich wird noch upgedated (oh Gott!), d.h. geloescht und neu
	 * gezeichnet.
	 */
    public void setAllUnexploredAttributesUnvisibleOf(ListBoxFigure liBoFi) {
        this.getListBoxFigureBuilder().setAllUnexploredAttributesUnvisible(liBoFi);
        this.initializeListBoxFigureAndUpdateExplorerFigure(liBoFi);
    }

    /**
	 * Kommentar: Diese Methode geht so vor: Die Methode
	 * setAllExploredAttributesVisible macht folgendes: Zunaechst werden alle
	 * AttributeModels, die in der ListBoxFigure enthalten sind und nicht
	 * explored sind,auf unvisible gesetzt. Dann wird die Weite von allen Teilen
	 * der ListBoxFigure auf 0 (null) gesetzt, und die Orte werden gleich dem
	 * Ort der ListBoxFigure gesetzt. Mit allen TeilFiguren, die TextBoxFigure
	 * sind, geschieht dann folgendes: Die DisplayBox wird erneut berechnet. Die
	 * Be- rechnung der DisplayBox hat zur Folge, dass der Ort der Figure wieder
	 * auf 0,0 gesetzt wird. Also wird die TextBoxFigure wieder um die Position
	 * der ListBoxFigure verschoben.
	 * 
	 * Nach der Methode setAllAttributesUnvisible sind alle Voraussetzungen
	 * geschaffen, um initializeListBoxFigure anzuwenden. Diese Methode setzt
	 * alle Weiten auf ein einheit- liches Mass, naemlich das Maximum aller
	 * sicht- baren Figuren. Anschliessend werden alle Figuren je nach ihrer
	 * Erscheinung (Stelle in der Liste und (Sichtbarkeit) richtig po-
	 * sitioniert.
	 * 
	 * Schliesslich wird noch upgedated (oh Gott!), d.h. geloescht und neu
	 * gezeichnet.
	 */
    public void setAllUnexploredAttributesVisibleOf(ListBoxFigure liBoFi) {
        this.getListBoxFigureBuilder().setAllUnexploredAttributesVisible(liBoFi);
        this.initializeListBoxFigureAndUpdateExplorerFigure(liBoFi);
    }

    /**
	 * Kommentar: Diese Methode geht so vor: Die Methode
	 * setAllAttributesUnvisible macht folgendes: Zunaechst werden alle
	 * AttributeModels, die in der ListBoxFigure enthalten sind und keinen Wert
	 * null haben, auf unvisible gesetzt. Dann wird die Weite von allen Teilen
	 * der ListBoxFigure auf 0 (null) gesetzt, und die Orte werden gleich dem
	 * Ort der ListBoxFigure gesetzt. Mit allen TeilFiguren, die TextBoxFigure
	 * sind, geschieht dann folgendes: Die DisplayBox wird erneut berechnet. Die
	 * Be- rechnung der DisplayBox hat zur Folge, dass der Ort der Figure wieder
	 * auf 0,0 gesetzt wird. Also wird die TextBoxFigure wieder um die Position
	 * der ListBoxFigure verschoben.
	 * 
	 * Nach der Methode setAllAttributesUnvisible sind alle Voraussetzungen
	 * geschaffen, um initializeListBoxFigure anzuwenden. Diese Methode setzt
	 * alle Weiten auf ein einheit- liches Mass, naemlich das Maximum aller
	 * sicht- baren Figuren. Anschliessend werden alle Figuren je nach ihrer
	 * Erscheinung (Stelle in der Liste und (Sichtbarkeit) richtig po-
	 * sitioniert.
	 * 
	 * Schliesslich wird noch upgedated (oh Gott!), d.h. geloescht und neu
	 * gezeichnet.
	 */
    public void setNullAttributesUnvisibleOf(ListBoxFigure liBoFi) {
        this.getListBoxFigureBuilder().setNullAttributesUnvisible(liBoFi);
        this.initializeListBoxFigureAndUpdateExplorerFigure(liBoFi);
    }

    /**
	 * Kommentar: Diese Methode geht so vor: Die Methode
	 * setAllAttributesUnvisible macht folgendes: Zunaechst werden alle
	 * AttributeModels, die in der ListBoxFigure enthalten sind und keinen Wert
	 * null haben, auf unvisible gesetzt. Dann wird die Weite von allen Teilen
	 * der ListBoxFigure auf 0 (null) gesetzt, und die Orte werden gleich dem
	 * Ort der ListBoxFigure gesetzt. Mit allen TeilFiguren, die TextBoxFigure
	 * sind, geschieht dann folgendes: Die DisplayBox wird erneut berechnet. Die
	 * Be- rechnung der DisplayBox hat zur Folge, dass der Ort der Figure wieder
	 * auf 0,0 gesetzt wird. Also wird die TextBoxFigure wieder um die Position
	 * der ListBoxFigure verschoben.
	 * 
	 * Nach der Methode setAllAttributesUnvisible sind alle Voraussetzungen
	 * geschaffen, um initializeListBoxFigure anzuwenden. Diese Methode setzt
	 * alle Weiten auf ein einheit- liches Mass, naemlich das Maximum aller
	 * sicht- baren Figuren. Anschliessend werden alle Figuren je nach ihrer
	 * Erscheinung (Stelle in der Liste und (Sichtbarkeit) richtig po-
	 * sitioniert.
	 * 
	 * Schliesslich wird noch upgedated (oh Gott!), d.h. geloescht und neu
	 * gezeichnet.
	 */
    public void setNullAttributesVisibleOf(ListBoxFigure liBoFi) {
        this.getListBoxFigureBuilder().setNullAttributesVisible(liBoFi);
        this.initializeListBoxFigureAndUpdateExplorerFigure(liBoFi);
    }

    /**
	 * Kommentar: Diese Methode geht so vor: Die Methode
	 * setSingleAttributesUnvisible macht folgendes: Zunaechst wird das
	 * AttributeModel, das mit der TextBoxFigure verknuepft ist, auf unvisible
	 * gesetzt. Dann wird die Weite von allen Teilen der ListBoxFigure auf 0
	 * (null) gesetzt, und die Orte werden gleich dem Ort der ListBoxFigure
	 * gesetzt. Mit allen TeilFiguren, die TextBoxFigure sind, geschieht dann
	 * folgendes: Die DisplayBox wird erneut berechnet. Die Be- rechnung der
	 * DisplayBox hat zur Folge, dass der Ort der Figure wieder auf 0,0 gesetzt
	 * wird. Also wird die TextBoxFigure wieder um die Position der
	 * ListBoxFigure verschoben.
	 * 
	 * Nach der Methode setAllAttributesUnvisible sind alle Voraussetzungen
	 * geschaffen, um initializeListBoxFigure anzuwenden. Diese Methode setzt
	 * alle Weiten auf ein einheit- liches Mass, naemlich das Maximum aller
	 * sicht- baren Figuren. Anschliessend werden alle Figuren je nach ihrer
	 * Erscheinung (Stelle in der Liste und (Sichtbarkeit) richtig po-
	 * sitioniert.
	 * 
	 * Schliesslich wird noch upgedated (oh Gott!), d.h. geloescht und neu
	 * gezeichnet.
	 */
    public void setSingleAttributeUnvisibleOf(TextBoxFigure teBoFi) {
        ListBoxFigure liBoFi = (ListBoxFigure) teBoFi.getParent();
        this.getListBoxFigureBuilder().setSingleAttributeUnvisible(teBoFi);
        this.initializeListBoxFigureAndUpdateExplorerFigure(liBoFi);
    }

    /**
	 * Kommentar: Diese Methode geht so vor: Die Methode
	 * setAllAttributesUnvisible macht folgendes: Zunaechst werden alle
	 * AttributeModels, die in der ListBoxFigure enthalten sind und keinen Wert
	 * null haben, auf unvisible gesetzt. Dann wird die Weite von allen Teilen
	 * der ListBoxFigure auf 0 (null) gesetzt, und die Orte werden gleich dem
	 * Ort der ListBoxFigure gesetzt. Mit allen TeilFiguren, die TextBoxFigure
	 * sind, geschieht dann folgendes: Die DisplayBox wird erneut berechnet. Die
	 * Be- rechnung der DisplayBox hat zur Folge, dass der Ort der Figure wieder
	 * auf 0,0 gesetzt wird. Also wird die TextBoxFigure wieder um die Position
	 * der ListBoxFigure verschoben.
	 * 
	 * Nach der Methode setAllAttributesUnvisible sind alle Voraussetzungen
	 * geschaffen, um initializeListBoxFigure anzuwenden. Diese Methode setzt
	 * alle Weiten auf ein einheit- liches Mass, naemlich das Maximum aller
	 * sicht- baren Figuren. Anschliessend werden alle Figuren je nach ihrer
	 * Erscheinung (Stelle in der Liste und (Sichtbarkeit) richtig po-
	 * sitioniert.
	 * 
	 * Schliesslich wird noch upgedated (oh Gott!), d.h. geloescht und neu
	 * gezeichnet.
	 */
    public void setStaticAttributesUnvisibleOf(ListBoxFigure liBoFi) {
        this.getListBoxFigureBuilder().setStaticAttributesUnvisible(liBoFi);
        this.initializeListBoxFigureAndUpdateExplorerFigure(liBoFi);
    }

    /**
	 * Kommentar: Diese Methode geht so vor: Die Methode
	 * setAllAttributesUnvisible macht folgendes: Zunaechst werden alle
	 * AttributeModels, die in der ListBoxFigure enthalten sind und keinen Wert
	 * null haben, auf unvisible gesetzt. Dann wird die Weite von allen Teilen
	 * der ListBoxFigure auf 0 (null) gesetzt, und die Orte werden gleich dem
	 * Ort der ListBoxFigure gesetzt. Mit allen TeilFiguren, die TextBoxFigure
	 * sind, geschieht dann folgendes: Die DisplayBox wird erneut berechnet. Die
	 * Be- rechnung der DisplayBox hat zur Folge, dass der Ort der Figure wieder
	 * auf 0,0 gesetzt wird. Also wird die TextBoxFigure wieder um die Position
	 * der ListBoxFigure verschoben.
	 * 
	 * Nach der Methode setAllAttributesUnvisible sind alle Voraussetzungen
	 * geschaffen, um initializeListBoxFigure anzuwenden. Diese Methode setzt
	 * alle Weiten auf ein einheit- liches Mass, naemlich das Maximum aller
	 * sicht- baren Figuren. Anschliessend werden alle Figuren je nach ihrer
	 * Erscheinung (Stelle in der Liste und (Sichtbarkeit) richtig po-
	 * sitioniert.
	 * 
	 * Schliesslich wird noch upgedated (oh Gott!), d.h. geloescht und neu
	 * gezeichnet.
	 */
    public void setStaticAttributesVisibleOf(ListBoxFigure liBoFi) {
        this.getListBoxFigureBuilder().setStaticAttributesVisible(liBoFi);
        this.initializeListBoxFigureAndUpdateExplorerFigure(liBoFi);
    }
}
