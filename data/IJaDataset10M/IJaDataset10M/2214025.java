package wsdir.server.axis;

import wsdir.core.DSProfile;
import wsdir.core.DirectoryDescription;
import atomik.core.AConcept;
import atomik.interactions.AInteractionException;
import atomik.syntax.ASyntaxException;

/**
 * During the Call of the methods register, modify and search, a java Object is passed as parameter.
 * SOAP doesn't allow generic java objects, so they need to be first transformed into a SL0 String
 * before beeing sent over a SOAP message.
 */
public class SL0MimeObject implements java.io.Serializable {

    public static final String MIME_ACONCEPT = "x_wsdir_aconcept";

    public static final String MIME_DSPROFILE = "x_wsdir_dsprofile";

    public static final String MIME_DIRECTORYDESCRIPTION = "x_wsdir_directorydescription";

    public static final String MIME_NULL = "x_wsdir_null";

    /** The SL0 representation of the object */
    public String sl0_string;

    /** The Mime type of the object */
    public String mime;

    /** Empty Constructor, do not remove */
    public SL0MimeObject() {
    }

    /** 
	 * Construct a SL0Mime Object from a Java AConcept/DSProfile/DirectoryDescription Object
	 * @param obj the java object to transform
	 * @throws MalformedSL0MimeObjectException if something went wrong
	 */
    public SL0MimeObject(Object obj) throws MalformedSL0MimeObjectException {
        try {
            if (obj instanceof AConcept) {
                sl0_string = new AtomikHelper().getSL0StringFromAConcept((AConcept) obj);
                mime = new String(MIME_ACONCEPT);
            } else if (obj instanceof DSProfile) {
                sl0_string = new AtomikHelper().getSL0StringFromDSProfile((DSProfile) obj);
                mime = new String(MIME_DSPROFILE);
            } else if (obj instanceof DirectoryDescription) {
                sl0_string = new AtomikHelper().getSL0StringFromDirectoryDescription((DirectoryDescription) obj);
                mime = new String(MIME_DIRECTORYDESCRIPTION);
            } else if (obj == null) {
                sl0_string = new AtomikHelper().getSL0StringNull();
                mime = new String(MIME_NULL);
            } else {
                throw new MalformedSL0MimeObjectException("Trying to get an sl0 String from an object that is not a DSProfile/DirectoryDescription/AConcept/null type : " + obj.getClass().toString());
            }
        } catch (ASyntaxException e) {
            throw new MalformedSL0MimeObjectException("ASyntaxException occured. Could not create an sl0Object from the following DSProfile: " + obj.toString());
        } catch (AInteractionException e) {
            throw new MalformedSL0MimeObjectException("AInteractionException occured. Could not create an sl0Object from the following DSProfile: " + obj.toString());
        }
    }

    /**
	 * Transform a SL0Mime object into a Java AConcept/DSProfile/DirectoryDescription object
	 * @return the java object
	 * @throws MalformedSL0MimeObjectException if something went wrong...
	 */
    public Object toObject() throws MalformedSL0MimeObjectException {
        Object result = null;
        try {
            if (mime.equals(MIME_ACONCEPT)) {
                result = new AtomikHelper().getAConceptFromSL0String(sl0_string);
            } else if (mime.equals(MIME_DSPROFILE)) {
                result = new AtomikHelper().getDSProfileFromSL0String(sl0_string);
            } else if (mime.equals(MIME_DIRECTORYDESCRIPTION)) {
                result = new AtomikHelper().getDirectoryDescriptionFromSL0String(sl0_string);
            } else if (mime.equals(MIME_NULL)) {
                result = new AtomikHelper().getNullFromSL0String(sl0_string);
            } else {
                throw new MalformedSL0MimeObjectException("Trying to get an object from a sl0 String that is not a DSProfile/DirectoryDescription/AConcept mime : " + mime);
            }
        } catch (ASyntaxException e) {
            throw new MalformedSL0MimeObjectException("ASyntaxExpression Occured. Could not get an Object from the following sl0 String: " + sl0_string + ", mime: " + mime);
        }
        return result;
    }
}
