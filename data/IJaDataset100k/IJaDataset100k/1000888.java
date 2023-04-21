package net.sourceforge.jcoupling2.dao.obsolete;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;
import net.sourceforge.jcoupling2.dao.Channel;
import net.sourceforge.jcoupling2.dao.ChannelRepository;
import net.sourceforge.jcoupling2.dao.internal.Utilities;
import org.apache.log4j.Logger;

public class DaoPropertyReader {

    private static Logger log = Logger.getLogger(DaoPropertyReader.class.getSimpleName());

    private Properties props = null;

    public static final String CHANNEL = Channel.class.getSimpleName();

    public static final String CHANNEL_REPOSITORY = ChannelRepository.class.getSimpleName();

    /**
	 */
    public DaoPropertyReader() {
        this.props = new Properties();
        try {
            props.load(this.getClass().getResourceAsStream("dao.properties"));
        } catch (IOException e) {
            log.error(e.toString());
        }
    }

    public TreeSet<Operation> readOperations(String daoName) {
        TreeSet<Operation> operations = new TreeSet<Operation>();
        String storedProcedure = null;
        Operation operation = null;
        String rootKey = null;
        String key = null;
        rootKey = daoName + ".";
        try {
            for (Range.OperationType operationType : Range.OperationType.values()) {
                key = rootKey + operationType.toString().toLowerCase();
                storedProcedure = props.getProperty(key);
                if (storedProcedure != null) {
                    operation = new Operation();
                    operation.operationType = operationType;
                    operation.storedProcedure = storedProcedure;
                    operation.attributes = readOperationParams(daoName, operationType);
                    operations.add(operation);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(e.getLocalizedMessage());
        }
        return operations;
    }

    public TreeSet<Attribute> readAttributes(String daoName) {
        TreeSet<Attribute> attributeList = new TreeSet<Attribute>();
        Attribute attribute = null;
        String propertyString = null;
        String rootKey = daoName + ".attribute[";
        String key = null;
        int i = 1;
        key = rootKey + i + "]";
        while (props.containsKey(key)) {
            propertyString = props.getProperty(key).trim();
            if (propertyString != null) {
                try {
                    String[] propertyList = propertyString.split(";");
                    attribute = new Attribute();
                    attribute.id = new Integer(propertyList[0]);
                    attribute.name = propertyList[1];
                    attribute.datatype = Utilities.getDatatype(propertyList[2]);
                    attribute.isPrimary = Utilities.getBoolean(propertyList[3]);
                    attribute.isUnique = Utilities.getBoolean(propertyList[4]);
                    attribute.comment = propertyList[5];
                    attributeList.add(attribute);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
                key = rootKey + i + "]";
            }
        }
        return attributeList;
    }

    private TreeSet<Attribute> readOperationParams(String dao, Range.OperationType operationType) throws DaoPropertyReaderException {
        TreeSet<Attribute> attributes = null;
        Attribute attribute = null;
        Attribute referencedAttribute = null;
        Range.AttributeType attributeType = null;
        Range.Datatype datatype = null;
        String usageComment = null;
        String attributeName = null;
        String propertyString = null;
        String root = null;
        String key = null;
        DataAccessObject referencedDao = null;
        Boolean isMandatory = false;
        Boolean isUnique = false;
        Boolean isChanged = false;
        Integer attributeId = null;
        int i = 0;
        attributes = new TreeSet<Attribute>();
        attributeType = this.getAttributeType(operationType);
        i = 1;
        root = dao + "." + operationType.toString().toLowerCase() + ".parameter[";
        key = root + i + "]";
        while (props.containsKey(key)) {
            if (props.getProperty(key) != null) {
                try {
                    propertyString = props.getProperty(key).trim();
                    String[] propertyList = propertyString.split(";");
                    referencedDao = getDataAccessObject(propertyList[0]);
                    attributeId = new Integer(propertyList[1]);
                    if (propertyList[2] != null && propertyList[2].length() > 0) {
                        isMandatory = Utilities.getBoolean(propertyList[2]);
                    } else {
                        isMandatory = null;
                    }
                    usageComment = propertyList[3];
                    referencedAttribute = this.getReferencedAttribute(referencedDao, attributeId);
                    attributeName = referencedAttribute.name;
                    datatype = referencedAttribute.datatype;
                    if (operationType.equals(Range.OperationType.CHANGE)) {
                        isChanged = true;
                        isUnique = false;
                    } else {
                        isChanged = false;
                        isUnique = referencedAttribute.isUnique;
                    }
                    attribute = new Attribute(referencedDao, attributeType, attributeId, attributeName, datatype, isMandatory, isUnique, isChanged, usageComment);
                    attributes.add(attribute);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
            }
            key = root + i + "]";
        }
        return attributes;
    }

    private Range.AttributeType getAttributeType(Range.OperationType operationType) {
        Range.AttributeType attributeType = null;
        if (operationType.equals(Range.OperationType.ADD)) {
            attributeType = Range.AttributeType.CAN_BE_SAVED;
        } else if (operationType.equals(Range.OperationType.CHANGE)) {
            attributeType = Range.AttributeType.CAN_BE_CHANGED;
        } else if (operationType.equals(Range.OperationType.REMOVE)) {
            attributeType = Range.AttributeType.NARROW_DOWN;
        } else if (operationType.equals(Range.OperationType.RETRIEVE)) {
            attributeType = Range.AttributeType.NARROW_DOWN;
        }
        return attributeType;
    }

    private DataAccessObject getDataAccessObject(String name) {
        DataAccessObject dao = null;
        if (name.equals(CHANNEL)) {
            dao = new Channel();
        } else if (name.equals(CHANNEL_REPOSITORY)) {
            dao = new ChannelRepository();
        }
        return dao;
    }

    private Attribute getReferencedAttribute(DataAccessObject dao, Integer attributeId) {
        Attribute attribute = null;
        if (dao instanceof Channel) {
            attribute = getAttribute(attributeId, Channel.supportedAttributes.iterator());
        } else if (dao instanceof ChannelRepository) {
            attribute = getAttribute(attributeId, ChannelRepository.supportedAttributes.iterator());
        }
        return attribute;
    }

    private Attribute getAttribute(Integer refId, Iterator<Attribute> attributeList) {
        Attribute attribute = null;
        Attribute referencedAttribute = null;
        try {
            while (attributeList.hasNext()) {
                attribute = attributeList.next();
                if (attribute.id.equals(refId)) {
                    referencedAttribute = attribute;
                    break;
                }
            }
        } catch (Exception e) {
            log.debug(e.getLocalizedMessage());
        }
        return referencedAttribute;
    }
}
