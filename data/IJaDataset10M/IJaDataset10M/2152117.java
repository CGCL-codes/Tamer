package org.openjf.bbcode;

import java.util.HashMap;
import java.util.Map;
import org.openjf.template.TemplateModel;
import org.openjf.template.SimpleTemplate;

public class ReplacementTag implements Tag {

    private String bbCode;

    private SimpleTemplate replacementTemplate;

    public ReplacementTag(String bbCode, String replacementHtml) {
        this.bbCode = bbCode;
        this.replacementTemplate = new SimpleTemplate(replacementHtml);
    }

    public String getBBCode() {
        return bbCode;
    }

    public boolean valid(TagState state) {
        return true;
    }

    public void open(TagState state) {
        state.setIgnoreContent(true);
    }

    public void openClean(TagState state) {
        open(state);
    }

    public void close(TagState state, boolean explicitClose) {
        final TagState internalState = state;
        Map model = new HashMap(internalState.getTemplateModel());
        model.put("matcher", new TemplateModel() {

            public String value(String expression) {
                return internalState.getContent();
            }
        });
        state.setText(replacementTemplate.format(model));
    }

    public void closeClean(TagState state, boolean explicitClose) {
        state.setText(" " + state.getContent() + " ");
    }

    public boolean endTagNeeded(TagState state) {
        return true;
    }
}
