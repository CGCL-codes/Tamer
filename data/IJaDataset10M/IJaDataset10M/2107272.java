package net.sourceforge.ondex.parser.biopaxmodel;

/**
 * Implementations of this listener may be registered with instances of net.sourceforge.ondex.parser.biopaxmodel.conversion to 
 * receive notification when properties changed, added or removed.
 * <br>
 */
public interface conversionListener extends com.ibm.adtech.jastor.ThingListener {

    /**
	 * Called when a value of DATA_DASH_SOURCE has been added
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param newValue the object representing the new value
	 */
    public void DATA_DASH_SOURCEAdded(net.sourceforge.ondex.parser.biopaxmodel.conversion source, net.sourceforge.ondex.parser.biopaxmodel.dataSource newValue);

    /**
	 * Called when a value of DATA_DASH_SOURCE has been removed
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param oldValue the object representing the removed value
	 */
    public void DATA_DASH_SOURCERemoved(net.sourceforge.ondex.parser.biopaxmodel.conversion source, net.sourceforge.ondex.parser.biopaxmodel.dataSource oldValue);

    /**
	 * Called when a value of AVAILABILITY has been added
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param newValue the object representing the new value
	 */
    public void AVAILABILITYAdded(net.sourceforge.ondex.parser.biopaxmodel.conversion source, java.lang.String newValue);

    /**
	 * Called when a value of AVAILABILITY has been removed
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param oldValue the object representing the removed value
	 */
    public void AVAILABILITYRemoved(net.sourceforge.ondex.parser.biopaxmodel.conversion source, java.lang.String oldValue);

    /**
	 * Called when a value of COMMENT has been added
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param newValue the object representing the new value
	 */
    public void COMMENTAdded(net.sourceforge.ondex.parser.biopaxmodel.conversion source, java.lang.String newValue);

    /**
	 * Called when a value of COMMENT has been removed
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param oldValue the object representing the removed value
	 */
    public void COMMENTRemoved(net.sourceforge.ondex.parser.biopaxmodel.conversion source, java.lang.String oldValue);

    /**
	 * Called when SHORT_DASH_NAME has changed
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 */
    public void SHORT_DASH_NAMEChanged(net.sourceforge.ondex.parser.biopaxmodel.conversion source);

    /**
	 * Called when a value of SYNONYMS has been added
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param newValue the object representing the new value
	 */
    public void SYNONYMSAdded(net.sourceforge.ondex.parser.biopaxmodel.conversion source, java.lang.String newValue);

    /**
	 * Called when a value of SYNONYMS has been removed
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param oldValue the object representing the removed value
	 */
    public void SYNONYMSRemoved(net.sourceforge.ondex.parser.biopaxmodel.conversion source, java.lang.String oldValue);

    /**
	 * Called when NAME has changed
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 */
    public void NAMEChanged(net.sourceforge.ondex.parser.biopaxmodel.conversion source);

    /**
	 * Called when a value of XREF has been added
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param newValue the object representing the new value
	 */
    public void XREFAdded(net.sourceforge.ondex.parser.biopaxmodel.conversion source, net.sourceforge.ondex.parser.biopaxmodel.xref newValue);

    /**
	 * Called when a value of XREF has been removed
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param oldValue the object representing the removed value
	 */
    public void XREFRemoved(net.sourceforge.ondex.parser.biopaxmodel.conversion source, net.sourceforge.ondex.parser.biopaxmodel.xref oldValue);

    /**
	 * Called when a value of PARTICIPANTS has been added
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param newValue the object representing the new value
	 */
    public void PARTICIPANTSAdded(net.sourceforge.ondex.parser.biopaxmodel.conversion source, net.sourceforge.ondex.parser.biopaxmodel.physicalEntityParticipant newValue);

    /**
	 * Called when a value of PARTICIPANTS has been removed
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param oldValue the object representing the removed value
	 */
    public void PARTICIPANTSRemoved(net.sourceforge.ondex.parser.biopaxmodel.conversion source, net.sourceforge.ondex.parser.biopaxmodel.physicalEntityParticipant oldValue);

    /**
	 * Called when a value of PARTICIPANTS has been added
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param newValue the object representing the new value
	 */
    public void PARTICIPANTSAdded(net.sourceforge.ondex.parser.biopaxmodel.conversion source, net.sourceforge.ondex.parser.biopaxmodel.entity newValue);

    /**
	 * Called when a value of PARTICIPANTS has been removed
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param oldValue the object representing the removed value
	 */
    public void PARTICIPANTSRemoved(net.sourceforge.ondex.parser.biopaxmodel.conversion source, net.sourceforge.ondex.parser.biopaxmodel.entity oldValue);

    /**
	 * Called when a value of EVIDENCE has been added
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param newValue the object representing the new value
	 */
    public void EVIDENCEAdded(net.sourceforge.ondex.parser.biopaxmodel.conversion source, net.sourceforge.ondex.parser.biopaxmodel.evidence newValue);

    /**
	 * Called when a value of EVIDENCE has been removed
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param oldValue the object representing the removed value
	 */
    public void EVIDENCERemoved(net.sourceforge.ondex.parser.biopaxmodel.conversion source, net.sourceforge.ondex.parser.biopaxmodel.evidence oldValue);

    /**
	 * Called when a value of INTERACTION_DASH_TYPE has been added
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param newValue the object representing the new value
	 */
    public void INTERACTION_DASH_TYPEAdded(net.sourceforge.ondex.parser.biopaxmodel.conversion source, net.sourceforge.ondex.parser.biopaxmodel.openControlledVocabulary newValue);

    /**
	 * Called when a value of INTERACTION_DASH_TYPE has been removed
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param oldValue the object representing the removed value
	 */
    public void INTERACTION_DASH_TYPERemoved(net.sourceforge.ondex.parser.biopaxmodel.conversion source, net.sourceforge.ondex.parser.biopaxmodel.openControlledVocabulary oldValue);

    /**
	 * Called when a value of LEFT has been added
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param newValue the object representing the new value
	 */
    public void LEFTAdded(net.sourceforge.ondex.parser.biopaxmodel.conversion source, net.sourceforge.ondex.parser.biopaxmodel.physicalEntityParticipant newValue);

    /**
	 * Called when a value of LEFT has been removed
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param oldValue the object representing the removed value
	 */
    public void LEFTRemoved(net.sourceforge.ondex.parser.biopaxmodel.conversion source, net.sourceforge.ondex.parser.biopaxmodel.physicalEntityParticipant oldValue);

    /**
	 * Called when SPONTANEOUS has changed
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 */
    public void SPONTANEOUSChanged(net.sourceforge.ondex.parser.biopaxmodel.conversion source);

    /**
	 * Called when a value of RIGHT has been added
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param newValue the object representing the new value
	 */
    public void RIGHTAdded(net.sourceforge.ondex.parser.biopaxmodel.conversion source, net.sourceforge.ondex.parser.biopaxmodel.physicalEntityParticipant newValue);

    /**
	 * Called when a value of RIGHT has been removed
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.conversion
	 * @param oldValue the object representing the removed value
	 */
    public void RIGHTRemoved(net.sourceforge.ondex.parser.biopaxmodel.conversion source, net.sourceforge.ondex.parser.biopaxmodel.physicalEntityParticipant oldValue);
}
