package eu.funcnet.clients.go_db.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import eu.funcnet.clients.go_db.GoException;
import eu.funcnet.clients.go_db.GoRepository;

public class GoJdbcRepository implements GoRepository {

    private static final String __getTermToNameQuery = "select t.acc, t.name " + "from " + "term t " + "inner join graph_path grp " + "on t.id = grp.term1_id " + "inner join association a " + "on grp.term2_id = a.term_id " + "inner join gene_product gp " + "on a.gene_product_id = gp.id " + "inner join evidence e " + "on e.association_id = a.id " + "inner join species s " + "on gp.species_id = s.id " + "inner join dbxref x " + "on gp.dbxref_id = x.id " + "where " + "s.ncbi_taxa_id = ? " + "and " + "e.code in ( 'EXP', 'IDA', 'IPI', 'IMP', 'IGI', 'IEP', 'TAS', 'IC' ) " + "and trim( t.name ) not in ( 'all', 'biological_process', 'cellular_component', 'molecular_function' ) " + "and x.xref_dbname like 'UniProt%' " + "group by " + "t.acc, t.name " + "having count( distinct left( gp.id, 6 ) ) >= ? " + "order by t.name asc;";

    private static final String __getTermByAccQuery = "select name " + "from term " + "where acc = ?;";

    private static final String __getProteinsByTermAccQuery = "select distinct left( x.xref_key, 6 ) as protein " + "from " + "term t " + "inner join graph_path grp " + "on t.id = grp.term1_id " + "inner join association a " + "on grp.term2_id = a.term_id " + "inner join gene_product gp " + "on a.gene_product_id = gp.id " + "inner join evidence e " + "on e.association_id = a.id " + "inner join species s " + "on gp.species_id = s.id " + "inner join dbxref x " + "on gp.dbxref_id = x.id " + "where " + "t.acc = ? " + "and " + "s.ncbi_taxa_id = ? " + "and " + "e.code in ( 'EXP', 'IDA', 'IPI', 'IMP', 'IGI', 'IEP', 'TAS', 'IC' ) " + "and " + "x.xref_dbname like 'UniProt%' " + "limit ?";

    private final String _dbUrl;

    private final int _taxon;

    private final int _maxProteinsPerQuery;

    private final Map<String, String> _accToName;

    private final Map<String, Set<String>> _accToProteins;

    public GoJdbcRepository(final int taxon, final int minProteinsPerTerm, final int maxProteinsPerQuery) throws GoException {
        this(null, taxon, minProteinsPerTerm, maxProteinsPerQuery);
    }

    public GoJdbcRepository(final String url, final int taxon, final int minProteinsPerTerm, final int maxProteinsPerQuery) throws GoException {
        _taxon = taxon;
        _maxProteinsPerQuery = maxProteinsPerQuery;
        _accToName = Collections.synchronizedMap(new LinkedHashMap<String, String>());
        _accToProteins = Collections.synchronizedMap(new HashMap<String, Set<String>>());
        PreparedStatement stat = null;
        try {
            if (url == null) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (final ClassNotFoundException e) {
                    throw new GoException("Failed at Class.forName step", e);
                }
                String dbURL = System.getProperty("mysql.go.url");
                if (dbURL == null) {
                    dbURL = System.getenv("MYSQL_GO_URL");
                    if (dbURL == null) throw new GoException("No connection string supplied, and the system property mysql.go.url and the environment variable MYSQL_GO_URL are both unset");
                }
                _dbUrl = dbURL;
            } else _dbUrl = url;
            final Connection conn = getConnection();
            stat = conn.prepareStatement(__getTermToNameQuery);
            stat.setInt(1, taxon);
            stat.setInt(2, minProteinsPerTerm);
            final ResultSet resultSet = stat.executeQuery();
            while (resultSet.next()) {
                final String acc = resultSet.getString("acc");
                final String name = resultSet.getString("name");
                _accToName.put(acc.trim().toUpperCase(), name.trim());
            }
        } catch (final SQLException e) {
            throw new GoException(e);
        } finally {
            tryToClose(stat);
        }
    }

    public Set<String> getProteinsByAcc(String acc) throws GoException {
        acc = acc.trim().toUpperCase();
        if (_accToProteins.containsKey(acc)) return _accToProteins.get(acc);
        final Set<String> proteins = new HashSet<String>();
        PreparedStatement stat = null;
        try {
            final Connection conn = getConnection();
            stat = conn.prepareStatement(__getProteinsByTermAccQuery);
            stat.setString(1, acc);
            stat.setInt(2, _taxon);
            stat.setInt(3, _maxProteinsPerQuery);
            final ResultSet result = stat.executeQuery();
            while (result.next()) {
                proteins.add(result.getString("protein"));
            }
        } catch (final SQLException e) {
            throw new GoException(e);
        } finally {
            tryToClose(stat);
        }
        if (proteins.size() > 0) _accToProteins.put(acc, proteins);
        return new HashSet<String>(proteins);
    }

    public Map<String, String> getAccToNameMap() {
        return new LinkedHashMap<String, String>(_accToName);
    }

    public String getTermNameByAcc(String acc) throws GoException {
        acc = acc.trim().toUpperCase();
        if (_accToName.containsKey(acc)) return _accToName.get(acc);
        String name = null;
        PreparedStatement stat = null;
        try {
            final Connection conn = getConnection();
            stat = conn.prepareStatement(__getTermByAccQuery);
            stat.setString(1, acc);
            final ResultSet result = stat.executeQuery();
            if (result.next()) {
                name = result.getString("name");
                _accToName.put(acc, name);
            }
        } catch (final SQLException e) {
            throw new GoException(e);
        } finally {
            tryToClose(stat);
        }
        return name;
    }

    public Map<String, String> getTermsBySubstrings(final List<String> substrings) {
        final Map<String, String> out = new HashMap<String, String>();
        synchronized (_accToName) {
            for (final Map.Entry<String, String> entry : _accToName.entrySet()) {
                if (allSubstringsMatch(substrings, entry.getValue())) out.put(entry.getValue(), entry.getKey());
            }
        }
        return out;
    }

    private boolean allSubstringsMatch(final List<String> substrings, final String string) {
        final String lc = string.toLowerCase();
        for (final String substring : substrings) {
            if (!lc.contains(substring.trim().toLowerCase())) return false;
        }
        return true;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(_dbUrl);
    }

    private void tryToClose(final PreparedStatement stat) throws GoException {
        try {
            if (stat != null) {
                stat.close();
                if (stat.getConnection() != null) stat.getConnection().close();
            }
        } catch (final SQLException e) {
            throw new GoException(e);
        }
    }

    public int accCacheEntryCount() {
        return _accToName.size();
    }

    public int proteinCacheEntryCount() {
        return _accToProteins.size();
    }

    public void clearAccCache() {
        _accToName.clear();
    }

    public void clearProteinCache() {
        _accToProteins.clear();
    }
}
