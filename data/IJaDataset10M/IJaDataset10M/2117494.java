package oxdoc.html;

import java.text.MessageFormat;
import oxdoc.OxDoc;

public class Header extends Element {

    String title;

    int level;

    int iconType;

    public Header(OxDoc oxdoc, int level, int iconType, String title) {
        super(oxdoc);
        this.oxdoc = oxdoc;
        this.level = level;
        this.iconType = iconType;
        this.title = title;
    }

    protected void render(StringBuffer buffer) {
        Object args[] = { "" + level, oxdoc.fileManager.largeIcon(iconType), title };
        if (oxdoc.config.EnableIcons) buffer.append(MessageFormat.format("<h{0}><span class=\"icon\">{1}</span><span class=\"text\">{2}</span></h{0}>\n", args)); else buffer.append(MessageFormat.format("<h{0}><span class=\"text\">{2}</span></h{0}>\n", args));
    }
}
