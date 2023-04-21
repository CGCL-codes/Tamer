package confhotelwar.BookingManagement.Organizer;

import com.sun.webui.jsf.component.Calendar;
import com.sun.webui.jsf.component.Message;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.TextField;
import confhotelwar.*;
import com.sun.webui.jsf.component.Body;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.DropDown;
import com.sun.webui.jsf.component.Form;
import com.sun.webui.jsf.component.Head;
import com.sun.webui.jsf.component.Html;
import com.sun.webui.jsf.component.Label;
import com.sun.webui.jsf.component.Link;
import com.sun.webui.jsf.component.Page;
import com.sun.webui.jsf.model.Option;
import com.sun.webui.jsf.model.SingleSelectOptionsList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import confhotelwar.RequestBean1;
import confhotelwar.ApplicationBean1;
import confhotelwar.SessionBean1;
import hotel.ejb.controls.BookingManagerLocal;
import hotel.ejb.controls.HotelManagementLocal;
import hotel.ejb.controls.UserManagementLocal;
import hotel.ejb.domain.Hotel;
import hotel.ejb.domain.HotelRoom;
import hotel.ejb.domain.Participant;
import hotel.ejb.domain.RoomType;
import hotel.ejb.exceptions.DatabaseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.event.ValueChangeEvent;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @author Administrator
 */
public class ModifyPrebooking extends PageCodeBase {

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

    private SingleSelectOptionsList dropDown4DefaultOptions = new SingleSelectOptionsList();

    public SingleSelectOptionsList getDropDown4DefaultOptions() {
        return dropDown4DefaultOptions;
    }

    public void setDropDown4DefaultOptions(SingleSelectOptionsList ssol) {
        this.dropDown4DefaultOptions = ssol;
    }

    private HtmlPanelGrid gridPanel1 = new HtmlPanelGrid();

    public HtmlPanelGrid getGridPanel1() {
        return gridPanel1;
    }

    public void setGridPanel1(HtmlPanelGrid hpg) {
        this.gridPanel1 = hpg;
    }

    private Label label1 = new Label();

    public Label getLabel1() {
        return label1;
    }

    public void setLabel1(Label l) {
        this.label1 = l;
    }

    private DropDown dropDown1 = new DropDown();

    public DropDown getDropDown1() {
        return dropDown1;
    }

    public void setDropDown1(DropDown dd) {
        this.dropDown1 = dd;
    }

    private HtmlPanelGrid gridPanel2 = new HtmlPanelGrid();

    public HtmlPanelGrid getGridPanel2() {
        return gridPanel2;
    }

    public void setGridPanel2(HtmlPanelGrid hpg) {
        this.gridPanel2 = hpg;
    }

    private Label label2 = new Label();

    public Label getLabel2() {
        return label2;
    }

    public void setLabel2(Label l) {
        this.label2 = l;
    }

    private DropDown dropDown2 = new DropDown();

    public DropDown getDropDown2() {
        return dropDown2;
    }

    public void setDropDown2(DropDown dd) {
        this.dropDown2 = dd;
    }

    private HtmlPanelGrid grpModifyPrebooking = new HtmlPanelGrid();

    public HtmlPanelGrid getGrpModifyPrebooking() {
        return grpModifyPrebooking;
    }

    public void setGrpModifyPrebooking(HtmlPanelGrid hpg) {
        this.grpModifyPrebooking = hpg;
    }

    private Label lblFrom = new Label();

    public Label getLblFrom() {
        return lblFrom;
    }

    public void setLblFrom(Label l) {
        this.lblFrom = l;
    }

    private Label lblTo = new Label();

    public Label getLblTo() {
        return lblTo;
    }

    public void setLblTo(Label l) {
        this.lblTo = l;
    }

    private Button btnModify = new Button();

    public Button getBtnModify() {
        return btnModify;
    }

    public void setBtnModify(Button b) {
        this.btnModify = b;
    }

