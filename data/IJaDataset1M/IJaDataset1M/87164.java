package org.codehaus.groovy.runtime;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javassist.gluonj.Glue;
import javassist.gluonj.Refine;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import com.webobjects.appserver.WOComponent;
import com.webobjects.eoaccess.EODatabaseContext;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EORelationship;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOArrayDataSource;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOQualifierEvaluation;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSComparator;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSNumberFormatter;
import com.webobjects.foundation.NSPathUtilities;
import com.webobjects.foundation.NSRange;
import com.webobjects.foundation.NSSelector;
import com.webobjects.foundation.NSSet;
import com.webobjects.foundation.NSTimestamp;
import er.extensions.ERXArrayUtilities;
import er.extensions.ERXDictionaryUtilities;
import er.extensions.ERXEOAccessUtilities;
import er.extensions.ERXEOControlUtilities;
import er.extensions.ERXEOEncodingUtilities;
import er.extensions.ERXFileUtilities;
import er.extensions.ERXGenericRecord;
import er.extensions.ERXGraphUtilities;
import er.extensions.ERXNumberFormatter;
import er.extensions.ERXSQLHelper;
import er.extensions.ERXStringUtilities;
import er.extensions.ERXTimestampUtilities;
import er.extensions.ERXTimestampUtility;
import er.extensions.ERXUtilities;
import er.extensions.ERXValueUtilities;
import groovy.lang.Closure;
import groovy.lang.IntRange;
import groovy.lang.ObjectRange;
import groovy.lang.Range;
import groovy.lang.webobjects.categories.NSKeyValueCodingAdditions;
import groovy.lang.webobjects.components.WOTranscript;
import groovy.util.MapEntry;

@Glue
public class DefaultGroovyMethodsAdditions {

    @Refine
    public static class DefaultAdditions extends DefaultGroovyMethods {

        public static void connectWithModelNamed(EOEditingContext self, String name, NSDictionary overrides) {
            EOUtilities.connectWithModelNamed(self, name, overrides);
        }

        public static EOEnterpriseObject createAndInsertInstance(EOEditingContext self, String entityName) {
            return EOUtilities.createAndInsertInstance(self, entityName);
        }

        public static NSDictionary primaryKeyForObject(EOEditingContext self, EOEnterpriseObject object) {
            return EOUtilities.primaryKeyForObject(self, object);
        }

        public static EOQualifier qualifierForEnterpriseObject(EOEditingContext self, EOEnterpriseObject object) {
            return EOUtilities.qualifierForEnterpriseObject(self, object);
        }

        public static Number getNextValFromSequenceNamed(EOEditingContext self, String modelName, String sequenceName) {
            EODatabaseContext dbContext = EOUtilities.databaseContextForModelNamed(self, modelName);
            return ERXSQLHelper.newSQLHelper(dbContext).getNextValFromSequenceNamed(self, modelName, sequenceName);
        }

        public static EOFetchSpecification primaryKeyFetchSpecificationForEntity(EOEditingContext self, String entityName, EOQualifier eoqualifier, NSArray sortOrderings, NSArray additionalKeys) {
            return ERXEOControlUtilities.primaryKeyFetchSpecificationForEntity(self, entityName, eoqualifier, sortOrderings, additionalKeys);
        }

        public static NSArray primaryKeysMatchingQualifier(EOEditingContext self, String entityName, EOQualifier eoqualifier, NSArray sortOrderings) {
            return ERXEOControlUtilities.primaryKeysMatchingQualifier(self, entityName, eoqualifier, sortOrderings);
        }

        public static EOEnterpriseObject createAndInsertObject(EOEditingContext self, String entityName) {
            return ERXEOControlUtilities.createAndInsertObject(self, entityName);
        }

        public static EOEnterpriseObject createAndInsertObject(EOEditingContext self, String entityName, NSDictionary objectInfo) {
            return ERXEOControlUtilities.createAndInsertObject(self, entityName, objectInfo);
        }

        public static EOEnterpriseObject createAndAddObjectToRelationship(EOEditingContext self, String entityName, String relationshipName, EOEnterpriseObject eo) {
            return ERXEOControlUtilities.createAndAddObjectToRelationship(self, eo, relationshipName, entityName, null);
        }

        public static EOEnterpriseObject createAndAddObjectToRelationship(EOEditingContext self, String entityName, String relationshipName, EOEnterpriseObject eo, NSDictionary objectInfo) {
            return ERXEOControlUtilities.createAndAddObjectToRelationship(self, eo, relationshipName, entityName, null);
        }

        public static EODatabaseContext databaseContextForModelNamed(EOEditingContext self, String name) {
            return EOUtilities.databaseContextForModelNamed(self, name);
        }

        public static NSDictionary destinationKeyForSourceObject(EOEditingContext self, EOEnterpriseObject object, String name) {
            return EOUtilities.destinationKeyForSourceObject(self, object, name);
        }

        public static EOEntity entityForClass(EOEditingContext self, Class theClass) {
            return EOUtilities.entityForClass(self, theClass);
        }

        public static EOEntity entityForObject(EOEditingContext self, EOEnterpriseObject obj) {
            return EOUtilities.entityForObject(self, obj);
        }

        public static EOEntity entityNamed(EOEditingContext self, String name) {
            return EOUtilities.entityNamed(self, name);
        }

