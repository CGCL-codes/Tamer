package confhotelwar.test;

import hotel.ejb.domain.Hotel;
import hotel.ejb.domain.HotelRoom;
import hotel.ejb.domain.RoomType;
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Body;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.Form;
import com.sun.webui.jsf.component.Head;
import com.sun.webui.jsf.component.Html;
import com.sun.webui.jsf.component.Link;
import com.sun.webui.jsf.component.Page;
import javax.faces.FacesException;
import hotel.ejb.dto.hotelInfo;
import confhotelwar.RequestBean1;
import confhotelwar.ApplicationBean1;
import confhotelwar.SessionBean1;
import hotel.ejb.controls.HotelManagementLocal;
import hotel.ejb.dto.roomTypeInfo;
import hotel.ejb.exceptions.DatabaseException;
import java.awt.Label;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import confhotelwar.BookingManagement.Organizer.ModifyPrebooking;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @author Administrator
 */
public class AddTestData extends AbstractPageBean {

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }

    private Page page1 = new Page();

    public Page getPage1() {
        return page1;
    }

    public void setPage1(Page p) {
        this.page1 = p;
    }

    private Html html1 = new Html();

    public Html getHtml1() {
        return html1;
    }

    public void setHtml1(Html h) {
        this.html1 = h;
    }

    private Head head1 = new Head();

    public Head getHead1() {
        return head1;
    }

    public void setHead1(Head h) {
        this.head1 = h;
    }

    private Link link1 = new Link();

    public Link getLink1() {
        return link1;
    }

    public void setLink1(Link l) {
        this.link1 = l;
    }

    private Body body1 = new Body();

    public Body getBody1() {
        return body1;
    }

    public void setBody1(Body b) {
        this.body1 = b;
    }

    private Form form1 = new Form();

    public Form getForm1() {
        return form1;
    }

    public void setForm1(Form f) {
        this.form1 = f;
    }

    private Button btnAddData = new Button();

    public Button getBtnAddData() {
        return btnAddData;
    }

    public void setBtnAddData(Button b) {
        this.btnAddData = b;
    }

    @EJB
    private HotelManagementLocal hotelManager;

    private Button button1 = new Button();

    public Button getButton1() {
        return button1;
    }

    public void setButton1(Button b) {
        this.button1 = b;
    }

    private Button btnClearData = new Button();

    public Button getBtnClearData() {
        return btnClearData;
    }

    public void setBtnClearData(Button b) {
        this.btnClearData = b;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public AddTestData() {
    }

    /**
     * <p>Callback method that is called whenever a page is navigated to,
     * either directly via a URL, or indirectly via page navigation.
     * Customize this method to acquire resources that will be needed
     * for event handlers and lifecycle methods, whether or not this
     * page is performing post back processing.</p>
     * 
     * <p>Note that, if the current request is a postback, the property
     * values of the components do <strong>not</strong> represent any
     * values submitted with this request.  Instead, they represent the
     * property values that were saved for this view when it was rendered.</p>
     */
    @Override
    public void init() {
        super.init();
        try {
            _init();
        } catch (Exception e) {
            log("AddTestData Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }
    }

    /**
     * <p>Callback method that is called after the component tree has been
     * restored, but before any event processing takes place.  This method
     * will <strong>only</strong> be called on a postback request that
     * is processing a form submit.  Customize this method to allocate
     * resources that will be required in your event handlers.</p>
     */
    @Override
    public void preprocess() {
    }

    /**
     * <p>Callback method that is called just before rendering takes place.
     * This method will <strong>only</strong> be called for the page that
     * will actually be rendered (and not, for example, on a page that
     * handled a postback and then navigated to a different page).  Customize
     * this method to allocate resources that will be required for rendering
     * this page.</p>
     */
    @Override
    public void prerender() {
    }

    /**
     * <p>Callback method that is called after rendering is completed for
     * this request, if <code>init()</code> was called (regardless of whether
     * or not this was the page that was actually rendered).  Customize this
     * method to release resources acquired in the <code>init()</code>,
     * <code>preprocess()</code>, or <code>prerender()</code> methods (or
     * acquired during execution of an event handler).</p>
     */
    @Override
    public void destroy() {
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected RequestBean1 getRequestBean1() {
        return (RequestBean1) getBean("RequestBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1) getBean("ApplicationBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
    }

    public String btnAddData_Clicked() {
        hotelInfo hotel = new hotelInfo();
        roomTypeInfo rt = new roomTypeInfo();
        boolean successfulCreation = true;
        try {
            hotel.setName(new String("BB Budapest"));
            hotel.setAddress(new String("Rozalia Utca 76-78, 1031 Budapest"));
            hotel.setBankAccountNo(new String("1112545565"));
            hotel.setPhone(new String("555-55-68"));
            hotel.setMail(new String("foglalas@bbudpest.hu"));
            hotel.setContingentRooms(new Integer(20));
            hotel.setStars(new Integer(5));
            hotel.setBar(true);
            hotelManager.createNewHotel(hotel);
            hotel.setName(new String("Corinthia Grand Hotel"));
            hotel.setAddress(new String("Erzsébet körút 43-49, Budapest"));
            hotel.setBankAccountNo(new String("1188888865"));
            hotel.setPhone(new String("565-56-64"));
            hotel.setMail(new String("foglalas@corinthia.hu"));
            hotel.setContingentRooms(new Integer(30));
            hotel.setStars(new Integer(5));
            hotel.setBar(true);
            hotelManager.createNewHotel(hotel);
            hotel.setName(new String("Ramada"));
            hotel.setAddress(new String("Árpád fejedelem útja 94, Budapest"));
            hotel.setBankAccountNo(new String("1117786565"));
            hotel.setPhone(new String("445-75-68"));
            hotel.setMail(new String("foglalas@ramada.hu"));
            hotel.setContingentRooms(new Integer(10));
            hotel.setStars(new Integer(5));
            hotel.setBar(true);
            hotelManager.createNewHotel(hotel);
            hotel.setName(new String("Domina Inn Fiesta"));
            hotel.setAddress(new String("Király utca 20, Budapest"));
            hotel.setBankAccountNo(new String("13553465565"));
            hotel.setPhone(new String("134-57-99"));
            hotel.setMail(new String("foglalas@rdominainnfiesta.hu"));
            hotel.setContingentRooms(new Integer(8));
            hotel.setStars(new Integer(4));
            hotel.setBar(true);
            hotelManager.createNewHotel(hotel);
            hotel.setName(new String("Hotel Anna"));
            hotel.setAddress(new String("Gyulai Pál u. 14, Budapest "));
            hotel.setBankAccountNo(new String("1117613255"));
            hotel.setPhone(new String("122-88-99"));
            hotel.setMail(new String("foglalas@hotelanna.hu"));
            hotel.setContingentRooms(new Integer(16));
            hotel.setStars(new Integer(3));
            hotel.setBar(false);
            hotelManager.createNewHotel(hotel);
            hotel.setName(new String("Bara Junior"));
            hotel.setAddress(new String("Hegyalja 34-36, Budapest "));
            hotel.setBankAccountNo(new String("1117613255"));
            hotel.setPhone(new String("136-23-67"));
            hotel.setMail(new String("foglalas@barajunior.hu"));
            hotel.setContingentRooms(new Integer(14));
            hotel.setStars(new Integer(2));
            hotel.setBar(false);
            hotelManager.createNewHotel(hotel);
            hotel.setName(new String("Bara Guest Hosue"));
            hotel.setAddress(new String("Hegyalja ut 34-36, Budapest"));
            hotel.setBankAccountNo(new String("1117613255"));
            hotel.setPhone(new String("136-23-67"));
            hotel.setMail(new String("foglalas@baraguesthouse.hu"));
            hotel.setContingentRooms(new Integer(9));
            hotel.setStars(new Integer(1));
            hotel.setBar(false);
            hotelManager.createNewHotel(hotel);
            hotel.setName(new String("Akacfa Holiday Apartments"));
            hotel.setAddress(new String("Akacfa utca 12 - 14,Budapest"));
            hotel.setBankAccountNo(new String("148376487365"));
            hotel.setPhone(new String("214-76-21"));
            hotel.setMail(new String("foglalas@akacfaholiday.hu"));
            hotel.setContingentRooms(new Integer(3));
            hotel.setStars(new Integer(0));
            hotel.setBar(false);
            hotelManager.createNewHotel(hotel);
            rt.setName(new String("Simple"));
            rt.setBarInRoom(false);
            rt.setCanSmoke(false);
            rt.setInternet(false);
            hotelManager.createRoomType(rt);
            rt.setName(new String("Just with Bar"));
            rt.setBarInRoom(true);
            rt.setCanSmoke(false);
            rt.setInternet(false);
            hotelManager.createRoomType(rt);
            rt.setName(new String("Just Smoker"));
            rt.setBarInRoom(false);
            rt.setCanSmoke(true);
            rt.setInternet(false);
            hotelManager.createRoomType(rt);
            rt.setName(new String("Just with Internet"));
            rt.setBarInRoom(false);
            rt.setCanSmoke(false);
            rt.setInternet(true);
            hotelManager.createRoomType(rt);
            rt.setName(new String("Smoker with Bar"));
            rt.setBarInRoom(true);
            rt.setCanSmoke(true);
            rt.setInternet(false);
            hotelManager.createRoomType(rt);
            rt.setName(new String("Smoker with Internet"));
            rt.setBarInRoom(false);
            rt.setCanSmoke(true);
            rt.setInternet(true);
            hotelManager.createRoomType(rt);
            rt.setName(new String("With Bar and Internet"));
            rt.setBarInRoom(false);
            rt.setCanSmoke(false);
            rt.setInternet(false);
            hotelManager.createRoomType(rt);
        } catch (Exception e) {
            successfulCreation = false;
        }
        if (successfulCreation) return "addDatauccess";
        return "case2";
    }

    public String btnClearData_Clicked() {
        List<Hotel> allHotels = hotelManager.getAllHotel();
        int hotelsNum = allHotels.size();
        if (allHotels != null) {
            boolean successfulDelete = true;
            for (int i = 0; i < hotelsNum; i++) {
                try {
                    hotelManager.deleteHotel((allHotels.get(i)).getId());
                } catch (Exception e) {
                    successfulDelete = false;
                    log("Error during deletion of hotel");
                }
                if (successfulDelete) return "deleteHotelSuccess";
            }
        }
        return null;
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected ModifyPrebooking getBookingManagement$Organizer$ModifyPrebooking() {
        return (ModifyPrebooking) getBean("BookingManagement$Organizer$ModifyPrebooking");
    }
}
