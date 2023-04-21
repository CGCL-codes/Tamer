package record.webcore.db.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import record.webcore.db.connector.Connector;
import record.webcore.db.data.FinancialAccount;
import record.webcore.db.data.FinancialAccountData;
import record.webcore.db.data.FinancialAccountDataDetail;
import record.webcore.db.data.FinancialClass;
import record.webcore.db.data.FinancialSubClass;
import record.webcore.db.data.WebUser;
import record.webcore.db.util.BatchHelper;
import record.webcore.db.util.QueryHelper;
import record.webcore.db.util.UpdateHelper;
import record.webcore.text.CalendarFormat;

public abstract class Financial implements Connector, record.webcore.db.Financial {

    private static final String ID_TRANSFER_FROM = "a41a57cd-cbff-4ba3-9df9-96a029d6efa9";

    private static final String ID_TRANSFER_TO = "0afdeacb-19e2-46a3-b78e-33f8ff267e1e";

    private static final String ID_TRANSFER_CHARGE = "c66c0713-5380-4bcb-a28f-ddd1b5a46be4";

    private CalendarFormat timeFormatter;

    protected Financial(TimeZone timeZone) {
        this.timeFormatter = new CalendarFormat(timeZone);
    }

    protected abstract WebUser getWebUser();

    private UUID getUserId() {
        UUID userId = null;
        WebUser user = this.getWebUser();
        if (user != null) {
            userId = user.getUserId();
        }
        return userId;
    }

    private String getUserIdString() {
        String userIdString = null;
        UUID userId = this.getUserId();
        if (userId != null) {
            userIdString = userId.toString();
        }
        return userIdString;
    }

