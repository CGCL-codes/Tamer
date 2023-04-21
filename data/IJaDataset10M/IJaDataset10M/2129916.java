package com.threerings.jpkg.ant.dpkg.scripts.standard;

import com.threerings.jpkg.debian.MaintainerScript.Type;

/**
 * An {@link AbstractTypeScript} field for defining a &lt;postrm&gt; script.
 */
public class PostRm extends AbstractTypeScript {

    public PostRm() {
        super(Type.POSTRM);
    }

    public String getFieldName() {
        return "postrm";
    }

    public String getFriendlyName() {
        return "Simple postrm script";
    }
}
