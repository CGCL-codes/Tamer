package net.sourceforge.workreporter.dao;

import java.util.List;
import net.sourceforge.workreporter.model.Expense;

/**
 * Expense Data Access Object
 * 
 * @author Flavio Donze <flavio.donze@gmail.com>
 * @version $Id: ExpenseDao.java 8 2008-06-11 22:34:03Z flaviodonze $
 */
public interface ExpenseDao {

    /**
	 * Stores an Expense
	 * @param expense
	 */
    Expense store(Expense expense);

    /**
	 * Finds an Expense with the id
	 * @param id
	 * @return
	 */
    Expense findByPrimaryKey(int id);

    /**
	 * Finds and returns all Expenses
	 * @return
	 */
    List<Expense> findAll();

    /**
	 * Removes a Expense from the database
	 * @param expense
	 */
    void delete(Expense expense);
}
