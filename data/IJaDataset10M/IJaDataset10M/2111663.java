package org.torweg.pulse.component.util.representative.admin;

import java.util.List;
import java.util.Locale;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.torweg.pulse.annotations.Action;
import org.torweg.pulse.annotations.Groups;
import org.torweg.pulse.annotations.Permission;
import org.torweg.pulse.annotations.RequireToken;
import org.torweg.pulse.bundle.Bundle;
import org.torweg.pulse.component.util.admin.Editor;
import org.torweg.pulse.component.util.model.Representative;
import org.torweg.pulse.configuration.Configurable;
import org.torweg.pulse.invocation.lifecycle.Lifecycle;
import org.torweg.pulse.service.PulseException;
import org.torweg.pulse.service.event.XSLTOutputEvent;
import org.torweg.pulse.service.request.Command;
import org.torweg.pulse.service.request.ServiceRequest;
import org.torweg.pulse.util.adminui.JSONCommunicationUtils;

/**
 * The main editor for the pulse website-administration of the
 * {@code Representative} utility package.
 * 
 * @author Daniel Dietz
 * @version $Revision: 2021 $
 */
public class RepresentativeEditor extends Editor implements Configurable<RepresentativeEditorConfiguration> {

    /**
	 * The {@code Configuration}.
	 */
    private RepresentativeEditorConfiguration config;

    /**
	 * Initialises the representative(-overview)-editor of the pulse
	 * website-administration.
	 * 
	 * @param bundle
	 *            the current {@code Bundle}
	 * @param request
	 *            the current {@code ServiceRequest}
	 * 
	 * @return a {@code RepresentativeEditorResult}
	 */
    @RequireToken
    @Action(value = "initRepresentativesEditor", generate = true)
    @Permission("initRepresentativesEditor")
    @Groups(values = { "RepresentativeAdministrator" })
    public final RepresentativeEditorResult initRepresentativesEditor(final Bundle bundle, final ServiceRequest request) {
        XSLTOutputEvent event = new XSLTOutputEvent(this.config.getAjaxXSLHandle());
        event.setStopEvent(true);
        request.getEventManager().addEvent(event);
        return new RepresentativeEditorResult();
    }

    /**
	 * Initialises the representative-editor-window of the pulse
	 * website-administration.
	 * 
	 * @param bundle
	 *            the current {@code Bundle}.
	 * @param request
	 *            the current {@code ServiceRequest}.
	 * 
	 * @return a {@code RepresentativeEditorResult}.
	 */
    @RequireToken
    @Action(value = "initRepresentativeEditor", generate = true)
    @Permission("initRepresentativeEditor")
    @Groups(values = { "RepresentativeAdministrator" })
    public final RepresentativeEditorResult initRepresentativeEditor(final Bundle bundle, final ServiceRequest request) {
        RepresentativeEditorResult result = new RepresentativeEditorResult();
        String id = getValueFromCommand("id", request.getCommand());
        if (id != null && !id.equals("")) {
            Session s = Lifecycle.getHibernateDataSource().createNewSession();
            Transaction tx = s.beginTransaction();
            try {
                Representative representative = (Representative) s.get(Representative.class, Long.parseLong(id));
                if (representative == null) {
                    result.setSuccess(false);
                    result.addError(RepresentativeEditor.Error.COULD_NOT_LOAD_REPRESENTATIVE_FOR_GIVEN_ID.toString());
                } else {
                    result.setRepresentative(representative);
                }
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new PulseException("Error: " + e.getLocalizedMessage(), e);
            } finally {
                s.close();
            }
        }
        XSLTOutputEvent event = new XSLTOutputEvent(this.config.getAjaxRepresentativeXSLHandle());
        event.setStopEvent(true);
        request.getEventManager().addEvent(event);
        return result;
    }

