package sourceagile.development.client.sourceCode;

import sourceagile.development.client.features.FeatureVizualizationPanel;
import sourceagile.shared.entities.entry.ClassFile;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.gwtext.client.widgets.SyntaxHighlightPanel;

/**
 * Show the source code of the class with the syntax highlighted in this web
 * bases.
 * 
 * @MainFeature
 */
public class SourceCodeView {

    public SourceCodeView(ClassFile entry) {
        FeatureVizualizationPanel.featureContent.clear();
        FeatureVizualizationPanel.featureContent.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        FeatureVizualizationPanel.featureContent.add(getSyntaxPanel(entry));
        FeatureVizualizationPanel.featureContent.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        FeatureVizualizationPanel.featureContent.add(new Label(" "));
        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(20);
        hp.add(new ButtonInProgress(entry));
        hp.add(new ButtonBlocked(entry));
        hp.add(buttonEdit(entry));
        FeatureVizualizationPanel.featureContent.add(hp);
        FeatureVizualizationPanel.featureContent.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    }

    private SyntaxHighlightPanel getSyntaxPanel(ClassFile entry) {
        SyntaxHighlightPanel syntaxPanel = new SyntaxHighlightPanel(entry.getSourceCode(), getFileExtension(entry));
        syntaxPanel.enable();
        syntaxPanel.setSize(700, 600);
        syntaxPanel.setTitle(entry.getFileName());
        syntaxPanel.setAutoScroll(true);
        return syntaxPanel;
    }

    private String getFileExtension(ClassFile entry) {
        String fileExtension = null;
        String[] fileNameArray = entry.getFileName().split("\\.");
        if (fileNameArray.length > 1) {
            fileExtension = fileNameArray[1];
        }
        return fileExtension;
    }

    private Button buttonEdit(final ClassFile entry) {
        Button button = new Button("Edit", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                new SourceCodeEdition(entry);
            }
        });
        return button;
    }
}
