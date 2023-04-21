package com.xavax.jsf.vms.rb;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import javax.annotation.PostConstruct;
import com.xavax.jsf.util.CollectionFactory;
import com.xavax.jsf.vms.AbstractVocabularyService;
import com.xavax.jsf.vms.Concept;
import com.xavax.jsf.vms.DisplayTextMap;
import com.xavax.jsf.vms.ValueSet;

/**
 * ResourceBundleVocabularyService is a vocabulary service that loads
 * all of its information from a list of resource bundles.
 */
public class ResourceBundleVocabularyService extends AbstractVocabularyService {

    /**
   * Construct a ResourceBundleVocabularyService.
   */
    public ResourceBundleVocabularyService() {
    }

    /**
   * Initialize this service.
   */
    @PostConstruct
    public void initialize() {
        load();
    }

    /**
   * Load the vocabulary data.
   */
    private void load() {
        final String method = "load";
        enter(method);
        load(labelBundles, new RBLabelParser(), labels, labelNotFound);
        load(messageBundles, new RBMessageParser(), messages, messageNotFound);
        load(valueSetBundles, new RBValueSetParser());
        leave(method);
    }

    /**
   * Flush any cached vocabulary data and reload the vocabulary.
   */
    public void reload() {
        super.reload();
        ResourceBundle.clearCache();
        load();
    }

    /**
   * Load the label or message resource bundles.
   *
   * @param bundles  contains a comma-separated list of resource bundles.
   * @param parser   the parser to use for parsing each bundle entry.
   * @param map      the map of localized concepts.
   */
    private void load(String bundles, RBConceptParser parser, Map<Locale, DisplayTextMap> map, String errorMessage) {
        final String method = "load";
        enter(method);
        if (bundles != null && !bundles.equals("")) {
            List<ResourceBundle> list = getBundles(bundles);
            for (ResourceBundle bundle : list) {
                String pathname = bundle.getClass().getName();
                Locale locale = bundle.getLocale();
                DisplayTextMap localizedMap = map.get(locale);
                if (localizedMap == null) {
                    localizedMap = new DisplayTextMap(errorMessage);
                    map.put(locale, localizedMap);
                }
                Set<String> keys = bundle.keySet();
                for (String key : keys) {
                    String value = bundle.getString(key);
                    parser.parse(pathname, locale, key, value, concepts, localizedMap);
                }
            }
        }
        leave(method);
    }

    /**
   * Load the value set resource bundles.
   *
   * @param bundles  contains a comma-separated list of resource bundles.
   * @param parser   the parser to use for parsing each bundle entry.
   */
    private void load(String bundles, RBValueSetParser parser) {
        final String method = "load";
        enter(method);
        if (bundles != null && !bundles.equals("")) {
            List<ResourceBundle> list = getBundles(bundles);
            for (ResourceBundle bundle : list) {
                String pathname = bundle.getClass().getName();
                Set<String> keys = bundle.keySet();
                for (String key : keys) {
                    String value = bundle.getString(key);
                    parser.parse(pathname, key, value, concepts, valueSets);
                }
            }
        }
        Set<String> keys = valueSets.keySet();
        for (String key : keys) {
            ValueSet valueSet = valueSets.get(key);
            if (valueSet != null) {
                valueSet.sort();
            }
        }
        leave(method);
    }

    /**
   * Parse a string containing a comma-separated list of resource bundles
   * names, get each bundle, and return a list of bundles.
   *
   * @param bundles  a comma-separated list of resource bundle names.
   * @return a list of resource bundles.
   */
    private List<ResourceBundle> getBundles(String bundles) {
        final String method = "getBundles";
        enter(method);
        List<ResourceBundle> list = CollectionFactory.arrayList();
        String[] names = bundles.split(",");
        for (int i = 0; i < names.length; ++i) {
            String fullname = names[i].trim();
            String[] parts = fullname.split("_");
            String basename = parts[0];
            Locale locale = null;
            if (parts.length >= 2) {
                String language = parts[1];
                if (parts.length >= 3) {
                    String country = parts[2];
                    locale = new Locale(language, country);
                } else {
                    locale = new Locale(language);
                }
            } else {
                locale = Locale.getDefault();
            }
            try {
                ResourceBundle bundle = ResourceBundle.getBundle(basename, locale);
                if (bundle != null) {
                    list.add(bundle);
                }
            } catch (MissingResourceException e) {
                String msg = "Missing resoure bundle: " + fullname;
                error(msg);
            }
        }
        leave(method, list);
        return list;
    }

    /**
   * Returns the concept with the specified name.
   */
    public Concept concept(String name) {
        return concepts.get(name);
    }

    /**
   * Returns a string containing a comma-separated list of resource bundles
   * for labels.
   *
   * @return a comma-separated list of resource bundles.
   */
    public String getLabelBundles() {
        return labelBundles;
    }

    /**
   * Set the list of resource bundles for labels.
   *
   * @param bundles  contains a comma-separated list of resource bundles.
   */
    public void setLabelBundles(String bundles) {
        this.labelBundles = bundles;
    }

    /**
   * Returns a string containing a comma-separated list of resource bundles
   * for messages.
   *
   * @return a comma-separated list of resource bundles.
   */
    public String getMessageBundles() {
        return messageBundles;
    }

    /**
   * Set the list of resource bundles for messages.
   *
   * @param bundles  contains a comma-separated list of resource bundles.
   */
    public void setMessageBundles(String bundles) {
        this.messageBundles = bundles;
    }

    /**
   * Returns a string containing a comma-separated list of resource bundles
   * for value sets.
   *
   * @return a comma-separated list of resource bundles.
   */
    public String getValueSetBundles() {
        return valueSetBundles;
    }

    /**
   * Set the list of resource bundles for value sets.
   *
   * @param bundles  contains a comma-separated list of resource bundles.
   */
    public void setValueSetBundles(String bundles) {
        this.valueSetBundles = bundles;
    }

    private static String labelNotFound = "Label %s not found!";

    private static String messageNotFound = "Message %s not found!";

    private String labelBundles;

    private String messageBundles;

    private String valueSetBundles;
}