        public static EOEnterpriseObject faultWithPrimaryKey(EOEditingContext self, String entityName, NSDictionary pkDict) {
            return EOUtilities.faultWithPrimaryKey(self, entityName, pkDict);
        }

        public static EOEnterpriseObject faultWithPrimaryKeyValue(EOEditingContext self, String entityName, Object value) {
            return EOUtilities.faultWithPrimaryKeyValue(self, entityName, value);
        }

        public static EOEnterpriseObject localInstanceOfObject(EOEditingContext self, EOEnterpriseObject object) {
            return EOUtilities.localInstanceOfObject(self, object);
        }

        public static NSArray localInstancesOfObjects(EOEditingContext self, NSArray objects) {
            return EOUtilities.localInstancesOfObjects(self, objects);
        }

        public static EOModelGroup modelGroup(EOEditingContext self) {
            return EOUtilities.modelGroup(self);
        }

        public static Number objectCountWithQualifier(EOEditingContext self, String entityName, EOQualifier qualifier) {
            return ERXEOControlUtilities.objectCountWithQualifier(self, entityName, qualifier);
        }

        public static NSArray deletedObjectsPKeys(EOEditingContext self) {
            return ERXUtilities.deletedObjectsPKeys(self);
        }

        public static NSDictionary primaryKeyDictionaryForEntity(EOEditingContext self, String entityName) {
            return ERXEOAccessUtilities.primaryKeyDictionaryForEntity(self, entityName);
        }

        public static NSArray rawRowsForSQL(EOEditingContext self, String modelName, String sqlString, NSArray keys) {
            return EOUtilities.rawRowsForSQL(self, modelName, sqlString, keys);
        }

        public static NSArray rawRowsForStoredProcedureNamed(EOEditingContext self, String name, NSDictionary args) {
            return EOUtilities.rawRowsForStoredProcedureNamed(self, name, args);
        }

        public static NSArray rawRowsMatchingKeyAndValue(EOEditingContext self, String name, String key, Object value) {
            return EOUtilities.rawRowsMatchingKeyAndValue(self, name, key, value);
        }

        public static NSDictionary executeStoredProcedureNamed(EOEditingContext self, String name, NSDictionary args) {
            return EOUtilities.executeStoredProcedureNamed(self, name, args);
        }

        public static void evaluateSQLWithEntityNamed(EOEditingContext self, String entityName, String exp) {
            ERXEOAccessUtilities.evaluateSQLWithEntityNamed(self, entityName, exp);
        }

        public static EOEnterpriseObject objectFromRawRow(EOEditingContext self, String name, NSDictionary row) {
            return EOUtilities.objectFromRawRow(self, name, row);
        }

        public static EOEnterpriseObject objectMatchingKeyAndValue(EOEditingContext self, String name, String key, Object value) {
            return EOUtilities.objectMatchingKeyAndValue(self, name, key, value);
        }

        public static EOEnterpriseObject objectMatchingValues(EOEditingContext self, String name, NSDictionary values) {
            return EOUtilities.objectMatchingValues(self, name, values);
        }

        public static NSArray objectsForEntityNamed(EOEditingContext self, String name) {
            return EOUtilities.objectsForEntityNamed(self, name);
        }

        public static NSArray objectsMatchingKeyAndValue(EOEditingContext self, String name, String key, Object value) {
            return EOUtilities.objectsMatchingKeyAndValue(self, name, key, value);
        }

        public static NSArray objectsMatchingValues(EOEditingContext self, String name, NSDictionary values) {
            return EOUtilities.objectsMatchingValues(self, name, values);
        }

        public static NSArray objectsOfClass(EOEditingContext self, Class aClass) {
            return EOUtilities.objectsOfClass(self, aClass);
        }

        public static NSArray objectsWithFetchSpecificationAndBindings(EOEditingContext self, String entityName, String fetchSpecName, NSDictionary bindings) {
            return EOUtilities.objectsWithFetchSpecificationAndBindings(self, entityName, fetchSpecName, bindings);
        }

        public static NSArray objectsWithQualifierFormat(EOEditingContext self, String name, String format, NSArray args) {
            return EOUtilities.objectsWithQualifierFormat(self, name, format, args);
        }

        public static EOEnterpriseObject objectWithFetchSpecificationAndBindings(EOEditingContext self, String entityName, String fetchSpecName, NSDictionary bindings) {
            return EOUtilities.objectWithFetchSpecificationAndBindings(self, entityName, fetchSpecName, bindings);
        }

        public static EOEnterpriseObject objectWithPrimaryKey(EOEditingContext self, String entityName, NSDictionary pkDict) {
            return EOUtilities.objectWithPrimaryKey(self, entityName, pkDict);
        }

        public static EOEnterpriseObject objectWithPrimaryKeyValue(EOEditingContext self, String entityName, Object value) {
            return EOUtilities.objectWithPrimaryKeyValue(self, entityName, value);
        }

        public static EOEnterpriseObject objectWithQualifierFormat(EOEditingContext self, String name, String format, NSArray args) {
            return EOUtilities.objectWithQualifierFormat(self, name, format, args);
        }

        public static NSArray decodeEnterpriseObjectsFromFormValues(EOEditingContext self, NSDictionary values) {
            return ERXEOEncodingUtilities.decodeEnterpriseObjectsFromFormValues(self, values);
        }

