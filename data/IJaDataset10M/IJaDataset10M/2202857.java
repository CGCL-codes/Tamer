package vydavkyaswar.objekty;

import com.sun.data.provider.RowKey;
import com.sun.data.provider.impl.ObjectListDataProvider;
import com.sun.data.provider.impl.TableRowDataProvider;
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.Checkbox;
import com.sun.webui.jsf.component.DropDown;
import com.sun.webui.jsf.component.Form;
import com.sun.webui.jsf.component.MessageGroup;
import com.sun.webui.jsf.component.PanelGroup;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.Table;
import com.sun.webui.jsf.component.TableColumn;
import com.sun.webui.jsf.component.TableRowGroup;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.NumberConverter;
import javax.faces.event.ValueChangeEvent;
import vydavky.client.ciselniky.Ciselniky;
import vydavky.client.ciselniky.TypNavratu;
import vydavky.client.ciselniky.TypObjektu;
import vydavky.client.ciselniky.TypSkupiny;
import vydavky.client.objects.ClovekValue;
import vydavky.client.objects.KoeficientVydavku;
import vydavky.client.objects.ProjektValue;
import vydavky.client.objects.SkupinaValue;
import vydavky.client.objects.TransakciaValue;
import vydavky.client.objects.clientserver.NavratValue;
import vydavky.client.utils.CNBHelper;
import vydavky.client.utils.ClientUtils;
import vydavkyaswar.SessionBean;
import vydavkyaswar.utils.BaseValBoolFloatTriple;
import vydavkyaswar.utils.WebUtils;

/**
 * Stranka pre editaciu transakcii.
 */
public class Transakcie extends AbstractPageBean {

    /**
   * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
   * This method is automatically generated, so any user-specified code inserted
   * here is subject to being replaced.</p>
   */
    private void _init() throws Exception {
        dataProviderTransakcie.setList(new ArrayList<TransakciaValue>(getSessionBean().getCSR().getTransakcie(null).values()));
        dataProviderTransakcie.setObjectType(TransakciaValue.class);
        dateTimeConverter.setTimeZone(java.util.TimeZone.getTimeZone("Europe/Prague"));
        dateTimeConverter.setPattern("d.M.yyyy");
        numberConverter.setMinIntegerDigits(1);
        numberConverter.setMaxIntegerDigits(40);
        numberConverter.setMaxFractionDigits(3);
        numberConverter.setGroupingUsed(false);
    }

    private Form form1 = new Form();

    public Form getForm1() {
        return form1;
    }

    public void setForm1(final Form f) {
        this.form1 = f;
    }

    private MessageGroup messageGroup1 = new MessageGroup();

    public MessageGroup getMessageGroup1() {
        return messageGroup1;
    }

    public void setMessageGroup1(final MessageGroup mg) {
        this.messageGroup1 = mg;
    }

    private ObjectListDataProvider dataProviderTransakcie = new ObjectListDataProvider();

    public ObjectListDataProvider getdataProviderTransakcie() {
        return dataProviderTransakcie;
    }

    public void setdataProviderTransakcie(final ObjectListDataProvider oldp) {
        this.dataProviderTransakcie = oldp;
    }

    private Table table_Transakcie = new Table();

    public Table getTable_Transakcie() {
        return table_Transakcie;
    }

    public void setTable_Transakcie(final Table t) {
        this.table_Transakcie = t;
    }

    private TableRowGroup tableRowGroup1 = new TableRowGroup();

    public TableRowGroup getTableRowGroup1() {
        return tableRowGroup1;
    }

    public void setTableRowGroup1(final TableRowGroup trg) {
        this.tableRowGroup1 = trg;
    }

    private TableColumn tableColumn1 = new TableColumn();

    public TableColumn getTableColumn1() {
        return tableColumn1;
    }

    public void setTableColumn1(final TableColumn tc) {
        this.tableColumn1 = tc;
    }

