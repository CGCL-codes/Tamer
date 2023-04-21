package de.fau.cs.dosis.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import de.fau.cs.dosis.account.model.User;
import de.fau.cs.dosis.drug.ActiveIngredientNotFoundException;
import de.fau.cs.dosis.drug.manager.ActiveIngredientManager;
import de.fau.cs.dosis.drug.manager.ActiveIngredientManagerException;
import de.fau.cs.dosis.drug.model.ActiveIngredient;
import de.fau.cs.dosis.drug.model.ActiveIngredientDigest;
import de.fau.cs.dosis.drug.model.ActiveIngredientRevision;
import de.fau.cs.dosis.drug.model.BrandName;
import de.fau.cs.dosis.drug.model.RevisionStatus;
import de.fau.cs.dosis.drug.model.TechnicalStatus;
import de.fau.cs.dosis.schema.Datetime;
import de.fau.cs.dosis.schema.UserReference;
import de.fau.cs.dosis.schema.WeakReference;
import de.fau.cs.dosis.schema.XmlActiveIngredient;
import de.fau.cs.dosis.schema.XmlActiveIngredientRevision;
import de.fau.cs.dosis.schema.XmlBrandname;
import de.fau.cs.dosis.schema.XmlContainer;
import de.fau.cs.dosis.schema.XmlTechnicalStatus;

public class ActiveIngredientManagerXml extends ActiveIngredientManager {

    private static final Logger logger = LoggerFactory.getLogger(ActiveIngredientManagerXml.class);

    private static final String SCHEMA = "de/fau/cs/dosis/container.xsd";

    private static final String SCHEMA_PACKAGE = "de.fau.cs.dosis.schema";

    private final Unmarshaller unmarshaller;

    private final Marshaller marshaller;

    private final DatatypeFactory dataTypes;

    private boolean writeOnChange;

    private File storage;

    private XmlContainer container;

    private final ActiveIngredientTransform transform;

    private AtomicInteger idGenerator = new AtomicInteger(1);

    public ActiveIngredientManagerXml() throws ActiveIngredientManagerException {
        this.transform = new ActiveIngredientTransform();
        this.container = getObjectFactory().createXmlContainer();
        this.writeOnChange = false;
        try {
            this.unmarshaller = createUnmarshaller(SCHEMA, SCHEMA_PACKAGE);
            this.marshaller = createMarshaller(SCHEMA, SCHEMA_PACKAGE);
            this.dataTypes = DatatypeFactory.newInstance();
        } catch (JAXBException e) {
            throw new ActiveIngredientManagerException(e.getMessage(), e);
        } catch (DatatypeConfigurationException e) {
            throw new ActiveIngredientManagerException(e.getMessage(), e);
        }
    }

