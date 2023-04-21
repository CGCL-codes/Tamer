package org.isurf.cpfr.cpfr;

import java.io.Serializable;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import oasis.names.specification.ubl.schema.xsd.exceptioncriteria_2.ExceptionCriteriaType;
import oasis.names.specification.ubl.schema.xsd.exceptionnotification_2.ExceptionNotificationType;
import oasis.names.specification.ubl.schema.xsd.forecast_2.ForecastType;
import oasis.names.specification.ubl.schema.xsd.productactivity_2.ProductActivityType;
import oasis.names.specification.ubl.schema.xsd.productionplan_2.ProductionPlanType;
import oasis.names.specification.ubl.schema.xsd.purchaseconditions_2.PurchaseConditionsType;
import oasis.names.specification.ubl.schema.xsd.tradeitemlocationprofile_2.TradeItemLocationProfileType;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.isurf.cpfr.CPFRWS;
import org.isurf.cpfr.CPFRWSService;
import org.isurf.cpfr.templates.TemplateCPFR;

@SuppressWarnings("unchecked")
public class ShowAllEntities extends TemplateCPFR {

    private final List entryList = new Vector<Entity>();

    private final ListView entryListView;

    @SuppressWarnings("serial")
    public ShowAllEntities(PageParameters params) {
        super(params);
        add(new ChooseForm("choose"));
        add(entryListView = new ListView("entities", entryList) {

            @Override
            protected void populateItem(final ListItem listItem) {
                final Entity entry = (Entity) listItem.getModelObject();
                listItem.add(new Label("label", entry.getFriendlyName()));
                listItem.add(new MultiLineLabel("message", entry.getMessage()));
            }
        });
    }

    @Override
    public String getPageTitle() {
        return "Show All Entities";
    }

    class ChooseForm extends Form {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        private final String PURCHASE_CONDITION = "Purchase Condition";

        private final String EXCEPTION_CRITERIA = "Exception Criteria";

        private final String TRADE_ITEM_LOCATION_PROFILE = "Trade Item Location Profile";

        private final String PRODUCT_ACTIVITY = "Product Activity";

        private final String FORECAST = "Forecast";

        private final String EXCEPTION_NOTIFICATION = "Exception Notification";

        private final String PRODUCTION_PLAN = "Production Plan";

        private List<String> choices;

        private DropDownChoice<String> ddcMessageType;

        public ChooseForm(String id) {
            super(id);
            this.choices = new ArrayList<String>();
            this.choices.add(0, PURCHASE_CONDITION);
            this.choices.add(1, EXCEPTION_CRITERIA);
            this.choices.add(2, TRADE_ITEM_LOCATION_PROFILE);
            this.choices.add(3, PRODUCT_ACTIVITY);
            this.choices.add(4, FORECAST);
            this.choices.add(5, EXCEPTION_NOTIFICATION);
            this.choices.add(6, PRODUCTION_PLAN);
            ddcMessageType = new DropDownChoice<String>("choices", new Model(), this.choices);
            add(ddcMessageType);
        }

