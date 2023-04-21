package net.sourceforge.freejava.annotation.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import net.sourceforge.freejava.annotation.util.impl.DiscardableArrayList;
import net.sourceforge.freejava.annotation.util.impl.IDiscardableList;
import net.sourceforge.freejava.annotation.util.impl.UniqueArrayList;

public class TagStyleAnnotationUtil extends AnnotationParseUtil {

    protected static interface ValueFunction<A extends Annotation> {

        String[] value(A annotation);
    }

    /**
     * @return Empty string array if no author. Or
     */
    protected static final <A extends Annotation> String[] getTags(Class<A> annotationType, ValueFunction<A> valuef, AnnotatedElement annotatedElement) {
        if (annotatedElement == null) throw new NullPointerException("annotatedElement");
        A annotation = annotatedElement.getAnnotation(annotationType);
        if (annotation == null) return EMPTY_STRING_ARRAY;
        String[] tags = valuef.value(annotation);
        return tags;
    }

    /**
     * @param mergeAllOccurences
     * @param reverseOrder
     * @return Empty string array if no author. Or
     */
    protected static final <A extends Annotation> String[] getClassTags(Class<A> annotationType, ValueFunction<A> valuef, Class<?> clazz, boolean mergeAllOccurences, boolean reverseOrder, final boolean unique) {
        if (clazz == null) throw new NullPointerException("clazz");
        if (!mergeAllOccurences && !unique) return getTags(annotationType, valuef, clazz);
        IDiscardableList<String> list = unique ? new UniqueArrayList<String>() : new DiscardableArrayList<String>();
        if (!mergeAllOccurences) {
            String[] tags = getTags(annotationType, valuef, clazz);
            for (int i = 0; i < tags.length; i++) list.add(tags[i]);
        } else {
            while (clazz != null) {
                A _a = AnnotationUtil.getDeclaredAnnotation(clazz, annotationType);
                if (_a != null) {
                    String[] local = valuef.value(_a);
                    assert local != null;
                    int insertPoint = 0;
                    for (int i = 0; i < local.length; i++) {
                        String author = local[i];
                        if (reverseOrder) {
                            list.add(author);
                        } else {
                            if (unique) {
                                int lastOccurence = list.indexOf(author);
                                if (lastOccurence != -1) {
                                    list.remove(lastOccurence);
                                    if (lastOccurence < insertPoint) insertPoint--;
                                }
                            }
                            list.add(insertPoint++, author);
                        }
                    }
                }
                clazz = clazz.getSuperclass();
                if (clazz != null) continue;
                break;
            }
        }
        return list.toArray(EMPTY_STRING_ARRAY);
    }
}
