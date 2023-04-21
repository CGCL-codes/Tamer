package interfaces.options;

import main.InitGame;
import main.listeners.listenerClasses.LanguageListener;
import org.fenggui.appearance.DefaultAppearance;
import org.fenggui.decorator.background.PlainBackground;
import org.fenggui.util.Color;
import fileHandling.language.LanguageLoader;
import interfaces.GUISource;
import interfaces.WordWrapHelper;
import interfaces.superWidgets.InterfaceLabel;
import interfaces.superWidgets.StaticContent;

public class ToolTipContent extends StaticContent {

    public static final String TOOL_TIP_STRING = "TT";

    protected OptionWidget widget;

    public ToolTipContent(int width, int height, OptionWidget widget) {
        super(width, height);
        this.widget = widget;
        DefaultAppearance app = getAppearance();
        app.removeAll();
        app.add(new PlainBackground(Color.WHITE));
        InitGame.get().addLanguageListener(new LanguageListener() {

            @Override
            public void languageChanged() {
                removeAllWidgets();
                init();
            }
        });
        init();
    }

    protected void init() {
        String text = LanguageLoader.get(widget.getDescription() + TOOL_TIP_STRING);
        InterfaceLabel label = new InterfaceLabel(GUISource.middleFont);
        label.setSize(width, height);
        addWidget(label);
        GUISource.setTextColor(label, Color.BLACK);
        WordWrapHelper.wrapWords(label, text);
        setHeight(label.getHeight());
        layout();
    }
}
