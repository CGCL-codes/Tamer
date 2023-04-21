package basic.sell;

import basic.logger.SimpleLogger;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Stub;
import uExhibitClasses.TBid;
import uExhibitClasses.TFullOfferInfo;
import uExhibitClasses.TPhoto;
import uExhibitClasses.holders.TFullOfferInfoHolder;

/**
 * @author Admin
 */
public class OfferDetailedBean {

    private Stub stub = null;

    private org.tempuri.IExhibitSrvbindingStub service;

    private OfferDetailed offerDetailed;

    private TFullOfferInfo tInfo;

    private TFullOfferInfoHolder tInfoHolder;

    private int offerID = -1;

    private ImagePaginator imagePaginator;

    public OfferDetailedBean() {
        init();
    }

    protected void init() {
        try {
            stub = (Stub) (new org.tempuri.IExhibitSrvserviceLocator().getIExhibitSrvPort());
        } catch (ServiceException ex) {
            System.out.println("myError! When: " + "Creating stub." + " Reason: " + ex.toString());
        }
        service = (org.tempuri.IExhibitSrvbindingStub) stub;
        offerDetailed = new OfferDetailed();
        imagePaginator = new ImagePaginator();
        String t1 = new String();
        int t2 = 0;
        double t3 = 0;
        tInfo = new TFullOfferInfo(t1, t1, t2, t1, t1, t3, t1, t3, t1, t3, t1, t1, t1, t1, t1, t1, t1, t2, new GregorianCalendar(), t1, t2, t2, t2, t2, new TBid[0], new GregorianCalendar(), new TPhoto[0]);
        tInfoHolder = new TFullOfferInfoHolder(tInfo);
        parse();
    }

    private void createOfferDetailed(Integer id, Integer image) {
        offerID = id;
        service.setHeader("urn:uExhibitSrvIntf", "TAuthHeader", "null");
        try {
            service.getFullOfferInfo(id, tInfoHolder);
        } catch (RemoteException ex) {
            SimpleLogger.logging(ex.toString());
            return;
        }
        Calendar srvTime = null;
        try {
            srvTime = service.getSrvTime();
        } catch (RemoteException ex) {
            SimpleLogger.logging(ex.toString());
            return;
        }
        offerDetailed.setCategoryName(tInfoHolder.value.getCategoryName());
        offerDetailed.setPreviews(tInfoHolder.value.getPreviews());
        offerDetailed.setCurrentPhoto(image);
        offerDetailed.setId(id);
        offerDetailed.setNameOffer(tInfoHolder.value.getNameOffer());
        long endTime = tInfoHolder.value.getDateEnd().getTime().getTime();
        long startTime = srvTime.getTime().getTime();
        long time = endTime - startTime;
        offerDetailed.setDiffTime(time / 1000);
        offerDetailed.setLotValue(tInfoHolder.value.getLotValue());
        offerDetailed.setEdIzm(tInfoHolder.value.getEdIzm());
        offerDetailed.setPrice(tInfoHolder.value.getPrice());
        offerDetailed.setCurrency(tInfoHolder.value.getCurrency());
        offerDetailed.setAct(tInfoHolder.value.getAct());
        offerDetailed.setFirmMade(tInfoHolder.value.getFirmMade());
        offerDetailed.setBidsCount(tInfoHolder.value.getBidsCount());
        offerDetailed.setCountryMade(tInfoHolder.value.getCountryMade());
        offerDetailed.setCountryName(tInfoHolder.value.getCountryName());
        offerDetailed.setLocation(tInfoHolder.value.getLocation());
        offerDetailed.setPayCondition(tInfoHolder.value.getPayCondition());
        offerDetailed.setDeliveryCondition(tInfoHolder.value.getDeliveryCondition());
        offerDetailed.setDeliveryPeriod(tInfoHolder.value.getDeliveryPeriod());
        offerDetailed.setOfferInfo(tInfoHolder.value.getOfferInfo());
        offerDetailed.setOfferInfo(tInfoHolder.value.getOfferInfo());
        offerDetailed.setBids(tInfoHolder.value.getBids());
        imagePaginator.createPaginator(image, tInfoHolder.value.getPreviews().length);
    }

    public void parse() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpServletRequest rq = (HttpServletRequest) ec.getRequest();
        String sOfferID = rq.getParameter("offer_id");
        if (sOfferID != null) {
            if (!sOfferID.equals("")) {
                try {
                    int id = Integer.parseInt(sOfferID);
                    String sImage = rq.getParameter("image");
                    if (sImage != null) {
                        if (!sImage.equals("")) {
                            try {
                                int image = Integer.parseInt(sImage);
                                createOfferDetailed(id, image);
                            } catch (NumberFormatException ex) {
                                SimpleLogger.logging(ex.toString());
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (NumberFormatException ex) {
                    SimpleLogger.logging(ex.toString());
                    return;
                }
            } else {
                return;
            }
        } else {
            return;
        }
    }

    public OfferDetailed getOfferDetailed() {
        return offerDetailed;
    }

    public ImagePaginator getImagePaginator() {
        return imagePaginator;
    }

    public int getOfferID() {
        return offerID;
    }

    public boolean isBidsTableVisible() {
        if (offerDetailed.getBids().size() == 0) {
            return false;
        } else {
            return true;
        }
    }
}
