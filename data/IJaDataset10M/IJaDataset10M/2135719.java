package org.exist.xquery.functions.xmldb;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;
import org.exist.dom.QName;
import org.exist.security.Account;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;
import org.exist.security.SecurityManager;
import org.exist.xquery.value.ValueSequence;
import org.exist.xquery.value.StringValue;

/**
 * @author Adam Retter <adam@existsolutions.com>
 */
public class XMLDBGetUsers extends BasicFunction {

    protected static final Logger logger = Logger.getLogger(XMLDBGetUsers.class);

    public static final FunctionSignature signature = new FunctionSignature(new QName("get-users", XMLDBModule.NAMESPACE_URI, XMLDBModule.PREFIX), "Returns the list of users in the group", new SequenceType[] { new FunctionParameterSequenceType("group-name", Type.STRING, Cardinality.EXACTLY_ONE, "The group-name") }, new FunctionReturnSequenceType(Type.STRING, Cardinality.ZERO_OR_MORE, "The list of users in the group identified by $group-name"));

    public XMLDBGetUsers(XQueryContext context) {
        super(context, signature);
    }

    @Override
    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        String groupName = args[0].getStringValue();
        SecurityManager manager = context.getBroker().getBrokerPool().getSecurityManager();
        List<Account> users = manager.getGroupMembers(groupName);
        Collections.sort(users, new Comparator<Account>() {

            @Override
            public int compare(Account t, Account t1) {
                return t.getUsername().compareTo(t1.getUsername());
            }
        });
        ValueSequence userNames = new ValueSequence(users.size());
        for (Account user : users) {
            if (user.hasGroup(groupName)) {
                userNames.add(new StringValue(user.getName()));
            }
        }
        return userNames;
    }
}
