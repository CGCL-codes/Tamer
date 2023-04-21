package org.rjs.sample.dao;

import java.util.List;
import org.rjs.sample.domain.Lecturer;

public interface LecturerDao {

    public Lecturer save(Lecturer course);

    public Lecturer delete(Lecturer course);

    public List<Lecturer> findAll();

    public Lecturer findById(Long id);
}