        public static Object getAt(EOEnterpriseObject self, Object key) {
            return self.valueForKey(key.toString());
        }

        public static void putAt(EOEnterpriseObject self, Object key, Object object) {
            self.takeValueForKey(object, key.toString());
        }

        public static void refault(EOEnterpriseObject self) {
            ERXEOControlUtilities.refaultObject(self);
        }

        public static void addToObjectOnBothSidesOfRelationshipWithKey(EOEnterpriseObject self, EOEnterpriseObject reference, String key) {
            ERXEOControlUtilities.addObjectToObjectOnBothSidesOfRelationshipWithKey(self, reference, key);
        }

        public static EORelationship relationshipWithKeyPath(EOEnterpriseObject self, String keyPath) {
            return ERXUtilities.relationshipWithObjectAndKeyPath(self, keyPath);
        }

        public static void deplicateRelationship(ERXGenericRecord self, String relationshipName) {
            ERXUtilities.deplicateRelationshipFromEO(self, relationshipName);
        }

        public static void replicateDataToEO(ERXGenericRecord self, ERXGenericRecord other, NSArray attributeNames) {
            ERXUtilities.replicateDataFromEOToEO(self, other, attributeNames);
        }

        public static void replicateRelationshipToEO(ERXGenericRecord self, ERXGenericRecord other, String relationshipName) {
            ERXUtilities.replicateRelationshipFromEOToEO(self, other, relationshipName);
        }

        public static String encodePrimaryKeyForUrl(EOEnterpriseObject self, String seperator, boolean encrypt) {
            return ERXEOEncodingUtilities.encodeEnterpriseObjectPrimaryKeyForUrl(self, seperator, encrypt);
        }

        public static String encodeEntityName(EOEnterpriseObject self) {
            return ERXEOEncodingUtilities.entityNameEncode(self);
        }

        public static NSArray allSubEntities(EOEntity self, boolean includeAbstracts) {
            return ERXUtilities.allSubEntitiesForEntity(self, includeAbstracts);
        }

        public static EOEntity rootParentEntity(EOEntity self) {
            return ERXUtilities.rootParentEntityForEntity(self);
        }

        public static Object getAt(EOGenericRecord self, String key) {
            return self.valueForKey(key);
        }

        public static void putAt(EOGenericRecord self, String key, Object object) {
            self.takeValueForKey(object, key);
        }

        public static void put(EOGenericRecord self, Map map) {
            NSDictionary dictionary = new NSDictionary(map, true);
            self.takeValuesFromDictionary(dictionary);
        }

        public static void put(EOGenericRecord self, NSDictionary dictionary) {
            self.takeValuesFromDictionary(dictionary);
        }

        public static NSArray entities(EOModelGroup self) {
            return ERXUtilities.entitiesForModelGroup(self);
        }

        public static NSArray filesInDirectory(File self, boolean recursive) {
            return ERXFileUtilities.arrayByAddingFilesInDirectory(self, recursive);
        }

        public static byte[] bytes(File self) throws IOException {
            return ERXFileUtilities.bytesFromFile(self);
        }

        public static byte[] bytes(InputStream self) throws IOException {
            return ERXFileUtilities.bytesFromInputStream(self);
        }

        public static String string(File self) throws IOException {
            return ERXFileUtilities.stringFromFile(self);
        }

        public static String string(File self, String encoding) throws IOException {
            return ERXFileUtilities.stringFromFile(self, encoding);
        }

        public static String string(InputStream self) throws IOException {
            return ERXFileUtilities.stringFromInputStream(self);
        }

        public static String string(InputStream self, String encoding) throws IOException {
            return ERXFileUtilities.stringFromInputStream(self, encoding);
        }

        public static void copyFiles(File self, File dstDirectory, boolean deleteOriginals, boolean recursiveCopy, FileFilter filter) throws IOException, FileNotFoundException {
            ERXFileUtilities.copyFilesFromDirectory(self, dstDirectory, deleteOriginals, recursiveCopy, filter);
        }

        public static boolean deleteDirectory(File self) {
            return ERXFileUtilities.deleteDirectory(self);
        }

        public static void deleteFiles(File self, boolean recurseIntoDirectories) {
            ERXFileUtilities.deleteFilesInDirectory(self, recurseIntoDirectories);
        }

        public static void linkFiles(File self, File destination, boolean symbolic, boolean allowUnlink, boolean doNotFollowSymbolicLinks) throws IOException {
            ERXFileUtilities.linkFiles(self, destination, symbolic, allowUnlink, doNotFollowSymbolicLinks);
        }

        public static File unzip(File self, File destination) throws IOException {
            return ERXFileUtilities.unzipFile(self, destination);
        }

        public static void writeToFile(InputStream self, File file) throws IOException {
            ERXFileUtilities.writeInputStreamToFile(self, file);
        }

        public static NSArray arrayValue(List self) {
            return new NSArray(self.toArray());
        }

        public static NSMutableArray mutableArrayValue(List self) {
            return new NSMutableArray(self.toArray());
        }

        public static NSDictionary dictionaryValue(Map self) {
            return new NSDictionary(self.values().toArray(), self.keySet().toArray());
        }

        public static NSMutableDictionary mutableDictionaryValue(Map self) {
            return new NSMutableDictionary(self.values().toArray(), self.keySet().toArray());
        }

