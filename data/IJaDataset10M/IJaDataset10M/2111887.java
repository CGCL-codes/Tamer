package lif.webclient.validator;

import lif.core.Constants;
import lif.core.dao.DataAccessException;
import lif.core.dao.DataObjectNotFoundException;
import lif.core.dao.DuplicateDataObjectException;
import lif.core.domain.DbProfile;
import lif.core.service.ProfileService;
import lif.core.util.xmlvalidators.XMLValidator;
import lif.webclient.view.ProfileUploadBean;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.User;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

public class ProfileUploadValidator implements Validator {

    private Logger logger = Logger.getLogger(ProfileUploadValidator.class);

    private boolean isDebugEnabled = logger.isDebugEnabled();

    private ProfileService profileService;

    private String profileSource;

    private XMLValidator xmlValidator;

    public boolean supports(Class aClass) {
        return aClass.equals(ProfileUploadBean.class);
    }

    public void validate(Object command, Errors errors) {
        ProfileUploadBean bean = (ProfileUploadBean) command;
        MultipartFile profile = bean.getFile();
        debug("in the profile validator");
        System.out.println("[" + bean.toString() + "]");
        if (profile == null || profile.getSize() == 0) {
            errors.rejectValue("file", "validator.profile.upload.emptyPath", "empty file");
        }
        String currentUser = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        System.out.println("[" + currentUser + "]");
        if (profile != null && profile.getOriginalFilename().length() != 0) {
            try {
                DbProfile dbProfile = profileService.getDbProfile(bean.getProfileName());
                debug("at the profile validator db profile is is [" + dbProfile.toString() + "]");
                System.out.println("at the profile validator db profile is is [" + dbProfile.toString() + "]");
                if (!dbProfile.isOwner(currentUser)) {
                    errors.rejectValue("file", "validator.profile.upload.unauthorized", "User has no rights");
                }
            } catch (DuplicateDataObjectException e) {
                debug("DuplicateDataObjectException acocor at profile validation  [" + e.toString() + "]");
            } catch (DataObjectNotFoundException e) {
                errors.rejectValue("file", "validator.profile.upload.unauthorized", "User has no rights");
            } catch (DataAccessException e) {
                debug("DataAccessException acocor at profile validation  [" + e.toString() + "]");
            }
        }
        assert profile != null;
        if ((!profile.getContentType().equals("application/zip")) && (!profile.getContentType().equals("application/x-zip-compressed")) && (!profile.getContentType().equals("application/octet-stream")) && (!profile.getContentType().equals("application/x-forcedownload"))) {
            errors.rejectValue("file", "validator.profile.upload.invalidFileType", "Uploaded file is not a zip file");
        }
        if (errors.getErrorCount() == 0) {
            profileService.profileSave(profileSource, profile);
            profileService.extractProfile(profileSource, bean.getFileName(), bean.getProfileName());
            String profileXmlFilePath = profileSource + File.separator + bean.getProfileName() + File.separator + Constants.profileMappingXml;
            if (xmlValidator.validateXMLFile(profileXmlFilePath, Constants.PROFILE_MAPPING_SCHEMA)) {
                debug("the profile [" + bean.getProfileName() + "] is valid");
                sendMail(currentUser, bean.getProfileName());
            } else {
                boolean success = profileService.deleteProfileDir(bean.getProfileName());
                if (success) {
                    debug("deleted successfully");
                    System.out.println("deleted successfully");
                } else {
                    errors.rejectValue("file", "validator.profile.upload.deletion.error", bean.getProfileName() + " profile deletion error");
                }
                errors.rejectValue("file", "validator.profile.upload.schema.error", "Profile_mapping.xml is not according to the Schema.Please check with: http://localhost:8080/webclient/schemas/profile_mapping_schema.xsd");
            }
        }
    }

    public void setProfileService(ProfileService profileService) {
        this.profileService = profileService;
    }

    public void setProfileSource(String profileSource) {
        this.profileSource = profileSource;
    }

    public void setXmlValidator(XMLValidator xmlValidator) {
        this.xmlValidator = xmlValidator;
    }

    private void debug(String str) {
        if (isDebugEnabled) {
            logger.debug(str);
        }
    }

    private void sendMail(String user, String profile) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "cse.mrt.ac.lk");
            Session session1 = Session.getDefaultInstance(props, null);
            Message message = new MimeMessage(session1);
            message.setFrom(new InternetAddress("lifproject-request@cse.mrt.ac.lk"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("niranjan.uom@gmail.com", false));
            message.setSubject("News: New Profile Uploaded");
            String userDetails = user + " has uploaed a profile with name " + profile + "<br>";
            userDetails += "Thank you" + "<br>";
            message.setContent(userDetails, "text/html;charset=utf-8");
            Transport.send(message);
            System.out.println("message sent successfully");
        } catch (AddressException e) {
            e.printStackTrace();
            System.out.println("error");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("error");
        }
    }
}
