package net.algid.purchase.logic;

import java.sql.SQLException;
import java.util.List;
import org.eclipse.jface.dialogs.MessageDialog;
import net.algid.purchase.DAO.DAOFactory;
import net.algid.purchase.DAO.UnitDAO;
import net.algid.purchase.interfaces.IUnitLogic;
import net.algid.purchase.valueObject.Unit;

public class UnitLogic extends AbstractLogic implements IUnitLogic {

    private UnitDAO unitDAO = null;

    public UnitLogic(DAOFactory daoFactory) {
        super(daoFactory);
        try {
            unitDAO = daoFactory.getUnitDAO();
        } catch (SQLException e) {
            MessageDialog.openError(null, "������", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int add(Unit unit) {
        return unitDAO.add(unit);
    }

    @Override
    public List<Unit> getList(Unit unitFilter) {
        return unitDAO.getList(unitFilter);
    }

    @Override
    public void remove(int code) {
        unitDAO.remove(code);
    }

    @Override
    public void save(Unit unit) {
        unitDAO.save(unit);
    }

    @Override
    public Unit getObject(int code) {
        return unitDAO.getObject(code);
    }
}
