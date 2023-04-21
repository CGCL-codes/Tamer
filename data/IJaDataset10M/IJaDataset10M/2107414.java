package eu.annocultor.core.api;

import java.util.List;
import java.util.Map;
import eu.annocultor.core.context.Concepts;
import eu.annocultor.xconverter.api.DataObject;
import eu.annocultor.xconverter.api.Path;
import eu.annocultor.xconverter.api.PropertyRule;
import eu.annocultor.xconverter.api.Triple;
import eu.annocultor.xconverter.api.Value;
import eu.annocultor.xconverter.api.XmlValue;

/**
 * Special rule that can convert {@link DataObject}, by invoking other rules
 * that convert properties or translate their values.
 * 
 * @author Borys Omelayenko
 * 
 */
public abstract class ObjectRule extends Rule {

    /**
	 * 
	 * @param dataObject
	 * @return <code>null</code> if a pre- or post- condition has failed, or
	 *         object identifier (subject).
	 * @throws Exception
	 */
    public final Value checkConditionsAndGetSubject(DataObject dataObject) throws Exception {
        if (selectorPreCondition(dataObject)) {
            preprocess(dataObject);
            if (selectorPostCondition(dataObject)) {
                XmlValue subjectName = dataObject.getFirstValue(getPrimaryRecordIdPath());
                if (subjectName == null) throw new Exception("null identifier");
                for (DataObject child : dataObject.findAllChildren()) {
                    child.addValue(Concepts.INTERNAL.PART_TO_PARENT, subjectName);
                    child.addValue(Concepts.INTERNAL.PARENT_TO_PART, subjectName);
                }
                return subjectName;
            }
        }
        return null;
    }

    /**
	 * @see DataObjectPreprocessor
	 * @param dataObject
	 * @return
	 * @throws Exception
	 */
    public abstract boolean selectorPreCondition(DataObject dataObject) throws Exception;

    /**
	 * @see DataObjectPreprocessor
	 * @param dataObject
	 * @return
	 * @throws Exception
	 */
    public abstract boolean selectorPostCondition(DataObject dataObject) throws Exception;

    /**
	 * @see DataObjectPreprocessor
	 * @param dataObject
	 * @return
	 * @throws Exception
	 */
    public abstract void preprocess(DataObject dataObject) throws Exception;

    /**
	 * Returns the identifier of this {@link DataObject}.
	 * 
	 * @param dataObject
	 * @return
	 * @throws Exception
	 */
    public abstract String getDataObjectId(DataObject dataObject) throws Exception;

    /**
	 * Returns the secondary identifying path (used in error messages, etc).
	 * 
	 * @return
	 */
    public abstract Path getSecondaryRecordIdPath();

    /**
	 * Returns the primary identifying path used to generate object id.
	 */
    public abstract Path getPrimaryRecordIdPath();

    /**
	 * Gets parent rule for part-of nested objects.
	 */
    public abstract ObjectRule getParent();

    @Override
    public final void fire(Triple triple, DataObject dataObject) throws Exception {
        ObjectRule rule = dataObject.getDataObjectRule();
        String subjectName = rule.checkConditionsAndGetSubject(dataObject).getValue();
        if (subjectName != null) {
            processDataObject(triple.getSubject(), dataObject);
        }
    }

    /**
	 * Apply property conversion rules.
	 * 
	 * @param subject
	 * @param dataObject
	 * @throws Exception
	 */
    public abstract void processDataObject(String subject, DataObject dataObject) throws Exception;

    public abstract boolean isQualifiedLocalRecordIdentifier();

    public abstract void addPreprocessor(DataObjectPreprocessor preprocessor);

    /**
	 * Adds a property conversion rule to convert an XML path specified
	 * relatively, from the record separating tag.
	 * 
	 */
    @Deprecated
    public abstract void addRelRule(Path relativePath, PropertyRule rule) throws Exception;

    /**
	 * Adds a property conversion rule to convert an XML path specified
	 * absolutely, from the XML root. May be also used for the predefined tags
	 * <code>Concepts.INTERNAL.PART_TO_PARENT</code> and
	 * <code>Concepts.INTERNAL.PARENT_TO_PART</code> that are absolute.
	 * 
	 */
    @Deprecated
    public abstract void addAbsRule(Path absolutePath, PropertyRule rule) throws Exception;

    /**
	 * Adds a property conversion rule to convert an XML path specified
	 * absolutely or relatively  (from the record separating tag), 
	 * as stated in {@link Rule.getSourcePath()}.
	 * 
	 */
    public abstract void addRule(PropertyRule rule) throws Exception;

    /**
	 * Get all rules that are applicable to a specific XML path.
	 * 
	 */
    public abstract List<Rule> getRule(Path data) throws Exception;

    public abstract String report();

    public abstract void setSecondaryRecordIdTag(Path secondaryRecordIdTag);

    public abstract void setPrimaryRecordIdField(Path primaryRecordIdTag);

    public abstract Path getRecordSeparatingPath();

    public abstract void setRecordSeparatingTag(Path recordSeparatingTag);

    public abstract Task getTask();

    /**
	 * Do not prefix the identifier with the target namespace.
	 */
    public abstract void assumeQualifiedLocalRecordIdentifier();

    public abstract Map<Path, Integer> getMissedPaths();
}
