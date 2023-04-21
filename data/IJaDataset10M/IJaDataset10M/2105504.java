package wotlas.libs.net;

/** This interface contains only one variable that indicates the version of
 *  the Network Engine. When the client send a ClientRegisterMessage the message
 *  contains his local version number. We check the number is the same in the
 *  ClientRegisterMsgBehaviour.
 * 
 * @author Aldiss
 */
public interface NetEngineVersion {

    public static final float VERSION = 1.3f;
}
