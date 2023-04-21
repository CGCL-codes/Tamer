package com.googlecode.projecteleanor.expense;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.googlecode.projecteleanor.expense.dao.ExpenseDao;
import com.googlecode.projecteleanor.expense.dao.ExpenseCategoryDao;
import com.googlecode.projecteleanor.expense.dao.jpa.ExpenseDaoJpa;
import com.googlecode.projecteleanor.expense.dao.jpa.ExpenseCategoryDaoJpa;
import com.googlecode.projecteleanor.expense.service.ExpenseService;
import com.googlecode.projecteleanor.expense.service.impl.ExpenseServiceImpl;

/**
 * Create Date: 2009/8/13
 *
 * @author Alan She
 */
public class ExpenseModule {

    public static Module buildModule() {
        return Modules.combine(new AbstractModule() {

            protected void configure() {
                bind(ExpenseDao.class).to(ExpenseDaoJpa.class);
                bind(ExpenseCategoryDao.class).to(ExpenseCategoryDaoJpa.class);
                bind(ExpenseService.class).to(ExpenseServiceImpl.class);
            }
        });
    }
}
