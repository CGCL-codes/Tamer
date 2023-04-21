package gov.lanl.CoasViewers;

import gov.lanl.Utility.XML;

/**
 * Has trait names for OpenEMed not in XML.java; need to move all
 * of the ones used to a config file!
 * @author Jim George
 * @version $Revision: 3353 $ $Date: 2006-03-27 20:32:36 -0500 (Mon, 27 Mar 2006) $
 */
public interface MXML {

    static final String SPECIMENSOURCE = org.omg.DsObservationQualifiers.SpecimenSource.value;

    static final String ORDERINGPROVIDER = org.omg.DsObservationQualifiers.OrderingProvider.value;

    static final String RESPONSIBLEOBSERVER = org.omg.DsObservationQualifiers.ResponsibleObserver.value;

    static final String PRINCIPALRESULTINTERPRETER = org.omg.DsObservationQualifiers.PrincipalResultInterpreter.value;

    static final String NOTE = "DNS:OpenEMed.org/TraitCode/Note";

    static final String REFERENCEID = org.omg.DsObservationQualifiers.ObservationIdentifier.value;

    static final String OBSIDCODE = XML.ObjId;

    static final String DATERECORDED = XML.DateTimeRecorded;
}