        @Override
        protected void onSubmit() {
            entryList.clear();
            final String CHOICE = ddcMessageType.getModelObject();
            final String DEFAULT_CHOICE = "";
            try {
                CPFRWSService service = new CPFRWSService(new URL("http://localhost:8080/CPFR-CPFRWSEJB/DBManager?wsdl"), new QName("http://cpfr.isurf.org/", "CPFRWSService"));
                CPFRWS port = service.getCPFRWSPort();
                if (CHOICE.equals(DEFAULT_CHOICE)) {
                } else if (CHOICE.equals(PURCHASE_CONDITION)) {
                    List<PurchaseConditionsType> purchaseConditions = port.getAllPurchaseCondition();
                    JAXBContext jc = JAXBContext.newInstance("oasis.names.specification.ubl.schema.xsd.purchaseconditions_2");
                    Marshaller m = jc.createMarshaller();
                    for (int i = 0; i < purchaseConditions.size(); i++) {
                        PurchaseConditionsType p = purchaseConditions.get(i);
                        StringWriter sw = new StringWriter();
                        m.marshal(p, sw);
                        entryList.add(new Entity("PurchaseCondition " + (i + 1), sw.toString()));
                    }
                } else if (CHOICE.equals(EXCEPTION_CRITERIA)) {
                    List<ExceptionCriteriaType> exceptionCriterias = port.getAllExceptionCriteria();
                    JAXBContext jc = JAXBContext.newInstance("oasis.names.specification.ubl.schema.xsd.exceptioncriteria_2");
                    Marshaller m = jc.createMarshaller();
                    for (int i = 0; i < exceptionCriterias.size(); i++) {
                        ExceptionCriteriaType e = exceptionCriterias.get(i);
                        StringWriter sw = new StringWriter();
                        m.marshal(e, sw);
                        entryList.add(new Entity("ExceptionCriteria " + (i + 1), sw.toString()));
                    }
                } else if (CHOICE.equals(TRADE_ITEM_LOCATION_PROFILE)) {
                    List<TradeItemLocationProfileType> tilps = port.getAllTILP();
                    JAXBContext jc = JAXBContext.newInstance("oasis.names.specification.ubl.schema.xsd.tradeitemlocationprofile_2");
                    Marshaller m = jc.createMarshaller();
                    for (int i = 0; i < tilps.size(); i++) {
                        TradeItemLocationProfileType t = tilps.get(i);
                        StringWriter sw = new StringWriter();
                        m.marshal(t, sw);
                        entryList.add(new Entity("TradeItemLocationProfile " + (i + 1), sw.toString()));
                    }
                } else if (CHOICE.equals(PRODUCT_ACTIVITY)) {
                    List<ProductActivityType> productActivities = port.getAllProductActivity();
                    JAXBContext jc = JAXBContext.newInstance("oasis.names.specification.ubl.schema.xsd.productactivity_2");
                    Marshaller m = jc.createMarshaller();
                    for (int i = 0; i < productActivities.size(); i++) {
                        ProductActivityType p = productActivities.get(i);
                        StringWriter sw = new StringWriter();
                        m.marshal(p, sw);
                        entryList.add(new Entity("ProductActivity " + (i + 1), sw.toString()));
                    }
                } else if (CHOICE.equals(FORECAST)) {
                    List<ForecastType> forecasts = port.getAllForecast();
                    JAXBContext jc = JAXBContext.newInstance("oasis.names.specification.ubl.schema.xsd.forecast_2");
                    Marshaller m = jc.createMarshaller();
                    for (int i = 0; i < forecasts.size(); i++) {
                        ForecastType f = forecasts.get(i);
                        StringWriter sw = new StringWriter();
                        m.marshal(f, sw);
                        entryList.add(new Entity("Forecast " + (i + 1), sw.toString()));
                    }
                } else if (CHOICE.equals(EXCEPTION_NOTIFICATION)) {
                    List<ExceptionNotificationType> exceptionNotifications = port.getAllExceptionNotification();
                    JAXBContext jc = JAXBContext.newInstance("oasis.names.specification.ubl.schema.xsd.exceptionnotification_2");
                    Marshaller m = jc.createMarshaller();
                    for (int i = 0; i < exceptionNotifications.size(); i++) {
                        ExceptionNotificationType e = exceptionNotifications.get(i);
                        StringWriter sw = new StringWriter();
                        m.marshal(e, sw);
                        entryList.add(new Entity("ExceptionNotification " + (i + 1), sw.toString()));
                    }
                } else if (CHOICE.equals(PRODUCTION_PLAN)) {
                    List<ProductionPlanType> productionPlans = port.getAllProductionPlan();
                    JAXBContext jc = JAXBContext.newInstance("oasis.names.specification.ubl.schema.xsd.productionplan_2");
                    Marshaller m = jc.createMarshaller();
                    for (int i = 0; i < productionPlans.size(); i++) {
                        ProductionPlanType p = productionPlans.get(i);
                        StringWriter sw = new StringWriter();
                        m.marshal(p, sw);
                        entryList.add(new Entity("ProductionPlan" + (i + 1), sw.toString()));
                    }
                } else {
                    System.err.println("Selection is not procesable! Unknown choice.");
                }
            } catch (JAXBException e) {
                System.err.println("Selection is not procesable! Marshalling failed.");
                e.printStackTrace();
            } catch (MalformedURLException e) {
                System.err.println("Selection is not procesable! Inaccesible Webservice.");
                e.printStackTrace();
            }
            if (entryList.isEmpty()) {
                entryList.add(new Entity("No Entries found!", "Database does not contain any entry for '" + CHOICE + "'."));
            }
            entryListView.modelChanged();
            super.onSubmit();
        }
    }

    class Entity implements Serializable {

        private static final long serialVersionUID = 1L;

        private String friendlyName;

        private String message;

        public Entity(String friendlyName, String message) {
            this.friendlyName = friendlyName;
            this.message = message;
        }

        public String getFriendlyName() {
            return friendlyName;
        }

        public void setFriendlyName(String friendlyName) {
            this.friendlyName = friendlyName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
