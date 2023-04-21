package com.entelience.probe.mail;

import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Set;
import com.entelience.directory.Company;
import com.entelience.sql.Db;
import com.entelience.sql.DbHelper;

/**
 * EMF import skeleton
 *
 *
 */
public class EMFImport extends SpamMailDbImport {

    protected String defaultCompany;

    protected static final Set<String> cieDomains = new HashSet<String>();

    public void prepare() throws Exception {
        super.prepare();
        Db db = getDb();
        if (defaultCompany == null) throw new Exception("No default company is specified");
        PreparedStatement pstResolveCie = db.prepareStatement("SELECT e_company_id FROM e_company WHERE name = ?");
        pstResolveCie.setString(1, defaultCompany);
        Integer cieId = DbHelper.getKey(pstResolveCie);
        if (cieId == null) {
            throw new Exception("Provided default company " + defaultCompany + " does not resolve to an existing company");
        }
        cieDomains.addAll(Company.listDomainNames(db, defaultCompany));
        _logger.info("Company domains are " + cieDomains);
        if (cieDomains.isEmpty()) {
            _logger.warn("No domains specified for company " + defaultCompany);
        }
    }

    /**
     * here we should check the db version, but it seems that this info is not available
     */
    protected boolean checkVersion(Db foreignDb) throws Exception {
        try {
            foreignDb.enter();
            return true;
        } finally {
            foreignDb.exit();
        }
    }

    protected String getVersion() throws Exception {
        return null;
    }

    protected void importUserMimeDaily(Db foreignDb) throws Exception {
        try {
            foreignDb.enter();
        } finally {
            foreignDb.exit();
        }
    }

    protected void importUsers(Db foreignDb) throws Exception {
        try {
            foreignDb.enter();
        } finally {
            foreignDb.exit();
        }
    }

    protected void importInternalUserDaily(Db foreignDb) throws Exception {
        try {
            foreignDb.enter();
        } finally {
            foreignDb.exit();
        }
    }

    protected void importUserDaily(Db foreignDb) throws Exception {
        try {
            foreignDb.enter();
        } finally {
            foreignDb.exit();
        }
    }

    protected void importAttachmentsDaily(Db foreignDb) throws Exception {
        try {
            foreignDb.enter();
        } finally {
            foreignDb.exit();
        }
    }

    protected void importMailDaily(Db foreignDb) throws Exception {
        try {
            foreignDb.enter();
        } finally {
            foreignDb.exit();
        }
    }

    protected void importSpamDaily(Db foreignDb) throws Exception {
        try {
            foreignDb.enter();
        } finally {
            foreignDb.exit();
        }
    }

    protected void importMessagesDaily(Db foreignDb) throws Exception {
        try {
            foreignDb.enter();
        } finally {
            foreignDb.exit();
        }
    }

    protected void updateMinMax(Db foreignDb) throws Exception {
        try {
            foreignDb.enter();
            _logger.debug("updateMinMax");
        } finally {
            foreignDb.exit();
        }
    }

    public String[] getTableNames() {
        return new String[] {};
    }
}
