package fr.umlv.jee.hibou.bean;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean class for Information list object.
 * @author micka, alex, nak, matt
 *
 */
@XmlRootElement(name = "informationListBean", namespace = "http://javax.hibou/jaxws")
public class InformationListBean {

    @XmlElement(name = "list", namespace = "")
    @XmlList
    private InformationBean[] list;

    /**
	 * Constructor.
	 * @param list the informations list.
	 */
    public InformationListBean(List<InformationBean> list) {
        this.list = list.toArray(new InformationBean[list.size()]);
    }

    /**
	 * @return the list
	 */
    public InformationBean[] getList() {
        return list;
    }

    /**
	 * @param list the list to set
	 */
    public void setList(InformationBean[] list) {
        this.list = list;
    }
}
