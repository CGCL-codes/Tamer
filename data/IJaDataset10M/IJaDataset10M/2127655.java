package net.sourceforge.ondex.core;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.naming.OperationNotSupportedException;
import net.sourceforge.ondex.config.Config;
import net.sourceforge.ondex.core.security.perm.Access;
import net.sourceforge.ondex.core.security.perm.AccessScope;
import net.sourceforge.ondex.core.security.perm.GlobalPermissions;
import net.sourceforge.ondex.core.security.perm.Permission;
import static net.sourceforge.ondex.core.security.perm.Permission.*;
import net.sourceforge.ondex.core.security.perm.PermissionInit;
import net.sourceforge.ondex.event.type.AccessDeniedEvent;
import net.sourceforge.ondex.event.type.EmptyStringEvent;
import net.sourceforge.ondex.event.type.NullValueEvent;
import net.sourceforge.ondex.event.type.SerialisationFailedEvent;
import net.sourceforge.ondex.exception.type.AccessDeniedException;
import net.sourceforge.ondex.exception.type.DataLossException;
import net.sourceforge.ondex.exception.type.EmptyStringException;
import net.sourceforge.ondex.exception.type.NullValueException;
import net.sourceforge.ondex.exception.type.StorageException;
import net.sourceforge.ondex.exception.type.WrongParameterException;

/**
 * This class represents a ONDEX Concept. It can have 0..* ConceptNames, 0..*
 * ConceptAccessions and 0..* ConceptGDS. ConceptNames, ConceptAccessions and
 * ConceptGDS can be added with the create*-methods.
 * 
 * @author sierenk, taubertj
 * 
 */
public abstract class AbstractConcept extends AbstractONDEXEntity implements Viewable {

    /**
	 * Reference to parent AbstractONDEXGraph. Necessary for the lazy access to
	 * meta data associated with the AbstractConcept, e.g. CV for elementOf.
	 */
    protected AbstractONDEXGraph aog;

    /**
	 * Primary unique id for this AbstractConcept within the current
	 * AbstractONDEXGraph. This id is assigned at creation time by the
	 * AbstractONDEXGraph by simply counting up. An id doesn't get reassigned at
	 * any time, so the maximum number of concepts in a graph is
	 * Integer.MAX_VALUE.
	 */
    private Integer id;

    /**
	 * A arbitrary id for this AbstractConcept, which could be assigned by a
	 * parser or any other means. This is not required and can be null. The
	 * intension of this id is for debugging or associating an id from a data
	 * source with this AbstractConcept.
	 */
    private String pid;

    /**
	 * Relevant annotation for this AbstractConcept, which is a short
	 * significant human readable description.
	 */
    private String annotation;

    /**
	 * Any longer description String for this AbstractConcept. Should be
	 * distinct from annotation.
	 */
    private String description;

    /**
	 * CV (data source) to which this AbstractConcept belongs to. The actual
	 * instance for CV.
	 */
    private CV elementOf;

    /**
	 * String id representing elementOf CV used for lazy initialisation of
	 * elementOf field.
	 */
    private String elementOfS;

    /**
	 * ConceptClass of which this AbstractConcept belongs to. The actual
	 * instance for ConceptClass.
	 */
    private ConceptClass ofType;

    /**
	 * String id representing ofType ConceptClass used for lazy initialisation
	 * of ofType field.
	 */
    private String ofTypeS;

    /**
	 * Constructor which fills all fields of this AbstractConcept and
	 * initialises internal data structures.
	 * 
	 * @param sid
	 *            unique id associated with parent AbstartONDEXGraph
	 * @param aog
	 *            parent AbstractONDEXGraph
	 * @param id
	 *            unique ID of this AbstractConcept
	 * @param pid
	 *            parser assigned ID of this AbstractConcept
	 * @param annotation
	 *            relevant annotations of this AbstractConcept
	 * @param description
	 *            every associated description of this AbstractConcept
	 * @param elementOf
	 *            CV (data source) to which this AbstractConcept belongs to
	 * @param ofType
	 *            ConceptClass of this AbtractConcept
	 * @throws AccessDeniedException if elementOf or ofType cannot be read.
	 */
    @PermissionInit
    protected AbstractConcept(long sid, AbstractONDEXGraph aog, Integer id, String pid, String annotation, String description, CV elementOf, ConceptClass ofType) {
        this(sid, aog, id, pid, annotation, description, elementOf.getId(), ofType.getId());
        this.elementOf = elementOf;
        this.ofType = ofType;
    }

