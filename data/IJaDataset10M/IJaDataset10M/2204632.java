package net.rptools.maptool.client.ui.htmlframe;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.awt.*;
import javax.swing.ComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.FormView;
import javax.swing.text.html.HTML;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

public class HTMLPaneFormView extends FormView {

    private static final Logger LOGGER = Logger.getLogger(HTMLPaneFormView.class);

    private HTMLPane htmlPane;

    /**
	 * Creates a new HTMLPaneFormView.
	 * @param elem The element this is a view for.
	 * @param pane The HTMLPane this element resides on.
	 */
    public HTMLPaneFormView(Element elem, HTMLPane pane) {
        super(elem);
        htmlPane = pane;
    }

    @Override
    protected Component createComponent() {
        Component c = null;
        AttributeSet attr = getElement().getAttributes();
        HTML.Tag t = (HTML.Tag) attr.getAttribute(StyleConstants.NameAttribute);
        if (t == HTML.Tag.TEXTAREA) {
            JScrollPane sp = (JScrollPane) super.createComponent();
            JTextArea area = (JTextArea) sp.getViewport().getView();
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            c = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        } else {
            c = super.createComponent();
        }
        return c;
    }

    @Override
    protected void submitData(String data) {
        Element formElement = null;
        for (Element e = getElement(); e != null; e = e.getParentElement()) {
            if (e.getAttributes().getAttribute(StyleConstants.NameAttribute) == HTML.Tag.FORM) {
                formElement = e;
                break;
            }
        }
        if (formElement != null) {
            AttributeSet att = formElement.getAttributes();
            String action = att.getAttribute(HTML.Attribute.ACTION).toString();
            String method;
            if (att.getAttribute(HTML.Attribute.METHOD) != null) {
                method = att.getAttribute(HTML.Attribute.METHOD).toString();
            } else {
                method = "get";
            }
            action = action == null ? "" : action;
            method = method == null ? "" : method.toLowerCase();
            if (method.equals("json")) {
                JSONObject jobj = new JSONObject();
                String[] values = data.split("&");
                for (String v : values) {
                    String[] dataStr = v.split("=");
                    if (dataStr.length == 1) {
                        try {
                            jobj.put(URLDecoder.decode(dataStr[0], "utf8"), "");
                        } catch (UnsupportedEncodingException e) {
                            jobj.put(dataStr[0], "");
                        }
                    } else if (dataStr.length > 2) {
                        jobj.put(dataStr[0], dataStr[1]);
                    } else {
                        try {
                            jobj.put(URLDecoder.decode(dataStr[0], "utf8"), URLDecoder.decode(dataStr[1], "utf8"));
                        } catch (UnsupportedEncodingException e) {
                            jobj.put(dataStr[0], dataStr[1]);
                        }
                    }
                }
                try {
                    data = URLEncoder.encode(jobj.toString(), "utf8");
                } catch (UnsupportedEncodingException e) {
                    data = jobj.toString();
                }
            }
            htmlPane.doSubmit(method, action, data);
        }
    }

    @Override
    protected void imageSubmit(String data) {
        Element formElement = null;
        for (Element e = getElement(); e != null; e = e.getParentElement()) {
            if (e.getAttributes().getAttribute(StyleConstants.NameAttribute) == HTML.Tag.FORM) {
                formElement = e;
                break;
            }
        }
        if (formElement != null) {
            String imageMapName = data.replaceFirst("\\..*", "");
            Map<String, String> fdata = new HashMap<String, String>();
            fdata.putAll(getDataFrom(formElement, imageMapName));
            StringBuilder sb = new StringBuilder();
            for (String s : fdata.keySet()) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(s).append("=").append(fdata.get(s));
            }
            sb.append("&").append(data);
            submitData(sb.toString());
        } else {
            submitData(data);
        }
    }

    private Map<String, String> getDataFrom(Element ele, String selectedImageMap) {
        Map<String, String> vals = new HashMap<String, String>();
        for (int i = 0; i < ele.getElementCount(); i++) {
            Element e = ele.getElement(i);
            AttributeSet as = e.getAttributes();
            if (as.getAttribute(StyleConstants.ModelAttribute) != null || as.getAttribute(HTML.Attribute.TYPE) != null) {
                String type = (String) as.getAttribute(HTML.Attribute.TYPE);
                String name = (String) as.getAttribute(HTML.Attribute.NAME);
                Object model = as.getAttribute(StyleConstants.ModelAttribute);
                if (type == null && model instanceof PlainDocument) {
                    PlainDocument pd = (PlainDocument) model;
                    try {
                        vals.put(name, encode(pd.getText(0, pd.getLength())));
                    } catch (BadLocationException e1) {
                        LOGGER.error(e1.getStackTrace());
                    }
                } else if (type == null && model instanceof ComboBoxModel) {
                    vals.put(name, ((ComboBoxModel) model).getSelectedItem().toString());
                } else if ("text".equals(type)) {
                    PlainDocument pd = (PlainDocument) model;
                    try {
                        vals.put(name, encode(pd.getText(0, pd.getLength())));
                    } catch (BadLocationException e1) {
                        LOGGER.error(e1.getStackTrace());
                    }
                } else if ("submit".equals(type)) {
                } else if ("image".equals(type)) {
                    if (name != null && name.equals(selectedImageMap)) {
                        String val = (String) as.getAttribute(HTML.Attribute.VALUE);
                        vals.put(name + ".value", encode(val == null ? "" : val));
                    }
                } else if ("radio".equals(type)) {
                    if (as.getAttribute(HTML.Attribute.CHECKED) != null) {
                        vals.put(name, encode(encode((String) as.getAttribute(HTML.Attribute.VALUE))));
                    }
                } else if ("checkbox".equals(type)) {
                    if (as.getAttribute(HTML.Attribute.CHECKED) != null) {
                        vals.put(name, encode(encode((String) as.getAttribute(HTML.Attribute.VALUE))));
                    }
                } else if ("password".equals(type)) {
                    PlainDocument pd = (PlainDocument) model;
                    try {
                        vals.put(name, encode(pd.getText(0, pd.getLength())));
                    } catch (BadLocationException e1) {
                        LOGGER.error(e1.getStackTrace());
                    }
                } else if ("hidden".equals(type)) {
                    vals.put(name, encode(encode((String) as.getAttribute(HTML.Attribute.VALUE))));
                }
            }
            vals.putAll(getDataFrom(e, selectedImageMap));
        }
        return vals;
    }

    private String encode(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getStackTrace());
            return str;
        }
    }
}
