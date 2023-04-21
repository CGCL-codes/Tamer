package com.serie402.business.command.classifieds.jobs;

import org.apache.log4j.Logger;
import com.kiss.fw.command.AbstractCommand;
import com.kiss.fw.dto.AbstractDTO;
import com.kiss.fw.exceptions.CommandException;
import com.kiss.fw.exceptions.CommandInitializeException;
import com.serie402.business.dao.ClassifiedsDAO;
import com.serie402.business.dao.dto.ClassifiedsDAODTO;

public class SearchJobsCmd extends AbstractCommand {

    private static final Logger logger = Logger.getLogger(SearchJobsCmd.class);

    private static ClassifiedsDAO dao;

    @Override
    protected void initialize() throws CommandInitializeException {
        dao = ClassifiedsDAO.getInstance();
    }

    @Override
    protected void authorize(AbstractDTO _dto) throws SecurityException {
    }

    @Override
    protected void preProcess(AbstractDTO _dto) throws CommandException {
    }

    @Override
    protected void process(AbstractDTO _dto) throws CommandException {
        ClassifiedsDAODTO daoDto = new ClassifiedsDAODTO();
        try {
            dao.create(daoDto);
        } catch (Exception _exception) {
            handleException(logger, "process() - Error", _exception);
        }
    }

    @Override
    protected void postProcess(AbstractDTO _dto) throws CommandException {
    }
}
