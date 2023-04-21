package uk.org.sgj.YAT;

import javax.swing.*;
import java.awt.*;
import uk.org.sgj.SGJNifty.FontUtils.FontAndColor;
import uk.org.sgj.SGJNifty.FontUtils.FontCallBack;
import uk.org.sgj.SGJNifty.FontUtils.FontSelectionPane;

public class FontControls extends JPanel {

    YATFontSet currentTestSettings, currentTableSettings, selectedTestSettings, selectedTableSettings;

    public FontControls() {
        currentTestSettings = YAT.getProject().getFontsForTestRuns();
        currentTableSettings = YAT.getProject().getFontsForTableView();
        selectedTestSettings = new YATFontSet(currentTestSettings);
        selectedTableSettings = new YATFontSet(currentTableSettings);
        YATFontPanel testPanel = new YATFontPanel(selectedTestSettings, "Fonts to use during tests:");
        YATFontPanel tablePanel = new YATFontPanel(selectedTableSettings, "Fonts to use for table view:");
        setLayout(new BorderLayout());
        add(testPanel, BorderLayout.LINE_START);
        add(tablePanel, BorderLayout.LINE_END);
    }

    void applySelectedChanges() {
        currentTestSettings.overwriteFonts(selectedTestSettings);
        currentTableSettings.overwriteFonts(selectedTableSettings);
        YAT.getProject().updateTableSettings();
        YATWindows.updateTestFonts();
        YAT.projectUpdated();
    }

    class YATFontPanel extends JPanel {

        YATFontPanel(YATFontSet settings, String title) {
            FontSelectionPane foreign, foreignCxt, translation, translationCxt, full;
            BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
            setLayout(layout);
            foreign = new ForeignFontSettingsPane(settings);
            add(foreign);
            foreignCxt = new ForeignCxtFontSettingsPane(settings);
            add(foreignCxt);
            translation = new TranslationFontSettingsPane(settings);
            add(translation);
            translationCxt = new TranslationCxtFontSettingsPane(settings);
            add(translationCxt);
            full = new FullFontSettingsPane(settings);
            add(full);
            setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title));
        }
    }

    class CBI implements FontCallBack {

        private YATFontSet y;

        CBI(YATFontSet yatSettings) {
            y = yatSettings;
        }

        @Override
        public void fontSelected(FontAndColor f) {
            y.setI(f);
        }
    }

    class FullFontSettingsPane extends FontSelectionPane {

        FullFontSettingsPane(YATFontSet yatSettings) {
            super("Full information", "", yatSettings.getI(), new CBI(yatSettings));
        }
    }

    class CBF implements FontCallBack {

        private YATFontSet y;

        CBF(YATFontSet yatSettings) {
            y = yatSettings;
        }

        @Override
        public void fontSelected(FontAndColor f) {
            y.setF(f);
        }
    }

    class ForeignFontSettingsPane extends FontSelectionPane {

        ForeignFontSettingsPane(YATFontSet yatSettings) {
            super("Foreign", "", yatSettings.getF(), new CBF(yatSettings));
        }
    }

    class CBFC implements FontCallBack {

        private YATFontSet y;

        CBFC(YATFontSet yatSettings) {
            y = yatSettings;
        }

        @Override
        public void fontSelected(FontAndColor f) {
            y.setFC(f);
        }
    }

    class ForeignCxtFontSettingsPane extends FontSelectionPane {

        ForeignCxtFontSettingsPane(YATFontSet yatSettings) {
            super("Foreign Context", "", yatSettings.getFC(), new CBFC(yatSettings));
        }
    }

    class CBT implements FontCallBack {

        private YATFontSet y;

        CBT(YATFontSet yatSettings) {
            y = yatSettings;
        }

        @Override
        public void fontSelected(FontAndColor f) {
            y.setT(f);
        }
    }

    class TranslationFontSettingsPane extends FontSelectionPane {

        TranslationFontSettingsPane(YATFontSet yatSettings) {
            super("Translation", "", yatSettings.getT(), new CBT(yatSettings));
        }
    }

    class CBTC implements FontCallBack {

        private YATFontSet y;

        CBTC(YATFontSet yatSettings) {
            y = yatSettings;
        }

        @Override
        public void fontSelected(FontAndColor f) {
            y.setTC(f);
        }
    }

    class TranslationCxtFontSettingsPane extends FontSelectionPane {

        TranslationCxtFontSettingsPane(YATFontSet yatSettings) {
            super("Translation in Context", "", yatSettings.getTC(), new CBTC(yatSettings));
        }
    }
}