    /**
	 * Loads the {@code Representative}s.
	 * 
	 * @param bundle
	 *            the current {@code Bundle}
	 * @param request
	 *            the current {@code ServiceRequest}
	 */
    @RequireToken
    @Action(value = "loadRepresentatives", generate = true)
    @Permission("loadRepresentatives")
    @Groups(values = { "RepresentativeAdministrator" })
    @SuppressWarnings("unchecked")
    public final void loadRepresentatives(final Bundle bundle, final ServiceRequest request) {
        Command command = request.getCommand();
        int start = Integer.parseInt(getValueFromCommand("start", command));
        int limit = Integer.parseInt(getValueFromCommand("limit", command));
        String sort = getValueFromCommand("sort", command);
        String dir = getValueFromCommand("dir", command);
        Order order = null;
        if (dir.equalsIgnoreCase("asc")) {
            order = Order.asc(sort).ignoreCase();
        } else {
            order = Order.desc(sort).ignoreCase();
        }
        long total = 0;
        List<Representative> representatives = null;
        Session s = Lifecycle.getHibernateDataSource().createNewSession();
        Transaction tx = s.beginTransaction();
        try {
            total = (Long) s.createCriteria(Representative.class).setProjection(Projections.rowCount()).uniqueResult();
            representatives = (List<Representative>) s.createCriteria(Representative.class).addOrder(order).setFirstResult(start).setMaxResults(limit).list();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new PulseException("Error: " + e.getLocalizedMessage(), e);
        } finally {
            s.close();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", total);
        JSONArray jsonArray = new JSONArray();
        for (Representative representative : representatives) {
            jsonArray.add(representative.toJSON());
        }
        jsonObject.put("data", jsonArray);
        JSONCommunicationUtils.jsonSuccessMessage(request, jsonObject);
    }

    /**
	 * Loads a {@code Representative}.
	 * 
	 * @param request
	 *            the current {@code ServiceRequest}
	 */
    @RequireToken
    @Action(value = "loadRepresentative", generate = true)
    @Permission("loadRepresentative")
    @Groups(values = { "RepresentativeAdministrator" })
    public final void loadRepresentative(final ServiceRequest request) {
        String id = getValueFromCommand("id", request.getCommand());
        Representative representative = null;
        if (!id.equals("")) {
            Session s = Lifecycle.getHibernateDataSource().createNewSession();
            Transaction tx = s.beginTransaction();
            try {
                representative = (Representative) s.get(Representative.class, Long.parseLong(id));
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new PulseException("Error: " + e.getLocalizedMessage(), e);
            } finally {
                s.close();
            }
        }
        if (representative == null) {
            JSONObject error = new JSONObject();
            error.put("e", RepresentativeEditor.Error.COULD_NOT_LOAD_REPRESENTATIVE_FOR_GIVEN_ID);
            JSONCommunicationUtils.jsonErrorMessage(request, error);
        } else {
            JSONCommunicationUtils.jsonSuccessMessage(request, "data", representative.toJSON());
        }
    }

    /**
	 * Deletes a {@code Representative}.
	 * 
	 * @param bundle
	 *            the current {@code Bundle}
	 * @param request
	 *            the current {@code ServiceRequest}
	 */
    @RequireToken
    @Action(value = "deleteRepresentative", generate = true)
    @Permission("deleteRepresentative")
    @Groups(values = { "RepresentativeAdministrator" })
    public final void deleteRepresentative(final Bundle bundle, final ServiceRequest request) {
        String id = getValueFromCommand("id", request.getCommand());
        if (id == null) {
            JSONCommunicationUtils.jsonErrorMessage(request);
            return;
        }
        Session s = Lifecycle.getHibernateDataSource().createNewSession();
        Transaction tx = s.beginTransaction();
        JSONObject error = null;
        try {
            Representative toDelete = (Representative) s.get(Representative.class, Long.parseLong(id));
            if (toDelete == null) {
                error = new JSONObject();
                error.put("e", RepresentativeEditor.Error.COULD_NOT_DELETE_REPRESENTATIVE_FOR_GIVEN_ID);
            } else {
                s.delete(toDelete);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new PulseException("Error: " + e.getLocalizedMessage(), e);
        } finally {
            s.close();
        }
        if (error != null) {
            JSONCommunicationUtils.jsonErrorMessage(request, error);
        } else {
            JSONCommunicationUtils.jsonSuccessMessage(request);
        }
    }

    /**
	 * Saves a {@code Representative} - either during creation or during
	 * editing.
	 * 
	 * @param bundle
	 *            the current {@code Bundle}.
	 * @param request
	 *            the current {@code ServiceRequest}.
	 */
    @RequireToken
    @Action(value = "saveRepresentative", generate = true)
    @Permission("saveRepresentative")
    @Groups(values = { "RepresentativeAdministrator" })
    public final void saveRepresentative(final Bundle bundle, final ServiceRequest request) {
        Command command = request.getCommand();
        String id = getValueFromCommand("id", command);
        Session s = Lifecycle.getHibernateDataSource().createNewSession();
        Transaction tx = s.beginTransaction();
        JSONObject error = null;
        Representative representative;
        try {
            if (id == null) {
                representative = new Representative();
            } else {
                representative = (Representative) s.get(Representative.class, Long.parseLong(id));
            }
            if (representative == null) {
                error = new JSONObject();
                error.put("e", RepresentativeEditor.Error.COULD_NOT_LOAD_REPRESENTATIVE_FOR_GIVEN_ID);
            } else {
                error = applyRepresentativeValues(representative, command);
                if (error == null) {
                    s.saveOrUpdate(representative);
                }
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new PulseException("Error: " + e.getLocalizedMessage(), e);
        } finally {
            s.close();
        }
        if (error != null) {
            JSONCommunicationUtils.jsonErrorMessage(request, error);
        } else {
            JSONCommunicationUtils.jsonSuccessMessage(request, "data", representative.toJSON());
        }
    }

    /**
	 * Applies the values form the given {@code Command} to the given
	 * {@code Representative}.
	 * 
	 * @param representative
	 *            the current {@code Representative}
	 * @param command
	 *            the current {@code Command}
	 * 
	 * @return a error-{@code JSONObject} if any occurs, {@code null} otherwise
	 */
    private JSONObject applyRepresentativeValues(final Representative representative, final Command command) {
        String representsRegion = getValueFromCommand("representsRegion", command);
        if (representsRegion != null) {
            representative.setRepresentsRegion(representsRegion.toUpperCase(Locale.ENGLISH));
        }
        String representsCountry = getValueFromCommand("representsCountry", command);
        if (representsCountry != null) {
            representative.setRepresentsCountry(representsCountry.toUpperCase(Locale.ENGLISH));
        }
        String representsState = getValueFromCommand("representsState", command);
        if (representsState != null) {
            representative.setRepresentsState(representsState.toUpperCase(Locale.ENGLISH));
        }
        String name = getValueFromCommand("name", command);
        if (name != null) {
            representative.setName(name);
        }
        String nameAffix = getValueFromCommand("nameAffix", command);
        if (nameAffix != null) {
            representative.setNameAffix(nameAffix);
        }
        String contactPerson = getValueFromCommand("contactPerson", command);
        if (contactPerson != null) {
            representative.setContactPerson(contactPerson);
        }
        applyAddressValues(representative, command);
        applyContactData(representative, command);
        return applyRepresentativeValuesErrorCheck(representative);
    }

    /**
	 * Applies the address-values from the given {@code Command} to the given
	 * {@code Representative}:<br/>
	 * <strong>[ </strong><tt>street</tt>, <tt>postalCode</tt>, <tt>city</tt>,
	 * <tt>country</tt><strong> ]</strong>.
	 * 
	 * 
	 * @param representative
	 *            the {@code Representative}
	 * @param command
	 *            the {@code Command}
	 */
    private void applyAddressValues(final Representative representative, final Command command) {
        String street = getValueFromCommand("street", command);
        if (street != null) {
            representative.setStreet(street);
        }
        String postalCode = getValueFromCommand("postalCode", command);
        if (postalCode != null) {
            representative.setPostalCode(postalCode);
        }
        String city = getValueFromCommand("city", command);
        if (city != null) {
            representative.setCity(city);
        }
        String country = getValueFromCommand("country", command);
        if (country != null) {
            representative.setCountry(country.toUpperCase(Locale.ENGLISH));
        }
        String state = getValueFromCommand("state", command);
        if (state != null) {
            representative.setState(state);
        }
        String addressAffix = getValueFromCommand("addressAffix", command);
        if (addressAffix != null) {
            representative.setAddressAffix(addressAffix);
        }
    }

    /**
	 * Applies the address-values from the given {@code Command} to the given
	 * {@code Representative}:<br/>
	 * <strong>[</strong> <tt>phoneNumber</tt>, <tt>mobilePhoneNumber</tt>,
	 * <tt>faxNumber</tt>, <tt>email</tt>, <tt>url</tt> <strong> ]</strong>.
	 * 
	 * 
	 * @param representative
	 *            the {@code Representative}
	 * @param command
	 *            the {@code Command}
	 */
    private void applyContactData(final Representative representative, final Command command) {
        String phoneNumber = getValueFromCommand("phoneNumber", command);
        if (phoneNumber != null) {
            representative.setPhoneNumber(phoneNumber);
        }
        String mobilePhoneNumber = getValueFromCommand("mobilePhoneNumber", command);
        if (mobilePhoneNumber != null) {
            representative.setMobilePhoneNumber(mobilePhoneNumber);
        }
        String faxNumber = getValueFromCommand("faxNumber", command);
        if (faxNumber != null) {
            representative.setFaxNumber(faxNumber);
        }
        String email = getValueFromCommand("email", command);
        if (email != null) {
            representative.setEmail(email);
        }
        String url = getValueFromCommand("url", command);
        if (url != null) {
            representative.setUrl(url);
        }
    }

    /**
	 * Checks the given {@code Representative} for value-errors.
	 * 
	 * @param representative
	 *            the current {@code Representative}
	 * @return a error-{@code JSONObject} if any occurs, {@code null} otherwise
	 */
    private JSONObject applyRepresentativeValuesErrorCheck(final Representative representative) {
        if (representative.getRepresentsRegion() == null || representative.getRepresentsRegion().equals("")) {
            JSONObject error = new JSONObject();
            error.put("e", RepresentativeEditor.Error.CANNOT_STORE_REPRESENTATIVE_WITH_REPRESENTS_REGION_NOT_SET);
            return error;
        }
        if (representative.getRepresentsCountry() == null || representative.getRepresentsCountry().equals("")) {
            JSONObject error = new JSONObject();
            error.put("e", RepresentativeEditor.Error.CANNOT_STORE_REPRESENTATIVE_WITH_REPRESENTS_COUNTRY_NOT_SET);
            return error;
        }
        if (representative.getCountry() == null || representative.getCountry().equals("")) {
            JSONObject error = new JSONObject();
            error.put("e", RepresentativeEditor.Error.CANNOT_STORE_REPRESENTATIVE_WITH_COUNTRY_NOT_SET);
            return error;
        }
        if (representative.getName() == null || representative.getName().equals("")) {
            JSONObject error = new JSONObject();
            error.put("e", RepresentativeEditor.Error.CANNOT_STORE_REPRESENTATIVE_WITH_EMPTY_NAME);
            return error;
        }
        return null;
    }

    /**
	 * Initialises the {@code Configuration} with the given
	 * {@code Configuration}.
	 * 
	 * @param conf
	 *            the {@code Configuration}
	 */
    public final void initialize(final RepresentativeEditorConfiguration conf) {
        this.config = conf;
    }

    /**
	 * The {@code Error}-codes of the {@code RepresentativeEditor}.
	 * 
	 * @author Daniel Dietz
	 * @version $Revision: 2021 $
	 * 
	 */
    public static enum Error {

        /**
		 * Could not load representative for given id.
		 */
        COULD_NOT_LOAD_REPRESENTATIVE_FOR_GIVEN_ID, /**
		 * Could not delete representative for given id.
		 */
        COULD_NOT_DELETE_REPRESENTATIVE_FOR_GIVEN_ID, /**
		 * Cannot store representative with empty name.
		 */
        CANNOT_STORE_REPRESENTATIVE_WITH_EMPTY_NAME, /**
		 * Cannot store representative with represents region not set.
		 */
        CANNOT_STORE_REPRESENTATIVE_WITH_REPRESENTS_REGION_NOT_SET, /**
		 * Cannot store representative represents country not set.
		 */
        CANNOT_STORE_REPRESENTATIVE_WITH_REPRESENTS_COUNTRY_NOT_SET, /**
		 * Cannot store representative with country not set.
		 */
        CANNOT_STORE_REPRESENTATIVE_WITH_COUNTRY_NOT_SET
    }
}
