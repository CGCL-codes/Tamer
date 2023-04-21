package net.infordata.ifw2.web.tags;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import net.infordata.ifw2.web.view.ECSAdapter;

public class GlueTag extends DivTag {

    private static final long serialVersionUID = 1L;

    private String ivPlaceHolderId;

    private String ivPlaceHolderCSAC;

    private boolean ivOverrideParent = false;

    public GlueTag() {
        super.setGroupRefresh(false);
    }

    @Override
    public void release() {
        super.release();
        super.setGroupRefresh(false);
        ivPlaceHolderId = null;
        ivPlaceHolderCSAC = null;
        ivOverrideParent = false;
    }

    public void setPlaceHolderId(String placeHolderId) {
        ivPlaceHolderId = placeHolderId;
    }

    public void setOverrideParent(boolean value) {
        ivOverrideParent = value;
    }

    @Override
    @Deprecated
    public void setGroupRefresh(boolean groupRefresh) {
        throw new IllegalStateException("Not supported");
    }

    @Override
    protected final String getTag() {
        return "div";
    }

    @Override
    public final int doStartTag() throws JspException {
        if (ivPlaceHolderId == null) throw new NullPointerException("placeHolderId is null");
        ivPlaceHolderCSAC = getClientScriptAccessCode(ivPlaceHolderId);
        if (ivPlaceHolderCSAC == null) throw new IllegalStateException("placeHolderId not found: " + ivPlaceHolderId);
        ivClass = (ivClass == null) ? "GLUE" : ivClass + " GLUE";
        final int res = super.doStartTag();
        return res;
    }

    @Override
    protected void html(Writer wt, char[] content) throws IOException {
        super.html(wt, content);
        wt.write("<script>");
        wt.write("{");
        wt.write("var glue=" + getClientScriptAccessCode() + ";");
        wt.write("var ph=" + ivPlaceHolderCSAC + ";");
        wt.write("glue.$ifw$placeHolder=ph.id;");
        wt.write("glue.$ifw$overrideParent=" + ivOverrideParent + ";");
        wt.write("}");
        wt.write("</script>");
    }

    public static class ECSBaseAdapter extends ECSAdapter<GlueTag> {

        private static final long serialVersionUID = 1L;

        private final GlueTag ivMyTag = new GlueTag();

        private final String ivId, ivPlaceHolderId;

        private Boolean ivOverrideParent;

        private String ivStyle, ivCssClass;

        /**
     * @param id
     * @param placeHolderId - This elements is moved over the placeHolder element
     */
        public ECSBaseAdapter(String id, String placeHolderId) {
            if (id == null) throw new NullPointerException();
            if (placeHolderId == null) throw new NullPointerException();
            ivId = id;
            ivPlaceHolderId = id;
        }

        @Override
        public ECSBaseAdapter setStyle(String value) {
            ivStyle = value;
            return this;
        }

        public ECSBaseAdapter setCssClass(String value) {
            ivCssClass = value;
            return this;
        }

        /**
     * @param value - False by default, if true the glue element is moved over the placeHolder 
     *   parent element
     * @return
     */
        public ECSBaseAdapter setOverrideParent(boolean value) {
            ivOverrideParent = value;
            return this;
        }

        @Override
        public GlueTag getTag() {
            return ivMyTag;
        }

        @Override
        protected void initTag(GlueTag tag) {
            tag.setId(ivId);
            tag.setPlaceHolderId(ivPlaceHolderId);
            if (ivOverrideParent != null) tag.setOverrideParent(ivOverrideParent);
            tag.setCssClass(ivCssClass);
            tag.setStyle(ivStyle);
        }
    }
}