    @PermissionInit
    protected AbstractConcept(long sid, AbstractONDEXGraph aog, Integer id, String pid, String annotation, String description, String elementOfS, String ofTypeS) {
        this.sid = sid;
        this.aog = aog;
        this.id = id;
        this.pid = pid;
        this.annotation = annotation;
        this.description = description;
        this.elementOfS = elementOfS.intern();
        this.ofTypeS = ofTypeS.intern();
    }

    /**
	 * Returns a byte array serialisation of this class.
	 * 
	 * @return serialisation in the form of byte[]
	 */
    @Override
    public byte[] serialise() {
        return serialise(this.sid);
    }

    /**
	 * Returns a byte array serialisation of this class.
	 * 
	 * @param sid
	 *            override current sid
	 * @return serialisation in the form of byte[]
	 */
    public byte[] serialise(long sid) {
        byte[] retVal = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(baos));
            byte[] array = GlobalPermissions.getInstance(getSID()).serializeConcept(getId());
            dos.writeInt(array.length);
            dos.write(array);
            array = null;
            dos.writeLong(sid);
            dos.writeInt(id.intValue());
            dos.writeUTF(pid);
            dos.writeUTF(annotation);
            dos.writeUTF(description);
            dos.writeUTF(elementOfS);
            dos.writeUTF(ofTypeS);
            dos.flush();
            retVal = baos.toByteArray();
            dos.close();
            dos = null;
            baos.close();
            baos = null;
        } catch (IOException ioe) {
            fireEventOccurred(new SerialisationFailedEvent(ioe.getMessage(), "[AbstractConcept - serialise]"));
            throw new StorageException();
        }
        return retVal;
    }

    /**
	 * Creates a new ConceptName with the given name. Then adds the new
	 * ConceptName to the list of ConceptNames of this AbstractConcept.
	 * 
	 * @param name
	 *            name of the new ConceptName
	 * @return new ConceptName
	 * @throws NullValueException if the given name parameter is null.
	 * @throws AccessDeniedException if the permissions do not allow modifying the concept.
	 * @throws EmptyStringException if the given name parameter is an empty string.
	 */
    @Access(CREATE)
    public ConceptName createConceptName(String name) {
        return createConceptName(name, false);
    }

    /**
	 * Creates a new ConceptName with the given name and the information if this
	 * name is preferred. Then adds the new ConceptName to the list of
	 * ConceptNames of this Concept.
	 * 
	 * @param name
	 *            name of the new ConceptName
	 * @param isPreferred
	 *            is name preferred?
	 * @return new ConceptName
	 */
    @Access(CREATE)
    public ConceptName createConceptName(String name, boolean isPreferred) {
        try {
            if (name == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.ConceptNameNameNull"), "[AbstractConcept - createConceptName]"));
                throw new NullValueException(Config.properties.getProperty("AbstractConcept.ConceptNameNameNull"));
            } else if (name.trim().length() == 0) {
                fireEventOccurred(new EmptyStringEvent(Config.properties.getProperty("AbstractConcept.ConceptNameNameEmpty"), "[AbstractConcept - createConceptName]"));
                throw new EmptyStringException(Config.properties.getProperty("AbstractConcept.ConceptNameNameEmpty"));
            } else {
                return storeConceptName(new ConceptName(sid, id, name, isPreferred));
            }
        } catch (OperationNotSupportedException onse) {
            fireEventOccurred(new AccessDeniedEvent(Config.properties.getProperty("AbstractConcept.ReadOnly"), "[AbstractConcept - createConceptName]"));
            throw new AccessDeniedException(Config.properties.getProperty("AbstractConcept.ReadOnly"));
        }
    }

    /**
	 * Deletes a ConceptName with the given name and returns the deleted one or
	 * null if unsuccessful.
	 * 
	 * @param name
	 *            name of ConceptName to be deleted
	 * @return deleted ConceptName
	 * @throws AccessDeniedException if no permission to delete concept.
	 * @throws NullValueException if the name parameter is null.
	 * @throws EmptyStringException  if the name parameter is an empty string.
	 */
    @Access(DELETE)
    public ConceptName deleteConceptName(String name) {
        try {
            if (name == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.ConceptNameNameNull"), "[AbstractConcept - deleteConceptName]"));
                throw new NullValueException(Config.properties.getProperty("AbstractConcept.ConceptNameNameNull"));
            } else if (name.trim().length() == 0) {
                fireEventOccurred(new EmptyStringEvent(Config.properties.getProperty("AbstractConcept.ConceptNameNameEmpty"), "[AbstractConcept - deleteConceptName]"));
                throw new EmptyStringException(Config.properties.getProperty("AbstractConcept.ConceptNameNameEmpty"));
            } else {
                return removeConceptName(name);
            }
        } catch (OperationNotSupportedException onse) {
            fireEventOccurred(new AccessDeniedEvent(Config.properties.getProperty("AbstractConcept.ReadOnly"), "[AbstractConcept - deleteConceptName]"));
            throw new AccessDeniedException(Config.properties.getProperty("AbstractConcept.ReadOnly"));
        }
    }

    /**
	 * Returns the preferred ConceptName or null if non is present.
	 * 
	 * @return preferred ConceptName
	 * @throws AccessDeniedException if no permission to read.
	 */
    @Access(GET)
    public ConceptName getConceptName() {
        return preferredConceptName();
    }

    /**
	 * Returns a ConceptName or null if unsuccessful for a given name or null if
	 * unsuccessful.
	 * 
	 * @param name
	 *            name of ConceptName to be returned
	 * @return existing ConceptName
	 * @throws AccessDeniedException if no permission to read.
	 * @throws NullValueException if name parameter is null.
	 * @throws EmptyStringException if name parameter is an empty string.
	 */
    @Access(GET)
    public ConceptName getConceptName(String name) {
        if (name == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.ConceptNameNameNull"), "[AbstractConcept - getConceptName]"));
            throw new NullValueException(Config.properties.getProperty("AbstractConcept.ConceptNameNameNull"));
        } else if (name.trim().length() == 0) {
            fireEventOccurred(new EmptyStringEvent(Config.properties.getProperty("AbstractConcept.ConceptNameNameEmpty"), "[AbstractConcept - getConceptName]"));
            throw new EmptyStringException(Config.properties.getProperty("AbstractConcept.ConceptNameNameEmpty"));
        } else {
            return retrieveConceptName(name);
        }
    }

    /**
	 * Returns all ConceptNames contained in the list of ConceptNames.
	 * 
	 * @return all ConceptNames as AbstractONDEXIterator<ConceptName>
	 * @throws AccessDeniedException if no permission.
	 */
    @Access(GET)
    public AbstractONDEXIterator<ConceptName> getConceptNames() {
        return retrieveConceptNameAll();
    }

    /**
	 * Creates a new ConceptAccession with the given accession and the
	 * information which CV it belongs to. The ConceptAccession will be
	 * ambiguous. Then adds the new ConceptAccession to the list of
	 * ConceptAccession of this AbstractConcept.
	 * 
	 * @param accession
	 *            accession of the new ConceptAccession
	 * @param elementOf
	 *            control vocabulary CV
	 * @return new ConceptAccession
	 * @throws AccessDeniedException if no permission.
	 * @throws NullValueException if accession or elementOf parameters is null.
	 * @throws EmptyStringException if accession parameter is an empty string.
	 */
    @Access(CREATE)
    public ConceptAccession createConceptAccession(String accession, CV elementOf) {
        return createConceptAccession(accession, elementOf, true);
    }

    /**
	 * Creates a new ConceptAccession with the given accession, the information
	 * which CV it belongs to and if its accession is ambiguous. Then adds the
	 * new ConceptAccession to the list of ConceptAccessions of this
	 * AbstractConcept.
	 * 
	 * @param accession
	 *            accession of the new ConceptAccession
	 * @param elementOf
	 *            control vocabulary CV
	 * @param ambiguous
	 *            is ambigous accession
	 * @return new ConceptAccession
	 * @throws AccessDeniedException if no permission.
	 * @throws NullValueException if accession or elementOf parameters is null.
	 * @throws EmptyStringException if accession parameter is an empty string.
	 */
    @Access(CREATE)
    public ConceptAccession createConceptAccession(String accession, CV elementOf, boolean ambiguous) {
        try {
            if (accession == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.ConceptAccessionAccessionNull"), "[AbstractConcept - createConceptAccession]"));
                throw new NullValueException(Config.properties.getProperty("AbstractConcept.ConceptAccessionAccessionNull"));
            } else if (accession.trim().length() == 0) {
                fireEventOccurred(new EmptyStringEvent(Config.properties.getProperty("AbstractConcept.ConceptAccessionAccessionEmpty"), "[AbstractConcept - createConceptAccession]"));
                throw new EmptyStringException(Config.properties.getProperty("AbstractConcept.ConceptAccessionAccessionEmpty"));
            } else if (elementOf == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.ConceptAccessionElementOfNull"), "[AbstractConcept - createConceptAccession]"));
                throw new NullValueException(Config.properties.getProperty("AbstractConcept.ConceptAccessionElementOfNull"));
            } else {
                return storeConceptAccession(new ConceptAccession(sid, id, aog, accession, elementOf, ambiguous));
            }
        } catch (OperationNotSupportedException onse) {
            fireEventOccurred(new AccessDeniedEvent(Config.properties.getProperty("AbstractConcept.ReadOnly"), "[AbstractConcept - createConceptAccession]"));
            throw new AccessDeniedException(Config.properties.getProperty("AbstractConcept.ReadOnly"));
        }
    }

    /**
	 * Deletes a ConceptAccession specified by accession and elementOf from the
	 * list of ConceptAccession of this AbstractConcept. Returns the removed
	 * ConceptAccession or null if unsuccessful.
	 * 
	 * @param accession
	 *            accession of ConceptAccession to be deleted
	 * @param elementOf
	 *            CV of ConceptAccession to be deleted
	 * @return deleted ConceptAccession
	 * @throws AccessDeniedException if no permission.
	 * @throws NullValueException if parameter is null.
	 * @throws EmptyStringException if parameter is empty string.
	 */
    @Access(DELETE)
    public ConceptAccession deleteConceptAccession(String accession, CV elementOf) {
        try {
            if (accession == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.ConceptAccessionAccessionNull"), "[AbstractConcept - deleteConceptAccession]"));
                throw new NullValueException(Config.properties.getProperty("AbstractConcept.ConceptAccessionAccessionNull"));
            } else if (accession.trim().length() == 0) {
                fireEventOccurred(new EmptyStringEvent(Config.properties.getProperty("AbstractConcept.ConceptAccessionAccessionEmpty"), "[AbstractConcept - deleteConceptAccession]"));
                throw new EmptyStringException(Config.properties.getProperty("AbstractConcept.ConceptAccessionAccessionEmpty"));
            } else if (elementOf == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.ConceptAccessionElementOfNull"), "[AbstractConcept - deleteConceptAccession]"));
                throw new NullValueException(Config.properties.getProperty("AbstractConcept.ConceptAccessionElementOfNull"));
            } else {
                return removeConceptAccession(accession, elementOf);
            }
        } catch (OperationNotSupportedException onse) {
            fireEventOccurred(new AccessDeniedEvent(Config.properties.getProperty("AbstractConcept.ReadOnly"), "[AbstractConcept - deleteConceptAccession]"));
            throw new AccessDeniedException(Config.properties.getProperty("AbstractConcept.ReadOnly"));
        }
    }

    /**
	 * Returns a ConceptAccession or null if unsuccessful for a given accession
	 * and CV or null if unsuccessful.
	 * 
	 * @param accession
	 *            accession of ConceptAccession to be returned
	 * @param elementOf
	 *            CV of ConceptAccession to be returned
	 * @return existing ConceptAccession
	 * @throws AccessDeniedException if no permission.
	 * @throws NullValueException if parameter is null.
	 * @throws EmptyStringException if parameter is an empty string.
	 */
    @Access(GET)
    public ConceptAccession getConceptAccession(String accession, CV elementOf) {
        if (accession == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.ConceptAccessionAccessionNull"), "[AbstractConcept - getConceptAccession]"));
            throw new NullValueException(Config.properties.getProperty("AbstractConcept.ConceptNameNameNull"));
        } else if (accession.trim().length() == 0) {
            fireEventOccurred(new EmptyStringEvent(Config.properties.getProperty("AbstractConcept.ConceptAccessionAccessionEmpty"), "[AbstractConcept - getConceptAccession]"));
            throw new EmptyStringException(Config.properties.getProperty("AbstractConcept.ConceptAccessionAccessionEmpty"));
        } else if (elementOf == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.ConceptAccessionElementOfNull"), "[AbstractConcept - getConceptAccession]"));
            throw new NullValueException(Config.properties.getProperty("AbstractConcept.ConceptAccessionElementOfNull"));
        } else {
            return retrieveConceptAccession(accession, elementOf);
        }
    }

    /**
	 * Returns all ConceptAccessions contained in the list of ConceptAccessions.
	 * 
	 * @return all ConceptAccessions as AbstractONDEXIterator<ConceptAccession>
	 * @throws AccessDeniedException if no permission.
	 */
    @Access(GET)
    public AbstractONDEXIterator<ConceptAccession> getConceptAccessions() {
        return retrieveConceptAccessionAll();
    }

    /**
	 * Creates a new ConceptGDS with the given AttributeName and value. Adds the
	 * new ConceptGDS to the list of ConceptGDS of this AbstractConcept.
	 * 
	 * @param attrname
	 *            the AttributeName of the new ConceptGDS
	 * @param value
	 *            the Object value of the new ConceptGDS
	 * @param doIndex
	 *            whether or not to index the new ConceptGDS
	 * @return new ConceptGDS
	 * @throws AccessDeniedException if no permission.
	 * @throws NullValueException if parameter is null.
	 */
    @Access(CREATE)
    public ConceptGDS createConceptGDS(AttributeName attrname, Object value, boolean doIndex) throws AccessDeniedException, NullValueException {
        try {
            if (attrname == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.ConceptGDSAttributeNameNull"), "[AbstractConcept - createConceptGDS]"));
                throw new NullValueException(Config.properties.getProperty("AbstractConcept.ConceptGDSAttributeNameNull"));
            } else if (value == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.ConceptGDSValueNull"), "[AbstractConcept - createConceptGDS]"));
                throw new NullValueException(Config.properties.getProperty("AbstractConcept.ConceptGDSValueNull"));
            } else {
                return storeConceptGDS(new ConceptGDS(sid, id, aog, attrname.getId(), value, doIndex));
            }
        } catch (OperationNotSupportedException onse) {
            fireEventOccurred(new AccessDeniedEvent(Config.properties.getProperty("AbstractConcept.ReadOnly"), "[AbstractConcept - createConceptGDS]"));
            throw new AccessDeniedException(Config.properties.getProperty("AbstractConcept.ReadOnly"));
        }
    }

    /**
	 * Removes a ConceptGDS or null if unsuccessful from the list of ConceptGDS
	 * of this AbstractConcept and returns the removed ConceptGDS.
	 * 
	 * @param attrname
	 *            AttributeName of ConceptGDS to be removed
	 * @return deleted ConceptGDS
	 */
    @Access(DELETE)
    public ConceptGDS deleteConceptGDS(AttributeName attrname) {
        try {
            if (attrname == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.ConceptGDSAttributeNameNull"), "[AbstractConcept - deleteConceptGDS]"));
                throw new NullValueException(Config.properties.getProperty("AbstractConcept.ConceptGDSAttributeNameNull"));
            } else {
                return removeConceptGDS(attrname);
            }
        } catch (OperationNotSupportedException onse) {
            fireEventOccurred(new AccessDeniedEvent(Config.properties.getProperty("AbstractConcept.ReadOnly"), "[AbstractConcept - deleteConceptGDS]"));
            throw new AccessDeniedException(Config.properties.getProperty("AbstractConcept.ReadOnly"));
        }
    }

    /**
	 * Returns a ConceptGDS or null if unsuccessful for a given AttributeName.
	 * 
	 * @param attrname
	 *            AttributeName to search
	 * @return existing ConceptGDS
	 */
    @Access(GET)
    public ConceptGDS getConceptGDS(AttributeName attrname) {
        if (attrname == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.ConceptGDSAttributeNameNull"), "[AbstractConcept - getConceptGDS]"));
            throw new NullValueException(Config.properties.getProperty("AbstractConcept.ConceptGDSAttributeNameNull"));
        } else {
            return retrieveConceptGDS(attrname);
        }
    }

    /**
	 * Returns all ConceptGDS contained in the list of ConceptGDS of this
	 * instance of AbstractConcept.
	 * 
	 * @return all ConceptGDS as AbstractONDEXIterator<ConceptGDS>
	 */
    @Access(GET)
    public AbstractONDEXIterator<ConceptGDS> getConceptGDSs() {
        return retrieveConceptGDSAll();
    }

    /**
	 * Returns the annotation of this instance of AbstractConcept.
	 * 
	 * @return annotation String
	 */
    @Access(GET)
    public String getAnnotation() {
        return annotation;
    }

    /**
	 * Sets the annotation of this instance of AbstractConcept.
	 * 
	 * @param annotation
	 *            the annotation to set.
	 */
    @Access(UPDATE)
    public void setAnnotation(String annotation) {
        if (annotation == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.AnnotationNull"), "[AbstractConcept - setAnnotation]"));
            throw new NullValueException(Config.properties.getProperty("AbstractConcept.AnnotationNull"));
        } else {
            this.annotation = annotation;
            fireUpdate(this);
        }
    }

    /**
	 * Returns the description of this instance of AbstractConcept.
	 * 
	 * @return description String
	 */
    @Access(GET)
    public String getDescription() {
        return description;
    }

    /**
	 * Sets the description of this instance of AbstractConcept.
	 * 
	 * @param description
	 *            the description to set
	 */
    @Access(UPDATE)
    public void setDescription(String description) {
        if (description == null) {
            fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.DescriptionNull"), "[AbstractConcept - setDescription]"));
            throw new NullValueException(Config.properties.getProperty("AbstractConcept.DescriptionNull"));
        } else {
            this.description = description;
            fireUpdate(this);
        }
    }

    /**
	 * Returns the CV, which this AbstractConcept belongs to.
	 * 
	 * @return this controlled vocabulary CV
	 */
    @Access(GET)
    public CV getElementOf() {
        if (elementOf == null && elementOfS != null) {
            try {
                elementOf = aog.getONDEXGraphData().getCV(elementOfS);
            } catch (WrongParameterException e) {
                throw new DataLossException("CV of concept " + getId() + " has been lost!!");
            }
        }
        return elementOf;
    }

    /**
	 * Returns the ConceptClass this AbstractConcept belongs to.
	 * 
	 * @return this ConceptClass
	 */
    @Access(GET)
    public ConceptClass getOfType() {
        if (ofType == null && ofTypeS != null) {
            try {
                ofType = aog.getONDEXGraphData().getConceptClass(ofTypeS);
            } catch (WrongParameterException e) {
                throw new DataLossException("CV of concept " + getId() + " has been lost!!");
            }
        }
        return ofType;
    }

    /**
	 * Returns the parser id of this instance of AbstractConcept.
	 * 
	 * @return parser id String
	 */
    @Access(GET)
    public String getPID() {
        return pid;
    }

    public Integer getId() {
        return id;
    }

    /**
	 * Returns a list of all evidences for this AbstractConcept.
	 * 
	 * @return list of evidences as AbstractONDEXIterator<EvidenceType>
	 */
    @Access(GET)
    public AbstractONDEXIterator<EvidenceType> getEvidence() {
        return getEvidenceTypes();
    }

    /**
	 * Adds a given EvidenceType to the AbstractConcept.
	 * 
	 * @param evidencetype
	 *            EvidenceType to be added
	 */
    @Access(UPDATE)
    public void addEvidenceType(EvidenceType evidencetype) {
        try {
            if (evidencetype == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.EvidenceTypeNull"), "[AbstractConcept - addEvidenceType]"));
                throw new NullValueException(Config.properties.getProperty("AbstractConcept.EvidenceTypeNull"));
            } else {
                saveEvidenceType(evidencetype);
            }
        } catch (OperationNotSupportedException onse) {
            fireEventOccurred(new AccessDeniedEvent(Config.properties.getProperty("AbstractConcept.ReadOnly"), "[AbstractConcept - addEvidenceType]"));
            throw new AccessDeniedException(Config.properties.getProperty("AbstractConcept.ReadOnly"));
        }
    }

    /**
	 * Removes a given EvidenceType for the set of EvidenceType of this
	 * AbstractConcept.
	 * 
	 * @param evidencetype
	 *            EvidenceType to be removed
	 * @return true if successful
	 */
    @Access(UPDATE)
    public boolean removeEvidenceType(EvidenceType evidencetype) {
        try {
            if (evidencetype == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.EvidenceTypeNull"), "[AbstractConcept - removeEvidenceType]"));
                throw new NullValueException(Config.properties.getProperty("AbstractConcept.EvidenceTypeNull"));
            } else {
                boolean result = dropEvidenceType(evidencetype);
                return result;
            }
        } catch (OperationNotSupportedException onse) {
            fireEventOccurred(new AccessDeniedEvent(Config.properties.getProperty("AbstractConcept.ReadOnly"), "[AbstractConcept - removeEvidenceType]"));
            throw new AccessDeniedException(Config.properties.getProperty("AbstractConcept.ReadOnly"));
        }
    }

    /**
	 * Returns an ONDEXView on AbstractConcepts representing the context of this
	 * AbstractConcept.
	 * 
	 * @return context as ONDEXView<AbstractConcept>
	 */
    @Access(GET)
    public ONDEXView<AbstractConcept> getContext() {
        return getContexts();
    }

    /**
	 * Adds an AbstractConcept to the context of this AbstractConcept.
	 * 
	 * @param ac
	 *            AbstractConcept
	 */
    @Access(UPDATE)
    public void addContext(AbstractConcept ac) throws AccessDeniedException, NullValueException {
        try {
            if (ac == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.AbstractConceptNull"), "[AbstractConcept - addContext]"));
                throw new NullValueException(Config.properties.getProperty("AbstractConcept.AbstractConceptNull"));
            } else {
                saveContext(ac);
            }
        } catch (OperationNotSupportedException onse) {
            fireEventOccurred(new AccessDeniedEvent(Config.properties.getProperty("AbstractConcept.ReadOnly"), "[AbstractConcept - addContext]"));
            throw new AccessDeniedException(Config.properties.getProperty("AbstractConcept.ReadOnly"));
        }
    }

    /**
	 * Removes an AbstractConcept from the context of this AbstractConcept.
	 * 
	 * @param ac
	 *            AbstractConcept
	 * @return true if successful
	 */
    @Access(UPDATE)
    public boolean removeContext(AbstractConcept ac) {
        try {
            if (ac == null) {
                fireEventOccurred(new NullValueEvent(Config.properties.getProperty("AbstractConcept.AbstractConceptNull"), "[AbstractConcept - removeContext]"));
                throw new NullValueException(Config.properties.getProperty("AbstractConcept.AbstractConceptNull"));
            } else {
                boolean result = dropContext(ac);
                return result;
            }
        } catch (OperationNotSupportedException onse) {
            fireEventOccurred(new AccessDeniedEvent(Config.properties.getProperty("AbstractConcept.ReadOnly"), "[AbstractConcept - removeContext]"));
            throw new AccessDeniedException(Config.properties.getProperty("AbstractConcept.ReadOnly"));
        }
    }

    /**
	 * Saves a given EvidenceType to the list of EvidenceTypes.
	 * 
	 * @param evidencetype
	 *            EvidenceType to save
	 */
    protected abstract void saveEvidenceType(EvidenceType evidencetype) throws OperationNotSupportedException;

    /**
	 * Drops a given EvidenceType out of the list of EvidenceTypes.
	 * 
	 * @param evidencetype
	 *            EvidenceType to drop
	 * @return boolean
	 */
    protected abstract boolean dropEvidenceType(EvidenceType evidencetype) throws OperationNotSupportedException;

    /**
	 * Returns the list of all EvidenceTypes.
	 * 
	 * @return AbstractONDEXIterator<EvidenceType>
	 */
    protected abstract AbstractONDEXIterator<EvidenceType> getEvidenceTypes();

    /**
	 * Saves a given AbstractConcept to the context list.
	 * 
	 * @param concept
	 *            AbstractConcept to save
	 * @throws AccessDeniedException if the context is not readable.
	 */
    protected abstract void saveContext(AbstractConcept concept) throws OperationNotSupportedException, AccessDeniedException;

    /**
	 * Drops a given AbstractConcept out of the context list.
	 * 
	 * @param concept
	 *            AbstracConcept to drop
	 * @return boolean
	 * @throws AccessDeniedException 
	 */
    protected abstract boolean dropContext(AbstractConcept concept) throws OperationNotSupportedException, AccessDeniedException;

    /**
	 * Returns the context list.
	 * 
	 * @return ONDEXView<AbstractConcept>
	 */
    protected abstract ONDEXView<AbstractConcept> getContexts();

    /**
	 * Stores the given ConceptName in the repository.
	 * 
	 * @param cn
	 *            ConceptName to store
	 * @return ConceptName
	 */
    protected abstract ConceptName storeConceptName(ConceptName cn) throws OperationNotSupportedException;

    /**
	 * Removes a ConceptName by a given name from the repository.
	 * 
	 * @param name
	 *            name of ConceptName to be removed
	 * @return ConceptName
	 */
    protected abstract ConceptName removeConceptName(String name) throws OperationNotSupportedException;

    /**
	 * Returns the preferred ConceptName from the repository.
	 * 
	 * @return ConceptName
	 */
    protected abstract ConceptName preferredConceptName();

    /**
	 * Retrieves a ConceptName by a given name from the repository.
	 * 
	 * @param name
	 *            name of ConceptName to be retrieved
	 * @return ConceptName
	 */
    protected abstract ConceptName retrieveConceptName(String name);

    /**
	 * Returns all ConceptNames contained in the repository.
	 * 
	 * @return AbstractONDEXIterator<ConceptName>
	 */
    protected abstract AbstractONDEXIterator<ConceptName> retrieveConceptNameAll();

    /**
	 * Stores the given ConceptAccession in the repository.
	 * 
	 * @param ca
	 *            ConceptAccession to store
	 * @return ConceptAccession
	 */
    protected abstract ConceptAccession storeConceptAccession(ConceptAccession ca) throws OperationNotSupportedException;

    /**
	 * Removes a ConceptAccession by a given name from the repository.
	 * 
	 * @param accession
	 *            accession to be removed
	 * @param elementOf
	 *            CV of ConceptAccession
	 * @return ConceptAccession
	 */
    protected abstract ConceptAccession removeConceptAccession(String accession, CV elementOf) throws OperationNotSupportedException;

    /**
	 * Retrieves a ConceptAccession by the given accession and elementOf from
	 * the repository.
	 * 
	 * @param accession
	 *            accession to be retrieved
	 * @param elementOf
	 *            CV of ConceptAccession
	 * @return ConceptAccession
	 */
    protected abstract ConceptAccession retrieveConceptAccession(String accession, CV elementOf);

    /**
	 * Returns all ConceptAccessions contained in the repository.
	 * 
	 * @return AbstractONDEXIterator<ConceptAccession>
	 */
    protected abstract AbstractONDEXIterator<ConceptAccession> retrieveConceptAccessionAll();

    /**
	 * Stores a given ConceptGDS to the repository.
	 * 
	 * @param gds
	 *            ConceptGDS to store
	 * @return ConceptGDS
	 */
    protected abstract ConceptGDS storeConceptGDS(ConceptGDS gds) throws OperationNotSupportedException;

    /**
	 * Removes a ConceptGDS by a given AttributeName from the repository.
	 * 
	 * @param attrname
	 *            AttributeName
	 * @return ConceptGDS
	 */
    protected abstract ConceptGDS removeConceptGDS(AttributeName attrname) throws OperationNotSupportedException;

    /**
	 * Retrieves a ConceptGDS by a given AttributeName from the repository.
	 * 
	 * @param attrname
	 *            AttributeName
	 * @return ConceptGDS
	 */
    protected abstract ConceptGDS retrieveConceptGDS(AttributeName attrname);

    /**
	 * Retrieves a AbstractONDEXIterator of ConceptGDS from the repository.
	 * 
	 * @return AbstractONDEXIterator<ConceptGDS>
	 */
    protected abstract AbstractONDEXIterator<ConceptGDS> retrieveConceptGDSAll();

    /**
	 * set permission on this object to p for the given scope s.
	 * @param s the access scope
	 * @param p the permissions level.
	 * @throws AccessDeniedException if not owner.
	 */
    @Override
    public void setPermission(AccessScope s, Permission p) throws AccessDeniedException {
        GlobalPermissions.getInstance(sid).setConceptPermission(id, s, p);
    }

    /**
	 * returns the current permissions level on this object for
	 * a given access scope.
	 * @param s the access scope to query for
	 * @return the current permission level
	 */
    @Override
    public Permission getPermission(AccessScope s) {
        return GlobalPermissions.getInstance(sid).getConceptPermissionLevel(id, s);
    }

    /**
	 * if called by root, this sets the owner user id for this object
	 * to the given value. 
	 * @param uid the user id to set.
	 * @throws AccessDeniedException if not root.
	 */
    @Override
    public void setOwnerUserID(int uid) throws AccessDeniedException {
        GlobalPermissions.getInstance(sid).setConceptOwner(id, uid);
    }

    /**
	 * returns the owner user id for this object.
	 * @return the owner user id.
	 */
    @Override
    public int getOwnerUserID() {
        return GlobalPermissions.getInstance(sid).getConceptOwnerUserID(id);
    }

    /**
	 * if called by root, this sets the owner group id for this object
	 * to the given value. 
	 * @param gid the group id to set.
	 * @throws AccessDeniedException if not root.
	 */
    @Override
    public void setOwnerGroupID(int gid) throws AccessDeniedException {
        GlobalPermissions.getInstance(sid).setConceptGroup(id, gid);
    }

    /**
	 * returns the owner group id of this object.
	 * @return the owner group id
	 */
    @Override
    public int getOwnerGroupID() {
        return GlobalPermissions.getInstance(sid).getConceptOwnerGroupID(id);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AbstractConcept) {
            AbstractConcept c = (AbstractConcept) o;
            return this.id.equals(c.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    public String toString() {
        return this.pid;
    }
}
