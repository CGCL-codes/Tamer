package uk.co.weft.fisherman.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Vector;
import javax.servlet.ServletException;
import uk.co.weft.dbutil.Context;
import uk.co.weft.dbutil.Contexts;
import uk.co.weft.dbutil.DataFormatException;
import uk.co.weft.dbutil.DataStoreException;
import uk.co.weft.dbutil.TableDescriptor;
import uk.co.weft.fisherman.LastChanged;
import uk.co.weft.fisherman.entities.*;
import uk.co.weft.fisherman.entities.ActionType;
import uk.co.weft.fisherman.entities.Deputy;
import uk.co.weft.fisherman.entities.Licence;
import uk.co.weft.fisherman.entities.LicenceStatus;
import uk.co.weft.fisherman.entities.LicenceType;
import uk.co.weft.fisherman.entities.Person;
import uk.co.weft.fisherman.entities.Vessel;
import uk.co.weft.fisherman.entities.views.PersonFullname;
import uk.co.weft.htform.ActionWidget;
import uk.co.weft.htform.Auxiliary;
import uk.co.weft.htform.DataMenuWidget;
import uk.co.weft.htform.InitialisationException;
import uk.co.weft.htform.LabelPseudoWidget;
import uk.co.weft.htform.LinkTableWidget;
import uk.co.weft.htform.Servlet;
import uk.co.weft.htform.SimpleDataMenuWidget;
import uk.co.weft.htform.Widget;
import uk.co.weft.htform.WidgetSet;

/**
 * A form to add or edit a licence. There do need to be some rules about this!
 * TODO: work out what the rules are and enforce them!
 *
 * @author $author$
 * @version $Revision: 1.14 $
 */
public class LicenceForm extends LastChanged {

    /** A widget set to use if licence type = vessel */
    protected WidgetSet vesselWidgetSet = new VesselWidgetSet();

    /**
	 * Specialisation: initialise me with necessary widgets
	 *
	 * @param config my configuration
	 *
	 * @throws InitialisationException
	 *
	 * @see uk.co.weft.htform.TableWrapperForm#init(uk.co.weft.dbutil.Context)
	 */
    public void init(Context config) throws InitialisationException {
        table = Licence.TABLENAME;
        keyField = Licence.KEYFN;
        vesselWidgetSet.connect(this, "vessel", 60);
        Widget w = new LabelPseudoWidget(Licence.NUMBERFN, "Licence Number", "The licence number of this licence, if issued");
        addWidget(w);
        vesselWidgetSet.addWidget(w);
        w = new Widget(Licence.SEASONFN, "Season");
        w.setSize(4);
        w.setMandatory(true);
        addWidget(w);
        vesselWidgetSet.addWidget(w);
        DataMenuWidget mw = new DataMenuWidget(Licence.HOLDERFN, "Holder", "The holder of/applicant for this licence", "Select " + Person.KEYFN + ", " + Person.FORENAMESFN + ", " + Person.SURNAMEFN + " from " + Person.TABLENAME + " order by " + Person.SURNAMEFN + ", " + Person.FORENAMESFN);
        mw.setShowExtraCols(true);
        mw.setMandatory(true);
        mw.setSize(8);
        mw.setImmutable(true);
        addWidget(mw);
        vesselWidgetSet.addWidget(mw);
        mw = new StatusWidget(Licence.STATUSFN, "Status", "The status of this licence", LicenceStatus.TABLENAME, LicenceStatus.KEYFN, LicenceStatus.DESCFN);
        mw.setMandatory(true);
        mw.setImmutable(true);
        mw.setDefault(new Integer(LicenceStatus.STATUSRECEIVED));
        addWidget(mw);
        vesselWidgetSet.addWidget(mw);
        mw = new SimpleDataMenuWidget(Licence.TYPEFN, "Type", "The type of this licence", LicenceType.TABLENAME, LicenceType.KEYFN, LicenceType.DESCFN);
        mw.setMandatory(true);
        mw.setImmutable(true);
        addWidget(mw);
        vesselWidgetSet.addWidget(mw);
        w = addWidget(new DeputiesWidget("Deputies", "Persons other than the holder authorised to use this licence"));
        vesselWidgetSet.addWidget(w);
        mw = new DataMenuWidget(Licence.VESSELFN, "Vessel", "The vessel registered for use under this licence", "select " + Vessel.KEYFN + ", " + Vessel.NAMEFN + ", " + Vessel.FLNFN + " from " + Vessel.TABLENAME + " order by " + Vessel.NAMEFN);
        mw.setMandatory(true);
        vesselWidgetSet.addWidget(mw);
        w = addWidget(new ActionActionWidget());
        w.setCssClass(Widget.ACTIONCSSCLASS);
        vesselWidgetSet.addWidget(w);
        w = addWidget(new IssueTaggablesWidget());
        w.setCssClass(Widget.DANGEROUSCSSCLASS);
        vesselWidgetSet.addWidget(w);
        w = addWidget(new IssueLDBookWidget());
        w.setCssClass(Widget.DANGEROUSCSSCLASS);
        vesselWidgetSet.addWidget(w);
        super.init(config);
        vesselWidgetSet.addWidget(identWidget);
        vesselWidgetSet.addWidget(updateWidget);
        if (allowDelete) {
            vesselWidgetSet.addWidget(deleteWidget);
        }
        if (allowLogout) {
            vesselWidgetSet.addWidget(logoutWidget);
        }
        addAuxiliary(new Auxiliary(Badge.TABLENAME, Badge.KEYFN, Badge.LICENCEFN, Licence.KEYFN, "badge", "Badges on this licence", Badge.KEYFN));
        Auxiliary history = addAuxiliary(new History());
        history.canAdd = false;
    }

