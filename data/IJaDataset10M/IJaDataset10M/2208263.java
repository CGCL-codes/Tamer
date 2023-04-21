package listing.grammar;

import static net.sf.extcos.internal.JavaClassResourceType.javaClasses;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import net.sf.extcos.ComponentQuery;
import net.sf.extcos.selector.TypeFilterJunction;
import net.sf.extcos.selector.annotation.ArgumentsDescriptor;

public class TestClassSelector extends ComponentQuery {

    @Override
    protected void query() {
        select();
        select(javaClasses());
        select().from();
        select().from("");
        select().from("", "");
        select().from("").returning(all());
        select().from().returning(allExtending(Object.class));
        select().from().returning(allImplementing(Serializable.class));
        select().from().returning(allImplementing(Serializable.class, Cloneable.class));
        select().from().returning(allAnnotatedWith(Annotation.class));
        select().from().returning(allAnnotatedWith(Annotation.class, withArgument(key("key"), value(new Object()))));
        select().from().returning(allAnnotatedWith(Annotation.class, withArguments(and(mapping(key(""), value("")), mapping(key(""), value(""))))));
        select().from().returning(allAnnotatedWith(Annotation.class, withArguments(or(mapping(key(""), value("")), mapping(key(""), value(""))))));
        select().from().returning(allAnnotatedWith(Annotation.class, withArguments(and(or(mapping(key(""), value("")), mapping(key(""), value(""))), or(mapping(key(""), value("")), mapping(key(""), value("")))))));
        select().from().returning(allAnnotatedWith(Annotation.class, withArguments(or(and(mapping(key(""), value("")), mapping(key(""), value(""))), and(mapping(key(""), value("")), mapping(key(""), value(""))), mapping(key(""), value(""))))));
        select().from().returning(allBeing(and(subclassOf(Object.class), implementorOf(Serializable.class), implementorOf(Cloneable.class), annotatedWith(Annotation.class, withArguments(or(and(mapping(key(""), value("")), mapping(key(""), value(""))), and(mapping(key(""), value("")), mapping(key(""), value(""))), mapping(key(""), value(""))))), annotatedWith(Annotation.class))));
        select().from().returning(allBeing(or(annotatedWith(Annotation.class, withArguments(or(and(mapping(key(""), value("")), mapping(key(""), value(""))), and(mapping(key(""), value("")), mapping(key(""), value(""))), mapping(key(""), value(""))))), subclassOf(Object.class), subclassOf(Object.class), and(subclassOf(Object.class), implementorOf(Serializable.class)))));
        select().from().returning(allBeing(and(or(subclassOf(Object.class), implementorOf(Serializable.class), and(implementorOf(Serializable.class), implementorOf(Cloneable.class))))));
        select().from().andStore().returning(none());
        select().from().andStore().returning(allMerged());
        select().from().andStore().returning(all());
        select().from().andStore().returning(allExtending(Object.class));
        select().from().andStore().returning(allImplementing(Serializable.class));
        select().from().andStore().returning(allAnnotatedWith(Annotation.class));
        select().from().andStore().returning(allBeing((TypeFilterJunction) null));
        select().from().andStore(thoseExtending(Object.class).into(new HashSet<Class<?>>()), thoseImplementing(Serializable.class).into(new HashSet<Class<? extends Serializable>>()), thoseImplementing(Serializable.class, Cloneable.class).into(new HashSet<Class<?>>()), thoseAnnotatedWith(Annotation.class).into(new HashSet<Class<?>>()), thoseAnnotatedWith(Annotation.class, (ArgumentsDescriptor) null).into(new HashSet<Class<?>>()), thoseBeing((TypeFilterJunction) null).into(new HashSet<Class<?>>()));
    }
}