        public static NSArray arrayValue(Object self) {
            return ERXValueUtilities.arrayValue(self);
        }

        public static NSArray arrayValueWithDefault(Object self, NSArray def) {
            return ERXValueUtilities.arrayValueWithDefault(self, def);
        }

        public static boolean booleanValue(Object self) {
            return ERXValueUtilities.booleanValue(self);
        }

        public static boolean booleanValueWithDefault(Object self, boolean def) {
            return ERXValueUtilities.booleanValueWithDefault(self, def);
        }

        public static NSDictionary dictionaryValue(Object self) {
            return ERXValueUtilities.dictionaryValue(self);
        }

        public static NSDictionary dictionaryValueWithDefault(Object self, NSDictionary def) {
            return ERXValueUtilities.dictionaryValueWithDefault(self, def);
        }

        public static int intValue(Object self) {
            return ERXValueUtilities.intValue(self);
        }

        public static int intValueWithDefault(Object self, int def) {
            return ERXValueUtilities.intValueWithDefault(self, def);
        }

        public static NSArray lastNMonthsArray(int self) {
            return ERXGraphUtilities.lastNMonthsArray(self);
        }

        public static NSArray lastNMonthsAsStringsArray(int self) {
            return ERXGraphUtilities.lastNMonthsAsStringsArray(self);
        }

        public static NSTimestamp unixDate(Number self) {
            return ERXTimestampUtilities.unixDate(self);
        }

        public static String stackTrace(Throwable self) {
            return ERXUtilities.stackTrace(self);
        }

        public static String localizedTemplateStringForKey(Object self, String key, String framework, NSArray languages) {
            return ERXStringUtilities.localizedTemplateStringWithObjectForKey(self, key, framework, languages);
        }

        public static NSArray sortOrderings(Map self) {
            NSMutableArray orderings = new NSMutableArray();
            for (Iterator iter = self.keySet().iterator(); iter.hasNext(); ) {
                String key = iter.next().toString();
                NSSelector selector = (NSSelector) self.get(key);
                orderings.addObject(new EOSortOrdering(key, selector));
            }
            return orderings;
        }

        public static boolean existsFiles(URL self) {
            return NSPathUtilities.fileExistsAtPathURL(self);
        }

        public static ArrayList asType(NSArray array, Class clazz) {
            return array.arrayList();
        }

        public static NSArray arrayByAddingObjectsFromArrayWithoutDuplicates(NSArray self, NSArray other) {
            return ERXArrayUtilities.arrayByAddingObjectsFromArrayWithoutDuplicates(self, other);
        }

        public static NSDictionary dictionaryGroupedByKeyPath(NSArray self, String keyPath) {
            return ERXArrayUtilities.arrayGroupedByKeyPath(self, keyPath);
        }

        public static NSDictionary dictionaryGroupedByKeyPath(NSArray self, String keyPath, boolean includeNulls, String extraKeyPathForValues) {
            return ERXArrayUtilities.arrayGroupedByKeyPath(self, keyPath, includeNulls, extraKeyPathForValues);
        }

        public static NSDictionary dictionaryGroupedByToManyKeyPath(NSArray self, String keyPath, boolean includeNulls) {
            return ERXArrayUtilities.arrayGroupedByToManyKeyPath(self, keyPath, includeNulls);
        }

        public static NSDictionary dictionaryOfObjectsIndexedByKeyPath(NSArray self, String keyPath) {
            return ERXArrayUtilities.dictionaryOfObjectsIndexedByKeyPath(self, keyPath);
        }

        public static NSArray arrayWithoutDuplicateKeyValue(NSArray self, String key) {
            return ERXArrayUtilities.arrayWithoutDuplicateKeyValue(self, key);
        }

        public static NSArray arrayWithoutDuplicates(NSArray self) {
            return ERXArrayUtilities.arrayWithoutDuplicates(self);
        }

        public static String friendlyDisplayForKeyPath(NSArray self, String attribute, String nullArrayDisplay, String separator, String finalSeparator) {
            return ERXArrayUtilities.friendlyDisplayForKeyPath(self, attribute, nullArrayDisplay, separator, finalSeparator);
        }

        public static NSArray intersectingElements(NSArray self, NSArray other) {
            return ERXArrayUtilities.intersectingElements(self, other);
        }

        public static List list(NSArray self) {
            return new ArrayList(self.vector());
        }

        public static EOArrayDataSource dataSource(NSArray self) {
            return ERXUtilities.dataSourceForArray(self);
        }

        public static NSSet set(NSArray self) {
            return ERXArrayUtilities.setFromArray(self);
        }

        public static boolean containsAnyObjectFromArray(NSArray self, NSArray objects) {
            return ERXArrayUtilities.arrayContainsAnyObjectFromArray(self, objects);
        }

        public static boolean containsArray(NSArray self, NSArray objects) {
            return ERXArrayUtilities.arrayContainsArray(self, objects);
        }

        public static boolean areIdenticalSets(NSArray self, NSArray other) {
            return ERXArrayUtilities.arraysAreIdenticalSets(self, other);
        }

        public static Object computeAvgForKey(NSArray self, String key) {
            return self.valueForKeyPath("@" + NSArray.AverageOperatorName + "." + key);
        }

        public static Object computeCountForKey(NSArray self, String key) {
            return self.valueForKeyPath("@" + NSArray.CountOperatorName + "." + key);
        }

