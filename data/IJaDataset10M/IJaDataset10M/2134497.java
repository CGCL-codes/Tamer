package com.ail.openquote.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import com.ail.core.Type;

/**
 * A RowScroller represents a Collection of records in a table format with one row per record.<br>
 * <p><img src="doc-files/RowScroller.png"/></p>
 * <p>The records included in the scroller are selected by the scroller's {@link #getBinding() binding}, and
 * the columns are defined by the {@link AttributeField AttributeFields} included in the scroller's
 * {@link #getItem() items} list.</p>
 * <p>The form elements used to render each item cell and the validations applied to them are both defined by
 * the nature of the {@link Attribute} that the item is bound to. The binding itself is evaluated relative to the binding
 * of the scroller. See {@link SectionScoller SectionScroller} for an example.</p>
 * <p>RowScroller supports the addition and removal of rows from the scroller if the {@link #isAddAndDeleteEnabled() 
 * addAndDeleteEnabled} property is set to true. If this is the case, then the <b>row controls</b> (see screenshot)
 * are rendered. The {@link #getMinRows() minRows} and {@link #getMaxRows() maxRows} properties limit what the user
 * can do. A user cannot remove rows if that would leave the scroller with less than <i>minRows</i>, and the <b>add 
 * row</b> icon will be disabled if the scroller already contains <i>maxRows</i>. In the screenshot above, the scroller
 * has <i>minRows</i> set to 1, so the user cannot remove the first row. <i>maxRows</i> is set to 4, so in its 
 * current state the user can add one more row.</p>
 * @see SectionScroller
 * @see AttributeField
 */
public class RowScroller extends Repeater {

    private static final long serialVersionUID = -6043887157243002172L;

    @SuppressWarnings("unchecked")
    @Override
    public void renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        PrintWriter w = response.getWriter();
        w.printf("<table width='100%%' border='0' cols='%d'>", isAddAndDeleteEnabled() ? item.size() + 1 : item.size());
        w.printf(" <tr class='portlet-section-alternate'>");
        for (AttributeField a : item) {
            if (a.getSubTitle() != null) {
                w.printf("<td align='center'>%s<br/>%s</td>", a.getTitle(), a.getSubTitle());
            } else {
                w.printf("<td align='center'>%s</td>", a.getTitle());
            }
        }
        if (isAddAndDeleteEnabled()) {
            w.printf("<td>&nbsp;</td>");
            w.printf(" </tr>");
        }
        int rowCount = 0;
        for (Iterator it = model.xpathIterate(getBinding()); it.hasNext(); ) {
            Type t = (Type) it.next();
            w.printf("<tr>");
            for (AttributeField a : item) {
                w.printf("<td align='center'>");
                a.renderResponse(request, response, t, getBinding() + "[" + rowCount + "]");
                w.printf("</td>");
            }
            if (isAddAndDeleteEnabled()) {
                if (rowCount >= getMinRows()) {
                    w.printf("<td align='center'>");
                    w.printf("<input type='image' src='/quotation/images/delete.gif' name='op=delete:id=%s:row=%d:immediate=true:'/>", getId(), rowCount);
                    w.printf("</td>");
                } else {
                    w.printf("<td>&nbsp;</td>");
                }
            }
            w.printf("</tr>");
            rowCount++;
        }
        if (isAddAndDeleteEnabled()) {
            w.print("<tr>");
            for (int i = 0; i < item.size(); i++) {
                w.print("<td>&nbsp;</td>");
            }
            w.printf("<td align='center'>");
            if (maxRows != -1 && rowCount == maxRows) {
                w.printf("<input type='image' src='/quotation/images/add-disabled.gif' name='op=add:id=%s:immediate=true:' disabled='true'/>", getId());
            } else {
                w.printf("<input type='image' src='/quotation/images/add.gif' name='op=add:id=%s:immediate=true:'/>", getId());
            }
            w.printf("</td></tr>");
        }
        w.printf("</table>");
    }
}
