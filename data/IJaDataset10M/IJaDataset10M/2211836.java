package org.likken.core;

import org.likken.core.command.Command;
import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.*;

/**
 * @author Stephane Boisson
 * @version $Revision: 1.4 $ $Date: 2001/03/02 18:09:08 $
 */
public class LikkenSession {

    private static Schema schema;

    private static EntryFactory entryFactory;

    private LikkenContext context;

    private InitialLdapContext connection;

    public LikkenSession(final LikkenContext aContext) throws DirectoryException {
        context = aContext;
        if (context == null) {
            throw new DirectoryException("null context");
        }
        if (schema == null) {
            schema = new Schema();
            schema.loadAttributeTypeFromResource("/org/likken/core/resources/schema/rfc2256.at");
            schema.loadAttributeTypeFromResource("/org/likken/core/resources/schema/rfc2252.at");
            schema.loadAttributeTypeFromResource("/org/likken/core/resources/schema/rfc2798.at");
            schema.loadAttributeTypeFromResource("/org/likken/core/resources/schema/rfc1274.at");
            schema.loadAttributeTypeFromResource("/org/likken/core/resources/schema/rfc2307.at");
            schema.loadObjectClassFromResource("/org/likken/core/resources/schema/rfc2256.oc");
            schema.loadObjectClassFromResource("/org/likken/core/resources/schema/rfc2252.oc");
            schema.loadObjectClassFromResource("/org/likken/core/resources/schema/rfc2798.oc");
            schema.loadObjectClassFromResource("/org/likken/core/resources/schema/rfc1274.oc");
            schema.loadObjectClassFromResource("/org/likken/core/resources/schema/rfc2307.oc");
        }
        if (entryFactory == null) {
            entryFactory = new EntryFactory(schema);
        }
    }

    protected InitialLdapContext getConnection() throws DirectoryException {
        if (connection == null) {
            try {
                connection = new InitialLdapContext(context.getEnvironment(), null);
            } catch (final AuthenticationException e) {
                throw new PermissionDeniedException("Authentication failed", e);
            } catch (final NameNotFoundException e) {
                throw new PermissionDeniedException("Authentication failed", e);
            } catch (final NamingException e) {
                throw new DirectoryException(e);
            }
        }
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (final NamingException e) {
                e.printStackTrace();
            } finally {
                connection = null;
            }
        }
    }

    protected void finalize() {
        close();
    }

    public Schema getSchema() {
        return schema;
    }

    public List listEntries() throws DirectoryException {
        SearchControls ctls = new SearchControls();
        String[] attrIDs = { "dn" };
        ctls.setReturningAttributes(attrIDs);
        ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        try {
            ArrayList entries = new ArrayList();
            DirContext ctx = getConnection();
            try {
                NamingEnumeration answer = ctx.search("", "(cn=*)", ctls);
                while (answer.hasMore()) {
                    SearchResult sr = (SearchResult) answer.next();
                    entries.add(new DistinguishedName(sr.getName()));
                }
            } finally {
                close();
            }
            return entries;
        } catch (final NoPermissionException e) {
            throw new PermissionDeniedException("Cannot list entries", e);
        } catch (final NamingException e) {
            throw new DirectoryException(e);
        }
    }

    public Entry getEntry(final String aDN) throws DirectoryException {
        return getEntry(new DistinguishedName(aDN));
    }

    public Entry getEntry(final DistinguishedName aDN) throws DirectoryException {
        try {
            DirContext ctx = getConnection();
            try {
                return entryFactory.makeCompleteEntry(ctx, aDN, context.getSuffix());
            } finally {
                close();
            }
        } catch (final NoPermissionException e) {
            throw new PermissionDeniedException("Cannot access entry", e);
        } catch (final NamingException e) {
            throw new DirectoryException(e);
        }
    }

    public void modify(final Entry entry, final Command[] commands) throws DirectoryException {
        System.err.println("    modify " + entry.getDN());
        try {
            DistinguishedName relativeDN = entry.getDN().withoutSuffix(context.getSuffix());
            DirContext ctx = getConnection();
            try {
                ModificationItem[] mods = new ModificationItem[commands.length];
                for (int i = 0; i < mods.length; ++i) {
                    mods[i] = commands[i].getModificationItem();
                    System.err.println("    " + mods[i]);
                }
                ctx.modifyAttributes(relativeDN.toString(), mods);
            } finally {
                close();
            }
        } catch (final NoPermissionException e) {
            throw new PermissionDeniedException("Cannot update entry", e);
        } catch (final NamingException e) {
            throw new DirectoryException(e);
        }
    }
}