        public static Object computeMaxForKey(NSArray self, String key) {
            return self.valueForKeyPath("@" + NSArray.MaximumOperatorName + "." + key);
        }

        public static Object computeMinForKey(NSArray self, String key) {
            return self.valueForKeyPath("@" + NSArray.MinimumOperatorName + "." + key);
        }

        public static Object computeSumForKey(NSArray self, String key) {
            return self.valueForKeyPath("@" + NSArray.SumOperatorName + "." + key);
        }

        public static Object getAt(NSArray self, int index) {
            return self.objectAtIndex(index);
        }

        public static Object getAt(NSArray self, String key) {
            return self.valueForKey(key);
        }

        public static Object getAt(NSArray self, NSArray paths) {
            return ERXArrayUtilities.valuesForKeyPaths(self, paths);
        }

        public static Object getAt(NSArray self, Range range) {
            return getAt(self, range);
        }

        public static Object getAt(NSArray self, ObjectRange range) {
            return getAt(self, range);
        }

        public static List getAt(NSArray self, Collection indices) {
            NSArray answer = new NSMutableArray(indices.size());
            for (Iterator iter = indices.iterator(); iter.hasNext(); ) {
                Object value = iter.next();
                if (value instanceof Range) {
                    answer.addAll((Collection) getAt(self, (Range) value));
                } else if (value instanceof List) {
                    answer.addAll(getAt(self, (List) value));
                } else {
                    int idx = DefaultTypeTransformation.intUnbox(value);
                    answer.add(getAt(self, idx));
                }
            }
            return answer;
        }

        public static Object getAt(NSArray self, IntRange range) {
            RangeInfo info = subListBorders(self.size(), range);
            List answer = self.subarrayWithRange(new NSRange(info.from, (info.to - info.from)));
            if (info.reverse) {
                answer = DefaultGroovyMethods.reverse(answer);
            }
            return answer;
        }

        public static int count(NSArray self, Object object) {
            int count = 0;
            for (Enumeration e = self.objectEnumerator(); e.hasMoreElements(); ) {
                if (e.nextElement().equals(object)) count++;
            }
            return count;
        }

        public static NSArray valuesForKeyPaths(NSArray self, NSArray paths) {
            return ERXArrayUtilities.valuesForKeyPaths(self, paths);
        }

        public static NSArray flatten(NSArray self) {
            return ERXArrayUtilities.flatten(self);
        }

        public static NSArray filteredArrayWithQualifier(NSArray self, EOQualifier qualifier) {
            return EOQualifier.filteredArrayWithQualifier(self, qualifier);
        }

        public static NSArray filteredArrayWithEntityFetchSpecification(NSArray self, String fetchSpec, String entity) {
            return ERXArrayUtilities.filteredArrayWithEntityFetchSpecification(self, fetchSpec, entity);
        }

        public static NSArray filteredArrayWithEntityFetchSpecification(NSArray self, String entity, String fetchSpec, NSDictionary bindings) {
            return ERXArrayUtilities.filteredArrayWithEntityFetchSpecification(self, entity, fetchSpec, bindings);
        }

        public static NSArray filteredArrayWithQualifierEvaluation(NSArray self, EOQualifierEvaluation qualifier) {
            return ERXArrayUtilities.filteredArrayWithQualifierEvaluation(self, qualifier);
        }

        public static NSArray sortedArrayUsingKeyOrderArray(NSArray self, NSArray sortOrderings) {
            return EOSortOrdering.sortedArrayUsingKeyOrderArray(self, sortOrderings);
        }

        public static NSArray sortedArraySortedWithKey(NSArray self, String key, NSSelector selector) {
            return ERXArrayUtilities.sortedArraySortedWithKey(self, key, selector);
        }

        public static NSMutableArray sortedMutableArraySortedWithKey(NSArray self, String key) {
            return ERXArrayUtilities.sortedMutableArraySortedWithKey(self, key);
        }

        public static NSArray sort(NSArray self) {
            ArrayList list = self.arrayList();
            Collections.sort(list);
            NSArray answer = new NSArray(list, true);
            return answer;
        }

        public static NSArray sort(NSArray self, String key) {
            return ERXArrayUtilities.sortedArraySortedWithKey(self, key);
        }

        public static NSArray sort(NSArray self, String key, NSSelector selector) {
            return ERXArrayUtilities.sortedArraySortedWithKey(self, key, selector);
        }

        public static NSArray sortAsc(NSArray self, String key) {
            return ERXArrayUtilities.sortedArraySortedWithKey(self, key);
        }

        public static NSArray sortDesc(NSArray self, String key) {
            return ERXArrayUtilities.sortedArraySortedWithKey(self, key, EOSortOrdering.CompareDescending);
        }

        public static NSArray sortInsensitiveAsc(NSArray self, String key) {
            return ERXArrayUtilities.sortedArraySortedWithKey(self, key, EOSortOrdering.CompareCaseInsensitiveAscending);
        }

        public static Object sortInsensitiveDesc(NSArray self, String key) {
            return ERXArrayUtilities.sortedArraySortedWithKey(self, key, EOSortOrdering.CompareCaseInsensitiveDescending);
        }

        public static NSArray plus(NSArray left, NSArray right) {
            return left.arrayByAddingObjectsFromArray(right);
        }

        public static NSArray plus(NSArray left, Object right) {
            return left.arrayByAddingObject(right);
        }

