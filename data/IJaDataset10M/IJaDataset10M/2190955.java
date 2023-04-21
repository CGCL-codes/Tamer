package seedpod.webapp.view.htmlwidget;

public class Image extends GenericHtmlWidget {

    private String _imageSrc;

    private String _width;

    private String _align = "middle";

    public Image(String widgetID, String imageSrc) {
        super(widgetID);
        this._imageSrc = imageSrc;
    }

    public String render() {
        String html = "<img " + ATT("src", _imageSrc) + ATT("class", _cssClass) + ATT("id", _widgetID) + ATT("border", 0);
        if (_width != null) html += ATT("width", _width);
        html += ATT("align", _align) + ">";
        return html;
    }

    public void setAlign(String align) {
        _align = align;
    }

    public void setWidth(int pixelSize) {
        _width = Integer.toString(pixelSize);
    }
}
