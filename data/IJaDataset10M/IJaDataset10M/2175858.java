package com.antlersoft.bbq.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import com.antlersoft.odb.ExactMatchIndexEnumeration;
import com.antlersoft.odb.IndexObjectDB;
import com.antlersoft.odb.KeyGenerator;
import com.antlersoft.odb.ObjectKeyHashMap;
import com.antlersoft.odb.ObjectRef;
import com.antlersoft.odb.ObjectRefKey;
import com.antlersoft.odb.Persistent;
import com.antlersoft.odb.PersistentImpl;
import com.antlersoft.query.EmptyEnum;
import com.antlersoft.query.SingleEnum;

/**
 * Base class for annotations-- data added to an element in the analyzed system
 * that is available on reflection.
 * 
 * An annotation is a relationship between the annotated element and the
 * class of the annotation type in the analyzed system.
 * 
 * @param C Type of object representing a class in the analyzed system
 * 
 * @author Michael A. MacDonald
 *
 */
public class DBAnnotationBase implements AnnotationBase, Serializable {

    public static final String ANNOTATION_CLASS = "ANNOTATION_CLASS";

    private ObjectRef<DBClassBase> annotationClass;

    private ObjectRef<DBAnnotatable> annotatedObject;

    public DBAnnotationBase(DBClassBase annotationClass, DBAnnotatable annotated) {
        this.annotationClass = new ObjectRef<DBClassBase>(annotationClass);
        this.annotatedObject = new ObjectRef<DBAnnotatable>(annotated);
    }

    public DBClassBase getAnnotationClass() {
        return annotationClass.getReferenced();
    }

    public DBAnnotatable getAnnotatedObject() {
        return annotatedObject.getReferenced();
    }

    public ObjectRef<DBClassBase> getAnnotationClassRef() {
        return annotationClass;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(annotationClass.getReferenced().toString());
        sb.append(" annotation of ");
        sb.append(annotatedObject.getReferenced().toString());
        return sb.toString();
    }

    public static Enumeration getAnnotationsWithType(IndexObjectDB db, Persistent annotationClass) {
        return new ExactMatchIndexEnumeration(db.greaterThanOrEqualEntries(ANNOTATION_CLASS, new ObjectRefKey(annotationClass)));
    }

    public static class AnnotationUpdater {

        /** Map of annotation classes to values in the current annotated object */
        private ObjectKeyHashMap<DBClassBase, AnnotationBase> existing;

        private ArrayList<AnnotationBase> after;

        boolean changed;

        private AnnotationCollection collection;

        private ArrayList<AnnotationBase> getAfter() {
            if (after == null) after = new ArrayList<AnnotationBase>();
            return after;
        }

        public AnnotationUpdater(DBAnnotatable target) {
            after = null;
            changed = false;
            collection = target.getAnnotationCollection();
            Enumeration e = collection.getAnnotations();
            if (e.hasMoreElements()) {
                existing = new ObjectKeyHashMap<DBClassBase, AnnotationBase>();
                while (e.hasMoreElements()) {
                    AnnotationBase annotation = (AnnotationBase) e.nextElement();
                    existing.put(annotation.getAnnotationClassRef(), annotation);
                }
            }
        }

        public AnnotationBase getExisting(DBClassBase annotationclass) {
            AnnotationBase result = null;
            if (existing != null) result = existing.get(annotationclass);
            if (result != null) {
                existing.remove(annotationclass);
                getAfter().add(result);
            }
            return result;
        }

        /**
		 * Add a newly created annotation to the collection
		 * @param newannotation
		 */
        public void addNew(DBAnnotationBase newannotation) {
            changed = true;
            getAfter().add(newannotation);
        }

        public boolean cleanup(IndexObjectDB db) {
            if (existing != null && existing.size() > 0) {
                changed = true;
                for (AnnotationBase i : existing.values()) {
                    db.deleteObject(i);
                }
            }
            if (changed) {
                if (collection.annotations == null) {
                    collection.annotations = new ArrayList<ObjectRef<AnnotationBase>>(after.size());
                } else collection.annotations.clear();
                for (AnnotationBase ann : after) {
                    collection.annotations.add(new ObjectRef<AnnotationBase>(ann));
                }
            }
            return changed;
        }
    }

    /**
	 * @author Michael A. MacDonald
	 *
	 */
    public static class AnnotationClassKeyGenerator implements KeyGenerator {

        public Comparable generateKey(Object o1) {
            return new ObjectRefKey(((AnnotationBase) o1).getAnnotationClassRef());
        }
    }
}
