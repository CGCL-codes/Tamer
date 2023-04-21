package com.ivis.xprocess.ui.widgets;

import org.eclipse.swt.events.ModifyListener;
import com.ivis.xprocess.util.RichText;

public interface IRichTextWidget {

    public RichText getRichText();

    public void setRichText(RichText richText);

    public String getEditText();

    public void addModifyListener(ModifyListener modifyListener);

    public void dispose();
}
