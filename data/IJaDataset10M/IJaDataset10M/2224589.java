package org.pustefixframework.editor.common.remote.transferobjects;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class IncludePartThemeVariantTO implements Serializable {

    private static final long serialVersionUID = -7081358794051834368L;

    public String path;

    public String part;

    public String theme;

    public String md5;

    public List<String> affectedPages = new LinkedList<String>();

    public boolean readOnly;
}
