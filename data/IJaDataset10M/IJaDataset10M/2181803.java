package com.entelience.provider.mail;

import org.apache.log4j.Logger;
import com.entelience.util.Logs;
import com.entelience.sql.Db;
import com.entelience.sql.DbHelper;
import com.entelience.util.StringHelper;
import com.entelience.util.DateHelper;
import com.entelience.util.EmailHelper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import com.entelience.objects.mail.UserInformation;
import com.entelience.objects.mail.Address;
import com.entelience.provider.DbCnilLevel;

/**
 * Provider for the mail user
 * 
 */
public class DbMailUser {

    protected static final Logger _logger = Logs.getLogger();

    private DbMailUser() {
    }

    /**
     */
    public static int addUser(Db db, UserInformation ui) throws Exception {
        if (ui == null) throw new IllegalArgumentException("The UserInformation cannot be null");
        if (StringHelper.nullify(ui.getDomain()) == null) throw new IllegalArgumentException("The UserInformation domain cannot be null");
        if (StringHelper.nullify(ui.getUser()) == null) throw new IllegalArgumentException("The UserInformation user cannot be null");
        try {
            db.enter();
            if (ui.getDomainId() != null) {
                String domain = DbMailDomain.getDomain(db, ui.getDomainId());
                if (domain == null) throw new IllegalArgumentException("The domain id (" + ui.getDomainId() + ") is invalid");
                if (ui.getDomain() != null && !domain.equalsIgnoreCase(ui.getDomain())) {
                    throw new IllegalArgumentException("The domain id doesn't match (" + ui.getDomain() + ") but (" + domain + ")");
                } else {
                    ui.setDomain(domain);
                }
            } else {
                Integer domainId = DbMailDomain.getDomainId(db, ui.getDomain());
                if (domainId == null) throw new IllegalArgumentException("The domain (" + ui.getDomain() + ") doesn't exist");
                ui.setDomainId(domainId);
            }
            DbCnilLevel.getCnilLevel(db);
            String userName = DbCnilLevel.obfuscateUsername(StringHelper.nullify(ui.getUser()));
            PreparedStatement pst = db.prepareStatement("INSERT INTO mail.t_domain_user (t_domain_id, user_name, first_occurrence, last_occurrence, domain_name, rfc_compliance) VALUES (?, ?, ?, ?, ?, ?) RETURNING t_domain_user_id");
            pst.setInt(1, ui.getDomainId());
            pst.setString(2, userName);
            pst.setTimestamp(3, DateHelper.sqlOrNull(ui.getFirstSeen()));
            pst.setTimestamp(4, DateHelper.sqlOrNull(ui.getLastSeen()));
            pst.setString(5, ui.getDomain());
            pst.setBoolean(6, ui.isRfcCompliant());
            return DbHelper.getIntKey(pst);
        } finally {
            db.exit();
        }
    }

