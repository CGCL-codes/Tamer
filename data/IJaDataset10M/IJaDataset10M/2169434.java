package org.openscience.nmrshiftdb.portlets;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.ecs.ConcreteElement;
import org.apache.ecs.StringElement;
import org.apache.jetspeed.portal.portlets.AbstractPortlet;
import org.apache.jetspeed.services.security.JetspeedDBSecurityService;
import org.apache.turbine.om.NumberKey;
import org.apache.turbine.services.db.TurbineDB;
import org.apache.turbine.util.Log;
import org.apache.turbine.util.RunData;
import org.apache.turbine.util.ServletUtils;
import org.apache.turbine.util.db.Criteria;
import org.apache.turbine.util.mail.MailMessage;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.openscience.nmrshiftdb.om.DBMolecule;
import org.openscience.nmrshiftdb.om.DBMoleculePeer;
import org.openscience.nmrshiftdb.om.DBSpectrum;
import org.openscience.nmrshiftdb.om.DBSpectrumPeer;
import org.openscience.nmrshiftdb.om.DBUserDBSpectrum;
import org.openscience.nmrshiftdb.om.DBUserDBSpectrumPeer;
import org.openscience.nmrshiftdb.om.NmrshiftdbUser;
import org.openscience.nmrshiftdb.om.NmrshiftdbUserPeer;
import org.openscience.nmrshiftdb.util.Export;
import org.openscience.nmrshiftdb.util.GeneralUtils;
import org.openscience.nmrshiftdb.util.NmrshiftdbConstants;
import org.openscience.nmrshiftdb.util.SubmittingData;

/**
 *  This is the portlet handling reviewing spectra/molecules...
 *
 * @author     Stefan Kuhn
 * @created    26. Februar 2002
 */
public class ReviewPortlet extends AbstractPortlet {

    /**
	 *Writes an spectrum to disk as an submittingData object.
	 *
	 * @param  spectrum    The spectrum
	 * @param  runData     The runData object
	 */
    public void setReviewFlagInBackup(DBSpectrum spectrum, RunData runData) {
        File file = new File("Unknown");
        String filename = spectrum.getNmrshiftdbNumber();
        try {
            String dir = GeneralUtils.getNmrshiftdbProperty("serializeddir", runData);
            file = new File(dir + File.separatorChar + filename.replace('/', '|') + ".xml");
            SubmittingData sd = spectrum.getMeAsSubmittingData(true, ServletUtils.expandRelative(runData.getServletConfig(), "/WEB-INF/conf/normalizer.xml"), false);
            FileWriter out = new FileWriter(file);
            out.write(sd.serializeMe());
            out.close();
        } catch (FileNotFoundException ex) {
            Log.debug("No serialized file found for " + filename);
            return;
        } catch (Exception ex) {
            Log.error("Problems when reading serilized file " + file.toString());
            return;
        }
    }

