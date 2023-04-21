package org.javacoding.upupa.webapp.controller;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.GenericManager;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.controller.BaseFormController;
import org.javacoding.upupa.model.catalog.RingingRecordType;
import org.javacoding.upupa.model.entity.EURing;
import org.javacoding.upupa.model.entity.MoultCard;
import org.javacoding.upupa.model.entity.RingingRecord;
import org.javacoding.upupa.model.entity.Site;
import org.javacoding.upupa.service.EuRingManager;
import org.javacoding.upupa.service.SiteManager;
import org.javacoding.upupa.webapp.editor.EuringEditor;
import org.javacoding.upupa.webapp.editor.RingerEditor;
import org.javacoding.upupa.webapp.editor.SiteEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import sun.awt.windows.WWindowPeer;

public class RingingRecordFormController extends BaseFormController {

    public static String dateFormat = "dd/MM/yyyy";

    private static final SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

    private GenericManager<RingingRecord, Long> ringingRecordManager = null;

    private EuRingManager euRingManager = null;

    private SiteManager siteManager = null;

    private UserManager userManager = null;

    public void setRingingRecordManager(GenericManager<RingingRecord, Long> ringingRecordManager) {
        this.ringingRecordManager = ringingRecordManager;
    }

    public void setEuRingManager(EuRingManager euRingManager) {
        this.euRingManager = euRingManager;
    }