        public static NSArray minus(NSArray left, NSArray right) {
            return ERXArrayUtilities.arrayMinusArray(left, right);
        }

        public static NSArray minus(NSArray left, Object right) {
            return ERXArrayUtilities.arrayMinusObject(left, right);
        }

        public static NSArray reverse(NSArray self) {
            return ERXArrayUtilities.reverse(self);
        }

        public static void each(NSArray self, Closure closure) {
            for (Enumeration e = self.objectEnumerator(); e.hasMoreElements(); ) {
                closure.call(e.nextElement());
            }
        }

        public static void eachWithIndex(NSArray self, Closure closure) {
            int counter = 0;
            for (Enumeration e = self.objectEnumerator(); e.hasMoreElements(); ) {
                Object[] objects = new Object[2];
                objects[0] = e.nextElement();
                objects[1] = new Integer(counter++);
                closure.call(objects);
            }
        }

        public static void reverseEach(NSArray self, Closure closure) {
            for (Enumeration e = self.reverseObjectEnumerator(); e.hasMoreElements(); ) {
                closure.call(e.nextElement());
            }
        }

        public static NSArray collect(NSArray self, Closure closure) {
            NSMutableArray answer = new NSMutableArray();
            for (Enumeration e = self.objectEnumerator(); e.hasMoreElements(); ) {
                answer.addObject(closure.call(e.nextElement()));
                if (closure.getDirective() == Closure.DONE) {
                    break;
                }
            }
            return answer;
        }

        public static Object find(NSArray self, Closure closure) {
            for (Enumeration e = self.objectEnumerator(); e.hasMoreElements(); ) {
                Object value = e.nextElement();
                if (DefaultTypeTransformation.castToBoolean(closure.call(value))) {
                    return value;
                }
            }
            return null;
        }

        public static NSArray findAll(NSArray self, Closure closure) {
            NSMutableArray answer = new NSMutableArray(self);
            for (Enumeration e = self.objectEnumerator(); e.hasMoreElements(); ) {
                Object value = e.nextElement();
                if (DefaultTypeTransformation.castToBoolean(closure.call(value))) {
                    answer.addObject(value);
                }
            }
            return answer;
        }

        public static Object inject(NSArray self, Object value, Closure closure) {
            Object[] params = new Object[2];
            for (Enumeration e = self.objectEnumerator(); e.hasMoreElements(); ) {
                Object item = e.nextElement();
                params[0] = value;
                params[1] = item;
                value = closure.call(params);
            }
            return value;
        }

        public static NSDictionary dictionaryOfFormValues(NSArray self, String separator, boolean encrypt) {
            return ERXEOEncodingUtilities.dictionaryOfFormValuesForEnterpriseObjects(self, separator, encrypt);
        }

        public static String stringByEncodingPrimaryKeys(NSArray self, String separator, boolean encrypt) {
            return ERXEOEncodingUtilities.encodeEnterpriseObjectsPrimaryKeyForUrl(self, separator, encrypt);
        }

        protected static class _RangeInfo {

            protected int from, to;

            protected boolean reverse;

            public _RangeInfo(int from, int to, boolean reverse) {
                this.from = from;
                this.to = to;
                this.reverse = reverse;
            }

            public int getFrom() {
                return this.from;
            }

            public int getTo() {
                return this.to;
            }
        }

        public static Object getAt(NSDictionary self, Object key) {
            return self.objectForKey(key);
        }

        public static void each(NSDictionary self, Closure closure) {
            for (Enumeration e = self.keyEnumerator(); e.hasMoreElements(); ) {
                Object key = e.nextElement();
                Map.Entry entry = new MapEntry(key, self.objectForKey(key));
                if (closure.getMaximumNumberOfParameters() == 2) {
                    closure.call(new Object[] { entry.getKey(), entry.getValue() });
                } else {
                    closure.call(entry);
                }
            }
        }

        public static void eachKey(NSDictionary self, Closure closure) {
            for (Enumeration e = self.keyEnumerator(); e.hasMoreElements(); ) {
                closure.call(e.nextElement());
            }
        }

        public static void eachValue(NSDictionary self, Closure closure) {
            for (Enumeration e = self.objectEnumerator(); e.hasMoreElements(); ) {
                closure.call(e.nextElement());
            }
        }

        public static NSArray collect(NSDictionary self, Closure closure) {
            NSMutableArray answer = new NSMutableArray();
            for (Enumeration e = self.keyEnumerator(); e.hasMoreElements(); ) {
                Object key = e.nextElement();
                Map.Entry entry = new MapEntry(key, self.objectForKey(key));
                answer.addObject(closure.call(entry));
            }
            return new NSArray(answer);
        }

        public static Object find(NSDictionary self, Closure closure) {
            for (Enumeration e = self.keyEnumerator(); e.hasMoreElements(); ) {
                Object key = e.nextElement();
                Map.Entry entry = new MapEntry(key, self.objectForKey(key));
                if (DefaultTypeTransformation.castToBoolean(closure.call(entry))) {
                    return entry;
                }
            }
            return null;
        }

        public static NSDictionary dictionaryByRemovingFromDictionaryKeysInArray(NSDictionary self, List array) {
            NSArray list;
            if (array instanceof NSArray) {
                list = (NSArray) array;
            } else {
                list = new NSArray(array, false);
            }
            return ERXDictionaryUtilities.dictionaryByRemovingFromDictionaryKeysInArray(self, list);
        }

