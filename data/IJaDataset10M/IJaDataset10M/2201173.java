package corner.orm.tapestry.component.insert;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import corner.util.StringUtils;

/**
 * 复写Tapestry的Insert,让Insert可以指定显示字符的长度,大于该长度的部分用...代替
 * @author Ghost
 * @version $Revision: 4531 $
 * @since 2.2.1
 */
public abstract class Insert extends org.apache.tapestry.components.Insert {

    @Override
    protected void printText(IMarkupWriter writer, IRequestCycle cycle, String value) {
        if (StringUtils.notBlank(value) && this.getLength() > 0 && value.length() > this.getLength()) {
            StringBuffer buffer = new StringBuffer(value.substring(0, this.getLength()));
            buffer.append("...");
            value = buffer.toString();
        }
        super.printText(writer, cycle, value);
    }

    /**
	 * 取得指定该字段显示的长度
	 * @return
	 */
    public abstract int getLength();
}
