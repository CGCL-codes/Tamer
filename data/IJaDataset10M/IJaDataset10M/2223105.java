package com.serie402.business.command.multimedia.photoalbum;

import org.apache.log4j.Logger;
import com.kiss.fw.command.AbstractCommand;
import com.kiss.fw.dto.AbstractDTO;
import com.kiss.fw.exceptions.CommandException;
import com.kiss.fw.exceptions.CommandInitializeException;
import com.serie402.business.dao.PhotoAlbumDAO;
import com.serie402.business.dao.dto.PhotoAlbumDAODTO;

public class ReadPhotoAlbumCmd extends AbstractCommand {

    private static final Logger logger = Logger.getLogger(ReadPhotoAlbumCmd.class);

    private static PhotoAlbumDAO dao;

    @Override
    protected void initialize() throws CommandInitializeException {
        dao = PhotoAlbumDAO.getInstance();
    }

    @Override
    protected void authorize(AbstractDTO _dto) throws SecurityException {
    }

    @Override
    protected void preProcess(AbstractDTO _dto) throws CommandException {
    }

    @Override
    protected void process(AbstractDTO _dto) throws CommandException {
        PhotoAlbumDAODTO daoDto = new PhotoAlbumDAODTO();
        try {
            dao.read(daoDto);
        } catch (Exception _exception) {
            handleException(logger, "process() - Error", _exception);
        }
    }

    @Override
    protected void postProcess(AbstractDTO _dto) throws CommandException {
    }
}