        public static NSDictionary dictionaryWithDictionary(NSDictionary self, NSDictionary other) {
            return ERXDictionaryUtilities.dictionaryWithDictionaryAndDictionary(self, other);
        }

        public static Map map(NSDictionary self) {
            return new HashMap(self.hashtable());
        }

        public static NSArray sortOrderings(NSDictionary self) {
            NSMutableArray orderings = new NSMutableArray();
            for (Enumeration e = self.keyEnumerator(); e.hasMoreElements(); ) {
                String key = e.nextElement().toString();
                NSSelector selector = (NSSelector) self.objectForKey(key);
                orderings.addObject(new EOSortOrdering(key, selector));
            }
            return orderings;
        }

        public static Object get(NSKeyValueCoding self, String key) {
            return self.valueForKey(key);
        }

        ;

        public static void set(NSKeyValueCoding self, String key, Object value) {
            self.takeValueForKey(value, key);
        }

        public static Object valueForKey(Object self, String key) {
            return NSKeyValueCoding.Utility.valueForKey(self, key);
        }

        public static void takeValueForKey(Object self, Object value, String key) {
            NSKeyValueCoding.Utility.takeValueForKey(self, value, key);
        }

        public static Object valueForKeyPath(Object self, String keyPath) {
            if (self instanceof NSKeyValueCodingAdditions) {
                return ((com.webobjects.foundation.NSKeyValueCodingAdditions) self).valueForKeyPath(keyPath);
            }
            return com.webobjects.foundation.NSKeyValueCodingAdditions.DefaultImplementation.valueForKeyPath(self, keyPath);
        }

        public static void takeValueForKeyPath(Object self, Object value, String keyPath) {
            if (self instanceof NSKeyValueCodingAdditions) {
                ((com.webobjects.foundation.NSKeyValueCodingAdditions) self).takeValueForKeyPath(value, keyPath);
            } else {
                com.webobjects.foundation.NSKeyValueCodingAdditions.DefaultImplementation.takeValueForKeyPath(self, value, keyPath);
            }
        }

        public static void putAt(NSMutableArray self, int index, Object object) {
            self.replaceObjectAtIndex(object, index);
        }

        public static NSMutableArray leftShift(NSMutableArray self, Object object) {
            self.addObject(object);
            return self;
        }

        public static NSMutableArray push(NSMutableArray self, Object object) {
            self.addObject(object);
            return self;
        }

        public static NSMutableArray unshift(NSMutableArray self, Object object) {
            self.insertObjectAtIndex(object, 0);
            return self;
        }

        public static void addObjectsFromArrayWithoutDuplicates(NSMutableArray self, NSArray other) {
            ERXArrayUtilities.addObjectsFromArrayWithoutDuplicates(self, other);
        }

        public static void shiftObjectLeft(NSMutableArray self, Object object) {
            ERXArrayUtilities.shiftObjectLeft(self, object);
        }

        public static void shiftObjectRight(NSMutableArray self, Object object) {
            ERXArrayUtilities.shiftObjectRight(self, object);
        }

        public static Object pop(NSMutableArray self) {
            if (self.count() == 0) return null;
            return self.removeLastObject();
        }

        public static Object shift(NSMutableArray self) {
            if (self.count() == 0) return null;
            return self.removeObjectAtIndex(0);
        }

        public static void filterArrayWithQualifier(NSMutableArray self, EOQualifier qualifier) {
            EOQualifier.filterArrayWithQualifier(self, qualifier);
        }

        public static void sortArrayUsingKeyOrderArray(NSMutableArray self, NSArray sortOrderings) {
            EOSortOrdering.sortArrayUsingKeyOrderArray(self, sortOrderings);
        }

        public static void sortArrayWithKey(NSMutableArray self, String key) {
            ERXArrayUtilities.sortArrayWithKey(self, key, EOSortOrdering.CompareCaseInsensitiveAscending);
        }

        public static void sortArrayWithKey(NSMutableArray self, String key, NSSelector selector) {
            ERXArrayUtilities.sortArrayWithKey(self, key, selector);
        }

        public static void putAt(NSMutableDictionary self, String key, Object object) {
            self.setObjectForKey(object, key);
        }

        public static GregorianCalendar calendar(NSTimestamp self) {
            return ERXTimestampUtility.calendarForTimestamp(self);
        }

        public static NSTimestamp timestampByAddingTime(NSTimestamp self, NSTimestamp other) {
            return ERXTimestampUtilities.timestampByAddingTime(self, other);
        }

        public static Integer unixTimestamp(NSTimestamp self) {
            return ERXTimestampUtilities.unixTimestamp(self);
        }

        public static int dayOfCommonEra(NSTimestamp self) {
            return ERXTimestampUtility.dayOfCommonEra(self);
        }

        public static int dayOfWeek(NSTimestamp self) {
            return ERXTimestampUtility.dayOfWeek(self);
        }

        public static int dayOfYear(NSTimestamp self) {
            return ERXTimestampUtility.dayOfYear(self);
        }

        public static int hourOfDay(NSTimestamp self) {
            return ERXTimestampUtility.hourOfDay(self);
        }

        public static int minuteOfHour(NSTimestamp self) {
            return ERXTimestampUtility.minuteOfHour(self);
        }