    /**
	 *  Gets the content to be displayed in the portlet as HTML
	 *
	 * @param  runData  Created by turbine
	 * @return          The content to be displayed as the portlet
	 */
    public ConcreteElement getContent(RunData runData) {
        HttpServletRequest req = runData.getRequest();
        String action = req.getParameter("nmrshiftdbaction");
        HttpSession session = req.getSession();
        if (req.getParameter("applet") != null && req.getParameter("applet").equals("applet")) {
            session.setAttribute("applet", "applet");
        }
        if (req.getParameter("applet") != null && req.getParameter("applet").equals("noapplet")) {
            runData.getSession().setAttribute("applet", "noapplet");
        }
        if (action != null && action.equals("reviewbyurl")) {
            try {
                String spectrumId = req.getParameter("id");
                String reviewKey = req.getParameter("reviewkey");
                session.setAttribute("user", req.getParameter("userId"));
                DBSpectrum spectrum = null;
                try {
                    spectrum = DBSpectrumPeer.retrieveByPK(new NumberKey(spectrumId));
                } catch (Exception ex) {
                    return (new StringElement("Seems there is no such spectrum. Log in and try to enter the id given in the email manually, if the problem persist please contact the administrator!"));
                }
                String dbReviewKey = spectrum.getReviewKey();
                if (!dbReviewKey.equals(reviewKey)) {
                    return (new StringElement("The keys don't mach. Log in and try to enter the id given in the email manually, if the problem persist please contact the administrator!"));
                }
                session.setAttribute("reviewbyurl", "true");
                return (new StringElement(makeReviewScreen(spectrumId, runData)));
            } catch (Exception ex) {
                return (new StringElement(GeneralUtils.logError(ex, "review", runData, true)));
            }
        }
        if (!new JetspeedDBSecurityService().checkPermission(runData, "review") && session.getAttribute("reviewbyurl") == null) {
            try {
                return new StringElement("You are not a reviewer or not logged in! If you are interested in becoming a reviewer, <a href=\"mailto:" + GeneralUtils.getAdminEmail(runData) + "\">email</a> the administrator!");
            } catch (Exception ex) {
                return new StringElement("You are not a reviewer or not logged in! If you are interested in becoming a reviewer, email the administrator!");
            }
        }
        String message = "";
        if (action != null && action.equals("changeemail")) {
            try {
                DBSpectrum spectrum = DBSpectrumPeer.retrieveByPK(new NumberKey(req.getParameter("spectrumid")));
                String email = "";
                StringBuffer url = javax.servlet.http.HttpUtils.getRequestURL(runData.getRequest());
                url.append("pane0/Submit?nmrshiftdbaction=contributoreditbyurl&spectrum=" + spectrum.getSpectrumId() + "&reviewkey=" + spectrum.getReviewKey() + "&userId=" + spectrum.getUserId());
                if (session.getAttribute("user") != null) {
                    email = NmrshiftdbUserPeer.retrieveByPK(new NumberKey((String) session.getAttribute("user"))).getEmail();
                    session.removeAttribute("user");
                } else {
                    email = ((NmrshiftdbUser) runData.getUser()).getEmail();
                }
                MailMessage mail = new MailMessage(GeneralUtils.getSmtpServer(runData), NmrshiftdbUserPeer.retrieveByPK(spectrum.getUserId()).getEmail(), ((NmrshiftdbUser) runData.getUser()).getEmail(), "Reviewer questions regarding your NMRShiftDB submit", "The reviewer of your input from " + spectrum.getDate() + " (id:" + spectrum.getNmrshiftdbNumber() + ") sends you the following comment: \r\n\r\n" + req.getParameter("comment") + ".\r\n\r\nIf you have questions about this, you can contact the reviewer by email:" + email + "." + GeneralUtils.getEmailSignature(runData) + " or by replying to this message. You may also edit the spectrum via your personal page.");
                if (req.getParameter("cc") != null && req.getParameter("cc").equals("true")) mail.setCc(((NmrshiftdbUser) runData.getUser()).getEmail());
                boolean success = mail.send();
                action = "review";
                if (!success) {
                    message = "Problem sending email!";
                } else {
                    message = "The email was successfully sent.";
                }
            } catch (Exception ex) {
                return new StringElement(GeneralUtils.logError(ex, "ReviewPortlet/sending correct email", runData, true));
            }
        }
        if (action != null && action.equals("accept")) {
            try {
                review(runData, NmrshiftdbConstants.TRUE, new NumberKey(req.getParameter("spectrumid")));
            } catch (Exception ex) {
                return (new StringElement(GeneralUtils.logError(ex, "database problems", runData, true)));
            }
            return (new StringElement(req.getParameter("spectrumid") + " has been reviewed as valid. Thanks for your participation! <a href=\"portal/pane0/Review\">New review</a>"));
        }
        if (action != null && action.equals("reject")) {
            try {
                review(runData, NmrshiftdbConstants.REJECTED, new NumberKey(req.getParameter("spectrumid")));
                return (new StringElement(new StringElement(req.getParameter("spectrumid") + " has been reviewed as invalid. Thanks for your participation! <a href=\"portal/pane0/Review\">New review</a>")));
            } catch (Exception ex) {
                return (new StringElement(GeneralUtils.logError(ex, "database problems", runData, true)));
            }
        }
        if (action != null && action.equals("review")) {
            try {
                String moleculeId = req.getParameter("moleculeid");
                String spectrumId = req.getParameter("spectrumid");
                String sql = "select SPECTRUM.SPECTRUM_ID from MOLECULE join SPECTRUM using (MOLECULE_ID) where ";
                if (spectrumId != null) sql += "SPECTRUM.SPECTRUM_ID=" + spectrumId; else sql += "MOLECULE.MOLECULE_ID=" + moleculeId + " and SPECTRUM.REVIEW_FLAG='false'";
                ResultSet rs = TurbineDB.getConnection().createStatement().executeQuery(sql);
                if (!rs.next()) return (new StringElement("Seems there is no such spectrum!<br> <form action=\"portal;jsessionid=" + runData.getSession().getId() + "\" method=\"post\"><input type=\"hidden\" name=\"nmrshiftdbaction\" value=\"review\">Enter the Id given in the email sent to you:<input type=\"text\" name=\"moleculeid\"><input type=\"submit\" value=\"Review this submit\"></form>"));
                return (new StringElement("<p style=\"color:red\">" + message + "</p><br><br>" + makeReviewScreen(rs.getString(1), runData)));
            } catch (Exception ex) {
                return (new StringElement(GeneralUtils.logError(ex, "review", runData, true)));
            }
        }
        return (new StringElement("<form action=\"portal;jsessionid=" + runData.getSession().getId() + "\" method=\"post\"><input type=\"hidden\" name=\"nmrshiftdbaction\" value=\"review\">Enter the Id given in the email sent to you:<input type=\"text\" name=\"spectrumid\"><input type=\"submit\" value=\"Review this submit\"></form>"));
    }

