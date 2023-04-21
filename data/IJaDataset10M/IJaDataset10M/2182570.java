package com.serie402.web.action;

import org.apache.log4j.Logger;
import com.kiss.fw.action.AbstractAction;
import com.kiss.fw.context.ActionContext;
import com.kiss.fw.exceptions.ActionException;

public class ClassifiedsAction extends AbstractAction {

    private static final Logger logger = Logger.getLogger(ClassifiedsAction.class);

    @Override
    public boolean validate(final ActionContext _context) {
        return true;
    }

    @Override
    public void create(ActionContext _context) throws ActionException {
    }

    @Override
    public void read(ActionContext _context) throws ActionException {
    }

    @Override
    public void update(ActionContext _context) throws ActionException {
    }

    @Override
    public void delete(ActionContext _context) throws ActionException {
    }

    @Override
    public void search(ActionContext _context) throws ActionException {
    }

    @Override
    public void browse(ActionContext _context) throws ActionException {
    }
}
