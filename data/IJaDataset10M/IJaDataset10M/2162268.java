package com.googlecode.lawu.net.irc;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A user or server in the IRC protocol.
 * 
 * @author Miorel-Lucian Palii
 */
public class Entity {

    private static final Pattern ENTITY_INFO_FIELD;

    private static final Pattern ENTITY_INFO;

    static {
        String eifr = "[^!@]+";
        ENTITY_INFO_FIELD = Pattern.compile(eifr);
        ENTITY_INFO = Pattern.compile(String.format("(?:(%1$s)!)?(?:(%1$s)@)?(%1$s)", eifr));
    }

    private final String nick;

    private final String ident;

    private final String host;

    private final SocketAddress address;

    /**
	 * <p>
	 * Constructs an entity with the specified IRC nick, ident string, host, and
	 * address.
	 * </p>
	 * <p>
	 * Here the address effectively refers to the network. Note that this
	 * distinction may be too strict sometimes: networks often have multiple
	 * servers and even multiple ports on the same server; therefore if you're
	 * connected to a network multiple times you might get objects which really
	 * represent the same entity but have different addresses. The
	 * {@link #equalsIgnoreAddress(Entity)} method is provided to allow checking
	 * for equality in such cases.
	 * </p>
	 * 
	 * @param nick
	 *            the IRC nick, or <code>null</code> if one isn't known (as in
	 *            the case of a server)
	 * @param ident
	 *            the ident, or <code>null</code> if one isn't known (as in the
	 *            case of a server)
	 * @param host
	 *            the entity's host
	 * @param address
	 *            the address with which this entity is associated
	 */
    public Entity(String nick, String ident, String host, SocketAddress address) {
        if (nick != null) {
            if (nick.isEmpty()) throw new IllegalArgumentException("The nickname may not be zero-length, use null instead."); else if (!ENTITY_INFO_FIELD.matcher(nick).matches()) throw new IllegalArgumentException("The nickname must be a valid entity info field.");
        }
        if (ident != null) {
            if (ident.isEmpty()) throw new IllegalArgumentException("The ident may not be zero-length, use null instead."); else if (!ENTITY_INFO_FIELD.matcher(ident).matches()) throw new IllegalArgumentException("The ident must be a valid entity info field.");
        }
        if (host == null) throw new NullPointerException("The host may not be null.");
        if (host.isEmpty()) throw new IllegalArgumentException("The host may not be zero-length.");
        if (!ENTITY_INFO_FIELD.matcher(host).matches()) throw new IllegalArgumentException("The host must be a valid entity info field.");
        if (address != null && address instanceof InetSocketAddress && ((InetSocketAddress) address).getAddress().isAnyLocalAddress()) throw new IllegalArgumentException("The address may not be a wildcard, use null instead.");
        this.nick = nick;
        this.ident = ident;
        this.host = host;
        this.address = address;
    }

    /**
	 * Returns the address.
	 * 
	 * @return the address
	 */
    public SocketAddress getAddress() {
        return this.address;
    }

    /**
	 * Returns the nickname.
	 * 
	 * @return the nickname
	 */
    public String getNick() {
        return this.nick;
    }

    /**
	 * Returns the ident.
	 * 
	 * @return the ident
	 */
    public String getIdent() {
        return this.ident;
    }

    /**
	 * Returns the host.
	 * 
	 * @return the host
	 */
    public String getHost() {
        return this.host;
    }

    /**
	 * Builds an entity from the specified entity information string, associated with the specified address.
	 * 
	 * @param entityInfo
	 *            the entity information
	 *            @param address the address with which this entity is associated
	 * @return an entity with the specified information
	 * @see #forInfo(String)
	 */
    public static Entity forInfo(String entityInfo, SocketAddress address) {
        if (entityInfo == null) throw new NullPointerException("Can't parse null string.");
        Matcher m = ENTITY_INFO.matcher(entityInfo);
        if (!m.matches()) throw new IllegalArgumentException("Improperly formatted info string.");
        return new Entity(m.group(1), m.group(2), m.group(3), address);
    }

    /**
	 * Builds an entity from the specified entity information string. This
	 * method is equivalent to
	 * <code>Entity.{@link #forInfo(String,SocketAddress) forInfo}(entityInfo, null)</code>.
	 * 
	 * @param entityInfo
	 *            the entity information
	 * @return an entity with the specified information
	 * @see #forInfo(String, SocketAddress)
	 */
    public static Entity forInfo(String entityInfo) {
        return forInfo(entityInfo, null);
    }

    /**
	 * <p>
	 * Checks whether or not this entity represents an IRC user. If it's not a
	 * user, then it's probably a server.
	 * </p>
	 * <p>
	 * A user is defined as an entity that has both a nick and an ident.
	 * </p>
	 * 
	 * @return <code>true</code> if and only if this entity represents an IRC
	 *         user
	 */
    public boolean isUser() {
        return this.nick != null && this.ident != null;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean ret = false;
        if (this == obj) ret = true; else if (obj instanceof Entity) {
            Entity e = (Entity) obj;
            ret = (this.address == null ? e.address == null : this.address.equals(e.address)) && equalsIgnoreAddress(e);
        }
        return ret;
    }

    /**
	 * Indicates whether some other entity is "equal to" this one, ignoring the
	 * address. This might come in handy for comparing entities from connections
	 * to different servers or different ports of the same network.
	 * 
	 * @param e
	 *            the entity with which to compare
	 * @return <code>true</code> if this entity is the same as the argument,
	 *         <code>false</code> otherwise
	 */
    public boolean equalsIgnoreAddress(Entity e) {
        boolean ret = false;
        if (this == e) ret = true; else if (e != null) {
            ret = (this.nick == null ? e.nick == null : this.nick.equals(e.nick)) && (this.ident == null ? e.ident == null : this.ident.equals(e.ident)) && this.host.equals(e.host);
        }
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isUser()) sb.append(this.nick).append('!').append(this.ident).append('@');
        sb.append(this.host);
        if (this.address != null) sb.append(" on ").append(this.address);
        return sb.toString();
    }
}