    /**
     */
    public static Integer getUserId(Db db, Address ad) throws Exception {
        if (ad == null) throw new IllegalArgumentException("The address must not be null");
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT t_domain_user_id FROM mail.t_domain_user WHERE lower(user_name) = lower(?) AND lower(domain) = lower(?)");
            pst.setString(1, ad.getUser());
            pst.setString(2, ad.getDomain());
            return DbHelper.getKey(pst);
        } finally {
            db.exit();
        }
    }

    /**
     */
    public static Integer getUserId(Db db, String user, String domain) throws Exception {
        if (StringHelper.nullify(user) == null) throw new IllegalArgumentException("The email must not be null");
        if (StringHelper.nullify(domain) == null) throw new IllegalArgumentException("The domain must not be null");
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT t_domain_user_id FROM mail.t_domain_user WHERE lower(user_name) = lower(?) AND lower(domain) = lower(?)");
            pst.setString(1, StringHelper.nullify(user));
            pst.setString(2, StringHelper.nullify(domain));
            return DbHelper.getKey(pst);
        } finally {
            db.exit();
        }
    }

    /**
     */
    public static Integer getUserId(Db db, String user, int domainId) throws Exception {
        if (StringHelper.nullify(user) == null) throw new IllegalArgumentException("The email must not be null");
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT t_domain_user_id FROM mail.t_domain_user WHERE lower(user_name) = lower(?) AND t_domain_id = ?");
            pst.setString(1, StringHelper.nullify(user));
            pst.setInt(2, domainId);
            return DbHelper.getKey(pst);
        } finally {
            db.exit();
        }
    }

    /**
     */
    public static UserInformation getUser(Db db, int userId) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT t_domain_user_id, t_domain_id, user_name, first_occurrence, last_occurrence, domain_name, rfc_compliance FROM mail.t_domain_user WHERE t_domain_user_id = ?");
            pst.setInt(1, userId);
            ResultSet rs = db.executeQuery(pst);
            UserInformation ui = null;
            if (rs.next()) {
                ui = fromRs(rs);
            }
            return ui;
        } finally {
            db.exit();
        }
    }

    /**
     */
    public static UserInformation getUser(Db db, Address address) throws Exception {
        if (address == null) throw new IllegalArgumentException("The address must not be null");
        return getUser(db, address.getUser(), address.getDomain());
    }

    /**
     */
    public static UserInformation getUser(Db db, String user, String domain) throws Exception {
        if (StringHelper.nullify(user) == null) throw new IllegalArgumentException("The email must not be null");
        if (StringHelper.nullify(domain) == null) throw new IllegalArgumentException("The domain must not be null");
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT t_domain_user_id, t_domain_id, user_name, first_occurrence, last_occurrence, domain_name, rfc_compliance FROM mail.t_domain_user WHERE lower(user_name) = lower(?) AND lower(domain_name) = lower(?)");
            pst.setString(1, StringHelper.nullify(user));
            pst.setString(2, StringHelper.nullify(domain));
            ResultSet rs = db.executeQuery(pst);
            UserInformation ui = null;
            if (rs.next()) {
                ui = fromRs(rs);
            }
            return ui;
        } finally {
            db.exit();
        }
    }

    /**
     */
    public static List<UserInformation> getDomainUsers(Db db, String domain) throws Exception {
        if (StringHelper.nullify(domain) == null) throw new IllegalArgumentException("The domain must not be null");
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT t_domain_user_id, t_domain_id, user_name, first_occurrence, last_occurrence, domain_name, rfc_compliance FROM mail.t_domain_user WHERE lower(domain_name) = lower(?)");
            pst.setString(1, StringHelper.nullify(domain));
            ResultSet rs = db.executeQuery(pst);
            List<UserInformation> list = new ArrayList<UserInformation>();
            if (rs.next()) {
                do {
                    list.add(fromRs(rs));
                } while (rs.next());
            }
            return list;
        } finally {
            db.exit();
        }
    }

    /**
     */
    public static List<UserInformation> getDomainUsers(Db db, int domainId) throws Exception {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT t_domain_user_id, t_domain_id, user_name, first_occurrence, last_occurrence, domain_name, rfc_compliance FROM mail.t_domain_user WHERE t_domain_id = ?");
            pst.setInt(1, domainId);
            ResultSet rs = db.executeQuery(pst);
            List<UserInformation> list = new ArrayList<UserInformation>();
            if (rs.next()) {
                do {
                    list.add(fromRs(rs));
                } while (rs.next());
            }
            return list;
        } finally {
            db.exit();
        }
    }

    private static UserInformation fromRs(ResultSet rs) throws Exception {
        UserInformation ui = new UserInformation();
        ui.setUserId(rs.getInt(1));
        ui.setDomainId(rs.getInt(2));
        ui.setUser(rs.getString(3));
        ui.setFirstSeen(rs.getDate(4));
        ui.setLastSeen(rs.getDate(5));
        ui.setDomain(rs.getString(6));
        ui.setRfcCompliant(rs.getBoolean(7));
        return ui;
    }
}
