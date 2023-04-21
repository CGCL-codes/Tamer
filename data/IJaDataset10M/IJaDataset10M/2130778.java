package layer.disseminator.segmentationReassembly;

/**
 * 
 * @author Yark Schroeder, Manuel Scholz
 *
 */
public interface SPProperties {

    /**
	 * Gibt das Fassungsverm�gen des Puffers an, der Dateinheiten f�r das
	 * Weiterleiten an die unten angrenzende Schicht zwischenpuffert.
	 */
    public static final String SP_OutgoingDataUnitBufferSize = "SP_OutgoingDataUnitBufferSize";

    /**
	 * Gibt das Fassungsverm�gen des Puffers an, der Dateinheiten f�r das
	 * Weiterleiten an die oben angrenzende Schicht zwischenpuffert.
	 */
    public static final String SP_IncomingDataUnitBufferSize = "SP_IncomingDataUnitBufferSize";

    /**
	 * Ganzzahliger Parameter, der in ms angibt, nach welcher Zeit der
	 * Garbagecollector anl�uft.
	 */
    public static final String SP_CollectGarbageTime = "SP_CollectGarbageTime";

    /**
	 * Ganzzahliger Parameter, der in ms angibt, wie lange ein Pufferzugriff her
	 * sein muss, bevor der Garbagecollector den Puffer l�scht.
	 */
    public static final String SP_IdleTime = "SP_IdleTime";

    /**
	 * Ganzzahliger Parameter, der angibt, wie gro� die Kapazit�t der
	 * Zwischenablage f�r Segmente ist.
	 */
    public static final String SP_SegmentCacheSize = "SP_SegmentCacheSize";

    /**
	 * Ganzzahliger Parameter, der angibt, nach welcher Anzahl von
	 * Fragmentsequenzen die Vergabe der Identifikationsnummern wieder bei Null
	 * beginnt.
	 */
    public static final String SP_IdentificationRange = "SP_IdentificationRange";

    /**
	 * Ganzzahliger Parameter, der die maximale Anzahl von Segmenten angibt,
	 * aus der eine Dateneinheit bestehen darf.
	 */
    public static final String SP_NumberOfSegments = "SP_NumberOfSegments";

    /**
	 * Ganzzahliger Parameter, der angibt, wie gro�, in Byte, der Datenanteil
	 * sein darf.
	 */
    public static final String SP_DataFractionSize = "SP_DataFractionSize";

    /**
	 * Gibt an, welches der Protokolle der darunterliegenden Schicht benutzt
	 * wird.
	 */
    public static final String SP_UtilizedBottomLayerProtocol = "SP_UtilizedBottomLayerProtocol";
}
