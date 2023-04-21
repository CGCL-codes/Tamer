package com.manydesigns.portofino.base;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Paolo Predonzani - paolo.predonzani@manydesigns.com
 * @author Angelo Lupo      - angelo.lupo@manydesigns.com
 */
public class MDConfigException extends Exception {

    public static final String copyright = "Copyright (c) 2005-2009, ManyDesigns srl";

    private final Collection<String> errors;

    /** Creates a new instance of MDConfigException */
    public MDConfigException(Collection<String> errors) {
        this.errors = new ArrayList<String>();
        this.errors.addAll(errors);
    }

    public Collection<String> getErrors() {
        return errors;
    }
}
