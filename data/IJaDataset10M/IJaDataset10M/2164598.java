package org.colombbus.annotation.processor;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import org.colombbus.annotation.LocalizableClass;

/**
 * The factory of the dictionaries used to localize types and methods
 *
 */
public class DictionaryStore {

    /** The processing environment furnished by the APT */
    private ProcessingEnvironment processingEnv;

    /**
     * The table of dictionaries
     * <ul>
     * <li>key: the address of the properties file</li>
     * <li>value:</li>
     * </ul>
     */
    private final Map<String, Dictionary> dicMap = new Hashtable<String, Dictionary>();

    /**
     * Create a new factory
     *
     * @param processingEnv
     *            the annotation processing environment
     */
    public DictionaryStore(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    /**
     * Find the dictionary of the a specific type with a given local
     *
     * @param typeElem
     *            the type to translate
     * @param locale
     *            the translation local
     * @return the dictionary to use to translate the class
     */
    public Dictionary findDictionary(TypeElement typeElem, Locale locale) {
        String resourceName = buildResourceName(typeElem, locale);
        Dictionary dic = dicMap.get(resourceName);
        if (dic == null) {
            DictionnaryBuilder dicBuilder = new DictionnaryBuilder();
            dicBuilder.setProcessingEnv(processingEnv);
            dicBuilder.setLocale(locale);
            dicBuilder.setResourceName(resourceName);
            dicBuilder.setTypeElement(typeElem);
            dic = dicBuilder.createDictionnary();
            dicMap.put(resourceName, dic);
        }
        return dic;
    }

    /** Properties file extension */
    private static final String PROP_EXT = ".properties";

    /**
     * Get the localized resource name of the bundle of a type.
     * <p>
     * For instance, the following declaration type;
     *
     * <pre>
     * package org.colombbus;
     * &#064;LocalizableClass(value=&quot;MyClass&quot;,bundle=&quot;mytranslations&quot;)
     * public class MyClass {...}
     * </pre>
     *
     * with a french language and a France country, the resource name
     * <code>org/colombbus/mytranslations_fr_FR.properties</code>
     * </p>
     *
     * @param typeElem
     *            the type
     * @param locale
     *            the localization
     * @return <code>my.package.bundle_&lt;localization&gt;.properties</code>
     */
    private String buildResourceName(TypeElement typeElem, Locale locale) {
        LocalizableClass localizeInfo = typeElem.getAnnotation(LocalizableClass.class);
        String packageName = elementUtils().getPackageOf(typeElem).getQualifiedName().toString();
        StringBuilder baseName = new StringBuilder();
        baseName.append(packageName).append('.').append(localizeInfo.bundle());
        StringBuilder resourceName = new StringBuilder();
        resourceName.append('/').append(baseName.toString().replace('.', '/')).append('_').append(locale.toString()).append(PROP_EXT);
        return resourceName.toString();
    }

    private Elements elementUtils() {
        return processingEnv.getElementUtils();
    }

    /**
     * Get the list of the dictionaries in the buffer
     *
     * @return a collection of dictionaries, never <code>null</code>
     */
    public Collection<Dictionary> dictionaries() {
        return dicMap.values();
    }
}
