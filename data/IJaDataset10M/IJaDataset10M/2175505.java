package org.exist.xquery.functions.securitymanager;

import java.util.Collections;
import java.util.List;
import org.exist.dom.QName;
import org.exist.security.SecurityManager;
import org.exist.security.Subject;
import org.exist.storage.DBBroker;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.StringValue;
import org.exist.xquery.value.Type;
import org.exist.xquery.value.ValueSequence;

/**
 *
 * @author Adam Retter <adam@existsolutions.com>
 */
public class FindUserFunction extends BasicFunction {

    private static final QName qnFindUsersByUsername = new QName("find-users-by-username", SecurityManagerModule.NAMESPACE_URI, SecurityManagerModule.PREFIX);

    private static final QName qnFindUsersByName = new QName("find-users-by-name", SecurityManagerModule.NAMESPACE_URI, SecurityManagerModule.PREFIX);

    private static final QName qnFindUsersByNamePart = new QName("find-users-by-name-part", SecurityManagerModule.NAMESPACE_URI, SecurityManagerModule.PREFIX);

    public static final FunctionSignature signatures[] = { new FunctionSignature(qnFindUsersByUsername, "Finds users whoose username starts with a matching string", new SequenceType[] { new FunctionParameterSequenceType("starts-with", Type.STRING, Cardinality.EXACTLY_ONE, "The starting string against which to match usernames") }, new FunctionReturnSequenceType(Type.STRING, Cardinality.ZERO_OR_MORE, "The list of matching usernames")), new FunctionSignature(qnFindUsersByName, "Finds users whoose personal name starts with a matching string", new SequenceType[] { new FunctionParameterSequenceType("starts-with", Type.STRING, Cardinality.EXACTLY_ONE, "The starting string against which to match a personal name") }, new FunctionReturnSequenceType(Type.STRING, Cardinality.ZERO_OR_MORE, "The list of matching usernames")), new FunctionSignature(qnFindUsersByNamePart, "Finds users whoose first name or last name starts with a matching string", new SequenceType[] { new FunctionParameterSequenceType("starts-with", Type.STRING, Cardinality.EXACTLY_ONE, "The starting string against which to match a first or last name") }, new FunctionReturnSequenceType(Type.STRING, Cardinality.ZERO_OR_MORE, "The list of matching usernames")) };

    public FindUserFunction(XQueryContext context, FunctionSignature signature) {
        super(context, signature);
    }

    @Override
    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        DBBroker broker = getContext().getBroker();
        Subject currentUser = broker.getSubject();
        if (currentUser.getName().equals(SecurityManager.GUEST_USER)) {
            throw new XPathException("You must be an authenticated user");
        }
        final String startsWith = args[0].getStringValue();
        SecurityManager securityManager = broker.getBrokerPool().getSecurityManager();
        List<String> usernames;
        if (isCalledAs(qnFindUsersByUsername.getLocalName())) {
            usernames = securityManager.findUsernamesWhereUsernameStarts(currentUser, startsWith);
        } else if (isCalledAs(qnFindUsersByName.getLocalName())) {
            usernames = securityManager.findUsernamesWhereNameStarts(currentUser, startsWith);
        } else if (isCalledAs(qnFindUsersByNamePart.getLocalName())) {
            usernames = securityManager.findUsernamesWhereNamePartStarts(currentUser, startsWith);
        } else {
            throw new XPathException("Unknown function");
        }
        Collections.sort(usernames);
        Sequence result = new ValueSequence();
        for (String username : usernames) {
            result.add(new StringValue(username));
        }
        return result;
    }
}
