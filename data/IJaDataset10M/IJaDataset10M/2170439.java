package com.p6spy.engine.outage;

import java.sql.*;
import com.p6spy.engine.spy.*;

public class P6OutageStatement extends P6Statement implements Statement {

    public P6OutageStatement(P6Factory factory, Statement statement, P6Connection conn) {
        super(factory, statement, conn);
    }

    public boolean execute(String p0) throws java.sql.SQLException {
        statementQuery = p0;
        long startTime = System.currentTimeMillis();
        if (P6OutageOptions.getOutageDetection()) {
            P6OutageDetector.getInstance().registerInvocation(this, startTime, "statement", "", p0);
        }
        try {
            return passthru.execute(p0);
        } finally {
            if (P6OutageOptions.getOutageDetection()) {
                P6OutageDetector.getInstance().unregisterInvocation(this);
            }
        }
    }

    public boolean execute(String p0, int p1) throws java.sql.SQLException {
        statementQuery = p0;
        long startTime = System.currentTimeMillis();
        if (P6OutageOptions.getOutageDetection()) {
            P6OutageDetector.getInstance().registerInvocation(this, startTime, "statement", "", p0);
        }
        try {
            return passthru.execute(p0, p1);
        } finally {
            if (P6OutageOptions.getOutageDetection()) {
                P6OutageDetector.getInstance().unregisterInvocation(this);
            }
        }
    }

    public boolean execute(String p0, int p1[]) throws java.sql.SQLException {
        statementQuery = p0;
        long startTime = System.currentTimeMillis();
        if (P6OutageOptions.getOutageDetection()) {
            P6OutageDetector.getInstance().registerInvocation(this, startTime, "statement", "", p0);
        }
        try {
            return passthru.execute(p0, p1);
        } finally {
            if (P6OutageOptions.getOutageDetection()) {
                P6OutageDetector.getInstance().unregisterInvocation(this);
            }
        }
    }

    public boolean execute(String p0, String p1[]) throws java.sql.SQLException {
        statementQuery = p0;
        long startTime = System.currentTimeMillis();
        if (P6OutageOptions.getOutageDetection()) {
            P6OutageDetector.getInstance().registerInvocation(this, startTime, "statement", "", p0);
        }
        try {
            return passthru.execute(p0, p1);
        } finally {
            if (P6OutageOptions.getOutageDetection()) {
                P6OutageDetector.getInstance().unregisterInvocation(this);
            }
        }
    }

    public ResultSet executeQuery(String p0) throws java.sql.SQLException {
        statementQuery = p0;
        long startTime = System.currentTimeMillis();
        if (P6OutageOptions.getOutageDetection()) {
            P6OutageDetector.getInstance().registerInvocation(this, startTime, "statement", "", p0);
        }
        try {
            return (new P6ResultSet(getP6Factory(), passthru.executeQuery(p0), this, "", p0));
        } finally {
            if (P6OutageOptions.getOutageDetection()) {
                P6OutageDetector.getInstance().unregisterInvocation(this);
            }
        }
    }

    public int executeUpdate(String p0) throws java.sql.SQLException {
        statementQuery = p0;
        long startTime = System.currentTimeMillis();
        if (P6OutageOptions.getOutageDetection()) {
            P6OutageDetector.getInstance().registerInvocation(this, startTime, "statement", "", p0);
        }
        try {
            return (passthru.executeUpdate(p0));
        } finally {
            if (P6OutageOptions.getOutageDetection()) {
                P6OutageDetector.getInstance().unregisterInvocation(this);
            }
        }
    }

    public int executeUpdate(String p0, int p1) throws java.sql.SQLException {
        statementQuery = p0;
        long startTime = System.currentTimeMillis();
        if (P6OutageOptions.getOutageDetection()) {
            P6OutageDetector.getInstance().registerInvocation(this, startTime, "statement", "", p0);
        }
        try {
            return (passthru.executeUpdate(p0, p1));
        } finally {
            if (P6OutageOptions.getOutageDetection()) {
                P6OutageDetector.getInstance().unregisterInvocation(this);
            }
        }
    }

    public int executeUpdate(String p0, int p1[]) throws java.sql.SQLException {
        statementQuery = p0;
        long startTime = System.currentTimeMillis();
        if (P6OutageOptions.getOutageDetection()) {
            P6OutageDetector.getInstance().registerInvocation(this, startTime, "statement", "", p0);
        }
        try {
            return (passthru.executeUpdate(p0, p1));
        } finally {
            if (P6OutageOptions.getOutageDetection()) {
                P6OutageDetector.getInstance().unregisterInvocation(this);
            }
        }
    }

    public int executeUpdate(String p0, String p1[]) throws java.sql.SQLException {
        statementQuery = p0;
        long startTime = System.currentTimeMillis();
        if (P6OutageOptions.getOutageDetection()) {
            P6OutageDetector.getInstance().registerInvocation(this, startTime, "statement", "", p0);
        }
        try {
            return (passthru.executeUpdate(p0, p1));
        } finally {
            if (P6OutageOptions.getOutageDetection()) {
                P6OutageDetector.getInstance().unregisterInvocation(this);
            }
        }
    }

    public void addBatch(String p0) throws java.sql.SQLException {
        statementQuery = p0;
        long startTime = System.currentTimeMillis();
        if (P6OutageOptions.getOutageDetection()) {
            P6OutageDetector.getInstance().registerInvocation(this, startTime, "batch", "", p0);
        }
        try {
            passthru.addBatch(p0);
        } finally {
            if (P6OutageOptions.getOutageDetection()) {
                P6OutageDetector.getInstance().unregisterInvocation(this);
            }
        }
    }

    public int[] executeBatch() throws java.sql.SQLException {
        long startTime = System.currentTimeMillis();
        if (P6OutageOptions.getOutageDetection()) {
            P6OutageDetector.getInstance().registerInvocation(this, startTime, "statement", "", statementQuery);
        }
        try {
            return (passthru.executeBatch());
        } finally {
            if (P6OutageOptions.getOutageDetection()) {
                P6OutageDetector.getInstance().unregisterInvocation(this);
            }
        }
    }
}
