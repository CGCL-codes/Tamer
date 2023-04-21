package hoplugins.tsforecast;

import java.sql.SQLException;
import java.util.ArrayList;
import plugins.IHOMiniModel;
import plugins.IMatchDetails;

public class LoepiCurve extends ForecastCurve {

    private TrainerCurve m_TrainerCurve = null;

    public LoepiCurve(IHOMiniModel ihominimodel, TrainerCurve t, boolean future) throws SQLException {
        super(ihominimodel, future);
        m_TrainerCurve = t;
    }

    protected double forecastUpdate(Curve.Point point1, Curve.Point point2) throws Exception {
        double dRet = point1.m_dSpirit;
        if (dRet >= m_dGeneralSpirit) {
            dRet *= 1.0D - (((dRet - m_dGeneralSpirit) / (m_TrainerCurve.getLeadership(point1.m_dDate) / 3D)) / 100D);
        } else {
            dRet *= 1.0D + (((dRet - m_dGeneralSpirit) * -1D) * (m_TrainerCurve.getLeadership(point1.m_dDate) / 2D) / 100D);
        }
        return dRet;
    }
}
