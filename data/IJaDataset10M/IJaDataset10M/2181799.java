package x36dip.bb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import x36dip.model.PriceList;
import x36dip.sb.SeasonSessionBeanLocal;
import x36dip.model.Season;
import x36dip.sb.RoomSessionBeanLocal;
import x36dip.model.RoomType;
import x36dip.utils.MessagesPicker;

/**
 *
 * @author Petr
 */
@ManagedBean(name = "seasons")
@SessionScoped
public class SeasonBean implements Serializable {

    @EJB
    private SeasonSessionBeanLocal seasonSessionBean;

    @EJB
    private RoomSessionBeanLocal roomSessionBean;

    private Season season = null;

    private PriceList priceList = null;

    private List<PriceList> priceLists = null;

    private RoomType parameter;

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public PriceList getPriceList() {
        return priceList;
    }

    public void setPriceList(PriceList priceList) {
        this.priceList = priceList;
    }

    public List<PriceList> getPriceLists() {
        return priceLists;
    }

    public void setPriceLists(List<PriceList> priceLists) {
        this.priceLists = priceLists;
    }

    public RoomType getParameter() {
        return parameter;
    }

    public void setParameter(RoomType parameter) {
        this.parameter = parameter;
    }

    public List<Season> getAllSeasons() {
        return seasonSessionBean.getAllSeasons();
    }

    public List<PriceList> getallPriceLists() {
        return seasonSessionBean.getAllPriceLists();
    }

    public String newSeason() {
        this.season = new Season();
        return "season";
    }

    public String saveSeason() {
        seasonSessionBean.updateSeason(season);
        return "seasons";
    }

    public String editSeason(Season season) {
        this.season = season;
        return "season";
    }

    public String removeSeason() {
        for (PriceList p : seasonSessionBean.getSeasonsPriceLists(season)) {
            seasonSessionBean.removePriceList(p);
        }
        seasonSessionBean.removeSeason(season);
        return "seasons";
    }

    public String addPriceList(Season season) {
        if (roomSessionBean.getAllRoomTypes() == null || roomSessionBean.getAllRoomTypes().isEmpty()) {
            MessagesPicker.validationError("#{msgs.pricelist_create_error}");
            return "";
        } else {
            this.season = season;
            this.setParameter(roomSessionBean.getAllRoomTypes().get(0));
            List<PriceList> pl = seasonSessionBean.getSeasonsPriceLists(season);
            if (pl == null || pl.isEmpty()) {
                pl = initializePriceLists();
            }
            this.setPriceLists(pl);
            return "pricelist";
        }
    }

    public List<PriceList> initializePriceLists() {
        List<PriceList> p = new ArrayList<PriceList>();
        List<RoomType> rl = roomSessionBean.getAllRoomTypes();
        for (RoomType r : rl) {
            PriceList price = new PriceList();
            price.setOccupancy(1);
            price.setPrice(this.season.getBedPrice());
            price.setRoomtype(r);
            price.setSeason(this.season);
            p.add(price);
        }
        return p;
    }

    public List<PriceList> initializeSpecificPriceList() {
        List<PriceList> p = new ArrayList<PriceList>();
        PriceList price = new PriceList();
        price.setOccupancy(1);
        price.setPrice(this.season.getBedPrice());
        price.setRoomtype(parameter);
        price.setSeason(this.season);
        p.add(price);
        return p;
    }

    public List<PriceList> findPriceListsforRoomType() {
        List<PriceList> pricelist = new ArrayList<PriceList>();
        for (PriceList p : this.priceLists) {
            if (p.getRoomtype().equals(parameter)) {
                pricelist.add(p);
            }
        }
        if (pricelist.isEmpty()) {
            pricelist = initializeSpecificPriceList();
            this.priceLists.addAll(pricelist);
        }
        return pricelist;
    }

    public Integer roomCapacity() {
        return parameter.getBeds() + parameter.getExtrabeds();
    }

    public Integer bedsCapacity() {
        return parameter.getBeds();
    }

    public String addItemPrice() {
        Integer highest = 0;
        PriceList last = null;
        for (PriceList p : this.priceLists) {
            if (p.getRoomtype().equals(parameter)) {
                if (p.getOccupancy() > highest) {
                    highest = p.getOccupancy();
                    last = p;
                }
                System.out.print("priceee:" + p.getOccupancy());
            }
        }
        if (roomCapacity() <= highest) {
            MessagesPicker.validationError("#{msgs.pricelist_max_capacity}");
            return "";
        } else {
            PriceList priceL = new PriceList();
            priceL.setOccupancy(highest + 1);
            if (bedsCapacity() > highest) {
                priceL.setPrice(last.getPrice() + this.season.getBedPrice());
            } else {
                priceL.setPrice(last.getPrice() + this.season.getExtraBedPrice());
            }
            priceL.setRoomtype(parameter);
            priceL.setSeason(this.season);
            this.priceLists.add(priceL);
            return "pricelist";
        }
    }

    public String savePriceList() {
        List<PriceList> pricelist = new ArrayList<PriceList>();
        for (PriceList p : this.priceLists) {
            if (p.getRoomtype().equals(parameter)) {
                pricelist.add(p);
            }
        }
        this.season.setPricelists(pricelist);
        for (PriceList p : pricelist) {
            seasonSessionBean.updatePriceList(p);
        }
        return "seasons";
    }
}
