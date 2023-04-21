package com.nhncorp.cubridqa.cases;

import org.eclipse.swt.widgets.Composite;

/**
 * 
 * The composite is used to display cases.
 * @ClassName: CaseComposite
 * @date 2009-9-1
 * @version V1.0 Copyright (C) www.nhn.com
 */
public class CaseComposite extends Composite {

    protected CasesView instance;

    /**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
    public CaseComposite(Composite parent, int style) {
        super(parent, style);
    }

    public CaseComposite(Composite parent, int style, CasesView view) {
        super(parent, style);
        this.instance = view;
    }

    @Override
    protected void checkSubclass() {
    }

    /**
	 * fresh view data must be override
	 * 
	 * @param input
	 */
    public void fresh(Object input) {
    }
}
