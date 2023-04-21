package org.t2framework.commons.util;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.t2framework.commons.exception.IORuntimeException;

/**
 * 
 * <#if locale="en">
 * <p>
 * {@link Closeable} utility.
 * </p>
 * <#else>
 * <p>
 * {@code Closeable}のためのユーティリティクラスです．
 * </p>
 * </#if>
 * 
 * @author shot
 * @since 0.4.0-ga
 */
public class CloseableUtil {

    private CloseableUtil() {
    }

    /**
	 * <#if locale="en">
	 * <p>
	 * Close {@link Closeable} resource.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param c
	 */
    public static void close(Closeable c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (IOException e) {
            Throwable cause = e.getCause();
            if (cause != null && cause instanceof SQLException) {
                throw new IORuntimeException(cause);
            } else {
                throw new IORuntimeException(e);
            }
        }
    }

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Close {@link ResultSet}.
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param rs
	 */
    public static void close(final ResultSet rs) {
        if (rs == null) {
            return;
        }
        close(new Closeable() {

            @Override
            public void close() throws IOException {
                try {
                    if (rs.isClosed() == false) {
                        rs.close();
                    }
                } catch (SQLException e) {
                    throw new IOException(e);
                }
            }
        });
    }

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Close {@link Connection}.
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param connection
	 */
    public static void close(final Connection connection) {
        if (connection == null) {
            return;
        }
        close(new Closeable() {

            @Override
            public void close() throws IOException {
                try {
                    if (connection.isClosed() == false) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    throw new IOException(e);
                }
            }
        });
    }

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Close {@link Statement}.
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param statement
	 */
    public static void close(final Statement statement) {
        if (statement == null) {
            return;
        }
        close(new Closeable() {

            @Override
            public void close() throws IOException {
                try {
                    if (statement.isClosed() == false) {
                        statement.close();
                    }
                } catch (SQLException e) {
                    throw new IOException(e);
                }
            }
        });
    }
}
