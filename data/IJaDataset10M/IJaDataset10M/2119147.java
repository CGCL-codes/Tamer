package jframe.taglib.bean.html;

import java.io.Writer;
import org.apache.struts2.components.Component;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @描述:<p>select标签属性bean </p>
 *
 * @作者: 叶平平(yepp)
 *
 * @时间: 2012-2-26 下午03:56:08
 */
public class HtmlSelect extends Component {

    /**
	 * 构造方法
	 */
    public HtmlSelect(ValueStack stack) {
        super(stack);
    }

    public boolean start(Writer writer) {
        boolean result = super.start(writer);
        return result;
    }
}
