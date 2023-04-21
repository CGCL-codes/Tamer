package vqwiki;

import java.io.*;
import java.util.*;
import vqwiki.WikiMail;
import vqwiki.WikiException;

/**
 * Stores a list of usernames and their registered email addresses so
 * that users may set notifications and reminders per topic page. Users
 * must set a canonical username and provide a valid email address.
 * An email will then be sent to the supplied address with a hyperlink
 * containing a validation key. The key is then checked against the
 * list of registered names and confirmed, at which point the user
 * is allowed to set notifications and reminders. This is a file-based
 * implementation of the WikiMembers interface.
 * 
 * @author Robert E Brewer
 * @version 0.1
 */
public class FileWikiMembers implements WikiMembers {

    private static Random rn = new Random();

    private static final int KEY_LENGTH = 20;

    private File memberFile = new File(Environment.dir() + "member.hashtable");

    private Hashtable memberTable;

    /**
 * Constructor for the members list.
 * 
 * @exception ClassNotFoundException, IOException if the members file could not be read
 */
    public FileWikiMembers() throws ClassNotFoundException, IOException {
        if (!memberFile.exists()) {
            File dirFile = new File(Environment.getInstance().dir());
            if (!dirFile.exists()) dirFile.mkdir();
            memberTable = new Hashtable();
        } else {
            ObjectInputStream in = null;
            try {
                in = new ObjectInputStream(new FileInputStream(memberFile));
            } catch (IOException e) {
                throw e;
            }
            memberTable = (Hashtable) in.readObject();
            in.close();
        }
    }

    /**
 * Confirms a member (that is, gives them the ability to set notifications and reminders)
 * if the supplied key matches the key generated when the account was created. It is expected
 * that another class or JSP page will call this function at the request of a user.
 *
 * @param username  the name of the user to confirm
 * @param key the key to compare to the key generated when the account was created
 * @return boolean  True if the user is in the list.
*/
    public boolean confirmMembership(String username, String key) {
        WikiMember aMember = findMemberByName(username);
        if (aMember.isConfirmed()) return true;
        if (!aMember.checkKey(key)) return false;
        aMember.confirm();
        writeMemberTable();
        return writeMemberTable();
    }

    /**
 * Finds a WikiMember object in the Member collection using the username.
 *
 * @param username  the name of the user to find
 * @return WikiMember  the Member object for the specified user
*/
    public WikiMember findMemberByName(String username) {
        WikiMember aMember;
        if (memberTable.containsKey(username)) {
            aMember = (WikiMember) memberTable.get(username);
        } else {
            aMember = new WikiMember(username);
        }
        return aMember;
    }

    /**
 * Add a user account to the Members collection. A key will be generated and sent via
 * email to the specified address in a hyperlink, which the user can then visit
 * to confirm the membership request.
 * 
 * @param username  the name of the user for whom membership is requested
 * @param email  the email address of the user for whom membership is requested
 * @param requestURL  the JSP page to which the user should be directed
 * in the confirmation email. For example, http://www.mybogusdomain.com/vqwiki/jsp/confirm.jsp
 * @return boolean  true if the user account has been added, false if an account already exists for this username or the member file could not be written
 * @exception WikiException if the mailer could not be instantiated
*/
    public synchronized boolean requestMembership(String username, String email, String requestURL) throws WikiException {
        if (memberTable.containsKey(username)) return false;
        WikiMember aMember = new WikiMember(username, email);
        byte b[] = new byte[KEY_LENGTH];
        for (int i = 0; i < KEY_LENGTH; i++) b[i] = (byte) (rn.nextInt(26) + 65);
        String newKey = new String(b);
        aMember.setKey(newKey);
        String BodyText = username + ", your membership request has been received. " + "Please follow this link to confirm your request.\n\n" + requestURL + "?userName=" + username + "&key=" + newKey;
        WikiMail mailer = WikiMail.getInstance();
        String replyAddress = Environment.getInstance().getReplyAddress();
        mailer.sendMail(replyAddress, email, "Wiki Membership", BodyText);
        memberTable.put(username, aMember);
        return writeMemberTable();
    }

    /**
 * Removes a WikiMember from the members list.
 *
 * @param username  the name of the user to remove
 * @return boolean  true if the operation completed successfully, false if the member file could not be written
*/
    public synchronized boolean removeMember(String username) {
        if (!memberTable.containsKey(username)) return false;
        memberTable.remove(username);
        return writeMemberTable();
    }

    private boolean writeMemberTable() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(memberFile));
            out.writeObject(memberTable);
            out.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
