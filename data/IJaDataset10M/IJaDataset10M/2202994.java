package fr.umlv.jee.hibou.bean;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean class for RSSFeed list object.
 * @author micka, alex, nak, matt
 *
 */
@XmlRootElement(name = "RSSFeedListBean", namespace = "http://javax.hibou/jaxws")
public class RSSFeedListBean {

    @XmlElement(name = "list", namespace = "")
    @XmlList
    private RSSFeedBean[] list;

    /**
	 * Constructor.
	 * @param list the RSSFeed list.
	 */
    public RSSFeedListBean(List<RSSFeedBean> list) {
        this.list = list.toArray(new RSSFeedBean[list.size()]);
    }

    /**
	 * @return the list
	 */
    public RSSFeedBean[] getList() {
        return list;
    }

    /**
	 * @param list the list to set
	 */
    public void setList(RSSFeedBean[] list) {
        this.list = list;
    }
}