    /**
	 * Puts together the review screen
	 *
	 * @param  spectrumId  The spectrum to display
	 * @param  runData     The RunData object
	 * @return             Review screen for this spectrum
	 */
    public String makeReviewScreen(String spectrumId, RunData runData) throws Exception {
        DBSpectrum spectrum = null;
        DBMolecule mol = null;
        try {
            spectrum = DBSpectrumPeer.retrieveByPK(new NumberKey(spectrumId));
        } catch (Exception ex) {
            return ("There is no such data!<br><br><form action=\"portal;jsessionid=" + runData.getSession().getId() + "\" method=\"post\"><input type=\"hidden\" name=\"nmrshiftdbaction\" value=\"review\">Enter the Id given in the email sent to you:<input type=\"text\" name=\"moleculeid\"><input type=\"submit\" value=\"Review this submit\"></form>");
        }
        if (spectrum.getReviewFlag().equals(NmrshiftdbConstants.EDITED)) {
            return (spectrumId + " has been edited. The edited spectrum has been reassigned. You do not need to do anything with respect to this review! <a href=\"portal/pane0/Review;jsessionid=" + runData.getSession().getId() + "\">New review</a>");
        }
        if (!spectrum.getReviewFlag().equals(NmrshiftdbConstants.FALSE)) {
            return (spectrumId + " has already been reviewed! <a href=\"portal/pane0/Review;jsessionid=" + runData.getSession().getId() + "\">New review</a>");
        }
        Criteria crit = new Criteria();
        crit.add(DBMoleculePeer.MOLECULE_ID, spectrum.getMoleculeId());
        mol = (DBMolecule) DBMoleculePeer.doSelect(crit).get(0);
        spectrum.predict = true;
        VelocityContext context = new VelocityContext();
        spectrum.runData = runData;
        context.put("spectrum", spectrum);
        context.put("mol", mol);
        if (runData.getSession().getAttribute("applet") != null && ((String) runData.getSession().getAttribute("applet")).equals("applet")) {
            File outputFile = new File(ServletUtils.expandRelative(runData.getServletConfig(), "/nmrshiftdbhtml/" + System.currentTimeMillis() + "review.mol"));
            FileWriter out = new FileWriter(outputFile);
            out.write(mol.getStructureFile(1, false));
            out.close();
            context.put("molfile", "/nmrshiftdbhtml/" + outputFile.getName());
        } else {
            Export export = new Export(spectrum);
            export.backColor = Color.lightGray;
            String filename = "/nmrshiftdbhtml/" + System.currentTimeMillis() + "review";
            File outputFile = new File(ServletUtils.expandRelative(runData.getServletConfig(), filename));
            export.getImage(true, "jpeg", outputFile.toString(), 350, 250, true, null);
            context.put("image", filename + ".jpg");
            filename = "/nmrshiftdbhtml/" + System.currentTimeMillis() + "reviewspec";
            outputFile = new File(ServletUtils.expandRelative(runData.getServletConfig(), filename));
            export.getImage(false, "jpeg", outputFile.toString(), 350, 250, false, null);
            context.put("specimage", filename + ".jpg");
        }
        context.put("data", runData);
        StringWriter w = new StringWriter();
        Velocity.mergeTemplate("review.vm", "ISO-8859-1", context, w);
        return ("" + w);
    }

