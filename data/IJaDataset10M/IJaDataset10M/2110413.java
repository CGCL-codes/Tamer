package org.opensource.jdom.taggen.html.tag;

import org.opensource.jdom.taggen.html.CommonAttributes;

/**
 * An embedded multimedia object. Often used in conjunction with param.
 *
 * <h2>Example</h2> <pre><code class="html">
 * <strong>&lt;object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" 
 * codebase="someplace/swflash.cab" width="200" height="300" id="penguin"&gt;
 * </strong>
 * &lt;param name="movie" value="flash/penguin.swf" /&gt;
 * &lt;param name="quality" value="high" /&gt;
 * &lt;img src="images/penguin.jpg" width="200" height="300" alt="Penguin" /&gt;
 * <strong>&lt;/object&gt;</strong>
 * </code></pre>
 *
 * @see Param
 * @author sergio.valdez
 */
public class Object extends CommonAttributes {

    /**
     * can be used to specify the location of the object in the form of a URL or
     * Windows Registry location.
     */
    private String classid;

    /**
     * can be used to specify the location of the data for the object in the
     * form of a URL.
     */
    private String data;

    /**
     * can be used to specify the base location from which relative URLs
     * specified in the classid, data and archive attributes should be taken.
     */
    private String codebase;

    /**
     * can be used to specify that the object is a declaration only. It must be
     * used in the format declare="declare".
     */
    private boolean declare;

    /**
     * can be used to specify the content type of the data specified by the
     * data attribute.
     */
    private String type;

    /**
     * can be used to specify the content type of the object.
     */
    private String codetype;

    /**
     * can be used to specify resources relevant to the object. The value
     * should be a URL or a number of URLs separated by spaces.
     */
    private String archive;

    /**
     * can be used to specify text that will be displayed while the object is
     * loading.
     */
    private String standby;

    /**
     * can be used to specify the width of the object (in pixels).
     * This can also be done with CSS.
     */
    private String width;

    /**
     * can be used to specify the height of the object (in pixels).
     * This can also be done with CSS.
     */
    private String height;

    /**
     * can be used to specify a name by which the object can be referenced.
     */
    private String cssName;

    /**
     * can be used to specify where the element appears in the tab order of
     * the page.
     */
    private String tabindex;

    /**
     * can be used to specify the location of the object in the form of a URL or
     * Windows Registry location.
     * @return the classid
     */
    public String getClassid() {
        return classid;
    }

    /**
     * can be used to specify the location of the object in the form of a URL or
     * Windows Registry location.
     * @param classid the classid to set
     */
    public void setClassid(String classid) {
        this.classid = classid;
    }

    /**
     * can be used to specify the location of the data for the object in the
     * form of a URL.
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * can be used to specify the location of the data for the object in the
     * form of a URL.
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * can be used to specify the base location from which relative URLs
     * specified in the classid, data and archive attributes should be taken.
     * @return the codebase
     */
    public String getCodebase() {
        return codebase;
    }

    /**
     * can be used to specify the base location from which relative URLs
     * specified in the classid, data and archive attributes should be taken.
     * @param codebase the codebase to set
     */
    public void setCodebase(String codebase) {
        this.codebase = codebase;
    }

    /**
     * can be used to specify that the object is a declaration only. It must be
     * used in the format declare="declare".
     * @return the declare
     */
    public boolean isDeclare() {
        return declare;
    }

    /**
     * can be used to specify that the object is a declaration only. It must be
     * used in the format declare="declare".
     * @param declare the declare to set
     */
    public void setDeclare(boolean declare) {
        this.declare = declare;
    }

    /**
     * can be used to specify the content type of the data specified by the
     * data attribute.
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * can be used to specify the content type of the data specified by the
     * data attribute.
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * can be used to specify the content type of the object.
     * @return the codetype
     */
    public String getCodetype() {
        return codetype;
    }

    /**
     * can be used to specify the content type of the object.
     * @param codetype the codetype to set
     */
    public void setCodetype(String codetype) {
        this.codetype = codetype;
    }

    /**
     * can be used to specify resources relevant to the object. The value
     * should be a URL or a number of URLs separated by spaces.
     * @return the archive
     */
    public String getArchive() {
        return archive;
    }

    /**
     * can be used to specify resources relevant to the object. The value
     * should be a URL or a number of URLs separated by spaces.
     * @param archive the archive to set
     */
    public void setArchive(String archive) {
        this.archive = archive;
    }

    /**
     * can be used to specify text that will be displayed while the object is
     * loading.
     * @return the standby
     */
    public String getStandby() {
        return standby;
    }

    /**
     * can be used to specify text that will be displayed while the object is
     * loading.
     * @param standby the standby to set
     */
    public void setStandby(String standby) {
        this.standby = standby;
    }

    /**
     * can be used to specify the width of the object (in pixels).
     * This can also be done with CSS.
     * @return the width
     */
    public String getWidth() {
        return width;
    }

    /**
     * can be used to specify the width of the object (in pixels).
     * This can also be done with CSS.
     * @param width the width to set
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * can be used to specify the height of the object (in pixels).
     * This can also be done with CSS.
     * @return the height
     */
    public String getHeight() {
        return height;
    }

    /**
     * can be used to specify the height of the object (in pixels).
     * This can also be done with CSS.
     * @param height the height to set
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * can be used to specify a name by which the object can be referenced.
     * @return the name
     */
    public String getCssName() {
        return cssName;
    }

    /**
     * can be used to specify a name by which the object can be referenced.
     * @param name the name to set
     */
    public void setCssName(String name) {
        this.cssName = name;
    }

    /**
     * can be used to specify where the element appears in the tab order of
     * the page.
     * @return the tabindex
     */
    public String getTabindex() {
        return tabindex;
    }

    /**
     * can be used to specify where the element appears in the tab order of
     * the page.
     * @param tabindex the tabindex to set
     */
    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }
}
