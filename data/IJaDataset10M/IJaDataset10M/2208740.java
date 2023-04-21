package eu.redseeds.sc.editor.rsl.editors.domain.controls;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import eu.redseeds.common.Constants;
import eu.redseeds.common.SCProjectHelper;
import eu.redseeds.sc.editor.rsl.editors.RequirementEditor;
import eu.redseeds.sc.editor.rsl.editors.RequirementEditorControl;
import eu.redseeds.sc.editor.rsl.editors.UseCaseEditor;
import eu.redseeds.sc.editor.rsl.editors.UseCaseMainView;
import eu.redseeds.sc.terminology.model.RemoteJGWNL;
import eu.redseeds.sc.terminology.model.TermSenseDTO;
import eu.redseeds.scl.model.BusinessLayerFacade;
import eu.redseeds.scl.model.rsl.rsldomainelements.notions.NotionDTO;

/**
 * Creates a hyperlink in a given description styled text
 *
 */
public class CreateHyperlinkListener implements SelectionListener {

    private StyledText descriptionText = null;

    private Composite control = null;

    /**
	 * @param descriptionText control to create hyperlink in
	 * @param control main control on which styled text control resides
	 */
    public CreateHyperlinkListener(StyledText descriptionText, Composite control) {
        super();
        this.descriptionText = descriptionText;
        this.control = control;
    }

    public void widgetSelected(SelectionEvent e) {
        if (descriptionText.getSelectionText().length() == 0) {
            return;
        }
        int x = descriptionText.getSelection().x;
        int y = descriptionText.getSelection().y;
        if (y == descriptionText.getText().length()) {
            y--;
        }
        String text = descriptionText.getText(x, y);
        if (text.startsWith(" ")) {
            x++;
            text = text.substring(1, text.length() - 1);
        }
        if (text.endsWith(" ") || text.endsWith(",") || text.endsWith(".") || text.endsWith(":") || text.endsWith("?") || text.endsWith("!") || text.endsWith(")") || text.endsWith(">") || text.endsWith("]")) {
            y--;
            text = text.substring(0, text.length() - 1);
        }
        String post = "";
        if (y + 1 < descriptionText.getText().length() - 1) {
            post = descriptionText.getText(y + 1, descriptionText.getText().length() - 1);
        }
        if (!text.equalsIgnoreCase("")) {
            if (text.endsWith("\n")) {
                text = text.substring(0, text.length() - 1);
                post = post + "\n";
            }
        }
        Color linkColor = SCProjectHelper.getShell().getDisplay().getSystemColor(Constants.HYPERLINK_FONT_COLOR_TARGET_DOES_NOT_EXIST);
        BusinessLayerFacade facade = DomainElementEditorControlFactory.getFacadeForControl(control);
        if (facade != null) {
            List<NotionDTO> notions = facade.getAllNotions();
            NotionDTO foundNotion = null;
            List<String> lemmas = new ArrayList<String>();
            if (RemoteJGWNL.getInstance().isConnected()) {
                TermSenseDTO[] ts = RemoteJGWNL.getInstance().getTermSenses(text, Constants.TERM_NOUN);
                for (TermSenseDTO sense : ts) {
                    lemmas.add(sense.getLemma());
                }
            }
            for (NotionDTO notion : notions) {
                if (notion.getName().equalsIgnoreCase(text)) {
                    foundNotion = notion;
                    break;
                }
                for (String lemma : lemmas) {
                    if (notion.getName().equalsIgnoreCase(lemma)) {
                        foundNotion = notion;
                        break;
                    }
                }
            }
            if (foundNotion != null) {
                linkColor = SCProjectHelper.getShell().getDisplay().getSystemColor(Constants.HYPERLINK_FONT_COLOR_TARGET_EXISTS);
                if (control instanceof ActorEditorControl) {
                    if (!((ActorEditorControl) control).getActorDTO().checkIfRelated(foundNotion)) {
                        ((ActorEditorControl) control).getActorDTO().addRelatedNotion(foundNotion);
                    }
                } else if (control instanceof NotionEditorControl) {
                    if (!((NotionEditorControl) control).getNotionDTO().checkIfRelated(foundNotion)) {
                        ((NotionEditorControl) control).getNotionDTO().addRelatedNotion(foundNotion);
                    }
                } else if (control instanceof SystemElementEditorControl) {
                    if (!((SystemElementEditorControl) control).getSystemElementDTO().checkIfRelated(foundNotion)) {
                        ((SystemElementEditorControl) control).getSystemElementDTO().addRelatedNotion(foundNotion);
                    }
                } else if (control instanceof RequirementEditorControl) {
                    if (((RequirementEditorControl) control).getRequirement() != null) {
                        if (!((RequirementEditorControl) control).getRequirement().checkIfRelated(foundNotion)) {
                            ((RequirementEditorControl) control).getRequirement().addRelatedNotion(foundNotion);
                        }
                    }
                } else if (control.getParent() instanceof UseCaseMainView) {
                    if (((UseCaseMainView) control.getParent()).getUseCase() != null) {
                        if (!((UseCaseMainView) control.getParent()).getUseCase().checkIfRelated(foundNotion)) {
                            ((UseCaseMainView) control.getParent()).getUseCase().addRelatedNotion(foundNotion);
                        }
                    }
                }
            }
        }
        descriptionText.setStyleRange(new StyleRange(x, text.length(), linkColor, null, Constants.HYPERLINK_FONT_STYLE));
        if (control instanceof ActorEditorControl) {
            ((ActorEditorControl) control).setDirty();
        } else if (control instanceof NotionEditorControl) {
            ((NotionEditorControl) control).setDirty();
        } else if (control instanceof SystemElementEditorControl) {
            ((SystemElementEditorControl) control).setDirty();
        } else if (control instanceof RequirementEditorControl) {
            ((RequirementEditor) ((RequirementEditorControl) control).getEditor()).setDirty(true);
        } else if (control.getParent() instanceof UseCaseMainView) {
            ((UseCaseEditor) ((UseCaseMainView) control.getParent()).getEditor()).setDirty(true);
        }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
    }
}