    /**
	 *Reviews a spectrum
	 *
	 * @param  runData        The RunData object
	 * @param  newFlag        The new review flag
	 * @exception  Exception  Database problems
	 */
    public void review(RunData runData, String newFlag, NumberKey spectrumId) throws Exception {
        HttpServletRequest req = runData.getRequest();
        HttpSession session = req.getSession();
        DBSpectrum spectrum = DBSpectrumPeer.retrieveByPK(spectrumId);
        spectrum.setReviewFlag(newFlag);
        spectrum.save();
        GeneralUtils.logToSql("Reviewed as " + newFlag + " - " + spectrum.getPrimaryKeyAsString(), runData);
        Criteria crit = new Criteria();
        crit.add(DBUserDBSpectrumPeer.SPECTRUM_ID, spectrumId);
        NmrshiftdbUser user = null;
        if (session.getAttribute("user") != null) {
            crit.add(DBUserDBSpectrumPeer.USER_ID, (String) session.getAttribute("user"));
            user = NmrshiftdbUserPeer.retrieveByPK(new NumberKey((String) session.getAttribute("user")));
            session.removeAttribute("user");
        } else {
            crit.add(DBUserDBSpectrumPeer.USER_ID, ((NmrshiftdbUser) runData.getUser()).getUserId().toString());
            user = (NmrshiftdbUser) runData.getUser();
        }
        crit.add(DBUserDBSpectrumPeer.REVIEW_DATE, null);
        Vector v = DBUserDBSpectrumPeer.doSelect(crit);
        if (v.size() > 0) {
            DBUserDBSpectrum userSpectrum = (DBUserDBSpectrum) v.get(0);
            userSpectrum.setReviewDate(new Date());
            userSpectrum.save();
        } else {
            DBUserDBSpectrum userSpectrum = new DBUserDBSpectrum(spectrum.getSpectrumId(), user, null);
            userSpectrum.setReviewDate(new Date());
            userSpectrum.save();
        }
        setReviewFlagInBackup(spectrum, runData);
        ((NmrshiftdbUser) runData.getUser()).mymissingreviews = null;
        if (GeneralUtils.getNmrshiftdbProperty("sendreviewmail", runData).equals("true")) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss_SSSS", Locale.US);
            MailMessage email = new org.apache.turbine.util.mail.MailMessage(GeneralUtils.getSmtpServer(runData), spectrum.getContributor().getEmail(), GeneralUtils.getAdminEmail(runData), "Your input " + formatter.format(spectrum.getDate()) + " is " + (newFlag.equals("true") ? "approved" : "declined"), "The spectrum for " + spectrum.getDBMolecule().getChemicalNamesAsOneString(false) + " you submitted at " + spectrum.getDate() + " (full ID:" + spectrum.getNmrshiftdbNumber() + ") has been reviewed by <href=\"mailto:" + user.getEmail() + "\">" + user.getUserName() + "</a> (email: " + user.getEmail() + ") as " + (newFlag.equals("true") ? "valid" : "invalid") + ". For enquiries about this decision, please contact the reviewer. Thanks for your contribution!" + GeneralUtils.getEmailSignature(runData));
            if (email.send() == false) {
                Log.error("Sending email to contributor after review failed for spectrum " + spectrum.getSpectrumId());
            }
        }
        if (session.getAttribute("reviewbyurl") != null) {
            session.removeAttribute("reviewbyurl");
        }
        if (newFlag.equals(NmrshiftdbConstants.TRUE) && spectrum.getDBSpectrumType().getName().equals("13C")) {
            String check = GeneralUtils.checkAgainstCdk(spectrum.getDBMolecule());
            if (!check.equals("")) GeneralUtils.sendEmailToEventReceivers("Accepted value is outside of cdk range", check, runData, 0);
        }
    }
}
