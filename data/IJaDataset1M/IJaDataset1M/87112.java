package de.schwarzrot.ui.control;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import org.springframework.context.MessageSource;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import de.schwarzrot.app.support.ApplicationServiceProvider;
import de.schwarzrot.ui.control.model.FontSelectionAdapter;
import de.schwarzrot.ui.control.model.FontSelectionModel;
import de.schwarzrot.ui.control.support.AbstractDialogBase;
import de.schwarzrot.ui.control.support.FontSelectorPreview;
import de.schwarzrot.ui.render.ComboBoxEnumCellRenderer;
import de.schwarzrot.ui.service.DefaultFormComponentFactory;
import de.schwarzrot.ui.service.FormComponentFactory;

/**
 * a dialog to show and select a truetype font and change font styles. It has a
 * preview pane which displays user given sample text with the selected font and
 * font styles.
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 * 
 */
public class FontChooser extends AbstractDialogBase {

    public static final String DEFAULT_SAMPLE_TEXT = "FontChooser.sample.text";

    private static final String FONT_NAME_PROPERTY = "fontName";

    private static final String FONT_STYLE_PROPERTY = "fontStyle";

    private static final String FONT_SIZE_PROPERTY = "fontSize";

    private static final long serialVersionUID = 713L;

    private static FormComponentFactory componentFactory = new DefaultFormComponentFactory();

    private static List<Integer> knownStyles = Arrays.asList(new Integer[] { FontStyles.PLAIN.ordinal(), FontStyles.BOLD.ordinal(), FontStyles.ITALIC.ordinal(), FontStyles.BOLD_ITALIC.ordinal() });

    private static MessageSource msgSource;

    public class FontSelectorData extends Model {

        private static final long serialVersionUID = 713L;

        public final String getFontChoice() {
            return fontChoice;
        }

        public final String getSampleText() {
            return sampleText;
        }

        public final int getSizeChoice() {
            return sizeChoice;
        }

        public final int getStyleChoice() {
            return styleChoice;
        }

        public final void setFontChoice(String fontChoice) {
            String ov = this.fontChoice;
            this.fontChoice = fontChoice;
            PropertyChangeEvent pce = new PropertyChangeEvent(this, "fontChoice", ov, fontChoice);
            this.firePropertyChange(pce);
        }

        public final void setSampleText(String sampleText) {
            String ov = this.sampleText;
            this.sampleText = sampleText;
            PropertyChangeEvent pce = new PropertyChangeEvent(this, "sampleText", ov, sampleText);
            this.firePropertyChange(pce);
        }

        public final void setSizeChoice(int sizeChoice) {
            int ov = this.sizeChoice;
            this.sizeChoice = sizeChoice;
            PropertyChangeEvent pce = new PropertyChangeEvent(this, "sizeChoice", ov, sizeChoice);
            this.firePropertyChange(pce);
        }

        public final void setStyleChoice(int styleChoice) {
            int ov = this.styleChoice;
            this.styleChoice = styleChoice;
            PropertyChangeEvent pce = new PropertyChangeEvent(this, "styleChoice", ov, styleChoice);
            this.firePropertyChange(pce);
        }

        private String sampleText = DEFAULT_SAMPLE_TEXT;

        private String fontChoice = "Dialog";

        private int styleChoice = FontStyles.PLAIN.ordinal();

        private int sizeChoice = 20;
    }

    public enum FontStyles {

        PLAIN, BOLD, ITALIC, BOLD_ITALIC
    }

    public FontChooser() {
        this((PresentationModel<?>) null);
    }

    public FontChooser(Font initialFont) {
        super(DialogMode.CANCEL_APPROVE);
        this.initialFont = initialFont;
        setName("FontChooser");
        setFontNamePropertyName("fontChoice");
        setFontStylePropertyName("styleChoice");
        setFontSizePropertyName("sizeChoice");
        data = new FontSelectorData();
        data.setFontChoice(initialFont.getFamily());
        switch(initialFont.getStyle()) {
            case Font.BOLD:
                data.setStyleChoice(FontStyles.BOLD.ordinal());
                break;
            case Font.ITALIC:
                data.setStyleChoice(FontStyles.ITALIC.ordinal());
                break;
            case Font.BOLD | Font.ITALIC:
                data.setStyleChoice(FontStyles.BOLD_ITALIC.ordinal());
                break;
            default:
                data.setStyleChoice(FontStyles.PLAIN.ordinal());
                break;
        }
        data.setSizeChoice(initialFont.getSize());
        presentationModel = new PresentationModel<FontSelectorData>(data);
        sampleTextModel = createSampleTextModel();
        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        knownFonts = Arrays.asList(gEnv.getAvailableFontFamilyNames());
    }

    public FontChooser(PresentationModel<?> model) {
        super(DialogMode.CANCEL_APPROVE);
        if (model == null) {
            data = new FontSelectorData();
            presentationModel = new PresentationModel<FontSelectorData>(data);
            sampleTextModel = presentationModel.getModel("sampleText");
            setFontNamePropertyName("fontChoice");
            setFontStylePropertyName("styleChoice");
            setFontSizePropertyName("sizeChoice");
        } else {
            presentationModel = model;
            sampleTextModel = createSampleTextModel();
        }
        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        knownFonts = Arrays.asList(gEnv.getAvailableFontFamilyNames());
    }

