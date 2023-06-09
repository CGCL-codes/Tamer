package net.sourceforge.vrapper.vim.commands;

import net.sourceforge.vrapper.utils.ContentType;
import net.sourceforge.vrapper.utils.Position;
import net.sourceforge.vrapper.utils.TextRange;
import net.sourceforge.vrapper.utils.VimUtils;
import net.sourceforge.vrapper.vim.EditorAdaptor;
import net.sourceforge.vrapper.vim.modes.NormalMode;
import net.sourceforge.vrapper.vim.register.RegisterContent;
import net.sourceforge.vrapper.vim.register.StringRegisterContent;

public class YankOperation extends SimpleTextOperation {

    public static final YankOperation INSTANCE = new YankOperation();

    private YankOperation() {
    }

    @Override
    public void execute(EditorAdaptor editorAdaptor, TextRange region, ContentType contentType) {
        doIt(editorAdaptor, region, contentType);
    }

    public TextOperation repetition() {
        return null;
    }

    public static void doIt(EditorAdaptor editorAdaptor, TextRange range, ContentType contentType) {
        String text = editorAdaptor.getModelContent().getText(range.getLeftBound().getModelOffset(), range.getModelLength());
        if (contentType == ContentType.LINES && (text.length() == 0 || !VimUtils.isNewLine(text.substring(text.length() - 1)))) {
            text += editorAdaptor.getConfiguration().getNewLine();
        }
        RegisterContent content = new StringRegisterContent(contentType, text);
        editorAdaptor.getRegisterManager().getActiveRegister().setContent(content);
        if (contentType == ContentType.LINES && NormalMode.NAME.equals(editorAdaptor.getCurrentModeName())) {
            int lineNo = editorAdaptor.getModelContent().getLineInformationOfOffset(range.getLeftBound().getModelOffset()).getNumber();
            Position stickyPosition = editorAdaptor.getCursorService().stickyColumnAtModelLine(lineNo);
            editorAdaptor.getCursorService().setPosition(stickyPosition, true);
        } else {
            editorAdaptor.getCursorService().setPosition(range.getLeftBound(), true);
        }
    }
}
