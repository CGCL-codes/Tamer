package hrc.tool.xml;

import org.w3c.dom.Element;

/**
 * implements this when you want to convert a xml dom to a more flexible bean
 * @author hrc
 *
 */
public interface XmlMapper<T> {

    /**
	 * 
	 * @param element a dom element that given by the xpath query
	 * @param index the index of the result,it can be 0,1,2.....base on the length of the result
	 * @return the bean you what to load
	 */
    public T mapRow(Element element, int index);
}