    private SingleSelectOptionsList dropDown14DefaultOptions = new SingleSelectOptionsList();

    public SingleSelectOptionsList getDropDown14DefaultOptions() {
        return dropDown14DefaultOptions;
    }

    public void setDropDown14DefaultOptions(SingleSelectOptionsList ssol) {
        this.dropDown14DefaultOptions = ssol;
    }

    private HtmlPanelGrid errorPanel1 = new HtmlPanelGrid();

    public HtmlPanelGrid getErrorPanel1() {
        return errorPanel1;
    }

    public void setErrorPanel1(HtmlPanelGrid hpg) {
        this.errorPanel1 = hpg;
    }

    private Label lblName = new Label();

    public Label getLblName() {
        return lblName;
    }

    public void setLblName(Label l) {
        this.lblName = l;
    }

    private DropDown ddlName = new DropDown();

    public DropDown getDdlName() {
        return ddlName;
    }

    public void setDdlName(DropDown dd) {
        this.ddlName = dd;
    }

    private Label lblMail = new Label();

    public Label getLblMail() {
        return lblMail;
    }

    public void setLblMail(Label l) {
        this.lblMail = l;
    }

    private Label lblUserMail = new Label();

    public Label getLblUserMail() {
        return lblUserMail;
    }

    public void setLblUserMail(Label l) {
        this.lblUserMail = l;
    }

    private Label lblCity = new Label();

    public Label getLblCity() {
        return lblCity;
    }

    public void setLblCity(Label l) {
        this.lblCity = l;
    }

    private Label lblUserCity = new Label();

    public Label getLblUserCity() {
        return lblUserCity;
    }

    public void setLblUserCity(Label l) {
        this.lblUserCity = l;
    }

    private Label lblPAddress = new Label();

    public Label getLblPAddress() {
        return lblPAddress;
    }

    public void setLblPAddress(Label l) {
        this.lblPAddress = l;
    }

    private Label lblUserAddress = new Label();

    public Label getLblUserAddress() {
        return lblUserAddress;
    }

    public void setLblUserAddress(Label l) {
        this.lblUserAddress = l;
    }

    private Label label11 = new Label();

    public Label getLabel11() {
        return label11;
    }

    public void setLabel11(Label l) {
        this.label11 = l;
    }

    private Label lblUserPhone = new Label();

    public Label getLblUserPhone() {
        return lblUserPhone;
    }

    public void setLblUserPhone(Label l) {
        this.lblUserPhone = l;
    }

    private Label lblHotelName = new Label();

    public Label getLblHotelName() {
        return lblHotelName;
    }

    public void setLblHotelName(Label l) {
        this.lblHotelName = l;
    }

    private DropDown ddlHotelName = new DropDown();

    public DropDown getDdlHotelName() {
        return ddlHotelName;
    }

    public void setDdlHotelName(DropDown dd) {
        this.ddlHotelName = dd;
    }

    private Label lblMail1 = new Label();

    public Label getLblMail1() {
        return lblMail1;
    }

    public void setLblMail1(Label l) {
        this.lblMail1 = l;
    }

    private Label lblHotelMail = new Label();

    public Label getLblHotelMail() {
        return lblHotelMail;
    }

    public void setLblHotelMail(Label l) {
        this.lblHotelMail = l;
    }

    private Label lblPhone = new Label();

    public Label getLblPhone() {
        return lblPhone;
    }

    public void setLblPhone(Label l) {
        this.lblPhone = l;
    }

    private Label lblHotelPhone = new Label();

    public Label getLblHotelPhone() {
        return lblHotelPhone;
    }

    public void setLblHotelPhone(Label l) {
        this.lblHotelPhone = l;
    }

    private Label lblAddress = new Label();

    public Label getLblAddress() {
        return lblAddress;
    }

    public void setLblAddress(Label l) {
        this.lblAddress = l;
    }

    private Label lblHotelAddress = new Label();

