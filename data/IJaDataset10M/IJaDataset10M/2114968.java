package org.davic.media;

/**
 * This exception indicates that the source can not be accessed in order to
 * reference the new content, the source has not been accepted.
 */
public class NotAuthorizedMediaException extends NotAuthorizedException implements org.davic.mpeg.NotAuthorizedInterface {

    /**
	 * Constructor for exception due to failure accessing an MPEG service
	 * @param s the service which could not be accessed
	 * @param reason the reason why the service could not be accessed
	 */
    public NotAuthorizedMediaException(org.davic.mpeg.Service s, int reason) {
    }

    /**
	 * Constructor for exception due to failure accessing one or more MPEG
	 * elementary streams The caller of this constructor is responsible for
	 * ensuring the two arrays provided as parameters are the same size. The
	 * implementation is not expected to check this.
	 * <p>
	 * Use of the constructor NotAuthorizedMediaException(ElementaryStream[] e,
	 * int[] reason) will result in the major reason for each elementary stream
	 * being the one specified in the reason parameter to the method and the
	 * minor reason being OTHER as defined in NotAuthorizedInterface.
	 * @param e the elementary streams which could not be accessed
	 * @param reason the reason why the exception was thrown for each elementary
	 *            stream
	 */
    public NotAuthorizedMediaException(org.davic.mpeg.ElementaryStream[] e, int reason[]) {
    }

    /**
	 * Constructor for exception due to failure accessing one or more MPEG
	 * elementary streams The caller of this constructor is responsible for
	 * ensuring the three arrays provided as parameters are the same size. The
	 * implementation is not expected to check this.
	 * @param e the elementary streams which could not be accessed
	 * @param major_reason the major reason why the exception was thrown for
	 *            each elementary stream
	 * @param minor_reason the minor reason why the exception was thrown for
	 *            each elementary stream
	 */
    public NotAuthorizedMediaException(org.davic.mpeg.ElementaryStream[] e, int major_reason[], int minor_reason[]) {
    }

    /**
	 * Constructor for exception due to failure accessing an MPEG service
	 * @param s the service which could not be accessed
	 * @param major_reason the major reason why the service could not be
	 *            accessed
	 * @param minor_reason the minor reason why the service could not be
	 *            accessed
	 * @since MHP 1.0.2
	 */
    public NotAuthorizedMediaException(org.davic.mpeg.Service s, int major_reason, int minor_reason) {
    }

    /**
	 * @return SERVICE or ELEMENTARY_STREAM to indicate that either a service
	 *         (MPEG program) or one or more elementary streams could not be
	 *         descrambled. Implements method from
	 *         org.davic.mpeg.NotAuthorizedInterface.
	 */
    public int getType() {
        return 0;
    }

    /**
	 * If getType() returns SERVICE, then this method returns the Service that
	 * could not be descrambled. Otherwise it returns null. Implements method
	 * from org.davic.mpeg.NotAuthorizedInterface.
	 * @return either the Service that could not be descrambled or null
	 */
    public org.davic.mpeg.Service getService() {
        return null;
    }

    /**
	 * If getType() returns ELEMENTARY_STREAM, then this method returns the set
	 * of ElementaryStreams that could not be descrambled. Otherwise it returns
	 * null. Implements method from org.davic.mpeg.NotAuthorizedInterface.
	 * @return either the set of ElementaryStreams that could not be descrambled
	 *         or null
	 */
    public org.davic.mpeg.ElementaryStream[] getElementaryStreams() {
        return null;
    }

    /**
	 * Returns the reason(s) why descrambling was not possible. Implements
	 * method from org.davic.mpeg.NotAuthorizedInterface.
	 * @param index If the component to which access failed is a Service, index
	 *            shall be 0. Otherwise index shall refer to one stream in the
	 *            set returnedby getElementaryStreams().
	 * @return an array of length 2 where the first element of the array is the
	 *         major reason and the second element of the array is the minor
	 *         reason.
	 * @exception IndexOutOfBoundsException If the component to which access
	 *                failed is a Service, this exception will be thrown if
	 *                index is non zero. If the component(s) to which access
	 *                failed was a (set of) elementary streams then this
	 *                exception will be thrown where index is beyond the size of
	 *                the array returned by getElementaryStreams.
	 * @see org.davic.mpeg.NotAuthorizedInterface#getElementaryStreams
	 */
    public int[] getReason(int index) throws java.lang.IndexOutOfBoundsException {
        return null;
    }
}
