package rql4j.iodata;

import rql4j.domain.IoData;
import rql4j.domain.Link;
import java.util.List;

public class IoLinks extends IoObject {

    public IoLinks(IoData ioData) {
        super(ioData);
    }

    public Link getLinksByName(String name) {
        if (this.ioData != null && this.ioData.getPage() != null && this.ioData.getPage().getLinks() != null && this.ioData.getPage().getLinks().getLinkList() != null) {
            List<Link> links = this.ioData.getPage().getLinks().getLinkList();
            for (Link link : links) {
                if (link.getName().equals(name)) return link;
            }
        }
        return null;
    }
}
