package com.unboundid.ldap.sdk.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.LinkedHashMap;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.util.NotMutable;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;
import static com.unboundid.ldap.sdk.schema.SchemaMessages.*;
import static com.unboundid.util.StaticUtils.*;
import static com.unboundid.util.Validator.*;

/**
 * This class provides a data structure that describes an LDAP matching rule
 * schema element.
 */
@NotMutable()
@ThreadSafety(level = ThreadSafetyLevel.COMPLETELY_THREADSAFE)
public final class MatchingRuleDefinition extends SchemaElement {

    /**
   * The serial version UID for this serializable class.
   */
    private static final long serialVersionUID = 8214648655449007967L;

    private final boolean isObsolete;

    private final Map<String, String[]> extensions;

    private final String description;

    private final String matchingRuleString;

    private final String oid;

    private final String syntaxOID;

    private final String[] names;

    /**
   * Creates a new matching rule from the provided string representation.
   *
   * @param  s  The string representation of the matching rule to create, using
   *            the syntax described in RFC 4512 section 4.1.3.  It must not be
   *            {@code null}.
   *
   * @throws  LDAPException  If the provided string cannot be decoded as a
   *                         matching rule definition.
   */
    public MatchingRuleDefinition(final String s) throws LDAPException {
        ensureNotNull(s);
        matchingRuleString = s.trim();
        final int length = matchingRuleString.length();
        if (length == 0) {
            throw new LDAPException(ResultCode.DECODING_ERROR, ERR_MR_DECODE_EMPTY.get());
        } else if (matchingRuleString.charAt(0) != '(') {
            throw new LDAPException(ResultCode.DECODING_ERROR, ERR_MR_DECODE_NO_OPENING_PAREN.get(matchingRuleString));
        }
        int pos = skipSpaces(matchingRuleString, 1, length);
        StringBuilder buffer = new StringBuilder();
        pos = readOID(matchingRuleString, pos, length, buffer);
        oid = buffer.toString();
        final ArrayList<String> nameList = new ArrayList<String>(1);
        String descr = null;
        Boolean obsolete = null;
        String synOID = null;
        final Map<String, String[]> exts = new LinkedHashMap<String, String[]>();
        while (true) {
            pos = skipSpaces(matchingRuleString, pos, length);
            final int tokenStartPos = pos;
            while ((pos < length) && (matchingRuleString.charAt(pos) != ' ')) {
                pos++;
            }
            final String token = matchingRuleString.substring(tokenStartPos, pos);
            final String lowerToken = toLowerCase(token);
            if (lowerToken.equals(")")) {
                if (pos < length) {
                    throw new LDAPException(ResultCode.DECODING_ERROR, ERR_MR_DECODE_CLOSE_NOT_AT_END.get(matchingRuleString));
                }
                break;
            } else if (lowerToken.equals("name")) {
                if (nameList.isEmpty()) {
                    pos = skipSpaces(matchingRuleString, pos, length);
                    pos = readQDStrings(matchingRuleString, pos, length, nameList);
                } else {
                    throw new LDAPException(ResultCode.DECODING_ERROR, ERR_MR_DECODE_MULTIPLE_ELEMENTS.get(matchingRuleString, "NAME"));
                }
            } else if (lowerToken.equals("desc")) {
                if (descr == null) {
                    pos = skipSpaces(matchingRuleString, pos, length);
                    buffer = new StringBuilder();
                    pos = readQDString(matchingRuleString, pos, length, buffer);
                    descr = buffer.toString();
                } else {
                    throw new LDAPException(ResultCode.DECODING_ERROR, ERR_MR_DECODE_MULTIPLE_ELEMENTS.get(matchingRuleString, "DESC"));
                }
            } else if (lowerToken.equals("obsolete")) {
                if (obsolete == null) {
                    obsolete = true;
                } else {
                    throw new LDAPException(ResultCode.DECODING_ERROR, ERR_MR_DECODE_MULTIPLE_ELEMENTS.get(matchingRuleString, "OBSOLETE"));
                }
            } else if (lowerToken.equals("syntax")) {
                if (synOID == null) {
                    pos = skipSpaces(matchingRuleString, pos, length);
                    buffer = new StringBuilder();
                    pos = readOID(matchingRuleString, pos, length, buffer);
                    synOID = buffer.toString();
                } else {
                    throw new LDAPException(ResultCode.DECODING_ERROR, ERR_MR_DECODE_MULTIPLE_ELEMENTS.get(matchingRuleString, "SYNTAX"));
                }
            } else if (lowerToken.startsWith("x-")) {
                pos = skipSpaces(matchingRuleString, pos, length);
                final ArrayList<String> valueList = new ArrayList<String>();
                pos = readQDStrings(matchingRuleString, pos, length, valueList);
                final String[] values = new String[valueList.size()];
                valueList.toArray(values);
                if (exts.containsKey(token)) {
                    throw new LDAPException(ResultCode.DECODING_ERROR, ERR_MR_DECODE_DUP_EXT.get(matchingRuleString, token));
                }
                exts.put(token, values);
            } else {
                throw new LDAPException(ResultCode.DECODING_ERROR, ERR_MR_DECODE_UNEXPECTED_TOKEN.get(matchingRuleString, token));
            }
        }
        description = descr;
        syntaxOID = synOID;
        if (syntaxOID == null) {
            throw new LDAPException(ResultCode.DECODING_ERROR, ERR_MR_DECODE_NO_SYNTAX.get(matchingRuleString));
        }
        names = new String[nameList.size()];
        nameList.toArray(names);
        isObsolete = (obsolete != null);
        extensions = Collections.unmodifiableMap(exts);
    }

