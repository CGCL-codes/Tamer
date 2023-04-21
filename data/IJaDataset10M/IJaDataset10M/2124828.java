package seedpod.webapp.view.seedpodwidgets;

import seedpod.exceptions.InvalidWidgetException;
import seedpod.model.AVPair;
import seedpod.model.ProtegeValueType;
import seedpod.model.SeedpodModel.RdbCls.FormWidget;
import seedpod.model.SeedpodModel.RdbCls.ViewWidget;
import seedpod.model.rdb.RdbValueType;
import seedpod.webapp.Seedpod;
import seedpod.webapp.view.htmlwidget.ImageButton;
import seedpod.webapp.view.htmlwidget.TextInput;

public class Password extends Text {

    private String _encPwd;

    public Password() {
    }

    public Password(AVPair avpair) {
        super(avpair);
    }

    @Override
    public FormWidget getFormWidgetType() {
        return FormWidget.PASSWORD;
    }

    @Override
    public ViewWidget getViewWidgetType() {
        return ViewWidget.PASSWORD;
    }

    @Override
    public String render() {
        String html = "";
        try {
            validateWidget();
        } catch (InvalidWidgetException e) {
            return renderErrorComment(e.getMessage());
        }
        TextInput pwdInput = new TextInput(_widgetID, _encPwd);
        pwdInput.readOnlyInput(true);
        pwdInput.setType("password");
        pwdInput.setCssClass("text");
        pwdInput.setMaxLength(TextInput.MAX_LENGTH);
        pwdInput.setSize(super.getInputSize());
        String commentButtonUrl = Seedpod.getAbsoluteUrl("/images/comment.gif");
        ImageButton commentBtn = new ImageButton("pwdComment", commentButtonUrl, "#", "click for message");
        commentBtn.addJavascript("onclick=\"Effect.toggle('uneditable','appear'); return false;\" ");
        String comment = "<span id=\"uneditable\" style=\"display:none; position:absolute;\">";
        comment += "Sorry, the password is not editable.";
        comment += "</span>";
        html = "<div style=\"padding:2px\">" + pwdInput.render() + commentBtn.render() + comment + "</div>";
        return html;
    }

    @Override
    public void setData(Object password) {
        _encPwd = (String) password;
    }

    @Override
    public void validateWidget() throws InvalidWidgetException {
        boolean valid = getAttribute().getRdbValueType().equals(RdbValueType.VARCHAR) && getAttribute().getProtegeValueType().equals(ProtegeValueType.String);
        if (!valid) throw new InvalidWidgetException(" Password is a String value.");
    }
}