    public void setSiteManager(SiteManager siteManager) {
        this.siteManager = siteManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public RingingRecordFormController() {
        setCommandClass(RingingRecord.class);
        setCommandName("ringingRecord");
    }

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        super.initBinder(request, binder);
        NumberFormat nf = NumberFormat.getInstance(request.getLocale());
        binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, nf, true));
        NumberFormat integerNumberFormat = NumberFormat.getInstance(request.getLocale());
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, integerNumberFormat, false));
        binder.registerCustomEditor(Site.class, new SiteEditor(siteManager));
        binder.registerCustomEditor(EURing.class, new EuringEditor(euRingManager));
        binder.registerCustomEditor(User.class, "ringer", new RingerEditor(userManager));
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        if (!StringUtils.isBlank(id)) {
            return ringingRecordManager.get(new Long(id));
        }
        final RingingRecord r = new RingingRecord();
        String nextRingNumber = (String) request.getSession().getAttribute("nextRingNumber");
        r.setRingNumber(nextRingNumber);
        r.setSite((Site) request.getSession().getAttribute("site"));
        r.setRinger((User) request.getSession().getAttribute("ringer"));
        r.setEuring((EURing) request.getSession().getAttribute("euring"));
        r.setRingingDate((String) request.getSession().getAttribute("ringingDate"));
        r.setTimeHour((Byte) request.getSession().getAttribute("hour"));
        r.setType((RingingRecordType) request.getSession().getAttribute("type"));
        return r;
    }

    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        log.debug("entering 'onSubmit' method...");
        RingingRecord ringingRecord = (RingingRecord) command;
        boolean isNew = (ringingRecord.getId() == null);
        String success = getSuccessView();
        Locale locale = request.getLocale();
        if (!hasValidWingLength(ringingRecord.getEuring(), ringingRecord.getWingLength())) {
            errors.rejectValue("wingLength", "ringingRecord.wingLength.error");
            return showForm(request, response, errors);
        }
        if (!Pattern.matches("(0[1-9]|[12][0-9]|3[01])[/](0[1-9]|1[012])[/](19|20)\\d\\d", ringingRecord.getRingingDate())) {
            errors.rejectValue("ringingDate", "ringingRecord.ringingDate.error");
            return showForm(request, response, errors);
        }
        if (new Date().before(ringingRecord.getRingingDateAsDateObject())) {
            errors.rejectValue("ringingDate", "ringingRecord.ringingDate.setinfuture.error");
            return showForm(request, response, errors);
        }
        if ((ringingRecord.getWeighingDate() != null) && (!ringingRecord.getWeighingDate().trim().equals(""))) {
            if (!Pattern.matches("(0[1-9]|[12][0-9]|3[01])[/](0[1-9]|1[012])[/](19|20)\\d\\d", ringingRecord.getWeighingDate())) {
                errors.rejectValue("weighingDate", "ringingRecord.weighingDate.error");
                return showForm(request, response, errors);
            }
            final Date weighingDate = ringingRecord.getWeighingDateAsDateObject();
            final Date ringingDate = new Date(ringingRecord.getRingingDateAsDateObject().getTime() + (2 * 24 * 60 * 60 * 1000));
            if (weighingDate.before(ringingDate)) {
                errors.rejectValue("weighingDate", "ringingRecord.weighingDate.tooearly.error");
                return showForm(request, response, errors);
            }
        }
        if (request.getParameter("delete") != null) {
            ringingRecordManager.remove(ringingRecord.getId());
            saveMessage(request, getText("ringingRecord.deleted", locale));
        } else {
            final HttpSession session = request.getSession();
            MoultCard moultCard = (MoultCard) session.getAttribute("newMoultCard");
            if (moultCard != null) {
                ringingRecord.setMoultCardId(moultCard.getId());
            }
            ringingRecordManager.save(ringingRecord);
            String ringNumber = ringingRecord.getRingNumber();
            if (!RingingRecordType.RETRAP.equals(ringingRecord.getType())) {
                session.setAttribute("nextRingNumber", getNextRingCode(ringNumber));
            }
            session.setAttribute("euring", ringingRecord.getEuring());
            session.setAttribute("site", ringingRecord.getSite());
            session.setAttribute("ringer", ringingRecord.getRinger());
            session.setAttribute("ringingDate", ringingRecord.getRingingDate());
            session.setAttribute("hour", ringingRecord.getTimeHour());
            session.setAttribute("type", ringingRecord.getType());
            String key = (isNew) ? "ringingRecord.added" : "ringingRecord.updated";
            saveMessage(request, getText(key, locale));
            if (!isNew) {
                success = "redirect:ringingrecords.html";
            }
        }
        return new ModelAndView(success);
    }

    private boolean hasValidWingLength(final EURing euring, final BigDecimal wingLength) {
        if (wingLength == null) {
            return true;
        }
        final BigDecimal maxLength = euring.getMaxWing();
        final BigDecimal minLength = euring.getMinWing();
        if ((maxLength.compareTo(BigDecimal.ZERO) != 0) && (wingLength.compareTo(maxLength) > 0)) {
            return false;
        }
        if ((minLength.compareTo(BigDecimal.ZERO) != 0) && (wingLength.compareTo(minLength) < 0)) {
            return false;
        }
        return true;
    }

    private String getNextRingCode(String ringNumber) {
        String nextRingNumber = null;
        String padding = null;
        if (ringNumber.length() > 0) {
            if (isInteger(ringNumber)) {
                char[] ringChars = ringNumber.toCharArray();
                padding = "";
                for (char ringChar : ringChars) {
                    if (ringChar == '0') {
                        padding = padding + ringChar;
                    } else {
                        break;
                    }
                }
                nextRingNumber = padding + (Integer.parseInt(ringNumber) + 1);
            } else {
                int charAt = -1;
                for (int i = ringNumber.length() - 1; i >= 0; i--) {
                    if (!isInteger("" + ringNumber.charAt(i))) {
                        charAt = i;
                        break;
                    }
                }
                int lastNumber = 0;
                if (charAt + 1 == ringNumber.length()) {
                    padding = "";
                    lastNumber++;
                } else {
                    char[] ringChars = ringNumber.substring(charAt + 1).toCharArray();
                    padding = "";
                    for (char ringChar : ringChars) {
                        if (ringChar == '0') {
                            padding = padding + ringChar;
                        } else {
                            break;
                        }
                    }
                    lastNumber = Integer.parseInt(ringNumber.substring(charAt + 1));
                    lastNumber += 1;
                }
                nextRingNumber = ringNumber.substring(0, charAt + 1) + padding + lastNumber;
            }
        }
        return nextRingNumber;
    }

    private boolean isInteger(final String putativeInteger) {
        try {
            Integer.parseInt(putativeInteger);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
