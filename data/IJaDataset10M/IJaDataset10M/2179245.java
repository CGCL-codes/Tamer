package anima.info;

public class InfoTitleNode extends InfoDataNode {

    /**
   * Title of resource.
   *
   * @see #setTitle(String)
   * @see #getTitle()
   */
    protected String title;

    public InfoTitleNode() {
        super();
        title = null;
    }

    public InfoTitleNode(String id, String title, Object value) {
        super(id, value);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        if (isTerminal()) return (String) value; else return "";
    }

    public void setDescription(String description) {
        if (value == null || isTerminal()) value = description;
    }
}
