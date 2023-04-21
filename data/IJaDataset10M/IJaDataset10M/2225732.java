package Action.traitsearch;

import java.util.Hashtable;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import traitmap.filter.AccessControl;
import traitmap.login.UserInformation;
import Action.AxisInfo;
import Tflame.FormBean;
import Tflame.Abstracts.BaseActionAbstract;

/**
 * TraitMap�f�[�^�x�[�X�y�щ{���V�X�e��, �Q�O�O�R�N�P��<br>
 * �S���ҁF�L�c�N�Y�A�Q�m���m���x�[�X�����J���`�[���E�C���t�H�}�e�B�N�X��Վ{�݁EGSC�ERIKEN
 * 
 * XML�t�@�C���i�[�p�R���e�i��XML Templete�\���́B
 * 
 * @deprecated
 * @version	1.1
 * @author	A.Moroda
 */
public class TraitSearchAction extends BaseActionAbstract {

    static Logger log = Logger.getLogger(TraitSearchAction.class);

    /**
	 * @see Tflame.Abstracts.BaseActionAbstract#Action(FormBean)
	 */
    public FormBean Action(FormBean fb) {
        Hashtable param = fb.getParamTable();
        String keyword = (String) param.get("keyword");
        String hHead = (String) param.get("hHead");
        String hCheck = (String) param.get("hCheck");
        AxisInfo horizontal = new AxisInfo(hHead, hCheck);
        TraitSearchCommand command = new TraitSearchCommand();
        command.setSpeciesAlias(horizontal.getSpeciesAlias());
        command.setSpeciesCheck(horizontal.getCheck());
        command.setChromosome(horizontal.getChromosome());
        command.setStart(horizontal.getStart());
        command.setEnd(horizontal.getEnd());
        Document doc;
        try {
            doc = command.searchFullText(keyword);
            fb.setUserInformation();
            doc = appendAccessControl(doc, fb.getUserInformation());
            fb.setDoc(doc);
        } catch (TransformerException e) {
            fb.setErrorLog("TraitSearch: ", e);
        }
        return fb;
    }

    private Document appendAccessControl(Document doc, UserInformation userInfo) {
        NodeList nl = doc.getElementsByTagName("entry");
        try {
            for (int i = 0; i < nl.getLength(); i++) {
                Element _node = (Element) nl.item(i);
                String traitId = _node.getAttribute("name");
                boolean isAccessible = AccessControl.isControlTrait(userInfo, traitId);
                if (isAccessible) {
                    Node pNode = _node.getParentNode();
                    pNode.removeChild(_node);
                }
                log.debug(traitId + ": " + isAccessible);
            }
        } catch (Exception e) {
            log.error(e);
        }
        return doc;
    }
}
