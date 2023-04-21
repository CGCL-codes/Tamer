package net.sf.tacos.model.impl;

import java.io.Serializable;
import net.sf.tacos.model.IKeyProvider;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Key provider that uses a property of the object as a key.
 * 
 * @author andyhot
 */
public class BeanPropertyKeyProvider implements IKeyProvider {

    private String property;

    public BeanPropertyKeyProvider(String property) {
        this.property = property;
    }

    public Serializable getKey(Object obj) {
        try {
            return BeanUtils.getProperty(obj, property);
        } catch (Exception e) {
            throw new RuntimeException("Error getting property", e);
        }
    }
}
