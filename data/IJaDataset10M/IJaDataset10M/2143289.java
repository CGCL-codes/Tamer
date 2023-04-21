package com.epam.mvc3.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.epam.mvc3.model.Tag;
import com.epam.mvc3.model.Topic;

@Repository
public interface TopicRepository extends CrudRepository<Topic, Long> {

    List<Topic> findAllByTopicTag(Tag currentTag);
}
