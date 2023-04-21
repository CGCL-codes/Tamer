package com.serie402.business.command.shopping.specials;

import org.apache.log4j.Logger;
import com.kiss.fw.command.AbstractCommand;
import com.kiss.fw.dto.AbstractDTO;
import com.kiss.fw.exceptions.CommandException;
import com.kiss.fw.exceptions.CommandInitializeException;
import com.serie402.business.dao.ShoppingDAO;
import com.serie402.business.dao.dto.ShoppingDAODTO;

public class DeleteSpecialCmd extends AbstractCommand {

    private static final Logger logger = Logger.getLogger(DeleteSpecialCmd.class);

    private static ShoppingDAO dao;

    @Override
    protected void initialize() throws CommandInitializeException {
        dao = ShoppingDAO.getInstance();
    }

    @Override
    protected void authorize(AbstractDTO _dto) throws SecurityException {
    }

    @Override
    protected void preProcess(AbstractDTO _dto) throws CommandException {
    }

    @Override
    protected void process(AbstractDTO _dto) throws CommandException {
        ShoppingDAODTO daoDto = new ShoppingDAODTO();
        try {
            dao.delete(daoDto);
        } catch (Exception _exception) {
            handleException(logger, "process() - Error", _exception);
        }
    }

    @Override
    protected void postProcess(AbstractDTO _dto) throws CommandException {
    }
}