    /**
   * Creates a new matching rule with the provided information.
   *
   * @param  oid          The OID for this matching rule.  It must not be
   *                      {@code null}.
   * @param  names        The set of names for this matching rule.  It may be
   *                      {@code null} or empty if the matching rule should only
   *                      be referenced by OID.
   * @param  description  The description for this matching rule.  It may be
   *                      {@code null} if there is no description.
   * @param  isObsolete   Indicates whether this matching rule is declared
   *                      obsolete.
   * @param  syntaxOID    The syntax OID for this matching rule.  It must not be
   *                      {@code null}.
   * @param  extensions   The set of extensions for this matching rule.
   *                      It may be {@code null} or empty if there should not be
   *                      any extensions.
   */
    public MatchingRuleDefinition(final String oid, final String[] names, final String description, final boolean isObsolete, final String syntaxOID, final Map<String, String[]> extensions) {
        ensureNotNull(oid, syntaxOID);
        this.oid = oid;
        this.description = description;
        this.isObsolete = isObsolete;
        this.syntaxOID = syntaxOID;
        if (names == null) {
            this.names = NO_STRINGS;
        } else {
            this.names = names;
        }
        if (extensions == null) {
            this.extensions = Collections.emptyMap();
        } else {
            this.extensions = Collections.unmodifiableMap(extensions);
        }
        final StringBuilder buffer = new StringBuilder();
        createDefinitionString(buffer);
        matchingRuleString = buffer.toString();
    }

