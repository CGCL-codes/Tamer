package de.ios.framework.remote.auth.sv.co;

import java.rmi.*;
import java.util.*;
import de.ios.framework.basic.*;
import de.ios.framework.remote.sv.co.*;
import de.ios.framework.remote.auth.sv.co.*;

/**
 * MemberLinkController deals with a set of MemberLinks
 * within the kontor framework.
 *
 * @see MemberLink
 * @see MemberFactory
 * @see Member
 */
public interface MemberRightLinkController extends BasicController {

    /**
   * Get the Member with the matching Object-ID
   * @exception de.ios.kontor.utils.KontorException if the loading of Order failed.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public MemberRightLink getMemberRightLinkByOId(long oid) throws KontorException, RemoteException;

    /**
   * Find MemberRightLinks by some of it's Attributes
   * @exception de.ios.kontor.utils.KontorException if the loading of MemberRightLinks failed.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public Iterator getMemberRightLinks(MemberRightLink comp) throws KontorException, RemoteException;

    /**
   * Create a new MemberRightLink.
   * @param member         the Member.
   * @param right          the Right.
   * @param adds	   if true, the Right gets added, if false, the Right gets removed, null: illegal (gets rejected by the DB).
   * @param serverName     the Name of the Server to which the Right is limited.
   * @param serverIPNet    the IPNet of the Client to which the Right is limited.
   * @exception de.ios.kontor.utils.KontorException if the creation of MemberRightLink failed.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public MemberRightLink createMemberRightLink(Member member, MemberRight right, Boolean adds, String serverName, String serverIPNet) throws KontorException, RemoteException;

    /**
   * Get all specified MemberRight(Link)s.
   * @param memberId the ID of a Members.
   * @param rightName a RightName(-Prefix).
   * @return an Iterator containing all specified Right(Link)s.
   * @exception de.ios.framework.basic.ServerException if the operation failed due to a Server-Error.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public Iterator getMemberRightLinkDCs(Long memberId, String rightName) throws de.ios.framework.basic.ServerException, RemoteException;
}

;
