package riafswing;

import javax.swing.JPasswordField;
import riaf.facade.IContainer;
import riaf.facade.IContentAlign;
import riaf.facade.IMutableStyle;
import riaf.facade.IPassword;
import riaf.facade.IStyle;
import riaf.formatters.FormatterResult;
import riaf.models.DefaultModel;
import riafswing.helper.RFocusListener;
import riafswing.helper.RTextDocument;

/**
 * The Class RPassword binds Swing's JPasswordField.
 */
public class RPassword extends RComponent implements IPassword, IContentAlign {

    private RTextDocument textDocument;

    /**
	 * The public constructor instantiating a new JPasswordFiled to a parent
	 * container with an id.
	 * 
	 * @param id
	 *            the id to be used
	 * @param parent
	 *            the parent container to bind the password field to
	 */
    public RPassword(String id, IContainer parent) {
        super(id, parent);
        JPasswordField res = new JPasswordField();
        setComponent(res);
        textDocument = new RTextDocument(this, res);
        res.setDocument(textDocument);
        res.addFocusListener(new RFocusListener(this));
    }

    @Override
    public void clear() {
        textDocument.clear();
        textDocument = null;
        super.clear();
    }

    @Override
    public String getName() {
        return IPassword.name;
    }

    @Override
    public JPasswordField getImpl() {
        return (JPasswordField) super.getImpl();
    }

    public void setSelection(int start, int end) {
        getImpl().setCaretPosition(start);
        getImpl().moveCaretPosition(end);
    }

    @Override
    public void selectAll() {
        getImpl().selectAll();
    }

    @Override
    protected DefaultModel createModel() {
        DefaultModel model = new DefaultModel(this);
        addListener(model);
        return model;
    }

    @Override
    protected void setImplContent(String content) {
        FormatterResult res = format(content);
        super.setImplContent(res.getUiContent());
        textDocument.setSubCall(true);
        getImpl().setText(res.getUiContent());
        textDocument.setSubCall(false);
        if (res.getCaretSetPosition() != -1) getImpl().setCaretPosition(res.getCaretSetPosition());
        if (res.getCaretMovePosition() != -1) getImpl().moveCaretPosition(res.getCaretMovePosition());
    }

    @Override
    protected void clearImplContent() {
        FormatterResult res = format("");
        super.clearImplContent();
        textDocument.setSubCall(true);
        getImpl().setText(res.getUiContent());
        textDocument.setSubCall(false);
        if (res.getCaretSetPosition() != -1) getImpl().setCaretPosition(res.getCaretSetPosition());
        if (res.getCaretMovePosition() != -1) getImpl().moveCaretPosition(res.getCaretMovePosition());
    }

    @Override
    protected ProcessResult setStyle(IStyle style) {
        ProcessResult result = super.setStyle(style);
        setHorizontalAlignmentImpl(style.getInteger(IStyle.Properties.HORIZONTAL_ALIGNMENT));
        return result;
    }

    @Override
    public Integer getHorizontalAlignment() {
        return getImpl().getHorizontalAlignment();
    }

    /**
	 * Returns the default value for a vertical alignment
	 * {@link IStyle#INT_CENTER}, since vertical alignment is not supported from
	 * {@code RPassword}
	 * 
	 * @return {@link IStyle#INT_CENTER}
	 */
    @Override
    public Integer getVerticalAlignment() {
        return IStyle.INT_CENTER;
    }

    public void setHorizontalAlignmentImpl(int alignment) {
        getImpl().setHorizontalAlignment(alignment);
    }

    @Override
    public void fillDefaults(IMutableStyle out) {
        super.fillDefaults(out);
        out.setProperty(IStyle.Properties.HORIZONTAL_ALIGNMENT, getImpl().getHorizontalAlignment());
    }
}
