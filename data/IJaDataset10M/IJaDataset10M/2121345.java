package pl.edu.pw.DVDManiac.JavaBeans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import pl.edu.pw.DVDManiac.AccountsManager.AccountsManager;
import pl.edu.pw.DVDManiac.AccountsManager.iAccountsInterface;
import pl.edu.pw.DVDManiac.hibernate.dao.DaoFactory;
import pl.edu.pw.DVDManiac.hibernate.dao.DvdDAO;
import pl.edu.pw.DVDManiac.hibernate.dao.DvdOrderDAO;
import pl.edu.pw.DVDManiac.hibernate.model.pojo.Account;
import pl.edu.pw.DVDManiac.hibernate.model.pojo.Dvd;
import pl.edu.pw.DVDManiac.hibernate.model.pojo.DvdOrder;
import pl.edu.pw.DVDManiac.hibernate.model.pojo.Payment;
import pl.edu.pw.DVDManiac.orders.DVDOrderStatus;
import pl.edu.pw.DVDManiac.orders.OrderManager;

public class KasjerZwrotBean {

    private Account user;

    private long user_id;

    private String tytul;

    private int wybranyFilm;

    private List<DvdOrder> orders;

    private List<SelectItem> listaFilmow;

    public KasjerZwrotBean() {
    }

    /**	
	 * metody do obslugi wyszukiwania uzytkownika
	 */
    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long nuser_id) {
        user_id = nuser_id;
    }

    public Account getUser() {
        iAccountsInterface iAcc = AccountsManager.getInstance();
        user = iAcc.doSearch(user_id);
        if (user == null) return new Account(); else return user;
    }

    /**	
	 * metody do obslugi wyszukiwania i wyboru filmu
	 * 
	 */
    public void setTytul(String ntytul) {
        tytul = ntytul;
    }

    public String getTytul() {
        return tytul;
    }

    public void setWybranyFilm(int film_id) {
        wybranyFilm = film_id;
    }

    public int getWybranyFilm() {
        return wybranyFilm;
    }

    public void changeWybranyFilm(ValueChangeEvent event) {
    }

    public List<SelectItem> getListaFilmow() {
        listaFilmow = new ArrayList<SelectItem>();
        DvdOrderDAO orderDAO = DaoFactory.getDvdOrderDAO();
        orders = orderDAO.getUserOrders(user, DVDOrderStatus.IN_PROGRESS);
        for (DvdOrder order : orders) {
            SelectItem tmp = new SelectItem();
            tmp.setLabel(order.getDvd().getTitle());
            tmp.setValue(order.getDvd().getId());
            listaFilmow.add(tmp);
        }
        return listaFilmow;
    }

    public DvdOrder getOrder() {
        DvdOrderDAO dvdOrderDAO = DaoFactory.getDvdOrderDAO();
        DvdDAO dvdDAO = DaoFactory.getDvdDAO();
        Dvd dvdMovie = wybranyFilm > 0 ? dvdDAO.getDvdById(wybranyFilm) : null;
        DvdOrder order = null;
        if (user != null && dvdMovie != null) {
            order = dvdOrderDAO.getOrderByOwnerAndMovie(user, dvdMovie);
        }
        return order;
    }

    public boolean isLate() {
        if (getOrder() != null) {
            Date now = new Date();
            Date dateTo = new Date();
            dateTo.setYear(getOrder().getDate_to().getYear());
            dateTo.setMonth(getOrder().getDate_to().getMonth());
            dateTo.setDate(getOrder().getDate_to().getDay());
            return now.before(dateTo);
        }
        return false;
    }

    public String zwrot() {
        try {
            Date toDay = new Date();
            DvdOrderDAO dvdOrderDAO = DaoFactory.getDvdOrderDAO();
            DvdDAO dvdDAO = DaoFactory.getDvdDAO();
            Dvd dvdMovie = wybranyFilm > 0 ? dvdDAO.getDvdById(wybranyFilm) : null;
            DvdOrder order = dvdOrderDAO.getOrderByOwnerAndMovie(user, dvdMovie);
            if (order == null) {
                return "";
            }
            int days = Math.round((order.getDate_from().getTime() - toDay.getTime()) / (24 * 60 * 60 * 1000)) + 1;
            Payment payment = order.getPayment();
            if (payment != null) {
                payment.setAmount(Float.valueOf(days * dvdMovie.getPrice()).doubleValue());
            }
            order.setStatus(DVDOrderStatus.FINISHED);
            OrderManager.saveOrder(order);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("msgOk", new FacesMessage("Zarejestrowano zwrot"));
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            sessionMap.remove("kasjerZwrotBean");
            return "stronaStartowa";
        } catch (Exception e) {
            System.err.println("niepoprawna data");
        }
        return "";
    }
}
