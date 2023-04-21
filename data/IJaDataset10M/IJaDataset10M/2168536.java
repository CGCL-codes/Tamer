package com.novocode.naf.gui.image;

import com.novocode.naf.app.NAFException;
import com.novocode.naf.resource.ConfigurableObject;
import com.novocode.naf.resource.ConfProperty;

/**
 * An &lt;image&gt; element which is used in various components.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Mar 14, 2008
 */
public class ImageDescriptor implements ConfigurableObject {

    private String id, imageRes;

    @ConfProperty(value = "id", required = true)
    public void setID(String id) {
        if (id == null || id.isEmpty()) throw new NAFException("id must not be empty");
        this.id = id;
    }

    public String getID() {
        return id;
    }

    @ConfProperty(value = "image", required = true)
    public void setImageResource(String s) {
        imageRes = s;
    }

    public String getImageResource() {
        return imageRes;
    }
}
