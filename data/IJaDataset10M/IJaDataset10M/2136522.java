package com.nhncorp.cubridqa.configuration.composite;

import org.eclipse.swt.widgets.Composite;

/**
 * 
 * A parent composite of all configuration composites.
 * @ClassName: ParentComposite
 * @date 2009-9-1
 * @version V1.0
 * Copyright (C) www.nhn.com
 */
public class ParentComposite extends Composite {

    public ParentComposite(Composite parent, int style) {
        super(parent, style);
    }

    public boolean save() {
        return false;
    }

    public String getTitle() {
        return null;
    }
}
