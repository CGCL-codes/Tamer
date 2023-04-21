package fashionstore.manager;

import java.sql.SQLException;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restfaces.annotation.HttpAction;
import org.restfaces.annotation.Instance;
import org.restfaces.annotation.Param;
import fashionstore.dao.DealerDao;
import fashionstore.dao.HumanDao;
import fashionstore.domain.Dealer;
import fashionstore.domain.Human;
import fashionstore.fileUpload.FileUploadBean;

@Instance("dealerManager")
public class DealerManager {

    private Dealer dealer = null;

    private DealerDao dealerDao = null;

    private final Log logger = LogFactory.getLog(this.getClass());

    private PhotoViewBean photoViewBean = null;

    private List<Dealer> dealerList = null;

    private HumanDao humanDao = null;

    private List<Human> concernedHumanList = null;

    private FileUploadBean fileUploadBean = null;

    public List<Dealer> getDealerList() {
        return dealerList;
    }

    public void setDealerList(List<Dealer> dealerList) {
        this.dealerList = dealerList;
    }

    public FileUploadBean getFileUploadBean() {
        return fileUploadBean;
    }

    public void setFileUploadBean(FileUploadBean fileUploadBean) {
        this.fileUploadBean = fileUploadBean;
    }

    public HumanDao getHumanDao() {
        return humanDao;
    }

    public void setHumanDao(HumanDao humanDao) {
        this.humanDao = humanDao;
    }

    public List<Human> getConcernedHumanList() {
        return concernedHumanList;
    }

    public void setConcernedHumanList(List<Human> concernedHumanList) {
        this.concernedHumanList = concernedHumanList;
    }

    public PhotoViewBean getPhotoViewBean() {
        return photoViewBean;
    }

    public void setPhotoViewBean(PhotoViewBean photoViewBean) {
        this.photoViewBean = photoViewBean;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public DealerDao getDealerDao() {
        return dealerDao;
    }

    public void setDealerDao(DealerDao dealerDao) {
        this.dealerDao = dealerDao;
    }

    @HttpAction(value = "viewDealerDetails")
    public String detail(@Param("id") Long id) {
        try {
            dealerDao.queryById(id, dealer);
            humanDao.queryByDealerId(id, concernedHumanList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "/view/dealer/details.jsf";
    }

    @HttpAction(value = "viewDealerPhoto")
    public String view(@Param("id") Long id, @Param("name") String name) {
        if (!id.equals(dealer.getId())) {
            try {
                dealerDao.queryById(id, dealer);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        photoViewBean.setImageNames(dealer.getImageNameArray());
        photoViewBean.setCurrentImageName(name);
        return "/view/dealer/photo.jsf";
    }
}
