package org.form4j.form.field.data;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JProgressBar;
import javax.swing.JToolTip;
import org.form4j.form.main.Form;
import org.form4j.form.util.tip.ToolTipUtil;
import org.w3c.dom.Element;

/**
 * @author $Author: cjuon $
 * @version $Revision: 1.2 $ $Date: 2008/09/04 14:44:07 $
 */
public class ProgressBar extends AbstractDataField {

    private JProgressBar pBar;

    private int min;

    private int max;

    public ProgressBar(final Form form, final Element desc) throws Exception {
        super(form, desc);
        pBar = new JProgressBar() {

            private int toolTipModifiers = 0;

            public Point getToolTipLocation(final MouseEvent event) {
                toolTipModifiers = event.getModifiers();
                return super.getToolTipLocation(event);
            }

            public JToolTip createToolTip() {
                return ToolTipUtil.createToolTip(ProgressBar.this, toolTipModifiers);
            }

            public Dimension getPreferredSize() {
                return (isVisible() ? super.getPreferredSize() : new Dimension(0, 0));
            }
        };
        if (ToolTipUtil.isToolTipDebugActive()) {
            pBar.setToolTipText(ToolTipUtil.TOOLTIP_PLACEHOLDER);
        }
        setComponent(pBar);
        init();
    }

    public void init() throws Exception {
        pBar = (JProgressBar) getComponent();
        pBar.setString("");
        pBar.setStringPainted(true);
        min = Integer.parseInt(getFieldDescriptor().getAttribute("min"));
        max = Integer.parseInt(getFieldDescriptor().getAttribute("max"));
        pBar.setMinimum(min);
        pBar.setMaximum(max);
        String key = getFieldDescriptor().getAttribute("key");
        Object data = getForm().getModel().getData(key);
        setData(data);
        Dimension prefSize = pBar.getPreferredSize();
        prefSize.width = Integer.parseInt(getFieldDescriptor().getAttribute("width"));
        pBar.setPreferredSize(prefSize);
        getForm().getModel().addItemChangeListener(this);
        super.init();
    }

    public void setData(Object data) {
        pBar = (JProgressBar) getComponent();
        String stringPainted = getFieldDescriptor().getAttribute("stringPainted");
        boolean paintString = "true".equals(stringPainted);
        boolean paintAsPercent = "percent".equals(stringPainted);
        if (data != null) {
            try {
                int value = (int) Double.parseDouble(((String) data).trim());
                value = Math.min(value, max);
                value = Math.max(value, min);
                pBar.setValue(value);
                if (paintString) pBar.setString(Integer.toString(value));
                if (paintAsPercent) pBar.setString("" + (int) (pBar.getPercentComplete() * 100) + " %");
            } catch (NumberFormatException e) {
                pBar.setValue(min);
                if (paintString || paintAsPercent) pBar.setString(("" + data).trim());
            }
        }
    }

    public Object getData() {
        return new Integer(pBar.getValue());
    }
}
