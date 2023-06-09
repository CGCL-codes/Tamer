package org.netuno.proteu.com.form;

import org.netuno.proteu.com.Component;
import org.netuno.proteu.Proteu;
import org.netuno.proteu.Script;
import org.netuno.psamata.RegEx;
import org.netuno.psamata.Values;

/**
 * Radio
 * @author eduveks
 */
public class Radio implements Component {

    private Proteu proteu = null;

    private Values parameters = new Values();

    private String type = "";

    private String description = "";

    private String style = "";

    private String visible = "true";

    private int count = 0;

    private String name = "";

    private String value = "";

    private String cssclass = "";

    private String accesskey = "";

    private String alt = "";

    private String altkey = "";

    private String checked = "";

    private String disabled = "";

    private String id = "";

    private String onblur = "";

    private String onchange = "";

    private String onclick = "";

    private String ondblclick = "";

    private String onfocus = "";

    private String onkeydown = "";

    private String onkeypress = "";

    private String onkeyup = "";

    private String onmousedown = "";

    private String onmousemove = "";

    private String onmouseout = "";

    private String onmouseover = "";

    private String onmouseup = "";

    private String tabindex = "";

    private String title = "";

    private String titlekey = "";

    /**
     * Radio
     * @param proteu Proteu
     */
    public Radio(Proteu proteu) {
        this.proteu = proteu;
    }

    /**
     * Parent
     * @param component Component
     */
    public void parent(Component component) {
    }

