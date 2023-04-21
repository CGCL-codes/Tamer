package database;

import constants.IDatabaseConstants;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import model.Indicator;
import model.MecoUser;
import model.Signal;
import model.SignalDefinition;
import model.StatisticalMethod;
import util.StringUtils;

public class SignalDB {

    private DBAccessPool dbAccess;

    private IndicatorDB indicatorDB = IndicatorDB.getInstance();

    public SignalDB(DBAccessPool dbAccess) {
        this.dbAccess = dbAccess;
    }

    @Deprecated
    public static SignalDB getInstance() {
        DBAccessPool dbAccess = new DBAccessPool(IDatabaseConstants.DB_IP_ADDRESS, IDatabaseConstants.DB_SCHEMA, "", IDatabaseConstants.DB_USERNAME, IDatabaseConstants.DB_PASSWORD, IDatabaseConstants.DB_DRIVER, IDatabaseConstants.DB_SERVER);
        SignalDB signalDB = new SignalDB(dbAccess);
        return signalDB;
    }

    /**
	 * 
	 * @param idSignal
	 * @param signalUser
	 * @return
	 */
    public Signal getSignal(String idSignal, MecoUser signalUser, boolean includeDocuments) {
        String query = "select * from Data.Signal where IdSignal = '" + idSignal + "'";
        PreparedStatement ps = null;
        Signal s = new Signal();
        try {
            ps = DBAccess.getInstance().getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                s.setIdSignal(idSignal);
                Indicator i = null;
                if (includeDocuments) {
                    i = indicatorDB.getIndicator(rs.getInt("IdIndicator"));
                } else {
                    i = indicatorDB.getIndicatorWithoutDocuments(rs.getInt("IdIndicator"));
                }
                SignalDefinition sd = SignalDefinitionDB.getInstance().getSignalDefinitionById(rs.getString("IdSignalDefinition"));
                if (sd != null) {
                    s.setSignalDefinition(sd);
                }
                sd.setStatisticalMethod(getStatisticalMethodBySignalDefinitionByID(sd.getIdSignalDefinition()));
                s.setIndicator(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBAccess.getInstance().closeConnection(ps);
        }
        return s;
    }

    /**
	 * @param idSignal
	 * @param signalUser
	 * @return
	 */
    public Signal getSignalByIndicatorId(int indicatorId) {
        String query = "select * from Data.Signal where IdIndicator = " + indicatorId;
        PreparedStatement ps = null;
        Signal signal = null;
        try {
            ps = DBAccess.getInstance().getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                signal = new Signal();
                signal.setIdSignal(rs.getString("IdSignal"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBAccess.getInstance().closeConnection(ps);
        }
        return signal;
    }

    /**
	 * Light method for the web service
	 * @param userId
	 * @param includeDocuments
	 * @return
	 */
    public List<Signal> getRecommendedUserSignal(String userId, String startDate, String endDate) {
        List<Signal> signalsFromRecommendedIndicators = new ArrayList<Signal>();
        signalsFromRecommendedIndicators = IndicatorDB.getInstance().getSignalIdsFromRecommendedIndicators(userId, startDate, endDate);
        return signalsFromRecommendedIndicators;
    }

    /**
	 * Light method for the web service
	 * @param userId
	 * @param includeDocuments
	 * @return
	 */
    public List<Signal> recommendedSignalsBySignalDefinition(String signalDefinitionId, String startDate, String endDate) {
        List<Signal> signalsFromRecommendedIndicators = new ArrayList<Signal>();
        signalsFromRecommendedIndicators = IndicatorDB.getInstance().getSignalIdsFromRecommendedIndicatorsBySignalDefinition(signalDefinitionId, startDate, endDate);
        return signalsFromRecommendedIndicators;
    }

    /**
	 * light method for web services it populate objects only with ids.
	 * @param userId
	 * @param includeDocuments
	 * @return
	 */
    public List<Signal> getSignalsByUser(String userId, boolean includeDocuments) {
        return this.getSignals(userId, new Date().toString(), new Date().toString());
    }

    /**
	 * @param signalDefinition
	 * @return
	 */
    public StatisticalMethod getStatisticalMethodBySignalDefinitionByID(String signalDefinitionID) {
        String query = "select * from [Data].[StatisticalMethod] where IdSignalDefinition = '" + signalDefinitionID + "'";
        PreparedStatement ps = null;
        StatisticalMethod statisticalMethod = null;
        try {
            ps = DBAccess.getInstance().getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                statisticalMethod = new StatisticalMethod();
                statisticalMethod.setName(rs.getString("name"));
                statisticalMethod.setIdSignalDefinition(rs.getString("IdSignalDefinition"));
                statisticalMethod.setNameTimeSeriesUnit(rs.getString("nameTimeSeriesUnit"));
                statisticalMethod.setTemplateId(rs.getInt("templateId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBAccess.getInstance().closeConnection(ps);
        }
        return statisticalMethod;
    }

    /**
	 * To avoid long retrievals we are restricting to 100 retrievals. To be improved by pagination.
	 */
    private static int MAX_RECOMMENDATION_SIZE = 100;

    /**
	    * light method for web services it populate objects only with ids.
     * @param userId
     * @param includeDocuments
     * @return
     */
    public List<Signal> getSignals(String userId, String startDate, String endDate) {
        Timestamp startDateTimeStamp = StringUtils.getTimeStampShortFormat(startDate);
        Timestamp endDateTimeStamp = StringUtils.getTimeStampShortFormat(endDate);
        PreparedStatement ps = null;
        List<Signal> signalsFromRecommendedIndicators = new ArrayList<Signal>();
        String query = null;
        try {
            query = "select distinct s.IdSignal, s.IdIndicator, s.IdSignalDefinition " + " from Data.Signal s, data.indicator i " + " where s.idindicator = i.idIndicator " + " and exists ( " + "select 1 " + "from IndicatorDocument id " + " where s.IdIndicator = id.IdIndicator) " + " and exists (select 1 from data.signaldefinition sd where sd.idSignalDefinition = s.IdSignalDefinition " + " and sd.GuidUser = ?)" + " ";
            if (startDate != null && endDate != null) {
                query += " and i.CreatedAt > '" + startDateTimeStamp + "' and  i.CreatedAt < '" + endDateTimeStamp + "'";
            }
            ps = dbAccess.prepareStatement(query);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                Signal signal = new Signal();
                signal.setIdSignal(rs.getString("IdSignal"));
                Indicator indicator = indicatorDB.getIndicatorWithoutDocuments(rs.getInt("IdIndicator"));
                indicator.setDocuments(DocumentDB.getInstance().getDocumentsWithGuidOnlyByIndicatorId(indicator.getId()));
                SignalDefinition signalDefinition = SignalDefinitionDB.getInstance().getSignalDefinitionById(rs.getString("IdSignalDefinition"));
                if (signalDefinition != null) {
                    signalDefinition.setStatisticalMethod(this.getStatisticalMethodBySignalDefinitionByID(signalDefinition.getIdSignalDefinition()));
                    signal.setIndicator(indicator);
                    signal.setSignalDefinition(signalDefinition);
                    signalsFromRecommendedIndicators.add(signal);
                    if (signalsFromRecommendedIndicators.size() > MAX_RECOMMENDATION_SIZE) {
                        break;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBAccess.getInstance().closeConnection(ps);
        }
        return signalsFromRecommendedIndicators;
    }

    /**
	 * Light method for web services it populate objects only with ids.
     * @param userId
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Signal> getSignalsBySignalDefinition(String signalDefinitionId, String startDate, String endDate) {
        Timestamp startDateTimeStamp = StringUtils.getTimeStampShortFormat(startDate);
        Timestamp endDateTimeStamp = StringUtils.getTimeStampShortFormat(endDate);
        PreparedStatement ps = null;
        List<Signal> signalsFromRecommendedIndicators = new ArrayList<Signal>();
        SignalDefinition signalDefinition = null;
        StatisticalMethod statisticalMethod = null;
        String query = null;
        try {
            if (startDate == null && endDate == null) {
                query = "select distinct IdSignal, IdIndicator, IdSignalDefinition from Data.Signal s where" + " exists ( select 1 from IndicatorDocument id where s.IdIndicator = id.IdIndicator) " + " and exists (select 1 from data.signaldefinition sd where sd.idSignalDefinition = s.IdSignalDefinition and s.idSignalDefinition = '" + signalDefinitionId + "' )";
            } else if (startDate != null && endDate != null) {
                query = "select distinct IdSignal, IdIndicator, IdSignalDefinition from Data.Signal s where" + " exists ( select 1 from IndicatorDocument id where s.IdIndicator = id.IdIndicator) " + " and exists (select 1 from data.signaldefinition sd where sd.idSignalDefinition = s.IdSignalDefinition " + " and sd.idSignalDefinition = '" + signalDefinitionId + "' and CreatedAt > '" + startDateTimeStamp + "' and  CreatedAt < '" + endDateTimeStamp + "')";
            }
            ps = DBAccess.getInstance().getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                Signal signal = new Signal();
                signal.setIdSignal(rs.getString("IdSignal"));
                Indicator indicator = new Indicator(rs.getInt("IdIndicator"));
                indicator.setDocuments(DocumentDB.getInstance().getDocumentsWithGuidOnlyByIndicatorId(indicator.getId()));
                if (signalDefinition == null) {
                    signalDefinition = SignalDefinitionDB.getInstance().getSignalDefinitionById(signalDefinitionId);
                    statisticalMethod = getStatisticalMethodBySignalDefinitionByID(signalDefinition.getIdSignalDefinition());
                }
                if (signalDefinition != null) {
                    signalDefinition.setStatisticalMethod(statisticalMethod);
                    signal.setIndicator(indicator);
                    signal.setSignalDefinition(signalDefinition);
                    signalsFromRecommendedIndicators.add(signal);
                    if (signalsFromRecommendedIndicators.size() > MAX_RECOMMENDATION_SIZE) {
                        break;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBAccess.getInstance().closeConnection(ps);
        }
        return signalsFromRecommendedIndicators;
    }

    /**
	 * Copy a signal to another, with a different target signal definition (i.e., changes the user)
	 */
    public void copySignal(Signal signalSource, SignalDefinition signalDefinitionTarget) {
        String sql = "insert into data.signal(IdSignal, IdIndicator, IdSignalDefinition) " + "   values ('" + UUID.randomUUID().toString() + "', " + signalSource.getIndicator().getIdIndicator() + ", " + "'" + signalDefinitionTarget.getIdSignalDefinition() + "')";
        PreparedStatement ps = dbAccess.prepareStatement(sql);
        dbAccess.ExecInsert(ps);
    }

    public int copySignalsFromSignalDefinitionToAnother(SignalDefinition signalDefinitionSource, SignalDefinition signalDefinitionTarget) {
        int nSignalsCopied = 0;
        String query = "select IdIndicator from Data.Signal s1 " + " where IdSignalDefinition = '" + signalDefinitionSource.getIdSignalDefinition() + "'" + " and not exists (" + " 	select 1 " + " 	from data.signal s2 " + "		where s2.IdIndicator = s1.IdIndicator " + "			and s2.IdSignalDefinition = '" + signalDefinitionTarget.getIdSignalDefinition() + "'" + "		) ";
        PreparedStatement ps = null;
        try {
            ps = dbAccess.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            List<Integer> listIdIndicators = new ArrayList<Integer>();
            while (rs != null && rs.next()) {
                listIdIndicators.add(rs.getInt("IdIndicator"));
            }
            for (Integer idIndicator : listIdIndicators) {
                System.out.println("Inserting signal of indicator id " + idIndicator + " and signal definition id '" + signalDefinitionTarget.getIdSignalDefinition() + " (User: " + signalDefinitionTarget.getUser().getName() + ")'...");
                String sql = "insert into data.signal(IdSignal, IdIndicator, IdSignalDefinition) " + "   values ('" + UUID.randomUUID().toString() + "', " + idIndicator + ", " + "'" + signalDefinitionTarget.getIdSignalDefinition() + "')";
                ps = dbAccess.prepareStatement(sql);
                dbAccess.ExecInsert(ps);
                nSignalsCopied++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBAccess.getInstance().closeConnection(ps);
        }
        return nSignalsCopied;
    }

    /**
	 * @param signalId
	 * @param userId
	 * @param rating
	 * @param dateString
	 * @return
	 */
    public boolean saveUserSignalRating(String signalId, String userId, int rating, String dateString) {
        boolean saved = false;
        if (!existSignal(signalId)) {
            String sql = "insert into [dbo].[UserSignalRating]([IdSignal],[GuidUser],[Rating],[DateRating]) VALUES" + "('" + signalId + "','" + userId + "','" + rating + "'," + new java.sql.Date(StringUtils.getTimeStamp(dateString).getTime()) + ")";
            PreparedStatement ps = dbAccess.prepareStatement(sql);
            dbAccess.ExecInsert(ps);
            saved = true;
        }
        return saved;
    }

    /**
     * @param id
     * @return
     */
    public boolean existSignal(String signalId) {
        boolean existSignal = false;
        PreparedStatement ps = null;
        try {
            String query = "select * from Data.Signal where IdSignal = '" + signalId + "'";
            ps = dbAccess.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                existSignal = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBAccess.getInstance().closeConnection(ps);
        }
        return existSignal;
    }

    public static boolean isSignalStored(String signalId) {
        boolean existSignal = false;
        PreparedStatement ps = null;
        try {
            String query = "select * from Data.Signal where IdSignal = '" + signalId + "'";
            ps = DBAccess.getInstance().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                existSignal = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBAccess.getInstance().closeConnection(ps);
        }
        return existSignal;
    }

    public String getSignalIdByIndicatorId(String userId, int idIndicator) {
        String sql = "select IdSignal " + " from Data.Signal s, data.signalDefinition sd" + " where s.IdSignalDefinition = sd.IdSignalDefinition " + "     and s.IdIndicator = ? " + "     and sd.GuidUser = ?";
        PreparedStatement ps = null;
        String signalId = "";
        try {
            ps = dbAccess.prepareStatement(sql);
            ps.setInt(1, idIndicator);
            ps.setString(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                signalId = rs.getString("signalId");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBAccess.getInstance().closeConnection(ps);
        }
        return signalId;
    }
}
