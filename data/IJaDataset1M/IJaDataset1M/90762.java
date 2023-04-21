package flickr.response;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author leon
 */
@XmlRootElement(name = "photosets")
public class PhotoSets extends ResponseObject {

    private List<PhotoSet> photoSets;

    @XmlElements(@XmlElement(name = "photoset"))
    public List<PhotoSet> getPhotoSets() {
        if (photoSets == null) {
            photoSets = new ArrayList<PhotoSet>();
        }
        return photoSets;
    }

    public void setPhotoSets(List<PhotoSet> photoSets) {
        this.photoSets = photoSets;
    }
}
