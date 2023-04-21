package com.centropresse.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Locale;
import java.sql.PreparedStatement;
import org.apache.log4j.Logger;
import com.centropresse.dto.Cliente;
import com.centropresse.exception.DataAccessException;
import com.centropresse.util.Constants;
import com.centropresse.util.LogFactory;
import java.util.Collection;
import java.util.ArrayList;

public class ClienteDAO extends AbstractDAO {

    private static final String nameClass = "ClienteDAO";

    public static Logger logger = LogFactory.getWebLogger();

    private static String prefixLogClass = Constants.APPLICATION_CODE_BUSINESS + "." + nameClass + ".class";

    private static final String DB_TABLE_LOGIN_FIELD_ID = Constants.DB_TABLE_LOGIN_FIELD_ID;

    private static final String DB_TABLE_CLIENTE = Constants.DB_TABLE_CLIENTE;

    private static final String DB_FIELD_NOME = Constants.DB_TABLE_CLIENTE_FIELD_NOME;

    private static final String DB_FIELD_COGNOME = Constants.DB_TABLE_CLIENTE_FIELD_COGNOME;

    private static final String DB_FIELD_RSOCIALE = Constants.DB_TABLE_CLIENTE_FIELD_RSOCIALE;

    private static final String DB_FIELD_INDIRIZZO = Constants.DB_TABLE_CLIENTE_FIELD_INDIRIZZO;

    private static final String DB_FIELD_CAP = Constants.DB_TABLE_CLIENTE_FIELD_CAP;

    private static final String DB_FIELD_CITTA = Constants.DB_TABLE_CLIENTE_FIELD_CITTA;

    private static final String DB_FIELD_PROVINCIA = Constants.DB_TABLE_CLIENTE_FIELD_PROVINCIA;

    private static final String DB_FIELD_TELEFONO = Constants.DB_TABLE_CLIENTE_FIELD_TELEFONO;

    private static final String DB_FIELD_ALTROTELEFONO = Constants.DB_TABLE_CLIENTE_FIELD_ALTROTELEFONO;

    private static final String DB_FIELD_EMAIL = Constants.DB_TABLE_CLIENTE_FIELD_EMAIL;

    private static final String DB_FIELD_CFISCALE = Constants.DB_TABLE_CLIENTE_FIELD_CFISCALE;

    private static final String SQL_EXCEPTION = "Errore durante la connessione al DB";

    private static final String SQL_EXCEPTION_INSERT = "Errore durante l'inserimento del " + DB_TABLE_CLIENTE;

    private static final String SQL_EXCEPTION_UPDATE = "Errore durante la modifica del " + DB_TABLE_CLIENTE;

    private StringBuffer SQL_SELECT = new StringBuffer();

    private StringBuffer SQL_UPDATE = new StringBuffer();

    private StringBuffer SQL_INSERT = new StringBuffer();

    private PreparedStatement pstm;

    private Connection conn;

    private ResultSet rset;

    Cliente cliente;

    public ClienteDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertCliente(Cliente cliente) throws DataAccessException {
        String prefixLogMethod = prefixLogClass + "::insert" + DB_TABLE_CLIENTE + " - ";
        logger.info(prefixLogMethod + "BEGIN");
        try {
            SQL_INSERT.append("INSERT INTO " + DB_TABLE_CLIENTE);
            SQL_INSERT.append(" (");
            SQL_INSERT.append(DB_TABLE_LOGIN_FIELD_ID + ", ");
            SQL_INSERT.append(DB_FIELD_NOME + ", ");
            SQL_INSERT.append(DB_FIELD_COGNOME + ", ");
            SQL_INSERT.append(DB_FIELD_INDIRIZZO + ", ");
            SQL_INSERT.append(DB_FIELD_CAP + ", ");
            SQL_INSERT.append(DB_FIELD_CITTA + ", ");
            SQL_INSERT.append(DB_FIELD_PROVINCIA + ", ");
            SQL_INSERT.append(DB_FIELD_TELEFONO + ", ");
            SQL_INSERT.append(DB_FIELD_ALTROTELEFONO + ", ");
            SQL_INSERT.append(DB_FIELD_EMAIL + ", ");
            SQL_INSERT.append(DB_FIELD_RSOCIALE + ", ");
            SQL_INSERT.append(DB_FIELD_CFISCALE);
            SQL_INSERT.append(") ");
            SQL_INSERT.append("VALUES (");
            SQL_INSERT.append("?,?,?,?,?,?,?,?,?,?,?,?");
            SQL_INSERT.append(")");
            logger.debug(prefixLogMethod + SQL_INSERT.toString());
            pstm = conn.prepareStatement(SQL_INSERT.toString());
            int k = 0;
            setString(pstm, k, cliente.getId_user());
            setString(pstm, k, (String) cliente.getNome_cliente());
            setString(pstm, k, (String) cliente.getCognome_cliente());
            setString(pstm, k, (String) cliente.getIndirizzo_cliente());
            setString(pstm, k, (String) cliente.getCap_cliente());
            setString(pstm, k, (String) cliente.getCitta_cliente());
            setString(pstm, k, (String) cliente.getProvincia_cliente());
            setString(pstm, k, (String) cliente.getTelefono_cliente());
            setString(pstm, k, (String) cliente.getAltrotelefono_cliente());
            setString(pstm, k, (String) cliente.getPostaelettronica_cliente());
            setString(pstm, k, (String) cliente.getRagioneSociale());
            setString(pstm, k, (String) cliente.getCfpiva_cliente());
            if (pstm.executeUpdate() == 0) insertFailed(SQL_EXCEPTION_INSERT);
        } catch (Exception e) {
            logger.fatal(SQL_EXCEPTION_INSERT);
            e.printStackTrace();
        } finally {
            close(conn, pstm);
            logger.info(prefixLogMethod + "END");
        }
    }

    public String getPassword(Cliente cliente) {
        String password = "";
        return password;
    }
}
