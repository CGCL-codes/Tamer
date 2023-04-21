package sk.naive.talker.command;

import sk.naive.talker.*;
import sk.naive.talker.adapter.TagConsts;
import sk.naive.talker.props.PropertyStoreException;
import sk.naive.talker.message.*;
import sk.naive.talker.util.*;
import java.rmi.RemoteException;
import java.util.*;

/**
 *
 * @author <a href="mailto:richter@bgs.sk">Richard Richter</a>
 * @version $Revision: 1.23 $ $Date: 2005/01/18 20:51:30 $
 */
public class Ignore extends AbstractCommand {

    /** User property storing set of ignored category objects. */
    public static final String UPROP_IGNORED = "ignored";

    public void exec() throws CommandException, RemoteException, PropertyStoreException {
        Map ctx = user.getProperties();
        if (params != null && params.length() > 0) {
            String catName = Utils.findFirstInCollection(params, Category.allCategoryNames(), true);
            if (catName == null) {
                ctx.put(DefaultMessageFactory.CTXKEY_VAL, params);
                StringBuilder sb = new StringBuilder(getString("ignore.unknownCategory", ctx));
                sb.append(status(user));
                user.send(sb.toString());
            } else {
                Category cat = Category.forName(catName);
                ctx.put(DefaultMessageFactory.CTXKEY_VAL, catName);
                Set ignored = (Set) user.get(UPROP_IGNORED);
                if (ignored.contains(cat)) {
                    ignored.remove(cat);
                    user.send(getString("ignore.notIgnoring", user.getProperties()));
                } else {
                    ignored.add(cat);
                    user.send(getString("ignore.ignoring", user.getProperties()));
                }
                user.set(UPROP_IGNORED, ignored);
            }
        } else {
            StringBuilder sb = new StringBuilder(getString("ignore.whatCategory", ctx));
            sb.append(status(user));
            user.send(sb.toString());
        }
    }

    private String status(User u) throws PropertyStoreException {
        StringBuilder sbIgnored = new StringBuilder();
        StringBuilder sbNotIgnored = new StringBuilder();
        Set ignored = (Set) u.get(UPROP_IGNORED);
        for (Category cat : Category.allCategories()) {
            if (ignored.contains(cat)) {
                Utils.addToBuffer(sbIgnored, cat.toString(), ", ");
            } else {
                Utils.addToBuffer(sbNotIgnored, cat.toString(), ", ");
            }
        }
        if (sbIgnored.length() == 0) {
            sbIgnored.append(getString("message.none", u.getProperties()));
        }
        if (sbNotIgnored.length() == 0) {
            sbNotIgnored.append(getString("message.none", u.getProperties()));
        }
        sbIgnored.insert(0, getString("ignore.statusIgnored", u.getProperties()));
        sbNotIgnored.insert(0, getString("ignore.statusNotIgnored", u.getProperties()));
        sbIgnored.append(Utils.tag(TagConsts.BR));
        sbNotIgnored.append(Utils.tag(TagConsts.BR));
        return sbIgnored.toString() + sbNotIgnored.toString();
    }
}
