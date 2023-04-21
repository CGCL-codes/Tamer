package net.admin4j.dao;

import java.util.Set;
import net.admin4j.entity.ExceptionInfo;

/**
 * Handles I/O for Exception Information
 * @author D. Ashmore
 *
 */
public interface ExceptionInfoDAO {

    public Set<ExceptionInfo> findAll();

    public void saveAll(Set<ExceptionInfo> exceptionList);
}
