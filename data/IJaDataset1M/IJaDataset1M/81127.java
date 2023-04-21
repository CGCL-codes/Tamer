package au.edu.diasb.annotation.repeater;

/**
 * This class holds the web page parts extracted by the page repeater's
 * HTML page parser, and some additional properties to simplify the JSP logic.
 * 
 * @author scrawley
 */
public class PageParts {

    private String lead;

    private String htmlAttrs;

    private String fill1;

    private String headAttrs;

    private String headContent;

    private String fill2;

    private String bodyAttrs;

    private String bodyContent;

    private String fill3;

    private String fill4;

    private boolean addLoadEventHandler;

    private boolean addBaseElement;

    private boolean addLaunchDashboard;

    private String url;

    public PageParts() {
    }

    /**
	 * The 'lead' part consists of everything before the &lt;html&gt; element.
	 * 
	 * @return the lead' part
	 */
    public String getLead() {
        return lead;
    }

    /**
	 * Set the 'lead' part.
	 * 
	 * @param lead
	 */
    public void setLead(String lead) {
        this.lead = lead;
    }

    /**
	 * The 'htmlAttrs' part consists of the attributes of the &lt;html&gt; element.
	 * @return the 'htmlAttrs' part
	 */
    public String getHtmlAttrs() {
        return htmlAttrs;
    }

    /**
	 * Set the 'htmlAttrs' part.
	 * 
	 * @param htmlAttrs
	 */
    public void setHtmlAttrs(String htmlAttrs) {
        this.htmlAttrs = htmlAttrs;
    }

    /**
	 * The 'fill1' part consists of anything between the end of the &lt;html&gt; element and
	 * the start of the &lt;head&gt; element.
	 * 
	 * @return the 'fill1' element.
	 */
    public String getFill1() {
        return fill1;
    }

    /**
	 * Set the 'fill1' part.
	 * 
	 * @param fill1
	 */
    public void setFill1(String fill1) {
        this.fill1 = fill1;
    }

    /**
	 * The 'headAttrs' part consists of the attributes of the &lt;head&gt; element.
	 * @return the 'headAttrs' part
	 */
    public String getHeadAttrs() {
        return headAttrs;
    }

    /**
	 * Set the 'headAttrs' part.
	 * 
	 * @param headAttrs
	 */
    public void setHeadAttrs(String headAttrs) {
        this.headAttrs = headAttrs;
    }

    /**
	 * The 'headContent' part consists everything within the &lt;head&gt; element.
	 * @return the 'headContent' part.
	 */
    public String getHeadContent() {
        return headContent;
    }

    /**
	 * Set the 'headContent' part.
	 * 
	 * @param headContent
	 */
    public void setHeadContent(String headContent) {
        this.headContent = headContent;
    }

    /**
	 * The 'fill2' part consists of anything between the end of the &lt;head&gt; element and
	 * the start of the &lt;body&gt; element.
	 * 
	 * @return the 'fill2' element.
	 */
    public String getFill2() {
        return fill2;
    }

    /**
	 * Set the 'fill2' part.
	 * 
	 * @param fill2
	 */
    public void setFill2(String fill2) {
        this.fill2 = fill2;
    }

    /**
	 * The 'bodyAttrs' part consists of the attributes of the &lt;body&gt; element.
	 * @return the 'bodyAttrs' part
	 */
    public String getBodyAttrs() {
        return bodyAttrs;
    }

    /**
	 * Set the 'bodyAttrs' part.
	 * 
	 * @param bodyAttrs
	 */
    public void setBodyAttrs(String bodyAttrs) {
        this.bodyAttrs = bodyAttrs;
    }

    /**
	 * The 'bodyContent' part consists everything within the &lt;body&gt; element.
	 * @return the 'bodyContent' part.
	 */
    public String getBodyContent() {
        return bodyContent;
    }

    /**
	 * Set the 'bodyContent' part.
	 * 
	 * @param bodyContent
	 */
    public void setBodyContent(String bodyContent) {
        this.bodyContent = bodyContent;
    }

    /**
	 * The 'fill3' part consists of anything between the end of the &lt;body&gt; element and
	 * the end of the &lt;html&gt; element.
	 * 
	 * @return the 'fill3' element.
	 */
    public String getFill3() {
        return fill3;
    }

    /**
	 * Set the 'fill3' part.
	 * 
	 * @param fill3
	 */
    public void setFill3(String fill3) {
        this.fill3 = fill3;
    }

    /**
	 * The 'fill4' part consists of anything after the &lt;html&gt; element.
	 * 
	 * @return the 'fill4' element.
	 */
    public String getFill4() {
        return fill4;
    }

    /**
	 * Set the 'fill4' part.
	 * 
	 * @param fill4
	 */
    public void setFill4(String fill4) {
        this.fill4 = fill4;
    }

    /**
	 * Set the 'addLoadEventHandler' property
	 * 
	 * @param addLoadEventHandler the property value
	 */
    public void setAddLoadEventHandler(boolean addLoadEventHandler) {
        this.addLoadEventHandler = addLoadEventHandler;
    }

    /**
	 * If {@literal true}, the JSP needs to include a load event handler 
	 * script in the &lt;head&gt; element.
	 * 
	 * @return the property value
	 */
    public boolean isAddLoadEventHandler() {
        return addLoadEventHandler;
    }

    /**
	 * If {@literal true}, the JSP needs to include a &lt;base&gt; element
	 * in the &lt;head&gt; element.
	 * 
	 * @return the property value
	 */
    public boolean isAddBaseElement() {
        return addBaseElement;
    }

    /**
	 * Set the 'addBaseElement' property
	 * 
	 * @param addBaseElement the property value
	 */
    public void setAddBaseElement(boolean addBaseElement) {
        this.addBaseElement = addBaseElement;
    }

    /**
	 * This property is the URL of the page that we are repeating.
	 * 
	 * @return the URL
	 */
    public String getUrl() {
        return url;
    }

    /**
	 * Set the 'url' property
	 * 
	 * @param url the property value
	 */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Set the 'addLaunchDashboard' property
     */
    public void setAddLaunchDashboard(boolean addLaunchDashboard) {
        this.addLaunchDashboard = addLaunchDashboard;
    }

    /**
     * If {@literal true}, the loadEventHandler script needs to 
     * launch the Danno dashboard.
     * 
     * @return the property value
     */
    public final boolean isAddLaunchDashboard() {
        return addLaunchDashboard;
    }
}
