package com.jxva.tool.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.grouplayout.GroupLayout;
import org.eclipse.swt.layout.grouplayout.LayoutStyle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author  The Jxva Framework
 * @since   1.0
 * @version 2008-12-02 12:53:13 by Jxva
 */
public class LicenseGenerateUI extends Composite {

    private Text text_1;

    private Text text;

    /**
	 * Create the composite
	 * @param parent
	 * @param style
	 */
    public LicenseGenerateUI(Composite parent, int style) {
        super(parent, style);
        Group jkhjGroup;
        jkhjGroup = new Group(this, SWT.NONE);
        jkhjGroup.setText("jkhj");
        Label label;
        label = new Label(jkhjGroup, SWT.NONE);
        label.setText("Label");
        text = new Text(jkhjGroup, SWT.BORDER);
        Label label_1;
        label_1 = new Label(jkhjGroup, SWT.NONE);
        label_1.setText("Label");
        text_1 = new Text(jkhjGroup, SWT.BORDER);
        final GroupLayout groupLayout_1 = new GroupLayout(jkhjGroup);
        groupLayout_1.setHorizontalGroup(groupLayout_1.createParallelGroup(GroupLayout.LEADING).add(groupLayout_1.createSequentialGroup().addContainerGap().add(groupLayout_1.createParallelGroup(GroupLayout.LEADING).add(label).add(label_1)).add(47, 47, 47).add(groupLayout_1.createParallelGroup(GroupLayout.LEADING, false).add(text_1).add(text, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)).addContainerGap(220, Short.MAX_VALUE)));
        groupLayout_1.setVerticalGroup(groupLayout_1.createParallelGroup(GroupLayout.LEADING).add(groupLayout_1.createSequentialGroup().addContainerGap().add(groupLayout_1.createParallelGroup(GroupLayout.BASELINE).add(label).add(text, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(groupLayout_1.createParallelGroup(GroupLayout.BASELINE).add(label_1).add(text_1, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)).addContainerGap(280, Short.MAX_VALUE)));
        jkhjGroup.setLayout(groupLayout_1);
        final GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.LEADING).add(groupLayout.createSequentialGroup().addContainerGap().add(jkhjGroup, GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE).addContainerGap()));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.LEADING).add(groupLayout.createSequentialGroup().addContainerGap().add(jkhjGroup, GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE).addContainerGap()));
        setLayout(groupLayout);
    }
}