    /**
	 * Specialisation: check for licence-holding officer!
	 *
	 * @param context
	 *
	 * @return true on success, else throw exception
	 *
	 * @throws DataStoreException
	 * @throws ServletException
	 *
	 * @see uk.co.weft.htform.TableWrapperForm#store(uk.co.weft.dbutil.Context)
	 */
    protected boolean store(Context context) throws DataStoreException, ServletException {
        boolean result = false;
        if (Person.getOfficer(context) == null) {
            result = super.store(context);
            Integer person = context.getValueAsInteger(Licence.HOLDERFN);
            if (result && (person != null)) {
                context.put(Servlet.REDIRECTMAGICTOKEN, "person?" + Person.KEYFN + "=" + person);
            }
        } else {
            throw new DataFormatException("Person cannot both be an officer and hold a licence");
        }
        return result;
    }

    class ActionActionWidget extends ActionWidget {

        /**
		 * set up my label, promptDisplay etc.
		 */
        public ActionActionWidget() {
            super("Next Action");
        }

        /**
		 * Specialisation: redirect to ActionForm
		 *
		 * @param c the service context
		 *
		 * @throws Exception
		 *
		 * @see uk.co.weft.htform.ActionWidget#execute(uk.co.weft.dbutil.Context)
		 */
        protected void execute(Context c) throws Exception {
            c.put(Servlet.REDIRECTMAGICTOKEN, "action?" + Licence.KEYFN + "=" + c.getValueAsString(Licence.KEYFN));
        }

        /**
		 * I should be disabled if there is no value for my form's keyField in
		 * the context, or if there is no action which can move the licence
		 * on from its current status
		 *
		 * @exception DataStoreException if can't contact the database, or
		 * 			  something is badly wrong with permissions.
		 */
        protected void postProcess(Context context) throws DataStoreException, ServletException {
            Object key = context.get(Licence.KEYFN);
            if (key == null) {
                setContextAttribute(context, "disabled", Boolean.TRUE);
            } else {
                Context scratch = new Context();
                scratch.copyDBTokens(context);
                scratch.put(Licence.KEYFN, key);
                TableDescriptor.getDescriptor(Licence.TABLENAME, Licence.KEYFN, scratch).fetch(scratch);
                scratch.put(ActionType.PREVFN, scratch.get(Licence.STATUSFN));
                scratch.put(LastChanged.LASTCHANGEDATEFN, null);
                scratch.put(LastChanged.LASTCHANGEUSERFN, null);
                Contexts rows = TableDescriptor.getDescriptor(ActionType.TABLENAME, ActionType.KEYFN, scratch).match(scratch, true);
                if ((rows == null) || rows.isEmpty()) {
                    setContextAttribute(context, "disabled", Boolean.TRUE);
                }
            }
        }
    }

    /**
	 * Special link table widget to wrap the deputies table
	 */
    class DeputiesWidget extends LinkTableWidget {

        /**
		 * @param promptDisplay my promptDisplay
		 * @param help helptext to expand on my promptDisplay
		 */
        public DeputiesWidget(String prompt, String help) {
            super(Deputy.TABLENAME, prompt, help, false, 8, Licence.TABLENAME, PersonFullname.TABLENAME, Deputy.LICENCEFN, Deputy.PERSONFN, PersonFullname.FULLNAMEFN);
        }