    @Override
    public List<FinancialClass> listClasses(final boolean income) {
        List<FinancialClass> result = null;
        try {
            result = new QueryHelper<List<FinancialClass>>() {

                @Override
                protected String getSqlString() {
                    return "SELECT id, name, sort_order FROM financial_class WHERE income = ? ORDER BY sort_order";
                }

                @Override
                protected void preCommit(PreparedStatement stmt) throws SQLException {
                    stmt.setBoolean(1, income);
                }

                @Override
                protected List<FinancialClass> postCommit(PreparedStatement stmt) throws SQLException {
                    ResultSet resultSet = stmt.getResultSet();
                    ArrayList<FinancialClass> result = new ArrayList<FinancialClass>();
                    while (resultSet.next()) {
                        FinancialClass dataClass = new FinancialClass();
                        dataClass.setId(UUID.fromString(resultSet.getString(1)));
                        dataClass.setName(resultSet.getString(2));
                        dataClass.setIncome(income);
                        dataClass.setSortOrder(resultSet.getInt(3));
                        result.add(dataClass);
                    }
                    return result;
                }
            }.commit(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (result == null) {
            result = Collections.emptyList();
        }
        return result;
    }

    @Override
    public List<FinancialSubClass> listSubClasses(FinancialClass theClass) {
        List<FinancialSubClass> result = null;
        final UUID classId = theClass.getId();
        if (classId != null) {
            try {
                result = new QueryHelper<List<FinancialSubClass>>() {

                    @Override
                    protected String getSqlString() {
                        return "SELECT id, name, sort_order FROM financial_subclass WHERE class_id = CAST(? AS uuid) ORDER BY sort_order";
                    }

                    @Override
                    protected void preCommit(PreparedStatement stmt) throws SQLException {
                        stmt.setString(1, classId.toString());
                    }

                    @Override
                    protected List<FinancialSubClass> postCommit(PreparedStatement stmt) throws SQLException {
                        ResultSet resultSet = stmt.getResultSet();
                        ArrayList<FinancialSubClass> result = new ArrayList<FinancialSubClass>();
                        while (resultSet.next()) {
                            FinancialSubClass subClass = new FinancialSubClass();
                            subClass.setId(UUID.fromString(resultSet.getString(1)));
                            subClass.setName(resultSet.getString(2));
                            subClass.setClassId(classId);
                            subClass.setSortOrder(resultSet.getInt(3));
                            result.add(subClass);
                        }
                        return result;
                    }
                }.commit(this);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (result == null) {
            result = Collections.emptyList();
        }
        return result;
    }

    @Override
    public List<FinancialAccount> listAccounts() {
        List<FinancialAccount> result = null;
        final String userId = this.getUserIdString();
        if (userId != null) {
            try {
                result = new QueryHelper<List<FinancialAccount>>() {

                    @Override
                    protected String getSqlString() {
                        return "SELECT id, name, initial_value, custom_order, total_value FROM financial_account WHERE owner = CAST( ? AS uuid) ORDER BY custom_order ASC";
                    }

                    @Override
                    protected void preCommit(PreparedStatement stmt) throws SQLException {
                        stmt.setString(1, userId);
                    }

                    @Override
                    protected List<FinancialAccount> postCommit(PreparedStatement stmt) throws SQLException {
                        ResultSet resultSet = stmt.getResultSet();
                        ArrayList<FinancialAccount> result = new ArrayList<FinancialAccount>();
                        while (resultSet.next()) {
                            FinancialAccount account = new FinancialAccount();
                            account.setId(UUID.fromString(resultSet.getString(1)));
                            account.setName(resultSet.getString(2));
                            account.setInitialValue(resultSet.getInt(3));
                            account.setCustomOrder(resultSet.getInt(4));
                            account.setTotalValue(resultSet.getInt(5));
                            result.add(account);
                        }
                        return result;
                    }
                }.commit(this);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (result == null) {
            result = Collections.emptyList();
        }
        return result;
    }

    @Override
    public List<FinancialAccountDataDetail> listAccountDataDetail(Calendar begin, Calendar end) {
        List<FinancialAccountDataDetail> result = null;
        final UUID ownerId = this.getUserId();
        if (ownerId != null) {
            final String ownerString = ownerId.toString();
            final String beginString = this.timeFormatter.format(begin);
            final String endString = this.timeFormatter.format(end);
            try {
                result = new QueryHelper<List<FinancialAccountDataDetail>>() {

                    @Override
                    protected String getSqlString() {
                        return "SELECT detail_table.id AS id," + "       account_table.id AS account_id," + "       name AS account_name," + "       value," + "       comment," + "       time_happened," + "       time_logged," + "       class_id," + "       class_name," + "       subclass_id," + "       subclass_name " + "FROM   ( " + "  SELECT id, name, initial_value " + "  FROM   financial_account " + "  WHERE  owner = CAST( ? AS uuid) ) AS account_table JOIN ( ( " + "    SELECT id, account, value, comment, time_happened, time_logged, subclass " + "    FROM   financial_account_data " + "    WHERE  time_happened BETWEEN CAST( ? AS timestamp) AND CAST( ? AS timestamp) " + "                                                            ) AS data_table JOIN financial_class_detail ON ( data_table.subclass = financial_class_detail.subclass_id ) " + "                                                          ) AS detail_table ON ( account_table.id = detail_table.account) " + "ORDER BY time_happened DESC, time_logged DESC";
                    }

                    @Override
                    protected void preCommit(PreparedStatement stmt) throws SQLException {
                        stmt.setString(1, ownerString);
                        stmt.setString(2, beginString);
                        stmt.setString(3, endString);
                    }

                    @Override
                    protected List<FinancialAccountDataDetail> postCommit(PreparedStatement stmt) throws SQLException {
                        ResultSet resultSet = stmt.getResultSet();
                        List<FinancialAccountDataDetail> result = new ArrayList<FinancialAccountDataDetail>();
                        while (resultSet.next()) {
                            try {
                                FinancialAccountDataDetail detail = new FinancialAccountDataDetail();
                                detail.setId(UUID.fromString(resultSet.getString(1)));
                                detail.setAccount(UUID.fromString(resultSet.getString(2)));
                                detail.setAccountName(resultSet.getString(3));
                                detail.setValue(resultSet.getInt(4));
                                detail.setComment(resultSet.getString(5));
                                detail.setTimeHappened((Calendar) timeFormatter.parseObject(resultSet.getString(6)));
                                detail.setTimeLogged((Calendar) timeFormatter.parseObject(resultSet.getString(7)));
                                detail.setTheClass(UUID.fromString(resultSet.getString(8)));
                                detail.setTheClassName(resultSet.getString(9));
                                detail.setSubClass(UUID.fromString(resultSet.getString(10)));
                                detail.setSubClassName(resultSet.getString(11));
                                detail.setAccountOwner(ownerId);
                                result.add(detail);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        return result;
                    }
                }.commit(this);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (result == null) {
            result = Collections.emptyList();
        }
        return result;
    }

    @Override
    public boolean insertAccount(final String name, final int initialValue) {
        boolean result = false;
        final String owner = this.getUserIdString();
        if (owner != null && name != null) {
            final String id = UUID.randomUUID().toString();
            try {
                result = new UpdateHelper() {

                    @Override
                    protected String getSqlString() {
                        return "INSERT INTO financial_account " + "( id, owner, name, initial_value, total_value, custom_order) " + "VALUES ( CAST( ? AS uuid), CAST( ? AS uuid), ?, ?, ?, " + "COALESCE( ( SELECT MAX( custom_order) + 1 AS new_order FROM financial_account WHERE owner = CAST( ? AS uuid)), 0))";
                    }

                    @Override
                    protected void preCommit(PreparedStatement stmt) throws SQLException {
                        stmt.setString(1, id);
                        stmt.setString(2, owner);
                        stmt.setString(3, name);
                        stmt.setInt(4, initialValue);
                        stmt.setInt(5, initialValue);
                        stmt.setString(6, owner);
                    }
                }.commit(this) == 1;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean insertAccountData(FinancialAccountData data) {
        boolean result = false;
        if (data != null && data.getSubClass() != null) {
            final String id = data.getId().toString();
            final String account = data.getAccount().toString();
            final int value = data.getValue();
            final String comment = data.getComment();
            final String timeHappened = this.timeFormatter.format(data.getTimeHappened());
            final String timeLogged = this.timeFormatter.format(data.getTimeLogged());
            final String subClass = data.getSubClass().toString();
            try {
                result = new UpdateHelper() {

                    @Override
                    protected String getSqlString() {
                        return "INSERT INTO financial_account_data" + " ( id, account, value, comment, time_happened, time_logged, subclass)" + " VALUES ( CAST( ? AS uuid), CAST( ? AS uuid), ?, ?, CAST( ? AS timestamp), CAST( ? AS timestamp), CAST( ? AS uuid))";
                    }

                    @Override
                    protected void preCommit(PreparedStatement stmt) throws SQLException {
                        stmt.setString(1, id);
                        stmt.setString(2, account);
                        stmt.setInt(3, value);
                        stmt.setString(4, comment);
                        stmt.setString(5, timeHappened);
                        stmt.setString(6, timeLogged);
                        stmt.setString(7, subClass);
                    }
                }.commit(this) != 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean insertAccountTransfer(FinancialAccount from, FinancialAccount to, Calendar calendarHappened, Calendar calendarLogged, final int value, final int charge) {
        boolean result = false;
        if (from != null && to != null) {
            final int dataCount = charge == 0 ? 2 : 3;
            final String[] ids = new String[dataCount];
            for (int i = 0; i < dataCount; ++i) {
                ids[i] = UUID.randomUUID().toString();
            }
            final String accountFrom = from.getId().toString();
            final String accountTo = to.getId().toString();
            final String timeHappened = this.timeFormatter.format(calendarHappened);
            final String timeLogged = this.timeFormatter.format(calendarLogged);
            try {
                result = new BatchHelper() {

                    @Override
                    protected String getSqlString() {
                        return "INSERT INTO financial_account_data" + " ( id, account, value, comment, time_happened, time_logged, subclass)" + " VALUES ( CAST( ? AS uuid), CAST( ? AS uuid), ?, ?, CAST( ? AS timestamp), CAST( ? AS timestamp), CAST( ? AS uuid))";
                    }

                    @Override
                    protected void preCommit(PreparedStatement stmt) throws SQLException {
                        stmt.setString(1, ids[0]);
                        stmt.setString(2, accountFrom);
                        stmt.setInt(3, value);
                        stmt.setString(4, "Auto-generated data");
                        stmt.setString(5, timeHappened);
                        stmt.setString(6, timeLogged);
                        stmt.setString(7, ID_TRANSFER_FROM);
                        stmt.addBatch();
                        if (charge != 0) {
                            stmt.setString(1, ids[2]);
                            stmt.setString(2, accountFrom);
                            stmt.setInt(3, charge);
                            stmt.setString(4, "Auto-generated data");
                            stmt.setString(5, timeHappened);
                            stmt.setString(6, timeLogged);
                            stmt.setString(7, ID_TRANSFER_CHARGE);
                            stmt.addBatch();
                        }
                        stmt.setString(1, ids[1]);
                        stmt.setString(2, accountTo);
                        stmt.setInt(3, -value);
                        stmt.setString(4, "Auto-generated data");
                        stmt.setString(5, timeHappened);
                        stmt.setString(6, timeLogged);
                        stmt.setString(7, ID_TRANSFER_TO);
                        stmt.addBatch();
                    }
                }.commit(this) == dataCount;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean updateAccountName(final FinancialAccount account, final String newName) {
        boolean result = false;
        final String owner = this.getUserIdString();
        if (owner != null && account != null && account.getId() != null && newName != null) {
            try {
                final String id = account.getId().toString();
                result = new UpdateHelper() {

                    @Override
                    protected String getSqlString() {
                        return "UPDATE financial_account SET name = ? WHERE id = CAST( ? AS uuid) and owner = CAST( ? AS uuid)";
                    }

                    @Override
                    protected void preCommit(PreparedStatement stmt) throws SQLException {
                        stmt.setString(1, newName);
                        stmt.setString(2, id);
                        stmt.setString(3, owner);
                    }
                }.commit(this) == 1;
                if (result) {
                    account.setName(newName);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean updateAccountInitialValue(final FinancialAccount account, final int newInitialValue) {
        boolean result = false;
        final String owner = this.getUserIdString();
        if (owner != null && account != null && account.getId() != null) {
            try {
                final String id = account.getId().toString();
                result = new UpdateHelper() {

                    @Override
                    protected String getSqlString() {
                        return "UPDATE financial_account SET initial_value = ? WHERE id = CAST( ? AS uuid) and owner = CAST( ? AS uuid)";
                    }

                    @Override
                    protected void preCommit(PreparedStatement stmt) throws SQLException {
                        stmt.setInt(1, newInitialValue);
                        stmt.setString(2, id);
                        stmt.setString(3, owner);
                    }
                }.commit(this) == 1;
                if (result) {
                    int newTotalValue = account.getTotalValue() + newInitialValue - account.getInitialValue();
                    account.setInitialValue(newInitialValue);
                    account.setTotalValue(newTotalValue);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean updateAccount(final FinancialAccount account) {
        boolean result = false;
        final String owner = this.getUserIdString();
        if (owner != null && account != null && account.getId() != null) {
            try {
                final String id = account.getId().toString();
                result = new UpdateHelper() {

                    @Override
                    protected String getSqlString() {
                        return "UPDATE financial_account SET ( name, initial_value) = ( ?, ?) WHERE id = CAST( ? AS uuid) and owner = CAST( ? AS uuid)";
                    }

                    @Override
                    protected void preCommit(PreparedStatement stmt) throws SQLException {
                        stmt.setString(1, account.getName());
                        stmt.setInt(2, account.getInitialValue());
                        stmt.setString(3, id);
                        stmt.setString(4, owner);
                    }
                }.commit(this) == 1;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean updateAccountData(FinancialAccountData accountData) {
        boolean result = false;
        if (accountData != null) {
            try {
                final String id = accountData.getId().toString();
                final String account = accountData.getAccount().toString();
                final int value = accountData.getValue();
                final String comment = accountData.getComment();
                final String timeHappened = this.timeFormatter.format(accountData.getTimeHappened());
                final String timeLogged = this.timeFormatter.format(accountData.getTimeLogged());
                final String subClassId = accountData.getSubClass().toString();
                result = new UpdateHelper() {

                    @Override
                    protected String getSqlString() {
                        return "UPDATE financial_account_data SET" + "  (" + "    account," + "    value," + "    comment," + "    time_happened," + "    time_logged," + "    subclass" + "  ) = (" + "    CAST( ? AS uuid)," + "    ?," + "    ?," + "    CAST( ? AS timestamp)," + "    CAST( ? AS timestamp)," + "    CAST( ? AS uuid)" + "  ) WHERE id = CAST( ? AS uuid)";
                    }

                    @Override
                    protected void preCommit(PreparedStatement stmt) throws SQLException {
                        stmt.setString(1, account);
                        stmt.setInt(2, value);
                        stmt.setString(3, comment);
                        stmt.setString(4, timeHappened);
                        stmt.setString(5, timeLogged);
                        stmt.setString(6, subClassId);
                        stmt.setString(7, id);
                    }
                }.commit(this) == 1;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean deleteAccountData(final List<FinancialAccountData> dataDetails) {
        boolean result = false;
        if (dataDetails.isEmpty() == false) {
            final String sqlString;
            if (dataDetails.size() > 1) {
                StringBuilder sqlBuilder = new StringBuilder("DELETE FROM financial_account_data WHERE id IN ( CAST( ? AS uuid)");
                for (int i = dataDetails.size(); --i > 0; ) {
                    sqlBuilder.append(", CAST( ? AS uuid)");
                }
                sqlBuilder.append(")");
                sqlString = sqlBuilder.toString();
            } else {
                sqlString = "DELETE FROM financial_account_data WHERE id = CAST( ? AS uuid)";
            }
            try {
                result = new UpdateHelper() {

                    @Override
                    protected String getSqlString() {
                        return sqlString;
                    }

                    @Override
                    protected void preCommit(PreparedStatement stmt) throws SQLException {
                        for (int i = 0; i < dataDetails.size(); ++i) {
                            stmt.setString(i + 1, dataDetails.get(i).getId().toString());
                        }
                    }
                }.commit(this) > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