    public Label getLblHotelAddress() {
        return lblHotelAddress;
    }

    public void setLblHotelAddress(Label l) {
        this.lblHotelAddress = l;
    }

    private Label lblStars2 = new Label();

    public Label getLblStars2() {
        return lblStars2;
    }

    public void setLblStars2(Label l) {
        this.lblStars2 = l;
    }

    private Label lblStars = new Label();

    public Label getLblStars() {
        return lblStars;
    }

    public void setLblStars(Label l) {
        this.lblStars = l;
    }

    private Label label39 = new Label();

    public Label getLabel39() {
        return label39;
    }

    public void setLabel39(Label l) {
        this.label39 = l;
    }

    private DropDown ddlRoomType = new DropDown();

    public DropDown getDdlRoomType() {
        return ddlRoomType;
    }

    public void setDdlRoomType(DropDown dd) {
        this.ddlRoomType = dd;
    }

    private Label lblPrice1 = new Label();

    public Label getLblPrice1() {
        return lblPrice1;
    }

    public void setLblPrice1(Label l) {
        this.lblPrice1 = l;
    }

    private Label lblPrice = new Label();

    public Label getLblPrice() {
        return lblPrice;
    }

    public void setLblPrice(Label l) {
        this.lblPrice = l;
    }

    private Calendar cldFromDate = new Calendar();

    public Calendar getCldFromDate() {
        return cldFromDate;
    }

    public void setCldFromDate(Calendar c) {
        this.cldFromDate = c;
    }

    private Calendar cldToDate = new Calendar();

    public Calendar getCldToDate() {
        return cldToDate;
    }

    public void setCldToDate(Calendar c) {
        this.cldToDate = c;
    }

    private Message message1 = new Message();

    public Message getMessage1() {
        return message1;
    }

    public void setMessage1(Message m) {
        this.message1 = m;
    }

    private Message message2 = new Message();

    public Message getMessage2() {
        return message2;
    }

    public void setMessage2(Message m) {
        this.message2 = m;
    }

    private Message message3 = new Message();

    public Message getMessage3() {
        return message3;
    }

    public void setMessage3(Message m) {
        this.message3 = m;
    }

    private Message message4 = new Message();

    public Message getMessage4() {
        return message4;
    }

    public void setMessage4(Message m) {
        this.message4 = m;
    }

    private Message message5 = new Message();

    public Message getMessage5() {
        return message5;
    }

    public void setMessage5(Message m) {
        this.message5 = m;
    }

    private StaticText staticText1 = new StaticText();

    public StaticText getStaticText1() {
        return staticText1;
    }

    public void setStaticText1(StaticText st) {
        this.staticText1 = st;
    }

    private StaticText staticText2 = new StaticText();

    public StaticText getStaticText2() {
        return staticText2;
    }

    public void setStaticText2(StaticText st) {
        this.staticText2 = st;
    }

    private StaticText staticText3 = new StaticText();

    public StaticText getStaticText3() {
        return staticText3;
    }

    public void setStaticText3(StaticText st) {
        this.staticText3 = st;
    }

    private StaticText staticText4 = new StaticText();

    public StaticText getStaticText4() {
        return staticText4;
    }

    public void setStaticText4(StaticText st) {
        this.staticText4 = st;
    }

    private StaticText staticText5 = new StaticText();

    public StaticText getStaticText5() {
        return staticText5;
    }

    public void setStaticText5(StaticText st) {
        this.staticText5 = st;
    }

    private StaticText staticText6 = new StaticText();

    public StaticText getStaticText6() {
        return staticText6;
    }

    public void setStaticText6(StaticText st) {
        this.staticText6 = st;
    }

    private StaticText staticText7 = new StaticText();

    public StaticText getStaticText7() {
        return staticText7;
    }

    public void setStaticText7(StaticText st) {
        this.staticText7 = st;
    }

    private StaticText staticText8 = new StaticText();

    public StaticText getStaticText8() {
        return staticText8;
    }

