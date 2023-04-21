package com.certesystems.swingforms.fields;

import javax.swing.JLabel;
import com.certesystems.swingforms.Context;
import com.certesystems.swingforms.entity.Entity;
import com.certesystems.swingforms.links.Link;
import com.certesystems.swingforms.links.LinkLabel;

public class FieldLabel extends Field {

    public FieldLabel() {
        super.setAlign("right,fill");
    }

    public Link createLink(Entity entity) {
        LinkLabel dum = new LinkLabel();
        dum.setField(this);
        JLabel label = new JLabel(getDescription());
        Context.getContext().getDecorator().decorateLabel(label);
        dum.setContainer(label);
        return dum;
    }

    public String getIcon() {
        return null;
    }

    public String getCellValue(Object register) {
        return "";
    }
}