    private StaticText staticText1 = new StaticText();

    public StaticText getStaticText1() {
        return staticText1;
    }

    public void setStaticText1(final StaticText st) {
        this.staticText1 = st;
    }

    private TableColumn tableColumnAkcie = new TableColumn();

    public TableColumn getTableColumnAkcie() {
        return tableColumnAkcie;
    }

    public void setTableColumnAkcie(final TableColumn tc) {
        this.tableColumnAkcie = tc;
    }

    private PanelGroup groupPanel1 = new PanelGroup();

    public PanelGroup getGroupPanel1() {
        return groupPanel1;
    }

    public void setGroupPanel1(final PanelGroup pg) {
        this.groupPanel1 = pg;
    }

    private Button btnEdit = new Button();

    public Button getBtnEdit() {
        return btnEdit;
    }

    public void setBtnEdit(final Button b) {
        this.btnEdit = b;
    }

    private Button btnDel = new Button();

    public Button getBtnDel() {
        return btnDel;
    }

    public void setBtnDel(final Button b) {
        this.btnDel = b;
    }

    private Button btnNova = new Button();

    public Button getBtnNova() {
        return btnNova;
    }

    public void setBtnNova(final Button b) {
        this.btnNova = b;
    }

    private Form form2 = new Form();

    public Form getForm2() {
        return form2;
    }

    public void setForm2(final Form f) {
        this.form2 = f;
    }

    private StaticText staticText2 = new StaticText();

    public StaticText getStaticText2() {
        return staticText2;
    }

    public void setStaticText2(final StaticText st) {
        this.staticText2 = st;
    }

    private DropDown ddProjekt = new DropDown();

    public DropDown getDdProjekt() {
        return ddProjekt;
    }

    public void setDdProjekt(final DropDown dd) {
        this.ddProjekt = dd;
    }

    private Checkbox cbPlatil = new Checkbox();

    public Checkbox getCbPlatil() {
        return cbPlatil;
    }

    public void setCbPlatil(final Checkbox c) {
        this.cbPlatil = c;
    }

    private DateTimeConverter dateTimeConverter = new DateTimeConverter();

    public DateTimeConverter getDateTimeConverter() {
        return dateTimeConverter;
    }

    public void setDateTimeConverter(final DateTimeConverter dtc) {
        this.dateTimeConverter = dtc;
    }

    private NumberConverter numberConverter = new NumberConverter();

    public NumberConverter getNumberConverter() {
        return numberConverter;
    }

    public void setNumberConverter(final NumberConverter nc) {
        this.numberConverter = nc;
    }

    private final Map<Long, ProjektValue> projektyCache;

    private final Map<Long, ClovekValue> ludiaCache;

    private final Map<Long, SkupinaValue> skupinyCache;

    private final Map<Long, TransakciaValue> transakcieCache;

    /**
   * Defaultny konstruktor.
   */
    public Transakcie() {
        super();
        this.projektyCache = getSessionBean().getCSR().getProjekty();
        this.ludiaCache = getSessionBean().getCSR().getLudia();
        this.skupinyCache = getSessionBean().getCSR().getSkupiny();
        this.transakcieCache = getSessionBean().getCSR().getTransakcie(null);
    }

