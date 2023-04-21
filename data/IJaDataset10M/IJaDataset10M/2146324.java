package org.nakedobjects.nos.store.hibernate.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.nakedobjects.noa.NakedObjectRuntimeException;
import org.nakedobjects.noa.security.AuthenticationRequest;
import org.nakedobjects.noa.security.Authenticator;
import org.nakedobjects.nof.core.security.PasswordAuthenticationRequest;
import org.nakedobjects.nof.core.util.Assert;
import org.nakedobjects.nos.store.hibernate.HibernateUtil;

public class DatabaseAuthenticator implements Authenticator {

    private static final boolean FAILED_AUTHENTICATION = false;

    private static final MessageDigest messageDigest;

    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (final NoSuchAlgorithmException nsa) {
            throw new NakedObjectRuntimeException(nsa);
        }
    }

    public DatabaseAuthenticator() {
    }

    private final void setRoles(final AuthenticationRequest request) {
        try {
            HibernateUtil.startTransaction();
            final SQLQuery sq = HibernateUtil.getCurrentSession().createSQLQuery("select r.rolename rr from role r, user u, user_role ur where r.id = ur.role and ur.user = u.id and u.username = ?");
            sq.setString(0, request.getName());
            sq.addScalar("rr", Hibernate.STRING);
            final List<String> roles = sq.list();
            HibernateUtil.commitTransaction();
            request.setRoles(roles.toArray(new String[roles.size()]));
        } catch (final Exception e) {
            HibernateUtil.rollbackTransaction();
        }
    }

    public static String generateHash(final String key) {
        synchronized (messageDigest) {
            messageDigest.reset();
            messageDigest.update(key.getBytes());
            final byte[] bytes = messageDigest.digest();
            final StringBuffer buff = new StringBuffer();
            for (int l = 0; l < bytes.length; l++) {
                final String hx = Integer.toHexString(0xFF & bytes[l]);
                if (hx.length() == 1) {
                    buff.append("0");
                }
                buff.append(hx);
            }
            return buff.toString().trim();
        }
    }

    private int count(final Object countValue) {
        if (countValue == null) {
            return 0;
        }
        if (countValue instanceof BigInteger) {
            return ((BigInteger) countValue).intValue();
        }
        if (countValue instanceof Integer) {
            return ((Integer) countValue).intValue();
        }
        throw new NakedObjectRuntimeException("Unexpected type");
    }

    public final boolean isValidUser(final AuthenticationRequest request) {
        final PasswordAuthenticationRequest passwordRequest = (PasswordAuthenticationRequest) request;
        final String username = passwordRequest.getName();
        if (username == null || username.equals("")) {
            return FAILED_AUTHENTICATION;
        }
        final String password = passwordRequest.getPassword();
        Assert.assertNotNull(password);
        try {
            HibernateUtil.startTransaction();
            final SQLQuery sq = HibernateUtil.getCurrentSession().createSQLQuery("select count(*) from user u where username = ? and password = ?");
            sq.setString(0, username);
            sq.setString(1, generateHash(password));
            final Object result = sq.uniqueResult();
            HibernateUtil.commitTransaction();
            return count(result) > 0;
        } catch (final Exception e) {
            HibernateUtil.rollbackTransaction();
        }
        return FAILED_AUTHENTICATION;
    }

    public final boolean isValid(final AuthenticationRequest request) {
        final boolean valid = isValidUser(request);
        if (valid) {
            setRoles(request);
        }
        return valid;
    }

    public final boolean canAuthenticate(AuthenticationRequest request) {
        return request instanceof PasswordAuthenticationRequest;
    }
}