    @Override
    public JComponent buildPanel() {
        if (msgSource == null) msgSource = ApplicationServiceProvider.getService(MessageSource.class);
        FormLayout layout = new FormLayout("right:max(50dlu;pref), 3dlu, pref, 20dlu, pref, " + "3dlu, max(50dlu;pref), 20dlu, pref, 3dlu, pref", "p, 3dlu, p, 3dlu, p");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        JComponent c = componentFactory.createComboBox(getPresentationModel().getModel(getFontNamePropertyName()), getKnownFonts());
        builder.addLabel(msgSource.getMessage(getClass().getSimpleName() + ".fontName.label", null, "Font-Family:", null), cc.xy(1, 1));
        c.setPreferredSize(new Dimension(350, 26));
        builder.add(c, cc.xy(3, 1));
        builder.addLabel(msgSource.getMessage(getClass().getSimpleName() + ".fontStyle.label", null, "Style:", null), cc.xy(5, 1));
        ComboBoxEnumCellRenderer<FontStyles> cbeRenderer = new ComboBoxEnumCellRenderer<FontStyles>(FontStyles.class);
        c = componentFactory.createComboBox(getPresentationModel().getModel(getFontStylePropertyName()), getKnownStyles(), cbeRenderer);
        c.setPreferredSize(new Dimension(80, 26));
        builder.add(c, cc.xy(7, 1));
        builder.addLabel(msgSource.getMessage(getClass().getSimpleName() + ".fontSize.label", null, "Size:", null), cc.xy(9, 1));
        c = componentFactory.createIntegerSpinner(getPresentationModel().getModel(getFontSizePropertyName()), 20, 6, 200, 1);
        c.setPreferredSize(new Dimension(70, 26));
        builder.add(c, cc.xy(11, 1));
        builder.addLabel(msgSource.getMessage(getClass().getSimpleName() + ".sampleText", null, "Sample-Text:", null), cc.xy(1, 3));
        c = componentFactory.createTextField(sampleTextModel);
        c.setPreferredSize(new Dimension(400, 26));
        builder.add(c, cc.xyw(3, 3, 9));
        FontSelectionModel fsm = new FontSelectionAdapter(getPresentationModel().getModel(getFontNamePropertyName()), getPresentationModel().getModel(getFontStylePropertyName()), getPresentationModel().getModel(getFontSizePropertyName()), sampleTextModel);
        fsp = new FontSelectorPreview(fsm);
        setLayout(new BorderLayout());
        add(builder.getPanel(), BorderLayout.NORTH);
        add(fsp, BorderLayout.CENTER);
        return this;
    }

    @Override
    public final Font getFont() {
        if (fsp != null) return fsp.getFont();
        return super.getFont();
    }

    public final String getFontNamePropertyName() {
        return fontNamePropertyName;
    }

    public final String getFontSizePropertyName() {
        return fontSizePropertyName;
    }

    public final String getFontStylePropertyName() {
        return fontStylePropertyName;
    }

    public final List<String> getKnownFonts() {
        return knownFonts;
    }

    public final PresentationModel<?> getPresentationModel() {
        return presentationModel;
    }

    @Override
    public void performReset() {
        if (initialFont != null) {
            getLogger().info("do something valuable!?!");
        }
        getPresentationModel().triggerFlush();
        repaint();
    }

    public final void setFontNamePropertyName(String fontNamePropertyName) {
        this.fontNamePropertyName = fontNamePropertyName;
    }

    public final void setFontSizePropertyName(String fontSizePropertyName) {
        this.fontSizePropertyName = fontSizePropertyName;
    }

    public final void setFontStylePropertyName(String fontStylePropertyName) {
        this.fontStylePropertyName = fontStylePropertyName;
    }

    public final void setPresentationModel(PresentationModel<?> presentationModel) {
        this.presentationModel = presentationModel;
    }

    protected ValueModel createSampleTextModel() {
        if (msgSource == null) msgSource = ApplicationServiceProvider.getService(MessageSource.class);
        String sampleText = msgSource.getMessage(DEFAULT_SAMPLE_TEXT, null, DEFAULT_SAMPLE_TEXT, null);
        return new ValueHolder(sampleText);
    }

    @Override
    protected String getDialogTitle() {
        if (msgSource == null) msgSource = ApplicationServiceProvider.getService(MessageSource.class);
        return msgSource.getMessage("FontChooser.title.text", null, "FontChooser.title.text", null);
    }

    public static final FormComponentFactory getComponentFactory() {
        return componentFactory;
    }

    public static final List<Integer> getKnownStyles() {
        return knownStyles;
    }

    public static final void setComponentFactory(FormComponentFactory componentFactory) {
        FontChooser.componentFactory = componentFactory;
    }

    private PresentationModel<?> presentationModel;

    private List<String> knownFonts;

    private String fontNamePropertyName = FONT_NAME_PROPERTY;

    private String fontStylePropertyName = FONT_STYLE_PROPERTY;

    private String fontSizePropertyName = FONT_SIZE_PROPERTY;

    private ValueModel sampleTextModel;

    private Font initialFont;

    private FontSelectorPreview fsp;

    private FontSelectorData data;
}