    @Override
    public void init() {
        super.init();
        try {
            _init();
        } catch (Exception e) {
            log("Transakcie Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }
    }

    @Override
    public void preprocess() {
    }

    @Override
    public void prerender() {
    }

    @Override
    public void destroy() {
    }

    protected SessionBean getSessionBean() {
        return (SessionBean) getBean(WebUtils.SESSION_BEAN);
    }

    public String btnEdit_action() {
        final TransakciaValue transakcia = getSelectedTransakcia();
        final NavratValue navrat = getSessionBean().getCSR().prizamkniObjekt(transakcia.getId().longValue());
        if (navrat.isOk()) {
            getSessionBean().setAktivnaTransakcia(transakcia);
            getSessionBean().setTransakciaPlatili(nacitajZoznamUcastnikov(transakcia.getPlatili(), projektyCache.get(transakcia.getProjekt()), true));
            getSessionBean().setTransakciaSpotrebovali(nacitajZoznamUcastnikov(transakcia.getSpotrebovali(), projektyCache.get(transakcia.getProjekt()), false));
            return null;
        }
        error(navrat.getText());
        return null;
    }

    public String btnDel_action() {
        final NavratValue navratStorno = getSessionBean().getCSR().stornoTransakcie(getSelectedTransakcia());
        if (navratStorno.isOk()) {
            return allOk();
        }
        if (navratStorno.getTyp().equals(TypNavratu.POUZITE)) {
            error("Transakciu nie je možné stornovať, používa sa. Odstráňte z nej najprv všetkých ľudí a skupiny, a pokúste sa ju stornovať znovu.");
            return null;
        }
        error(navratStorno.getText());
        return null;
    }

    public String btnOk_action() {
        if (getSessionBean().getAktivnaTransakcia() == null) {
            error("Neexistuje editovana transakcia");
            return null;
        }
        if (getSessionBean().getAktivnaTransakcia().getId() == null) {
            final TransakciaValue transakciaNew = getSessionBean().getAktivnaTransakcia();
            transakciaNew.getPlatili().clear();
            transakciaNew.getSpotrebovali().clear();
            presypDoZoznamu(getSessionBean().getTransakciaPlatili(), transakciaNew.getPlatili());
            presypDoZoznamu(getSessionBean().getTransakciaSpotrebovali(), transakciaNew.getSpotrebovali());
            final NavratValue navrat = getSessionBean().getCSR().ulozTransakciuNew(transakciaNew);
            if (navrat.isOk()) {
                return allOk();
            }
            error(navrat.getText());
            return null;
        }
        final TransakciaValue transakcia = getSessionBean().getAktivnaTransakcia();
        transakcia.getPlatili().clear();
        transakcia.getSpotrebovali().clear();
        presypDoZoznamu(getSessionBean().getTransakciaPlatili(), transakcia.getPlatili());
        presypDoZoznamu(getSessionBean().getTransakciaSpotrebovali(), transakcia.getSpotrebovali());
        NavratValue navrat = getSessionBean().getCSR().ulozTransakciu(transakcia);
        if (navrat.isOk()) {
            navrat = getSessionBean().getCSR().odomkniObjekt(transakcia.getId().longValue());
            if (navrat.isOk()) {
                return allOk();
            }
        }
        error(navrat.getText());
        return null;
    }

    private String allOk() {
        getSessionBean().setAktivnaTransakcia(null);
        getSessionBean().setPotvrdenieJumpTarget(WebUtils.NAV_TRANSAKCIE);
        return WebUtils.NAV_OK;
    }

    public String btnCancel_action() {
        if (getSessionBean().getAktivnaTransakcia() == null || getSessionBean().getAktivnaTransakcia().getId() == null) {
            getSessionBean().setAktivnaTransakcia(null);
            WebUtils.redirectTo("Transakcie.jsp");
            return null;
        }
        final NavratValue navratOdomknutie = getSessionBean().getCSR().odomkniObjekt(getSessionBean().getAktivnaTransakcia().getId().longValue());
        if (navratOdomknutie.isOk()) {
            getSessionBean().setAktivnaTransakcia(null);
            getSessionBean().setTransakciaPlatili(new ArrayList<BaseValBoolFloatTriple>());
            getSessionBean().setTransakciaSpotrebovali(new ArrayList<BaseValBoolFloatTriple>());
            WebUtils.redirectTo("Transakcie.jsp");
            return null;
        }
        error(navratOdomknutie.getText());
        return null;
    }

    public String btnNova_action() {
        final TransakciaValue transakciaNew = new TransakciaValue();
        transakciaNew.setDatum(new Date());
        transakciaNew.setKurz(1.0f);
        transakciaNew.setSuma(100.0f);
        getSessionBean().setAktivnaTransakcia(transakciaNew);
        getSessionBean().setTransakciaPlatili(nacitajZoznamUcastnikov(null, projektyCache.get(getSessionBean().getProjekty().get(0).getValue()), true));
        getSessionBean().setTransakciaSpotrebovali(nacitajZoznamUcastnikov(null, projektyCache.get(getSessionBean().getProjekty().get(0).getValue()), false));
        return null;
    }

    public void ddProjekt_processValueChange(final ValueChangeEvent event) {
        final List<KoeficientVydavku> zoznamPlatcov = getSessionBean().getAktivnaTransakcia() != null ? getSessionBean().getAktivnaTransakcia().getPlatili() : null;
        final List<KoeficientVydavku> zoznamSpotrebitelov = getSessionBean().getAktivnaTransakcia() != null ? getSessionBean().getAktivnaTransakcia().getSpotrebovali() : null;
        getSessionBean().setTransakciaPlatili(nacitajZoznamUcastnikov(zoznamPlatcov, projektyCache.get((Long) ddProjekt.getSelected()), true));
        getSessionBean().setTransakciaSpotrebovali(nacitajZoznamUcastnikov(zoznamSpotrebitelov, projektyCache.get((Long) ddProjekt.getSelected()), false));
        if (getSessionBean().getAktivnaTransakcia() != null) {
            getSessionBean().getAktivnaTransakcia().setProjekt((Long) ddProjekt.getSelected());
        }
    }

    public String btnCnb_action() {
        if (getSessionBean().getAktivnaTransakcia() == null) {
            return null;
        }
        final String mena = getSessionBean().getCSR().getMeny().get(getSessionBean().getAktivnaTransakcia().getMena()).getText();
        final Float kurz = CNBHelper.getKurzMeny(mena);
        if (kurz == null) {
            return null;
        }
        getSessionBean().getAktivnaTransakcia().setKurz(kurz.floatValue());
        return null;
    }

    public String getDatumFormatted() {
        final TableRowDataProvider rowData = (TableRowDataProvider) getBean(WebUtils.CURRENT_ROW);
        if (rowData == null) {
            return "";
        }
        final Date datum = (Date) rowData.getValue("datum");
        return ClientUtils.getDatumString(datum);
    }

    public String getSumaFormatted() {
        final TableRowDataProvider rowData = (TableRowDataProvider) getBean(WebUtils.CURRENT_ROW);
        if (rowData == null) {
            return "";
        }
        final Float suma = (Float) rowData.getValue("suma");
        final Long menaId = (Long) rowData.getValue("mena");
        return (new DecimalFormat(Ciselniky.FORMAT_MENA)).format(suma) + " " + getSessionBean().getCSR().getMeny().get(menaId);
    }

    public String getNazovProjektu() {
        final TableRowDataProvider rowData = (TableRowDataProvider) getBean(WebUtils.CURRENT_ROW);
        if (rowData == null) {
            return "";
        }
        final Long projektId = (Long) rowData.getValue("projekt");
        return projektyCache.get(projektId).getMeno();
    }

    public String getKoeficientMeno(final KoeficientVydavku koef) {
        if (koef.getClovek() != null && ludiaCache.get(koef.getClovek()) != null) {
            return ludiaCache.get(koef.getClovek()).getCeleMeno();
        }
        if (koef.getSkupina() != null && skupinyCache.get(koef.getSkupina()) != null) {
            return skupinyCache.get(koef.getSkupina()).getMeno();
        }
        return null;
    }

    public String getVypisPlatcovPrijemcov(final List<KoeficientVydavku> platciPrijemci) {
        final StringBuffer ret = new StringBuffer();
        boolean dalsi = false;
        for (KoeficientVydavku kv : platciPrijemci) {
            if (getKoeficientMeno(kv) != null) {
                ret.append(dalsi ? ", " : "");
                ret.append(getKoeficientMeno(kv));
                dalsi = true;
            }
        }
        return ret.toString();
    }

    public String getStringPlatili() {
        final TableRowDataProvider rowData = (TableRowDataProvider) getBean(WebUtils.CURRENT_ROW);
        if (rowData == null) {
            return "";
        }
        final Long transakciaId = (Long) rowData.getValue("id");
        return getVypisPlatcovPrijemcov(transakcieCache.get(transakciaId).getPlatili());
    }

    public String getStringSpotrebovali() {
        final TableRowDataProvider rowData = (TableRowDataProvider) getBean(WebUtils.CURRENT_ROW);
        if (rowData == null) {
            return "";
        }
        final Long transakciaId = (Long) rowData.getValue("id");
        return getVypisPlatcovPrijemcov(transakcieCache.get(transakciaId).getSpotrebovali());
    }

    private List<BaseValBoolFloatTriple> nacitajZoznamUcastnikov(final List<KoeficientVydavku> ucastnici, final ProjektValue projekt, final boolean platcovia) {
        final List<ClovekValue> ludia = new LinkedList<ClovekValue>(ludiaCache.values());
        final List<SkupinaValue> skupiny = new LinkedList<SkupinaValue>(skupinyCache.values());
        final List<BaseValBoolFloatTriple> ret = new ArrayList<BaseValBoolFloatTriple>();
        for (ClovekValue clovek : ludia) {
            if (!projekt.getLudia().contains(clovek.getId())) {
                continue;
            }
            if (ucastnici == null) {
                ret.add(new BaseValBoolFloatTriple(clovek, false, 1f));
            } else {
                KoeficientVydavku platil = null;
                for (KoeficientVydavku koef : ucastnici) {
                    if (koef.getClovek() != null && koef.getClovek().equals(clovek.getId())) {
                        platil = koef;
                        break;
                    }
                }
                ret.add(new BaseValBoolFloatTriple(clovek, platil != null, platil != null ? platil.getKoeficient() : 1f));
            }
        }
        for (SkupinaValue skupina : skupiny) {
            if (!projekt.getSkupiny().contains(skupina.getId())) {
                continue;
            }
            if (ucastnici == null && (skupina.getTyp() == TypSkupiny.OBOJE || platcovia == (skupina.getTyp() == TypSkupiny.PLATOBNA))) {
                ret.add(new BaseValBoolFloatTriple(skupina, false, 1f));
            } else {
                KoeficientVydavku platil = null;
                for (KoeficientVydavku koef : ucastnici) {
                    if (koef.getSkupina() != null && koef.getSkupina().equals(skupina.getId())) {
                        platil = koef;
                        break;
                    }
                }
                if (skupina.getTyp() == TypSkupiny.OBOJE || platcovia == (skupina.getTyp() == TypSkupiny.PLATOBNA)) {
                    ret.add(new BaseValBoolFloatTriple(skupina, platil != null, platil != null ? platil.getKoeficient() : 1f));
                }
            }
        }
        return ret;
    }

    private void presypDoZoznamu(final List<BaseValBoolFloatTriple> odkial, final List<KoeficientVydavku> kam) {
        for (BaseValBoolFloatTriple bvbt : odkial) {
            if (bvbt.isSelected()) {
                if (bvbt.getBaseValue().getTypObjektu() == TypObjektu.CLOVEK) {
                    kam.add(new KoeficientVydavku(bvbt.getPomer(), bvbt.getBaseValue().getId(), null));
                } else if (bvbt.getBaseValue().getTypObjektu() == TypObjektu.SKUPINA) {
                    kam.add(new KoeficientVydavku(bvbt.getPomer(), null, bvbt.getBaseValue().getId()));
                } else {
                    throw new IllegalArgumentException("Do zoznamu sa dostal objekt, co nie je clovek ani skupina");
                }
            }
        }
    }

    private TransakciaValue getSelectedTransakcia() {
        final RowKey rk = tableRowGroup1.getRowKey();
        final Object o = dataProviderTransakcie.getObject(rk);
        if (!(o instanceof TransakciaValue)) {
            throw new IllegalArgumentException("Do zoznamu transakcii sa dostal objekt, ktory nie je TransakciaValue");
        }
        return (TransakciaValue) o;
    }

    public String getTransakciaPopis() {
        if (getSessionBean().getAktivnaTransakcia() == null) {
            return "";
        }
        return getSessionBean().getAktivnaTransakcia().getPopis();
    }

    public void setTransakciaPopis(final String transakciaPopis) {
        if (getSessionBean().getAktivnaTransakcia() != null) {
            getSessionBean().getAktivnaTransakcia().setPopis(transakciaPopis);
        }
    }

    public Date getTransakciaDatum() {
        if (getSessionBean().getAktivnaTransakcia() == null) {
            return null;
        }
        return getSessionBean().getAktivnaTransakcia().getDatum();
    }

    public void setTransakciaDatum(final Date transakciaDatum) {
        if (getSessionBean().getAktivnaTransakcia() != null) {
            getSessionBean().getAktivnaTransakcia().setDatum(transakciaDatum);
        }
    }

    public Number getTransakciaSuma() {
        if (getSessionBean().getAktivnaTransakcia() == null) {
            return null;
        }
        return BigDecimal.valueOf(getSessionBean().getAktivnaTransakcia().getSuma());
    }

    public void setTransakciaSuma(final Number transakciaSuma) {
        if (getSessionBean().getAktivnaTransakcia() != null) {
            getSessionBean().getAktivnaTransakcia().setSuma(transakciaSuma.floatValue());
        }
    }

    public Number getTransakciaKurz() {
        if (getSessionBean().getAktivnaTransakcia() == null) {
            return null;
        }
        return BigDecimal.valueOf(getSessionBean().getAktivnaTransakcia().getKurz());
    }

    public void setTransakciaKurz(final Number transakciaKurz) {
        if (getSessionBean().getAktivnaTransakcia() != null) {
            getSessionBean().getAktivnaTransakcia().setKurz(transakciaKurz.floatValue());
        }
    }

    public Long getTransakciaMena() {
        if (getSessionBean().getAktivnaTransakcia() == null) {
            return null;
        }
        return getSessionBean().getAktivnaTransakcia().getMena();
    }

    public void setTransakciaMena(final Long transakciaMena) {
        if (getSessionBean().getAktivnaTransakcia() != null) {
            getSessionBean().getAktivnaTransakcia().setMena(transakciaMena);
        }
    }

    public Long getTransakciaTypVydavku() {
        if (getSessionBean().getAktivnaTransakcia() == null) {
            return null;
        }
        return getSessionBean().getAktivnaTransakcia().getTypVydavku();
    }

    public void setTransakciaTypVydavku(final Long transakciaTypVydavku) {
        if (getSessionBean().getAktivnaTransakcia() != null) {
            getSessionBean().getAktivnaTransakcia().setTypVydavku(transakciaTypVydavku);
        }
    }

    public Long getTransakciaProjekt() {
        if (getSessionBean().getAktivnaTransakcia() == null) {
            return null;
        }
        return getSessionBean().getAktivnaTransakcia().getProjekt();
    }

    public void setTransakciaProjekt(final Long transakciaProjekt) {
        if (getSessionBean().getAktivnaTransakcia() != null) {
            getSessionBean().getAktivnaTransakcia().setProjekt(transakciaProjekt);
        }
    }

    public boolean isEditacia() {
        return getSessionBean().getAktivnaTransakcia() != null;
    }
}
