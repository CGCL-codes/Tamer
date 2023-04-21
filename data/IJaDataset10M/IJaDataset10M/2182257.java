package net.opensesam.bo;

import net.opensesam.entity.Tag;
import java.util.List;

public interface TagBo {

    void add(Tag tag);

    void delete(Tag tag);

    void update(Tag tag);

    List<Tag> findAll();

    String findAllAsString();

    Tag findById(int id);

    Tag findByName(String name);
}
