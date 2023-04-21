package org.psystems.bpm.dashboard.client.pwidgets;

/**
 * @author dima_d
 *
 */
public class PaletteWidgetTitleSysdate extends PaletteWidgetTitle {

    public static final String TYPE = "SYSDATE";

    public PaletteWidgetTitleSysdate(String title) {
        super(title);
    }

    @Override
    protected PaletteWidget cloneWidget() {
        return new PaletteWidgetTitleSysdate(this.title);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