    public void setStaticText8(StaticText st) {
        this.staticText8 = st;
    }

    private StaticText staticText9 = new StaticText();

    public StaticText getStaticText9() {
        return staticText9;
    }

    public void setStaticText9(StaticText st) {
        this.staticText9 = st;
    }

    private Label lblRoom = new Label();

    public Label getLblRoom() {
        return lblRoom;
    }

    public void setLblRoom(Label l) {
        this.lblRoom = l;
    }

    private DropDown ddlRoom = new DropDown();

    public DropDown getDdlRoom() {
        return ddlRoom;
    }

    public void setDdlRoom(DropDown dd) {
        this.ddlRoom = dd;
    }

    private SingleSelectOptionsList ddlRoomDefaultOptions = new SingleSelectOptionsList();

    public SingleSelectOptionsList getDdlRoomDefaultOptions() {
        return ddlRoomDefaultOptions;
    }

    public void setDdlRoomDefaultOptions(SingleSelectOptionsList ssol) {
        this.ddlRoomDefaultOptions = ssol;
    }

    private Message message6 = new Message();

    public Message getMessage6() {
        return message6;
    }

    public void setMessage6(Message m) {
        this.message6 = m;
    }

    private StaticText staticText10 = new StaticText();

    public StaticText getStaticText10() {
        return staticText10;
    }

    public void setStaticText10(StaticText st) {
        this.staticText10 = st;
    }

    private StaticText staticText11 = new StaticText();

    public StaticText getStaticText11() {
        return staticText11;
    }

    public void setStaticText11(StaticText st) {
        this.staticText11 = st;
    }

    private StaticText staticText12 = new StaticText();

    public StaticText getStaticText12() {
        return staticText12;
    }

    public void setStaticText12(StaticText st) {
        this.staticText12 = st;
    }

    private StaticText staticText13 = new StaticText();

    public StaticText getStaticText13() {
        return staticText13;
    }

    public void setStaticText13(StaticText st) {
        this.staticText13 = st;
    }

    private StaticText staticText14 = new StaticText();

    public StaticText getStaticText14() {
        return staticText14;
    }

    public void setStaticText14(StaticText st) {
        this.staticText14 = st;
    }

    private StaticText staticText15 = new StaticText();

    public StaticText getStaticText15() {
        return staticText15;
    }

    public void setStaticText15(StaticText st) {
        this.staticText15 = st;
    }

    private StaticText staticText16 = new StaticText();

    public StaticText getStaticText16() {
        return staticText16;
    }

    public void setStaticText16(StaticText st) {
        this.staticText16 = st;
    }

    private StaticText staticText17 = new StaticText();

    public StaticText getStaticText17() {
        return staticText17;
    }

    public void setStaticText17(StaticText st) {
        this.staticText17 = st;
    }

    private StaticText staticText18 = new StaticText();

    public StaticText getStaticText18() {
        return staticText18;
    }

    public void setStaticText18(StaticText st) {
        this.staticText18 = st;
    }

    private Label lblOfferedPrice = new Label();

    public Label getLblOfferedPrice() {
        return lblOfferedPrice;
    }

    public void setLblOfferedPrice(Label l) {
        this.lblOfferedPrice = l;
    }

    private TextField txbOfferedPrice = new TextField();

    public TextField getTxbOfferedPrice() {
        return txbOfferedPrice;
    }

    public void setTxbOfferedPrice(TextField tf) {
        this.txbOfferedPrice = tf;
    }

    private Message msgTxbOfferedPrice = new Message();

    public Message getMsgTxbOfferedPrice() {
        return msgTxbOfferedPrice;
    }