    /**
   * Constructs a string representation of this matching rule definition in the
   * provided buffer.
   *
   * @param  buffer  The buffer in which to construct a string representation of
   *                 this matching rule definition.
   */
    private void createDefinitionString(final StringBuilder buffer) {
        buffer.append("( ");
        buffer.append(oid);
        if (names.length == 1) {
            buffer.append(" NAME '");
            buffer.append(names[0]);
            buffer.append('\'');
        } else if (names.length > 1) {
            buffer.append(" NAME (");
            for (final String name : names) {
                buffer.append(" '");
                buffer.append(name);
                buffer.append('\'');
            }
            buffer.append(" )");
        }
        if (description != null) {
            buffer.append(" DESC '");
            encodeValue(description, buffer);
            buffer.append('\'');
        }
        if (isObsolete) {
            buffer.append(" OBSOLETE");
        }
        buffer.append(" SYNTAX ");
        buffer.append(syntaxOID);
        for (final Map.Entry<String, String[]> e : extensions.entrySet()) {
            final String name = e.getKey();
            final String[] values = e.getValue();
            if (values.length == 1) {
                buffer.append(' ');
                buffer.append(name);
                buffer.append(" '");
                encodeValue(values[0], buffer);
                buffer.append('\'');
            } else {
                buffer.append(' ');
                buffer.append(name);
                buffer.append(" (");
                for (final String value : values) {
                    buffer.append(" '");
                    encodeValue(value, buffer);
                    buffer.append('\'');
                }
                buffer.append(" )");
            }
        }
        buffer.append(" )");
    }

    /**
   * Retrieves the OID for this matching rule.
   *
   * @return  The OID for this matching rule.
   */
    public String getOID() {
        return oid;
    }

    /**
   * Retrieves the set of names for this matching rule.
   *
   * @return  The set of names for this matching rule, or an empty array if it
   *          does not have any names.
   */
    public String[] getNames() {
        return names;
    }

    /**
   * Retrieves the primary name that can be used to reference this matching
   * rule.  If one or more names are defined, then the first name will be used.
   * Otherwise, the OID will be returned.
   *
   * @return  The primary name that can be used to reference this matching rule.
   */
    public String getNameOrOID() {
        if (names.length == 0) {
            return oid;
        } else {
            return names[0];
        }
    }

    /**
   * Indicates whether the provided string matches the OID or any of the names
   * for this matching rule.
   *
   * @param  s  The string for which to make the determination.  It must not be
   *            {@code null}.
   *
   * @return  {@code true} if the provided string matches the OID or any of the
   *          names for this matching rule, or {@code false} if not.
   */
    public boolean hasNameOrOID(final String s) {
        for (final String name : names) {
            if (s.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return s.equalsIgnoreCase(oid);
    }

    /**
   * Retrieves the description for this matching rule, if available.
   *
   * @return  The description for this matching rule, or {@code null} if there
   *          is no description defined.
   */
    public String getDescription() {
        return description;
    }

    /**
   * Indicates whether this matching rule is declared obsolete.
   *
   * @return  {@code true} if this matching rule is declared obsolete, or
   *          {@code false} if it is not.
   */
    public boolean isObsolete() {
        return isObsolete;
    }

    /**
   * Retrieves the OID of the syntax for this matching rule.
   *
   * @return  The OID of the syntax for this matching rule.
   */
    public String getSyntaxOID() {
        return syntaxOID;
    }

    /**
   * Retrieves the set of extensions for this matching rule.  They will be
   * mapped from the extension name (which should start with "X-") to the set
   * of values for that extension.
   *
   * @return  The set of extensions for this matching rule.
   */
    public Map<String, String[]> getExtensions() {
        return extensions;
    }

    /**
   * {@inheritDoc}
   */
    @Override()
    public int hashCode() {
        return oid.hashCode();
    }

    /**
   * {@inheritDoc}
   */
    @Override()
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof MatchingRuleDefinition)) {
            return false;
        }
        final MatchingRuleDefinition d = (MatchingRuleDefinition) o;
        return (oid.equals(d.oid) && syntaxOID.equals(d.syntaxOID) && stringsEqualIgnoreCaseOrderIndependent(names, d.names) && bothNullOrEqualIgnoreCase(description, d.description) && (isObsolete == d.isObsolete) && extensionsEqual(extensions, d.extensions));
    }

    /**
   * Retrieves a string representation of this matching rule definition, in the
   * format described in RFC 4512 section 4.1.3.
   *
   * @return  A string representation of this matching rule definition.
   */
    @Override()
    public String toString() {
        return matchingRuleString;
    }
}
