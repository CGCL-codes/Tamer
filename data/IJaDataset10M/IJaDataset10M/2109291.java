package net.sourceforge.solexatools.dao;

import net.sourceforge.solexatools.model.Processing;

public interface ProcessingDAO {

    public void insert(Processing processing);

    public void update(Processing processing);

    public Processing findByFilePath(String filePath);

    public Processing findByID(Integer id);
}