    public void setMsgTxbOfferedPrice(Message m) {
        this.msgTxbOfferedPrice = m;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public ModifyPrebooking() {
    }

    private void _Init() {
        try {
            this.setRequiredFields(true);
            List<Participant> participants = userManager.getAllParticipants();
            Collection participantItems = new ArrayList();
            if (participants != null) {
                for (Participant p : participants) {
                    Option o = new Option();
                    o.setLabel(p.getSsoUser().getPersonalData().getFirstName() + " " + p.getSsoUser().getPersonalData().getLastName());
                    o.setValue(p.getId().toString());
                    participantItems.add(o);
                }
                ddlName.setItems(participantItems);
            }
            List<Hotel> hotels = hotelManager.getDeletableHotels();
            Collection hotelItems = new ArrayList();
            if (hotels != null) {
                for (Hotel hotel : hotels) {
                    Option o = new Option();
                    o.setLabel(hotel.getName());
                    o.setValue(hotel.getId().toString());
                    hotelItems.add(o);
                }
                ddlHotelName.setItems(hotelItems);
            }
            List<RoomType> roomtypes = hotelManager.getRoomTypes();
            Collection roomtypeItems = new ArrayList();
            if (roomtypes != null) {
                for (RoomType rt : roomtypes) {
                    Option o = new Option();
                    o.setLabel(rt.getName());
                    o.setValue(rt.getId().toString());
                    roomtypeItems.add(o);
                }
                ddlRoomType.setItems(roomtypes);
            }
            List<HotelRoom> rooms = hotelManager.getHotelRoomList();
            Collection roomlist = new ArrayList();
            if (rooms != null) {
                for (HotelRoom r : rooms) {
                    Option o = new Option();
                    String name = "";
                    for (Participant p : r.getParticipant()) {
                        name += p.getSsoUser().getPersonalData().getFirstName() + p.getSsoUser().getPersonalData().getLastName();
                    }
                    o.setLabel(name);
                    o.setValue(r.getId().toString());
                    roomlist.add(o);
                }
                ddlRoom.setItems(roomlist);
            }
            this.lblPrice.setText("5000 EUR");
        } catch (DatabaseException ex) {
            Logger.getLogger(ModifyPrebooking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @EJB
    private BookingManagerLocal bookingManager;

    @EJB
    private HotelManagementLocal hotelManager;

    @EJB
    private UserManagementLocal userManager;

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
            log("nAddPreBooking Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }
        this._Init();
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

    public String btnModify_action() {
        boolean success = true;
        long pId = Long.parseLong(ddlName.getSelected().toString());
        long roomId = Long.parseLong(ddlRoom.getSelected().toString());
        long roomtypeId = Long.parseLong(ddlRoomType.getSelected().toString());
        long hotelId = Long.parseLong(ddlHotelName.getSelected().toString());
        try {
            getComputedPrice();
            if (txbOfferedPrice.toString() != null) bookingManager.ModifyBooking(pId, hotelId, roomtypeId, roomId, this.cldToDate.getSelectedDate(), this.cldFromDate.getSelectedDate(), Double.parseDouble(txbOfferedPrice.toString())); else bookingManager.ModifyBooking(pId, hotelId, roomtypeId, roomId, this.cldToDate.getSelectedDate(), this.cldFromDate.getSelectedDate(), getComputedPrice());
        } catch (DatabaseException dbEx) {
            success = false;
            List<UIComponent> components = errorPanel1.getChildren();
            Label dbErrLbl = new Label();
            dbErrLbl.setId("dbErrorLabel");
            components.add(dbErrLbl);
            log("Database error while modifying Hotel.", dbEx);
        } catch (Exception e) {
            success = false;
        }
        if (success) {
            return "modifyBookingSuccess";
        }
        return "modifyBookingFailure";
    }

    public void ddlName_processValueChange(ValueChangeEvent event) {
        try {
            Long newId = Long.parseLong(event.getNewValue().toString());
            lblUserMail.setText(userManager.getParticipantById(newId).getSsoUser().getEmail());
            lblUserAddress.setText(userManager.getParticipantById(newId).getSsoUser().getPersonalData().getAddress());
            lblUserCity.setText(userManager.getParticipantById(newId).getSsoUser().getPersonalData().getCity());
            lblUserPhone.setText(userManager.getParticipantById(newId).getSsoUser().getPersonalData().getPhone());
            this.setRequiredFields(false);
        } catch (DatabaseException ex) {
            Logger.getLogger(ModifyPrebooking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cldFromDate_processValueChange(ValueChangeEvent event) {
        cldToDate.setMinDate((Date) event.getNewValue());
        this.setRequiredFields(false);
    }

    public void ddlHotelName_processValueChange(ValueChangeEvent event) {
        Long newId = Long.parseLong(event.getNewValue().toString());
        lblHotelAddress.setText(hotelManager.getHotelById(newId).getAddress());
        lblHotelMail.setText(hotelManager.getHotelById(newId).getMail());
        lblHotelPhone.setText(hotelManager.getHotelById(newId).getPhone());
        List<RoomType> roomtypes = hotelManager.getRoomTypeByHotelId(newId);
        if (roomtypes != null) {
            ddlRoomType.setItems(CreateRoomTypeOptions(roomtypes));
            RefreshRoomList(Long.parseLong(ddlRoomType.getSelected().toString()), newId);
        }
        this.setRequiredFields(false);
    }

    public void ddlRoomType_processValueChange(ValueChangeEvent event) {
        long newId = Long.parseLong(event.getNewValue().toString());
        List<Hotel> hotels = hotelManager.getHotelsByRoomType(newId);
        if (hotels != null) {
            RefreshRoomList(newId, Long.parseLong(ddlHotelName.getSelected().toString()));
        }
        this.setRequiredFields(false);
    }

    private void RefreshRoomList(long roomtypeid, long hotelid) {
        List<HotelRoom> rooms = hotelManager.getAvaliableRoomsByHotelByRoomType(roomtypeid, hotelid);
        if (rooms != null) {
            this.CreateHotelRoomOptions(rooms);
            this.ddlRoom.setItems(rooms);
        }
    }

    public void setRequiredFields(boolean flag) {
        this.ddlHotelName.setRequired(flag);
        this.ddlName.setRequired(flag);
        this.ddlRoom.setRequired(flag);
        this.ddlRoomType.setRequired(flag);
        this.cldFromDate.setRequired(flag);
        this.cldToDate.setRequired(flag);
    }

    private double getComputedPrice() throws DatabaseException {
        try {
            double sumPrice = hotelManager.getHotelById(Long.parseLong(ddlHotelName.getSelected().toString())).getStars() * 10;
            sumPrice -= hotelManager.getRoomById(Long.parseLong(ddlRoom.getSelected().toString())).getBeds() * 10;
            if (hotelManager.getHotelById(Long.parseLong(ddlHotelName.getSelected().toString())).isBar()) {
                sumPrice += 5;
            }
            if (hotelManager.getRoomTypeById(Long.parseLong(ddlName.getSelected().toString())).isBarInRoom()) {
                sumPrice += 5;
            }
            if (hotelManager.getRoomTypeById(Long.parseLong(ddlName.getSelected().toString())).isInternet()) {
                sumPrice += 5;
            }
            Date t1 = userManager.getParticipantById(Long.parseLong(ddlName.getSelected().toString())).getCurrentBooking().getFromDate();
            Date t2 = userManager.getParticipantById(Long.parseLong(ddlName.getSelected().toString())).getCurrentBooking().getToDate();
            java.util.Calendar tFrom = java.util.Calendar.getInstance();
            java.util.Calendar tTo = java.util.Calendar.getInstance();
            tTo.setTime(t2);
            tFrom.setTime(t1);
            int days = Math.round((tTo.getTimeInMillis() - tFrom.getTimeInMillis()) / 86400000);
            lblPrice.setText(sumPrice * days);
            return sumPrice * days;
        } catch (Exception e) {
            throw new DatabaseException("Gebasz az árszámításnál", e);
        }
    }
}
