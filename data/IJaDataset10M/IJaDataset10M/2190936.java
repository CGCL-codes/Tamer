package pl.xperios.rdk.client.common.settings;

import com.extjs.gxt.ui.client.Style.ButtonArrowAlign;
import com.extjs.gxt.ui.client.Style.ButtonScale;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import pl.xperios.rdk.client.common.IfNotNullReturnParse;
import pl.xperios.rdk.client.common.JsonParse;

/**
 *
 * @author Praca
 */
public class Settings extends JsonParse {

    public Settings(String toParse) {
        setMainValue(parse(toParse));
    }

    public Settings(JSONValue mainValue) {
        setMainValue(mainValue);
    }

    public String getHeight() {
        return getString("height");
    }

    public String getWidth() {
        return getString("width");
    }

    public RdkLayout getLayout() {
        return new IfNotNullReturnParse<String, RdkLayout>(getString("layout"), null) {

            @Override
            protected RdkLayout parse(String toParse) {
                return RdkLayout.valueOf(toParse);
            }
        }.getParsed();
    }

    public Boolean isHeadingVisible() {
        return getBoolean("visibleHeading");
    }

    public Integer getColumnCount() {
        return getInteger("columnCount");
    }

    public String getScale() {
        return getString("scale");
    }

    public String getPanelType() {
        return getString("panelType");
    }

    public Integer getColumnSpan() {
        return getInteger("columnSpan");
    }

    public Integer getRowSpan() {
        return getInteger("rowSpan");
    }

    public ButtonScale getButtonScale() {
        String scaleString = getString("buttonScale");
        return new IfNotNullReturnParse<String, ButtonScale>(scaleString, null) {

            @Override
            protected ButtonScale parse(String toParse) {
                return ButtonScale.valueOf(toParse);
            }
        }.getParsed();
    }

    public IconAlign getIconAlign() {
        String scaleString = getString("iconAlign");
        return new IfNotNullReturnParse<String, IconAlign>(scaleString, null) {

            @Override
            protected IconAlign parse(String toParse) {
                return IconAlign.valueOf(toParse);
            }
        }.getParsed();
    }

    public ButtonArrowAlign getButtonArrowAlign() {
        String scaleString = getString("buttonArrowAlign");
        return new IfNotNullReturnParse<String, ButtonArrowAlign>(scaleString, null) {

            @Override
            protected ButtonArrowAlign parse(String toParse) {
                return ButtonArrowAlign.valueOf(toParse);
            }
        }.getParsed();
    }

    public Boolean isBodyBorder() {
        return getBoolean("isBodyBorder");
    }

    public Boolean isCollapsible() {
        return getBoolean("isCollapsible");
    }

    public RdkLayoutData getLayoutData() {
        return new RdkLayoutData(getMainProperty("layoutData"));
    }

    public String getFloat() {
        return getString("float");
    }

    public AbstractImagePrototype getIcon() {
        Integer width = getInteger("icoWidth");
        if (width == null) {
            return null;
        }
        Integer height = getInteger("icoHeight");
        if (height == null) {
            return null;
        }
        String url = getString("icoUrl");
        if (url != null) {
            return IconHelper.create(url, width, height);
        }
        return null;
    }
}