    /**
     * Next
     * @return Loop to next
     */
    public boolean next() {
        if (count == 0 && visible.equalsIgnoreCase("true")) {
            try {
                proteu.getOutput().print("<input type=\"radio\" name=\"" + name + "\" value=\"" + value);
            } catch (Exception e) {
                throw new Error(e);
            }
            count++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Close
     */
    public void close() {
        try {
            if (visible.equalsIgnoreCase("true")) {
                proteu.getOutput().print("\"");
                if (!id.equals("")) {
                    proteu.getOutput().print(" id=\"" + id + "\"");
                }
                if (!cssclass.equals("")) {
                    proteu.getOutput().print(" class=\"" + cssclass + "\"");
                }
                if (!style.equals("")) {
                    proteu.getOutput().print(" style=\"" + style + "\"");
                }
                if (!accesskey.equals("")) {
                    proteu.getOutput().print(" accesskey=\"" + accesskey + "\"");
                }
                if (!alt.equals("")) {
                    proteu.getOutput().print(" alt=\"" + alt + "\"");
                }
                if (!altkey.equals("")) {
                    proteu.getOutput().print(" altkey=\"" + altkey + "\"");
                }
                if (!checked.equals("")) {
                    proteu.getOutput().print(" checked=\"" + checked + "\"");
                }
                if (!disabled.equals("")) {
                    proteu.getOutput().print(" disabled=\"" + disabled + "\"");
                }
                if (!onblur.equals("")) {
                    proteu.getOutput().print(" onblur=\"" + onblur + "\"");
                }
                if (!onchange.equals("")) {
                    proteu.getOutput().print(" onchange=\"" + onchange + "\"");
                }
                if (!onclick.equals("")) {
                    proteu.getOutput().print(" onclick=\"" + onclick + "\"");
                }
                if (!ondblclick.equals("")) {
                    proteu.getOutput().print(" ondblclick=\"" + ondblclick + "\"");
                }
                if (!onfocus.equals("")) {
                    proteu.getOutput().print(" onfocus=\"" + onfocus + "\"");
                }
                if (!onkeydown.equals("")) {
                    proteu.getOutput().print(" onkeydown=\"" + onkeydown + "\"");
                }
                if (!onkeypress.equals("")) {
                    proteu.getOutput().print(" onkeypress=\"" + onkeypress + "\"");
                }
                if (!onkeyup.equals("")) {
                    proteu.getOutput().print(" onkeyup=\"" + onkeyup + "\"");
                }
                if (!onmousedown.equals("")) {
                    proteu.getOutput().print(" onmousedown=\"" + onmousedown + "\"");
                }
                if (!onmousemove.equals("")) {
                    proteu.getOutput().print(" onmousemove=\"" + onmousemove + "\"");
                }
                if (!onmouseout.equals("")) {
                    proteu.getOutput().print(" onmouseout=\"" + onmouseout + "\"");
                }
                if (!onmouseover.equals("")) {
                    proteu.getOutput().print(" onmouseover=\"" + onmouseover + "\"");
                }
                if (!onmouseup.equals("")) {
                    proteu.getOutput().print(" onmouseup=\"" + onmouseup + "\"");
                }
                if (!tabindex.equals("")) {
                    proteu.getOutput().print(" tabindex=\"" + tabindex + "\"");
                }
                if (!title.equals("")) {
                    proteu.getOutput().print(" title=\"" + title + "\"");
                }
                if (!titlekey.equals("")) {
                    proteu.getOutput().print(" titlekey=\"" + titlekey + "\"");
                }
                if (!parameters.getInFormat("\" ", "=\"").equals("")) {
                    proteu.getOutput().print(" " + parameters.getInFormat("\" ", "=\"") + "\"");
                }
                proteu.getOutput().print("/>");
            }
            count = 0;
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /**
     * Add Parameter
     * @param name Name
     * @param value Value
     */
    public void addParameter(String name, String value) {
        parameters.set(name, value);
    }

    /**
     * Set Parameter
     * exemple: setParameter("name[~]value")
     * @param parameter in format name + separator + value
     */
    public void setParameter(String parameter) {
        if (parameter.indexOf(Component.SEPARATOR) > -1) {
            String[] Parameter = parameter.split(RegEx.toRegEx(Component.SEPARATOR));
            parameters.set(Parameter[0], Parameter[1]);
        }
    }

    /**
     * Get Type
     * @return Type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Set Type
     * @param type Type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Set Description
     * @param description Description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get Description
     * @return Description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get Style
     * @return Style
     */
    public String getStyle() {
        return this.style;
    }

    /**
     * Set Style
     * @param style Style
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * Set Name
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get Name
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set Value
     * @param value value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get Value
     * @return value
     */
    public String getValue() {
        if (value.equals("")) {
            if (!proteu.getRequestGet().getString(name).equals("")) {
                value = proteu.getRequestGet().getString(name);
            }
            if (!proteu.getRequestPostCache().getString(name).equals("")) {
                value = proteu.getRequestPostCache().getString(name);
            }
        }
        return value;
    }

    /**
     * Set Visible
     * @param visible visible
     */
    public void setVisible(String visible) {
        this.visible = visible;
    }

    /**
     * Get Visible
     * @return visible
     */
    public String getVisible() {
        return this.visible;
    }

    /**
     * Set Cssclass
     * @param cssclass cssclass
     */
    public void setCssclass(String cssclass) {
        this.cssclass = cssclass;
    }

    /**
     * Get Cssclass
     * @return cssclass
     */
    public String getCssclass() {
        return this.cssclass;
    }

    /**
     * Set Accesskey
     * @param accesskey accesskey
     */
    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    /**
     * Get Accesskey
     * @return accesskey
     */
    public String getAccesskey() {
        return this.accesskey;
    }

    /**
     * Set Alt
     * @param alt alt
     */
    public void setAlt(String alt) {
        this.alt = alt;
    }

    /**
     * Get Alt
     * @return alt
     */
    public String getAlt() {
        return this.alt;
    }

    /**
     * Set Altkey
     * @param altkey altkey
     */
    public void setAltkey(String altkey) {
        this.altkey = altkey;
    }

    /**
     * Get Altkey
     * @return altkey
     */
    public String getAltkey() {
        return this.altkey;
    }

    /**
     * Set Checked
     * @param checked checked
     */
    public void setChecked(String checked) {
        this.checked = checked;
    }

    /**
     * Get Checked
     * @return checked
     */
    public String getChecked() {
        return this.checked;
    }

    /**
     * Set Disabled
     * @param disabled disabled
     */
    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    /**
     * Get Disabled
     * @return disabled
     */
    public String getDisabled() {
        return this.disabled;
    }

    /**
     * Set Id
     * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get Id
     * @return id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Set Onblur
     * @param onblur onblur
     */
    public void setOnblur(String onblur) {
        this.onblur = onblur;
    }

    /**
     * Get Onblur
     * @return onblur
     */
    public String getOnblur() {
        return this.onblur;
    }

    /**
     * Set Onchange
     * @param onchange onchange
     */
    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }

    /**
     * Get Onchange
     * @return onchange
     */
    public String getOnchange() {
        return this.onchange;
    }

    /**
     * Set Onclick
     * @param onclick onclick
     */
    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    /**
     * Get Onclick
     * @return onclick
     */
    public String getOnclick() {
        return this.onclick;
    }

    /**
     * Set Ondblclick
     * @param ondblclick ondblclick
     */
    public void setOndblclick(String ondblclick) {
        this.ondblclick = ondblclick;
    }

    /**
     * Get Ondblclick
     * @return ondblclick
     */
    public String getOndblclick() {
        return this.ondblclick;
    }

    /**
     * Set Onfocus
     * @param onfocus onfocus
     */
    public void setOnfocus(String onfocus) {
        this.onfocus = onfocus;
    }

    /**
     * Get Onfocus
     * @return onfocus
     */
    public String getOnfocus() {
        return this.onfocus;
    }

    /**
     * Set Onkeydown
     * @param onkeydown onkeydown
     */
    public void setOnkeydown(String onkeydown) {
        this.onkeydown = onkeydown;
    }

    /**
     * Get Onkeydown
     * @return onkeydown
     */
    public String getOnkeydown() {
        return this.onkeydown;
    }

    /**
     * Set Onkeypress
     * @param onkeypress onkeypress
     */
    public void setOnkeypress(String onkeypress) {
        this.onkeypress = onkeypress;
    }

    /**
     * Get Onkeypress
     * @return onkeypress
     */
    public String getOnkeypress() {
        return this.onkeypress;
    }

    /**
     * Set Onkeyup
     * @param onkeyup onkeyup
     */
    public void setOnkeyup(String onkeyup) {
        this.onkeyup = onkeyup;
    }

    /**
     * Get Onkeyup
     * @return onkeyup
     */
    public String getOnkeyup() {
        return this.onkeyup;
    }

    /**
     * Set Onmousedown
     * @param onmousedown onmousedown
     */
    public void setOnmousedown(String onmousedown) {
        this.onmousedown = onmousedown;
    }

    /**
     * Get Onmousedown
     * @return onmousedown
     */
    public String getOnmousedown() {
        return this.onmousedown;
    }

    /**
     * Set Onmousemove
     * @param onmousemove onmousemove
     */
    public void setOnmousemove(String onmousemove) {
        this.onmousemove = onmousemove;
    }

    /**
     * Get Onmousemove
     * @return onmousemove
     */
    public String getOnmousemove() {
        return this.onmousemove;
    }

    /**
     * Set Onmouseout
     * @param onmouseout onmouseout
     */
    public void setOnmouseout(String onmouseout) {
        this.onmouseout = onmouseout;
    }

    /**
     * Get Onmouseout
     * @return onmouseout
     */
    public String getOnmouseout() {
        return this.onmouseout;
    }

    /**
     * Set Onmouseover
     * @param onmouseover onmouseover
     */
    public void setOnmouseover(String onmouseover) {
        this.onmouseover = onmouseover;
    }

    /**
     * Get Onmouseover
     * @return onmouseover
     */
    public String getOnmouseover() {
        return this.onmouseover;
    }

    /**
     * Set Onmouseup
     * @param onmouseup onmouseup
     */
    public void setOnmouseup(String onmouseup) {
        this.onmouseup = onmouseup;
    }

    /**
     * Get Onmouseup
     * @return onmouseup
     */
    public String getOnmouseup() {
        return this.onmouseup;
    }

    /**
     * Set Tabindex
     * @param tabindex tabindex
     */
    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }

    /**
     * Get Tabindex
     * @return tabindex
     */
    public String getTabindex() {
        return this.tabindex;
    }

    /**
     * Set Title
     * @param title title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get Title
     * @return title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Set Titlekey
     * @param titlekey titlekey
     */
    public void setTitlekey(String titlekey) {
        this.titlekey = titlekey;
    }

    /**
     * Get Titlekey
     * @return titlekey
     */
    public String getTitlekey() {
        return this.titlekey;
    }
}