    public ActiveIngredientManagerXml(String storage) throws ActiveIngredientManagerException {
        this();
        this.storage = new File(storage);
        FileInputStream fos = null;
        try {
            fos = new FileInputStream(storage);
            container = load(fos);
        } catch (FileNotFoundException e) {
            throw new ActiveIngredientManagerException(e);
        } catch (JAXBException e) {
            throw new ActiveIngredientManagerException(e);
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    throw new ActiveIngredientManagerException(e);
                }
            }
        }
    }

    private static de.fau.cs.dosis.schema.ObjectFactory getObjectFactory() {
        return new de.fau.cs.dosis.schema.ObjectFactory();
    }

    private Unmarshaller getUnmarshaller() {
        return this.unmarshaller;
    }

    private Marshaller getMarshaller() {
        return this.marshaller;
    }

    public void persist(File storage) throws ActiveIngredientManagerException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(storage);
            persist(fos);
        } catch (FileNotFoundException e) {
            throw new ActiveIngredientManagerException("Cant open file " + storage.getName(), e);
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public void persist(OutputStream os) throws ActiveIngredientManagerException {
        try {
            getMarshaller().marshal(container, os);
        } catch (JAXBException e) {
            throw new ActiveIngredientManagerException("Unable to Serialize ActiveIngredient", e);
        }
    }

    private Marshaller createMarshaller(String schemaLocation, String packageName) throws JAXBException {
        JAXBContext jc;
        URL scm = Thread.currentThread().getContextClassLoader().getResource(schemaLocation);
        logger.info("using schema " + scm.toString());
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = null;
        try {
            schema = factory.newSchema(scm);
        } catch (SAXException e) {
            logger.error("cant parse schema", e);
        }
        if (schema != null) {
            jc = JAXBContext.newInstance(packageName);
            Marshaller ms = jc.createMarshaller();
            ms.setSchema(schema);
            return ms;
        } else {
            throw new JAXBException("Schema: " + schemaLocation + " could not be loaded");
        }
    }

    private Unmarshaller createUnmarshaller(String schemaLocation, String packageName) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(packageName);
        URL scm = Thread.currentThread().getContextClassLoader().getResource(schemaLocation);
        logger.info("using schema " + scm.toString());
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = null;
        try {
            schema = factory.newSchema(scm);
        } catch (SAXException e) {
            logger.error("cant parse schema", e);
        }
        if (schema != null) {
            Unmarshaller um = jc.createUnmarshaller();
            um.setSchema(schema);
            return um;
        } else {
            throw new JAXBException("Schema: " + schemaLocation + " could not be loaded");
        }
    }

    private Object deepClone(Object obj) {
        ByteArrayOutputStream os = null;
        InputStream is = null;
        try {
            os = new ByteArrayOutputStream();
            new ObjectOutputStream(os).writeObject(obj);
            os.close();
            is = new ByteArrayInputStream(os.toByteArray());
            Object o = new ObjectInputStream(is).readObject();
            return o;
        } catch (IOException e) {
            throw new RuntimeException("Invalid Object", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Invalid Objet", e);
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private XmlContainer load(InputStream is) throws JAXBException {
        Object obj;
        obj = getUnmarshaller().unmarshal(is);
        if (obj instanceof XmlContainer) {
            return (XmlContainer) obj;
        } else {
            throw new JAXBException("Invalid object in inputStream found: " + obj.getClass().getCanonicalName());
        }
    }

    public void close() throws ActiveIngredientManagerException {
        this.persist(storage);
    }

    private String getId() {
        String id = "_" + idGenerator.getAndIncrement();
        return id;
    }

    @Override
    public ActiveIngredient createActiveIngredient(ActiveIngredient activeIngredient) throws ActiveIngredientManagerException {
        XmlActiveIngredient ai = getObjectFactory().createXmlActiveIngredient();
        ai.setId(getId());
        ai.setGuid(activeIngredient.getGuid());
        ai.setName(activeIngredient.getName());
        ai.setSlug(activeIngredient.getSlug());
        ai.setTechnicalStatus(create(activeIngredient.getTechnicalStatus()));
        ai.setSubstanceClasses("");
        ai.setOwner(create(activeIngredient.getOwner(), activeIngredient.getOwnerGuid(), ""));
        this.container.getXmlActiveIngredient().add(ai);
        return transform.transform(ai);
    }

    private XmlActiveIngredient getByGuid(String guid) {
        for (XmlActiveIngredient ai : container.getXmlActiveIngredient()) {
            if (ai.getGuid().equals(guid)) {
                return ai;
            }
        }
        return null;
    }

    @Override
    public ActiveIngredientRevision createActiveIngredientRevision(ActiveIngredientRevision create) throws ActiveIngredientManagerException {
        XmlActiveIngredient xmlAi = getByGuid(create.getActiveIngredient().getGuid());
        if (null == xmlAi) {
            throw new ActiveIngredientManagerException("ActiveIngredient with Guid:" + create.getActiveIngredient().getGuid() + " nof found");
        }
        XmlActiveIngredientRevision air = getObjectFactory().createXmlActiveIngredientRevision();
        air.setActiveIngredient(xmlAi);
        air.setId(getId());
        air.setGuid(create.getGuid());
        if (null != create.getOwner()) {
            air.setOwner(create(create.getOwner(), create.getOwner().getGuid(), create.getOwner().getName()));
        } else {
            air.setOwner(create(null, create.getGuid(), create.getOwnerName()));
        }
        air.setCreated(create(create.getDateCreated()));
        air.setNumber(create.getRevisionNumber());
        air.setApplication(create.getApplication());
        air.setReferences(create.getReferences());
        air.setContraIndications(create.getContraIndications());
        air.setDosage(create.getDosage());
        air.setEffectSpectrum(create.getEffectSpectrum());
        air.setInteractionEffects(create.getInteractionEffects());
        air.setPharmacoDynamics(create.getPharmacoDynamics());
        air.setSideeffects(create.getSideEffects());
        container.getXmlActiveIngredientRevision().add(air);
        return transform.transform(air);
    }

    @Override
    public BrandName createBrandName(BrandName brandName) throws ActiveIngredientManagerException {
        if (null == brandName || null == brandName.getActiveIngredient()) {
            throw new ActiveIngredientManagerException("brandName or brandName.getActiveIngredient() must not be null");
        }
        XmlActiveIngredient ai = this.getByGuid(brandName.getActiveIngredient().getGuid());
        XmlBrandname bn = getObjectFactory().createXmlBrandname();
        bn.setName(brandName.getName());
        bn.setSlug(brandName.getSlug());
        ai.getXmlBrandname().add(bn);
        return brandName;
    }

    @Override
    public ActiveIngredient getActiveIngredientBySlug(String name) throws ActiveIngredientManagerException {
        for (XmlActiveIngredient ai : this.container.getXmlActiveIngredient()) {
            if (ai.getSlug().equals(name)) {
                return transform.transform(ai);
            }
        }
        return null;
    }

    @Override
    public ActiveIngredient getActiveIngredientByGuid(String guid) throws ActiveIngredientManagerException {
        for (XmlActiveIngredient ai : this.container.getXmlActiveIngredient()) {
            if (ai.getGuid().equals(guid)) {
                return transform.transform(ai);
            }
        }
        return null;
    }

    @Override
    public void updateActiveIngredient(ActiveIngredient ai) throws ActiveIngredientManagerException {
        XmlActiveIngredient merge = this.getByGuid(ai.getGuid());
        if (null == merge) {
            throw new ActiveIngredientManagerException("ActiveIngredient with guid: " + ai.getGuid() + " not found");
        }
        merge.setSubstanceClasses(ai.getSubstanceClasses());
    }

    public UserReference create(User user, String userGuid, String userName) {
        UserReference ref = getObjectFactory().createUserReference();
        if (null == user) {
            WeakReference wRef = getObjectFactory().createWeakReference();
            wRef.setGuid(userGuid);
            wRef.setName(userName);
            ref.setWeakUser(wRef);
        } else {
            WeakReference wRef = getObjectFactory().createWeakReference();
            wRef.setGuid(user.getGuid());
            wRef.setName(user.getName());
            ref.setWeakUser(wRef);
        }
        return ref;
    }

    public XmlTechnicalStatus create(TechnicalStatus status) {
        if (null == status) {
            return null;
        }
        switch(status) {
            case NEW:
                return XmlTechnicalStatus.NEW;
            case EDITING:
                return XmlTechnicalStatus.DEACTIVATED;
            case RELEASED:
                return XmlTechnicalStatus.RELEASED;
            case DEACTIVATED:
                return XmlTechnicalStatus.DEACTIVATED;
            default:
                throw new RuntimeException("Unsupported Technical status, " + status.toString());
        }
    }

    private Datetime create(GregorianCalendar calendar) {
        Datetime dt = getObjectFactory().createDatetime();
        dt.setCalendar(dataTypes.newXMLGregorianCalendar(calendar));
        dt.setTimezone(calendar.getTimeZone().getID());
        return dt;
    }

    @Override
    public ActiveIngredient getActiveIngredientByBrandSlug(String slug) throws ActiveIngredientNotFoundException {
        for (XmlActiveIngredient ai : this.container.getXmlActiveIngredient()) {
            for (XmlBrandname bn : ai.getXmlBrandname()) {
                if (bn.getSlug().equals(slug)) {
                    return transform.transform(ai);
                }
            }
        }
        return null;
    }

    public ActiveIngredient newActiveIngredient() {
        return new ActiveIngredient();
    }

    public ActiveIngredientRevision newActiveIngredientRevision() {
        return new ActiveIngredientRevision();
    }

    public BrandName newBrandName() {
        return new BrandName();
    }

    @Override
    public ActiveIngredientRevision getActiveIngredientRevisionByGuid(String guid) throws ActiveIngredientManagerException {
        for (XmlActiveIngredientRevision air : container.getXmlActiveIngredientRevision()) {
            if (air.getGuid().equals(guid)) {
                return transform.transform(air);
            }
        }
        return null;
    }

    @Override
    public int getMaximumRevisionNumber(String guid) throws ActiveIngredientManagerException {
        int max = 0;
        for (XmlActiveIngredientRevision rev : this.getByGuid(guid).getXmlActiveIngredientRevision()) {
        }
        return max;
    }

    @Override
    public void updateActiveIngredientRevision(ActiveIngredientRevision air) throws ActiveIngredientManagerException {
    }

    @Override
    public List<ActiveIngredientRevision> getActiveIngredientRevisionsFiltered(ActiveIngredient ai, RevisionStatus status, User owner) {
        return null;
    }

    @Override
    public List<ActiveIngredientDigest> getDigestFiltered(String nameStartsWith, TechnicalStatus status, Integer offset, Integer limit) {
        List<ActiveIngredientDigest> digests = new ArrayList<ActiveIngredientDigest>();
        ActiveIngredientDigest digest;
        for (XmlActiveIngredient ai : this.container.getXmlActiveIngredient()) {
            if (status == null || ai.getTechnicalStatus().equals(transform.transform(status))) {
                if (nameStartsWith == null || ai.getName().startsWith(nameStartsWith)) {
                    digest = new ActiveIngredientDigest();
                    digest.setGuid(ai.getGuid());
                    digest.setName(ai.getName());
                    digest.setSlug(ai.getSlug());
                    digest.setType("ACTIVE_INGREDIENT");
                    digests.add(digest);
                }
                for (XmlBrandname bn : ai.getXmlBrandname()) {
                    if (nameStartsWith == null || bn.getName().startsWith(nameStartsWith)) {
                        digest = new ActiveIngredientDigest();
                        digest.setGuid(ai.getGuid());
                        digest.setName(bn.getName());
                        digest.setSlug(bn.getSlug());
                        digest.setType("BRAND_NAME");
                        digests.add(digest);
                    }
                }
            }
        }
        return digests;
    }

    @Override
    public long getActiveIngredientRevisionsFilteredCount(ActiveIngredient ai, RevisionStatus status, User owner) {
        return this.getActiveIngredientRevisionsFiltered(ai, status, owner).size();
    }

    @Override
    public long getDigestFilteredCount(String nameStartsWith, TechnicalStatus status) {
        return this.getDigestFiltered(nameStartsWith, status, null, null).size();
    }

    @Override
    public List<ActiveIngredient> getActiveIngredientFiltered(String nameStartsWith, TechnicalStatus status, Integer offset, Integer limit) {
        return null;
    }

    @Override
    public long getActiveIngredientFilteredCount(String nameStartsWith, TechnicalStatus status) {
        return this.getActiveIngredientFiltered(nameStartsWith, status, null, null).size();
    }
}