        public static int monthOfYear(NSTimestamp self) {
            return ERXTimestampUtility.monthOfYear(self);
        }

        public static int yearOfCommonEra(NSTimestamp self) {
            return ERXTimestampUtility.yearOfCommonEra(self);
        }

        public static int compareTo(NSTimestamp left, NSTimestamp right) {
            int result = left.compare(right);
            if (result == NSComparator.OrderedAscending) return -1; else if (result == NSComparator.OrderedDescending) return 1; else return 0;
        }

        public static NSTimestamp timestamp(String self) {
            return ERXTimestampUtilities.timestampForString(self);
        }

        public static NSNumberFormatter numberFormatterForPattern(String self) {
            return ERXNumberFormatter.numberFormatterForPattern(self);
        }

        public static NSSelector sortSelector(String self) {
            return ERXArrayUtilities.sortSelectorWithKey(self);
        }

        public static Integer integer(String self) {
            return ERXStringUtilities.integerWithString(self);
        }

        public static String keyPathWithoutFirstProperty(String self) {
            return ERXStringUtilities.keyPathWithoutFirstProperty(self);
        }

        public static String keyPathWithoutLastProperty(String self) {
            return ERXStringUtilities.keyPathWithoutLastProperty(self);
        }

        public static String lastPropertyKey(String self) {
            return ERXStringUtilities.lastPropertyKeyInKeyPath(self);
        }

        public static String escapeApostrophe(String self) {
            return ERXUtilities.escapeApostrophe(self);
        }

        public static String escapeNonBasicLatinChars(char self) {
            return ERXStringUtilities.escapeNonBasicLatinChars(self);
        }

        public static String escapeNonBasicLatinChars(String self) {
            return ERXStringUtilities.escapeNonBasicLatinChars(self);
        }

        public static String escapeSpace(String self) {
            return ERXStringUtilities.escapeSpace(self);
        }

        public static String removeExtraDotsFromVersion(String self) {
            return ERXStringUtilities.removeExtraDotsFromVersionString(self);
        }

        public static String replaceStringByString(String self, String old, String newString) {
            return ERXStringUtilities.replaceStringByStringInString(self, old, newString);
        }

        public static String appendPathComponent(String self, String component) {
            return NSPathUtilities.stringByAppendingPathComponent(self, component);
        }

        public static String appendPathExtension(String self, String extension) {
            return NSPathUtilities.stringByAppendingPathExtension(self, extension);
        }

        public static String deleteLastPathComponent(String self) {
            return NSPathUtilities.stringByDeletingLastPathComponent(self);
        }

        public static String deletePathExtension(String self) {
            return NSPathUtilities.stringByDeletingPathExtension(self);
        }

        public static String normalizeExistingPath(String self) {
            return NSPathUtilities.stringByNormalizingExistingPath(self);
        }

        public static String normalizePath(String self) {
            return NSPathUtilities.stringByNormalizingPath(self);
        }

        public static EOQualifier qualifier(String self) {
            return EOQualifier.qualifierWithQualifierFormat(self, null);
        }

        public static EOQualifier qualifierWithArray(String self, NSArray arguments) {
            return EOQualifier.qualifierWithQualifierFormat(self, arguments);
        }

        public static EOQualifier qualifierWithArray(String self, List arguments) {
            return EOQualifier.qualifierWithQualifierFormat(self, new NSArray(arguments.toArray()));
        }

        public static void appendSeparatorIfLastNot(StringBuffer self, char separator, char not) {
            ERXStringUtilities.appendSeparatorIfLastNot(separator, not, self);
        }

        public static String displayName(String self) {
            return ERXStringUtilities.displayNameForKey(self);
        }

        public static double distance(String self, String other) {
            return ERXStringUtilities.distance(self, other);
        }

        public static String firstPropertyKey(String self) {
            return ERXStringUtilities.firstPropertyKeyInKeyPath(self);
        }

        public static int indexOfNumeric(String self) {
            return ERXStringUtilities.indexOfNumericInString(self);
        }

        public static int indexOfNumeric(String self, int fromIndex) {
            return ERXStringUtilities.indexOfNumericInString(self, fromIndex);
        }

        public static String lastPathComponent(String self) {
            return NSPathUtilities.lastPathComponent(self);
        }

        public static String pathExtension(String self) {
            return NSPathUtilities.pathExtension(self);
        }

        public static String toHexString(char self) {
            return ERXStringUtilities.toHexString(self);
        }

        public static String toHexString(String self) {
            return ERXStringUtilities.toHexString(self);
        }

        public static String toLowerCase(String self) {
            return ERXStringUtilities.toLowerCase(self);
        }

        public static String decodedEntityName(String self) {
            return ERXEOEncodingUtilities.entityNameDecode(self);
        }

        @SuppressWarnings("deprecation")
        public static boolean booleanValueForBindingWithDefault(WOComponent self, String binding, boolean def) {
            return ERXValueUtilities.booleanValueForBindingOnComponentWithDefault(binding, self, def);
        }

        public static EOEditingContext ec(WOComponent self) {
            return self.session().defaultEditingContext();
        }

        public static void show(Object self, Object value) {
            WOTranscript.defaultTranscript().show(value);
        }

        public static boolean WOGroovyIsLoaded() {
            return true;
        }
    }
}