        /**
		 * Drop deputies specified in this context which are in excess of the
		 * maximum number permitted
		 *
		 * @param context the service context, containing inter alia database
		 * 		  authentication tokens and a vector of deputy indexes
		 * @param permitted the maximum number permitted
		 *
		 * @exception DataStoreException if communication with the database
		 * 			  fails
		 */
        protected void dropDeputies(Context context, int permitted) throws DataStoreException {
            Vector values = null;
            Object v = context.get(name);
            Connection c = null;
            PreparedStatement s = null;
            try {
                c = context.getConnection();
                s = c.prepareStatement("delete from " + Deputy.TABLENAME + " where " + Deputy.LICENCEFN + " = " + context.getValueAsString(Licence.KEYFN) + " and " + Deputy.PERSONFN + " = ?");
                if (v instanceof Vector) {
                    values = (Vector) v;
                }
                if ((values != null) && (values.size() > 0)) {
                    for (int i = permitted; i < values.size(); i++) {
                        v = values.elementAt(i);
                        if (v instanceof Integer) {
                            s.setInt(1, ((Integer) v).intValue());
                        } else {
                            s.setInt(1, Integer.parseInt(v.toString()));
                        }
                        s.executeUpdate();
                    }
                }
            } catch (DataStoreException dse) {
                throw dse;
            } catch (Exception any) {
                throw new DataStoreException(any.getMessage(), any);
            } finally {
                try {
                    if (s != null) {
                        s.close();
                    }
                    if (c != null) {
                        context.releaseConnection(c);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
		 * Specialisation: disable me if licence type does not allow deputies
		 *
		 * @param context
		 *
		 * @throws DataStoreException
		 * @throws ServletException
		 *
		 * @see uk.co.weft.htform.LinkTableWidget#postProcess(uk.co.weft.dbutil.Context)
		 */
        protected void postProcess(Context context) throws DataStoreException, ServletException {
            Integer licenceType = context.getValueAsInteger(LicenceType.KEYFN);
            if (licenceType == null) {
                setContextAttribute(context, "disabled", Boolean.TRUE);
            } else {
                Context scratch = (Context) context.clone();
                TableDescriptor.getDescriptor(LicenceType.TABLENAME, LicenceType.KEYFN, scratch).fetch(scratch);
                if (new Integer(0).equals(scratch.getValueAsInteger(LicenceType.DEPUTIESFN))) {
                    setContextAttribute(context, "disabled", Boolean.TRUE);
                }
            }
            super.postProcess(context);
        }

        /**
		 * Specialisation: Do not allow more deputies than the maximum allowed
		 * for the licence type.
		 *
		 * @param context the service context
		 *
		 * @throws DataStoreException (actually DataFormatException) if too
		 * 		   many deputies selected
		 * @throws ServletException
		 *
		 * @see uk.co.weft.htform.LinkTableWidget#preProcess(uk.co.weft.dbutil.Context)
		 */
        protected void preProcess(Context context) throws DataStoreException, ServletException {
            super.preProcess(context);
            Integer licenceType = context.getValueAsInteger(LicenceType.KEYFN);
            if (licenceType == null) {
                dropDeputies(context, 0);
                throw new DataFormatException("You must supply a licence type before you can set up deputies");
            } else {
                Vector values = null;
                Object v = context.get(name);
                if (v instanceof Vector) {
                    values = (Vector) v;
                }
                if ((values != null) && (values.size() > 0)) {
                    Context scratch = (Context) context.clone();
                    TableDescriptor.getDescriptor(LicenceType.TABLENAME, LicenceType.KEYFN, scratch).fetch(scratch);
                    Integer deputies = scratch.getValueAsInteger(LicenceType.DEPUTIESFN);
                    if ((deputies != null) && (deputies.intValue() < values.size())) {
                        dropDeputies(context, deputies.intValue());
                        throw new DataFormatException("Too many deputies for this licence type; please select fewer");
                    }
                }
            }
        }
    }

    class History extends Auxiliary {

        protected History() {
            super("HISTORY_BY_LICENCE", ActionHistory.KEYFN, ActionHistory.LICENCEFN, "action");
            canAdd = false;
            query = "select " + ActionHistory.KEYFN + ", " + ActionHistory.DATEFN + ", " + ActionType.DESCFN + ", " + ActionHistory.NOTESFN + " from HISTORY_BY_LICENCE " + " where " + parent + " = ? order by " + ActionHistory.DATEFN + " desc";
        }

        public String getTitle() {
            return "History of this licence";
        }
    }

    class IssueTaggablesWidget extends LicenceActionWidget {

        /**
		 * set up my label, promptDisplay etc.
		 */
        public IssueTaggablesWidget() {
            super("Issue Taggables");
        }

        /**
		 * Specialisation: redirect to IssueTaggablesForm
		 *
		 * @param c the service context
		 *
		 * @throws Exception
		 *
		 * @see uk.co.weft.htform.ActionWidget#execute(uk.co.weft.dbutil.Context)
		 */
        protected void execute(Context c) throws Exception {
            c.put(Servlet.REDIRECTMAGICTOKEN, "issuetaggables?" + Licence.KEYFN + "=" + c.getValueAsString(Licence.KEYFN));
        }
    }

    class IssueLDBookWidget extends LicenceActionWidget {

        /**
		 * set up my label, promptDisplay etc.
		 */
        public IssueLDBookWidget() {
            super("Issue Landing Declaration Book");
        }

        /**
		 * Specialisation: redirect to IssueTaggablesForm
		 *
		 * @param c the service context
		 *
		 * @throws Exception
		 *
		 * @see uk.co.weft.htform.ActionWidget#execute(uk.co.weft.dbutil.Context)
		 */
        protected void execute(Context c) throws Exception {
            c.put(Servlet.REDIRECTMAGICTOKEN, "ldbook?" + Licence.KEYFN + "=" + c.getValueAsString(Licence.KEYFN));
        }
    }

    abstract class LicenceActionWidget extends ActionWidget {

        /**
		 * Just pass it on up the chain
		 * @param label
		 */
        public LicenceActionWidget(String label) {
            super(label);
        }

        /**
		 * I should be disabled if there is no value for my form's keyField in
		 * the context, or if the licence is in a state where fishing is not
		 * permitted
		 *
		 * @exception DataStoreException if can't contact the database, or
		 * 			  something is badly wrong with permissions.
		 */
        public void postProcess(Context context) throws DataStoreException, ServletException {
            Object key = context.get(Licence.KEYFN);
            if (key == null) {
                setContextAttribute(context, "disabled", Boolean.TRUE);
            } else {
                Context scratch = new Context();
                scratch.copyDBTokens(context);
                scratch.put(Licence.KEYFN, key);
                TableDescriptor.getDescriptor(Licence.TABLENAME, Licence.KEYFN, scratch).fetch(scratch);
                TableDescriptor.getDescriptor(LicenceStatus.TABLENAME, LicenceStatus.KEYFN, scratch).fetch(scratch);
                if (!Boolean.TRUE.equals(scratch.getValueAsBoolean(LicenceStatus.CANFISHFN))) {
                    setContextAttribute(context, "disabled", Boolean.TRUE);
                }
            }
        }
    }

    /**
	 * A widget like a LabelPseudoWidget but deriving values from a reference
	 * data table like a DataMenuWidget
	 */
    class StatusWidget extends SimpleDataMenuWidget {

        /**
		 * @param name
		 * @param promptDisplay
		 * @param myhelp
		 * @param table
		 * @param idCol
		 * @param textCol
		 */
        public StatusWidget(String name, String prompt, String myhelp, String table, String idCol, String textCol) {
            super(name, prompt, myhelp, table, idCol, textCol);
        }

        /**
		 * Specialisation: I'm never editable
		 *
		 * @param context
		 *
		 * @throws DataFormatException
		 * @throws ServletException
		 * @throws IOException
		 *
		 * @see uk.co.weft.htform.MenuWidget#layout(uk.co.weft.dbutil.Context)
		 */
        public void layout(Context context, int tabIndex) throws DataFormatException, ServletException, IOException {
            if (context.get(name) == null) {
                context.put(name, dflt);
            }
            layoutValue(context);
        }
    }

    class VesselWidgetSet extends WidgetSet {

        /**
		 * Specialisation: I bid for the context if type is vessel
		 *
		 * @param context the service context
		 * @param whinges any whinges that have been raised
		 *
		 * @return my bid for the context
		 *
		 * @throws Exception
		 *
		 * @see uk.co.weft.htform.WidgetSet#claim(uk.co.weft.dbutil.Context,
		 * 		uk.co.weft.dbutil.Context)
		 */
        protected int claim(Context context, Context whinges) throws Exception {
            int result = 0;
            if (new Integer(LicenceType.TYPEVESSEL).equals(context.getValueAsInteger(Licence.TYPEFN))) {
                result = precedence;
            }
            return result;
        }
    }
}
