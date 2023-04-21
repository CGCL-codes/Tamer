package de.jdufner.sudoku.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import de.jdufner.sudoku.common.board.Grid;
import de.jdufner.sudoku.common.board.SudokuSize;
import de.jdufner.sudoku.common.misc.Level;
import de.jdufner.sudoku.solver.service.Solution;

/**
 * @author <a href="mailto:jdufner@users.sf.net">J&uuml;rgen Dufner</a>
 * @since 0.1
 * @version $Revision: 116 $
 */
public final class SudokuDaoImpl implements SudokuDao {

    private static final Logger LOG = Logger.getLogger(SudokuDaoImpl.class);

    private HibernateTemplate hibernateTemplate;

    @Override
    public SudokuData deleteSudoku(int id) {
        SudokuData sudokuData = (SudokuData) hibernateTemplate.get(SudokuData.class, id);
        hibernateTemplate.delete(sudokuData);
        return sudokuData;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SudokuData> findSudokus(final int index, final int number) {
        hibernateTemplate.setFetchSize(number);
        return (List<SudokuData>) hibernateTemplate.executeFind(new HibernateCallback() {

            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery("from SudokuData");
                query.setFirstResult(index);
                query.setMaxResults(number);
                return query.list();
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SudokuData> findSudokus(SudokuSize size, Level level, int number, Boolean printed) {
        hibernateTemplate.setFetchSize(number);
        hibernateTemplate.setMaxResults(number);
        StringBuilder sb = new StringBuilder("from SudokuData s where s.size = ? and s.level = ? ");
        if (printed != null) {
            if (printed.booleanValue()) {
                sb.append("and s.printedAt is not null ");
            }
            if (!printed.booleanValue()) {
                sb.append("and s.printedAt is null ");
            }
        }
        sb.append("order by s.fixed asc, s.generatedAt asc");
        List<SudokuData> sudokuDataList = hibernateTemplate.find(sb.toString(), new Object[] { size.getHouseSize(), level.getValue() });
        return sudokuDataList;
    }

    @Override
    public SudokuData loadSudoku(int id) {
        return (SudokuData) hibernateTemplate.get(SudokuData.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Grid loadSudokuOfDay() {
        hibernateTemplate.setFetchSize(1);
        hibernateTemplate.setMaxResults(1);
        List<SudokuData> sudokus = hibernateTemplate.find("from SudokuData s where s.size = 9 order by s.generatedAt desc, s.level desc, s.fixed desc");
        return SudokuMapper.map(sudokus.get(0));
    }

    @Override
    public SudokuData saveSolution(Solution solution) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Save Solution");
        }
        SudokuData sudokuData = SudokuMapper.map(solution);
        updateTimestamp(sudokuData);
        Serializable serializable = hibernateTemplate.save(sudokuData);
        if (LOG.isInfoEnabled()) {
            LOG.info("Solution saved");
        }
        sudokuData.setId(((Integer) serializable).intValue());
        return sudokuData;
    }

    @Override
    public void update(Collection<SudokuData> collection) {
        for (SudokuData sudokuData : collection) {
            updateTimestamp(sudokuData);
        }
        hibernateTemplate.saveOrUpdateAll(collection);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void updatePrintedAt(int id, Date printedAt) {
        hibernateTemplate.setFetchSize(1);
        hibernateTemplate.setMaxResults(1);
        List<SudokuData> sudokus = hibernateTemplate.find("from SudokuData s where s.id = ?", new Object[] { id });
        if (sudokus.size() >= 1) {
            SudokuData sudokuData = sudokus.get(0);
            sudokuData.setPrintedAt(printedAt);
            updateTimestamp(sudokuData);
            hibernateTemplate.update(sudokuData);
        }
    }

    private void updateTimestamp(SudokuData sudokuData) {
        if (sudokuData.getCreated() == null) {
            sudokuData.setCreated(new Timestamp(new Date().getTime()));
        }
        sudokuData.setModified(new Timestamp(new Date().getTime()));
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        hibernateTemplate = new HibernateTemplate(sessionFactory);
    }
}
