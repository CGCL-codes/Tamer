package whf.shop.web.ext;

import java.util.Map;
import whf.framework.web.tag.TableRow;
import whf.framework.web.tag.ext.TableRowHTMLDecorator;
import whf.framework.web.tag.ext.LineNumberColorDifferRowDecorator;
import whf.shop.entity.Member;

/**
 * @author wanghaifeng
 * @create Apr 10, 2007 2:21:34 PM
 * 
 */
public class ConfirmRowDecorator extends LineNumberColorDifferRowDecorator implements TableRowHTMLDecorator {

    public ConfirmRowDecorator(String color1, String color2) {
        super();
    }

    public String decorateRow(TableRow row, Map<String, String> descriptors) {
        Member member = (Member) row.getRowObject();
        if (!member.isConfirmed()) {
            return "bgcolor='#FF000000'";
        } else {
            return super.decorateRow(row, descriptors);
        }
    }
}
